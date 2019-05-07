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
 * $Id: URIHelper.java,v 1.4 2005/12/01 13:44:38 tanderson Exp $
 */
package org.exolab.jms.net.uri;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.exolab.jms.common.security.BasicPrincipal;


/**
 * Helper for operations on URIs.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.4 $ $Date: 2005/12/01 13:44:38 $
 * @see URI
 */
public final class URIHelper {

    /**
     * Prevent construction of helper class.
     */
    private URIHelper() {
    }

    /**
     * Helper to create an URI.
     *
     * @param scheme the URI scheme
     * @param host   the host
     * @param port   the port
     * @return a new URI
     * @throws InvalidURIException if any argument is invalid
     */
    public static URI create(String scheme, String host, int port)
            throws InvalidURIException {
        return create(scheme, host, port, "/");
    }

    /**
     * Helper to create an URI.
     *
     * @param scheme the URI scheme
     * @param host   the host
     * @param port   the port
     * @param path   the path
     * @return a new URI
     * @throws InvalidURIException if any argument is invalid
     */
    public static URI create(String scheme, String host, int port, String path)
            throws InvalidURIException {
        URI result;
        try {
            result = new URI(scheme, null, host, port, path, null, null);
        } catch (URI.MalformedURIException exception) {
            throw new InvalidURIException(exception.getMessage());
        }
        return result;
    }

    /**
     * Helper to create an URI.
     *
     * @param scheme the URI scheme
     * @param host   the host
     * @param port   the port
     * @param path   the path
     * @param params a map of key/value pairs used to construct a query string
     * @return a new URI
     * @throws InvalidURIException if any argument is invalid
     */
    public static URI create(String scheme, String host, int port, String path,
                             Map params)
            throws InvalidURIException {
        Iterator iter = params.entrySet().iterator();
        StringBuffer query = new StringBuffer();
        for (int i = 0; iter.hasNext(); ++i) {
            if (i > 0) {
                query.append('&');
            }
            Map.Entry entry = (Map.Entry) iter.next();
            query.append(entry.getKey());
            query.append('=');
            query.append(entry.getValue());
        }
        return create(scheme, host, port, path, query.toString());
    }


    /**
     * Helper to create an URI.
     *
     * @param scheme the URI scheme
     * @param host   the host
     * @param port   the port
     * @param path   the path
     * @param query  the query
     * @return a new URI
     * @throws InvalidURIException if any argument is invalid
     */
    public static URI create(String scheme, String host, int port, String path,
                             String query)
            throws InvalidURIException {
        URI result;
        try {
            result = new URI(scheme, null, host, port, path, query, null);
        } catch (URI.MalformedURIException exception) {
            throw new InvalidURIException(exception.getMessage());
        }
        return result;
    }

    /**
     * Helper to parse a String to an URI. If no path was specified in, the URI,
     * it will default to <code>"/"</code>.
     *
     * @param uri the URI to parse
     * @return the parsed URI
     * @throws InvalidURIException if <code>uri</code> is invalid
     */
    public static URI parse(String uri) throws InvalidURIException {
        URI result;
        try {
            result = new URI(uri);
            fixPath(result);
        } catch (URI.MalformedURIException exception) {
            throw new InvalidURIException(exception.getMessage());
        }
        return result;
    }

    /**
     * Helper to parse an URI, verifying that it has the correct scheme.
     *
     * @param uri    the URI to parse
     * @param scheme the expected scheme
     * @return the parsed URI
     * @throws InvalidURIException if <code>uri</code> is invalid
     */
    public static URI parse(String uri, String scheme)
            throws InvalidURIException {
        URI result = parse(uri);
        if (!result.getScheme().equals(scheme)) {
            throw new InvalidURIException(
                    "Invalid scheme: " + result.getScheme());
        }
        return result;
    }

    /**
     * Helper to parse an URI, verifying that it has the correct scheme, host
     * and port specification, and no path.
     *
     * @param uri    the URI to parse
     * @param scheme the expected scheme
     * @return the parsed URI
     * @throws InvalidURIException if <code>uri</code> is invalid
     */
    public static URI parseHostPort(String uri, String scheme)
            throws InvalidURIException {
        URI result = parse(uri, scheme);
        if (result.getHost() == null) {
            throw new InvalidURIException("No host specified in URI: " + uri);
        }
        if (result.getPort() == -1) {
            throw new InvalidURIException("No port specified in URI: " + uri);
        }
        if (result.getPath() != null && !result.getPath().equals("")
            && !result.getPath().equals("/")) {
            throw new InvalidURIException(
                    "URI must not specify a path: " + uri);
        }
        return result;
    }

    /**
     * Helper to convert the host name portion of a URI to its corresponding IP
     * address. If the URI doesn't contain a host, or the host is specified as
     * an IP address, or the IP address can't be determined, then the URI is
     * returned unchanged.
     *
     * @param uri the uri to convert
     * @return the converted URI
     */
    public static URI convertHostToAddress(URI uri) {
        URI result = uri;
        String host = uri.getHost();
        if (host != null && !host.equals("")) {
            try {
                InetAddress address = InetAddress.getByName(host);
                result = new URI(uri.getScheme(), uri.getUserinfo(),
                                 address.getHostAddress(), uri.getPort(),
                                 uri.getPath(), uri.getQueryString(),
                                 uri.getFragment());
                fixPath(result);
            } catch (UnknownHostException ignore) {
            } catch (URI.MalformedURIException exception) {
                // should *never* happen
                throw new IllegalArgumentException("Failed to construct URI: "
                                                   + exception.getMessage());
            }
        }
        return result;
    }

    /**
     * Returns a {@link BasicPrincipal} corresponding to the user info specified
     * in a URI.
     *
     * @param uri the URI to get the user info from
     * @return a new BasicPrincipal containing the user info, or
     *         <code>null</code> if none was specified
     */
    public static BasicPrincipal getPrincipal(URI uri) {
        BasicPrincipal principal = null;
        String userinfo = uri.getUserinfo();
        if (userinfo != null && userinfo.length() != 0) {
            int index = userinfo.indexOf(":");
            String user;
            String password = "";
            if (index != -1) {
                user = userinfo.substring(0, index);
                password = userinfo.substring(index + 1);
            } else {
                user = userinfo;
            }
            principal = new BasicPrincipal(user, password);
        }
        return principal;
    }

    /**
     * Returns a URI minus query/fragment.
     *
     * @param uri the URI
     * @return the URI minus query/fragment or <code>uri</code> if there is
     *         no query/fragment
     */
    public static URI getURISansQuery(URI uri) {
        URI result = uri;
        if (uri.getQueryString() != null || uri.getFragment() != null) {
            try {
                result = new URI(uri.getScheme(), uri.getUserinfo(),
                                 uri.getHost(), uri.getPort(),
                                 uri.getPath(), null, null);
            } catch (URI.MalformedURIException exception) {
                // should *never* happen
                throw new IllegalArgumentException("Failed to construct URI: "
                                                   + exception.getMessage());

            }
        }
        return result;
    }

    /**
     * Parse a query string.
     * <p/>
     * Note that this implementation doesn't support multiple parameters.
     * with the same name. The value of the last will be returned.
     *
     * @param query the query to parse
     * @return the parsed parameters
     * @throws InvalidURIException if <code>query</code> is invalid
     */
    public static Map parseQuery(String query) throws InvalidURIException {
        Map result = new HashMap();
        StringTokenizer tokens = new StringTokenizer(query, "&");
        while (tokens.hasMoreTokens()) {
            String pair = (String) tokens.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                throw new InvalidURIException("Invalid query=" + query);
            }
            String key = pair.substring(0, pos);
            String value = pair.substring(pos + 1, pair.length());
            result.put(key, value);
        }
        return result;
    }

    /**
     * Ensures that the path is set to <code>"/"</code> if not specified in a
     * URI, so equality tests return true for instances of the form a://b:1234
     * == a://b:1234/
     *
     * @param uri the URI the fix the path for
     * @throws URI.MalformedURIException if the path can't be set
     */
    private static void fixPath(URI uri) throws URI.MalformedURIException {
        String path = uri.getPath();
        if (path == null || path.equals("")) {
            uri.setPath("/");
        }
    }

}
