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

import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;

import javax.management.*;


/**
 * Provides definitions of the notifications sent by monitor MBeans.
 * The notification source and a set of parameters concerning the monitor MBean's state
 * need to be specified when creating a new object of this class. The list of notifications
 * fired by the monitor
 * MBeans is the following:
 *
 * Common to all kind of monitors:
 *			 The observed object is not registered in the MBean server.
 *			 The observed attribute is not contained in the observed object.
 *		     The type of the observed attribute is not correct.
 *           Any exception (except the cases described above) occurs when trying to get the
 *           value of the observed attribute.
 * Common to the counter and the gauge monitors:
 *           The threshold high or threshold low are not of the same type as the gauge (gauge
 *           monitors).
 *           The threshold or the offset or the modulus are not of the same type as the counter
 *           (counter monitors).
 * Counter monitors only:
 *           The observed attribute has reached the threshold value.
 * Gauge monitors only:
 *           The observed attribute has exceeded the threshold high value.
 *           The observed attribute has exceeded the threshold low value.
 * String monitors only:
 *           The observed attribute has matched the "string to compare" value.
 *           The observed attribute has differed from the "string to compare" value.
 *
 */
public class MonitorNotification extends Notification{

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

	private		ObjectName	observedObject		=	 null;
	private		String		observedAttribute	=	 null;
	private		Object		derivedGauge		=	 null;
	private		Object		trigger				=	 null;
	private		String		type				=	 null;

	MonitorNotification(String type,Object source,ObjectName objectName,String attributeName,
							   Object derivedgauge,Object trigger,long sequenceNumber){

		super(type,source,sequenceNumber);
		this.type = type;
		this.observedObject = objectName;
		this.observedAttribute = attributeName;
		this.derivedGauge = derivedgauge;
		this.trigger = trigger;
	}

	/**
	 * Gets the observed object of this monitor notification.
	 * @return - The Observed Object.
	 */
	public ObjectName getObservedObject(){
		return observedObject;
	}

	/**
	 * Gets the observed attribute of this monitor notification.
	 * @return - The Observed Attribute.
	 */
	public java.lang.String getObservedAttribute(){
		return observedAttribute;
	}

	/**
	 * Gets the derived gauge of this monitor notification.
	 * @return - The derived gauge.
	*/
	public java.lang.Object getDerivedGauge(){
		return derivedGauge;
	}

	/**
	 * Gets the threshold/string (depending on the monitor type) that triggered off this
	 * monitor notification.
	 * @return - The trigger value.
	*/

	public java.lang.Object getTrigger(){
		return trigger;
	}

}
