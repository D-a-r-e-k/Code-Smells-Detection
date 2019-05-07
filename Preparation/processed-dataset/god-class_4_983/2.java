/**
     * <p>
     * Initialize the <code>{@link org.quartz.SchedulerFactory}</code> with
     * the contents of a <code>Properties</code> file and overriding System
     * properties.
     * </p>
     *
     * <p>
     * By default a properties file named "quartz.properties" is loaded from
     * the 'current working directory'. If that fails, then the
     * "quartz.properties" file located (as a resource) in the org/quartz
     * package is loaded. If you wish to use a file other than these defaults,
     * you must define the system property 'org.quartz.properties' to point to
     * the file you want.
     * </p>
     *
     * <p>
     * System properties (environment variables, and -D definitions on the
     * command-line when running the JVM) override any properties in the
     * loaded file.  For this reason, you may want to use a different initialize()
     * method if your application security policy prohibits access to
     * <code>{@link java.lang.System#getProperties()}</code>.
     * </p>
     */
public void initialize() throws SchedulerException {
    // short-circuit if already initialized 
    if (cfg != null) {
        return;
    }
    if (initException != null) {
        throw initException;
    }
    String requestedFile = System.getProperty(PROPERTIES_FILE);
    String propFileName = requestedFile != null ? requestedFile : "quartz.properties";
    File propFile = new File(propFileName);
    Properties props = new Properties();
    InputStream in = null;
    try {
        if (propFile.exists()) {
            try {
                if (requestedFile != null) {
                    propSrc = "specified file: '" + requestedFile + "'";
                } else {
                    propSrc = "default file in current working dir: 'quartz.properties'";
                }
                in = new BufferedInputStream(new FileInputStream(propFileName));
                props.load(in);
            } catch (IOException ioe) {
                initException = new SchedulerException("Properties file: '" + propFileName + "' could not be read.", ioe);
                throw initException;
            }
        } else if (requestedFile != null) {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(requestedFile);
            if (in == null) {
                initException = new SchedulerException("Properties file: '" + requestedFile + "' could not be found.");
                throw initException;
            }
            propSrc = "specified file: '" + requestedFile + "' in the class resource path.";
            in = new BufferedInputStream(in);
            try {
                props.load(in);
            } catch (IOException ioe) {
                initException = new SchedulerException("Properties file: '" + requestedFile + "' could not be read.", ioe);
                throw initException;
            }
        } else {
            propSrc = "default resource file in Quartz package: 'quartz.properties'";
            ClassLoader cl = getClass().getClassLoader();
            if (cl == null)
                cl = findClassloader();
            if (cl == null)
                throw new SchedulerConfigException("Unable to find a class loader on the current thread or class.");
            in = cl.getResourceAsStream("quartz.properties");
            if (in == null) {
                in = cl.getResourceAsStream("/quartz.properties");
            }
            if (in == null) {
                in = cl.getResourceAsStream("org/quartz/quartz.properties");
            }
            if (in == null) {
                initException = new SchedulerException("Default quartz.properties not found in class path");
                throw initException;
            }
            try {
                props.load(in);
            } catch (IOException ioe) {
                initException = new SchedulerException("Resource properties file: 'org/quartz/quartz.properties' " + "could not be read from the classpath.", ioe);
                throw initException;
            }
        }
    } finally {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
    }
    initialize(overrideWithSysProps(props));
}
