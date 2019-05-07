/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2003-2005 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id: Proxy.java,v 1.2 2005/11/16 12:32:50 tanderson Exp $
 */
package org.exolab.jms.net.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * The <code>Proxy</code> class is the common superclass for client proxies to
 * remote objects.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/11/16 12:32:50 $
 * @see Delegate
 */
public abstract class Proxy implements Serializable {

    /**
     * Serialization version.
     */
    static final long serialVersionUID = 1;

    /**
     * The delegate.
     */
    private Delegate _delegate;


    /**
     * Dispose this proxy, releasing any allocated resources.
     * <p/>
     * It is an error to invoke any method other than this, after the proxy
     * has been disposed.
     */
    public void disposeProxy() {
        _delegate.dispose();
    }

    /**
     * Construct a new <code>Proxy</code>
     *
     * @param delegate the delegate used to perform invocations
     */
    protected Proxy(Delegate delegate) {
        _delegate = delegate;
    }

    /**
     * Invoke a remote method via the delegate
     *
     * @param method   the method to invoke
     * @param args     the arguments to supply to the method
     * @param methodID a unique identifier for the method
     * @return the object returned by the remote method
     * @throws Throwable for any error
     */
    protected Object invoke(Method method, Object[] args, long methodID)
            throws Throwable {
        return _delegate.invoke(method, args, methodID);
    }

}
