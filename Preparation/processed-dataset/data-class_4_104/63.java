/**
	 *  returns the directory of the main server config. other classes can
	 * fetch their configs from there (e.g. the Authenticator-classes. 
	 */
public File getConfigDir() {
    return (configFile != null) ? configFile.getParentFile() : null;
}
