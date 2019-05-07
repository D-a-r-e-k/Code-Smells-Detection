/*
 * $Id: ConnectionFactory.java,v 1.3 2003/07/09 23:56:17 rwald Exp $
 * =======================================================================
 * Copyright (c) 2002 Axion Development Team.  All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above 
 *    copyright notice, this list of conditions and the following 
 *    disclaimer. 
 *   
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution. 
 *   
 * 3. The names "Tigris", "Axion", nor the names of its contributors may 
 *    not be used to endorse or promote products derived from this 
 *    software without specific prior written permission. 
 *  
 * 4. Products derived from this software may not be called "Axion", nor 
 *    may "Tigris" or "Axion" appear in their names without specific prior
 *    written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * =======================================================================
 */

package org.axiondb.jdbc;

import java.io.File;
import java.sql.Connection;

import org.axiondb.AxionException;

/**
 * Abstract factory for creating {@link AxionConnection}s.
 *
 * @version $Revision: 1.3 $ $Date: 2003/07/09 23:56:17 $
 * @author Rodney Waldhoff
 */
public abstract class ConnectionFactory {
   public static final String URL_PREFIX = "jdbc:axiondb:";

   protected boolean isValidConnectString(String url) {
      return (url != null && url.startsWith(URL_PREFIX));
   }

   protected Connection createConnection(String url) throws AxionException {
      String name = null;
      File path = null;

      String prefixStripped = url.substring(URL_PREFIX.length());
      int colon = prefixStripped.indexOf(":");
      if(colon == -1 || (prefixStripped.length()-1 == colon)) {
          name = prefixStripped;
      } else {
          name = prefixStripped.substring(0,colon);
          path = new File(prefixStripped.substring(colon+1));
      }

      return new AxionConnection(name,path,url);
   }

}
