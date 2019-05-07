/**
   * Init the properties.
   */
public static void initProperties() {
    usrProps = SETTINGS_DIRECTORY + ".jext-props.xml";
    defaultProps = props = new Properties();
    ///////////////////////////////////////////////////////////////// 
    // DEPRECATED BY THE METHOD loadXMLProps() 
    ///////////////////////////////////////////////////////////////// 
    //    loadProps(Jext.class.getResourceAsStream("jext-gui.keys")); 
    //    loadProps(Jext.class.getResourceAsStream("jext-gui.text")); 
    //    loadProps(Jext.class.getResourceAsStream("jext.props")); 
    //    loadProps(Jext.class.getResourceAsStream("jext.tips")); 
    ///////////////////////////////////////////////////////////////// 
    // loads specified language pack 
    File lang = new File(SETTINGS_DIRECTORY + ".lang");
    if (lang.exists()) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(lang));
            String language = reader.readLine();
            reader.close();
            if (language != null && !language.equals("English")) {
                File langPack = new File(JEXT_HOME + File.separator + "lang" + File.separator + language + "_pack.jar");
                if (langPack.exists()) {
                    languagePack = new ZipFile(langPack);
                    languageEntries = new ArrayList();
                    Enumeration entries = languagePack.entries();
                    while (entries.hasMoreElements()) languageEntries.add(entries.nextElement());
                    setLanguage(language);
                } else
                    lang.delete();
            }
        } catch (IOException ioe) {
        }
    }
    //loadXMLProps(Jext.class.getResourceAsStream("jext.props.xml"), "jext.props.xml"); 
    loadXMLProps(Jext.class.getResourceAsStream("jext-text.props.xml"), "jext-text.props.xml");
    loadXMLProps(Jext.class.getResourceAsStream("jext-keys.props.xml"), "jext-keys.props.xml");
    loadXMLProps(Jext.class.getResourceAsStream("jext-defs.props.xml"), "jext-defs.props.xml");
    loadXMLProps(Jext.class.getResourceAsStream("jext-tips.props.xml"), "jext-tips.props.xml");
    Properties pyProps = new Properties();
    pyProps.put("python.cachedir", SETTINGS_DIRECTORY + "pythoncache" + File.separator);
    PythonInterpreter.initialize(System.getProperties(), pyProps, new String[0]);
    initPlugins();
    if (usrProps != null) {
        props = new Properties(defaultProps);
        try {
            loadXMLProps(new FileInputStream(USER_PROPS), ".jext-props.xml");
            if (DELETE_OLD_SETTINGS) {
                String pVersion = getProperty("properties.version");
                if (pVersion == null || BUILD.compareTo(pVersion) > 0) {
                    File userSettings = new File(USER_PROPS);
                    if (userSettings.exists()) {
                        userSettings.delete();
                        defaultProps = props = new Properties();
                        //loadXMLProps(Jext.class.getResourceAsStream("jext.props.xml"), "jext.props.xml"); 
                        loadXMLProps(Jext.class.getResourceAsStream("jext-text.props.xml"), "jext-text.props.xml");
                        loadXMLProps(Jext.class.getResourceAsStream("jext-keys.props.xml"), "jext-keys.props.xml");
                        loadXMLProps(Jext.class.getResourceAsStream("jext-defs.props.xml"), "jext-defs.props.xml");
                        loadXMLProps(Jext.class.getResourceAsStream("jext-tips.props.xml"), "jext-tips.props.xml");
                        JARClassLoader.reloadPluginsProperties();
                        props = new Properties(defaultProps);
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
        } catch (IOException ioe) {
        }
    }
    initModes();
    //must be here since the user can change the mode filters. 
    Search.load();
    if (Utilities.JDK_VERSION.charAt(2) >= '4') {
        try {
            Class cl = Class.forName("org.jext.JavaSupport");
            Method m = cl.getMethod("initJavaSupport", new Class[0]);
            if (m != null)
                m.invoke(null, new Object[0]);
        } catch (Exception e) {
        }
    }
    // Add our protocols to java.net.URL's list 
    System.getProperties().put("java.protocol.handler.pkgs", "org.jext.protocol|" + System.getProperty("java.protocol.handler.pkgs", ""));
    initActions();
    JARClassLoader.initPlugins();
    initUI();
    sortModes();
    assocPluginsToModes();
}
