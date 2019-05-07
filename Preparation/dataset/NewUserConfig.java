 /**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.cms.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class NewUserConfig {    
  private String contentLocation ;
  private String defaultLocale ;
  private String workspace;
  private String template;  
  private String jcrPath;
  private String createHome; 
  private List definedLocales = new ArrayList(5);
  private List users = new ArrayList(5);
  
  public String getContentLocation() {  return contentLocation; }
  public void setContentLocation(String s) { this.contentLocation = s; }
  
  public String getDefaultLocale() {  return defaultLocale; }
  public void setDefaultLocale(String s) { this.defaultLocale = s; }
  
  public String getWorkspace() {  return workspace; }
  public void setWorkspace(String s) { this.workspace = s; }
  
  public String getJcrPath() {  return jcrPath; }
  public void setJcrPath(String s) { this.jcrPath = s; }  

  public String getCreateHome() { return createHome; }
  public void setCreateHome(String s) { this.createHome = s; }
  public boolean isCreateHome(){
    if("true".equals(this.createHome))
      return true;
    else 
      return false;
  }
  
  public String getTemplate() { return template; }
  public void setTemplate(String template) { this.template = template; }
  
  public List getDefinedLocales() {   return definedLocales; }
  public void setDefinedLocales(List s) {  this.definedLocales = s; }    
  
  public List getUsers() {   return users; }
  public void setUsers(List s) {  this.users = s; }  
    
  static public class User {
    private String userName ;
    private List  referencedFiles ;
    
    public User() {
      referencedFiles = new ArrayList(5) ;
    }
        
    public String getUserName() {   return userName;  }
    public void setUserName(String userName) {  this.userName = userName;   }
    
    public List getReferencedFiles() {  return referencedFiles;   }
    public void setReferencedFiles(List referencedFiles) {  this.referencedFiles = referencedFiles;  }    
  }  
}
