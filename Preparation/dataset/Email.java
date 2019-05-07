package org.lnicholls.galleon.apps.email;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.awt.Color;
import java.io.*;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.lnicholls.galleon.app.AppContext;
import org.lnicholls.galleon.app.AppFactory;
import org.lnicholls.galleon.app.AppConfigurationPanel.ComboWrapper;
import org.lnicholls.galleon.apps.email.EmailConfiguration.Account;
import org.lnicholls.galleon.server.Server;
import org.lnicholls.galleon.util.NameValue;
import org.lnicholls.galleon.util.ReloadCallback;
import org.lnicholls.galleon.util.ReloadTask;
import org.lnicholls.galleon.util.Tools;
import org.lnicholls.galleon.widget.DefaultApplication;
import org.lnicholls.galleon.widget.DefaultMenuScreen;
import org.lnicholls.galleon.widget.DefaultOptionsScreen;
import org.lnicholls.galleon.widget.DefaultScreen;
import org.lnicholls.galleon.widget.OptionsButton;
import org.lnicholls.galleon.widget.ScrollText;
import org.lnicholls.galleon.widget.DefaultApplication.Tracker;
import org.lnicholls.galleon.widget.DefaultApplication.VersionScreen;

import com.tivo.hme.bananas.BButton;
import com.tivo.hme.bananas.BEvent;
import com.tivo.hme.bananas.BList;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.interfaces.IHttpRequest;
import com.tivo.hme.sdk.IHmeProtocol;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.interfaces.IContext;
import com.tivo.hme.interfaces.IArgumentList;

import de.nava.informa.core.ItemIF;

public class Email extends DefaultApplication {

    private static Logger log = Logger.getLogger(Email.class.getName());

    public final static String TITLE = "Email";

    private Resource mMenuBackground;

    private Resource mInfoBackground;

    private Resource mViewerBackground;

    private Resource mFolderIcon;

    private Resource mItemIcon;

    public void init(IContext context) throws Exception {
        super.init(context);

        mMenuBackground = getSkinImage("menu", "background");
        mInfoBackground = getSkinImage("info", "background");
        mViewerBackground = getSkinImage("viewer", "background");
        mFolderIcon = getSkinImage("menu", "folder");
        mItemIcon = getSkinImage("menu", "item");

        EmailConfiguration emailConfiguration = (EmailConfiguration) ((EmailFactory) getFactory())
                .getAppContext().getConfiguration();

        if (emailConfiguration.getAccounts().size() == 1) {
            Account account = (Account) emailConfiguration.getAccounts().get(0);
            List mail = (List) ((EmailFactory) getFactory()).mAccounts.get(account.getName());
            push(new EmailAccountMenuScreen(this, account, mail, true), TRANSITION_NONE);
        } else
            push(new EmailMenuScreen(this), TRANSITION_NONE);
        
        initialize();
    }

    public class EmailMenuScreen extends DefaultMenuScreen {
        public EmailMenuScreen(Email app) {
            super(app, "Email");
            
            setFooter("Press ENTER for options");

            getBelow().setResource(mMenuBackground);

            EmailConfiguration emailConfiguration = (EmailConfiguration) ((EmailFactory) getFactory())
                    .getAppContext().getConfiguration();
            List accounts = emailConfiguration.getAccounts();
            Account[] feedArray = (Account[]) accounts.toArray(new Account[0]);
            Arrays.sort(feedArray, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Account account1 = (Account) o1;
                    Account account2 = (Account) o2;

                    return -account1.getName().compareTo(account2.getName());
                }
            });

            for (int i = 0; i < feedArray.length; i++) {
                Account account = (Account) feedArray[i];
                mMenuList.add(account);
            }
        }

        public boolean handleAction(BView view, Object action) {
            if (action.equals("push")) {
                if (mMenuList.size() > 0) {
                    load();
                    Account account = (Account) mMenuList.get(mMenuList.getFocus());

                    List stories = (List) ((EmailFactory) getFactory()).mAccounts.get(account.getName());
                    EmailAccountMenuScreen emailAccountMenuScreen = new EmailAccountMenuScreen((Email) getBApp(),
                            account, stories);
                    getBApp().push(emailAccountMenuScreen, TRANSITION_LEFT);
                    getBApp().flush();
                    return true;
                }
            }
            return super.handleAction(view, action);
        }

        protected void createRow(BView parent, int index) {
            BView icon = new BView(parent, 10, 3, 30, 30);
            icon.setResource(mFolderIcon);

            Account account = (Account) mMenuList.get(index);
            BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
            name.setShadow(true);
            name.setFlags(RSRC_HALIGN_LEFT);
            name.setValue(Tools.trim(account.getName(), 40));
        }
        
		public boolean handleKeyPress(int code, long rawcode) {
			switch (code) {
			case KEY_ENTER:
				getBApp().push(new OptionsScreen((Email) getBApp()), TRANSITION_LEFT);
			}

			return super.handleKeyPress(code, rawcode);
		}        
    }
    
	public class OptionsScreen extends DefaultOptionsScreen {

		public OptionsScreen(DefaultApplication app) {
			super(app);

			getBelow().setResource(mInfoBackground);

			EmailConfiguration emailConfiguration = (EmailConfiguration) ((EmailFactory) getFactory()).getAppContext().getConfiguration();

			int start = TOP;
			int width = 280;
			int increment = 37;
			int height = 25;
			BText text = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			text.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_CENTER);
			text.setFont("default-24-bold.font");
			text.setShadow(true);
			text.setValue("Reload");

			NameValue[] nameValues = new NameValue[] { new NameValue("5 minutes", "5"),
					new NameValue("10 minutes", "10"), new NameValue("20 minutes", "20"),
					new NameValue("30 minutes", "30"), new NameValue("1 hour", "60"), new NameValue("2 hours", "120"),
					new NameValue("4 hours", "240"), new NameValue("6 hours", "720"),
					new NameValue("10 minutes", "10"), new NameValue("24 hours", "1440") };
			mReloadButton = new OptionsButton(getNormal(), BORDER_LEFT + BODY_WIDTH - width, start, width, height,
					true, nameValues, String.valueOf(emailConfiguration.getReload()));
			
			start = start + increment;
			
			text = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
			text.setFlags(RSRC_HALIGN_LEFT | RSRC_TEXT_WRAP | RSRC_VALIGN_CENTER);
			text.setFont("default-24-bold.font");
			text.setShadow(true);
			text.setValue("Download Limit");

			nameValues = new NameValue[] { new NameValue("Unlimited", "-1"),
					new NameValue("1", "1"), new NameValue("2", "2"),
					new NameValue("3", "3"), new NameValue("4", "4"), new NameValue("5", "5"),
					new NameValue("10", "10"), new NameValue("20", "20"),
					new NameValue("40", "40"), new NameValue("60", "60") };
			mLimitButton = new OptionsButton(getNormal(), BORDER_LEFT + BODY_WIDTH - width, start, width, height,
					true, nameValues, String.valueOf(emailConfiguration.getLimit()));			
			
			setFocusDefault(mReloadButton);
		}

		public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);

			return super.handleEnter(arg, isReturn);
		}

		public boolean handleExit() {

			try {
				DefaultApplication application = (DefaultApplication)getApp();
				if (!application.isDemoMode())
				{
					EmailConfiguration emailConfiguration = (EmailConfiguration) ((EmailFactory) getFactory()).getAppContext().getConfiguration();
					emailConfiguration.setReload(Integer.parseInt(mReloadButton.getValue()));
					emailConfiguration.setLimit(Integer.parseInt(mLimitButton.getValue()));
	
					Server.getServer().updateApp(((EmailFactory) getFactory()).getAppContext());
				}
			} catch (Exception ex) {
				Tools.logException(Email.class, ex, "Could not configure email app");
			}
			return super.handleExit();
		}

		private OptionsButton mReloadButton;
		
		private OptionsButton mLimitButton;
	}    

    public class EmailAccountMenuScreen extends DefaultMenuScreen {

        public EmailAccountMenuScreen(Email app, Account account, List list) {
            this(app, account, list, false);
        }

        public EmailAccountMenuScreen(Email app, Account account, List list, boolean first) {
            super(app, null);
            
            setFooter("Press ENTER for options");

            mList = list;
            mFirst = first;

            getBelow().setResource(mMenuBackground);

            EmailConfiguration emailConfiguration = (EmailConfiguration) ((EmailFactory) getFactory())
                    .getAppContext().getConfiguration();

            setTitle(account.getName());
            
            if (!account.valid())
            {
	            BText countText = new BText(getNormal(), BORDER_LEFT, TOP - 25, BODY_WIDTH, 20);
	            countText.setFlags(IHmeProtocol.RSRC_HALIGN_CENTER);
	            countText.setFont("default-18.font");
	            countText.setColor(Color.RED);
	            countText.setShadow(true);
	            countText.setValue("Mail server error");
            }

            // Sort by date
            EmailItem[] itemArray = (EmailItem[]) list.toArray(new EmailItem[0]);
            Arrays.sort(itemArray, new Comparator() {
                public int compare(Object o1, Object o2) {
                    EmailItem emailItem1 = (EmailItem) o1;
                    EmailItem emailItem2 = (EmailItem) o2;

                    return -emailItem1.getDate().compareTo(emailItem2.getDate());
                }
            });

            mList = new ArrayList();
            for (int i = 0; i < itemArray.length; i++) {
                EmailItem item = (EmailItem) itemArray[i];
                mMenuList.add(item);
                mList.add(item);
            }
        }

        public boolean handleAction(BView view, Object action) {
            if (action.equals("push")) {
                if (mMenuList.size() > 0) {
                    load();
                    EmailItem item = (EmailItem) mMenuList.get(mMenuList.getFocus());

                    Tracker tracker = new Tracker(mList, mMenuList.getFocus());
                    
                    EmailScreen rssScreen = new EmailScreen((Email) getBApp(), tracker);
                    getBApp().push(rssScreen, TRANSITION_LEFT);
                    getBApp().flush();
                    return true;
                }
            }
            return super.handleAction(view, action);
        }

        protected void createRow(BView parent, int index) {
            BView icon = new BView(parent, 10, 3, 30, 30);
            icon.setResource(mItemIcon);

            EmailItem item = (EmailItem) mMenuList.get(index);
            BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
            name.setShadow(true);
            name.setFlags(RSRC_HALIGN_LEFT);
            name.setValue(Tools.trim(cleanHTML(item.getSubject()), 40));
        }

        public boolean handleKeyPress(int code, long rawcode) {
            switch (code) {
            case KEY_LEFT:
                if (!mFirst) {
                    postEvent(new BEvent.Action(this, "pop"));
                    return true;
                }
                break;
            case KEY_ENTER:
				getBApp().push(new OptionsScreen((Email) getBApp()), TRANSITION_LEFT);
				return true;
            }
            return super.handleKeyPress(code, rawcode);
        }

        private BView mImage;

        private boolean mFirst;
        
        private List mList;
    }

    public class EmailScreen extends DefaultScreen {

        public EmailScreen(Email app, Tracker tracker) {
            super(app, true);
            
            mTracker = tracker;
            
            getBelow().setResource(mInfoBackground);

            int start = BORDER_TOP;

            mFromText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 30);
            mFromText.setFlags(RSRC_HALIGN_LEFT);
            mFromText.setFont("default-24-bold.font");
            mFromText.setColor(Color.GREEN);
            mFromText.setShadow(Color.black, 2);

            start += 30;

            mDateFormat = new SimpleDateFormat();
            mDateFormat.applyPattern("EEE M/dd H:mm");

            mDateText = new BText(getNormal(), BORDER_LEFT, start, BODY_WIDTH, 20);
            mDateText.setFlags(RSRC_HALIGN_LEFT);
            mDateText.setFont("default-18-bold.font");
            mDateText.setColor(Color.GREEN);
            mDateText.setShadow(true);

            start += 25;

            mScrollText = new ScrollText(getNormal(), BORDER_LEFT, start, BODY_WIDTH - 25, getHeight() - 2
                    * SAFE_TITLE_V - 193, "");

            /*
             * mList = new DefaultOptionList(this.getNormal(), SAFE_TITLE_H, (getHeight() - SAFE_TITLE_V) - 40, (width -
             * (SAFE_TITLE_H * 2)) / 2, 90, 35); mList.add("Back to menu"); setFocusDefault(mList);
             */

            BButton button = new BButton(getNormal(), SAFE_TITLE_H + 10, (getHeight() - SAFE_TITLE_V) - 40, (int) Math
                    .round((getWidth() - (SAFE_TITLE_H * 2)) / 2.5), 35);
            button.setResource(createText("default-24.font", Color.white, "Return to menu"));
            button.setBarAndArrows(BAR_HANG, BAR_DEFAULT, "pop", null, null, null, true);
            setFocus(button);
        }
        
        public boolean handleEnter(java.lang.Object arg, boolean isReturn) {
			getBelow().setResource(mInfoBackground);
			updateView();

			return super.handleEnter(arg, isReturn);
		}
        
        private void updateView() {
        	EmailItem item = (EmailItem) mTracker.getList().get(mTracker.getPos());
        	
        	setSmallTitle(item.getSubject());
        	mFromText.setValue("From: " + item.getFrom());
        	mDateText.setValue("Date: " + mDateFormat.format(item.getDate()));
        	mScrollText.setText(cleanHTML(item.getBody()));
        }        

        public boolean handleKeyPress(int code, long rawcode) {
            switch (code) {
            case KEY_SELECT:
                postEvent(new BEvent.Action(this, "pop"));
                return true;
            case KEY_CHANNELUP:
				getBApp().play("pageup.snd");
				getBApp().flush();
				getPrevPos();
				updateView();
				return true;
			case KEY_CHANNELDOWN:
				getBApp().play("pagedown.snd");
				getBApp().flush();
				getNextPos();
				updateView();
				return true;            
            case KEY_UP:
            case KEY_DOWN:
                return mScrollText.handleKeyPress(code, rawcode);
            }
            return super.handleKeyPress(code, rawcode);
        }
        
        public void getNextPos() {
			if (mTracker != null) {
				int pos = mTracker.getNextPos();
			}
		}

		public void getPrevPos() {
			if (mTracker != null) {
				int pos = mTracker.getPrevPos();
			}
		}   
		
		private BText mFromText;
		
		private BText mDateText;
		
		private SimpleDateFormat mDateFormat;

        private BList mList;

        private ScrollText mScrollText;
        
        private Tracker mTracker;
    }

    private static class EmailItem {
        public EmailItem(String subject, String from, String body, Date date) {
            mSubject = subject;
            mFrom = from;
            mBody = body;
            mDate = date;
        }

        public String getSubject() {
            return mSubject;
        }

        public String getFrom() {
            return mFrom;
        }

        public String getBody() {
            return mBody;
        }

        public Date getDate() {
            return mDate;
        }

        private String mSubject = "";

        private String mFrom;

        private String mBody;

        private Date mDate;
    }

    public static class EmailFactory extends AppFactory {

        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        public void updateAppContext(AppContext appContext) {
            super.updateAppContext(appContext);

            updateAccounts();
        }

        private void updateAccounts() {
            final EmailConfiguration emailConfiguration = (EmailConfiguration) getAppContext().getConfiguration();

            new Thread() {
                public void run() {
                    Iterator iterator = emailConfiguration.getAccounts().iterator();
                    while (iterator.hasNext()) {
                        Account account = (Account) iterator.next();
                        List mail = (List) mAccounts.get(account.getName());
                        if (mail == null) {
                            mail = new ArrayList();
                            mAccounts.put(account.getName(), mail);
                        }
                        try {
                            Properties props = new Properties();

                            if (account.getProtocol().equals("pop3s")) {
                                props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
                                props.setProperty("mail.pop3.socketFactory.fallback", "false");
                                props.setProperty("mail.pop3.port", "995");
                                props.setProperty("mail.pop3.socketFactory.port", "995");
                            } else if (account.getProtocol().equals("imaps")) {
                                props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
                                props.setProperty("mail.imap.socketFactory.fallback", "true");
                                props.setProperty("mail.imap.port", "993");
                                props.setProperty("mail.imap.socketFactory.port", "993");
                            }

                            Session session = Session.getDefaultInstance(props, null);
                            Store store = session.getStore(account.getProtocol());
                            store.connect(account.getServer(), Tools.decrypt(account.getUsername()), Tools
                                    .decrypt(account.getPassword()));
                            // TODO Make this configurable too?
                            Folder folder = store.getFolder("INBOX");
                            folder.open(Folder.READ_ONLY);

                            // Get stats
                            int countEmail = folder.getMessageCount();
                            // TODO Should we look at this first?
                            int newCount = folder.getNewMessageCount();
                            int unreadCount = folder.getUnreadMessageCount();

                            mail.clear();

                            int count = 0;
                            for (int i = countEmail; i > 0; i--) {
                                boolean unread = false;

                                Message message = folder.getMessage(i);
                                String[] statusHeader = message.getHeader("Status");

                                count = count + 1;
                                
                                if (emailConfiguration.getLimit() > 0 && count > emailConfiguration.getLimit())
                                	break;

                                if (statusHeader != null && statusHeader.length > 0) {
                                    if (statusHeader[0].equals("")) {
                                        // New message
                                        unread = true;
                                    } else if (statusHeader[0].equals("O")) {
                                        // Unread message
                                        unread = true;
                                    }
                                } else {
                                    if (message.isSet(Flags.Flag.RECENT)) {
                                        // New message
                                        unread = true;
                                    } else if (!message.isSet(Flags.Flag.SEEN)) {
                                        // Unread message
                                        unread = true;
                                    }
                                }

                                if (unread) {
                                    String title = message.getSubject() == null ? "none" : message.getSubject();
                                    String from = "";
                                    String description = "";
                                    Address[] address;
                                    if ((address = message.getFrom()) != null) {
                                        if (address.length > 0) {
                                            from = address[0].toString();
                                        }
                                    }
                                    if (message.isMimeType("text/plain")) {
                                        description = (String) message.getContent();
                                    } 
                                    else 
                                	if (message.isMimeType("text/html")) {
                                        description = Tools.cleanHTML((String) message.getContent());
                                    } 
                                    else                                    	
                                	if (message.isMimeType("multipart/*")) {
	                                    Multipart mp = (Multipart) message.getContent();
	                                    for (int p = 0; p < mp.getCount(); p++) {
	                                        Part part = mp.getBodyPart(p);
	                                        if (part.isMimeType("text/plain")) {
	                                            description = (String) part.getContent();
	                                            break;
	                                        }
	                                        else
                                        	if (part.isMimeType("text/html")) {
	                                            description = Tools.cleanHTML((String) part.getContent());
	                                            break;
	                                        }	                                        	
	                                    }
                                    }
                                    EmailItem emailItem = new EmailItem(message.getSubject(), from, description,
                                            message.getSentDate());
                                    mail.add(emailItem);
                                }
                            }
                            folder.close(false);
                            store.close();
                            
                            account.setValid(true);
                        } catch (Exception ex) {
                        	account.setValid(false);
                        	Tools.logException(Email.class, ex, "Could not reload email");
                        }
                    }
                }
            }.start();
        }

        public void initialize() {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Security.setProperty("ssl.SocketFactory.provider", "org.lnicholls.galleon.util.DummySSLSocketFactory");

            EmailConfiguration emailConfiguration = (EmailConfiguration) getAppContext().getConfiguration();
            Server.getServer().scheduleShortTerm(new ReloadTask(new ReloadCallback() {
                public void reload() {
                    try {
                    	updateAccounts();
                    } catch (Exception ex) {
                        log.error("Could not download email", ex);
                    }
                }
            }), emailConfiguration.getReload());
        }

        public InputStream getStream(String uri) throws IOException {
        	if (uri.toLowerCase().equals("icon.png")) {
        		EmailConfiguration emailConfiguration = (EmailConfiguration) getAppContext().getConfiguration();
        		boolean hasMail = false;
                Iterator iterator = emailConfiguration.getAccounts().iterator();
                while (iterator.hasNext()) {
                    Account account = (Account) iterator.next();
                    List mail = (List) mAccounts.get(account.getName());
                    if (mail != null && mail.size() > 0) {
                        hasMail = true;
                        break;
                    }
                }

                if (hasMail)  
        			return super.getStream("alerticon.png");
            }

            return super.getStream(uri);
        }

        private static Hashtable mAccounts = new Hashtable();
    }

    private static String cleanHTML(String data) {
        String result = "";
        data = data.replaceAll("\n", " ");
        int pos1 = data.indexOf("<");
        if (pos1 != -1) {
            while (pos1 != -1) {
                int pos2 = data.indexOf(">");
                if (pos2 == -1) {
                    result = result + data;
                    break;
                }
                result = result + data.substring(0, pos1);
                data = data.substring(pos2 + 1);
                pos1 = data.indexOf("<");
            }
        } else
            result = data;
        return StringEscapeUtils.unescapeHtml(result);
    }
}