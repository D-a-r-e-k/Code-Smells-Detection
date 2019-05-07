// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.smtp;

import java.io.IOException;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import org.columba.api.command.IWorkerStatusController;
import org.columba.core.base.Blowfish;
import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.ProgressObservedInputStream;
import org.columba.core.gui.base.MultiLineLabel;
import org.columba.core.gui.frame.FrameManager;
import org.columba.mail.composer.SendableMessage;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.Identity;
import org.columba.mail.config.ImapItem;
import org.columba.mail.config.OutgoingItem;
import org.columba.mail.config.PopItem;
import org.columba.mail.config.SpecialFoldersItem;
import org.columba.mail.gui.util.PasswordDialog;
import org.columba.mail.pop3.POP3Store;
import org.columba.mail.util.AuthenticationManager;
import org.columba.mail.util.AuthenticationSecurityComparator;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.auth.AuthenticationException;
import org.columba.ristretto.auth.AuthenticationFactory;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.parser.ParserException;
import org.columba.ristretto.pop3.POP3Exception;
import org.columba.ristretto.smtp.SMTPException;
import org.columba.ristretto.smtp.SMTPProtocol;

/**
 * 
 * SMTPServer makes use of <class>SMTPProtocol </class> to add a higher
 * abstraction layer for sending messages.
 * 
 * It takes care of authentication all the details.
 * 
 * To send a message just create a <class>SendableMessage </class> object and
 * use <method>sendMessage </method>.
 * 
 * @author fdietz, Timo Stich <tstich@users.sourceforge.net>
 *  
 */
public class SMTPServer implements Observer  {

	private String[] capas;

	protected SMTPProtocol protocol;

	protected OutgoingItem smtpItem;
	
	protected Identity identity;

	protected String fromAddress;

	private boolean usingSSL;

	private AccountItem accountItem;

	/**
	 * Constructor for SMTPServer.
	 */
	public SMTPServer(AccountItem accountItem) {
		super();

		this.accountItem = accountItem;
		identity = accountItem.getIdentity();

		// initialise protocol layer
		smtpItem = accountItem.getSmtpItem();

		smtpItem.getRoot().addObserver(this);		
		protocol = new SMTPProtocol(smtpItem.get("host"), smtpItem.getInteger("port"));
	}

	private void ensureConnected() throws IOException, SMTPException, CommandCancelledException {
		if (protocol.getState() == SMTPProtocol.NOT_CONNECTED) {
			// Start login procedure
			protocol.openPort();

			initialize();
			
			doSSL();
		}
	}

	/**
	 * Open connection to SMTP server and login if needed.
	 * 
	 * @return true if connection was successful, false otherwise
	 */
	private void ensureAuthenticated() throws IOException, SMTPException,
			CommandCancelledException {
		String username;
		char[] password = new char[0];
		boolean savePassword;

		// Init Values
		// user's email address
		fromAddress = identity.getAddress().getMailAddress();

		// Sent Folder
		SpecialFoldersItem specialFoldersItem = accountItem
				.getSpecialFoldersItem();
		Integer i = new Integer(specialFoldersItem.get("sent"));
		int sentFolder = i.intValue();

		usingSSL = smtpItem.getBoolean("enable_ssl");
		int authMethod = getLoginMethod();

		boolean authenticated = (authMethod == AuthenticationManager.NONE);

		if (authMethod == AuthenticationManager.POP_BEFORE_SMTP) {
			// no esmtp - use POP3-before-SMTP instead
			try {
				pop3Authentification();
			} catch (POP3Exception e) {
				throw new SMTPException(e);
			}

			authenticated = true;
		}

		ensureConnected();


		if (!authenticated) {
			username = smtpItem.get("user");
			password = Blowfish.decrypt(smtpItem.getRoot().getAttribute("password", ""));
			savePassword = smtpItem.getBoolean("save_password");

			if (username.length() == 0) {
				// there seems to be no username set in the smtp-options
				//  -> use username from pop3 or imap options
				if (accountItem.isPopAccount()) {
					PopItem pop3Item = accountItem.getPopItem();
					username = pop3Item.get("user");
				} else {
					ImapItem imapItem = accountItem.getImapItem();
					username = imapItem.get("user");
				}
			}

			PasswordDialog passDialog = new PasswordDialog();

			// ask password from user
			if (password.length == 0) {
				passDialog.showDialog(MessageFormat.format(MailResourceLoader
						.getString("dialog", "password", "enter_password"),
						new Object[] { username,
								smtpItem.get("host") }),
						new String(password), savePassword);

				if (passDialog.success()) {
					password = passDialog.getPassword();
					savePassword = passDialog.getSave();
				} else {
					throw new CommandCancelledException();
				}
			}

			// try to authenticate
			while (!authenticated) {
				try {
					try {
						protocol.auth(AuthenticationManager
								.getSaslName(authMethod), username, password);
						authenticated = true;
					} catch (AuthenticationException e) {
						// If the cause is a SMTPExcpetion then only password
						// wrong
						// else bogus authentication mechanism
						if (e.getCause() instanceof SMTPException) {
							int errorCode = ((SMTPException) e.getCause()).getCode();
							
							// Authentication is not supported
							if( errorCode == 504 ) {
								//TODO: Add dialog to inform user that the smtp server
								// does not support authentication
								JOptionPane
								.showMessageDialog(
										FrameManager.getInstance()
												.getActiveFrame(),
										new MultiLineLabel(
												MailResourceLoader
																.getString(
																		"dialog",
																		"error",
																		"authentication_not_supported")),
										MailResourceLoader.getString("dialog",
												"error",
												"authentication_process_error"),
										JOptionPane.INFORMATION_MESSAGE);
								
								//Turn off authentication for the future
								smtpItem.setString("login_method", Integer
										.toString(AuthenticationManager.NONE));
								
								return;
							}
							
						} else {
							throw (SMTPException) e.getCause();
						} 

						// Some error in the client/server communication
						//  --> fall back to default login process
						int result = JOptionPane
								.showConfirmDialog(
										FrameManager.getInstance()
												.getActiveFrame(),
										new MultiLineLabel(
												e.getMessage()
														+ "\n"
														+ MailResourceLoader
																.getString(
																		"dialog",
																		"error",
																		"authentication_fallback_to_default")),
										MailResourceLoader.getString("dialog",
												"error",
												"authentication_process_error"),
										JOptionPane.OK_CANCEL_OPTION);

						if (result == JOptionPane.OK_OPTION) {
							authMethod = AuthenticationManager.SASL_PLAIN;
							smtpItem.setString("login_method", Integer
									.toString(authMethod));
						} else {
							throw new CommandCancelledException();
						}
						
					}
				} catch (SMTPException e) {
					passDialog.showDialog(MessageFormat.format(
							MailResourceLoader.getString("dialog", "password",
									"enter_password"), new Object[] { username,
									smtpItem.get("host") }), new String(
							password), savePassword);

					if (!passDialog.success()) {
						throw new CommandCancelledException();
					} else {
						password = passDialog.getPassword();
						savePassword = passDialog.getSave();
					}
				}
			}

			// authentication was successful
			// -> save name/password
			smtpItem.setString("user", username);
			smtpItem.setBoolean("save_password", savePassword);
			if (savePassword) {
				smtpItem.setString("password", Blowfish.encrypt(password));
			}
		}
	}

	/**
	 * @param smtpItem
	 * @throws CommandCancelledException
	 * @throws IOException
	 * @throws SMTPException
	 */
	private void doSSL() throws CommandCancelledException, IOException, SMTPException {
		if (smtpItem.getBoolean("enable_ssl")) {
			if (isSupported("STARTTLS")) {
				try {
					protocol.startTLS();
					usingSSL = true;
				} catch (Exception e) {
					Object[] options = new String[] {
							MailResourceLoader.getString("", "global", "ok")
									.replaceAll("&", ""),
							MailResourceLoader
									.getString("", "global", "cancel")
									.replaceAll("&", "") };

					int result = JOptionPane.showOptionDialog(null,
							MailResourceLoader.getString("dialog", "error",
									"ssl_handshake_error")
									+ ": "
									+ e.getLocalizedMessage()
									+ "\n"
									+ MailResourceLoader.getString("dialog",
											"error", "ssl_turn_off"),
							"Warning", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[0]);

					if (result == 1) {
						throw new CommandCancelledException();
					}

					// turn off SSL for the future
					smtpItem.setBoolean("enable_ssl", false);

					protocol.openPort();

					initialize();
				}
			} else {
				Object[] options = new String[] {
						MailResourceLoader.getString("", "global", "ok")
								.replaceAll("&", ""),
						MailResourceLoader.getString("", "global", "cancel")
								.replaceAll("&", "") };
				int result = JOptionPane.showOptionDialog(null,
						MailResourceLoader.getString("dialog", "error",
								"ssl_not_supported")
								+ "\n"
								+ MailResourceLoader.getString("dialog",
										"error", "ssl_turn_off"), "Warning",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);

				if (result == 1) {
					throw new CommandCancelledException();
				}

				// turn off SSL for the future
				smtpItem.setBoolean("enable_ssl", false);
			}
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private boolean isSupported(String string) {
		for (int i = 0; i < capas.length; i++) {
			if (capas[i].startsWith(string)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return
	 * @throws CommandCancelledException 
	 */
	public List checkSupportedAuthenticationMethods() throws IOException,
			SMTPException, CommandCancelledException {
		ensureConnected();

		List supportedMechanisms = new ArrayList();

		for (int i = 0; i < capas.length; i++) {
			if (capas[i].startsWith("AUTH")) {
				List authMechanisms = AuthenticationFactory.getInstance()
						.getSupportedMechanisms(capas[i]);
				Iterator it = authMechanisms.iterator();
				while (it.hasNext()) {
					supportedMechanisms.add(new Integer(AuthenticationManager
							.getSaslCode((String) it.next())));
				}

				break;
			}
		}

		if( supportedMechanisms.size() == 0) {
			// Add a default PLAIN login as fallback
			supportedMechanisms.add(new Integer(AuthenticationManager.SASL_PLAIN));
		}
		
		return supportedMechanisms;
	}

	private void initialize() throws IOException, SMTPException {
		try {
			capas = protocol.ehlo(InetAddress.getLocalHost());
		} catch (SMTPException e1) {
			// EHLO not supported -> AUTH not supported			
			if( protocol.getState() < SMTPProtocol.PLAIN ) {
				protocol.openPort();
			}
			protocol.helo(InetAddress.getLocalHost());
			capas = new String[] {};
		}
	}

	/**
	 * @param authType
	 * @return
	 * @throws CommandCancelledException 
	 */
	private int getLoginMethod() throws IOException, SMTPException, CommandCancelledException {
		String authType = accountItem.getSmtpItem().get("login_method");
		int method = 0;

		try {
			method = Integer.parseInt(authType);
		} catch (NumberFormatException e) {
			//Fallback to Securest Login method
		}

		if (method == 0) {
			List supported = checkSupportedAuthenticationMethods();

			if (accountItem.isPopAccount()) {
				supported
						.add(new Integer(AuthenticationManager.POP_BEFORE_SMTP));
			}

			if (supported.size() == 0) {
				// No Authentication available
				return AuthenticationManager.NONE;
			}

			if (usingSSL) {
				// NOTE if SSL is possible we just need the plain login
				// since SSL does the encryption for us.
				method = ((Integer) supported.get(0)).intValue();
			} else {
				Collections.sort(supported,
						new AuthenticationSecurityComparator());
				method = ((Integer) supported.get(supported.size() - 1))
						.intValue();
			}
		}

		return method;
	}

	/**
	 * 
	 * close the connection to the SMTP server
	 *  
	 */
	public void closeConnection() {
		// Close Port
		try {
			protocol.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * POP-before-SMTP authentication makes use of the POP3 authentication
	 * mechanism, before sending mail.
	 * 
	 * Basically you authenticate with the POP3 server, which allows you to use
	 * the SMTP server for sending mail for a specific amount of time.
	 * 
	 * @throws Exception
	 */
	protected void pop3Authentification() throws IOException, POP3Exception,
			CommandCancelledException {
		POP3Store.doPOPbeforeSMTP(accountItem.getPopItem());
	}

	/**
	 * Send a message
	 * 
	 * For an complete example of creating a <class>SendableMessage </class>
	 * object see <class>MessageComposer </class>
	 * 
	 * @param message
	 * @param workerStatusController
	 * @throws Exception
	 */
	public void sendMessage(SendableMessage message,
			IWorkerStatusController workerStatusController)
			throws SMTPException, IOException, CommandCancelledException {
		ensureAuthenticated();
		
		// send from address and recipient list to SMTP server
		// ->all addresses have to be normalized
		protocol.mail(identity.getAddress());

		Iterator recipients = message.getRecipients().iterator();

		while (recipients.hasNext()) {
			try {
				protocol.rcpt(Address.parse((String) recipients.next()));
			} catch (ParserException e1) {
				throw new SMTPException(e1);
			}
		}

		// now send message source
		protocol.data(new ProgressObservedInputStream(
				message.getSourceStream(), workerStatusController));
	}

	public String getName() {
		OutgoingItem smtpItem = accountItem.getSmtpItem();
		String host = smtpItem.get("host");

		return host;
	}
	
	public void dropConnection() throws IOException {
		protocol.dropConnection();
	}

	public void update(Observable o, Object arg) {
		protocol = new SMTPProtocol(smtpItem.get("host"), smtpItem.getInteger("port"));
	}
}