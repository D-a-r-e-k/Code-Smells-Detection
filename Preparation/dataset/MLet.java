/**
* The XMOJO Project 5
* Copyright � 2003 XMOJO.org. All rights reserved.

* NO WARRANTY

* BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY FOR
* THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN
* OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES
* PROVIDE THE LIBRARY "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED
* OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS
* TO THE QUALITY AND PERFORMANCE OF THE LIBRARY IS WITH YOU. SHOULD THE
* LIBRARY PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,
* REPAIR OR CORRECTION.

* IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL
* ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR REDISTRIBUTE
* THE LIBRARY AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
* GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
* USE OR INABILITY TO USE THE LIBRARY (INCLUDING BUT NOT LIMITED TO LOSS OF
* DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
* PARTIES OR A FAILURE OF THE LIBRARY TO OPERATE WITH ANY OTHER SOFTWARE),
* EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGES.
**/

package javax.management.loading;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.RuntimeOperationsException;
import javax.management.NotCompliantMBeanException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.ServiceNotFoundException;

import com.adventnet.agent.logging.Log;
import com.adventnet.agent.logging.LogFactory;


/**
 * This MLet service allows to instantiate and register one or several MBeans
 * in the MBean server coming from a remote URL. M-let is a shortcut for
 * management applet. The m-let service does this by loading an m-let text file,
 * which specifies information on the MBeans to be obtained. The information
 * on each MBean is specified in a single instance of a tag, called the MLET tag.
 * The location of the m-let text file is specified by a URL.
 * <p>
 * The <CODE>MLET</CODE> tag has the following syntax:
 * <p>
 * &lt;<CODE>MLET</CODE><BR>
 *      <CODE>CODE = </CODE><VAR>class</VAR><CODE> | OBJECT = </CODE><VAR>serfile</VAR><BR>
 *      <CODE>ARCHIVE = &quot;</CODE><VAR>archiveList</VAR><CODE>&quot;</CODE><BR>
 *      <CODE>[CODEBASE = </CODE><VAR>codebaseURL</VAR><CODE>]</CODE><BR>
 *      <CODE>[NAME = </CODE><VAR>mbeanname</VAR><CODE>]</CODE><BR>
 *      <CODE>[VERSION = </CODE><VAR>version</VAR><CODE>]</CODE><BR>
 * &gt;<BR>
 *	<CODE>[</CODE><VAR>arglist</VAR><CODE>]</CODE><BR>
 * &lt;<CODE>/MLET</CODE>&gt;
 * <p>
 * where:
 * <DL>
 * <DT><CODE>CODE = </CODE><VAR>class</VAR></DT>
 * <DD>
 * This attribute specifies the full Java class name, including package name,
 * of the MBean to be obtained. The compiled <CODE>.class</CODE> file of the
 * MBean must be contained in one of the <CODE>.jar</CODE> files specified by
 * the <CODE>ARCHIVE</CODE> attribute. Either <CODE>CODE</CODE> or
 * <CODE>OBJECT</CODE> must be present.
 * </DD>
 * <DT><CODE>OBJECT = </CODE><VAR>serfile</VAR></DT>
 * <DD>
 * This attribute specifies the <CODE>.ser</CODE> file that contains a
 * serialized representation of the MBean to be obtained. This file must be
 * contained in one of the <CODE>.jar</CODE> files specified by the
 * <CODE>ARCHIVE</CODE> attribute. If the <CODE>.jar</CODE> file contains a
 * directory hierarchy, specify the path of the file within this hierarchy.
 * Otherwise  a match will not be found. Either <CODE>CODE</CODE> or
 * <CODE>OBJECT</CODE> must be present.
 * </DD>
 * <DT><CODE>ARCHIVE = &quot;</CODE><VAR>archiveList</VAR><CODE>&quot;</CODE></DT>
 * <DD>
 * This mandatory attribute specifies one or more <CODE>.jar</CODE> files
 * containing MBeans or other resources used by the MBean to be obtained.
 * One of the <CODE>.jar</CODE> files must contain the file specified by
 * the <CODE>CODE</CODE> or <CODE>OBJECT</CODE> attribute.
 * If archivelist contains more than one file:
 * <UL>
 * <LI>Each file must be separated from the one that follows it by a comma (,).
 * <LI><VAR>archivelist</VAR> must be enclosed in double quote marks.
 * </UL>
 * All <CODE>.jar</CODE> files in <VAR>archivelist</VAR> must be stored in
 * the directory specified by the code base URL.
 * </DD>
 * <DT><CODE>CODEBASE = </CODE><VAR>codebaseURL</VAR></DT>
 * <DD>
 * This optional attribute specifies the code base URL of the MBean to be
 * obtained. It identifies the directory that contains the <CODE>.jar</CODE>
 * files specified by the <CODE>ARCHIVE</CODE> attribute. Specify this
 * attribute only if the <CODE>.jar</CODE> files are not in the same
 * directory as the m-let text file. If this attribute is not specified,
 * the base URL of the m-let text file is used.
 * </DD>
 * <DT><CODE>NAME = </CODE><VAR>mbeanname</VAR></DT>
 * <DD>
 * This optional attribute specifies the object name to be assigned to the
 * MBean instance when the m-let service registers it. If <VAR>mbeanname</VAR>
 * starts with the colon character (:), the domain part of the object name is
 * the domain of the agent. The m-let service invokes the
 * <CODE>getDomain()</CODE> method of the Framework class to
 * obtain this information.
 * </DD>
 * <DT><CODE>VERSION = </CODE><VAR>version</VAR></DT>
 * <DD>
 * This optional attribute specifies the version number of the MBean and
 * associated <CODE>.jar</CODE> files to be obtained. This version number can
 * be used to specify that the <CODE>.jar</CODE> files are loaded from the
 * server to update those stored locally in the cache the next time the m-let
 * text file is loaded. <VAR>version</VAR> must be a series of non-negative
 * decimal integers each separated by a period from the one that precedes it.
 * </DD>
 * <DT><VAR>arglist</VAR></DT>
 * <DD>
 * This optional attribute specifies a list of one or more parameters for the
 * MBean to be instantiated. This list describes the parameters to be passed
 * the MBean's constructor. Use the following syntax to specify each item in
 * <VAR>arglist</VAR>:</DD>
 * <DL>
 * <P>
 * <DT>&lt;<CODE>ARG TYPE=</CODE><VAR>argumentType</VAR> <CODE>VALUE=</CODE><VAR>value</VAR>&gt;</DT>
 * <P>
 * <DD>where:</DD>
 * <UL>
 * <LI><VAR>argumentType</VAR> is the type of the argument that will be passed
 * as parameter to the MBean's constructor.</UL>
 * </DL>
 * <P>The arguments' type in the argument list should be a Java primitive type
 * or a Java basic type (<CODE>java.lang.Boolean, java.lang.Byte,
 * java.lang.Short, java.lang.Long, java.lang.Integer, java.lang.Float,
 * java.lang.Double, java.lang.String</CODE>).
 * </DL>
 *
 * When an m-let text file is loaded, an instance of each MBean specified
 * in the file is created and registered.
 * <P>
 * The m-let Service extends the <CODE>java.net.URLClassLoader</CODE> and can
 * be used to load remote classes and jar files in the VM of the agent.
 * <p><STRONG>Note - </STRONG> The <CODE>MLet</CODE> class loader uses the
 * {@link javax.management.loading.DefaultLoaderRepository DefaultLoaderRepository}
 * to load classes that could not be found in the loaded jar files.
 */
public class MLet extends URLClassLoader implements MLetMBean, MBeanRegistration
{
 	/* Vector to store the list of MLets */
	protected Vector mletList = null;

    /* Variable to store the reference of the MBeanServer. */
	protected MBeanServer server = null;

    /* Variable to store the ObjectName of the MLet*/
    private ObjectName objectName = null;

    /* Parser variable used to parse the MLet text file. */
	private MLetParser parser = null;

    /* Variable to store the Library Directory used by the MLet */
    private String libDir = System.getProperty("user.dir");

    /*Variable to store the versions and the corresponding jarNames*/
    private Hashtable versionTable = null;

    /* Logger */
    private Log log;

    /* boolean to indicate whether to use default loader */
	boolean useDefaultLoader = false;

	/**
	 * Constructs a new MLet using the default delegation parent ClassLoader.
	 */
	public MLet()
	{
		this(new URL[0]);
		createLogger();
	}

	/**
	 * Constructs a new MLet for the specified URLs using the default delegation
	 * parent ClassLoader. The URLs will be searched in the order specified for
	 * classes and resources after first searching in the parent class loader.
	 *
	 * @param urls  the URLs from which to load classes and resources
	 */
	public MLet(URL[] urls)
	{
		super(urls);
		versionTable = new Hashtable();
		createLogger();
	}

	/**
	 * Constructs a new MLet for the given URLs. The URLs will be searched in
	 * the order specified for classes and resources after first searching in
	 * the specified parent class loader. URLStreamHandlerFactory. The parent
	 * argument will be used as the parent class loader for delegation. The
	 * factory argument will be used as the stream handler factory to obtain
	 * protocol handlers when creating new URLs.
	 *
	 * @param urls the URLs from which to load classes and resources
	 *
	 * @param parent the parent class loader for delegation
	 */
	public MLet(URL[] urls, ClassLoader parent)
	{
		super(urls,parent);
		versionTable = new Hashtable();
		createLogger();
	}

	/**
	 * Constructs a new MLet for the specified URLs, parent class loader, and
	 * URLStreamHandlerFactory. The parent argument will be used as the parent
	 * class loader for delegation. The factory argument will be used as the
	 * stream handler factory to obtain protocol handlers when creating new
	 * URLs.
	 *
	 * @param urls - the URLs from which to load classes and resources
	 *
	 * @param parent - the parent class loader for delegation
	 *
	 * @param factory - the URLStreamHandlerFactory to use when creating URLs
	 */
	public MLet(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
	{
		super(urls, parent, factory);
		versionTable = new Hashtable();
		createLogger();
	}

	/**
	 * Appends the specified URL to the list of URLs to search for classes and
	 * resources.
	 *
	 * @param url The url from which to load the MBeans in a string format.
	 *
	 * @exception javax.management.ServiceNotFoundException - The specified
	 * 				URL is malformed.
	 */
	public void addURL(String url) throws ServiceNotFoundException
	{
		try
		{
			URL Url = new URL(url);

			// if this URL object does not exists and then add it
			if(Arrays.asList(getURLs()).contains(url) == false)
				super.addURL(Url);
		}
		catch(MalformedURLException e)
		{
			log.warn("Check URL",e);
			throw new ServiceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Appends the specified URL to the list of URLs to search for classes and
	 * resources.
	 *
	 * @param url The url from which to load the MBeans in a string format.
	 *
	 * @overrides addURL in class java.net.URLClassLoader
	 */
	public void addURL(URL url)
	{
		// if this URL object does not exists and then add it
		if(Arrays.asList(getURLs()).contains(url) == false)
			super.addURL(url);
	}

	/**
	 * Gets the current directory used by the library loader for storing
	 * native libraries before they are loaded into memory.
	 *
	 * @return The current directory used by the library loader.
	 */
	public String getLibraryDirectory()
	{
		return libDir;
	}

	/**
	 * Sets the directory used by the library loader for storing
	 * native libraries before they are loaded into memory.
	 *
	 * @param libDir The directory used by the library loader.
	 */
	public void setLibraryDirectory(String libDir)
	{
		this.libDir = libDir;
	}

	/**
	 * Loads a text file containing MLET tags that define the MBeans to be
	 * added to the agent. The location of the text file is specified by a URL.
	 * The MBeans specified in the MLET file will be instantiated and
	 * registered by the MBeanServer.
	 *
	 * @param url - The URL of the text file to be loaded as String object.
	 *
	 * @return A set containing one entry per MLET tag in the m-let text file
	 * 				loaded. Each entry specifies either the ObjectInstance for
	 * 				the created MBean, or a throwable object (that is, an error
	 * 				or an exception) if the MBean could not be created.
	 *
	 * @exception javax.management.ServiceNotFoundException - One of the
	 * 				following errors has occurred:
	 * 				The m-let text file does not contain an MLET tag,
	 * 				the m-let text file is not found,
	 * 				a mandatory attribute of the MLET tag is not specified,
	 * 				the url is malformed.
	 *
 	 * @see MLetMBean#getMBeansFromURL(String)
	 */
	public Set getMBeansFromURL(String url) throws ServiceNotFoundException
	{
		try
		{
			return getMBeansFromURL(new URL(url));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(url,e);
			throw new ServiceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Loads a text file containing MLET tags that define the MBeans to be
	 * added to the agent. The location of the text file is specified by a URL.
	 * The MBeans specified in the MLET file will be instantiated and
	 * registered by the MBeanServer.
	 *
	 * @param url - The URL of the text file to be loaded as URL object.
	 *
	 * @return A set containing one entry per MLET tag in the m-let text file
	 * 				loaded. Each entry specifies either the ObjectInstance for
	 * 				the created MBean, or a throwable object (that is, an
	 * 				error or an exception) if the MBean could not be created.
	 *
	 * @exception javax.management.ServiceNotFoundException - One of the
	 * 				following errors has occurred:
	 * 				The m-let text file does not contain an MLET tag,
	 * 				the m-let text file is not found,
	 * 				a mandatory attribute of the MLET tag is not specified,
	 * 				the value of url is null.
	 *
	 * @see MLetMBean#getMBeansFromURL(URL)
	 */
	public Set getMBeansFromURL(URL url) throws ServiceNotFoundException
	{
		boolean isVersioning = false;

		//This hashset will be used to return the set
		HashSet mset = new HashSet();

		String protocol = url.getProtocol();
		String host = url.getHost();
		int port = url.getPort();

		try
		{
			URLConnection con = url.openConnection();
			String fix = new String();
			String tfix = new String();
			InputStreamReader dis = new InputStreamReader(con.getInputStream());
			BufferedReader stream = new BufferedReader(dis);
			fix = stream.readLine();

			while(fix != null)
			{
				int strIndex = fix.indexOf("VERSION");

				if( strIndex != -1)
				{
					String temp = fix;
					StringTokenizer tokenizer = new StringTokenizer(temp,"=");

					String firstHalf = tokenizer.nextToken();
					String ver = tokenizer.nextToken();
					ver = ver.trim();
					ver = "\""+ver+"\"";
					String secondHalf = ver;

					fix = firstHalf +" = "+secondHalf;

				}

				tfix = tfix +"\n"+ fix;
				tfix = tfix.replace('_','@');


				fix = stream.readLine();
			}

			StringReader r = new StringReader(tfix);
			parser = new MLetParser(r);
		}
		catch(ParseException pe)
		{
			log.error("Parse Error Exception ", pe);
			throw new ServiceNotFoundException(pe.getMessage());
		}
		catch(IOException ie)
		{
			 throw new ServiceNotFoundException(ie.getMessage());
		}
		catch (Exception ex)
		{
			log.error("Exception",ex);
			throw new ServiceNotFoundException(ex.getMessage());
		}

		Vector vec = parser.getMLetVector();

		for(int i =0;i<vec.size();i++)
		{
			URL toAdd = null;
			SimpleMLet mlet = (SimpleMLet)vec.elementAt(i);

			//  The following variables completely describe the MLet Tag.

			String codeBaseURL = mlet.getCodebase();
			String code = mlet.getCode();
			String serObject = mlet.getObject();
			Vector jarVector = mlet.getArchive();
			String name = mlet.getName();

			String version = mlet.getVersion();
			ArrayList argTypes = mlet.getArgTypes();
			ArrayList argValues = mlet.getArgValues();
			String className = null;
			if(code != null && code.endsWith("class"))
				className = code.substring(0,code.length()-6);

			//Step 1:
			//Add all the URLs appended with the jarNames and store the
			//version details in the versionTable.
			for(int count=0; count<jarVector.size(); count++)
			{
				String jarName = (String)jarVector.elementAt(count);
				String existingVersion = null;
				existingVersion = (String)(versionTable.get(jarName));
				if(existingVersion == null && version != null)
				{
					versionTable.put(jarName,version);
				}
				else
				{
					if(version != null)
					{
						if(! existingVersion.equals(version) )
						{
							isVersioning = true;
							//Version is changed. So need to reload the jar
							//files from the Server
						}
					}
				}
				//PERF --- < if the jar is already in the URL search path, dont add it all >
				try
				{

					if(codeBaseURL != null)
					{
						String testURL = getProperURLString(codeBaseURL,protocol,host,port,jarName);
						toAdd = new URL(testURL);

					}
					else
						toAdd = new URL(protocol, host, port, "/"+jarName);
				}
				catch(Exception e)
				{
//					e.printStackTrace();
					log.warn(e.getMessage()+" hence returning new HashSer");
					return new HashSet();
				}
				if(! isJarPresentInURLClasspath(toAdd))
				{
					super.addURL(toAdd);
				}
			}

			//Step 2:
			//Check for the Mandatory fields
			if(code == null && serObject == null)
			{
				log.error("BOTH CODE and OBJECT cannnot be null");
				throw new ServiceNotFoundException("Both CODE and OBJECT cannot be null");
			}

			if(code != null && serObject != null)
			{
				//Log Error...Both mandatory attributes cannot be present
				//for one MLet.
				log.error("Only one of the mandatory fields CODE/OBJECT must be present");
				mset.add( new Error("Only one of the mandatory fields CODE/OBJECT must be specified"));
				return mset;
			}

			//This is the object that will be registered.
			Object toReg = null;

			//Step 3:
			//Load the class if it is a Serialized file.
			if(serObject != null)
			{
				try
				{
					toReg = loadMBeanFromSerializedObject(this,serObject);
				}
				catch(Exception e)
				{
					mset.add(e);
                    e.printStackTrace();
					log.warn("loadMBeanFromSerializedObject",e);
					//Log the Error Messages from the deSerialization.
				}

				try
				{
					if(name != null)
					{
						mset.add(server.registerMBean(toReg,new ObjectName(name)));
					}
					else
					{
						mset.add(server.registerMBean(toReg,null));
					}
				}
				catch(InstanceAlreadyExistsException ie)
				{
					log.warn("",ie);
					mset.add(ie);
				}
				catch(MBeanRegistrationException mre)
				{
					log.warn("",mre);
					mset.add(mre);
				}
				catch(NotCompliantMBeanException nmbe)
				{
					log.warn("",nmbe);
					mset.add(nmbe);
				}
				catch(RuntimeOperationsException roe)
				{
					log.warn("",roe);
					mset.add(roe);
				}
				catch(MalformedObjectNameException me)
				{
					log.warn("",me);
					mset.add(me);
				}
			}
			else
			{
				try
				{
					//This means you have to load the class from the jar files.
					Class clazz = null;

					if(isVersioning)
					{
						clazz = super.findLoadedClass(mlet.getCode());
						if(clazz == null)
						{
							clazz = findClass(mlet.getCode());
						}

						isVersioning  = false;
					}

					//The above searches the entire URLS and loads the class.

					if(! (argTypes.size() == 0))
					{
						Object[] types = argTypes.toArray();
						Object[] values = argValues.toArray();

						String[] typesArr = new String[types.length];
						for(int j=0;j<types.length;j++)
						{
							typesArr[j] = (String)types[j];
						}

						if(name != null)
						{
							mset.add(server.createMBean(mlet.getCode(),
							  new ObjectName(name),objectName,values,typesArr));
						}
						else
						{
							mset.add(server.createMBean(mlet.getCode(),null,
											objectName,values,typesArr));
						}
					}
					else
					{
						if(name != null)
						{
							mset.add(server.createMBean(mlet.getCode(),
									new ObjectName(name),objectName));
						}
						else
						{
							mset.add(server.createMBean(mlet.getCode(),
														null,objectName));
						}
					}
				}
				catch(ClassNotFoundException e)
				{
					throw new ServiceNotFoundException(e.getMessage());
				}
				catch(Exception e)
				{
					e.printStackTrace();
					throw new ServiceNotFoundException(e.getMessage());
				}
			}
		}

		return mset;
	}//End of getMBeanFromURL(URL url)

	/**
	 * Returns the search path of URLs for loading classes and resources.
	 * This includes the original list of URLs specified to the constructor,
	 * along with any URLs subsequently appended by the addURL() method.
	 *
	 * @return This returns the search path of URLs for loading classes
	 * 			and resources
	 *
	 * @overrides getURLs in class java.net.URLClassLoader
	 *
	 * @see MLetMBean#getURLs()
	 */
	public URL[] getURLs()
	{
		return super.getURLs();
	}

	//MBeanRegistration implementation

	/**
	 * This method allows the m-let to perform any operations it needs before
	 * being registered in the MBeanServer. If the ObjectName is null,
	 * the m-let provides a default name for its registration :service=MLet
	 *
	 * @param server - The MBeanServer in which the m-let will be registered.
	 *
	 * @param name - The object name of the m-let.
	 *
	 * @return The name of the m-let registered.
	 *
	 * @exception java.lang.Exception - This exception should be caught by the
	 * 				MBeanServer and re-thrown as an MBeanRegistrationException.
	 *
	 * @see MBeanRegistration#preRegister(MBeanServer, ObjectName)
	 */
	public ObjectName preRegister(MBeanServer server, ObjectName name)
													throws Exception
	{
		this.server = server;

		if(name == null)
		{
			objectName = new ObjectName(":type=MLet");
			return objectName;
		}

		objectName = name;
		return objectName;
	}

	/**
	 * Allows the m-let to perform any operations needed after having been
	 * registered in the MBeanServer or after the registration has failed.
	 *
	 * @param registrationDone - Indicates whether or not the m-let has been
	 * 				successfully registered in the MBeanServer. The value
	 * 				false means that either the registration phase has failed.
	 *
 	 * @see MBeanRegistration#postRegister(Boolean)
	 */
	public void postRegister(Boolean registrationDone)
	{
	}

	/**
	 * Allows the m-let to perform any operations it needs before being
	 * de-registered by the MBeanServer.
	 *
	 * @exception java.langException - This exception should be caught by the
	 * 				MBeanServer and re-thrown as an MBeanRegistrationException.
	 *
	 * @see  MBeanRegistration#preDeregister()
	 */
	public void preDeregister() throws Exception
	{
	}

	/**
	 * Allows the m-let to perform any operations needed after having been
	 * de-registered in the MBeanServer.
	 *
	 * @see MBeanRegistration#postDeregister()
	 */
	public void postDeregister()
	{
	}

	/**
	 * This method reads the objects contents during Object Serialization.
	 *
	 * @param in The serialization input stream.
	 *
	 * @exception java.io.IOException Signals that an I/O exception of some
	 * 				sort has occurred.
	 *
	 * @exception java.lang.ClassNotFoundException The class for an object
	 * 				being restored cannot be found.
	 */
	public void readExternal(java.io.ObjectInput in)
                  throws java.io.IOException,
                         java.lang.ClassNotFoundException
	{
	}

	/**
	 * This method saves the objects contents during Object Serialization.
	 * @param out The serialization output stream.
	 *
	 * @exception java.io.IOException Signals that an I/O exception of some
	 * 				sort has occurred.
	 */
	public void writeExternal(java.io.ObjectOutput out)
                   throws java.io.IOException
	{
	}

	//-------------------------- Protected methods --------------------------//

	/**
	 * This is the main method for class loaders that is being redefined.
	 *
	 * @param name The name of the class.
	 *
	 * @return The resulting Class object.
	 *
	 * @exception java.lang.ClassNotFoundException The specified class
	 * 				could not be found.
	 */
	protected synchronized java.lang.Class findClass(java.lang.String name)
                             throws java.lang.ClassNotFoundException
	{
		Class clazz = null;
        try
        {
            clazz = super.findClass(name);
        }
		catch(ClassNotFoundException cnfe)
        {
        	//log Warning
        	log.warn("Class not available in the URLClassLoader path...Loading from the DefaultLoaderRepository");
		}

		if(clazz == null && !useDefaultLoader)
		{
            try
            {
				clazz = DefaultLoaderRepository.loadClass(name);
            }
			catch(ClassNotFoundException cnfe)
			{
				log.warn(name,cnfe);
            	throw cnfe;
			}
		}

		if(clazz == null)
			throw new ClassNotFoundException(name);

        return clazz;
	}

	 /**
	  * Returns the absolute path name of a native library. The VM invokes
	  * this method to locate the native libraries that belong to classes
	  * loaded with this class loader. Libraries are searched in the JAR files
	  * using first just the native library name and if not found the native
	  * library name together with the architecture-specific path name
      * (OSName/OSArch/OSVersion/lib/nativelibname), i.e.
      * <p>the library stat on Solaris SPARC 5.7 will be searched in the JAR
      * file as:
      * <ol>
      * <li>libstat.so
      * <li>SunOS/sparc/5.7/lib/libstat.so
      * </ol>
      * the library stat on Windows NT 4.0 will be searched in the JAR file as:
      * <ol>
      * <li>stat.dll
      * <li>WindowsNT/x86/4.0/lib/stat.dll
      * </ol>
      * If this method returns null, i.e. the libraries were not found in any of
      * the JAR files loaded with this class loader, the VM searches the library
      * along the path specified as the java.library.path property.
      * @overrides findLibrary in class java.lang.ClassLoader
      * @param libname the library name.
      * @return The absolute path of the native library.
	  */
	protected String findLibrary(String libname)
	{
		if(libname == null)
		{
		log.debug(" input is a null value..so returning null");
        	return null;
        }
		libname = System.mapLibraryName(libname);

		InputStream is = null;
		File file = null;
		FileOutputStream os = null;

		try{
			libname = libname.replace(File.separatorChar, '/');
			is = getResourceAsStream(libname);
            if(is != null)
            {
                file = new File(libDir, libname);

                if(file.exists())
                    file.delete();

                file.getParentFile().mkdirs();
                os = new FileOutputStream(file);

                int value = 0;
                while((value = is.read()) != -1)
				{
                    os.write(value);
				}
				is.close();
                os.close();
            }

			if(file != null && file.exists())
				return file.getAbsolutePath();

		}catch(Exception e){
			log.error("",e);
		}

		String os_name = deleteSpace(System.getProperty("os.name").trim());
		String os_version =
						deleteSpace(System.getProperty("os.version").trim());
		String os_arch = deleteSpace(System.getProperty("os.arch").trim());

		String fs = File.separator;
		libname = os_name + fs + os_arch + fs + os_version + fs + "lib" + fs + libname;

		try{
			libname = libname.replace(File.separatorChar, '/');
			is = getResourceAsStream(libname);
            if(is != null)
            {
                file = new File(libDir, libname);

                if(file.exists())
                    file.delete();

                file.getParentFile().mkdirs();
                os = new FileOutputStream(file);
                int value = 0;
                while((value = is.read()) != -1)
				{
                    os.write(value);
				}
                is.close();
                os.close();
            }

			if(file != null && file.exists())
				return file.getAbsolutePath();

		}catch(Exception e){
			log.error("",e);
		}
        return null;
	}

	//--------------------------- Package methods ---------------------------//

	void setUseDefaultLoader(boolean flag)
	{
		useDefaultLoader = flag;
	}

	synchronized Class findClassInMLet(String name) throws ClassNotFoundException
	{
		Class clazz = super.findClass(name);

		if(clazz == null)
			throw new ClassNotFoundException(name);

		return clazz;
	}

	//--------------------------- Private methods ---------------------------//

	/**
	 * This method loads the MBeans from the serialized file specified.
	 * It uses the URLClassLoader's getResourceAsStream() to read the
	 * specified serialized file and returns the Object.
	 *
	 * @param mlet - the MLet instance that defines this method.
	 *
	 * @param serObjectFileName - the serialized file name . It includes the
	 * 				durectory structure if one is present.
	 *
	 * @return the MBean object.
	 *
	 * @exception 	IOException
	 *
	 * @exception 	StreamCorruptedException
	 *
	 * @exception 	Exception
	 */
	private Object loadMBeanFromSerializedObject(MLet mlet, String serObjectFileName)
						throws IOException, StreamCorruptedException, Exception
	{
		Object toRet = null;
		InputStream inStr = getResourceAsStream(serObjectFileName);

		if(inStr == null)
		{
			log.error("Serialized file"+serObjectFileName+"not found");
			throw new Error("Serialized file "+serObjectFileName+" not found");
		}
		else
		{
			try
			{
				MLetObjectInputStream oStr = new MLetObjectInputStream(inStr,this);
				toRet = oStr.readObject();
				oStr.close();
			}
			catch(StreamCorruptedException sce)
			{
				log.error("Error while loading MBean from serialized object"+sce);
				throw sce;
			}
			catch(IOException ioe)
			{
				log.error("Error while loading MBean from serialized object"+ioe);
				throw ioe;
			}
			catch(Exception e)
			{
				log.error("Error while loading MBean from serialized object"+e);
				throw e;
			}
		}
		return toRet;
	}

	private String deleteSpace(String inStr)
	{
		char[] cha = inStr.toCharArray();
		int count = 0;

		for(int i=0;i<cha.length;i++)
		{
			if(cha[i] != ' ')
				count++;
		}

		char[] new_cha = new char[count];
		count = 0;

		for(int i=0;i<cha.length;i++)
		{
			if(cha[i] != ' ')
				new_cha[count++] = cha[i];
		}

		return new String(new_cha);
	}

	private Class getProperClass(String type)
	{
			if(type.endsWith("int[]"))
					return (new int[0]).getClass();
			else if(type.endsWith("long[]"))
					return (new long[0]).getClass();
			else if(type.endsWith("byte[]"))
					return (new byte[0]).getClass();
			else if(type.endsWith("float[]"))
					return (new float[0]).getClass();
			else if(type.endsWith("char[]"))
					return (new char[0]).getClass();
			else if(type.endsWith("short[]"))
					return (new short[0]).getClass();
			else if(type.endsWith("double[]"))
					return (new double[0]).getClass();
			else if(type.endsWith("boolean[]"))
					return (new boolean[0]).getClass();
			else if(type.endsWith("String[]"))
					return (new String[0]).getClass();

			if(type.endsWith("int"))
					return int.class;
			else if(type.endsWith("long"))
					return long.class;
			else if(type.endsWith("byte"))
					return byte.class;
			else if(type.endsWith("float"))
					return float.class;
			else if(type.endsWith("char"))
					return char.class;
			else if(type.endsWith("short"))
					return short.class;
			else if(type.endsWith("double"))
					return double.class;
			else if(type.endsWith("boolean"))
					return boolean.class;

			return null;
	}

	private boolean isJarPresentInURLClasspath(URL toAdd)
	{
		URL[] urls = super.getURLs();

		for(int i=0;i<urls.length;i++)
		{
			if(urls[i].equals(toAdd))
			{
				//the jar is already present in the searchpath.
				return true;
			}
		}

		return false;
	}

	private String getProperURLString(String codeBase, String protocol,
									  String host, int port, String jarName)
							throws MalformedURLException
	{
		String toRet = null;

		if(codeBase.indexOf(":") == -1)
		{
			//This is an relative path
			if(codeBase.startsWith("/") && codeBase.endsWith("/"))
			{
				//Construct the URL
				toRet = protocol+"://"+host+":"+port+codeBase+jarName;
			}
			else
			{
				toRet = protocol+"://"+host+":"+port+"/"+codeBase+"/"+jarName;
			}

			return toRet;
		}
		else if(codeBase.indexOf(".jar") == -1)
		{
			toRet = codeBase+"/"+jarName;
			new URL(toRet);
			return toRet;
		}
		else
		{
			new URL(codeBase);
			return codeBase;
		}
	}

	private void createLogger()
	{
		try
		{
			log=LogFactory.getInstance("JMX");
		}
		catch(Exception e)
		{
		}
	}
}//End of class MLet