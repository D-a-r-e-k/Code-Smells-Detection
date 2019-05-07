/**
	 * This method tests whether the monitor MBean is active. A monitor MBean
	 * is marked active when the start method is called. It becomes inactive
	 * when the stop method is called.
	 *
	 * @return boolean value indicating whether the MBean is active or not.
	 */
public boolean isActive() {
    return isActive;
}
