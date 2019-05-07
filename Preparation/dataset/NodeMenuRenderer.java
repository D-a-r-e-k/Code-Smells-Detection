/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.UINode ;
import org.exoplatform.faces.core.component.model.Parameter;

public  class NodeMenuRenderer extends HtmlBasicRenderer {

  protected static Parameter SELECT_TAB =  new Parameter(FacesConstants.ACTION , "selectTab") ;
  
  public void decode(FacesContext context, UIComponent component) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String action = (String) paramMap.get(FacesConstants.ACTION) ;
    if ("selectTab".equals(action)) {
      String tabId = (String) paramMap.get("tabId") ;
      UIExoComponent uiComponent = (UIExoComponent) component ;
      UIExoComponent target = uiComponent.findComponentById(tabId);
      if(target != null) {
        UINode uiNode = (UINode) target.getParent() ;
        uiNode.setRenderedComponent(tabId) ;
        context.renderResponse() ;
      }
    }
  }
}