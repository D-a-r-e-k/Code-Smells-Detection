/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.organization.hibernate;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 27, 2004
 * @version $Id: OrganizationConfig.java,v 1.1 2004/10/28 15:36:43 tuan08 Exp $
 */
public class OrganizationConfig {
  private List membershipType ;
  private List group ;
  private List user ;

  
  public OrganizationConfig() {    
    membershipType = new ArrayList(5) ;
    group = new ArrayList(5) ;
    user = new ArrayList(5) ;
  }
  
  
  public List getGroup() {  return group; }
  public void setGroup(List group) {  this.group = group; }
  
  public List getMembershipType() { return membershipType; }
  public void setMembershipType(List membershipType) {  this.membershipType = membershipType;}
    
  public List getUser() {   return user; }
  public void setUser(List user) {   this.user = user; }
  
  static public class MembershipType {
    private String type ;
    private String description ;
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getType() {  return type;  }
    public void setType(String type) { this.type = type; }
  }
  
  static public class Group {
    private String name ;
    private String type ;
    private String description ;
    private String parentId ;
    
    public String getDescription() { return description;  }
    public void setDescription(String description) { this.description = description;  }
    
    public String getName() {   return name;  }
    public void setName(String name) {   this.name = name;  }
    
    public String getParentId() {  return parentId; }
    public void setParentId(String parentId) {  this.parentId = parentId;  }
    
    public String getType() {   return type; }
    public void setType(String type) {  this.type = type;  }
  }   
  
  static public class User {
    private String userName ;
    private String password ;
    private String firstName ;
    private String lastName ;
    private String email ;
    private List  groups ;
    
    public User() {
      groups = new ArrayList(3) ;
    }
    
    public String getEmail() {  return email;  }
    public void setEmail(String email) { this.email = email;   }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {  this.firstName = firstName;  }
    
    public String getLastName() { return lastName;  }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPassword() {  return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List getGroups() {  return groups;   }
    public void setGroups(List groups) {  this.groups = groups;  }
    
    public String getUserName() {   return userName;  }
    public void setUserName(String userName) {  this.userName = userName;   }
  }
}