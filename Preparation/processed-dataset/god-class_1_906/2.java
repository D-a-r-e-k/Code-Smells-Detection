//}}} 
//{{{ saveGeometry() method 
/**
     * Saves a window's geometry to the properties.
     * The geometry is saved to the <code><i>name</i>.x</code>,
     * <code><i>name</i>.y</code>, <code><i>name</i>.width</code> and
     * <code><i>name</i>.height</code> properties.
     * @param win The window
     * @param name The window name
     */
public static void saveGeometry(Window win, String name) {
    if ((win instanceof Frame) && OperatingSystem.hasJava14()) {
        try {
            java.lang.reflect.Method meth = Frame.class.getMethod("getExtendedState", new Class[0]);
            Integer extState = (Integer) meth.invoke(win, new Object[0]);
            jsXe.setIntegerProperty(name + ".extendedState", extState.intValue());
            if (extState.intValue() != Frame.NORMAL) {
                return;
            }
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        } catch (IllegalAccessException e3) {
        } catch (java.lang.reflect.InvocationTargetException e4) {
        }
    }
    Rectangle bounds = win.getBounds();
    jsXe.setIntegerProperty(name + ".x", bounds.x);
    jsXe.setIntegerProperty(name + ".y", bounds.y);
    jsXe.setIntegerProperty(name + ".width", bounds.width);
    jsXe.setIntegerProperty(name + ".height", bounds.height);
}
