/**
     * Ant-compatible method for creating formatters.  Either
     * the type or the classname must be specified in the build
     * file or on the command line.
     *
     * @todo Modify Textui to accept 'types' on the command line.
     * @todo Make sure extension corresponds to type.
     * 
     * @return Formatter as an object
     */
public Formatter createFormatter() throws BuildException {
    // classname muat have been set either by setClassname or setType 
    if (classname == null) {
        throw new BuildException("missing formatter class name");
    }
    // particularly paranoid way of determining whether class is 
    // suitable.  First, is it actually a class? 
    Class fmtClass = null;
    try {
        fmtClass = Class.forName(classname);
    } catch (ClassNotFoundException e) {
        throw new BuildException(e);
    }
    // Get an instance of the class.  Is it a Formatter? 
    Formatter fmt = null;
    try {
        fmt = (Formatter) fmtClass.newInstance();
    } catch (IllegalAccessException e) {
        throw new BuildException(e);
    } catch (InstantiationException e) {
        throw new BuildException(e);
    } catch (ClassCastException e) {
        throw new BuildException(classname + " is not a Formatter");
    }
    if (useFile && outFile != null) {
        // we are supposed to write to a file  
        try {
            // open it 
            out = new FileOutputStream(outFile);
        } catch (java.io.IOException e) {
            throw new BuildException(e);
        }
    }
    // set the formatter to write to the output file (defaults to  
    // System.out) 
    fmt.setOutput(out);
    return fmt;
}
