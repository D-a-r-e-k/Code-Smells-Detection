/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.commons.xhtml;

/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Parameter.java,v 1.1 2004/04/07 21:41:20 tuan08 Exp $
 */
public class Parameter {
  protected String  name_ ;
  protected String  value_ ;

  public Parameter(String name, String value) {
    name_ = name ;
    value_ = value ;
  }
  
  public String getName() { return name_ ; }
  public void   setName(String name) { name_ = name ; }

  public String getValue() { return value_ ; }  
  public void setValue(String s) { value_ = s ; }  
}
