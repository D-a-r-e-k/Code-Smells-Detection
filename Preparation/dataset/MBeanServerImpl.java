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

package com.adventnet.jmx;

//----------------------Importing the Java Packages --------------------------//
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;

//----------------------Importing the JMX Packages --------------------------//
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.DynamicMBean;
import javax.management.ObjectName;
import javax.management.ObjectInstance;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.MBeanRegistration;
import javax.management.MBeanInfo;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.NotificationBroadcaster;
import javax.management.QueryExp;
import javax.management.AttributeNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InvalidAttributeValueException;
import javax.management.IntrospectionException;
import javax.management.JMRuntimeException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.OperationsException;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;

import com.adventnet.agent.logging.Log;
import com.adventnet.agent.logging.LogFactory;
import com.adventnet.jmx.utils.JmxUtilities;

/**
 * This is the implementation class for MBean manipulation on the agent side.
 * It contains the necessary methods for the creation, registration,
 * and deletion of MBeans as well as the access methods for registered
 * MBeans. This is the core component of the JMX infrastructure.
 * @version C_VERSION
 */

public class MBeanServerImpl implements MBeanServer
{
    //-----------------------Variable Declaration-----------------------//

    /** The default domain of the MBeanServer **/
    private String defaultDomain = new String("DefaultDomain");

    /** The static list which maintains the MBeanServer instances **/
    private static ArrayList servers = new ArrayList();

    /**
     * ServerTable maintains all the MBeans registered with the JMX agent
     * key = ObjectName(mbean objectname) ; value = Object(mbean)
     */
    private Hashtable serverTable = null;

    /** The delegate object for the MBeanServer **/
    private MBeanServerDelegate delegate = null;

    /**
     * The table that maintains the Loader reference for each of the MBeans
     * Key = String(mbeans name); value = Object(loader)
     */
    private Hashtable loaderTable = null;

    /** The Notification seqenceNumber for the MBeanServerDelegate **/
    private long sequenceNumber = 1;

    private Log log=null;

    /**
     * Default Constructor
     */
    public MBeanServerImpl()
    {
        this("defaultdomain");

        try
		{
        	createLogger();
        }
		catch(Exception e)
		{
        	System.out.println("FATAL:Log Creation Failed");
        }
    }

    /** Creates an MBeanServer with the specified default domain name.**/
    public MBeanServerImpl(String defaultDomain)
    {
        try
		{
        	createLogger();
        }
		catch(Exception e)
		{
        	System.out.println("FATAL:Log Creation Failed");
        }

        //PERF
        //< 28-11-2001 serverTable is going to store all the MBeans. The default hashtable allocates space only for 10 objects. Practical applications must be having 100s of MBeans !!! -- Balaji>
        serverTable = new Hashtable();
        loaderTable = new Hashtable();

        delegate = new MBeanServerDelegate();

        try
		{
        	registerMBean(delegate, new ObjectName("JMImplementation:type=MBeanServerDelegate"));
        }
		catch(Exception e)
		{
            //TODO
            log.error("MBeanServerDelegate Registration Failed",e);
            //e.printStackTrace();
		}

	    //TODO
	    //< 28-11-2001 IS this really needed ??? It will keep on populating the loaders in the MBeanServer with objects of same type.--Bala >
	    //DefaultLoaderRepositoryExt.addLoader( new DefaultClassLoader());
	    DefaultLoaderRepositoryExt.addLoader(Thread.currentThread().getContextClassLoader());

	    this.defaultDomain = defaultDomain;
    }

    /**
     * Checks whether an MBean, identified by its object name, is already
	 * registered with the MBeanServer.
	 *
     * @param name The object name of the MBean to be checked.
     *
     * @return true if the MBean is already controlled by the MBeanServer,
     * 				false otherwise.
     */
    public boolean isRegistered(ObjectName name)
    {
		return serverTable.containsKey(name);
    }

    /**
     * Returns true if the MBean specified is an instance of the specified
     * class, false otherwise.
     * @param name The object name of the MBean to be checked.
     * @param className The name of the class.
     * @return true if the MBean is the instance of the specified class, false otherwise.
     * @exception InstanceNotFoundException The MBean specified is not registered in the MBean server.
     */
    public boolean isInstanceOf(ObjectName name, String className)
                    throws InstanceNotFoundException
    {
        Object obj = serverTable.get(name);
        Class clazz = null;

        if(obj == null)
                throw new InstanceNotFoundException();

        if(obj instanceof javax.management.modelmbean.RequiredModelMBean)
        {
            try
            {
                MBeanInfo info = this.getMBeanInfo(name);

                if(info.getClassName().equals(className))
                	return true;

                clazz = Thread.currentThread().getContextClassLoader()
											  .loadClass(className);
                if(clazz.isInstance(obj))
                	return true;
                else
                    return false;
            }
			catch(Exception e1)
			{
                //TODO
                log.warn("RMM loading failed",e1);
                return false;
            }
        }
        else if(obj instanceof com.adventnet.jmx.DefaultDynamicMBean)
        {
            Object toCheck = ((DefaultDynamicMBean)(obj)).getStandardMBeanObject();
            clazz = null;

            try
            {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(className);

                if(clazz != null && clazz.isInstance(toCheck))
                {
                    return true;
                }
                else
                {
					return false;
                }
            }
			catch(Exception e1)
            {
                log.warn("Failed to loadClass"+toCheck.getClass().getName(),e1);
                //e1.printStackTrace();
                return false;
            }
        }
        else
        {
            clazz = null;

            try
            {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            }
			catch(Exception e)
            {
                log.warn("isInstanceOf() : Failed to loadClass "+className);
                return false;
            }
            if(clazz != null && clazz.isInstance(obj))
            {
                return true;
            }
            else
            {
				return false;
            }
        }
    }

    /**
	 * Returns the default domain used for the MBean naming.
	 */
    public String getDefaultDomain()
    {
		return defaultDomain;
    }

    /**
     * Gets MBeans controlled by the MBeanServer. This method allows any of
	 * the following to be obtained: All MBeans, a set of MBeans specified by
	 * pattern matching on the ObjectName and/or a Query expression, a specific
	 * MBean. When the object name is null or empty, all objects are to be
	 * selected (and filtered if a query is specified). It returns the set of
	 * ObjectInstance objects (containing the ObjectName and the Java Class
	 * name) for the selected MBeans.
	 *
     * @param name The object name pattern identifying the MBeans to be
     * 				retrieved. If null orempty all the MBeans registered
	 * 				will be retrieved.
	 *
     * @param query The query expression to be applied for selecting MBeans.
     *
     * @return A set containing the ObjectInstance objects for the selected MBeans.
     * 				If no MBean satisfies the query an empty list is returned.
     */
    public Set queryMBeans(ObjectName name, QueryExp query)
    {
		return queryFromTable(name, query, false);
    }

    /**
	 * All the pattern matching stuff with ObjectName is done here
	 */
    private Set queryFromTable(ObjectName name, QueryExp query,
                                                            boolean queryName)
    {
        boolean wildCard = false;

        StringBuffer buff = new StringBuffer();

        HashSet querySet = new HashSet();

        try
		{
			if(name == null)
			    name = new ObjectName("*:*");
        }
		catch(Exception e1)
		{
            //log.warn("queryFromTable");
            return querySet;
        }

        if(name != null && name.toString().length() != 0)
        {
            String strPattern = name.toString();

            if((strPattern.indexOf("*") != -1) || (strPattern.indexOf("?") != -1))
			{
				wildCard = true;
            }

            int length = strPattern.length();
            for(int i=0;i<length;i++)
            {
                char ch = strPattern.charAt(i) ;

                /*if(ch == '*' || ch == '?')
                        buff.append("$");
                */

                buff.append(ch);
            }
        }

        String sname = buff.toString();

        StringTokenizer st = new StringTokenizer(sname, ":");

        if(st.countTokens() != 2)
			return querySet;

        String dom = st.nextToken();
        String des = st.nextToken();

        if(dom == null || des == null)
			return querySet;

        for(Enumeration e = serverTable.keys(); e.hasMoreElements();)
        {
            ObjectName obj = (ObjectName)e.nextElement();
            if(name == null || (name != null && name.toString().length() == 0) )
            {
                if(queryName)
                {
                    Object mbean = serverTable.get(obj);
                    try
					{
                        if(query == null)
                            querySet.add(obj);
                        else
                        {
                            query.setMBeanServer(this);
                            if(query.apply(obj))
                                    querySet.add(obj);
                        }
                    }
					catch(Exception e1)
					{
                        if(!(e1 instanceof NullPointerException))
                        	log.warn("Null Pointer Exception",e1);
                    }
                }
                else
                {
                    Object mbean = serverTable.get(obj);
                    String classname = null;

                    try
					{
                    	classname = ((DynamicMBean)mbean).getMBeanInfo().getClassName();
                    }
					catch(Exception ex)
					{
                    }

                    try
					{
                        if(query == null)
                            querySet.add(new ObjectInstance(obj, classname));
                        else
                        {
                            query.setMBeanServer(this);
                            if(query.apply(obj))
                                    querySet.add(new ObjectInstance(obj, classname));
                        }
                    }
					catch(Exception e2)
					{
                        if(!(e2 instanceof NullPointerException))
							log.warn("NullPointer Exception",e2);
                    }
                }
            }
            else //if(obj.toString().indexOf(name.toString()) != -1)
            {
                String sobj = obj.toString();

                StringTokenizer st1 = new StringTokenizer(sobj, ":");

                String lobj = st1.nextToken();
                String robj = st1.nextToken();

                boolean retVal1  = false;
                boolean retVal2  = false;

                retVal1 = JmxUtilities.checkPattern(lobj, dom, wildCard );

                if(retVal1)
					retVal2 = JmxUtilities.checkPattern(robj, des, wildCard );

                if(retVal2)
                {
                    if(queryName)
                    {
                        Object mbean = serverTable.get(obj);

                        try
						{
                            if(query == null)
							{
                            	querySet.add(obj);
                            }
                            else
                            {
                                query.setMBeanServer(this);
                                if(query.apply(obj))
									querySet.add(obj);
                            }
                        }
						catch(Exception e3)
						{
                            if(!(e3 instanceof NullPointerException))
								log.warn("Null Pointer Exception",e3);
                        }
                    }
                    else
                    {
                        Object mbean = serverTable.get(obj);
                        String classname = null;

                        try
						{
                        	classname = ((DynamicMBean)mbean).getMBeanInfo().getClassName();
                        }
						catch(Exception ex)
						{
                        }

                        try
						{
                            if(query == null)
                                querySet.add(new ObjectInstance(obj, classname));
                            else
                            {
                                query.setMBeanServer(this);
                                if(query.apply(obj))
									querySet.add(new ObjectInstance(obj, classname));
                            }
                        }
						catch(Exception e4)
						{
							if(!(e4 instanceof NullPointerException))
							    log.warn("Null Pointer Exception",e4);
                        }
                    }
                }
            }
        }

	    return querySet;
    }

    /**
     * Gets the names of MBeans controlled by the MBeanServer.
	 * This method enables any of the following to be obtained:
	 * The names of all MBeans, the names of a set of MBeans specified by
	 * pattern matching on the ObjectName and/or a Query expression, a specific
	 * MBean name(equivalent to testing whether an MBean is registered). When
	 * the object name is null or empty, all objects are to be selected (and
	 * filtered if a query is specified). It returns the set of ObjectNames for
	 * the MBeans selected.
	 *
     * @param name The object name pattern identifying the MBean names
     * 				to be retrieved. If null or empty, the name of all
	 * 				registered MBeans will be retrieved.
	 *
     * @param query The query expression to be applied for selecting MBeans.
     *
     * @return A set containing the ObjectNames for the MBeans selected.
     * 				If no MBean satisfies the query an empty list is returned.
     */
    public Set queryNames(ObjectName name, QueryExp query)
    {
		return queryFromTable(name, query, true);
    }

    /**
     * Registers a pre-existing object as an MBean with the MBeanServer.
     * If the object name given is null, the MBean may automatically
     * provide its own name by implementing the MBeanRegistration interface.
     * The call returns the MBean name.
     *
     * @param object The Java Bean to be registered as an MBean.
     *
     * @param name The object name of the MBean. May be null.
     *
     * @return The ObjectInstance for the MBean that has been registered.
     *
     * @exception javax.management.InstanceAlreadyExistsException The MBean
     * 				is already under the control of the MBeanServer.
     *
     * @exception javax.management.MBeanRegistrationException The preRegister
     * 				(MBeanRegistration interface) method of the MBean has
	 * 				thrown an exception. The MBean will not be registered.
	 *
     * @exception javax.management.NotCompliantMBeanException This object is
     * 				not an JMX compliant MBean
     */
    public ObjectInstance registerMBean(Object object, ObjectName name)
                    			throws  InstanceAlreadyExistsException,
               							MBeanRegistrationException,
               							NotCompliantMBeanException
    {
		try
        {
			//Hyther:TCK - when user gives DefaultDomain will be
			//overidden by server.defaultdomain at any cost
			ObjectName temp = name;

			try
			{
				if(name.getDomain().equals("DefaultDomain") || name.getDomain().equals(""))
				{
					name = new ObjectName(defaultDomain+":"+name.getKeyPropertyListString());
				}
			}
			catch(Exception ee)
			{
				name = temp;
			}

            if(object == null)
				throw new IllegalArgumentException();

			if(object instanceof DynamicMBean )
			{
				try
				{
					MBeanInfo mbi = ((DynamicMBean)(object)).getMBeanInfo();
					log.trace("registerMBean:object instanceof RMM");
					if(mbi==null)
					{
						log.debug("MBeanInfo is Null");
						throw new NotCompliantMBeanException("Exception occured while invoking the getMBeanInfo() of DynamicMBean");
					}

					if(!isValidMBeanInfo(mbi))
					{
						throw new NotCompliantMBeanException("MBeanInfo is not proper ");
					}
				}
				catch(Exception ee)
				{
					throw new NotCompliantMBeanException("Exception occured while invoking the getMBeanInfo() of DynamicMBean");
				}
			}

			//calling the preRegister before registering the MBean
			boolean isStandard = false;
			Class className = object.getClass();
			String fileName = className.getName();
			Class[] interfaces = className.getInterfaces();
			for(int i=0; i<interfaces.length; i++)
			{
				if(interfaces[i].getName().equals(fileName+"MBean"))
				{
					isStandard = true;
					break;
				}
			}

			//if(interfaces.length == 0){
			if(!isStandard)
			{
				Class superClass = className;
				//for(;superClass != null;superClass = superClass.getSuperclass()){
				while(superClass != null)
				{
					superClass = superClass.getSuperclass();
					if(superClass == null)
					{
						break;
					}

					String superClassName = superClass.getName();
					interfaces = superClass.getInterfaces();

					for(int i=0; i<interfaces.length; i++)
					{
						if(interfaces[i].getName().equals(superClassName+"MBean"))
						{
							isStandard = true;
							break;
						}
					}
				}
			}

			if(object instanceof MBeanRegistration)
			{
				log.trace("registerMBean:object instanceOf MBeanRegistration");
				ObjectName objName = null;

				try
				{
					objName = ((MBeanRegistration)object).preRegister(this,name);
				}
				catch(RuntimeException re)
				{
					throw new RuntimeMBeanException(re);
				}
				catch(Exception e)
				{
					log.warn("MBeanRegistration Failed",e);
					if(e instanceof MBeanRegistrationException)
					{
						throw (MBeanRegistrationException)e;
					}

					throw new MBeanRegistrationException(e,e.getMessage());
				}

				name = objName;
			}

			if(name == null)
			{
				try
				{
					if(object instanceof MBeanRegistration)
						((MBeanRegistration)object).postRegister(new Boolean(false));
				}
				catch(RuntimeException re)
				{
					throw new RuntimeMBeanException(re);
				}
				catch(Exception e)
				{
					throw new MBeanRegistrationException(e,e.getMessage());
				}

				throw new RuntimeOperationsException(new IllegalArgumentException("Object name is null"));
			}

			if(serverTable.get(name) != null)
			{
				try
				{
					if(object instanceof MBeanRegistration)
						((MBeanRegistration)object).postRegister(new Boolean(false));

					throw new InstanceAlreadyExistsException();
				}
				catch(Exception e)
				{
					/** This required to handle even if postREgiter() throws any Exception */
					if(e instanceof RuntimeException)
					{
						throw new RuntimeMBeanException((RuntimeException)e);
					}
					else if(e instanceof InstanceAlreadyExistsException)
					{
						throw (InstanceAlreadyExistsException)e;
					}
					else
					{
						throw new MBeanRegistrationException(e);
					}
				}
			}

            /** No Other MBeans except MBeanServerDelegate can be registered with the domain JMImplementation */
            if(!(name.toString().equals("JMImplementation:type=MBeanServerDelegate")))
			{
                if(name.getDomain().equals("JMImplementation"))
				{
                	throw new JMRuntimeException();
                }
            }

            String oname = object.getClass().getName();
            //ClassLoader l = object.getClass().getClassLoader();
			ClassLoader l = getClassLoader(object);
            testForCompliance(oname,l);
            //if(! (object instanceof DynamicMBean) )
            if(isStandard)
            {
                log.trace("Object Not an instance of DynamicMBean");
                try
				{
                	object = new  DefaultDynamicMBean(object);
                }
				catch(Exception e)
                {
                    try
					{
                    	if(object instanceof MBeanRegistration)
                        	((MBeanRegistration)object).postRegister(new Boolean(false));
                    }
					catch(Exception poe)
                    {
                    	throw new MBeanRegistrationException(poe,"Exception occured in postRegister");
                    }

                    throw new NotCompliantMBeanException();
                }
            }

            if(name.isPattern())
            {
                try
				{
                	if(object instanceof MBeanRegistration)
                        ((MBeanRegistration)object).postRegister(new Boolean(false));
                }
				catch(RuntimeException re)
                {
                	throw new RuntimeMBeanException(re);
                }
				catch(Exception poe)
                {
                	throw new MBeanRegistrationException(poe,"Exception occured in postRegister");
                }

                throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName pattern fails"));
            }

            serverTable.put(name, object);
            loaderTable.put(oname, l);

			try
			{
                if(object instanceof MBeanRegistration)
                	((MBeanRegistration)object).postRegister(new Boolean(true));
            }
			catch(RuntimeException re)
            {
				throw new RuntimeMBeanException(re);
            }
			catch(Exception poe)
            {
				throw new MBeanRegistrationException(poe,"Exception occured in postRegister");
            }
			catch(Throwable th)
            {
                if(th instanceof Error)
				{
					throw new RuntimeErrorException((Error)th);
                }
                Exception e = (Exception)th;
                throw new MBeanRegistrationException(e,"Exception occured in postRegister");
            }

			if(object instanceof ClassLoader)
				DefaultLoaderRepositoryExt.addLoader((ClassLoader)object);

            if(object instanceof DefaultDynamicMBean &&
                    ((DefaultDynamicMBean)object).getStandardMBeanObject() instanceof ClassLoader)
			{
            	DefaultLoaderRepositoryExt.addLoader((ClassLoader)((DefaultDynamicMBean)object).getStandardMBeanObject());
			}

			//send Registration Notification
			Notification notif = new MBeanServerNotification(
			    			MBeanServerNotification.REGISTRATION_NOTIFICATION,
							delegate.getClass().getName(),
							sequenceNumber++,
							name);

            notif.setTimeStamp(System.currentTimeMillis());
			delegate.sendNotification(notif);

            if(object instanceof DefaultDynamicMBean)
            {
				return new ObjectInstance(name, ((DefaultDynamicMBean)object).getStandardMBeanObject().getClass().getName());
            }
            else
            {
                return new ObjectInstance(name, object.getClass().getName());
            }
		}
		catch(IllegalArgumentException ie)
		{
        	throw new RuntimeOperationsException(ie);
        }
    }

    /**
     * De-registers an MBean from the MBeanServer. The MBean is identified by
	 * its object name. Once the method has been invoked, the MBean may no
	 * longer be accessed by its object name.
	 *
     * @param name The object name of the MBean to be de-registered.
     *
     * @exception javax.management.InstanceNotFoundException The
     * 				specified MBean is not registered in the MBeanServer.
     *
     * @exception javax.management.MBeanRegistrationException The
     * 				preDeregister (MBeanRegistration interface) method of the
     * 				MBean has thrown an exception.
     **/
    public void unregisterMBean(ObjectName name)
            throws InstanceNotFoundException, MBeanRegistrationException
    {
        log.trace("Unregistering MBean with object name " + name);

        if(name == null)
			throw new RuntimeOperationsException(
				new IllegalArgumentException("ObjectName cannot be null!!!"));

        Object object = null;

        if((object = serverTable.get(name)) == null)
			throw new InstanceNotFoundException();

        if(object instanceof MBeanRegistration)
        {
            try
			{
            	((MBeanRegistration)object).preDeregister();
            }
            catch(RuntimeException  re)
			{
            	throw new RuntimeMBeanException(re);
            }
            catch(Exception e)
			{
                log.warn("Exception while unregistering the MBean ");
                if(e instanceof MBeanRegistrationException)
				{
                	throw (MBeanRegistrationException)e;
                }

                throw new MBeanRegistrationException(e);
            }
        }

        if(name.toString().equals("JMImplementation:type=MBeanServerDelegate"))
			throw new RuntimeOperationsException(new IllegalArgumentException(
							"MBeanServerDelegate Cannot be unregistered"));

        Object o =serverTable.get(name);
        serverTable.remove(name);
        loaderTable.remove(name);

        if(o instanceof DefaultDynamicMBean)
        {
            if(((DefaultDynamicMBean)o).getStandardMBeanObject() instanceof ClassLoader)
            {
				DefaultLoaderRepositoryExt.removeLoader((ClassLoader)(((DefaultDynamicMBean)o).getStandardMBeanObject()));
            }
        }

        if(o instanceof ClassLoader)
        {
			DefaultLoaderRepositoryExt.removeLoader((ClassLoader)o);
        }

        if(object instanceof MBeanRegistration)
		{
			try
			{
				((MBeanRegistration)object).postDeregister();
			}
			catch(Exception e)
			{
				if( e instanceof RuntimeException)
				{
					RuntimeException rt = (RuntimeException)e;
					throw new RuntimeMBeanException(rt);
				}
			}
			catch(Error e)
			{
				 throw new RuntimeErrorException(e);
			}
        }

        //send registration Notification...
        Notification notif = new MBeanServerNotification(
							MBeanServerNotification.UNREGISTRATION_NOTIFICATION,
							delegate.getClass().getName(),
							sequenceNumber++,
							name);

        delegate.sendNotification(notif);
    }

    /**
     * Gets the value of a specific attribute of a named MBean.
     * The MBean is identified by its object name.
     *
     * @param name The object name of the MBean from which the
     * 				attribute is to be retrieved.
     *
     * @param attribute A String specifying the name of the
     * 				attribute to be retrieved.
     *
     * @return The value of the retrieved attribute.
     *
     * @exception javax.management.AttributeNotFoundException The
     * 				specified attribute is not accessible in the MBean.
     *
     * @exception javax.management.MBeanException Wraps an exception
     * 				thrown by the MBean's getter.
     *
     * @exception javax.management.InstanceNotFoundException The
     * 				specified MBean is not registered in the MBeanServer.
     *
     * @exception javax.management.ReflectionException Wraps an
     * 				java.lang.Exception thrown while trying to invoke the setter.
     **/
    public Object getAttribute(ObjectName name, String attribute)
                    	throws MBeanException,
							   AttributeNotFoundException,
							   InstanceNotFoundException,
							   ReflectionException
    {
        log.trace("getAttribute - getting value for attribute " + attribute
								+ " of MBean with ObjectName " + name);

        if(name == null || attribute == null)
			throw new RuntimeOperationsException(
					new IllegalArgumentException("Null Values not possible"));

        Object object = null;
        if((object = serverTable.get(name)) == null)
                throw new InstanceNotFoundException();

        if(object instanceof DynamicMBean)
        {
            Object toRet = null;

            synchronized(object)
            {
                try
				{
                	toRet = ((DynamicMBean)object).getAttribute(attribute);
                }
				catch(AttributeNotFoundException ae)
				{
                	throw ae;
                }
				catch(ReflectionException re)
				{
                    Exception e = re.getTargetException();
                    if(e instanceof InvocationTargetException)
					{
                        InvocationTargetException it = (InvocationTargetException)e;
                        Exception ee = (Exception)it.getTargetException();
                        if(ee instanceof RuntimeException)
						{
                        	RuntimeException rt = (RuntimeException)ee;
                            throw new RuntimeMBeanException(rt);
                        }
                        else
						{
                        	throw new MBeanException(ee);
                        }
                    }

                    throw new MBeanException(e);
                }
				catch(Exception e)
				{
                        if(e instanceof RuntimeErrorException)
						{
                            RuntimeErrorException rt = (RuntimeErrorException)e;
                            throw rt;
                        }
                        throw new MBeanException(e);
                }
            }

            return toRet;
        }

        return null;
    }

    /**
     * Enables the values of several attributes of a named MBean.
     * The MBean is identified by its object name.
     *
     * @param name The object name of the MBean from which the
     * 				attributes are to be retrieved.
     *
     * @param attributes A list of the attributes to be retrieved.
     *
     * @return The list of the retrieved attributes.
     *
     * @exception javax.management.InstanceNotFoundException The
     * 				specified MBean is not registered in the MBeanServer.
     **/
	public AttributeList getAttributes(ObjectName name, String[] attributes)
                		throws InstanceNotFoundException, ReflectionException
	{
        log.trace("getAttributes entered");

		int length = 0;

        if(name == null || attributes == null)
			throw new RuntimeOperationsException(
				new IllegalArgumentException("Null values not possible"));

        Object object = null;

        if((object = serverTable.get(name)) == null)
			throw new InstanceNotFoundException();

		length = attributes.length;

		for(int i=0 ; i<length ; i++)
		{
			if (attributes[i] == null)
			throw new RuntimeOperationsException(new IllegalArgumentException("Null values not possible"));
		}


        if(object instanceof DynamicMBean)
        {
            synchronized(object)
            {
				try
				{
					  return ((DynamicMBean)object).getAttributes(attributes);
				}
				catch(Exception ex)
				{
					new ReflectionException(ex);
				}
            }
        }

        return null;
    }

    /**
     * Invokes an action on an MBean.
     *
     * @param name The object name of the MBean on which the method is to be invoked.
     *
     * @param actionName The name of the action to be invoked.
     *
     * @param params An array containing the parameters to be set when the action is invoked
     *
     * @param signature An array containing the signature of the action.
	 * 				The class objects will be loaded using the same class loader
	 * 				as the one used for loading the MBean on which the action was invoked.
	 *
     * @return The object returned by the action, which represents the result
	 * 				of invoking the action on the specified MBean.
	 *
     * @exception javax.management.InstanceNotFoundException The
     * 				specified MBean is not registered in the MBeanServer.
     *
     * @exception javax.management.MBeanException Wraps an exception
     * 				thrown by the MBean's invoked method.
     *
     * @exception javax.management.ReflectionException Wraps an
     * 				java.lang.Exception thrown while trying to invoke the method.
     **/
    public Object invoke(ObjectName name, String actionName,
						 Object[] params, String[] signature)
            	throws  InstanceNotFoundException,
                    	MBeanException,
                        ReflectionException
    {
        Object object = null;

        if((object = serverTable.get(name)) == null)
		{
        	throw new InstanceNotFoundException();
        }

        if(object instanceof DynamicMBean)
        {
            synchronized(object)
            {
                Object retObj = null;
                if( object instanceof DefaultDynamicMBean)
				{
                    try
					{
                    	retObj = ((DynamicMBean)object).invoke(actionName, params, signature);
                    }
                    catch(RuntimeOperationsException roe)
					{
                    	throw roe;
                    }
                    catch(RuntimeException re)
					{
                        RuntimeException rt = (RuntimeException)re;
                        if(rt instanceof RuntimeOperationsException)
						{
                            throw (RuntimeOperationsException)rt;
                        }

						if(rt instanceof RuntimeErrorException)
						{
                            throw (RuntimeErrorException)rt;
                        }

						if(rt instanceof RuntimeMBeanException)
						{
                        	throw rt;
                        }

                        throw new RuntimeMBeanException(rt);
                	}
                    catch(ReflectionException re)
					{
                    	throw re;
                    }
					catch(MBeanException me)
                    {
                        Exception e = me.getTargetException();

						if(e instanceof RuntimeException)
						{
                        	RuntimeException rt = (RuntimeException)e;
                            throw new RuntimeMBeanException(rt);
                        }

                        throw me;
                    }

                }
                else
				{
                    try
					{
                    	retObj= ((DynamicMBean)object).invoke(actionName, params, signature);
                    }
                    catch(ReflectionException re)
					{
                        Exception e = re.getTargetException();
                        if(e instanceof MBeanException)
						{
                        	MBeanException mb = (MBeanException)e;
                            throw mb;
                        }
                        else
						{
                        	throw re;
                        }
                    }
                }

                return retObj;
            }
        }

        return null;
    }

    /**
     * Sets the value of a specific attribute of a named MBean.
     * The MBean is identified by its object name.
     *
     * @param name The name of the MBean within which the attribute is to be set.
     *
     * @param attribute The identification of the attribute to
     * 				be set and the value it is to be set to.
     *
     * @return The value of the attribute that has been set.
     *
     * @exception javax.management.InstanceNotFoundException The
     * 				specified MBean is not registered in the MBeanServer.
     *
     * @exception javax.management.AttributeNotFoundException The
     * 				specified attribute is not accessible in the MBean.
     *
     * @exception javax.management.InvalidAttributeValueException The
     * 				specified value for the attribute is not valid.
     *
     * @exception javax.management.MBeanException Wraps an exception
     * 				thrown by the MBean's setter.
     *
     * @exception javax.management.ReflectionException Wraps an
     * 				java.lang.Exception thrown while trying to invoke the setter.
     **/
    public void setAttribute(ObjectName name, Attribute attribute)
                      throws InstanceNotFoundException,
                       		 AttributeNotFoundException,
                       		 InvalidAttributeValueException,
                       		 MBeanException,
                       		 ReflectionException
    {
        log.trace("setAttribute - setting attribute");

        if(name == null || attribute == null)
			throw new RuntimeOperationsException(
				new IllegalArgumentException("Null Values not possible"));

        Object object = null;
        if((object = serverTable.get(name)) == null)
			throw new InstanceNotFoundException("No Mbean " + name + "present in the JMXServer");

        synchronized(object)
        {
			((DynamicMBean)object).setAttribute(attribute);
        }
    }

    /**
     * Sets the values of several attributes of a named MBean.
     * The MBean is identified by its object name.
     *
     * @param name The object name of the MBean within which the attributes are to be set.
     *
     * @param attributes A list of attributes: The identification of the
	 * 				attributes to be set and the values they are to be set to.
	 *
     * @return The list of attributes that were set, with their new values.
     *
     * @exception javax.management.InstanceNotFoundException The specified
     * 				MBean is not registered in the MBeanServer.
     */
    public AttributeList setAttributes(ObjectName name, AttributeList attributes)
            throws InstanceNotFoundException, ReflectionException
    {
        log.trace("setAttributes called for MBean with ObjectName " + name);

        if(name == null || attributes == null)
                throw new RuntimeOperationsException(new IllegalArgumentException("Either ObjectName / attributes is null"));

        Object object = null;

        if((object = serverTable.get(name)) == null)
			throw new InstanceNotFoundException();

        if(object instanceof DynamicMBean)
        {
            synchronized(object)
            {
				return ((DynamicMBean)object).setAttributes(attributes);
            }
        }

        return null;
    }

    /**
     * This method discovers the attributes and operations that an MBean exposes
	 * for management. When flatten is false, inherited attributes are not returned.
	 *
     * @param name The name of the MBean to analyze
     *
     * @return An instance of MBeanInfo allowing to retrieve all attributes
     * 				and operations of this MBean.
     *
     * @exception java.beans.IntrospectionException An exception
     * 				occurs during introspection.
     *
     * @exception javax.management.InstanceNotFoundException The specified
     * 				MBean is not found.
     **/
    public MBeanInfo getMBeanInfo(ObjectName name)
            			   throws InstanceNotFoundException,
            			   		  IntrospectionException,
            			   		  ReflectionException
    {
        log.trace("getting MBeanInfo for MBean with ObjectName " + name);

        Object object = null;

        if((object = serverTable.get(name)) == null)
			throw new InstanceNotFoundException();

        if(object instanceof DynamicMBean)
        {
            synchronized(object)
            {
                MBeanInfo info = null;
                try
				{
                    info = ((DynamicMBean)object).getMBeanInfo();
                    if(info == null)
					{
                    	throw new JMRuntimeException();
                    }
                }
                catch(Exception e)
				{
                    if(e instanceof JMRuntimeException)
					{
                    	throw (JMRuntimeException)e;
                    }
                    if(e instanceof RuntimeException)
					{
                        RuntimeException rt = (RuntimeException)e;
                        throw new RuntimeMBeanException(rt);
                    }
                }

                return info;
            }
        }

        return null;
    }

    /**
     * Gets the ObjectInstance for a given MBean registered with the MBean server.
     *
     * @param name The object name of the MBean.
     *
     * @return The ObjectInstance associated to the MBean specified by name.
     *
     * @exception javax.management.InstanceNotFoundException The MBean specified
     * 				is not registered in the MBean server.
     */
    public ObjectInstance getObjectInstance(ObjectName name)
            						throws InstanceNotFoundException
    {
		if (name == null)
     		throw new InstanceNotFoundException("The specified ObjectName is null");

        if( ! isRegistered(name) )
        	throw new InstanceNotFoundException(name.toString());

        Object mbean = serverTable.get(name);
        String classname = null;

        try
		{
			if(mbean instanceof javax.management.modelmbean.ModelMBean)
			    classname = "javax.management.ModelMBean";
			else
			    classname = ((DynamicMBean)mbean).getMBeanInfo().getClassName();
        }
		catch(Exception ex)
		{
        }

        try
		{
        	return new ObjectInstance(name, classname);
        }
		catch(Exception ee)
		{
            log.error("ObjectInstance Creation failed",ee);
            return null;
        }
    }

    /**
     * Enables a couple (listener,handback) for a registered MBean to be added.
     * @param name The name of the MBean on which the listener should be added.
     * @param listener The listener object which will handles notifications
     * emitted by the registered MBean.
     * @param filter The filter object. If not specified, no filtering will be
     * performed before handling notifications.
     * @param handback The context to be sent to the listener when a notification
     * is emitted.
     * @exception javax.management.InstanceNotFoundException The MBean name
     * doesn't correspond to a registered MBean.
     * @exception java.lang.IllegalArgumentException The object with name "name"
     * doesn't implements NotificationBroadcaster
     * @exception javax.management.InstanceAlreadyExistsException The couple
     * (listener,handback) is already registered in the MBean.
     **/
    public void addNotificationListener(ObjectName name,
                                  NotificationListener listener,
                                  NotificationFilter filter,
                                  Object handback)
                           throws InstanceNotFoundException
    {
        Object object = null;

        if(name == null)
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
            								"ObjectName cannot be null!!!"));
        }

        object = serverTable.get(name);

        if(object == null)
        {
            throw new InstanceNotFoundException("No object with the name " +
            							name + " present in the JMXServer");
        }

        if( !(object instanceof NotificationBroadcaster))
        {
            throw new RuntimeOperationsException(new IllegalArgumentException(
				"MBean " + name + " does not implement NotificationBroadCaster"));
        }

        if(listener == null)
        {
            throw new RuntimeOperationsException(new IllegalArgumentException(
										"Null Listener is being passed !!!"));
        }

        synchronized(object)
        {
			((NotificationBroadcaster) object)
						.addNotificationListener(listener, filter, handback);
        }
    }

    /**
     * Enables a couple (listener,handback) for a registered MBean to be added.
     *
     * @param name The name of the MBean on which the listener should be added.
     *
     * @param listener The listener name which will handles notifications
     * 				emitted by the registered MBean.
     *
     * @param filter The filter object. If not specified, no filtering will be
     * 				performed before handling notifications.
     *
     * @param handback The context to be sent to the listener when a
	 * 				notification is emitted.
	 *
     * @exception javax.management.InstanceNotFoundException The MBean name
     * 				doesn't correspond to a registered MBean.
     *
     * @exception java.lang.IllegalArgumentException The object with name "name"
     * 				doesn't implements NotificationBroadcaster
     *
     * @exception javax.management.InstanceAlreadyExistsException The couple
     * 				(listener,handback) is already registered in the MBean.
     **/
    public void addNotificationListener(ObjectName name,
                                  ObjectName listener,
                                  NotificationFilter filter,
                                  Object handback)
                           throws InstanceNotFoundException
    {
        Object object = null;

        if(name == null)
        {
        	throw new RuntimeOperationsException(new IllegalArgumentException(
            								"ObjectName cannot be null!!!"));
        }

        object = serverTable.get(name);

        if(object == null)
        {
			throw new InstanceNotFoundException("No object with the name " +
            								name + " present in the JMXServer");
        }

        if( !(object instanceof NotificationBroadcaster))
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
				"MBean " + name + " does not implement NotificationBroadCaster"));
        }

        Object lobject = null;
        lobject = serverTable.get(listener);

        if(lobject == null)
        {
			throw new InstanceNotFoundException("No listener with the name "
								+ listener + " present in the JMXServer!!!");
        }

        if(lobject instanceof DefaultDynamicMBean)
        {
            if(!(((DefaultDynamicMBean)(lobject)).getStandardMBeanObject() instanceof NotificationListener))
            {
				throw new RuntimeOperationsException(new IllegalArgumentException(
					"MBean " + name + " does not implement NotificationListener"));
            }
        }

        if( !(lobject instanceof NotificationListener))
        {
            throw new RuntimeOperationsException(new IllegalArgumentException(
				"MBean " + name + " does not implement NotificationListener"));
        }

        synchronized(object)
        {
			((NotificationBroadcaster)object).addNotificationListener( (NotificationListener)lobject,filter,handback);
        }
    }

    /**
     * Instantiates and registers a MBean with the MBeanServer.
	 * The MBean server will use the default loader repository to load the
	 * class of the MBean. An object name is associated to the MBean. If the
	 * object name given is null, the MBean can automatically provide its own
	 * name by implementing the MBeanRegistration interface. The call returns a
	 * reference to the new instance and its object name.
	 *
     * @param className The class name of the MBean to be instantiated.
     *
     * @param name The object name of the MBean. May be null.
     *
     * @return An ObjectInstance, containing the ObjectName and the Java class
     * 				name of the newly instantiated MBean.
     *
     * @exception javax.management.ReflectionException Wraps Wraps a
     * 				ClassNotFoundException or a java.lang.Exception that
	 * 				occured trying to invoke the MBean's constructor.
	 *
     * @exception javax.management.InstanceAlreadyExistsException The MBean is
     * 				already under the control of the MBeanServer.
     *
     * @exception javax.management.MBeanRegistrationException The preRegister
     * 				(MBeanRegistration interface) method of the MBean has
	 * 				thrown an exception. The MBean will not be registered.
	 *
     * @exception javax.management.MBeanException The constructor of the
	 * 				MBean has thrown an exception
	 *
     * @exception javax.management.NotCompliantMBeanException This class is
	 * 				not an JMX compliant MBean
     **/
    public ObjectInstance createMBean(String className, ObjectName name)
                         	   throws ReflectionException,
                         	   		  InstanceAlreadyExistsException,
                         	   		  MBeanRegistrationException,
                         	   		  MBeanException,
                         	   		  NotCompliantMBeanException
    {
        log.trace("createMBean called with className and ObjectName");

        Object obj = null;

        if(className == null)
			throw new RuntimeOperationsException(new IllegalArgumentException(
								"ClassName or ObjectName cannot be null"));

        Class class_name = null;

        try
        {
			class_name = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
		catch(ClassNotFoundException cne)
        {
            try
            {
				class_name = DefaultLoaderRepositoryExt.loadClass(className);
            }
			catch(ClassNotFoundException cnee)
            {
            	throw new ReflectionException(cnee, "The MBean class could not be loaded by the default loader repository");
            }
        }

        try
		{
        	obj = class_name.newInstance();
        }
		catch(InstantiationException ite)
        {
            if(Modifier.isAbstract(class_name.getModifiers()))
			{
            	throw new NotCompliantMBeanException();
            }
            try
			{
            	Constructor cons = class_name.getConstructor(new Class[0]);
            }
            catch(NoSuchMethodException e)
			{
            	throw new ReflectionException(e);
            }

            throw new MBeanException(ite,"Exception occured while invoking the constructor of the MBean!!!");
        }
		catch(IllegalAccessException iae)
        {
        	throw new MBeanException(iae,"Exception occured while invoking the constructor of the MBean!!!");
        }
		catch(Exception e)
        {
            if(e instanceof NotCompliantMBeanException)
			{
            	throw (NotCompliantMBeanException)e;
            }

            throw new MBeanException(e, "General Exception occured. It might" +
							" be runtime errors while invoking the constructor");
        }

        return registerMBean(obj, name);
    }

    /**
     * Instantiates and registers a MBean with the MBeanServer.
	 * The MBean server will use the default loader repository to load the
	 * class of the MBean. An object name is associated to the MBean. If the
	 * object name given is null, the MBean can automatically provide its own
	 * name by implementing the MBeanRegistration interface. The call returns
	 * a reference to the new instance and its object name.
	 *
     * @param className The class name of the MBean to be instantiated.
     *
     * @param name The object name of the MBean. May be null.
     *
     * @param params An array containing the parameters of the constructor
	 * 				to be invoked.
	 *
     * @param signature An array containing the signature of the constructor
	 * 				to be invoked.
	 *
     * @return An ObjectInstance, containing the ObjectName and the Java class
     * 				name of the newly instantiated MBean.
     *
     * @exception javax.management.ReflectionException Wraps Wraps a
     * 				ClassNotFoundException or a java.lang.Exception that
	 * 				occured trying to invoke the MBean's constructor.
	 *
     * @exception javax.management.InstanceAlreadyExistsException The MBean is
     * 				already under the control of the MBeanServer.
     *
     * @exception javax.management.MBeanRegistrationException The preRegister
     * 				(MBeanRegistration interface) method of the MBean has
	 * 				thrown an exception. The MBean will not be registered.
	 *
     * @exception javax.management.MBeanException The constructor of the MBean
     * 				has thrown an exception
     **/
    public ObjectInstance createMBean(String className,
                                	  ObjectName name,
                                	  Object[] params,
                                	  String[] signature)
	                         throws ReflectionException,
	                                InstanceAlreadyExistsException,
	                                MBeanRegistrationException,
	                                MBeanException,
	                                NotCompliantMBeanException
    {
        log.trace("createMBean called with className, ObjectName, " +
					"parameters as Object array and signature as String array");

        Object obj = null;

        if(className == null)
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
								"ClassName or ObjectName cannot be null"));
        }

        Class class_name = null;

        try
        {
            class_name = DefaultLoaderRepositoryExt.loadClass(className);

            if(params == null || signature == null)
            {
				return createMBean(className,name);
            }

            Class[] sig = new Class[signature.length];

            for(int i = 0;i<signature.length;i++)
            {
                if((sig[i] = getThePrimitiveClassObject(signature[i])) == null)
                {
                    try
                    {
						sig[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
                    }
					catch(ClassNotFoundException ee)
                    {
						throw new ReflectionException(ee, "The MBean class " +
							"could not be loaded by the default loader repository");
                    }

                    try
					{
                    	if(obj instanceof DefaultDynamicMBean &&
                        	((DefaultDynamicMBean)obj).getStandardMBeanObject() instanceof ClassLoader)
                        {
							sig[i] = ((ClassLoader)((DefaultDynamicMBean)obj).getStandardMBeanObject()).loadClass(signature[i]);
                        }
                    }
					catch(ClassNotFoundException ce)
                    {
                        try
                        {
							sig[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
                        }
						catch(ClassNotFoundException ee)
                        {
                        	throw new ReflectionException(ee, "The MBean class could not be loaded by the default loader repository");
                        }
                    }

                }
            }

            Constructor  constr = null;

            constr = class_name.getConstructor(sig);
            obj = constr.newInstance(params);
        }
		catch(ClassNotFoundException cne)
        {
            throw new ReflectionException(cne, "The MBean class could not be loaded by the default loader repository");
        }
        catch(NoSuchMethodException ne)
		{
            throw new ReflectionException(ne);
        }
        catch(IllegalArgumentException iae)
		{
        	throw new ReflectionException(iae);
        }
        catch(InstantiationException ie)
		{
            try
			{
                Class[] classParams = new Class[params.length];

				for(int i=0; i<params.length; i++)
				{
                	classParams[i] = params[i].getClass();
                }

                Constructor cons = class_name.getConstructor(classParams);
            }
            catch(NoSuchMethodException ne)
			{
            	throw new ReflectionException(ne);
            }

            throw new MBeanException(ie);
        }
        catch(Exception e)
        {
            if(e instanceof ReflectionException)
			{
            	throw (ReflectionException)e;
            }

            throw new MBeanException(e, "Exception occured while calling the constructor of the MBean!!!");
        }

        return registerMBean(obj, name);
    }

    /**
     * Instantiates and registers a MBean with the MBeanServer.
	 * The class loader to be used is identified by its object name. An object
	 * name is associated to the MBean. If the object name of the loader is null,
	 * the system ClassLoader will be used.If the MBean's object name given is
	 * null, the MBean can automatically provide its own name by implementing
	 * the MBeanRegistration interface. The call returns a reference to the new
     * instance and its object name.
     *
     * @param className The class name of the MBean to be instantiated.
     *
     * @param name The object name of the MBean. May be null.
     *
     * @param loaderName The object name of the class loader to be used.
     *
     * @return An ObjectInstance, containing the ObjectName and the Java class
     * 				name of the newly instantiated MBean.
     *
     * @exception javax.management.InstanceAlreadyExistsException The MBean is
     * 				already under the control of the MBeanServer.
     *
     * @exception javax.management.MBeanRegistrationException The preRegister
     * 				(MBeanRegistration interface) method of the MBean has
	 * 				thrown an exception. The MBean will not be registered.
	 *
     * @exception javax.management.MBeanException The constructor of the MBean
     * 				has thrown an exception
     *
     * @exception javax.management.NotCompliantMBeanException This class is not
     * 				an JMX compliant MBean
     *
     * @exception javax.management.InstanceNotFoundException The specified class
     * 				loader is not registered in the MBeanServer.
     **/
    public ObjectInstance createMBean(String className,
	                                ObjectName name,
	                                ObjectName loaderName)
	                         throws ReflectionException,
	                                InstanceAlreadyExistsException,
	                                MBeanRegistrationException,
	                                MBeanException,
	                                NotCompliantMBeanException,
	                                InstanceNotFoundException
    {
        log.trace("createMBean called with className, ObjectName and " +
												"loaderName as ObjectName");

        Object object = null;
        Class clazz = null;
        Object obj = null;

        if(className == null )
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
								"ClassName or ObjectName cannot be null"));
        }

        try
		{
            if(loaderName == null)
            {
                //clazz = this.getClass().getClassLoader().loadClass(className);
				clazz = getClassLoader(this).loadClass(className);
            }
            else if((object = serverTable.get(loaderName)) == null)
            {
                //clazz = this.getClass().getClassLoader().loadClass(className);
				//clazz = getClassLoader(this).loadClass(className);
				throw new InstanceNotFoundException(loaderName.toString());
            }
            else if(object instanceof DefaultDynamicMBean &&
				((DefaultDynamicMBean)object).getStandardMBeanObject() instanceof ClassLoader)
            {
                clazz = ((ClassLoader)((DefaultDynamicMBean)object).getStandardMBeanObject()).loadClass(className);
            }
            else if( !(object instanceof ClassLoader))
            {
                throw new MBeanException(new Exception("Invalid ClassLoader"));
            }
            else
            {
                clazz = ((ClassLoader)object).loadClass(className);
            }

            obj = clazz.newInstance();
        }
		catch(ClassNotFoundException cne)
        {
            throw new ReflectionException(cne, "The MBean class could not be loaded by the available class Loaders");
        }
		catch(InstantiationException ite)
        {
            try
			{
            	Constructor cons = clazz.getConstructor(new Class[0]);
            }
            catch(NoSuchMethodException ne)
			{
            	throw new ReflectionException(ne);
            }

            throw new MBeanException(ite);
        }
		catch(IllegalAccessException iae)
        {
        	throw new MBeanException(iae,"Exception occured while invoking the constructor of the MBean!!!");
        }
        catch(Exception e)
        {
			throw new MBeanException(e,"General Exception occured .It might be runtime errors while invoking the constructor");
        }

        return registerMBean(obj, name);
    }

    /**
     * Instantiates and registers a MBean with the MBeanServer. The class
     * loader to be used is identified by its object name. An object name
     * is associated to the MBean. If the object name of the loader is not
     * specified, the system ClassLoader will be used.If the MBean object
     * name given is null, the MBean can automatically provide its own name
     * by implementing the MBeanRegistration interface. The call returns a
     * reference to the new instance and its object name.
     *
     * @param className The class name of the MBean to be instantiated.
     *
     * @param name The object name of the MBean. May be null.
     *
     * @param params An array containing the parameters of the constructor
	 * 				to be invoked.
	 *
     * @param signature An array containing the signature of the constructor
	 * 				to be invoked.
	 *
     * @param loaderName The object name of the class loader to be used.
     *
     * @return An ObjectInstance, containing the ObjectName and the Java class
     * 				name of the newly instantiated MBean.
     *
     * @exception javax.management.ReflectionException Wraps Wraps a
     * 				ClassNotFoundException or a java.lang.Exception that
	 * 				occured trying to invoke the MBean's constructor.
	 *
     * @exception javax.management.InstanceAlreadyExistsException The MBean is
     * 				already under the control of the MBeanServer.
     *
     * @exception javax.management.MBeanRegistrationException The preRegister
     * 				(MBeanRegistration interface) method of the MBean has thrown
	 * 				an exception. The MBean will not be registered.
	 *
     * @exception javax.management.MBeanException The constructor of the MBean
	 * 				has thrown an exception
     **/
    public ObjectInstance createMBean(String className,
	                                ObjectName name,
	                                ObjectName loaderName,
	                                Object[] params,
	                                String[] signature)
	                         throws ReflectionException,
	                                InstanceAlreadyExistsException,
	                                MBeanRegistrationException,
	                                MBeanException,
	                                RuntimeMBeanException,
	                                NotCompliantMBeanException,
	                                InstanceNotFoundException
    {
        log.trace("createMBean called with className, ObjectName, loaderName as " +
			"ObjectName, parameters as Object array and signature as String array");

        Object object = null;
        Class clazz = null;
        Object obj = null;

        if(className == null )
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
									"ClassName or ObjectName cannot be null"));
        }

        try
		{
            if(loaderName == null)
            {
                //clazz = this.getClass().getClassLoader().loadClass(className);
				clazz = getClassLoader(this).loadClass(className);
            }
            else if((object = serverTable.get(loaderName)) == null)
            {
                //clazz = this.getClass().getClassLoader().loadClass(className);
				//clazz = getClassLoader(this).loadClass(className);
				throw new InstanceNotFoundException(loaderName.toString());
            }
            else if(object instanceof DefaultDynamicMBean &&
				((DefaultDynamicMBean)object).getStandardMBeanObject() instanceof ClassLoader)
            {
				clazz = ((ClassLoader)((DefaultDynamicMBean)object).getStandardMBeanObject()).loadClass(className);
            }
            else if( !(object instanceof ClassLoader))
            {
                throw new MBeanException(new Exception("Invalid ClassLoader "));
            }
            else
            {
                clazz = ((ClassLoader)object).loadClass(className);
            }
        }
		catch(ClassNotFoundException cne)
        {
        	throw new ReflectionException(cne, "The MBean class could not be " +
									"loaded by the available class Loaders");
        }

        if(params == null || signature == null)
        {
			return createMBean(className,name,loaderName);
        }

        try
        {
            Class[] sig = new Class[signature.length];
            for(int i = 0;i<signature.length;i++)
            {
                try
                {
					if((sig[i] = getThePrimitiveClassObject(signature[i])) == null)
					    sig[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
                }
				catch(ClassNotFoundException eee)
                {
                    try
                    {
						DefaultLoaderRepositoryExt.loadClass(signature[i]);
                    }
					catch(ClassNotFoundException ee)
                    {
                    	throw new ReflectionException(ee, "The MBean class could not be loaded by the default loader repository");
                    }
                }
            }

            Constructor constr = null;
            constr = clazz.getConstructor(sig);
            obj = constr.newInstance(params);
        }
		catch(InstantiationException ie)
		{
            try
			{
                Class[] classParams = new Class[params.length];
                for(int i=0; i<params.length; i++){
                        classParams[i] = params[i].getClass();
                }
                Constructor cons = clazz.getConstructor(classParams);
            }
            catch(NoSuchMethodException ne)
			{
				throw new ReflectionException(ne);
            }
        }
		catch(NoSuchMethodException nsme)
		{
            try
			{
                Class[] classParams = new Class[params.length];
                for(int i=0; i<params.length; i++)
				{
                	classParams[i] = params[i].getClass();
                }

                Constructor cons = clazz.getConstructor(classParams);
                obj = cons.newInstance(params);
            }
			catch(NoSuchMethodException ne)
			{
            	throw new ReflectionException(ne);
            }
			catch(Exception ene)
			{
            	throw new MBeanException(ene);
            }
        }
		catch(IllegalAccessException iae)
        {
        	throw new MBeanException(iae,"Exception occured while invoking the constructor of the MBean!!!");
        }
		catch(Exception e)
        {
            if(e instanceof ReflectionException)
			{
            	throw (ReflectionException)e;
            }

            if(e instanceof RuntimeException)
			{
            	throw new RuntimeMBeanException((RuntimeException)e);
            }

            if(e instanceof MBeanException)
			{
            	throw (MBeanException)e;
            }

            if(e instanceof InvocationTargetException)
			{
                Exception ee = (Exception)((InvocationTargetException)e).getTargetException();
                if(ee instanceof RuntimeException)
				{
                	throw new RuntimeMBeanException((RuntimeException)ee);
                }
                else
				{
                    throw new MBeanException(ee);
                }
            }

            throw new ReflectionException(e,"General Exception occured. " +
					"It might be runtime errors while invoking the constructor");
        }

        return registerMBean(obj, name);
    }

    /**
     * De-serializes a byte array in the context of the class loader of an MBean.
     *
     * @param name The name of the MBean whose class loader should be used
	 * 				for the de-serialization.
	 *
     * @param data The byte array to be de-sererialized.
     *
     * @return The de-serialized object stream.
     *
     * @exception javax.management.InstanceNotFoundException The MBean
	 * 				specified is not found.
	 *
     * @exception javax.management.OperationsException Any of the usual
     * 				Input/Output related exceptions.
     */
	public java.io.ObjectInputStream deserialize(ObjectName name, byte[] data)
                                    throws InstanceNotFoundException,
                                           OperationsException
    {
        Object obj = serverTable.get(name);
        if(obj == null)
        	throw new InstanceNotFoundException(name.toString());

        try
		{
        	return deserialize(obj.getClass().getName(), data);
        }
		catch(ReflectionException re)
		{
        	throw new OperationsException("Exception while de-serializing the byte array data");
        }
    }

    /**
     * De-serializes a byte array in the context of a given MBean class loader.
     * The class loader is the one that loaded the class with name "className".
     *
     * @param name The name of the class whose class loader should be used
	 * 				for the de-serialization.
	 *
     * @param data The byte array to be de-sererialized.
     *
     * @return The de-serialized object stream.
     *
     * @exception javax.management.OperationsException Any of the usual
     * 				Input/Output related exceptions.
     *
     * @exception javax.management.ReflectionException The specified class could
     * 				not be loaded by the default loader repository
     */
	public java.io.ObjectInputStream deserialize(String className, byte[] data)
								throws OperationsException,ReflectionException
    {
		return deserialize(className, null, data);
    }

    /**
     * De-serializes a byte array in the context of a given MBean class loader.
     * The class loader is the one that loaded the class with name "className".
     * The name of the class loader to be used for loading the specified class
     * is specified. If null, the MBean Server's class loader will be used.
     *
     * @param name The name of the class whose class loader should be used
	 * 				for the de-serialization.
	 *
     * @param data The byte array to be de-sererialized.
     *
     * @param loaderName The name of the class loader to be used for loading
	 * 				the specified class. If null, the MBean Server's
	 * 				class loader will be used.
	 *
     * @return The de-serialized object stream.
     *
     * @exception javax.management.InstanceNotFoundException The specified
	 * 				class loader MBean is not found.
	 *
     * @exception javax.management.OperationsException Any of the usual
     * 				Input/Output related exceptions.
     *
     * @exception javax.management.ReflectionException The specified class
	 * 				could not be loaded by the specified class loader.
     */
	public java.io.ObjectInputStream deserialize(String className,
											 ObjectName loaderName,
											 byte[] data)
                                      throws InstanceNotFoundException,
											 OperationsException,
											 ReflectionException
    {
		Class clazz = null;

		if(data == null || data.length == 0)
			throw new RuntimeOperationsException(
					new IllegalArgumentException(), "invalid data");

		if(className == null)
			throw new RuntimeOperationsException(
					new IllegalArgumentException(), "invalid className");

		if(loaderName != null)
		{
			Object obj = serverTable.get(loaderName);
			try
			{
				if(obj == null)
				{
					throw new RuntimeOperationsException(new IllegalArgumentException("Invalid classloader "+loaderName));
				}
				else if(obj instanceof DefaultDynamicMBean &&
					((DefaultDynamicMBean)obj).getStandardMBeanObject() instanceof ClassLoader)
				{
					clazz = ((ClassLoader)((DefaultDynamicMBean)obj).getStandardMBeanObject()).loadClass(className);
				}
				else if(!(obj instanceof ClassLoader))
				{
					throw new RuntimeOperationsException(new IllegalArgumentException("Invalid classloader "+loaderName));
				}
				else
				{
					clazz = ((ClassLoader)obj).loadClass(className);
				}
			}
			catch(ClassNotFoundException cnfe)
			{
				throw new ReflectionException(cnfe, "Loading failed by the loader " + loaderName);
			}
		}
		else
		{
			try
			{
				//clazz = this.getClass().getClassLoader().loadClass(className);
				clazz = getClassLoader(this).loadClass(className);
			}
			catch(ClassNotFoundException cnfe)
			{
				throw new ReflectionException(cnfe, className + " MBean could not be loaded");
			}
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(data);

		ObjectInputStreamSupport oiss = null;
		try
		{
			//oiss = new ObjectInputStreamSupport(bis, clazz.getClassLoader());
			oiss = new ObjectInputStreamSupport(bis, getClassLoader(clazz));
		}
		catch(Exception e)
		{
			throw new OperationsException("Exception while de-serializing the byte array data");
		}

		return oiss;
    }

    /**
     * Returns the number of MBeans controlled by the MBeanServer.
     **/
    public Integer getMBeanCount()
    {
		return new Integer(serverTable.size());
    }

    /**
     * This method de-serializes an object in the context of a given MBean
     * class loader.
     *
     * @param name The name of the MBean which defines the class loader
     *
     * @param entityBody The byte array to be de-sererialized.
     *
     * @return The de-serializaed object. Null can be returned if entityBody
     * 				is null or has no element(length to zero).
     *
     * @exception javax.management.InstanceNotFoundException The specified
     * 				MBean is not found.
     *
     * @exception java.lang.ClassNotFoundException The object which has to be
     * 				deserialized is not found the context the the MBean class loader
     *
     * @exception java.io.OptionalDataException Primitive data was found in
     * 				the stream instead of objects.
     *
     * @exception java.io.IOException Any of the usual Input/Output related
     * 				exceptions.
     **/
    public Object getObjectInClassLoader(ObjectName name, byte[] entityBody)
                                  throws InstanceNotFoundException,
                                         ClassNotFoundException,
                                         java.io.OptionalDataException,
                                         java.io.IOException
                                                                                      //throws Exception
    {
        Object object = null;
        Class clazz = null;

        if(entityBody == null)
			throw new IOException("entityBody is null");

        if(entityBody.length == 0)
            throw new IOException("entityBody length is zero");

        return null;
    }

    /**
     * Instantiates an object using the list of all class loaders registered
     * in the MBeanServer. It returns a reference to the newly created object.
     *
     * @param className The class name of the object to be instantiated.
     *
     * @return The newly instantiated object.
     *
     * @exception javax.management.ReflectionException Wraps a
     * 				ClassNotFoundException or the java.lang.Exception that
	 * 				occured trying to invoke the object's constructor.
	 *
     * @exception javax.management.MBeanException The constructor of the object
     * 				has thrown an exception
     **/
    public Object instantiate(String className)
                           throws ReflectionException, MBeanException
    {
        if(className == null)
			throw new RuntimeOperationsException(new IllegalArgumentException(
									"className cannot be null"));

        Class class_name = null;

        try
		{
            class_name = DefaultLoaderRepositoryExt.loadClass(className);
            return class_name.newInstance();
        }
		catch(ClassNotFoundException e)
		{
        	throw new ReflectionException(e);
        }
        catch(IllegalAccessException ie)
		{
            throw new ReflectionException(ie);
        }
        catch(InstantiationException ie)
		{
            try
			{
                Constructor cons = class_name.getConstructor(new Class[0]);
            }
            catch(NoSuchMethodException e)
			{
            	throw new ReflectionException(e);
            }

            throw new MBeanException(ie);
        }
        catch(Exception e1)
		{
            if(e1 instanceof ReflectionException)
			{
            	throw (ReflectionException)e1;
            }

            throw new MBeanException(e1);
        }
    }

    /**
     * Instantiates an object using the list of all class loaders registered
     * in the MBeanServer. The call returns a reference to the newly created
     * object.
     *
     * @param className The class name of the object to be instantiated.
     *
     * @param params An array containing the parameters of the constructor
     * 				to be invoked.
     *
     * @param signature An array containing the signature of the constructor
     * 				to be invoked.
     *
     * @return The newly instantiated object.
     *
     * @exception javax.management.ReflectionException Wraps a
     * 				ClassNotFoundException or the java.lang.Exception that
	 * 				occured trying to invoke the object's constructor.
	 *
     * @exception javax.management.MBeanException The constructor of the object
     * 				has thrown an exception
     **/
	public Object instantiate(String className, Object[] params, String[] signature)
									throws ReflectionException, MBeanException
    {
		if(className == null)
		    throw new RuntimeOperationsException(new IllegalArgumentException("className cannot be null"));

		try
		{
		    Class class_name = DefaultLoaderRepositoryExt.loadClass(className);

		    if(params == null || signature == null)
		    {
				return instantiate(className);
		    }

		    Class[] sig = new Class[signature.length];
		    for(int i = 0;i<signature.length;i++)
		    {
		        if((sig[i] = getThePrimitiveClassObject(signature[i])) == null)
		        	sig[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
		    }

		    Constructor constr = class_name.getConstructor(sig);

		    return constr.newInstance(params);
		}
		catch(ClassNotFoundException e)
		{
		    throw new ReflectionException(e);
		}
		catch(IllegalArgumentException ie)
		{
		    throw new ReflectionException(ie);
		}
		catch(NoSuchMethodException ne)
		{
		    throw new ReflectionException(ne);
		}
		catch(Exception e1)
		{
		    throw new MBeanException(e1);
		}
    }

    /**
     * Instantiates an object using the class Loader specified by its ObjectName.
	 * If the loader name is null, the system ClassLoader will be used. It
	 * returns a reference to the new created object.
	 *
     * @param className The class name of the MBean to be instantiated.
     *
     * @param loaderName The object name of the class loader to be used.
     *
     * @return The newly instantiated object.
     *
     * @exception javax.management.ReflectionException Wraps a
     * 				ClassNotFoundException or the java.lang.Exception
     * 				that occured trying to invoke the object's constructor.
     *
     * @exception javax.management.MBeanException The constructor of the
     * 				object has thrown an exception.
     *
     * @exception javax.management.InstanceNotFoundException The specified
     * 				class loader is not registered in the MBeanServer.
     **/
    public Object instantiate(String className, ObjectName loaderName)
                           throws ReflectionException,
                                  MBeanException,
                                  InstanceNotFoundException
    {
        if(className == null )
                throw new RuntimeOperationsException(new IllegalArgumentException("className cannot be null"));

        Object object = null;
        Object loaderObject = null;
        Class clazz = null;

        try
        {
			object = serverTable.get(loaderName);
			if( (object instanceof DefaultDynamicMBean))
			{
			    loaderObject = ((DefaultDynamicMBean)(object)).getStandardMBeanObject();

			}
			else if( ! (object instanceof ClassLoader))
			{
			    throw new MBeanException(new Exception("Invalid ClassLoader "));
			}

			if(loaderObject instanceof ClassLoader)
			{
			    clazz = ((ClassLoader)loaderObject).loadClass(className);
			}
			else
			{
			    throw new MBeanException(new Exception("Invalid ClassLoader "));
			}
        }
		catch(ClassNotFoundException cnfe)
        {
        	throw new ReflectionException(cnfe);
        }
		catch(Exception e)
        {
			try
			{
				clazz = getClassLoader(this).loadClass(className);
			}
			catch(ClassNotFoundException cnfe)
			{
			    throw new ReflectionException(cnfe);
			}
        }

        try
        {
            return clazz.newInstance();
        }
		catch(IllegalAccessException ie)
		{
        	throw new ReflectionException(ie);
        }
		catch(Exception e1)
        {
			throw new MBeanException(e1,"Exception occured while invoking the constructor of the MBean");
        }
    }

	/**
	 * Instantiates an object. The class loader to be used is identified by its
	 * object name. If the object name of the loader is null, the system ClassLoader
	 * will be used. The call returns a reference to the newly created object.
	 *
	 * @param className The class name of the object to be instantiated.
	 *
	 * @param params An array containing the parameters of the constructor
	 * 				to be invoked.
	 *
	 * @param signature An array containing the signature of the constructor
	 * 				to be invoked.
	 *
	 * @param loaderName The object name of the class loader to be used.
	 *
	 * @return The newly instantiated object.
	 *
	 * @exception javax.management.ReflectionException Wraps a
	 * 				ClassNotFoundException or the java.lang.Exception that
	 * 				occured trying to invoke the object's constructor.
	 *
	 * @exception javax.management.MBeanException The constructor of the object has
	 * 				thrown an exception
	 *
	 * @exception javax.management.InstanceNotFoundException The specified class
	 * 				loader is not registered in the MBeanServer.
 	 **/
	public Object instantiate(String className, ObjectName loaderName,
							  Object[] params, String[] signature)
					   throws ReflectionException,
					   		  MBeanException,
					   		  InstanceNotFoundException
	{
		if(className == null )
			throw new RuntimeOperationsException(new IllegalArgumentException("className cannot be null"));

		Object object = null;
		Class clazz = null;
		Object loaderObject = null;

		try
		{
			object = serverTable.get(loaderName);

			if( (object instanceof DefaultDynamicMBean))
			{
				loaderObject = ((DefaultDynamicMBean)(object)).getStandardMBeanObject();
			}
			else if( ! (object instanceof ClassLoader))
			{
				throw new MBeanException(new Exception("Invalid ClassLoader "));
			}

			if(loaderObject instanceof ClassLoader)
			{
				clazz = ((ClassLoader)loaderObject).loadClass(className);
			}
			else
			{
				throw new MBeanException(new Exception("Invalid ClassLoader "));
			}
		}
		catch(ClassNotFoundException cnfe)
		{
			throw new ReflectionException(cnfe);
		}
		catch(Exception e)
		{
			try
			{
				//clazz = this.getClass().getClassLoader().loadClass(className);
				clazz = getClassLoader(this).loadClass(className);
			}
			catch(ClassNotFoundException cnfe)
			{
				throw new ReflectionException(cnfe);
			}
		}

		if(params == null || signature == null)
		{
			return instantiate(className,loaderName);
		}

		try
		{
			Class[] sig = new Class[signature.length];

			for(int i = 0;i<signature.length;i++)
			{
				if((sig[i] = getThePrimitiveClassObject(signature[i])) == null)
				sig[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
			}

			Constructor constr = clazz.getConstructor(sig);
			return constr.newInstance(params);
		}
		catch(ClassNotFoundException cnfe)
		{
			throw new ReflectionException(cnfe);
		}
		catch(IllegalAccessException ie){
			throw new ReflectionException(ie);
		}
		catch(Exception e1)
		{
			throw new MBeanException(e1);
		}
	}

	/**
	 * This method checks if an object has been loaded in the context of a
	 * MBean class loader.
	 *
	 * @param name The name of the MBean which defines the class loader
	 *
	 * @param object The object to check.
	 *
	 * @return True if the object as been loaded in the context of the MBean
	 * 				class loader ; false if not. of this MBean.
	 *
	 * @exception javax.management.InstanceNotFoundException The specified
	 * 				MBean is not found.
	 **/
	public boolean isInSameClassLoader(ObjectName name, Object object)
								throws InstanceNotFoundException
	{
		return false;
	}

    /**
     * @exception java.lang.IllegalArgumentException The object with name "name"
     * doesn't implements NotificationBroadcaster
     **/
    public void removeNotificationListener(ObjectName name,
	                                     NotificationListener listener)
	                              throws InstanceNotFoundException,
	                                     ListenerNotFoundException
    {
        Object object = null;

        if(name == null)
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
            							"ObjectName cannot be null!!!"));
        }

        object = serverTable.get(name);

        if(object == null)
        {
            throw new InstanceNotFoundException("No MBean with the Object Name "+listener+ " registered with MBeanServer");
        }

        if( !(object instanceof NotificationBroadcaster))
        {
            throw new RuntimeOperationsException(new IllegalArgumentException("The MBean"+name+" does not implement NotificationBroadCaster"));
        }

        synchronized(object)
        {
			((NotificationBroadcaster)object).removeNotificationListener(listener);
        }
    }

    /**
     * Enables a listener for an MBean to be removed.
     *
     * @param name The name of the MBean on which the listener should be removed
     *
     * @param listener The listener name which will handles notifications
     * 				emitted by the registered MBean. This method will remove
	 * 				all information related to this listener.
	 *
     * @exception javax.management.InstanceNotFoundException The MBean name or
     * 				the listener name doesn't correspond to a registered MBean
     *
     * @exception javax.management.ListenerNotFoundException The couple
     * 				(listener,handback) is not registered in the MBean. The
	 * 				exception message contains either "listener", "handback" or
	 * 				the object name depending on which object cannot be found.
	 *
     * @exception java.lang.IllegalArgumentException The object with name "name"
     * 				doesn't implements NotificationBroadcaster or the object
	 * 				with name "listener" doesn't implements NotificationListener.
	 * 				The faulty ObjectName string representation can be obtained
	 * 				by means of the getMessage() method of the
	 * 				IllegalArgumentException object.
     **/
    public void removeNotificationListener(ObjectName name, ObjectName listener)
					throws InstanceNotFoundException, ListenerNotFoundException
    {
        Object object = null;

        if(name == null)
        {
			throw new RuntimeOperationsException(new IllegalArgumentException(
                        					"ObjectName cannot be null!!!"));
        }
        object = serverTable.get(name);

        if(object == null)
        {
            throw new InstanceNotFoundException("No MBean with the Object Name "+listener+ " registered with MBeanServer");
        }

        Object lobject = null;
        lobject = serverTable.get(listener);

        if(lobject == null)
        {
            throw new InstanceNotFoundException("No listener with the name "+listener+ " present in the JMXServer!!!");
        }

        if( !(lobject instanceof NotificationListener))
        {
            throw new RuntimeOperationsException(new IllegalArgumentException("The MBean"+name+" does not implement NotificationListener"));
        }

        synchronized(object)
        {
			((NotificationBroadcaster)object).removeNotificationListener( (NotificationListener)lobject);
        }
    }

    /**
     * Returns the table that has the loader references os the mbeans.
     * Used by the package access class.
     */
    public Hashtable getLoaderTable()
    {
		return loaderTable;
    }

	//---------------------------- Private methods --------------------------//

    private Class getThePrimitiveClassObject(String str)
	{
		if(str.indexOf("boolean") != -1)
		    return Boolean.TYPE;
		else if(str.indexOf("int") != -1)
		    return Integer.TYPE;
		else if(str.indexOf("double") != -1)
		    return Double.TYPE;
		else if(str.indexOf("float") != -1)
		    return Float.TYPE;
		else if(str.indexOf("short") != -1)
		    return Short.TYPE;
		else if(str.indexOf("long") != -1)
		    return Long.TYPE;
		else if(str.indexOf("byte") != -1)
		    return Byte.TYPE;
		else if(str.indexOf("char") != -1)
		    return Character.TYPE;
		else
		    return null;
    }

    private void testForCompliance(String className,ClassLoader l)
                    throws NotCompliantMBeanException
    {
		Class cl = null;

		try
		{
			cl = Class.forName(className,true,l);
			int i = cl.getModifiers();
			if(Modifier.isAbstract(i))
				throw new NotCompliantMBeanException("The MBean cannot be a abstract class");

			java.lang.reflect.Constructor constructors[] = cl.getConstructors();

			if(constructors.length == 0)
				throw new NotCompliantMBeanException("The MBean does not have a public constructor");
		}
		catch(ClassNotFoundException ee)
		{
			log.warn("ClassNotFoundException",ee);
		}

		Class superClass = null;
		superClass = cl;
		Class class1 = null;
		Class class2 = null;
		boolean flag = false;
		for(;superClass != null;superClass = superClass.getSuperclass())
		{
			Class[] interfaces = superClass.getInterfaces();
			boolean checkFlag = false;
			for(int j=0;j<interfaces.length;j++)
			{
				Class tempClass = checkForMBeanInterface(interfaces[j],superClass.getName());
				if(tempClass != null)
				{
					flag = true;
					class1 = tempClass;
					class2 = superClass;
				}

				if(checkForDynamicMBeanInterface(interfaces[j]))
				{
					flag = true;
					checkFlag = true;
				}

				if(class1 != null && superClass.equals(class2) && checkFlag)
					throw new NotCompliantMBeanException(className + " implements javax.management.DynamicMBean and MBean interface");
			}
			if(flag)
			break;
		}

		if(!flag)
			throw new NotCompliantMBeanException(className + " does not implement the " + className+"MBean" + " interface or the DynamicMBean interface");
	}

	private Class checkForMBeanInterface(Class class1, String s)
	{
		if(class1.getName().compareTo(s + "MBean") == 0)
			return class1;

		Class aclass[] = class1.getInterfaces();
		for(int i = 0; i < aclass.length; i++)
		{
			if(aclass[i].getName().compareTo(s + "MBean") == 0)
				return aclass[i];
		}

		return null;
	}


	private boolean checkForDynamicMBeanInterface(Class class1)
	{
		if(class1.getName().compareTo("javax.management.DynamicMBean") == 0)
			return true;
		Class aclass[] = class1.getInterfaces();
		for(int i = 0; i < aclass.length; i++)
			if(aclass[i].getName().compareTo("javax.management.DynamicMBean") == 0)
				return true;

		return false;
	}

	private boolean isValidMBeanInfo(MBeanInfo mbInfo)
	{
		if(mbInfo.getClassName() == null)
		{
			return false;
		}

		/*
		MBeanConstructorInfo[] consInfo = mbInfo.getConstructors();
		if(consInfo == null){
				return false;
		}
		else if(consInfo.length == 0){
				return false;
		}*/

		return true;
	}

    private ClassLoader getClassLoader(Object o)
	{
        return getClassLoader(o.getClass());
	}

	private ClassLoader getClassLoader(Class clazz)
	{
        ClassLoader cl = clazz.getClassLoader();
		if(cl == null)
		{
            cl = Thread.currentThread().getContextClassLoader();
		}
		return cl;
	}

    private void createLogger()
	{
        try
		{
        	log = LogFactory.getInstance("JMX");
        }
		catch(Exception e)
		{
        	System.out.println("FATAL:Log Creation Failed");
        }
    }
}