//}}} 
//{{{ loadGeometry() 
/**
     * Loads a windows's geometry from the properties.
     * The geometry is loaded from the <code><i>name</i>.x</code>,
     * <code><i>name</i>.y</code>, <code><i>name</i>.width</code> and
     * <code><i>name</i>.height</code> properties.
     *
     * @param win The window
     * @param name The window name
     */
public static void loadGeometry(Window win, String name) {
    int x, y, width, height;
    Dimension size = win.getSize();
    Dimension screen = win.getToolkit().getScreenSize();
    width = jsXe.getIntegerProperty(name + ".width", size.width);
    height = jsXe.getIntegerProperty(name + ".height", size.height);
    Component parent = win.getParent();
    if (parent == null) {
        x = (screen.width - width) / 2;
        y = (screen.height - height) / 2;
    } else {
        Rectangle bounds = parent.getBounds();
        x = bounds.x + (bounds.width - width) / 2;
        y = bounds.y + (bounds.height - height) / 2;
    }
    x = jsXe.getIntegerProperty(name + ".x", x);
    y = jsXe.getIntegerProperty(name + ".y", y);
    // Make sure the window is displayed in visible region 
    Rectangle osbounds = OperatingSystem.getScreenBounds();
    if (x < osbounds.x || x + width > osbounds.width) {
        if (width > osbounds.width)
            width = osbounds.width;
        x = (osbounds.width - width) / 2;
    }
    if (y < osbounds.y || y + height > osbounds.height) {
        if (height >= osbounds.height) {
            height = osbounds.height;
        }
        y = (osbounds.height - height) / 2;
    }
    Rectangle desired = new Rectangle(x, y, width, height);
    win.setBounds(desired);
    if ((win instanceof Frame) && OperatingSystem.hasJava14()) {
        int extState = jsXe.getIntegerProperty(name + ".extendedState", Frame.NORMAL);
        try {
            java.lang.reflect.Method meth = Frame.class.getMethod("setExtendedState", new Class[] { int.class });
            meth.invoke(win, new Object[] { new Integer(extState) });
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e2) {
        } catch (IllegalAccessException e3) {
        } catch (java.lang.reflect.InvocationTargetException e4) {
        }
    }
}
