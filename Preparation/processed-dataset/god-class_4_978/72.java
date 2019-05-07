////////////////////////////////////////////////////////////////////////////////////////////// 
// END OF STATIC PART 
////////////////////////////////////////////////////////////////////////////////////////////// 
// MAIN ENTRY POINT 
////////////////////////////////////////////////////////////////////////////////////////////// 
/**
   * Start method. Load the property file, set the look and feel, create a new GUI,
   * load the options. If a file name is specified as first argument, we pass it
   * to the window contructor which will construct its full path (because you can
   * specify, for example, ..\..\test.java or ~\jext.props or ...../hello.cpp -
   * both / and \ can be used -)
   */
public static void main(String args[]) {
    ///////////////////////////////// DEBUG 
    System.setErr(System.out);
    initDirectories();
    args = parseOptions(args);
    synchronized (instances) {
        loadInSingleJVMInstance(args);
        initProperties();
        if (!isRunningBg()) {
            splash = new SplashScreen();
            newWindow(args);
        } else {
            if (keepInMemory)
                splash = new SplashScreen();
            //FIXME:maybe it should ignore arguments when backgrounding. 
            builtTextArea = newWindow(args, false);
            if (keepInMemory)
                newWindow(null, true);
        }
    }
    if (getBooleanProperty("check"))
        check = new VersionCheck();
}
