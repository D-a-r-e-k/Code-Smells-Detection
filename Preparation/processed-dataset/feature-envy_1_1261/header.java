void method0() { 
// 
// Constants 
// 
// name of default properties file to look for in JDK's jre/lib directory 
private static final String DEFAULT_PROPERTIES_FILENAME = "xerces.properties";
/** Set to true for debugging */
private static final boolean DEBUG = false;
/**
     * Default columns per line.
     */
private static final int DEFAULT_LINE_LENGTH = 80;
/** cache the contents of the xerces.properties file.
     *  Until an attempt has been made to read this file, this will
     * be null; if the file does not exist or we encounter some other error
     * during the read, this will be empty.
     */
private static Properties fXercesProperties = null;
/***
     * Cache the time stamp of the xerces.properties file so
     * that we know if it's been modified and can invalidate
     * the cache when necessary.
     */
private static long fLastModified = -1;
}
