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
public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
    this.server = server;
    return name;
}
