package org.exoplatform.services.organization;


public interface Group {
	public String getId() ;
  public String getParentId() ;
    
  public String getGroupName();
  public void setGroupName(String name);
  
  public String getLabel() ;
  public void   setLabel(String name) ;
  
  public String getDescription() ;
  public void   setDescription(String desc) ;
}
