/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Portlet.java,v 1.7 2004/09/30 01:00:05 tuan08 Exp $
 **/
public class Portlet extends Component {
  private String title;
  private String windowId ;
  private String portletStyle = "default" ;
  private boolean  showInfoBar = true ;
  private boolean  showWindowState = true ;
  private boolean  showPortletMode = true ;
  private ExoPortletPreferences portletPreferences ;
  
  public String getTitle() { return title ; }
  public void   setTitle(String s) { title = s ;}
  
  public String getWindowId() { return windowId ; }
  public void   setWindowId(String s) { windowId = s ;}
  
  public String getPortletStyle() { return portletStyle ; }
  public void   setPortletStyle(String s) { portletStyle = s ;}
  
  public boolean getShowInfoBar() { return showInfoBar ; }
  public void    setShowInfoBar(boolean b) { showInfoBar = b ; }
  
  public boolean getShowWindowState() { return showWindowState ; }
  public void    setShowWindowState(boolean b) { showWindowState = b ; }
  
  public boolean getShowPortletMode() { return showPortletMode ; }
  public void    setShowPortletMode(boolean b) { showPortletMode = b ; }
  
  public ExoPortletPreferences getPortletPreferences() { return portletPreferences ; }
  public void setPortletPreferences(ExoPortletPreferences prefs) { portletPreferences = prefs ; }
  
  public Component softCloneObject() {
  	Portlet portlet = new Portlet() ;
  	portlet.copyBasicProperties(this) ;
  	portlet.setTitle(title) ;
  	portlet.setWindowId(windowId) ;
  	portlet.setPortletStyle(portletStyle) ;
  	portlet.setShowInfoBar(showInfoBar) ;
  	portlet.setShowWindowState(showWindowState) ;
    portlet.setShowPortletMode(showPortletMode) ;
  	portlet.setPortletPreferences(portletPreferences);
  	return portlet ;
  }
}