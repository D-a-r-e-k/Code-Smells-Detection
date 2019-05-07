// *** End of Service interface methods  
/**
	 * Initialise and create the server.
	 * @param param of the xml configuration file.
	 * @exception AppException if QuickServerConfig creation failed from the xml config file.
	 * @since 1.4.7
	 */
public synchronized void initServer(Object param[]) throws AppException {
    QuickServerConfig qsConfig = null;
    try {
        qsConfig = ConfigReader.read((String) param[0]);
    } catch (Exception e) {
        logger.severe("Could not init server from xml file " + (new File((String) param[0]).getAbsolutePath()) + " : " + e);
        throw new AppException("Could not init server from xml file", e);
    }
    initServer(qsConfig);
}
