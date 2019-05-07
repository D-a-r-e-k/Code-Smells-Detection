/* FmtSelector.java */

package org.quilt.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import org.apache.tools.ant.BuildException;

public class FmtSelector {

    private String classname;
    private String extension = ".txt";
    private OutputStream out = System.out;
    private File outFile;
    private boolean useFile = true;

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
    // //////////////////////////////////////////////////////////////
    // SET TYPE 
    // //////////////////////////////////////////////////////////////
    /** File name extensions associated with formatters. */
    private static Hashtable extensions = null;
    /** Table of abbreviations for formatters. */
    private static Hashtable types = null;
    {
        types = new Hashtable();
        types.put ("brief",   "org.quilt.reports.BriefFormatter");
        types.put ("plain",   "org.quilt.reports.PlainFormatter");
        types.put ("summary", "org.quilt.reports.SummaryFormatter");
        types.put ("xml",     "org.quilt.reports.XMLFormatter");

        // could save work by just using default except when type is "xml"
        extensions = new Hashtable();
        extensions.put ("org.quilt.reports.BriefFormatter",   ".txt");
        extensions.put ("org.quilt.reports.PlainFormatter",   ".txt");
        extensions.put ("org.quilt.reports.SummaryFormatter", ".txt");
        extensions.put ("org.quilt.reports.XMLFormatter",     ".xml");
    }
    /** 
     * @return Whether the String passed is an alias for a standard formatter 
     */ 
    public boolean isKnownType (String t) {
        return types.containsKey(t);
    }
    /** 
     * Routine called by Ant to set the formatter using an alias.  This 
     * is an attribute of the formatter element.  Example
     * <pre>
     *
     *      &lt;formatter type="plain"&gt;
     *
     * </pre>
     * @param t 'Type' of the formatter (brief, plain, summary, xml)
     */
    public void setType (String t) {
        if (! types.containsKey(t) ) {
            throw new BuildException ("unknown formatter type " + t);
        }
        classname = (String) types.get(t);
        extension = (String) extensions.get(classname);
    }

    // //////////////////////////////////////////////////////////////
    // OTHER GET/SET METHODS 
    // //////////////////////////////////////////////////////////////
    /** @return the class name of the formatter */
    public String getClassname() {
        return classname;
    }
    /** Ant-compatible method for selecting formatter by class name. */
    public void setClassname(String classname) {
        this.classname = classname;
    }
  
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
    /** 
     * Ant-compatible method for setting the extension of the 
     * formatter output file.
     * 
     * @see #setOutfile
     */
    public void setExtension(String ext) {
        extension = ext;
    }
    /** 
     * Ant-compatible method for setting the base name of the output file. 
     * 
     * @see #setExtension
     */
    public void setOutfile(File out) {
        outFile = out;
    }
    public void setOutput(OutputStream out) {
        out = out;
    }
    
    /** @return Whether output to a file is enabled. */
    public boolean getUseFile() {
        return useFile;
    }
    /** 
     * Ant-compatible method for enabling output to a file. Defaults to 
     * true.
     */
    public void setUseFile(boolean b) {
        this.useFile = b;
    }
    /** 
     * Converts some class information to String format. 
     * @return The string.
     */
    public String toString() {
        return "Formatter: " + classname + ", extension: " + extension;
    }
}
