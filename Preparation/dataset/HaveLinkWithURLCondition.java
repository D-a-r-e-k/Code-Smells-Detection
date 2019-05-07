/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.condition;

import org.exoplatform.test.web.ExoWebClient;
import org.exoplatform.test.web.Util;
import com.meterware.httpunit.*;

/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: HaveLinkWithURLCondition.java,v 1.1 2004/10/11 23:36:04 tuan08 Exp $
 **/
public class HaveLinkWithURLCondition implements Condition {
  private String partOfURL_ ;
  
  public HaveLinkWithURLCondition(String text) {
    partOfURL_ = text ;
  }
  
  public boolean checkCondition(WebResponse response, WebTable block, ExoWebClient client) throws Exception {
    WebLink link = Util.findLinkWithURL(response, block,partOfURL_) ;
    return link != null ;
  }
  
  public String getName() { return "HaveLinkWithURLCondition" ; }
  public String getDescription() {
    return "This unit test should run only if the previous return response has the link with url '..." + partOfURL_ + "...'" ;
  }
}