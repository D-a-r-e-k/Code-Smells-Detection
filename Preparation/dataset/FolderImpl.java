/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message.impl;

import java.util.Date;
import org.exoplatform.services.communication.message.Folder;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Author : Benjamin Mestrallet
 *          benjamin.mestrallet@exoplatform.com
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 * @hibernate.class  table="EXO_MESSAGE_FOLDER"
 * @hibernate.cache  usage="read-write"
 */
public class FolderImpl implements Folder {
  private String id_ ;
  private String accountId_ ;
  private String name_ ;
  private String label_ ;
  private Date createdDate_ ;
  private boolean removeable_ = true  ;
  
  public FolderImpl() {} 
  
  public FolderImpl(String name , String label, String accountId) {
    name_ = name ;
    label_ = label ;
    accountId_ = accountId ;
  } 
  
  /**
   * @hibernate.id  generator-class="assigned"
   **/
  public String  getId() { return id_ ; }
  public void    setId(String value) {  id_ = value; }

  /**
   * @hibernate.property
   **/
  public String getAccountId() { return accountId_ ; }
  public void   setAccountId(String value) { accountId_ = value ; }
  
  /**
   * @hibernate.property
   **/
  public String getName() {return name_;}
  public void setName(String name) { name_ = name;}
  
  /**
   * @hibernate.property
   **/
  public String getLabel() { return label_ ; }
  public void setLabel(String label) { label_ = label ;}
  
  /**
   * @hibernate.property
   **/
  public Date getCreatedDate() { return createdDate_ ; }
  public void setCreatedDate(Date date) { createdDate_ = date ;}
  
  /**
   * @hibernate.property
   **/
  public boolean getRemoveable() { return removeable_  ; }
  public void setRemoveable(boolean b) { removeable_ = b ;}
}