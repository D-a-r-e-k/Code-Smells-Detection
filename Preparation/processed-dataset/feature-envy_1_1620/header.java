void method0() { 
// ----------------------------------------------------- Instance Variables 
/**
     * Path where context descriptors should be deployed.
     */
protected File configBase = null;
/**
     * The Context container associated with our web application.
     */
protected Context context = null;
/**
     * The debugging detail level for this servlet.
     */
protected int debug = 1;
/**
     * File object representing the directory into which the deploy() command
     * will store the WAR and context configuration files that have been
     * uploaded.
     */
protected File deployed = null;
/**
     * Path used to store revisions of webapps.
     */
protected File versioned = null;
/**
     * Path used to store context descriptors.
     */
protected File contextDescriptors = null;
/**
     * The associated host.
     */
protected Host host = null;
/**
     * The host appBase.
     */
protected File appBase = null;
/**
     * MBean server.
     */
protected MBeanServer mBeanServer = null;
/**
     * The associated deployer ObjectName.
     */
protected ObjectName oname = null;
/**
     * The global JNDI <code>NamingContext</code> for this server,
     * if available.
     */
protected javax.naming.Context global = null;
/**
     * The string manager for this package.
     */
protected static final StringManager sm = StringManager.getManager(Constants.Package);
/**
     * The Wrapper container associated with this servlet.
     */
protected Wrapper wrapper = null;
}
