/**
	 * If Application Jar Path was set, load the jars
	 * @since 1.4.6
	 */
private void loadApplicationClasses() throws Exception {
    if (getApplicationJarPath() != null && getClassLoader() == null) {
        setClassLoader(ClassUtil.getClassLoader(getApplicationJarPath()));
        //update qsadmin to use the same  
        if (adminServer != null) {
            adminServer.getServer().setClassLoader(getClassLoader());
        }
    }
}
