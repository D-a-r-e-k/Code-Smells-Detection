/**********************************************************************************************
	 * PLUGIN-METHODS (used for Commands,XmlRpc Handler and ServerPlugin)
	 **********************************************************************************************/
private void loadCommands(Vector<String> jarUrl) {
    allCommands = new HashMap<String, Object>();
    Vector<String> commandUrl = new Vector<String>();
    HashMap<String, Object> commandsStore = new HashMap<String, Object>();
    Enumeration<JarEntry> entries = null;
    for (Iterator<String> iterator = jarUrl.iterator(); iterator.hasNext(); ) {
        String jUrl = (String) iterator.next();
        try {
            entries = new JarFile(jUrl).entries();
        } catch (IOException e) {
            Server.log(this, "Jar File:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
        }
        String packagePattern = "freecs/commands/[^/]+\\.class";
        if (entries == null)
            Server.log(this, "illegal jar File", Server.MSG_ERROR, Server.LVL_HALT);
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().matches(packagePattern)) {
                //list only the classes in the com.google.inject Package   
                StringBuilder url = new StringBuilder(jarEntry.getName());
                int i = url.toString().indexOf(".");
                url = new StringBuilder(url.substring(0, i).toString().replaceAll("/", "."));
                // url = new StringBuilder(url.toString().replaceAll("/", "."));  
                if (url.toString().equals("freecs.commands.AbstractCommand") || url.toString().equals("freecs.commands.CommandSet"))
                    continue;
                commandUrl.add(url.toString());
                Class<?> piClass = null;
                try {
                    piClass = Class.forName(url.toString());
                } catch (ClassNotFoundException e) {
                    Server.log(this, "Class.forName:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
                }
                if (piClass == null)
                    continue;
                Method getInstance = null;
                try {
                    getInstance = piClass.getMethod("getInstance");
                } catch (SecurityException e) {
                    Server.log(this, "get Instance:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
                } catch (NoSuchMethodException e) {
                    Server.log(this, "get Instance:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
                }
                if (getInstance == null) {
                    Server.log(this, "Specified command-object doesn't implement static getInstance", Server.MSG_ERROR, Server.LVL_MAJOR);
                    continue;
                }
            } else {
                Server.log(this, "ignore Command Url " + jarEntry.getName(), Server.MSG_CONFIG, Server.LVL_VERY_VERBOSE);
            }
        }
    }
    for (Iterator<String> iterator = commandUrl.iterator(); iterator.hasNext(); ) {
        StringBuilder url = new StringBuilder((String) iterator.next());
        if (url.toString().length() < 1)
            continue;
        Object o = null;
        ;
        synchronized (commandsStore) {
            o = commandsStore.get(url.toString());
            if (o == null) {
                try {
                    Class<?> piClass = null;
                    try {
                        piClass = Class.forName(url.toString());
                    } catch (ClassNotFoundException e) {
                        Server.log(this, "Class.forName:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
                    }
                    Method getInstance = piClass.getMethod("getInstance");
                    if (getInstance == null) {
                        Server.log(this, "Specified Command-object doesn't implement static getMasterInstance", Server.MSG_ERROR, Server.LVL_MAJOR);
                        continue;
                    }
                    Object arg0 = null;
                    o = getInstance.invoke(arg0);
                    if (!(o instanceof ICommand)) {
                        Server.log(this, "Specified Command-object (" + url.toString() + ") doesn't implement interface ICommand", Server.MSG_ERROR, Server.LVL_MAJOR);
                        continue;
                    }
                    commandsStore.put(url.toString(), o);
                } catch (Exception e) {
                    Server.log(this, "invalid url for Command: (" + e + ") Url:" + url.toString(), Server.MSG_ERROR, Server.LVL_MINOR);
                    continue;
                }
            }
        }
        String cmd = ((ICommand) o).getCmd();
        String version = ((ICommand) o).getVersion();
        if (!version.startsWith("1.")) {
            Server.log(this, "invalid commandversion " + cmd + " (" + url.toString() + ")", Server.MSG_ERROR, Server.LVL_MAJOR);
            continue;
        }
        if (!allCommands.containsKey(cmd)) {
            allCommands.put(((ICommand) o).getCmd(), ((ICommand) o).instanceForSystem());
            if (Server.DEBUG)
                Server.log(this, "added Command " + cmd + "[ " + version + " ] (" + url.toString(), Server.MSG_CONFIG, Server.LVL_MAJOR);
        } else {
            Server.log(this, "Command " + cmd + " exists!", Server.MSG_CONFIG, Server.LVL_MAJOR);
            if (Server.DEBUG)
                Server.log(this, "ignore Command " + cmd + "[ " + version + " ] (" + url, Server.MSG_CONFIG, Server.LVL_MAJOR);
        }
    }
    commandUrl = null;
    commandsStore = null;
    entries = null;
}
