/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.monitor;

import java.util.Map ;
import java.util.Iterator;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 1, 2004
 * @version $Id$
 */
public class ActionData {
  private String portal ;
  private String page ;
  private String requestType ;
  private long   handleTime ;
  private String parameters ;
  private String error ;
  
  public ActionData(String portal, String page, String type, long time, Map params ) {
    this.portal = portal ;
    this.page = page ;
    this.requestType = type ;
    this.handleTime = time ;
    Iterator i = params.entrySet().iterator() ;
    StringBuffer b = new StringBuffer() ;
    while(i.hasNext()) {
      b.append("{") ;
      Map.Entry entry = (Map.Entry) i.next() ;
      Object obj = entry.getValue() ;
      String key =  (String)entry.getKey() ;
      b.append(key).append("=") ;
      if(key.startsWith("password"))  {
        b.append("**************") ;
      } else {
        if(obj instanceof String[]) {
          String[] values = (String[]) obj ;
          for(int j = 0;  j < values.length; j++) {
            if(j > 0) b.append(", ") ;
            b.append(values[j]) ;
          }
        } else {
          b.append(obj) ;
        }
      }
      b.append("} ") ;
    }
    this.parameters = b.toString() ;
  }
  
  public String getPortal() { return portal ; }
  
  public String getPage() { return page ; }
  
  public String getRequestType() { return requestType ; }
  
  public long  getHandleTime() { return handleTime ; }
  
  public String getParameters() { return parameters ; }
  
  public String getError() { return error ; }
  public void   setError(String error) { this.error = error ; }
}
