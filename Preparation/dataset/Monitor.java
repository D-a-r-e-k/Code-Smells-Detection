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

package javax.management.monitor;

import java.io.Serializable;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.NotificationBroadcasterSupport;

/**
 * This class is the base class for all types of monitor mbeans.Defines the
 * common part to all monitor MBeans. Using Monitor MBeans, the observed
 * attribute of another MBean (the observed MBean) is monitored at intervals
 * specified by the granularity period. A gauge value (derived gauge) is
 * derived from the values of the observed attribute.
 */
public abstract class Monitor extends NotificationBroadcasterSupport
						implements MonitorMBean, MBeanRegistration, Serializable
{
	/**
	 * Selected monitor errors that have already been notified
	 */
	protected int alreadyNotified = 0;

	/**
	 * Flag denoting that a notification has occurred after changing the
	 * observed attribute. This flag is used to check that the new observed
	 * attribute belongs to the observed object at the time of the first notification.
	 */
	protected static int OBSERVED_ATTRIBUTE_ERROR_NOTIFIED = 1;

	/**
	 * Flag denoting that a notification has occurred after changing the
	 * observed object or the observed attribute. This flag is used to check
	 * that the observed attribute type is correct (depending on the monitor in
	 * use) at the time of the first notification.
	 */
	protected static int OBSERVED_ATTRIBUTE_TYPE_ERROR_NOTIFIED = 1;

	/**
	 * Flag denoting that a notification has occurred after changing the
	 * observed object or the observed attribute. This flag is used to notify
	 * any exception (except the cases described above) when trying to get the
	 * value of the observed attribute at the time of the first notification.
	 */
	protected static int OBSERVED_OBJECT_ERROR_NOTIFIED = 1;

	/**
	 * This flag is used to reset the alreadyNotified monitor attribute.
	 */
	protected static int RESET_FLAGS_ALREADY_NOTIFIED = 1;

	/**
	 * Flag denoting that a notification has occurred after changing the
	 * observed object or the observed attribute. This flag is used to notify
	 * any exception (except the cases described above) when trying to get the
	 * value of the observed attribute at the time of the first notification.
	 */
	protected static int RUNTIME_ERROR_NOTIFIED = 1;

    /**
     * Reference on the MBean server.
     * This reference is null when the monitor MBean is not registered in an
	 * MBean server. This reference is initialized before the monitor MBean is
	 * registered in the MBean server.
	 *
     * @see #preRegister(MBeanServer server, ObjectName name)
     */
	protected MBeanServer server = null;

    /**
     * Monitor granularity period (in milliseconds).
     */
    long granularityPeriod =	1000;

    /**
     * Object to which the attribute to observe belongs to.
     * <BR>The default value is set to null.
     */
	ObjectName observedObject =	null;

    /**
     * Attribute to observe.
     * <BR>The default value is set to null.
     */
	String	attributeName	=	null;

    /**
     * Monitor state.
     * The default value is set to <CODE>false</CODE>.
     */
	boolean	isActive =	false;

	long derivedGaugeTimeStamp	=	0;

	protected String dgbTag = null;

	/**
	 * Default constructor
	 */
	public Monitor()
	{
	}

	/**
	 * This method gets the granularity period (in milliseconds).
	 *
	 * @return long value representing the value of the granularity period
	 * 				(in milliseconds).
	 */
	public long getGranularityPeriod()
	{
		return granularityPeriod;
	}

	/**
	 * This method sets the granularity period (in milliseconds). The default
	 * value is one second.
	 *
	 * @param period the granularity period value.
	 *
	 * @exception java.lang.IllegalArgumentException - The granularity period
	 * 				is less than or equal to zero.
	 */
	public void setGranularityPeriod(long period) throws IllegalArgumentException
	{
		if(period <= 0)
			throw new IllegalArgumentException("Invalid Granularity Period!!!!"
						+ " Granularity Period cannot be negative or Zero");
		else
			this.granularityPeriod = period;
	}

	/**
	 * This method gets the name of the attribute being observed.
	 *
	 * @return String The name of the attribute that is being observed.
	 */
	public String getObservedAttribute()
	{
		return attributeName;
	}

	/**
	 * This method sets the attribute being observed.
	 *
	 * @param attribute The attribute to be observed.
	 */
	public void setObservedAttribute(String attribute)
									throws IllegalArgumentException
	{
		if(attribute == null || attribute.equals(""))
		{
			throw new IllegalArgumentException("Attribute cannot be null");
		}

		this.attributeName = attribute;
	}

	/**
	 * This method gets the object name of the object being observed.
	 *
	 * @return The ObjectName of the object being observed.
	 */
	public ObjectName getObservedObject()
	{
		return observedObject;
	}

	/**
	 * This method sets the object name of the object being observed.
	 *
	 * @param object The ObjectName of the object to be observed.
	 *
	 * @throws IllegalArgumentException.
	 */
	public void setObservedObject(ObjectName object)
										throws IllegalArgumentException
	{
		if(object == null)
		{
			throw new IllegalArgumentException("ObjectName cannot be null");
		}

		observedObject = object;
	}

	/**
	 * This method tests whether the monitor MBean is active. A monitor MBean
	 * is marked active when the start method is called. It becomes inactive
	 * when the stop method is called.
	 *
	 * @return boolean value indicating whether the MBean is active or not.
	 */
	public boolean isActive()
	{
		return isActive;
	}

	/**
	 * This method allows the monitor MBean to perform any operations needed
	 * after having been de-registered by the MBean server.
	 * Not used in this context.
	 */
	public void postDeregister()
	{
	}

	/**
	 * This method allows the monitor MBean to perform any operations needed
	 * after having been registered in the MBean server or after the
	 * registration has failed.
	 *
	 * Not used in this context.
	 */
	public void postRegister(Boolean registrationDone)
	{
	}

	/**
	 * This method allows the monitor MBean to perform any operations it needs
	 * before being registered in the MBean server.
	 * Initializes the reference to the MBean server.
	 *
	 * @param server - The MBean server in which the monitor MBean will be registered.
	 *
	 * @param name - The object name of the monitor MBean.
	 *
	 * @return This method allows the monitor MBean to perform any operations
	 * 				it needs before being registered in the MBean server.
	 *
	 * @exception - java.lang.Exception.
	 */
	public ObjectName preRegister(MBeanServer server, ObjectName name)
														throws Exception
	{
		this.server = server;
		return name;
	}

	/**
	 * This method allows the monitor MBean to perform any operations it needs
	 * before being de-registered by the MBean server.
	 * Stops the monitor.
	 *
	 * @throws This operation throws java.lang.Exception
	 */
	public void preDeregister() throws Exception
	{
	}

	/**
	 * This method starts the monitor.
	 */
	public abstract void start();

	/**
	 * This method stops the monitor.
	 */
	public abstract void stop();

	/**
	 * This method returns a NotificationInfo object containing the name of the
	 * Java class of the notification and the notification types sent by the
	 * counter monitor.
	 *
	 * @return An Array of MBeanNotificationInfo objects.
	 */
	public MBeanNotificationInfo[] getNotificationInfo()
	{
		MBeanNotificationInfo[] notifInfo = new MBeanNotificationInfo[1];

		String[] types = {"jmx.monitor.error.mbean","jmx.monitor.error.attribute","jmx.monitor.error.type","jmx.monitor.error.runtime"};

		notifInfo[0] = new MBeanNotificationInfo(types,"MonitorErrorNotification","These types of notification are emitted on Monitor Error Cases");

		return notifInfo;
	}
}