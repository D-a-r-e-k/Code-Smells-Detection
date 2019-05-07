/* CVS ID: $Id: SendMessage.java,v 1.2 2002/10/04 21:23:37 wastl Exp $ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;

import net.wastl.webmail.misc.*;
import net.wastl.webmail.config.ConfigurationListener;
import net.wastl.webmail.exceptions.*;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.mail.*;
import javax.mail.internet.*;


import javax.servlet.ServletException;

// Modified by exce, start
import org.bulbul.webmail.util.TranscodeUtil;
// Modified by exce, end

/*
 * SendMessage.java
 *
 * Created: Tue Sep  7 13:59:30 1999
 *
 * Copyright (C) 1999-2000 Sebastian Schaffert
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/**
 * Send a message and show a result page.
 * 
 * provides: message send
 * requires: composer
 *
 * @author Sebastian Schaffert
 * @version
 */

public class SendMessage implements Plugin, URLHandler, ConfigurationListener {
    
    public static final String VERSION="1.8";
    public static final String URL="/send";
    
    Storage store;

    WebMailServer parent;

    Session mailsession;

    public SendMessage() {
	
    }

    public void register(WebMailServer parent) {
	parent.getURLHandler().registerHandler(URL,this);
	parent.getConfigScheme().configRegisterStringKey(this,"SMTP HOST","localhost","Host used to send messages via SMTP. Should be localhost or your SMTP smarthost");
	parent.getConfigScheme().configRegisterYesNoKey(this,"ADVERTISEMENT ATTACH", "Attach advertisement from ADVERTISEMENT SIGNATURE PATH to each outgoing message");
	parent.getConfigScheme().setDefaultValue("ADVERTISEMENT ATTACH","NO");
	parent.getConfigScheme().configRegisterStringKey(this,"ADVERTISEMENT SIGNATURE PATH","advertisement.sig","Path to advertisement to attach to all outgoing messages (either absolute or relative to data directory)");
	this.store=parent.getStorage();
	this.parent=parent;

	init();
    }

    protected void init() {
	Properties props=new Properties();
	props.put("mail.host",store.getConfig("SMTP HOST"));
	props.put("mail.smtp.host",store.getConfig("SMTP HOST"));
	mailsession=Session.getInstance(props,null);
    } 

    public String getName() {
	return "SendMessage";
    }

    public String getDescription() {
	return "This URL-Handler sends a submitted message.";
    }

    public String getVersion() {
	return VERSION;
    }

    public String getURL() {
	return URL;
    }
    
    public void notifyConfigurationChange(String key) {
	init();
    }

    public HTMLDocument handleURL(String suburl, HTTPSession sess1, HTTPRequestHeader head) throws WebMailException, ServletException {
	if(sess1 == null) {
	    throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
	}
	WebMailSession session=(WebMailSession)sess1;
	UserData user=session.getUser();
	HTMLDocument content;

	// Modified by exce, start
	Locale locale = user.getPreferredLocale();
	// Modified by exce, end
	
	/* Save message in case there is an error */
	session.storeMessage(head);

	if(head.isContentSet("SEND")) {
	    /* The form was submitted, now we will send it ... */
	    try {
		
		
		MimeMessage msg=new MimeMessage(mailsession);		
		
		Address from[]=new Address[1];
		try {
		    // Modified by exce, start
		    /**
		     * Why we need 
		     * org.bulbul.util.TranscodeUtil.transcodeThenEncodeByLocale()?
		     *
		     * Because we specify client browser's encoding to UTF-8, IE seems
		     * to send all data encoded in UTF-8. We have to transcode all byte
		     * sequences we received to UTF-8, and next we encode those strings
		     * using MimeUtility.encodeText() depending on user's locale. Since
		     * MimeUtility.encodeText() is used to convert the strings into its
		     * transmission format, finally we can use the strings in the
		     * outgoing e-mail which relies on receiver's email agent to decode
		     * the strings.
		     *
		     * As described in JavaMail document, MimeUtility.encodeText() conforms
		     * to RFC2047 and as a result, we'll get strings like "=?Big5?B......".
		     */
		    /**
		     * Since data in session.getUser() is read from file, the encoding
		     * should be default encoding.
		     */
		    // from[0]=new InternetAddress(MimeUtility.encodeText(session.getUser().getEmail()),
		    // 			MimeUtility.encodeText(session.getUser().getFullName()));
		    from[0]=
			new InternetAddress(
					    TranscodeUtil.transcodeThenEncodeByLocale(session.getUser().getEmail(), null, locale),
					    TranscodeUtil.transcodeThenEncodeByLocale(session.getUser().getFullName(), null, locale)
					    );
		    // Modified by exce, end
		} catch(UnsupportedEncodingException e) {
		    store.log(Storage.LOG_WARN,
			      "Unsupported Encoding while trying to send message: "+e.getMessage());
		    from[0]=
			new InternetAddress(session.getUser().getEmail(),
					    session.getUser().getFullName());
		}
		
		StringTokenizer t;
		try {
		    // Modified by exce, start
		    /**
		     * Since data in session.getUser() is read from file, the encoding
		     * should be default encoding.
		     */
		    // t=new StringTokenizer(MimeUtility.encodeText(head.getContent("TO")).trim(),",");
		    t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("TO"), null, locale).trim(), ",");
		    // Modified by exce, end
		} catch(UnsupportedEncodingException e) {
		    store.log(Storage.LOG_WARN,
			      "Unsupported Encoding while trying to send message: "+e.getMessage());
		    t=new StringTokenizer(head.getContent("TO").trim(),",;");
		}

		/* Check To: field, when empty, throw an exception */
		if(t.countTokens()<1) {
		    throw new MessagingException("The recipient field must not be empty!");
		}
		Address to[]=new Address[t.countTokens()];
		int i=0;
		while(t.hasMoreTokens()) {
		    to[i]=new InternetAddress(t.nextToken().trim());
		    i++;
		}
		
		try {
		    // Modified by exce, start
		    /**
		     * Since data in session.getUser() is read from file, the encoding
		     * should be default encoding.
		     */
		    // t=new StringTokenizer(MimeUtility.encodeText(head.getContent("CC")).trim(),",");
		    t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("CC"), null, locale).trim(), ",");
		    // Modified by exce, end
		} catch(UnsupportedEncodingException e) {
		    store.log(Storage.LOG_WARN,
			      "Unsupported Encoding while trying to send message: "+e.getMessage());
		    t=new StringTokenizer(head.getContent("CC").trim(),",;");
		}
		Address cc[]=new Address[t.countTokens()];
		i=0;
		while(t.hasMoreTokens()) {
		    cc[i]=new InternetAddress(t.nextToken().trim());
		    i++;
		}
		
		try {
		    // Modified by exce, start
		    /**
		     * Since data in session.getUser() is read from file, the encoding
		     * should be default encoding.
		     */
		    // t=new StringTokenizer(MimeUtility.encodeText(head.getContent("BCC")).trim(),",");
		    t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("BCC"), null, locale).trim(), ",");
		    // Modified by exce, end
		} catch(UnsupportedEncodingException e) {
		    store.log(Storage.LOG_WARN,
			      "Unsupported Encoding while trying to send message: "+e.getMessage());
		    t=new StringTokenizer(head.getContent("BCC").trim(),",;");
		}
		Address bcc[]=new Address[t.countTokens()];
		i=0;
		while(t.hasMoreTokens()) {
		    bcc[i]=new InternetAddress(t.nextToken().trim());
		    i++;
		}
		
		session.setSent(false);


		msg.addFrom(from);
		if(to.length > 0) {
		    msg.addRecipients(Message.RecipientType.TO,to);
		}
		if(cc.length > 0) {
		    msg.addRecipients(Message.RecipientType.CC,cc);
		}
		if(bcc.length > 0) {
		    msg.addRecipients(Message.RecipientType.BCC,bcc);
		}
		msg.addHeader("X-Mailer",WebMailServer.getVersion()+", "+getName()+" plugin v"+getVersion());

		String subject = null;

		if(!head.isContentSet("SUBJECT")) {
		    subject="no subject";
		} else {
		    try {
			// Modified by exce, start
			// subject=MimeUtility.encodeText(head.getContent("SUBJECT"));
			subject = TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("SUBJECT"), "ISO8859_1", locale);
			// Modified by exce, end
		    } catch(UnsupportedEncodingException e) {
			store.log(Storage.LOG_WARN,"Unsupported Encoding while trying to send message: "+e.getMessage());
			subject=head.getContent("SUBJECT");
		    }	    
		}
				
		msg.addHeader("Subject",subject);

		if(head.isContentSet("REPLY-TO")) {
		    // Modified by exce, start
		    // msg.addHeader("Reply-To",head.getContent("REPLY-TO"));
		    msg.addHeader("Reply-To", TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("REPLY-TO"), "ISO8859_1", locale));
		    // Modified by exce, end
		}
		
		msg.setSentDate(new Date(System.currentTimeMillis()));
		
		String contnt=head.getContent("BODY");

		//String charset=MimeUtility.mimeCharset(MimeUtility.getDefaultJavaCharset());
		String charset="utf-8";

		MimeMultipart cont=new MimeMultipart();
		MimeBodyPart txt=new MimeBodyPart();

		// Transcode to UTF-8
		contnt = new String(contnt.getBytes("ISO8859_1"), "UTF-8");
		// Encode text
		if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
		    txt.setText(contnt, "Big5");
		    txt.setHeader("Content-Type","text/plain; charset=\"Big5\"");
		    txt.setHeader("Content-Transfer-Encoding", "quoted-printable"); // JavaMail defaults to QP?
		} else {
		    txt.setText(contnt,"utf-8");
		    txt.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
		    txt.setHeader("Content-Transfer-Encoding", "quoted-printable"); // JavaMail defaults to QP?
		}


		/* Add an advertisement if the administrator requested to do so */
		cont.addBodyPart(txt);
		if(store.getConfig("ADVERTISEMENT ATTACH").equals("YES")) {
		    MimeBodyPart adv=new MimeBodyPart();
		    String file="";
		    if(store.getConfig("ADVERTISEMENT SIGNATURE PATH").startsWith("/")) {
			file=store.getConfig("ADVERTISEMENT SIGNATURE PATH");
		    } else {
			file=parent.getProperty("webmail.data.path")+System.getProperty("file.separator")+
			    store.getConfig("ADVERTISEMENT SIGNATURE PATH");
		    }
		    String advcont="";
		    try {
			BufferedReader fin=new BufferedReader(new FileReader(file));
			String line=fin.readLine();
			while(line!= null && !line.equals("")) {
			    advcont+=line+"\n";
			    line=fin.readLine();
			}
			fin.close();
		    } catch(IOException ex) {}

		    /**
		     * Transcode to UTF-8; Since advcont comes from file, we transcode 
		     * it from default encoding.
		     */
		    // Encode text
		    if (locale.getLanguage().equals("zh") &&
			locale.getCountry().equals("TW")) {
			advcont = new String(advcont.getBytes(), "Big5");
			adv.setText(advcont,"Big5");
			adv.setHeader("Content-Type","text/plain; charset=\"Big5\"");
			adv.setHeader("Content-Transfer-Encoding", "quoted-printable"); 
		    } else {
			advcont = new String(advcont.getBytes(), "UTF-8");
		    	adv.setText(advcont,"utf-8");
		    	adv.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
		    	adv.setHeader("Content-Transfer-Encoding", "quoted-printable"); 
		    }			

		    cont.addBodyPart(adv);
		}
		Enumeration atts=session.getAttachments().keys();
		while(atts.hasMoreElements()) {
		    ByteStore bs=session.getAttachment((String)atts.nextElement());
		    InternetHeaders ih=new InternetHeaders();
		    ih.addHeader("Content-Type",bs.getContentType());
		    ih.addHeader("Content-Transfer-Encoding","BASE64");
		    
		    PipedInputStream pin=new PipedInputStream();
		    PipedOutputStream pout=new PipedOutputStream(pin);
		    
		    /* This is used to write to the Pipe asynchronously to avoid blocking */
		    StreamConnector sconn=new StreamConnector(pin,(int)(bs.getSize()*1.6)+1000);
		    BufferedOutputStream encoder=new BufferedOutputStream(MimeUtility.encode(pout,"BASE64"));
		    encoder.write(bs.getBytes());
		    encoder.flush();
		    encoder.close();
		    //MimeBodyPart att1=sconn.getResult();
		    MimeBodyPart att1=new MimeBodyPart(ih,sconn.getResult().getBytes());
		    
		    
		    att1.addHeader("Content-Type",bs.getContentType());
		    att1.setDescription(bs.getDescription(),"utf-8");
		    // Modified by exce, start
		    /**
		     * As described in FileAttacher.java line #95, now we need to 
		     * encode the attachment file name.
		     */
		    // att1.setFileName(bs.getName());
		    String fileName = bs.getName();
		    System.err.println("fileName: " + fileName);
		    if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
			fileName = MimeUtility.encodeText(fileName, "Big5", null);
		    }
		    att1.setFileName(fileName);
		    // Modified by exce, end
		    cont.addBodyPart(att1);
		}
		msg.setContent(cont);
		// 		}
		
		msg.saveChanges();

		boolean savesuccess=true;

		msg.setHeader("Message-ID",session.getUserModel().getWorkMessage().getAttribute("msgid"));
		if(session.getUser().wantsSaveSent()) {
		    String folderhash=session.getUser().getSentFolder();		    
		    try {
			Folder folder=session.getFolder(folderhash);
			Message[] m=new Message[1];
			m[0]=msg;
			folder.appendMessages(m);
		    } catch(MessagingException e) {
			savesuccess=false;
		    } catch(NullPointerException e) {
			// Invalid folder:
			savesuccess=false;
		    }
		}

		boolean sendsuccess=false;

		try {
		    Transport.send(msg);
		    Address sent[]=new Address[to.length+cc.length+bcc.length];
		    int c1=0;int c2=0;
		    for(c1=0;c1<to.length;c1++) {
			sent[c1]=to[c1];
		    }
		    for(c2=0;c2<cc.length;c2++) {
			sent[c1+c2]=cc[c2];
		    }
		    for(int c3=0;c3<bcc.length;c3++) {
			sent[c1+c2+c3]=bcc[c3];
		    }
		    sendsuccess=true;
		    throw new SendFailedException("success",new Exception("success"),sent,null,null);
		} catch(SendFailedException e) {
		    session.handleTransportException(e);
		}
		
		//session.clearMessage();

		content=new XHTMLDocument(session.getModel(),
					  store.getStylesheet("sendresult.xsl",
							      user.getPreferredLocale(),user.getTheme()));
		if(sendsuccess) session.clearWork();
	    } catch(Exception e) {
		e.printStackTrace();
		
		store.log(Storage.LOG_ERR,e);
		throw new DocumentNotFoundException("Could not send message. (Reason: "+e.getMessage()+")");
	    }
	    
	} else if(head.isContentSet("ATTACH")) {
	    /* Redirect request for attachment (unfortunately HTML forms are not flexible enough to 
	       have two targets without Javascript) */
	    content=parent.getURLHandler().handleURL("/compose/attach",session,head);
	} else {
	    throw new DocumentNotFoundException("Could not send message. (Reason: No content given)");
	}
	return content;
    }
    

    public String provides() {
	return "message send";
    }

    public String requires() {
	return "composer";
    }
} // SendMessage
