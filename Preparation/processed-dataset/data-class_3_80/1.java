//}}} 
//{{{ The main() method of jsXe 
/**
     * The main method of jsXe
     * @param args The command line arguments
     */
public static void main(String args[]) {
    try {
        long startTime = System.currentTimeMillis();
        //{{{ Check the java version 
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.compareTo(MIN_JAVA_VERSION) < 0) {
            System.err.println(getAppTitle() + ": ERROR: You are running Java version " + javaVersion + ".");
            System.err.println(getAppTitle() + ": ERROR:" + getAppTitle() + " requires Java " + MIN_JAVA_VERSION + " or later.");
            System.exit(1);
        }
        //}}} 
        //{{{ set settings dirs 
        m_homeDirectory = System.getProperty("user.home");
        String fileSep = System.getProperty("file.separator");
        m_settingsDirectory = m_homeDirectory + fileSep + ".jsxe";
        File _settingsDirectory = new File(m_settingsDirectory);
        if (!_settingsDirectory.exists())
            _settingsDirectory.mkdirs();
        String pluginsDirectory = m_settingsDirectory + "/jars";
        File _pluginsDirectory = new File(pluginsDirectory);
        if (!_pluginsDirectory.exists())
            _pluginsDirectory.mkdirs();
        String jsXeHome = System.getProperty("jsxe.home");
        if (jsXeHome == null) {
            String classpath = System.getProperty("java.class.path");
            int index = classpath.toLowerCase().indexOf("jsxe.jar");
            int start = classpath.lastIndexOf(File.pathSeparator, index) + 1;
            // if started with java -jar jsxe.jar 
            if (classpath.equalsIgnoreCase("jsxe.jar")) {
                jsXeHome = System.getProperty("user.dir");
            } else {
                if (index > start) {
                    jsXeHome = classpath.substring(start, index - 1);
                } else {
                    // use user.dir as last resort 
                    jsXeHome = System.getProperty("user.dir");
                }
            }
        }
        //}}} 
        //{{{ start locale 
        Messages.initializePropertiesObject(null, jsXeHome + fileSep + "messages");
        //}}} 
        //{{{ get and load the configuration files 
        initDefaultProps();
        //}}} 
        //{{{ parse command line arguments 
        String viewname = null;
        ArrayList files = new ArrayList();
        boolean debug = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--help") || args[i].equals("-h")) {
                printUsage();
                System.exit(0);
            }
            if (args[i].equals("--version") || args[i].equals("-V")) {
                System.out.println(getVersion());
                System.exit(0);
            }
            if (args[i].equals("--debug")) {
                debug = true;
            } else {
                files.add(args[i]);
            }
        }
        //}}} 
        //{{{ start splash screen 
        ProgressSplashScreenWindow progressScreen = new ProgressSplashScreenWindow();
        int w = progressScreen.getSize().width;
        int h = progressScreen.getSize().height;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        progressScreen.setLocation(x, y);
        progressScreen.setVisible(true);
        //}}} 
        //{{{ start logging 
        Log.init(true, Log.ERROR, debug);
        try {
            BufferedWriter stream = new BufferedWriter(new FileWriter(getSettingsDirectory() + fileSep + "jsXe.log"));
            stream.flush();
            stream.write("Log file created on " + new Date());
            stream.write(System.getProperty("line.separator"));
            Log.setLogWriter(stream);
        } catch (IOException ioe) {
            Log.log(Log.ERROR, jsXe.class, ioe);
        }
        progressScreen.updateSplashScreenDialog(10);
        //}}} 
        //{{{ check Xerces version 
        String xercesVersion = org.apache.xerces.impl.Version.getVersion();
        if (MiscUtilities.compareStrings(xercesVersion, MIN_XERCES_VERSION, false) < 0) {
            String msg = Messages.getMessage("No.Xerces.Error", new String[] { MIN_XERCES_VERSION });
            Log.log(Log.ERROR, jsXe.class, msg);
            JOptionPane.showMessageDialog(null, msg, Messages.getMessage("No.Xerces.Error.Title", new String[] { MIN_XERCES_VERSION }), JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
        progressScreen.updateSplashScreenDialog(20);
        //}}} 
        //{{{ Load the recent files list 
        File recentFiles = new File(getSettingsDirectory(), "recent.xml");
        m_bufferHistory = new BufferHistory();
        try {
            m_bufferHistory.load(recentFiles);
        } catch (IOException ioe) {
            System.err.println(getAppTitle() + ": I/O ERROR: Could not open recent files list");
            System.err.println(getAppTitle() + ": I/O ERROR: " + ioe.toString());
        } catch (SAXException saxe) {
            System.err.println(getAppTitle() + ": I/O ERROR: recent.xml not formatted properly");
            System.err.println(getAppTitle() + ": I/O ERROR: " + saxe.toString());
        } catch (ParserConfigurationException pce) {
            System.err.println(getAppTitle() + ": I/O ERROR: Could not parse recent.xml");
            System.err.println(getAppTitle() + ": I/O ERROR: " + pce.toString());
        }
        progressScreen.updateSplashScreenDialog(30);
        //}}} 
        //{{{ load plugins 
        Log.log(Log.NOTICE, jsXe.class, "Loading plugins");
        m_pluginLoader = new JARClassLoader();
        Log.log(Log.NOTICE, jsXe.class, "Adding to plugin search path: " + pluginsDirectory);
        ArrayList pluginMessages = m_pluginLoader.addDirectory(pluginsDirectory);
        //add the jsXe home to the plugins directory 
        Log.log(Log.NOTICE, jsXe.class, "Adding to plugin search path: " + jsXeHome + fileSep + "jars");
        pluginMessages.addAll(m_pluginLoader.addDirectory(jsXeHome + fileSep + "jars"));
        progressScreen.updateSplashScreenDialog(40);
        //}}} 
        //{{{ start plugins 
        Log.log(Log.NOTICE, jsXe.class, "Starting plugins");
        pluginMessages.addAll(m_pluginLoader.startPlugins());
        Vector pluginErrors = new Vector();
        if (pluginMessages.size() != 0) {
            for (int i = 0; i < pluginMessages.size(); i++) {
                Object error = pluginMessages.get(i);
                if ((error instanceof IOException) || (error instanceof PluginDependencyException)) {
                    Log.log(Log.ERROR, jsXe.class, ((Exception) error).getMessage());
                    pluginErrors.add(((Exception) error).getMessage());
                } else {
                    if (error instanceof PluginLoadException) {
                        Log.log(Log.WARNING, jsXe.class, ((PluginLoadException) error).getMessage());
                    } else {
                        Log.log(Log.WARNING, jsXe.class, error.toString());
                    }
                }
            }
        }
        progressScreen.updateSplashScreenDialog(50);
        Iterator pluginItr = m_pluginLoader.getAllPlugins().iterator();
        while (pluginItr.hasNext()) {
            //load properties into jsXe's properties 
            ActionPlugin plugin = (ActionPlugin) pluginItr.next();
            Properties props = plugin.getProperties();
            Enumeration names = props.propertyNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement().toString();
                setProperty(name, props.getProperty(name));
            }
            addActionSet(plugin.getActionSet());
        }
        progressScreen.updateSplashScreenDialog(60);
        //}}} 
        //{{{ load user specific properties 
        File properties = new File(getSettingsDirectory(), "properties");
        try {
            FileInputStream filestream = new FileInputStream(properties);
            props.load(filestream);
        } catch (FileNotFoundException fnfe) {
            //Don't do anything right now 
            Log.log(Log.MESSAGE, jsXe.class, "User has no properties file. Running jsXe for the first time?");
        } catch (IOException ioe) {
            System.err.println(getAppTitle() + ": I/O ERROR: Could not open settings file");
            System.err.println(getAppTitle() + ": I/O ERROR: " + ioe.toString());
        }
        //init the catalog manager 
        CatalogManager.propertiesChanged();
        progressScreen.updateSplashScreenDialog(70);
        //}}} 
        //{{{ create the TabbedView 
        Log.log(Log.NOTICE, jsXe.class, "Starting the main window");
        TabbedView tabbedview = null;
        DocumentBuffer defaultBuffer = null;
        try {
            defaultBuffer = new DocumentBuffer();
            m_buffers.add(defaultBuffer);
            if (viewname == null) {
                tabbedview = new TabbedView(defaultBuffer);
            } else {
                try {
                    tabbedview = new TabbedView(defaultBuffer, viewname);
                } catch (UnrecognizedPluginException e) {
                    Log.log(Log.ERROR, jsXe.class, e.getMessage());
                    System.exit(1);
                }
            }
        } catch (IOException ioe) {
            Log.log(Log.ERROR, jsXe.class, ioe);
            JOptionPane.showMessageDialog(null, ioe.getMessage() + ".", Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
        m_activeView = tabbedview;
        progressScreen.updateSplashScreenDialog(85);
        //}}} 
        //{{{ Parse files to open on the command line 
        Log.log(Log.NOTICE, jsXe.class, "Parsing files to open on command line");
        if (files.size() > 0) {
            if (openXMLDocuments(tabbedview, (String[]) files.toArray(new String[] {}))) {
                try {
                    closeDocumentBuffer(tabbedview, defaultBuffer);
                } catch (IOException ioe) {
                }
            }
        }
        progressScreen.updateSplashScreenDialog(100);
        //}}} 
        tabbedview.setVisible(true);
        progressScreen.dispose();
        //Show plugin error dialog 
        if (pluginErrors.size() > 0) {
            new ErrorListDialog(tabbedview, "Plugin Error", "The following plugins could not be loaded:", new Vector(pluginErrors), true);
        }
        Log.log(Log.NOTICE, jsXe.class, "jsXe started in " + (System.currentTimeMillis() - startTime) + " milliseconds");
    } catch (Throwable e) {
        exiterror(null, e, 1);
    }
}
