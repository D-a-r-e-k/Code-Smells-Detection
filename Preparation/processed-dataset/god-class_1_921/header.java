void method0() { 
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
long granularityPeriod = 1000;
/**
     * Object to which the attribute to observe belongs to.
     * <BR>The default value is set to null.
     */
ObjectName observedObject = null;
/**
     * Attribute to observe.
     * <BR>The default value is set to null.
     */
String attributeName = null;
/**
     * Monitor state.
     * The default value is set to <CODE>false</CODE>.
     */
boolean isActive = false;
long derivedGaugeTimeStamp = 0;
protected String dgbTag = null;
}
