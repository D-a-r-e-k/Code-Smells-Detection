/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.condition;

import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;

/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Condition.java,v 1.1 2004/10/11 23:36:04 tuan08 Exp $
 **/
public interface Condition {
  
  public boolean checkCondition(WebResponse response, WebTable block, ExoWebClient client)  throws Exception ;
  
  public String getName() ;
  public String getDescription() ;
}
