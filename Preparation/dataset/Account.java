/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

import org.exoplatform.commons.utils.ExoProperties;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface Account  {
  final static String SERVER_SETTING_USERNAME = "server.setting.username" ;
  final static String SERVER_SETTING_PASSWORD = "server.setting.password" ;
  final static String SERVER_SETTING_HOSTNAME = "server.setting.hostname" ;
  
  public String getId() ;
  
  public String getAccountName()  ; 
  public void   setAccountName(String accountName) ; 
  
  public String getOwner()  ;
  public void   setOwner(String userName) ; 
  
  public String getOwnerName() ;
  public void   setOwnerName(String name) ;
  
  public String getReplyToAddress() ;
  public void   setReplyToAddress(String address) ;
  
  public String getSignature() ;
  public void   setSignature(String signature) ;

  public String getAccessRole()  ; 
  public void   setAccessRole(String role) ; 
  
  public String getProtocol()  ; 
  public void   setProtocol(String protocol)  ; 

  public ExoProperties getProperties()  ;
  public void setProperties(ExoProperties props) ;
  
  public String getProperty(String key)  ;
  public void   setProperty(String key, String value) ;
}