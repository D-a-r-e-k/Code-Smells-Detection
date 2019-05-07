/**
     *  Sets the repository attribute of the HttpSPMFileSystem object
     *
     *@param  rep  The new repository value
     */
public void setRepository(URL rep) {
    pluginsInfo = new Vector();
    toolInfo = new Vector();
    objectInfo = new Vector();
    startupInfo = new Vector();
    initialized = false;
    unknownHost = false;
    repository = rep;
    pluginsDoc = null;
    objectsDoc = null;
    startupDoc = null;
    toolsDoc = null;
}
