/**
   * Exits Jext by closing all the windows. If backgrounding, it doesn't kill the jext server.
   * This is synchronized because creating two windows at a time can be problematic.
   */
public static void exit() {
    synchronized (instances) {
        Object[] o = instances.toArray();
        for (int i = o.length - 1; i >= 0; i--) {
            JextFrame instance = ((JextFrame) o[i]);
            /*if (i == 0)
        {
          instance.fireJextEvent(JextEvent.KILLING_JEXT);
          if (!isRunningBg())
            stopPlugins();
        } else
          instance.fireJextEvent(JextEvent.CLOSE_WINDOW);*/
            closeToQuit(instance);
        }
    }
}
