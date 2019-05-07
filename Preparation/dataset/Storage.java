/* CVS ID: $Id: Storage.java,v 1.1.1.1 2002/10/02 18:42:51 wastl Exp $ */
package net.wastl.webmail.server;

import net.wastl.webmail.config.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.exceptions.*;
import java.io.*;
import java.util.*;

import javax.xml.transform.Templates;
import javax.xml.parsers.ParserConfigurationException;

/*
 * Storage.java
 *
 * Created: Feb 1999
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
 *
 *
 * This provides a generic interface for getting and setting stored
 * data in WebMail.
 * 
 * @see net.wastl.webmail.storage.simple.SimpleStorage
 * @author Sebastian Schaffert
 * @version $Revision: 1.1.1.1 $
*/
/* 9/24/2000 devink - updated for new challenge/response authentication */
public abstract class Storage {

    public static final int LOG_DEBUG=10;
    public static final int LOG_INFO=5;
    public static final int LOG_WARN=3;
    public static final int LOG_ERR=1;
    public static final int LOG_CRIT=0;

    protected static boolean debug;

    protected WebMailServer parent;

    protected ConfigScheme cs;

    protected XMLSystemData sysdata;

    protected XMLGenericModel generic_model;

    public Storage(WebMailServer parent) {
	debug=WebMailServer.getDebug();
	this.parent=parent;
	cs=parent.getConfigScheme();
	cs.configRegisterYesNoKey("FOLDER TRY LOGIN PASSWORD","Try to connect folders with the user's login password if authentication fails");

    }

    public void initConfigKeys() {
	// Initialize the configuration file with the default or set parameters
	// needed to complete the XML file
	Enumeration enum=cs.getPossibleKeys();
	while(enum.hasMoreElements()) {
	    String key=(String)enum.nextElement();
	    if(!sysdata.isConfigSet(key)) {
		// We must use the raw method so the input doesn't get filtered.
		sysdata.setConfig(key,(String)cs.getDefaultValue(key),false,false);
	    }
	}
	saveXMLSysData();
    }


    public void setDebug(boolean b) {
	debug=b;
    }

    public boolean getDebug() {
	return debug;
    }

    /**
     * Fetch all keys of the current configuration.
     */
    public Enumeration getConfigKeys() {
	return cs.getPossibleKeys();
    }

    /**
     * Fetch the configuration associated with the specified key.
     * @param key Identifier for the configuration
     */
    public String getConfig(String key) {	
	return sysdata.getConfig(key);
    }

    /**
     * Set a configuration "key" to the specified value.
     * @param key Identifier for the configuration
     * @param value value to set
      */
    public void setConfig(String key, String value) throws IllegalArgumentException {
    // Modified by exce, start
    /**
     * Maybe here is a bug.
     *
     * Consider that if administrator happens to delete the value of certain
     * key, that is, the key is an empty tag.
     * FileStorage.java:272 call ConfigScheme.configRegister???Key() to set
     * value first, which becomes a default value, then call this function.
     * However, ConfigStore.getConfig() returns default value 
     * if the key is an empty tag in configuration file that getConfigRaw() 
     * returns null/empty value. That means the value passing in here is always
     * equals to the value returned by ConfigStore.getConfig().
     *
     * The simplest way to enforce saving config data is to comment out the 
     * if statement. However, this may be a performance issue, so I don't 
     * modify the code.
     */
    // Modified by exce, end
	if(!value.equals(getConfig(key))) {
	    log(LOG_DEBUG,"Storage API: Setting configuration for '"+key+"' to '"+value+"'.");
	    sysdata.setConfig(key,value);
	    saveXMLSysData();
	}
    }

    /**
     * Get the String for key and the specified locale.
     * @param key Identifier for the String
     * @param locale locale of the String to fetch
     */
    public abstract String getStringResource(String key, Locale locale);


   /** 
     * Get a xsl stylesheet for the specified locale and theme.
     * @param key Identifier for the String
     * @param locale locale of the String to fetch
     * @param theme theme where to look for the file
     */
    public abstract Templates getStylesheet(String name, Locale locale, String theme) throws WebMailException;

   /** 
     * Get a binary file (most likely an image) for the specified locale and theme.
     * @param key Identifier for the String
     * @param locale locale of the String to fetch
     * @param theme theme where to look for the file
     */
    public abstract byte[] getBinaryFile(String name, Locale locale, String theme) throws BinaryNotFoundException;


    /**
     * Calculate the document base path for a given locale and theme
     */
    public String getBasePath(Locale locale, String theme) {
	String language_path=(parent.getProperty("webmail.template.path")+
			      System.getProperty("file.separator")+locale.getLanguage());
	File f=new File(language_path);
	if(!f.exists()) {
	    language_path=(parent.getProperty("webmail.template.path")+
			   System.getProperty("file.separator")+parent.getDefaultLocale().getLanguage());
	    f=new File(language_path);
	    if(!f.exists()) {
		System.err.println("Storage::getBasePath: Default Language templates not found \n(tried path: "+language_path+")");
	    }
	}
	String theme_path=language_path+System.getProperty("file.separator")+theme;
	f=new File(theme_path);
	if(!f.exists()) {
	    if(parent.getProperty("webmail.default.theme") != null) {
		theme_path=language_path+System.getProperty("file.separator")+
		    parent.getProperty("webmail.default.theme");
	    } else {
		theme_path=language_path+System.getProperty("file.separator")+
		    "bibop";
	    }
	    f=new File(theme_path);
	    if(!f.exists()) {
		System.err.println("Storage::getBasePath: Theme could not be found; probably a problem with your\n installation. Please check the lib/templates/bibop directory and the \nwebmail.default.theme property");
	    }
	}
	String basepath=theme_path+System.getProperty("file.separator");
	return basepath;
    }	

    public XMLSystemData getSystemData() {
	return sysdata;
    }

    public XMLUserModel createXMLUserModel(XMLUserData data) throws WebMailException {
	try {
	    return new XMLUserModel(parent,sysdata.getSysData(),data.getUserData());
	} catch(ParserConfigurationException ex) {
	    throw new WebMailException("Creating the generic XML model failed. Reason: "+
				       ex.getMessage());
	}
    }

    /**
     * Return a XML model that contains state and system information for administrator use   
     */
    public XMLAdminModel createXMLAdminModel() throws WebMailException {
	try {
	    XMLAdminModel model=new XMLAdminModel(parent,sysdata.getSysData());
	    model.init();
	    model.update();
	return model;
	} catch(ParserConfigurationException ex) {
	    throw new WebMailException("Creating the generic XML model failed. Reason: "+
				       ex.getMessage());
	}
    }

    /**
     * Return a generic XML model that only contains some state and system information.
     * This cannot be changed by a single session.
     */
    public XMLGenericModel createXMLGenericModel() throws WebMailException {
	try {
	    XMLGenericModel model=new XMLGenericModel(parent,sysdata.getSysData());
	    model.init();
	    model.update();
	    return model;
	} catch(ParserConfigurationException ex) {
	    throw new WebMailException("Creating the generic XML model failed. Reason: "+
				       ex.getMessage());
	}
    }

    /**
     * Return userlist for a given domain.
     */
    public abstract Enumeration getUsers(String domain);

    /**
     * @deprecated Use getUsers(String domain) instead
     */
    public Enumeration getUsers() {
	final Enumeration domains=getVirtualDomains();
	return new Enumeration() {
		Enumeration enum=null;
		public boolean hasMoreElements() {
		    return (domains.hasMoreElements() || (enum != null && enum.hasMoreElements()));
		}
		public Object nextElement() {
		    if(enum == null || !enum.hasMoreElements()) {
			if(domains.hasMoreElements()) {
			    enum=getUsers((String)domains.nextElement());
			} else {
			    return null;
			}
		    }
		    return enum.nextElement();
		}
	    };
    }

    /**
     * Get the userdata for the specified user.
     * @param user Name of the user
     * @param domain Virtual Domain name of the user
     * @param passwd Password that the user provided
     *
     * devink 7/15/2000 - deprecated, but left in for backwards compat.
     * devink 9/24/2000 - not deprecated any more
     * XXX should passwd be a parameter?
     */
    public XMLUserData getUserData(String user, String domain,String passwd) 
	 throws UserDataException, InvalidPasswordException {
	    return getUserData(user,domain,passwd,false);
    }

    /**
     * get the userdata for the specified user. 
     * Should do some sort of authentication if authentication is set. See 
     * Authenticator class for example.
     *
     * devink 7/15/2000 - depreciated, and is no longer abstract, so there is
     * no requirement to implement it.
     * devink 9/24/2000 - not depreciated, and is now abstract
     */
    public abstract XMLUserData getUserData(String user, String domain, 
     String password, boolean authenticate) 
     throws UserDataException, InvalidPasswordException; 

    /**
     * Create a userdata for the specified user
     */
    public abstract XMLUserData createUserData(String user, String domain, String password) 
	throws CreateUserDataException;

    /**
     * Save the userdata for the given user.
     * @param user Username of this user
     * @param domain Domain of this user
     */
    public abstract void saveUserData(String user, String domain);

    /**
     * Set the userdata for the specified user.
     * @param user Name of the user
     * @param userdata Data to store
     * @deprecated use saveUserData instead.
     */
    public void setUserData(String user, UserData userdata) {
	// Call saveUserData, do nothing with "userdata"
	StringTokenizer tok=new StringTokenizer(user,"@");
	String login=tok.nextToken();
	String domain="nodomain";
	if(tok.hasMoreTokens()) {
	    domain=tok.nextToken();
	}
	saveUserData(login,domain);
    }

    /**
     * Delete a WebMail user
     * @param user Name of the user to delete
     * @param domain Domain of that user
     */
    public abstract void deleteUserData(String user, String domain);

    /**
     * Delete a WebMail user
     * @param user Name of the user to delete
     * @deprecated use deleteUserData(String user, String domain) instead.
     */
    public void deleteUserData(String user) {
	StringTokenizer tok=new StringTokenizer(user,"@");
	String login=tok.nextToken();
	String domain="nodomain";
	if(tok.hasMoreTokens()) {
	    domain=tok.nextToken();
	}
	deleteUserData(user,domain);
    }	

    /**
     * Set/add a WebMail virtual domain
     */
    public void setVirtualDomain(String name,WebMailVirtualDomain v) {
	sysdata.setVirtualDomain(name,v);
	saveXMLSysData();
    }

    /**
     * Get a WebMail virtual domain
     */
    public WebMailVirtualDomain getVirtualDomain(String name) {
	return sysdata.getVirtualDomain(name);
    }

    public WebMailVirtualDomain createVirtualDomain(String name) throws Exception {
	sysdata.createVirtualDomain(name);
	return sysdata.getVirtualDomain(name);
    }

    /**
     * Delete a WebMail virtual domain
     */
    public void deleteVirtualDomain(String name) {
	sysdata.deleteVirtualDomain(name);
    }

    /**
     * Return a list of virtual domains
     */
    public Enumeration getVirtualDomains() {
	return sysdata.getVirtualDomains();
    }

    /**
     * Return this Storage's Authenticator.
     * This is necessary for changing passwords or re-checking authentication.
     */
    public abstract Authenticator getAuthenticator();

    /**
     * Send a message to the logging facility.
     * @param level severity level of the message
     * @param message the message
     */
    public abstract void log(int level, String message);

    /**
     * Send an exception to the logging facility.
     * @param level severity level of the message
     * @param message the message
     */
    public abstract void log(int level, Exception ex);


    public abstract void shutdown();

    public void save() {
	saveXMLSysData();
    }


    protected abstract void loadXMLSysData();

    protected abstract void saveXMLSysData();

    public String getMimeType(String name) {
	String content_type;
	if(name != null && (name.toLowerCase().endsWith("jpg") || name.toLowerCase().endsWith("jpeg"))) {
	    content_type="IMAGE/JPEG";
	} else if(name != null && name.toLowerCase().endsWith("gif")) {
	    content_type="IMAGE/GIF";
	} else if(name != null && name.toLowerCase().endsWith("png")) {
	    content_type="IMAGE/PNG";
	} else if(name != null && name.toLowerCase().endsWith("txt")) {
	    content_type="TEXT/PLAIN";
	} else if(name != null && (name.toLowerCase().endsWith("htm") || name.toLowerCase().endsWith("html"))) {
	    content_type="TEXT/HTML";
	} else {
	    content_type="APPLICATION/OCTET-STREAM";
	}
	return content_type;
    }
	
}
