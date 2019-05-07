private void loadXmlRpcHandler(Vector<String> jarUrl) {
    xmlRpcHandler = new HashMap<String, Object>();
    Vector<String> xmlRpcUrl = new Vector<String>();
    HashMap<String, Object> handlerStore = new HashMap<String, Object>();
    Enumeration<JarEntry> entries = null;
    for (Iterator<String> iterator = jarUrl.iterator(); iterator.hasNext(); ) {
        String jUrl = (String) iterator.next();
        try {
            entries = new JarFile(jUrl).entries();
        } catch (IOException e) {
            Server.log(this, "Jar File:" + e, Server.MSG_ERROR, Server.LVL_MAJOR);
        }
        String packagePattern = "freecs/external/xmlrpc/[^/]+\\.class";
        if (entries == null)
            Server.log(this, "illegal jar File", Server.MSG_ERROR, Server.LVL_HALT);
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().matches(packagePattern)) {
                //list only the classes in the freecs.external.xmlrpc Package   
                StringBuilder url = new StringBuilder(jarEntry.getName());
                int i = url.toString().indexOf(".");
                url = new StringBuilder(url.substring(0, i).toString().replaceAll("/", "."));
                if (url.toString().equals("freecs.external.xmlrpc.XmlRpcSendUser") || url.toString().equals("freecs.external.xmlrpc.XmlRpcManager") || url.toString().equals("freecs.external.xmlrpc.XmlRpcSendUser$UserState"))
                    continue;
                xmlRpcUrl.add(url.toString());
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
                    Server.log(this, "Specified xml rpc handler-object (" + url.toString() + ") doesn't implement static getInstance", Server.MSG_ERROR, Server.LVL_MAJOR);
                    continue;
                }
            } else {
                Server.log(this, "ignore XmlRpcHandler Url " + jarEntry.getName(), Server.MSG_CONFIG, Server.LVL_VERY_VERBOSE);
            }
        }
    }
    for (Iterator<String> iterator = xmlRpcUrl.iterator(); iterator.hasNext(); ) {
        StringBuilder url = new StringBuilder((String) iterator.next());
        if (url.toString().length() < 1)
            continue;
        Object o = null;
        ;
        synchronized (handlerStore) {
            o = handlerStore.get(url.toString());
            if (o == null) {
                try {
                    Class<?> piClass = Class.forName(url.toString());
                    Method getInstance = piClass.getMethod("getInstance");
                    if (getInstance == null) {
                        Server.log(this, "Specified Handler-object doesn't implement static getMasterInstance", Server.MSG_ERROR, Server.LVL_MAJOR);
                        continue;
                    }
                    Object arg0 = null;
                    o = getInstance.invoke(arg0);
                    if (!(o instanceof IXmlRpcHandler)) {
                        Server.log(this, "Specified Handler-object doesn't implement interface IXmlRpcHandler", Server.MSG_ERROR, Server.LVL_MAJOR);
                        continue;
                    }
                    handlerStore.put(url.toString(), o);
                } catch (Exception e) {
                    Server.log(this, "invalid url for Handler: (" + e + ") Url:" + url, Server.MSG_ERROR, Server.LVL_MINOR);
                    continue;
                }
            }
        }
        String handler = ((IXmlRpcHandler) o).getHandlername();
        String version = ((IXmlRpcHandler) o).getVersion();
        if (!version.startsWith("1.")) {
            Server.log(this, "invalid xmlrpchandlerversion " + handler + " (" + url + ")", Server.MSG_ERROR, Server.LVL_MAJOR);
            continue;
        }
        if (!xmlRpcHandler.containsKey(handler)) {
            xmlRpcHandler.put(((IXmlRpcHandler) o).getHandlername(), ((IXmlRpcHandler) o).instanceForSystem());
            if (Server.DEBUG)
                Server.log(this, "add XmlRpcHandler " + handler + "[ " + version + " ] (" + url + ")", Server.MSG_CONFIG, Server.LVL_MAJOR);
        } else {
            Server.log(this, "XmlRpcHandler " + handler + " exists!", Server.MSG_CONFIG, Server.LVL_MAJOR);
            if (Server.DEBUG)
                Server.log(this, "ignore XmlRpcHandler " + handler + "[ " + version + " ] (" + url, Server.MSG_CONFIG, Server.LVL_MAJOR);
        }
    }
    xmlRpcUrl = null;
    handlerStore = null;
    entries = null;
}
