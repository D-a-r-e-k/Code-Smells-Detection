/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:08:10 PM
 */
package org.exoplatform.services.portletcontainer.pci;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import java.util.Map;
import java.util.HashMap;

public class ActionOutput extends Output {

  private Map renderParameters = new HashMap();
  private PortletMode nextMode;
  private WindowState nextState;
  private byte[] portletState;

  public Map getRenderParameters() {
    return renderParameters;
  }

  public void setRenderParameters(Map renderParameters) {
    this.renderParameters = renderParameters;
  }

  public void setRenderParameter(String key, String value) {
    renderParameters.put(key, new String[] {value});
  }

  public void setRenderParameters(String key, String[] values) {
    renderParameters.put(key, values);
  }

  public PortletMode getNextMode() {
    return nextMode;
  }

  public void setNextMode(PortletMode nextMode) {
    this.nextMode = nextMode;
  }

  public WindowState getNextState() {
    return nextState;
  }

  public void setNextState(WindowState nextState) {
    this.nextState = nextState;
  }

  public byte[] getPortletState() {
    return portletState;
  }

  public void setPortletState(byte[] portletState) {
    this.portletState = portletState;
  }

}
