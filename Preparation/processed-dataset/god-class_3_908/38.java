//}}} 
//{{{ initDefaultProps() 
/**
     * Initialize the Default properties.
     */
private static void initDefaultProps() {
    //{{{ Load jsXe default properties 
    InputStream inputstream = jsXe.class.getResourceAsStream("/net/sourceforge/jsxe/properties");
    try {
        defaultProps.load(inputstream);
    } catch (IOException ioe) {
        Log.log(Log.ERROR, jsXe.class, "**** Could not open default settings file ****");
        Log.log(Log.ERROR, jsXe.class, "**** jsXe was probably not built correctly ****");
        exiterror(null, ioe, 1);
    }
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int windowWidth = (int) (screenSize.getWidth() / 2);
    int windowHeight = (int) (3 * screenSize.getHeight() / 4);
    int x = (int) (screenSize.getWidth() / 4);
    int y = (int) (screenSize.getHeight() / 8);
    defaultProps.setProperty("tabbedview.height", Integer.toString(windowHeight));
    defaultProps.setProperty("tabbedview.width", Integer.toString(windowWidth));
    defaultProps.setProperty("tabbedview.x", Integer.toString(x));
    defaultProps.setProperty("tabbedview.y", Integer.toString(y));
    //}}} 
    //{{{ Load build properties 
    inputstream = jsXe.class.getResourceAsStream("/net/sourceforge/jsxe/build.properties");
    try {
        buildProps.load(inputstream);
    } catch (IOException ioe) {
        Log.log(Log.ERROR, jsXe.class, "**** Could not open build properties file ****");
        Log.log(Log.ERROR, jsXe.class, "**** jsXe was probably not built correctly ****");
        exiterror(null, ioe, 1);
    }
}
