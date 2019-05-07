public static void closeWindow(JextFrame frame) {
    synchronized (instances) {
        if (getWindowsCount() == 1)
            frame.fireJextEvent(JextEvent.KILLING_JEXT);
        else
            frame.fireJextEvent(JextEvent.CLOSING_WINDOW);
        frame.closeWindow();
        if (getWindowsCount() == 0) {
            if (!isRunningBg())
                stopServer();
            Search.save();
            if (!isRunningBg())
                stopPlugins();
            frame.saveConsole();
            GUIUtilities.saveGeometry(frame, "jext");
            saveXMLProps("Jext v" + Jext.RELEASE + " b" + Jext.BUILD);
            //frame.cleanMemory(); 
            frame = null;
            System.gc();
            //this way, the garbage collector should do its work, without any NullPointerEx at all. 
            if (isRunningBg())
                builtTextArea = newWindow(null, false);
            else
                System.exit(0);
        }
    }
}
