void method0() { 
/**
	 * Notification type denoting that the observed attribute is not contained in the observed
	 * object. This notification is fired by all kinds of monitors.
	 * The value of this notification type is jmx.monitor.error.attribute.
	 */
public static final String OBSERVED_ATTRIBUTE_ERROR = "jmx.monitor.error.attribute";
/**
	 * Notification type denoting that the type of the observed attribute is not correct. This
	 * notification is fired by all kinds of monitors.
	 * The value of this notification type is jmx.monitor.error.type.
	 */
public static final String OBSERVED_ATTRIBUTE_TYPE_ERROR = "jmx.monitor.error.type";
/**
	 * Notification type denoting that the observed object is not registered in the MBean server.
	 * This notification is fired by all kinds of monitors.
	 * The value of this notification type is jmx.monitor.error.mbean.
	 */
public static final String OBSERVED_OBJECT_ERROR = "jmx.monitor.error.mbean";
/**
	 * Notification type denoting that a non-predefined error type has occurred when trying to get
	 * the value of the observed attribute. This notification is fired by all kinds of monitors.
	 * The value of this notification type is jmx.monitor.error.runtime.
	 */
public static final String RUNTIME_ERROR = "jmx.monitor.error.runtime";
/**
	 * Notification type denoting that the observed attribute has differed from the "string to
	 * compare" value. This notification is only fired by string monitors.
	 * The value of this notification type is jmx.monitor.string.differs.
	 */
public static final String STRING_TO_COMPARE_VALUE_DIFFERED = "jmx.monitor.string.differs";
/**
	 * Notification type denoting that the observed attribute has matched the "string to compare"
	 * value. This notification is only fired by string monitors.
	 * The value of this notification type is jmx.monitor.string.matches.
	 */
public static final String STRING_TO_COMPARE_VALUE_MATCHED = "jmx.monitor.string.matches";
/**
	 * Notification type denoting that the type of the thresholds, offset or modulus is not correct.
	 //* This notification is fired by counter and gauge monitors.
	 * The value of this notification type is jmx.monitor.error.threshold.
	 */
public static final String THRESHOLD_ERROR = "jmx.monitor.error.threshold";
/**
	 * Notification type denoting that the observed attribute has exceeded the threshold high value.
	 * This notification is only fired by gauge monitors.
	 * The value of this notification type is jmx.monitor.gauge.high.
	 */
public static final String THRESHOLD_HIGH_VALUE_EXCEEDED = "jmx.monitor.gauge.high";
/**
	 * Notification type denoting that the observed attribute has exceeded the threshold low value.
	 * This notification is only fired by gauge monitors.
	 * The value of this notification type is jmx.monitor.gauge.low.
	 */
public static final String THRESHOLD_LOW_VALUE_EXCEEDED = "jmx.monitor.gauge.low";
/**
	 * Notification type denoting that the observed attribute has reached the threshold value. This
	 * notification is only fired by counter monitors.
	 * The value of this notification type is jmx.monitor.counter.threshold.
	 */
public static final String THRESHOLD_VALUE_EXCEEDED = "jmx.monitor.counter.threshold";
//private variables to this class. 
private ObjectName observedObject = null;
private String observedAttribute = null;
private Object derivedGauge = null;
private Object trigger = null;
private String type = null;
}
