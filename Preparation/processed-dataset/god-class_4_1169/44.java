/*
   * === Helper methods ===
   */
/** Call invokeSlave with the appropriate JVMBuilder. */
private void _doStartup() {
    File dir = _workingDir;
    // TODO: Eliminate NULL_FILE.  It is a bad idea!  The correct behavior when it is used always depends on 
    // context, so it can never be treated transparently.  In this case, the process won't start. 
    if (dir == FileOps.NULL_FILE) {
        dir = IOUtil.WORKING_DIRECTORY;
    }
    List<String> jvmArgs = new ArrayList<String>();
    // ConcJUnit argument: -Xbootclasspath/p:rt.concjunit.jar 
    // ------------------------------------------------------ 
    // this section here loops if the rt.concjunit.jar file is 
    // being re-generated or the settings are changed 
    final CompletionMonitor cm = new CompletionMonitor();
    boolean repeat;
    do {
        repeat = false;
        File junitLocation = DrJava.getConfig().getSetting(OptionConstants.JUNIT_LOCATION);
        // ConcJUnit is available if (a) the built-in framework is used, or (b) the external 
        // framework is a valid ConcJUnit jar file. 
        boolean concJUnitAvailable = !DrJava.getConfig().getSetting(OptionConstants.JUNIT_LOCATION_ENABLED) || edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidConcJUnitFile(junitLocation);
        File rtLocation = DrJava.getConfig().getSetting(OptionConstants.RT_CONCJUNIT_LOCATION);
        boolean rtLocationConfigured = edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidRTConcJUnitFile(rtLocation);
        if (DrJava.getConfig().getSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED).equals(OptionConstants.ConcJUnitCheckChoices.ALL) && // "lucky" enabled 
        !rtLocationConfigured && // not valid 
        (rtLocation != null) && // not null 
        (!FileOps.NULL_FILE.equals(rtLocation)) && // not NULL_FILE 
        (rtLocation.exists())) {
            // but exists 
            // invalid file, clear setting 
            DrJava.getConfig().setSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED, OptionConstants.ConcJUnitCheckChoices.NO_LUCKY);
            rtLocationConfigured = false;
            javax.swing.JOptionPane.showMessageDialog(null, "The selected file is invalid and was disabled:\n" + rtLocation, "Invalid ConcJUnit Runtime File", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        if (concJUnitAvailable && // ConcJUnit available 
        rtLocationConfigured && // runtime configured 
        DrJava.getConfig().getSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED).equals(OptionConstants.ConcJUnitCheckChoices.ALL)) {
            // and "lucky" enabled 
            try {
                // NOTE: this is a work-around 
                // it seems like it's impossible to pass long file names here on Windows 
                // so we are using a clumsy method that determines the short file name 
                File shortF = FileOps.getShortFile(rtLocation);
                // check the JavaVersion of the rt.concjunit.jar file to make sure it is compatible 
                if (edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isCompatibleRTConcJUnitFile(shortF)) {
                    // enabled, valid and compatible 
                    // add the JVM argument 
                    jvmArgs.add("-Xbootclasspath/p:" + shortF.getAbsolutePath().replace(File.separatorChar, '/'));
                } else {
                    // enabled, valid but incompatible 
                    // ask to regenerate 
                    repeat = true;
                    // re-check settings 
                    cm.reset();
                    boolean attempted = edu.rice.cs.drjava.model.junit.ConcJUnitUtils.showIncompatibleWantToRegenerateDialog(null, new Runnable() {

                        public void run() {
                            cm.signal();
                        }
                    }, // yes 
                    new Runnable() {

                        public void run() {
                            cm.signal();
                        }
                    });
                    // no 
                    while (!cm.attemptEnsureSignaled()) ;
                    // wait for dialog to finish 
                    if (!attempted) {
                        repeat = false;
                    }
                }
            } catch (IOException ioe) {
                // we couldn't get the short file name (on Windows), disable "lucky" warnings 
                DrJava.getConfig().setSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED, OptionConstants.ConcJUnitCheckChoices.NO_LUCKY);
                rtLocationConfigured = false;
                javax.swing.JOptionPane.showMessageDialog(null, "There was a problem with the selected file, and it was disabled:\n" + rtLocation, "Invalid ConcJUnit Runtime File", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    } while (repeat);
    // end of the section that may loop 
    // ------------------------------------------------------ 
    if (_allowAssertions) {
        jvmArgs.add("-ea");
    }
    int debugPort = _getDebugPort();
    if (debugPort > -1) {
        jvmArgs.add("-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=" + debugPort);
        jvmArgs.add("-Xdebug");
        jvmArgs.add("-Xnoagent");
        jvmArgs.add("-Djava.compiler=NONE");
    }
    String slaveMemory = DrJava.getConfig().getSetting(OptionConstants.SLAVE_JVM_XMX);
    if (!"".equals(slaveMemory) && !OptionConstants.heapSizeChoices.get(0).equals(slaveMemory)) {
        jvmArgs.add("-Xmx" + slaveMemory + "M");
    }
    String slaveArgs = DrJava.getConfig().getSetting(OptionConstants.SLAVE_JVM_ARGS);
    if (PlatformFactory.ONLY.isMacPlatform()) {
        jvmArgs.add("-Xdock:name=Interactions");
    }
    // add additional boot class path items specified by the selected compiler 
    for (File f : _interactionsModel.getCompilerBootClassPath()) {
        try {
            // NOTE: this is a work-around 
            // it seems like it's impossible to pass long file names here on Windows 
            // so we are using a clumsy method that determines the short file name 
            File shortF = FileOps.getShortFile(f);
            jvmArgs.add("-Xbootclasspath/a:" + shortF.getAbsolutePath().replace(File.separatorChar, '/'));
        } catch (IOException ioe) {
        }
    }
    jvmArgs.addAll(ArgumentTokenizer.tokenize(slaveArgs));
    JVMBuilder jvmb = new JVMBuilder(_startupClassPath).directory(dir).jvmArguments(jvmArgs);
    // extend classpath if JUnit/ConcJUnit location specified 
    File junitLocation = DrJava.getConfig().getSetting(OptionConstants.JUNIT_LOCATION);
    boolean junitLocationConfigured = (edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidJUnitFile(junitLocation) || edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidConcJUnitFile(junitLocation));
    if (DrJava.getConfig().getSetting(OptionConstants.JUNIT_LOCATION_ENABLED) && // enabled 
    !junitLocationConfigured && // not valid  
    (junitLocation != null) && // not null 
    (!FileOps.NULL_FILE.equals(junitLocation)) && // not NULL_FILE 
    (junitLocation.exists())) {
        // but exists 
        // invalid file, clear setting 
        DrJava.getConfig().setSetting(OptionConstants.JUNIT_LOCATION_ENABLED, false);
        junitLocationConfigured = false;
    }
    ArrayList<File> extendedClassPath = new ArrayList<File>();
    if (DrJava.getConfig().getSetting(OptionConstants.JUNIT_LOCATION_ENABLED) && junitLocationConfigured) {
        extendedClassPath.add(junitLocation);
    }
    for (File f : jvmb.classPath()) {
        extendedClassPath.add(f);
    }
    jvmb = jvmb.classPath(edu.rice.cs.plt.iter.IterUtil.asSizedIterable(extendedClassPath));
    // add Java properties controlling ConcJUnit 
    Map<String, String> props = jvmb.propertiesCopy();
    // settings are mutually exclusive 
    boolean all = DrJava.getConfig().getSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED).equals(OptionConstants.ConcJUnitCheckChoices.ALL);
    boolean noLucky = DrJava.getConfig().getSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED).equals(OptionConstants.ConcJUnitCheckChoices.NO_LUCKY);
    boolean onlyThreads = DrJava.getConfig().getSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED).equals(OptionConstants.ConcJUnitCheckChoices.ONLY_THREADS);
    boolean none = DrJava.getConfig().getSetting(OptionConstants.CONCJUNIT_CHECKS_ENABLED).equals(OptionConstants.ConcJUnitCheckChoices.NONE);
    // "threads" is enabled as long as the setting isn't NONE 
    props.put("edu.rice.cs.cunit.concJUnit.check.threads.enabled", new Boolean(!none).toString());
    // "join" is enabled for ALL and NO_LUCKY 
    props.put("edu.rice.cs.cunit.concJUnit.check.join.enabled", new Boolean(all || noLucky).toString());
    // "lucky" is enabled only for ALL 
    props.put("edu.rice.cs.cunit.concJUnit.check.lucky.enabled", new Boolean(all).toString());
    jvmb = jvmb.properties(props);
    invokeSlave(jvmb);
}
