private void loadPlugin(Vector<String> pluginUrl) {
    Vector<IServerPlugin> plugins = new Vector<IServerPlugin>();
    for (Iterator<String> iterator = pluginUrl.iterator(); iterator.hasNext(); ) {
        StringBuilder url = new StringBuilder((String) iterator.next());
        if (url.toString().length() < 1)
            continue;
        Object o;
        synchronized (pluginStore) {
            o = pluginStore.get(url.toString());
            if (o == null) {
                try {
                    Class<?> piClass = null;
                    try {
                        piClass = Class.forName(url.toString());
                    } catch (ClassNotFoundException e) {
                        Server.log(this, "Class.forName:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
                    }
                    if (piClass == null)
                        continue;
                    Method getInstance = piClass.getMethod("getMasterInstance");
                    if (getInstance == null) {
                        Server.log(this, "Specified plugin-object doesn't implement static getMasterInstance", Server.MSG_ERROR, Server.LVL_MAJOR);
                        continue;
                    }
                    Object arg0 = null;
                    o = getInstance.invoke(arg0);
                    if (!(o instanceof IServerPlugin)) {
                        Server.log(this, "Specified plugin-object doesn't implement interface IServerPlugin", Server.MSG_ERROR, Server.LVL_MAJOR);
                        continue;
                    }
                    pluginStore.put(url.toString(), o);
                } catch (Exception e) {
                    Server.log(this, "invalid url for Plugin: (" + e + ") Url:" + url.toString(), Server.MSG_ERROR, Server.LVL_MINOR);
                    continue;
                }
            }
        }
        try {
            plugins.add(((IServerPlugin) o).instanceForSystem());
        } catch (Exception e) {
            Server.debug(this, "catched exception while getting ServerPlugin-instance", e, Server.MSG_STATE, Server.LVL_MAJOR);
        }
        serverPlugin = plugins.toArray(new IServerPlugin[0]);
    }
    plugins = null;
}
