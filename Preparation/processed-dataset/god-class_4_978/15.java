/**
   * Returns an array of installed plugins.
   */
public static Plugin[] getPlugins() {
    /*Object[] o = plugins.toArray();
    Plugin[] p = new Plugin[o.length];
    for (int i = 0; i < o.length; i++)
      p[i] = (Plugin) o[i];*/
    Plugin[] p = (Plugin[]) plugins.toArray(new Plugin[0]);
    return p;
}
