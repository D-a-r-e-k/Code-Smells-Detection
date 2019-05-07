/*

 * The Apache Software License, Version 1.1

 *

 *

 * Copyright (c) 1999 The Apache Software Foundation.  All rights

 * reserved.

 *

 * Redistribution and use in source and binary forms, with or without

 * modification, are permitted provided that the following conditions

 * are met:

 *

 * 1. Redistributions of source code must retain the above copyright

 *    notice, this list of conditions and the following disclaimer.

 *

 * 2. Redistributions in binary form must reproduce the above copyright

 *    notice, this list of conditions and the following disclaimer in

 *    the documentation and/or other materials provided with the

 *    distribution.

 *

 * 3. The end-user documentation included with the redistribution,

 *    if any, must include the following acknowledgment:

 *       "This product includes software developed by the

 *        Apache Software Foundation (http://www.apache.org/)."

 *    Alternately, this acknowledgment may appear in the software itself,

 *    if and wherever such third-party acknowledgments normally appear.

 *

 * 4. The names "Xalan" and "Apache Software Foundation" must

 *    not be used to endorse or promote products derived from this

 *    software without prior written permission. For written

 *    permission, please contact apache@apache.org.

 *

 * 5. Products derived from this software may not be called "Apache",

 *    nor may "Apache" appear in their name, without prior written

 *    permission of the Apache Software Foundation.

 *

 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED

 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES

 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE

 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR

 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,

 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT

 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF

 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND

 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,

 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT

 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF

 * SUCH DAMAGE.

 * ====================================================================

 *

 * This software consists of voluntary contributions made by many

 * individuals on behalf of the Apache Software Foundation and was

 * originally based on software copyright (c) 1999, Lotus

 * Development Corporation., http://www.lotus.com.  For more

 * information on the Apache Software Foundation, please see

 * <http://www.apache.org/>.

 */





package org.exolab.jms.net.uri;



import java.io.IOException;

import java.io.Serializable;





/**

 * A class to represent a Uniform Resource Identifier (URI). This class

 * is designed to handle the parsing of URIs and provide access to

 * the various components (scheme, host, port, userinfo, path, query

 * string and fragment) that may constitute a URI.

 * <p>

 * Parsing of a URI specification is done according to the URI

 * syntax described in RFC 2396

 * <http://www.ietf.org/rfc/rfc2396.txt?number=2396>. Every URI consists

 * of a scheme, followed by a colon (':'), followed by a scheme-specific

 * part. For URIs that follow the "generic URI" syntax, the scheme-

 * specific part begins with two slashes ("//") and may be followed

 * by an authority segment (comprised of user information, host, and

 * port), path segment, query segment and fragment. Note that RFC 2396

 * no longer specifies the use of the parameters segment and excludes

 * the "user:password" syntax as part of the authority segment. If

 * "user:password" appears in a URI, the entire user/password string

 * is stored as userinfo.

 * <p>

 * For URIs that do not follow the "generic URI" syntax (e.g. mailto),

 * the entire scheme-specific part is treated as the "path" portion

 * of the URI.

 * <p>

 * Note that, unlike the java.net.URL class, this class does not provide

 * any built-in network access functionality nor does it provide any

 * scheme-specific functionality (for example, it does not know a

 * default port for a specific scheme). Rather, it only knows the

 * grammar and basic set of operations that can be applied to a URI.

 *

 * @version  $Id: URI.java,v 1.1 2004/11/26 01:51:06 tanderson Exp $

 *

 */

public final class URI

{





  /**

   * MalformedURIExceptions are thrown in the process of building a URI

   * or setting fields on a URI when an operation would result in an

   * invalid URI specification.

   */

  public static class MalformedURIException

      extends IOException

  {





      /**

       * Constructs a <code>MalformedURIException</code> with no specified

       * detail message.

       */

      public MalformedURIException()

      {

          super();

      }





      /**

       * Constructs a <code>MalformedURIException</code> with the

       * specified detail message.

       *

       * @param message the detail message.

       */

      public MalformedURIException( String message )

      {

          super( message );

      }





  }





    /**

     * reserved characters

     */

    private static final String RESERVED_CHARACTERS = ";/?:@&=+$,";





    /**

     * URI punctuation mark characters - these, combined with

     * alphanumerics, constitute the "unreserved" characters

     */

    private static final String MARK_CHARACTERS = "-_.!~*'() ";





    /**

     * scheme can be composed of alphanumerics and these characters

     */

    private static final String SCHEME_CHARACTERS = "+-.";





    /**

     * userinfo can be composed of unreserved, escaped and these

     * characters

     */

    private static final String USERINFO_CHARACTERS = ";:&=+$,";





    /**

     * Stores the scheme (usually the protocol) for this URI.

     */

    private String _scheme = null;





    /**

     * If specified, stores the userinfo for this URI; otherwise null

     */

    private String _userinfo = null;





    /**

     * If specified, stores the host for this URI; otherwise null

     */

    private String _host = null;





    /**

     * If specified, stores the port for this URI; otherwise -1

     */

    private int _port = -1;





    /**

     * If specified, stores the path for this URI; otherwise null

     */

    private String _path = null;





    /**

     * If specified, stores the query string for this URI; otherwise

     * null.

     */

    private String _queryString = null;





    /**

     * If specified, stores the fragment for this URI; otherwise null

     */

    private String _fragment = null;





    /**

     * Indicate whether in DEBUG mode

     */

    private static boolean DEBUG = false;





    /**

     * Construct a new and uninitialized URI.

     */

    public URI()

    {

    }





    /**

     * Construct a new URI from another URI. All fields for this URI are

     * set equal to the fields of the URI passed in.

     *

     * @param other the URI to copy (cannot be null)

     */

    public URI( URI other)

    {

        initialize( other );

    }





    /**

     * Construct a new URI from a URI specification string. If the

     * specification follows the "generic URI" syntax, (two slashes

     * following the first colon), the specification will be parsed

     * accordingly - setting the scheme, userinfo, host,port, path, query

     * string and fragment fields as necessary. If the specification does

     * not follow the "generic URI" syntax, the specification is parsed

     * into a scheme and scheme-specific part (stored as the path) only.

     *

     * @param uriSpec the URI specification string (cannot be null or empty)

     * @throws MalformedURIException uriSpec violates any syntax rules

     */

    public URI( String uriSpec )

        throws MalformedURIException

    {

        this( (URI) null, uriSpec);

    }





    /**

     * Construct a new URI from a base URI and a URI specification string.

     * The URI specification string may be a relative URI.

     *

     * @param base the base URI (cannot be null if uriSpec is null or empty)

     * @param uriSpec the URI specification string (cannot be null or empty

     * if base is null)

     * @throws MalformedURIException uriSpec violates any syntax rules

     */

    public URI( URI base, String uriSpec)

        throws MalformedURIException

    {

        initialize( base, uriSpec );

    }





    /**

     * Construct a new URI that does not follow the generic URI syntax.

     * Only the scheme and scheme-specific part (stored as the path) are

     * initialized.

     *

     * @param scheme the URI scheme (cannot be null or empty)

     * @param schemeSpecificPart the scheme-specific part (cannot be

     * null or empty)

     * @throws MalformedURIException scheme violates any syntax rules

     */

    public URI( String scheme, String schemeSpecificPart )

        throws MalformedURIException

    {

        if ( scheme == null || scheme.trim().length() == 0 )

            throw new MalformedURIException( "Argument scheme is null or an empty string" );

        if ( schemeSpecificPart == null || schemeSpecificPart.trim().length() == 0 )

            throw new MalformedURIException( "Argument schemeSpecificPart is null or an empty string" );

        setScheme( scheme );

        setPath( schemeSpecificPart );

    }





    /**

     * Construct a new URI that follows the generic URI syntax from its

     * component parts. Each component is validated for syntax and some

     * basic semantic checks are performed as well.  See the individual

     * setter methods for specifics.

     *

     * @param scheme the URI scheme (cannot be null or empty)

     * @param host the hostname or IPv4 address for the URI

     * @param path the URI path - if the path contains '?' or '#',

     * then the query string and/or fragment will be set from the path;

     * however, if the query and fragment are specified both in the path

     * and as separate parameters, an exception is thrown

     * @param queryString the URI query string (cannot be specified

     * if path is null)

     * @param fragment the URI fragment (cannot be specified if path is null)

     * @throws MalformedURIException Any of the parameters violates

     * syntax rules or semantic rules

     */

    public URI( String scheme, String host, String path,

                String queryString, String fragment )

        throws MalformedURIException

    {

        this( scheme, null, host, -1, path, queryString, fragment );

    }





    /**

     * Construct a new URI that follows the generic URI syntax from its

     * component parts. Each component is validated for syntax and some

     * basic semantic checks are performed as well.  See the individual

     * setter methods for specifics.

     *

     * @param scheme the URI scheme (cannot be null or empty)

     * @param userinfo the URI userinfo (cannot be specified if host is null)

     * @param host the hostname or IPv4 address for the URI

     * @param port the URI port (may be -1 for "unspecified"; cannot

     * be specified if host is null)

     * @param path the URI path - if the path contains '?' or '#',

     * then the query string and/or fragment will be set from the path;

     * however, if the query and fragment are specified both in the path

     * and as separate parameters, an exception is thrown

     * @param queryString the URI query string (cannot be specified

     * if path is null)

     * @param fragment the URI fragment (cannot be specified if path is null)

     * @throws MalformedURIException Any of the parameters violates

     *  syntax rules or semantic rules

     */

    public URI( String scheme, String userinfo, String host, int port,

                String path, String queryString, String fragment )

        throws MalformedURIException

    {

        if ( scheme == null || scheme.trim().length() == 0 )

            throw new MalformedURIException( "Argument scheme is null or an empty string" );

        if ( host == null ) {

            if ( userinfo != null )

                throw new MalformedURIException( "Argument userInfo must be null if host is null" );

            if ( port != -1 )

                throw new MalformedURIException( "Argument port must be null if host is null" );

        } else if ( host.trim().length() == 0 )

            throw new IllegalArgumentException( "Argument host is an empty string" );

        if ( path != null ) {

            if ( path.indexOf('?') != -1 && queryString != null )

                throw new MalformedURIException( "Argument queryString is illegal if path includes query string" );

            if ( path.indexOf('#') != -1 && fragment != null )

                throw new MalformedURIException( "Argument fragment is illegal if path includes fragment identifier" );

        } else if ( path.trim().length() == 0 )

            throw new IllegalArgumentException( "Argument path is an empty string" );

        setScheme( scheme );

        setHost( host );

        setPort( port );

        setUserinfo( userinfo );

        setPath( path );

        setQueryString( queryString );

        setFragment( fragment );

    }





    /**

     * Initialize all fields of this URI from another URI.

     *

     * @param other the URI to copy (cannot be null)

     */

    private void initialize( URI other )

    {

        _scheme = other.getScheme();

        _userinfo = other.getUserinfo();

        _host = other.getHost();

        _port = other.getPort();

        _path = other.getPath();

        _queryString = other.getQueryString();

        _fragment = other.getFragment();

    }





    /**

     * Initializes this URI from a base URI and a URI specification string.

     * See RFC 2396 Section 4 and Appendix B for specifications on parsing

     * the URI and Section 5 for specifications on resolving relative URIs

     * and relative paths.

     *

     * @param base the base URI (may be null if uriSpec is an absolute URI)

     * @param uriSpec the URI spec string which may be an absolute or

     * relative URI (can only be null/empty if base is not null)

     * @throws MalformedURIException base is null and uriSpec is not an

     * absolute URI or uriSpec violates syntax rules

     */

    private void initialize( URI base, String uriSpec )

        throws MalformedURIException

    {

        int    uriSpecLen;

        int    index;

        int    startPos;

        char   testChar;



        if ( base == null && ( uriSpec == null || uriSpec.trim().length() == 0) )

            throw new MalformedURIException( "Argument base is null and argument uriSpec is null or an empty string" );

        // just make a copy of the base if spec is empty

        if ( uriSpec == null || uriSpec.trim().length() == 0 ) {

            initialize( base );

            return;

        }



        uriSpec = uriSpec.trim();

        uriSpecLen = uriSpec.length();

        index = 0;



        // check for scheme

        if ( uriSpec.indexOf( ':' ) == -1 ) {

            if ( base == null )

                throw new MalformedURIException( "No scheme found in URI." );

        } else {

            initializeScheme( uriSpec );

            index = _scheme.length() + 1;

        }



        // two slashes means generic URI syntax, so we get the authority

        if ( ( index + 1 < uriSpecLen ) && ( uriSpec.substring( index ).startsWith( "//" ) ) ) {

            index += 2;

            startPos = index;

            // get authority - everything up to path, query or fragment

            testChar = '\0';

            while ( index < uriSpecLen ) {

                testChar = uriSpec.charAt( index );

                if ( testChar == '/' || testChar == '?' || testChar == '#' )

                    break;

                index++;

            }



            // if we found authority, parse it out, otherwise we set the

            // host to empty string

            if ( index > startPos )

                initializeAuthority( uriSpec.substring( startPos, index ) );

            else

                _host = "";

        }



        initializePath( uriSpec.substring( index ) );



        // Resolve relative URI to base URI - see RFC 2396 Section 5.2

        // In some cases, it might make more sense to throw an exception

        // (when scheme is specified is the string spec and the base URI

        // is also specified, for example), but we're just following the

        // RFC specifications

        if ( base != null ) {



            // check to see if this is the current doc - RFC 2396 5.2 #2

            // note that this is slightly different from the RFC spec in that

            // we don't include the check for query string being null

            // - this handles cases where the urispec is just a query

            // string or a fragment (e.g. "?y" or "#s") -

            // see <http://www.ics.uci.edu/~fielding/url/test1.html> which

            // identified this as a bug in the RFC

            if ( _path.length() == 0 && _scheme == null && _host == null ) {

                _scheme = base.getScheme();

                _userinfo = base.getUserinfo();

                _host = base.getHost();

                _port = base.getPort();

                _path = base.getPath();

                if ( _queryString == null )

                    _queryString = base.getQueryString();

                return;

            }



            // check for scheme - RFC 2396 5.2 #3

            // if we found a scheme, it means absolute URI, so we're done

            if ( _scheme == null )

                _scheme = base.getScheme();

            else

                return;



            // check for authority - RFC 2396 5.2 #4

            // if we found a host, then we've got a network path, so we're done

            if ( _host == null ) {

                _userinfo = base.getUserinfo();

                _host = base.getHost();

                _port = base.getPort();

            } else

                return;



            // check for absolute path - RFC 2396 5.2 #5

            if ( _path.length() > 0 && _path.startsWith( "/" ) )

                return;



            // if we get to this point, we need to resolve relative path

            // RFC 2396 5.2 #6

            String tmpPath = new String();

            String basePath = base.getPath();



            // 6a - get all but the last segment of the base URI path

            if ( basePath != null ) {

                int lastSlash = basePath.lastIndexOf( '/' );

                if ( lastSlash != -1 )

                    tmpPath = basePath.substring( 0, lastSlash + 1 );

            }



            // 6b - append the relative URI path

            tmpPath = tmpPath.concat( tmpPath );

            // 6c - remove all "./" where "." is a complete path segment

            index = -1;

            while ( ( index = tmpPath.indexOf( "/./" ) ) != -1 )

                tmpPath = tmpPath.substring( 0, index + 1 ).concat( tmpPath.substring( index + 3 ) );



            // 6d - remove "." if path ends with "." as a complete path segment

            if ( tmpPath.endsWith("/.") )

                tmpPath = tmpPath.substring( 0, tmpPath.length() - 1 );



            // 6e - remove all "<segment>/../" where "<segment>" is a complete

            // path segment not equal to ".."

            index = -1;



            int segIndex = -1;

            String tempString = null;



            while ( ( index = tmpPath.indexOf( "/../" ) ) > 0 ) {

                tempString = tmpPath.substring( 0, tmpPath.indexOf( "/../" ) );

                segIndex = tempString.lastIndexOf( '/' );

                if ( segIndex != -1 )

                    if ( !tempString.substring( segIndex++ ).equals( ".." ) )

                        tmpPath = tmpPath.substring( 0, segIndex ).concat( tmpPath.substring( index + 4 ) );

            }



            // 6f - remove ending "<segment>/.." where "<segment>" is a

            // complete path segment

            if ( tmpPath.endsWith( "/.." ) ) {

                tempString = tmpPath.substring( 0, tmpPath.length() - 3 );

                segIndex = tempString.lastIndexOf( '/' );

                if ( segIndex != -1 )

                    tmpPath = tmpPath.substring( 0, segIndex + 1 );

            }

            _path = tmpPath;

        }

    }





    /**

     * Initialize the scheme for this URI from a URI string spec.

     *

     * @param uriSpec the URI specification (cannot be null)

     * @throws MalformedURIException URI does not have a conformant scheme

     */

    private void initializeScheme( String uriSpec )

        throws MalformedURIException

    {

        int uriSpecLen = uriSpec.length();

        int index = 0;

        String scheme = null;

        char testChar = '\0';



        while ( index < uriSpecLen ) {

            testChar = uriSpec.charAt( index );

            if ( testChar == ':' || testChar == '/' || testChar == '?' || testChar == '#' )

                break;

            index++;

        }

        scheme = uriSpec.substring( 0, index );

        if ( scheme.length() == 0 )

            throw new MalformedURIException( "No scheme found in URI." );

        else

            setScheme( scheme );

    }





    /**

     * Initialize the authority (userinfo, host and port) for this

     * URI from a URI string spec.

     *

     * @param uriSpec the URI specification (cannot be null)

     * @throws MalformedURIException uriSpec violates syntax rules

     */

    private void initializeAuthority( String uriSpec )

        throws MalformedURIException

    {

        int index = 0;

        int start = 0;

        int end = uriSpec.length();

        char testChar = '\0';

        String userinfo = null;



        // userinfo is everything up @

        if ( uriSpec.indexOf( '@', start ) != -1 ) {

            while ( index < end ) {

                testChar = uriSpec.charAt( index );

                if ( testChar == '@' )

                    break;

                index++;

            }

            userinfo = uriSpec.substring( start, index );

            index++;

        }



        // host is everything up to ':'

        String host = null;



        start = index;

        while ( index < end ) {

            testChar = uriSpec.charAt( index );

            if ( testChar == ':' )

                break;

            index++;

        }

        host = uriSpec.substring( start, index );



        int port = -1;



        if ( host.length() > 0 ) {

            // port

            if ( testChar == ':' ) {

                index++;

                start = index;

                while ( index < end )

                    index++;



                String portStr = uriSpec.substring( start, index );

                if ( portStr.length() > 0 ) {

                    for ( int i = 0 ; i < portStr.length() ; i++ )

                        if ( !isDigit( portStr.charAt( i ) ) )

                            throw new MalformedURIException( portStr + " is invalid. Port should only contain digits!" );

                    try {

                        port = Integer.parseInt( portStr );

                    } catch ( NumberFormatException nfe ) {

                        // can't happen

                    }

                }

            }

        }



        setHost( host );

        setPort( port );

        setUserinfo( userinfo );

    }





    /**

     * Initialize the path for this URI from a URI string spec.

     *

     * @param uriSpec the URI specification (cannot be null)

     * @throws MalformedURIException uriSpec violates syntax rules

     */

    private void initializePath( String uriSpec )

        throws MalformedURIException

    {

        if ( uriSpec == null )

            throw new MalformedURIException( "Argument uriSpec is null" );



        int index = 0;

        int start = 0;

        int end = uriSpec.length();

        char testChar = '\0';



        // path - everything up to query string or fragment

        while ( index < end ) {

            testChar = uriSpec.charAt( index );

            if ( testChar == '?' || testChar == '#' )

                break;

            // check for valid escape sequence

            if ( testChar == '%' ) {

                if ( index + 2 >= end || ! isHex( uriSpec.charAt( index + 1 ) ) ||

                     ! isHex( uriSpec.charAt( index + 2 ) ) )

                    throw new MalformedURIException( "Path contains invalid escape sequence!" );

            } else if ( ! isReservedCharacter( testChar ) &&

                        ! isUnreservedCharacter( testChar ) ) {

                if ( '\\' != testChar )

                    throw new MalformedURIException( "Path contains invalid character: " + testChar );

            }

            index++;

        }

        _path = uriSpec.substring( start, index );



        // query - starts with ? and up to fragment or end

        if ( testChar == '?' ) {

            index++;

            start = index;

            while ( index < end ) {

                testChar = uriSpec.charAt( index );

                if ( testChar == '#' )

                    break;

                if ( testChar == '%' ) {

                    if ( index + 2 >= end || ! isHex( uriSpec.charAt( index + 1 ) ) ||

                  ! isHex( uriSpec.charAt( index + 2 ) ) )

                        throw new MalformedURIException( "Query string contains invalid escape sequence!" );

                } else if ( ! isReservedCharacter( testChar ) &&

                            ! isUnreservedCharacter( testChar ) )

                    throw new MalformedURIException( "Query string contains invalid character:" + testChar );

                index++;

            }

            _queryString = uriSpec.substring( start, index );

        }



        // fragment - starts with #

        if ( testChar == '#' ) {

            index++;

            start = index;

            while ( index < end ) {

                testChar = uriSpec.charAt( index );

                if ( testChar == '%' ) {

                    if ( index + 2 >= end || ! isHex( uriSpec.charAt( index + 1 ) ) ||

                         !isHex( uriSpec.charAt( index + 2 ) ) )

                        throw new MalformedURIException( "Fragment contains invalid escape sequence!" );

                } else if ( ! isReservedCharacter( testChar ) &&

                            ! isUnreservedCharacter( testChar ) )

                    throw new MalformedURIException( "Fragment contains invalid character:" + testChar );

                index++;

            }

            _fragment = uriSpec.substring( start, index );

        }

    }





    /**

     * Get the scheme for this URI.

     *

     * @return the scheme for this URI

     */

    public String getScheme()

    {

        return _scheme;

    }





    /**

     * Get the scheme-specific part for this URI (everything following the

     * scheme and the first colon). See RFC 2396 Section 5.2 for spec.

     *

     * @return the scheme-specific part for this URI

     */

    public String getSchemeSpecificPart()

    {

        StringBuffer schemespec = new StringBuffer();

        if ( _userinfo != null || _host != null || _port != -1 ) {

            schemespec.append( "//" );

            if ( _userinfo != null) {

                schemespec.append( _userinfo );

                schemespec.append( '@' );

            }

            if ( _host != null )

                schemespec.append( _host );

            if ( _port != -1 ) {

                schemespec.append( ':' );

                schemespec.append( _port );

            }

        }

        if ( _path != null )

            schemespec.append( _path );

        if ( _queryString != null ) {

            schemespec.append( '?' );

            schemespec.append( _queryString );

        }

        if ( _fragment != null ) {

            schemespec.append( '#' );

            schemespec.append( _fragment );

        }

        return schemespec.toString();

    }





    /**

     * Get the userinfo for this URI.

     *

     * @return the userinfo for this URI (null if not specified).

     */

    public String getUserinfo()

    {

        return _userinfo;

    }





    /**

     * Get the host for this URI.

     *

     * @return the host for this URI (null if not specified).

     */

    public String getHost()

    {

        return _host;

    }





    /**

     * Get the port for this URI.

     *

     * @return the port for this URI (-1 if not specified).

     */

    public int getPort()

    {

        return _port;

    }





    /**

     * Get the path for this URI (optionally with the query string and

     * fragment).

     *

     * @param includeQueryString if true (and query string is not null),

     * then a "?" followed by the query string will be appended

     * @param includeFragment if true (and fragment is not null),

     * then a "#" followed by the fragment will be appended

     * @return the path for this URI possibly including the query string and fragment

     */

    public String getPath( boolean includeQueryString,

                           boolean includeFragment )

    {

        StringBuffer pathString = new StringBuffer( _path );

        if ( includeQueryString && _queryString != null ) {

            pathString.append( '?' );

            pathString.append( _queryString );

        }

        if ( includeFragment && _fragment != null ) {

            pathString.append( '#' );

            pathString.append( _fragment );

        }

        return pathString.toString();

    }





    /**

     * Get the path for this URI. Note that the value returned is the path

     * only and does not include the query string or fragment.

     *

     * @return the path for this URI.

     */

    public String getPath()

    {

        return _path;

    }





    /**

     * Get the query string for this URI.

     *

     * @return the query string for this URI. Null is returned if there

     * was no "?" in the URI spec, empty string if there was a "?" but no

     * query string following it.

     */

    public String getQueryString()

    {

        return _queryString;

    }





    /**

     * Get the fragment for this URI.

     *

     * @return the fragment for this URI. Null is returned if there

     * was no "#" in the URI spec, empty string if there was a

     *  "#" but no fragment following it.

     */

    public String getFragment()

    {

        return _fragment;

    }





    /**

     * Set the scheme for this URI. The scheme is converted to lowercase

     * before it is set.

     *

     * @param scheme the scheme for this URI (cannot be null)

     * @throws MalformedURIException scheme is not a conformant scheme name

     */

    public void setScheme( String scheme )

        throws MalformedURIException

    {

        if ( scheme == null )

            throw new MalformedURIException( "Argument scheme is null" );

        if ( ! isConformantSchemeName( scheme ) )

            throw new MalformedURIException( "The scheme is not conformant." );

        _scheme = scheme.toLowerCase();

    }





    /**

     * Set the userinfo for this URI. If a non-null value is passed in and

     * the host value is null, then an exception is thrown.

     *

     * @param userinfo the userinfo for this URI

     * @throws MalformedURIException userinfo contains invalid characters

     */

    public void setUserinfo( String userinfo )

        throws MalformedURIException

    {

        if ( userinfo == null )

            _userinfo = null;

        else {

            if ( _host == null)

                throw new MalformedURIException( "Userinfo cannot be set when host is null!" );

            // userinfo can contain alphanumerics, mark characters, escaped

            // and ';',':','&','=','+','$',','

            int index = 0;

            int end = userinfo.length();

            char testChar = '\0';



            while ( index < end ) {

                testChar = userinfo.charAt( index );

                if ( testChar == '%' ) {

                    if ( index + 2 >= end || ! isHex( userinfo.charAt( index + 1 ) ) ||

                         ! isHex( userinfo.charAt( index + 2 ) ) )

                        throw new MalformedURIException( "Userinfo contains invalid escape sequence!" );

                } else if ( ! isUnreservedCharacter( testChar ) && USERINFO_CHARACTERS.indexOf( testChar ) == -1 )

                    throw new MalformedURIException( "Userinfo contains invalid character:" + testChar );

                index++;

            }

        }

        _userinfo = userinfo;

    }





    /**

     * Set the host for this URI. If null is passed in, the userinfo

     * field is also set to null and the port is set to -1.

     *

     * @param host the host for this URI

     * @throws MalformedURIException host is not a valid IP address or DNS hostname.

     */

    public void setHost( String host )

        throws MalformedURIException

    {

        if ( host == null || host.trim().length() == 0 ) {

            _host = host;

            _userinfo = null;

            _port = -1;

        } else if ( ! isWellFormedAddress( host ) )

            throw new MalformedURIException( "Host is not a well formed address!" );

        _host = host;

    }





    /**

     * Set the port for this URI. -1 is used to indicate that the port is

     * not specified, otherwise valid port numbers are  between 0 and 65535.

     * If a valid port number is passed in and the host field is null,

     * an exception is thrown.

     *

     * @param port the port number for this URI

     * @throws MalformedURIException port is not -1 and not a valid port number

     */

    public void setPort( int port )

        throws MalformedURIException

    {

        if ( port >= 0 && port <= 65535 ) {

            if ( _host == null )

                throw new MalformedURIException( "Port cannot be set when host is null!" );

        } else if ( port != -1 )

            throw new MalformedURIException( "Invalid port number!" );

        _port = port;

    }





    /**

     * Set the path for this URI. If the supplied path is null, then the

     * query string and fragment are set to null as well. If the supplied

     * path includes a query string and/or fragment, these fields will be

     * parsed and set as well. Note that, for URIs following the "generic

     * URI" syntax, the path specified should start with a slash.

     * For URIs that do not follow the generic URI syntax, this method

     * sets the scheme-specific part.

     *

     * @param path the path for this URI (may be null)

     * @throws MalformedURIException path contains invalid characters

     */

    public void setPath( String path )

        throws MalformedURIException

    {

        if ( path == null ) {

            _path = null;

            _queryString = null;

            _fragment = null;

        } else

            initializePath( path );

    }





    /**

     * Append to the end of the path of this URI. If the current path does

     * not end in a slash and the path to be appended does not begin with

     * a slash, a slash will be appended to the current path before the

     * new segment is added. Also, if the current path ends in a slash

     * and the new segment begins with a slash, the extra slash will be

     * removed before the new segment is appended.

     *

     * @param addToPath the new segment to be added to the current path

     * @exception MalformedURIException addToPath contains syntax errors

     */

    public void appendPath( String addToPath )

        throws MalformedURIException

    {

        if ( addToPath == null || addToPath.trim().length() == 0 )

            return;

        if ( ! isURIString( addToPath ) )

            throw new MalformedURIException( "Path contains invalid character!" );

        if ( _path == null || _path.trim().length() == 0 ) {

            if ( addToPath.startsWith( "/" ) )

                _path = addToPath;

            else

                _path = "/" + addToPath;

        } else if ( _path.endsWith( "/" ) ) {

            if ( addToPath.startsWith( "/" ) )

                _path = _path.concat( addToPath.substring( 1 ) );

            else

                _path = _path.concat( addToPath );

        } else {

            if ( addToPath.startsWith( "/" ) )

                _path = _path.concat( addToPath );

            else

                _path = _path.concat( "/" + addToPath );

        }

    }





    /**

     * Set the query string for this URI. A non-null value is valid only

     * if this is an URI conforming to the generic URI syntax and

     * the path value is not null.

     *

     * @param queryString the query string for this URI

     * @exception MalformedURIException queryString is not null and this

     * URI does not conform to the generic URI syntax or if the path is null

     */

    public void setQueryString( String queryString )

        throws MalformedURIException

    {

        if ( queryString == null )

            _queryString = null;

        else if ( ! isGenericURI() )

            throw new MalformedURIException( "Query string can only be set for a generic URI!" );

        else if ( getPath() == null )

            throw new MalformedURIException( "Query string cannot be set when path is null!" );

        else if ( ! isURIString( queryString ) )

            throw new MalformedURIException( "Query string contains invalid character!" );

        else

            _queryString = queryString;

    }





    /**

     * Set the fragment for this URI. A non-null value is valid only

     * if this is a URI conforming to the generic URI syntax and

     * the path value is not null.

     *

     * @param fragment the fragment for this URI

     * @exception MalformedURIException fragment is not null and this

     * URI does not conform to the generic URI syntax or if the path is null

     */

    public void setFragment( String fragment )

        throws MalformedURIException

    {

        if ( fragment == null )

            _fragment = null;

        else if ( ! isGenericURI() )

            throw new MalformedURIException( "Fragment can only be set for a generic URI!" );

        else if ( getPath() == null )

            throw new MalformedURIException( "Fragment cannot be set when path is null!" );

        else if ( ! isURIString( fragment ) )

            throw new MalformedURIException( "Fragment contains invalid character!" );

        else

            _fragment = fragment;

    }





    /**

     * Determines if the passed-in Object is equivalent to this URI.

     *

     * @param test the Object to test for equality.

     * @return true if test is a URI with all values equal to this

     * URI, false otherwise

     */

    public boolean equals( Object test )

    {

        if ( test instanceof URI ) {

            URI testURI = (URI) test;

            return ( ( ( _scheme == null && testURI._scheme == null ) ||

                       ( _scheme != null && testURI._scheme != null && _scheme.equals( testURI._scheme) ) ) &&

                     ( ( _userinfo == null && testURI._userinfo == null ) ||

                       ( _userinfo != null && testURI._userinfo != null && _userinfo.equals( testURI._userinfo ) ) ) &&

                     ( ( _host == null && testURI._host == null ) ||

                       ( _host != null && testURI._host != null && _host.equals( testURI._host ) ) ) &&

                     _port == testURI._port &&

                     ( ( _path == null && testURI._path == null ) ||

                       ( _path != null && testURI._path != null && _path.equals( testURI._path ) ) ) &&

                     ( ( _queryString == null && testURI._queryString == null ) ||

                       ( _queryString != null && testURI._queryString != null &&

                         _queryString.equals( testURI._queryString ) ) ) &&

                     ( ( _fragment == null && testURI._fragment == null ) ||

                       ( _fragment != null && testURI._fragment != null && _fragment.equals( testURI._fragment ) ) ) );

        }

        return false;

    }





    /**

     * Get the URI as a string specification. See RFC 2396 Section 5.2.

     *

     * @return the URI string specification

     */

    public String toString()

    {

        StringBuffer uriSpecString = new StringBuffer();



        if ( _scheme != null ) {

            uriSpecString.append( _scheme );

            uriSpecString.append( ':' );

        }

        uriSpecString.append( getSchemeSpecificPart() );

        return uriSpecString.toString();

    }

    /**
     * Returns the hash code of this URI
     *
     * @return the hash code of this URI
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**

     * Get the indicator as to whether this URI uses the "generic URI"

     * syntax.

     *

     * @return true if this URI uses the "generic URI" syntax, false otherwise

     */

    public boolean isGenericURI()

    {

        // presence of the host (whether valid or empty) means

        // double-slashes which means generic uri

        return ( _host != null );

    }





    /**

     * Determine whether a scheme conforms to the rules for a scheme name.

     * A scheme is conformant if it starts with an alphanumeric, and

     * contains only alphanumerics, '+','-' and '.'.

     *

     *

     * @param scheme The sheme name to check

     * @return true if the scheme is conformant, false otherwise

     */

    public static boolean isConformantSchemeName( String scheme )

    {

        if ( scheme == null || scheme.trim().length() == 0 )

            return false;

        if ( ! isAlpha( scheme.charAt( 0 ) ) )

            return false;

        char testChar;

        for ( int i = 1 ; i < scheme.length() ; i++ ) {

            testChar = scheme.charAt( i );

            if ( ! isAlphanum( testChar ) && SCHEME_CHARACTERS.indexOf( testChar ) == -1 )

                return false;

        }

        return true;

    }





    /**

     * Determine whether a string is syntactically capable of representing

     * a valid IPv4 address or the domain name of a network host. A valid

     * IPv4 address consists of four decimal digit groups separated by a

     * '.'. A hostname consists of domain labels (each of which must

     * begin and end with an alphanumeric but may contain '-') separated

     * & by a '.'. See RFC 2396 Section 3.2.2.

     *

     * @param address The address string to check

     * @return true if the string is a syntactically valid IPv4 address or hostname

     */

    public static boolean isWellFormedAddress( String address )

    {

        char testChar;



        if ( address == null )

            return false;

        address = address.trim();

        int addrLength = address.length();



        if ( addrLength == 0 || addrLength > 255 )

            return false;



        if ( address.startsWith( "." ) || address.startsWith( "-" ) )

            return false;



        // rightmost domain label starting with digit indicates IP address

        // since top level domain label can only start with an alpha

        // see RFC 2396 Section 3.2.2

        int index = address.lastIndexOf( '.' );

        if ( address.endsWith( "." ) )

            index = address.substring( 0, index ).lastIndexOf( '.' );

        if ( index + 1 < addrLength && isDigit( address.charAt( index + 1 ) ) ) {

            int numDots = 0;



            // make sure that 1) we see only digits and dot separators, 2) that

            // any dot separator is preceded and followed by a digit and

            // 3) that we find 3 dots

            for ( int i = 0 ; i < addrLength ; i++) {

                testChar = address.charAt( i );

                if ( testChar == '.' ) {

                    if ( ! isDigit( address.charAt( i - 1 ) ) ||

                         ( i + 1 < addrLength && ! isDigit( address.charAt( i + 1 ) ) ) )

                        return false;



                    numDots++;

                } else if ( ! isDigit( testChar ) )

                    return false;

            }

            if ( numDots != 3 )

                return false;

        } else {

            // domain labels can contain alphanumerics and '-"

            // but must start and end with an alphanumeric

            for ( int i = 0 ; i < addrLength ; i++ ) {

                testChar = address.charAt( i );

                if ( testChar == '.' ) {

                    if ( ! isAlphanum( address.charAt( i - 1 ) ) )

                        return false;

                    if ( i + 1 < addrLength && ! isAlphanum( address.charAt( i + 1 ) ) )

                        return false;

                } else if ( ! isAlphanum( testChar ) && testChar != '-' )

                    return false;

            }

        }

        return true;

    }





    /**

     * Determine whether a char is a digit.

     *

     * @param ch the character to check

     * @return true if the char is betweeen '0' and '9', false otherwise

     */

    private static boolean isDigit( char ch )

    {

        return ch >= '0' && ch <= '9';

    }





    /**

     * Determine whether a character is a hexadecimal character.

     *

     * @param ch the character to check

     * @return true if the char is betweeen '0' and '9', 'a' and 'f'

     * or 'A' and 'F', false otherwise

     */

    private static boolean isHex( char ch )

    {

        return ( isDigit( ch ) || ( ch >= 'a' && ch <= 'f' ) ||

                 ( ch >= 'A' && ch <= 'F' ) );

    }





    /**

     * Determine whether a char is an alphabetic character: a-z or A-Z

     *

     * @param ch the character to check

     * @return true if the char is alphabetic, false otherwise

     */

    private static boolean isAlpha( char ch )

    {

        return ( ( ch >= 'a' && ch <= 'z' ) ||

                 ( ch >= 'A' && ch <= 'Z' ) );

    }





    /**

     * Determine whether a char is an alphanumeric: 0-9, a-z or A-Z

     *

     * @param ch the character to check

     * @return true if the char is alphanumeric, false otherwise

     */

    private static boolean isAlphanum( char ch )

    {

        return ( isAlpha( ch ) || isDigit( ch ) );

    }





    /**

     * Determine whether a character is a reserved character:

     * ';', '/', '?', ':', '@', '&', '=', '+', '$' or ','

     *

     * @param ch the character to check

     * @return true if the string contains any reserved characters

     */

    private static boolean isReservedCharacter( char ch )

    {

        return RESERVED_CHARACTERS.indexOf( ch ) != -1;

    }





    /**

     * Determine whether a char is an unreserved character.

     *

     * @param ch the character to check

     * @return true if the char is unreserved, false otherwise

     */

    private static boolean isUnreservedCharacter( char ch )

    {

        return ( isAlphanum( ch ) || MARK_CHARACTERS.indexOf( ch ) != -1 );

    }





    /**

     * Determine whether a given string contains only URI characters (also

     * called "uric" in RFC 2396). uric consist of all reserved

     * characters, unreserved characters and escaped characters.

     *

     * @param uric URI string

     * @return true if the string is comprised of uric, false otherwise

     */

    private static boolean isURIString( String uric )

    {

        if ( uric == null )

            return false;

        int end = uric.length();

        char testChar = '\0';

        for ( int i = 0 ; i < end ; i++ ) {

            testChar = uric.charAt( i );

            if ( testChar == '%' ) {

                if ( i + 2 >= end || ! isHex( uric.charAt( i + 1 ) ) ||

                     ! isHex( uric.charAt( i + 2 ) ) )

                    return false;

                else {

                    i += 2;

                    continue;

                }

            }

            if ( isReservedCharacter( testChar ) || isUnreservedCharacter( testChar ) )

                continue;

            else

                return false;

        }

        return true;

    }





}

