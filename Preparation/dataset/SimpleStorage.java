/* CVS ID: $Id: SimpleStorage.java,v 1.1.1.1 2002/10/02 18:42:54 wastl Exp $ */
package net.wastl.webmail.storage.simple;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import net.wastl.webmail.storage.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.config.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.exceptions.*;


//import org.apache.xml.serialize.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;

// Modified by exce, start
import org.xml.sax.InputSource;
// Modified by exce, end

/**
 * SimpleStorage.java
 *
 * Created:  Feb 1999
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
 * This is the SimpleStorage class for the non-enterprise edition of WebMail. 
 * It provides means of getting and storing data in ZIPFiles and 
 * ResourceBundles.
 *
 * @see net.wastl.webmail.server.Storage
 * @author Sebastian Schaffert
 * @versin $Revision: 1.1.1.1 $
 */
public class SimpleStorage extends FileStorage {
	
    public static final String user_domain_separator="|";

    protected Hashtable resources;

    protected Hashtable vdoms;
    	
    protected ExpireableCache user_cache;
	    
    protected int user_cache_size=100;
	
    /**
     * Initialize SimpleStorage.
     * Fetch Configuration Information etc.
     */
    public SimpleStorage(WebMailServer parent) {
	super(parent);
	saveXMLSysData();
    }

    protected void initConfig() {
	System.err.print("  * Configuration ... ");

	loadXMLSysData();

	System.err.println("successfully parsed XML configuration file.");

    }

    protected void loadXMLSysData() {
	String datapath=parent.getProperty("webmail.data.path");
	String file="file://"+datapath+System.getProperty("file.separator")+"webmail.xml";
	// String file=datapath+System.getProperty("file.separator")+"webmail.xml";
	// bug fixed by Christian Senet
	Document root;
        try {
	    DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            root=parser.parse(file);
	    if(debug) System.err.println("\nConfiguration file parsed, document: "+root);
	    sysdata=new XMLSystemData(root,cs);
	    log(Storage.LOG_DEBUG, "SimpleStorage: WebMail configuration loaded.");
        } catch(Exception ex) {
	    log(Storage.LOG_ERR, "SimpleStorage: Failed to load WebMail configuration file. Reason: "+ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }


    }	

    protected void saveXMLSysData() {
	try {
	    Document d=sysdata.getRoot();
	    OutputStream cfg_out=new FileOutputStream(parent.getProperty("webmail.data.path")+
						      System.getProperty("file.separator")+
						      "webmail.xml");

	    XMLCommon.writeXML(d,cfg_out,"file://"+parent.getProperty("webmail.xml.path")+
			       System.getProperty("file.separator")+"sysdata.dtd");

// 	    XMLCommon.writeXML(d,cfg_out,parent.getProperty("webmail.xml.path")+
// 			       System.getProperty("file.separator")+"sysdata.dtd");
	    cfg_out.flush();
	    cfg_out.close();
	    sysdata.setLoadTime(System.currentTimeMillis());
	    log(Storage.LOG_DEBUG, "SimpleStorage: WebMail configuration saved.");
	} catch(Exception ex) {
	    log(Storage.LOG_ERR,"SimpleStorage: Error while trying to save WebMail configuration ("+ex.getMessage()+").");
	    ex.printStackTrace();
	}
    }	
	

    protected void initCache() {
	// Initialize the file cache from FileStorage
	super.initCache();

	// Now initialize the user cache
	cs.configRegisterIntegerKey(this,"CACHE SIZE USER","100","Size of the user cache");
	try {
	    // Default value 100, if parsing fails.
	    user_cache_size=100;
	    user_cache_size=Integer.parseInt(getConfig("CACHE SIZE USER"));
	} catch(NumberFormatException e) {}
	if(user_cache==null) {
	    user_cache=new ExpireableCache(user_cache_size);
	} else {
	    user_cache.setCapacity(user_cache_size);
	}
    }
	
     
    public Enumeration getUsers(String domain) {
	String path=parent.getProperty("webmail.data.path")+System.getProperty("file.separator")+
	    domain+System.getProperty("file.separator");
	
	File f=new File(path);
	if(f.canRead() && f.isDirectory()) {
	    final String[] files=f.list(new FilenameFilter() {
		    public boolean accept(File file, String s) {
			if(s.endsWith(".xml")) {
			    return true;
			} else {
			    return false;
			}
		    }
		} );
	    return new Enumeration() {
		    int i=0;
		    public boolean hasMoreElements() {
			return i<files.length;
		    }
		    public Object nextElement() {
			int cur=i++;
			return files[cur].substring(0,files[cur].length()-4);
		    }
		};
	} else {
	    log(Storage.LOG_WARN,"SimpleStorage: Could not list files in directory "+path);
	    return new Enumeration() {
		    public boolean hasMoreElements() { return false; }
		    public Object nextElement() { return null; }
		};
	}
    }


    public XMLUserData createUserData(String user, String domain, String password) throws CreateUserDataException {
	XMLUserData data;
	String template=parent.getProperty("webmail.xml.path")+
	    System.getProperty("file.separator")+"userdata.xml";

	File f=new File(template);
	if(!f.exists()) {
	    log(Storage.LOG_WARN,"SimpleStorage: User configuration template ("+template+") doesn't exist!");
	    throw new CreateUserDataException("User configuration template ("+template+") doesn't exist!",user,domain);
	} else if(!f.canRead()) {
	    log(Storage.LOG_WARN,"SimpleStorage: User configuration template ("+template+") is not readable!");
	    throw new CreateUserDataException("User configuration template ("+template+") is not readable!",user,domain);
	}
	
	Document root;
	try {
	    DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    root=parser.parse("file://"+template);
	    data=new XMLUserData(root);
	    data.init(user,domain,password);
	    if(getConfig("SHOW ADVERTISEMENTS").toUpperCase().equals("YES")) {
	        if(domain.equals("")) {
		  data.setSignature(user+"\n\n"+
		  		    getConfig("ADVERTISEMENT MESSAGE"));
		} else {
		  data.setSignature(user+"@"+domain+"\n\n"+
				    getConfig("ADVERTISEMENT MESSAGE"));
		}
	    } else {
	        if(domain.equals("")) {
		  data.setSignature(user);
		} else {
		  data.setSignature(user+"@"+domain);
		}
	    }
	    data.setTheme(parent.getDefaultTheme());
	    WebMailVirtualDomain vdom=getVirtualDomain(domain);
	    data.addMailHost("Default",getConfig("DEFAULT PROTOCOL")+"://"+
			     vdom.getDefaultServer(),user,password);
	    	    
	} catch(Exception ex) {
	    log(Storage.LOG_WARN,"SimpleStorage: User configuration template ("+template+") exists but could not be parsed");
	    if(debug) ex.printStackTrace();
	    throw new CreateUserDataException("User configuration template ("+template+") exists but could not be parsed",user,domain);
	}
	return data;
    }


    /**
     * @see net.wastl.webmail.server.Storage.getUserData()
     *
     * devink 7/15/2000 - Added TwoPassAuthenticationException
     *                  - changed to doAuth*UserData()
     *                  - Added challenged arg.
     * 9/24/2000        - reverted to old getUserData for new cr auth 
     */
    public XMLUserData getUserData(String user, String domain, String password, boolean authenticate) 
	 throws UserDataException, InvalidPasswordException 
    {
	if(authenticate) {
	    auth.authenticatePreUserData(user,domain,password);
	}
		
	if(user.equals("")) {
	    return null;
	}
		
	XMLUserData data=(XMLUserData)user_cache.get(user+user_domain_separator+domain);
	if(data == null) {
	    user_cache.miss();
	    String filename=parent.getProperty("webmail.data.path")+
		System.getProperty("file.separator")+domain+
		System.getProperty("file.separator")+user+".xml";
	    boolean error=true;
	    File f=new File(filename);
	    if(f.exists() && f.canRead()) {
		log(Storage.LOG_INFO,"SimpleStorage: Reading user configuration ("+f.getAbsolutePath()+") for "+user);
		    
		long t_start=System.currentTimeMillis();
		try {
		    DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    Document root = parser.parse(new InputSource(new InputStreamReader(new FileInputStream(filename), "UTF-8")));
// 		    Document root = parser.parse(new InputSource(new InputStreamReader(new FileInputStream(f.getAbsolutePath()), "UTF-8")));

		    data=new XMLUserData(root);
		    if(debug) System.err.println("SimpleStorage: Parsed Document "+root);
		    error=false;
		} catch(Exception ex) {
		    log(Storage.LOG_WARN,"SimpleStorage: User configuration for "+user+
			" exists but could not be parsed ("+ex.getMessage()+")");
		    if(debug) ex.printStackTrace();
		    error=true;
		}
		long t_end=System.currentTimeMillis();
		log(Storage.LOG_DEBUG,"SimpleStorage: Parsing of XML userdata for "+user+", domain "
		    +domain+" took "+(t_end-t_start)+"ms.");

		if(authenticate) {
		    auth.authenticatePostUserData(data,domain,password);
		}
	    } 

	    if(error && !f.exists()) {
		log(Storage.LOG_INFO,"UserConfig: Creating user configuration for "+user);
		
		data=createUserData(user,domain,password);

		error=false;
					
		if(authenticate) {
		    auth.authenticatePostUserData(data,domain,password);
		}
	    } 
	    if(error) {
		log(Storage.LOG_ERR,"UserConfig: Could not read userdata for "+user+"!");
		throw new UserDataException("Could not read userdata!",user,domain);
	    }
	    user_cache.put(user+user_domain_separator+domain,data);
	} else {
	    user_cache.hit();
	    if(authenticate) {
		auth.authenticatePostUserData(data,domain,password);
	    }
	}

	return data;
    }
	

    public void saveUserData(String user, String domain) {
	try {
	   
	    String path=parent.getProperty("webmail.data.path")+
		System.getProperty("file.separator")+domain;
	    File p=new File(path);
	    if((p.exists() && p.isDirectory()) || p.mkdirs()) {
		File f=new File(path+System.getProperty("file.separator")+user+".xml");
		if((!f.exists() && p.canWrite()) || f.canWrite()) {
		    XMLUserData userdata=getUserData(user,domain,"",false);
		    Document d=userdata.getRoot();

		    long t_start=System.currentTimeMillis();

		    FileOutputStream out=new FileOutputStream(f);
// 		    XMLCommon.writeXML(d,out,parent.getProperty("webmail.xml.path")+
// 				       System.getProperty("file.separator")+"userdata.dtd");

		    XMLCommon.writeXML(d,out,"file://"+parent.getProperty("webmail.xml.path")+
				       System.getProperty("file.separator")+"userdata.dtd");
		    out.flush();
		    out.close();		  
		    long t_end=System.currentTimeMillis();
		    log(Storage.LOG_DEBUG,"SimpleStorage: Serializing userdata for "+user+
			", domain "+domain+" took "+(t_end-t_start)+"ms.");
		} else {
		    log(Storage.LOG_WARN,"SimpleStorage: Could not write userdata ("+f.getAbsolutePath()+") for user "+user);
		}
	    } else {
		log(Storage.LOG_ERR,"SimpleStorage: Could not create path "+path+
		    ". Aborting with user "+user);
	    }
	} catch(Exception ex) {
	    log(Storage.LOG_ERR,"SimpleStorage: Unexpected error while trying to save user configuration "+
		"for user "+user+"("+ex.getMessage()+").");
	    if(debug) ex.printStackTrace();
	}
    }
	
	
	
    /**
     * Delete a WebMail user
     * @param user Name of the user to delete
     */
    public void deleteUserData(String user, String domain) {
	String path=parent.getProperty("webmail.data.path")+System.getProperty("file.separator")+
	    domain+System.getProperty("file.separator")+user+".xml";
	File f=new File(path);
	if(!f.canWrite() || !f.delete()) {
	    log(Storage.LOG_ERR,"SimpleStorage: Could not delete user "+user+" ("+path+")!");
	} else {
	    log(Storage.LOG_INFO,"SimpleStorage: Deleted user "+user+"!");
	}
	user_cache.remove(user+user_domain_separator+domain);
    }	
		
    public String toString() {
	String s="SimpleStorage:\n"+super.toString();
	s+=" - user cache:  Capacity "+user_cache.getCapacity()+", Usage "+user_cache.getUsage();
	s+=", "+user_cache.getHits()+" hits, "+user_cache.getMisses()+" misses\n";
	return s;
    }
	
}


