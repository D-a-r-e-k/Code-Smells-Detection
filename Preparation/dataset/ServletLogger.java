/* CVS ID: $Id: ServletLogger.java,v 1.1.1.1 2002/10/02 18:42:48 wastl Exp $ */
package net.wastl.webmail.logger;


import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.config.*;

/**
 * ServletLogger.java
 *
 * Copyright (C) 1999-2002 Sebastian Schaffert
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
 * This logger implementation logs all WebMail logfile entries using the Servlet Container's
 * log facility.
 *
 * @author Sebastian Schaffert
 * @version
 */
public class ServletLogger implements Logger, ConfigurationListener {

    protected WebMailServlet parent;
    protected Storage store;
    protected int loglevel;

    public ServletLogger(WebMailServer parent, Storage st) throws WebMailException {
	this.store=st;
	
	if(! (parent instanceof WebMailServlet)) {
	    throw new WebMailException("ServletLogger can only be used by a Servlet!");
	} else {
	    this.parent=(WebMailServlet)parent;
	}
	parent.getConfigScheme().configRegisterIntegerKey(this,"LOGLEVEL","5",
							  "How much debug output will be written in the logfile");

	initLog();
    }

    protected void initLog() {
	try {
	    loglevel=Integer.parseInt(store.getConfig("LOGLEVEL"));
	} catch(NumberFormatException e) {
	    loglevel = 5;
	}
    }

    public void notifyConfigurationChange(String key) {
	initLog();
    }	


    public void log(int level, String message) {
	if(level <= loglevel) {
	    String s="LEVEL "+level;
	    switch(level) {
	    case Storage.LOG_DEBUG: s="DEBUG   "; break;
	    case Storage.LOG_INFO: s="INFO    "; break;
	    case Storage.LOG_WARN: s="WARNING "; break;
	    case Storage.LOG_ERR: s="ERROR   "; break;
	    case Storage.LOG_CRIT: s="CRITICAL"; break;
	    }
	    parent.getServletContext().log(s+" - "+message);
	}
    }	

    public void log(int level, Exception ex) {
	if(level <= loglevel) {
	    String s="LEVEL "+level;
	    switch(level) {
	    case Storage.LOG_DEBUG: s="DEBUG   "; break;
	    case Storage.LOG_INFO: s="INFO    "; break;
	    case Storage.LOG_WARN: s="WARNING "; break;
	    case Storage.LOG_ERR: s="ERROR   "; break;
	    case Storage.LOG_CRIT: s="CRITICAL"; break;
	    }
	    parent.getServletContext().log(s+" - An exception occured", ex);
	}
    }

    public void shutdown() {}


}
