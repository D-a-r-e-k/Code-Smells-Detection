private void dump() {
    if (!log.isDebugEnabled()) {
        return;
    }
    StringBuilder buf = new StringBuilder();
    //$NON-NLS-1$  
    buf.append("PLUG-IN REGISTRY DUMP:\r\n").append("-------------- DUMP BEGIN -----------------\r\n").append("\tPlug-ins: " + registeredPlugins.size() + "\r\n");
    //$NON-NLS-1$  
    for (PluginDescriptor descriptor : registeredPlugins.values()) {
        //$NON-NLS-1$  
        buf.append("\t\t").append(descriptor).append("\r\n");
    }
    buf.append("\tFragments: " + registeredFragments.size() + "\r\n");
    //$NON-NLS-1$  
    for (PluginFragment fragment : registeredFragments.values()) {
        //$NON-NLS-1$  
        buf.append("\t\t").append(fragment).append("\r\n");
    }
    //$NON-NLS-1$  
    //$NON-NLS-1$  
    //$NON-NLS-1$  
    buf.append("Memory TOTAL/FREE/MAX: ").append(Runtime.getRuntime().totalMemory()).append("/").append(Runtime.getRuntime().freeMemory()).append("/").append(Runtime.getRuntime().maxMemory()).append("\r\n");
    //$NON-NLS-1$  
    buf.append("-------------- DUMP END -----------------\r\n");
    //$NON-NLS-1$  
    log.debug(buf.toString());
}
