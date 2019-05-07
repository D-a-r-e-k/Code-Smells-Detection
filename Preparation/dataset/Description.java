/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.pci.model;

/**
 * Jul 7, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Description.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Description {
	private String lang ;
	private String description ;
	
	
	public String getDescription() {
		return description;
	}
    
	public void setDescription(String description) {
		this.description = description;
	}
    
	public String getLang() {
		return lang;
	}
    
	public void setLang(String lang) {
		this.lang = lang;
	}
}
