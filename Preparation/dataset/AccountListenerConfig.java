/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 29, 2004
 * @version $Id: AccountListenerConfig.java,v 1.1 2004/10/30 02:24:13 tuan08 Exp $
 */
public class AccountListenerConfig {
  private String accountName ;
  private String server ;
  private String protocol ;
  
  
  public String getAccountName() { return accountName; }
  public void setAccountName(String accountName) { this.accountName = accountName; }
  
  public String getProtocol() {  return protocol; }
  public void setProtocol(String protocol) { this.protocol = protocol; }
  
  public String getServer() {  return server; }
  public void setServer(String server) {  this.server = server; }
}