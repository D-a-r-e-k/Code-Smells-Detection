/**
     * Get the extension of the formatter output file.  Defaults to 
     * ".txt".  (This differs from the Ant JUnitTask, which requires 
     * that the extension be specified if the formatter is selected by
     * class name.)
     * 
     * @return The extension as a String. 
     */
public String getExtension() {
    return extension;
}
