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

package javax.management;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamField;
import java.io.IOException;
import java.util.EventObject;

/**
 * The Notification class represents a notification emitted by an MBean.
 * It contains a reference to the source MBean: if the notification has been
 * forwarded through the MBean server, this is the object name of the MBean.
 * If the listener has registered directly with the MBean, this is a
 * direct reference to the MBean.
 */
public class Notification extends EventObject //implements Serializable
{
	/* Serial version UID */
	private static final long serialVersionUID = 1716977971058914352L;

    /**
     * The notification message.
     */
	private String message = "Notification ...";

    /**
     * The notification type. A string expressed in a dot notation similar to
	 * Java properties. An example of notification type is network.alarm.router
     */
	private String type = null;

	/**
	 * The object on which the notification initially occurred.
	 */
	protected Object source = null;

    /**
     * The notification sequence number. A serial number which identify
	 * particular instance of notification in the context of notification source.
     */
	private long sequenceNumber = 0;

    /**
     * The notification timestamp. Indicating when the notification was generated
     */
	private long timeStamp = 0;

    /**
     * The notification user data.  Used for whatever other data the
	 * notification source wishes to communicate to its consumers.
     */
	private Object userData = null;

	/**
	 * Creates a Notification object. The notification timeStamp is set to
	 * the curent date.
	 *
	 * @param type The notification type.
	 *
	 * @param source The notification source.
	 *
	 * @param sequenceNumber The notification sequence number within the source object.
	 */
	public Notification(String type, Object source, long sequenceNumber)
	{
		super(source);
		this.type = type;
		this.source = source;
		this.sequenceNumber = sequenceNumber;
		this.timeStamp = System.currentTimeMillis();
	}

	/**
	 * Creates a Notification object. The notification timeStamp is set to
	 * the curent date.
	 *
	 * @param type The notification type.
	 *
	 * @param source The notification source.
	 *
	 * @param sequenceNumber The notification sequence number within the source object.
	 *
	 * @param message the detail message.
	 */
	public Notification(String type, Object source,
						long sequenceNumber, String message)
	{
		super(source);
		this.type = type;
		this.source = source;
		this.sequenceNumber = sequenceNumber;
		this.message = message;
		this.timeStamp = System.currentTimeMillis();
	}

	/**
	 * Creates a Notification object.
	 *
	 * @param type The notification type.
	 *
	 * @param source The notification source.
	 *
	 * @param sequenceNumber The notification sequence number within the source object.
	 *
	 * @param timeStamp The notification emission date.
	 **/
	public Notification(String type, Object source,
						long sequenceNumber, long timeStamp)
	{
		super(source);
		this.type = type;
		this.source = source;
		this.sequenceNumber = sequenceNumber;
		this.timeStamp = timeStamp;
	}

	/**
	 * Creates a Notification object.
	 *
	 * @param type The notification type.
	 *
	 * @param source The notification source.
	 *
	 * @param sequenceNumber The notification sequence number within the source object.
	 *
	 * @param timeStamp The notification emission date.
	 *
	 * @param message the detail message.
	 */
	public Notification(String type, Object source, long sequenceNumber,
						long timeStamp, String message)
	{
		super(source);
		this.type = type;
		this.source = source;
		this.sequenceNumber = sequenceNumber;
		this.timeStamp = timeStamp;
		this.message = message;
	}

	/**
	 * Get the notification message.
	 *
	 * @return The message string of this notification object.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Get the notification sequence number.
	 *
	 * @return The notification sequence number within the source object.
	 */
	public long getSequenceNumber()
	{
		return sequenceNumber;
	}

	/**
	 * Set the notification sequence number.
	 *
	 * @param sequenceNumber set the notification sequence number
	 * 				within the source object
	 */
	public void setSequenceNumber(long sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Get the notification timestamp.
	 *
	 * @return The notification timestamp.
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Set the notification timestamp.
	 *
	 * @param timeStamp set the notification emission date
	 */
	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	/**
	 * Get the notification type.
	 *
	 * @return The notification type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Get the user data.
	 *
	 * @return The user data object
	 */
	public Object getUserData()
	{
		return userData;
	}

	/**
	 * Set the source.
	 *
	 * @param source Set the source object.
	 *
     * @exception java.lang.IllegalArgumentException The source is not a ObjectName
	 */
	public void setSource(Object source) throws IllegalArgumentException
	{
        if(!(source instanceof ObjectName))
		{
            throw new java.lang.IllegalArgumentException(
					"Source is not an instance of javax.management.ObjectName");
        }

		this.source = source;
	}

	/**
	 * Set the user data.
	 *
	 * @param userData set the user data object
	 */
	public void setUserData(Object userData)
	{
		this.userData = userData;
	}

	/**
	 * Get the source Object.
	 *
	 * @return The source object.
	 */
	public Object getSource()
	{
		return source;
	}

	public String toString()
	{
		StringBuffer b = new StringBuffer("[");
		b.append("Source = ").append(getSource()).append(", ");
		b.append("Message = ").append(getMessage()).append(", ");
		b.append("Sequence number = ").append(getSequenceNumber()).append(", ");
		b.append("Notification type = ").append(getType()).append(", ");
		b.append("Time = ").append(getTimeStamp()).append(", ");
		b.append("User data = ").append(getUserData());
		b.append("]");
		return b.toString();
	}

	//------------------------- Private methods ---------------------------//

	private void readObject(ObjectInputStream objectinputstream)
								throws IOException, ClassNotFoundException
	{
		ObjectInputStream.GetField getfield = objectinputstream.readFields();

		try
		{
			message = (String)getfield.get("message", "Notification...");
			sequenceNumber = getfield.get("sequenceNumber", 0l);
			source = (Object)getfield.get("source", null);
			type = (String)getfield.get("type", null);
			timeStamp = getfield.get("timeStamp", System.currentTimeMillis());
			userData = (Object)getfield.get("userData", null);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}

	private void writeObject(ObjectOutputStream objectoutputstream)
													throws IOException
	{
		ObjectOutputStream.PutField putfield = objectoutputstream.putFields();

		putfield.put("message", getMessage());
		putfield.put("sequenceNumber", getSequenceNumber());
		putfield.put("source", getSource());
		putfield.put("timeStamp", getTimeStamp());
		putfield.put("userData", getUserData());
		putfield.put("type", getType());

		objectoutputstream.writeFields();
	}
}