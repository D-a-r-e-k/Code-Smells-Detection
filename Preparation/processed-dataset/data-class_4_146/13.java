/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
/**
     * Process the xml file in the default location (a file named
     * "quartz_jobs.xml" in the current working directory).
     *  
     */
protected void processFile() throws Exception {
    processFile(QUARTZ_XML_DEFAULT_FILE_NAME);
}
