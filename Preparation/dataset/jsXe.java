/*
jsXe.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

jsXe is the Java Simple XML Editor
jsXe is a gui application that creates a tree view of an XML document.
The user can then edit this tree and the content in the tree.

Copyright (C) 2002 Ian Lewis (IanLewis@member.fsf.org)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
Optionally, you may find a copy of the GNU General Public License
from http://www.fsf.org/copyleft/gpl.txt
*/

package net.sourceforge.jsxe;

//{{{ imports
/*
All classes are listed explicitly so
it is easy to see which package it
belongs to.
*/

//{{{ jsXe classes
import net.sourceforge.jsxe.gui.*;
import net.sourceforge.jsxe.util.Log;
import net.sourceforge.jsxe.util.MiscUtilities;
import net.sourceforge.jsxe.action.ActivityLogAction;
//}}}

//{{{ Swing classes
import javax.swing.*;
//}}}

//{{{ AWT Components
import java.awt.*;
import java.awt.event.ActionEvent;
//}}}

//{{{ DOM Classes
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.ParserConfigurationException;
//}}}

//{{{ Java Base classes
import java.io.*;
import java.net.URL;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
//}}}

//}}}

/**
 * The main class of the java simple XML editor (jsXe)
 * @author Ian Lewis (<a href="mailto:IanLewis@member.fsf.org">IanLewis@member.fsf.org</a>)
 * @version $Id: jsXe.java,v 1.128 2006/04/08 16:30:48 ian_lewis Exp $
 */
public class jsXe {
    
    //{{{ static instance variables
    public static final String MIN_JAVA_VERSION = "1.4.2";
    public static final String MIN_XERCES_VERSION = "Xerces-J 2.7.0";
    //}}}
    
    //{{{ The main() method of jsXe
    
    /**
     * The main method of jsXe
     * @param args The command line arguments
     */
    public static void main(String args[]) {
        
        try {
            long startTime = System.currentTimeMillis();
            
            //{{{ Check the java version
            String javaVersion = System.getProperty("java.version");
            if(javaVersion.compareTo(MIN_JAVA_VERSION) < 0)
            {
                System.err.println(getAppTitle() + ": ERROR: You are running Java version " + javaVersion + ".");
                System.err.println(getAppTitle() + ": ERROR:" + getAppTitle()+" requires Java "+MIN_JAVA_VERSION+" or later.");
                System.exit(1);
            }//}}}
            
            //{{{ set settings dirs
            m_homeDirectory = System.getProperty("user.home");
            String fileSep = System.getProperty("file.separator");
            
            m_settingsDirectory = m_homeDirectory+fileSep+".jsxe";
            
            File _settingsDirectory = new File(m_settingsDirectory);
            if(!_settingsDirectory.exists())
                _settingsDirectory.mkdirs();
            String pluginsDirectory = m_settingsDirectory+"/jars";
            File _pluginsDirectory = new File(pluginsDirectory);
            if(!_pluginsDirectory.exists())
                _pluginsDirectory.mkdirs();
            
            String jsXeHome = System.getProperty("jsxe.home");
            if (jsXeHome == null) {
                String classpath = System.getProperty("java.class.path");
                int index = classpath.toLowerCase().indexOf("jsxe.jar");
                int start = classpath.lastIndexOf(File.pathSeparator,index) + 1;
                // if started with java -jar jsxe.jar
                if (classpath.equalsIgnoreCase("jsxe.jar")) {
                    jsXeHome = System.getProperty("user.dir");
                } else {
                    if(index > start) {
                        jsXeHome = classpath.substring(start, index - 1);
                    } else {
                        // use user.dir as last resort
                        jsXeHome = System.getProperty("user.dir");
                    }
                }
            }
            
            //}}}
            
            //{{{ start locale
            Messages.initializePropertiesObject(null, jsXeHome+fileSep+"messages");
            //}}}
            
            //{{{ get and load the configuration files
            initDefaultProps();
            //}}}
            
            //{{{ parse command line arguments
            String viewname = null;
            ArrayList files = new ArrayList();
            boolean debug = false;
            for (int i=0; i<args.length; i++) {
                if (args[i].equals("--help") || args[i].equals("-h")) {
                    printUsage();
                    System.exit(0);
                }
                if (args[i].equals("--version") || args[i].equals("-V")) {
                    System.out.println(getVersion());
                    System.exit(0);
                }
                
                if (args[i].equals("--debug")) {
                    debug = true;
                } else {
                    files.add(args[i]);
                }
                
               // if (args[i].startsWith("--view") || args[i].startsWith("-v")) {
               //     if (i+1<args.length) {
               //         viewname = args[++i];
               //     } else {
               //         System.out.println(getAppTitle()+": No view name specified.");
               //         System.exit(1);
               //     }
               // } else {
               //     files.add(args[i]);
               // }
            }
            //}}}
            
            //{{{ start splash screen
            ProgressSplashScreenWindow progressScreen = new ProgressSplashScreenWindow();
            int w = progressScreen.getSize().width;
            int h = progressScreen.getSize().height;
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (dim.width-w)/2;
            int y = (dim.height-h)/2;
            progressScreen.setLocation(x, y);
            progressScreen.setVisible(true);
            //}}}
            
            //{{{ start logging
            Log.init(true, Log.ERROR, debug);
            try {
                BufferedWriter stream = new BufferedWriter(new FileWriter(getSettingsDirectory()+fileSep+"jsXe.log"));
                stream.flush();
                stream.write("Log file created on " + new Date());
                stream.write(System.getProperty("line.separator"));
                
                Log.setLogWriter(stream);
              
            } catch (IOException ioe) {
                Log.log(Log.ERROR, jsXe.class, ioe);
            }
            progressScreen.updateSplashScreenDialog(10);
            //}}}
            
            //{{{ check Xerces version
            String xercesVersion = org.apache.xerces.impl.Version.getVersion();
            if (MiscUtilities.compareStrings(xercesVersion, MIN_XERCES_VERSION, false) < 0) {
                String msg = Messages.getMessage("No.Xerces.Error", new String[] { MIN_XERCES_VERSION });
                Log.log(Log.ERROR, jsXe.class, msg);
                JOptionPane.showMessageDialog(null, msg, Messages.getMessage("No.Xerces.Error.Title", new String[] { MIN_XERCES_VERSION }), JOptionPane.WARNING_MESSAGE);
                System.exit(1);
            }
            progressScreen.updateSplashScreenDialog(20);
            //}}}
            
            //{{{ Load the recent files list
            File recentFiles = new File(getSettingsDirectory(), "recent.xml");
            m_bufferHistory = new BufferHistory();
            try {
                m_bufferHistory.load(recentFiles);
            } catch (IOException ioe) {
                System.err.println(getAppTitle() + ": I/O ERROR: Could not open recent files list");
                System.err.println(getAppTitle() + ": I/O ERROR: "+ioe.toString());
            } catch (SAXException saxe) {
                System.err.println(getAppTitle() + ": I/O ERROR: recent.xml not formatted properly");
                System.err.println(getAppTitle() + ": I/O ERROR: "+saxe.toString());
            } catch (ParserConfigurationException pce) {
                System.err.println(getAppTitle() + ": I/O ERROR: Could not parse recent.xml");
                System.err.println(getAppTitle() + ": I/O ERROR: "+pce.toString());
            }
            progressScreen.updateSplashScreenDialog(30);
            //}}}
            
            //{{{ load plugins
            Log.log(Log.NOTICE, jsXe.class, "Loading plugins");
            m_pluginLoader = new JARClassLoader();
            Log.log(Log.NOTICE, jsXe.class, "Adding to plugin search path: "+pluginsDirectory);
            ArrayList pluginMessages = m_pluginLoader.addDirectory(pluginsDirectory);
            
            //add the jsXe home to the plugins directory
            Log.log(Log.NOTICE, jsXe.class, "Adding to plugin search path: "+jsXeHome+fileSep+"jars");
            pluginMessages.addAll(m_pluginLoader.addDirectory(jsXeHome+fileSep+"jars"));
            progressScreen.updateSplashScreenDialog(40);
            //}}}

            //{{{ start plugins
            
            Log.log(Log.NOTICE, jsXe.class, "Starting plugins");
            pluginMessages.addAll(m_pluginLoader.startPlugins());
            Vector pluginErrors = new Vector();
            if (pluginMessages.size() != 0) {
                for (int i=0; i<pluginMessages.size(); i++) {
                    Object error = pluginMessages.get(i);
                    if ((error instanceof IOException) || (error instanceof PluginDependencyException)) {
                        Log.log(Log.ERROR, jsXe.class, ((Exception)error).getMessage());
                        pluginErrors.add(((Exception)error).getMessage());
                    } else {
                        if (error instanceof PluginLoadException) {
                            Log.log(Log.WARNING, jsXe.class, ((PluginLoadException)error).getMessage());
                        } else {
                            Log.log(Log.WARNING, jsXe.class, error.toString());
                        }
                    }
                }
            }
            progressScreen.updateSplashScreenDialog(50);
            
            Iterator pluginItr = m_pluginLoader.getAllPlugins().iterator();
            while (pluginItr.hasNext()) {
                
                //load properties into jsXe's properties
                ActionPlugin plugin = (ActionPlugin)pluginItr.next();
                Properties props = plugin.getProperties();
                Enumeration names = props.propertyNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement().toString();
                    setProperty(name, props.getProperty(name));
                }
                
                addActionSet(plugin.getActionSet());
            }
            progressScreen.updateSplashScreenDialog(60);
            //}}}
            
            //{{{ load user specific properties
            File properties = new File(getSettingsDirectory(),"properties");
            try {
                FileInputStream filestream = new FileInputStream(properties);
                props.load(filestream);
            } catch (FileNotFoundException fnfe) {                
                //Don't do anything right now
                Log.log(Log.MESSAGE, jsXe.class, "User has no properties file. Running jsXe for the first time?");
            } catch (IOException ioe) {
                System.err.println(getAppTitle() + ": I/O ERROR: Could not open settings file");
                System.err.println(getAppTitle() + ": I/O ERROR: "+ioe.toString());
            }
            
            //init the catalog manager
            CatalogManager.propertiesChanged();
            
            progressScreen.updateSplashScreenDialog(70);
            //}}}
            
            //{{{ create the TabbedView
            Log.log(Log.NOTICE, jsXe.class, "Starting the main window");
            TabbedView tabbedview = null;
            DocumentBuffer defaultBuffer = null;
            try {
                defaultBuffer = new DocumentBuffer();
                m_buffers.add(defaultBuffer);
                if (viewname == null) {
                    tabbedview = new TabbedView(defaultBuffer);
                } else {
                    try {
                        tabbedview = new TabbedView(defaultBuffer, viewname);
                    } catch (UnrecognizedPluginException e) {
                        Log.log(Log.ERROR, jsXe.class, e.getMessage());
                        System.exit(1);
                    }
                }
               
            } catch (IOException ioe) {
                Log.log(Log.ERROR, jsXe.class, ioe);
                JOptionPane.showMessageDialog(null, ioe.getMessage()+".", Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
                System.exit(1);
            }
            m_activeView = tabbedview;
            progressScreen.updateSplashScreenDialog(85);
            //}}}
            
            //{{{ Parse files to open on the command line
            Log.log(Log.NOTICE, jsXe.class, "Parsing files to open on command line");
            if (files.size() > 0) {
                if (openXMLDocuments(tabbedview, (String[])files.toArray(new String[] {}))) {
                    try {
                        closeDocumentBuffer(tabbedview, defaultBuffer);
                    } catch (IOException ioe) {
                        //it can't be saved. it's not dirty.
                    }             
                }
            }
            progressScreen.updateSplashScreenDialog(100);
            //}}}
            
            tabbedview.setVisible(true);
            
            progressScreen.dispose();
            
            //Show plugin error dialog
            if (pluginErrors.size() > 0) {
                new ErrorListDialog(tabbedview, "Plugin Error", "The following plugins could not be loaded:", new Vector(pluginErrors), true);
            }
            
            Log.log(Log.NOTICE, jsXe.class, "jsXe started in "+(System.currentTimeMillis()-startTime)+" milliseconds");
        } catch (Throwable e) {
            exiterror(null, e, 1);
        }
    }//}}}
    
    //{{{ getBuild()
    /**
     * Gets the internal build version for jsXe. An example is 00.03.15.00
     * @return a string of the form Major.Minor.Beta.Build
     * @since jsXe 0.3pre15
     */
    public static String getBuild() {
        // Major.Minor.Beta.Build
        String major  = buildProps.getProperty("major.version");
        String minor  = buildProps.getProperty("minor.version");
        String beta   = buildProps.getProperty("beta.version");
        String bugfix = buildProps.getProperty("build.version");
        
        if (major.length() == 1) {
            major = "0"+major;
        }
        if (minor.length() == 1) {
            minor = "0"+minor;
        }
        if (beta.length() == 1) {
            beta = "0"+beta;
        }
        if (bugfix.length() == 1) {
            bugfix = "0"+bugfix;
        }
        
        return major+"."+
               minor+"."+
               beta+"."+
               bugfix;
    }//}}}
    
    //{{{ getBuildDate()
    /**
     * Gets the date that jsXe was built as a string.
     * @return a date object for when jsXe was built.
     * @since jsXe 0.4 pre3
     */
    public static Date getBuildDate() {
        String buildTime = buildProps.getProperty("build.time");
        //The build date in the build.properties is always US locale.
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss z");
            return format.parse(buildTime);
        } catch (ParseException e) {
            Log.log(Log.ERROR, jsXe.class, e);
        }
        return null;
    }//}}}
    
    //{{{ getVersion()
    /**
     * Gets the formatted, human readable version of jsXe.
     * @return The current version of jsXe.
     */
    public static String getVersion() {
        return MiscUtilities.buildToVersion(getBuild());
    }//}}}
    
    //{{{ getIcon()
    /**
     * Gets jsXe's icon that is displayed in the about menu,
     * taskbar and upper left hand corner (where appropriate)
     * @return jsXe's icon
     */
    public static ImageIcon getIcon() {
        return jsXeIcon;
    }//}}}
    
    //{{{ getSettingsDirectory() method
    /**
     * Returns the path of the directory where user-specific settings
     * are stored.
     */
    public static String getSettingsDirectory() {
        return m_settingsDirectory;
    } //}}}
    
    //{{{ getAppTitle()
    /**
     * Gets the title of the jsXe application. Most likely "jsXe"
     * @return The title of the jsXe application.
     */
    public static String getAppTitle() {
        return buildProps.getProperty("application.name");
    }//}}}
    
    //{{{ getActiveView()
    /**
     * Gets the currently active view.
     * @return the currently active view.
     * @since jsXe 0.4 pre1
     */
    public static TabbedView getActiveView() {
        /*
        there is only one view per instance of jsXe currently.
        Eventually there may be more.
        */
        return m_activeView;
    }//}}}
    
    //{{{ showOpenFileDialog()
    /**
     * Shows an open file dialog for jsXe. When a file is selected jsXe attempts
     * to open it.
     * @param view The view that is to be the parent of the file dialog
     * @return true if the file is selected and opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean showOpenFileDialog(TabbedView view) throws IOException {
            DocumentBuffer buffer = view.getDocumentBuffer();
            File docFile = buffer.getFile();
            JFileChooser loadDialog;
            if (docFile == null) {
                loadDialog = new jsxeFileDialog(m_homeDirectory);
            } else {
                loadDialog = new jsxeFileDialog(docFile);
            }
            loadDialog.setMultiSelectionEnabled(true);
            
            int returnVal = loadDialog.showOpenDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                boolean success = false;
                File[] files = loadDialog.getSelectedFiles();
                for (int i = 0; i < files.length; i++) {
                    //success becomes true if at least one document is opened
                    //successfully.
                    if (files[i] != null) {
                        try {
                            success = openXMLDocument(view, files[i]) || success;
                        } catch (IOException ioe) {
                            //I/O error doesn't change value of success
                            Log.log(Log.WARNING, jsXe.class, ioe);
                            JOptionPane.showMessageDialog(view, ioe, Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
                return success;
            }
            return false;
    }//}}}
    
    //{{{ openXMLDocument()
    /**
     * Attempts to open an XML document in jsXe from a file on disk. If the file
     * is already open then the view's focus is set to that document.
     * @param view The view to open the document in.
     * @param file The file to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(TabbedView view, File file) throws IOException {
        /*
        Check if it's in the recent file history
        if so then use those properties
        */
        BufferHistory.BufferHistoryEntry entry = m_bufferHistory.getEntry(file.getPath());
        if (entry != null) {
            return openXMLDocument(view, file, entry.getProperties(), entry.getViewName());
        } else {
            return openXMLDocument(view, file, new Properties(), null);
        }
    }//}}}
    
    //{{{ openXMLDocument()
    /**
     * Attempts to open an XML document in jsXe from a file on disk. If the file
     * is already open then the view's focus is set to that document. The
     * properties and view name given are ignored if the document is already
     * open. If the view name is null then the document is opened in the default
     * DocumentView.
     *
     * @param view The view to open the document in.
     * @param file The file to open.
     * @param properties the properties to set to the new document
     * @param viewName the name of the view to open this document in
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(TabbedView view, File file, Properties properties, String viewName) throws IOException {
        
        if (file == null)
            return false;
        
        DocumentBuffer buffer = getOpenBuffer(file);
        if (buffer != null) {
            /*
            ignore properties since the file is already open and the user may have
            set propeties him/herself. We don't want to change what the user
            has set.
            */
            view.setDocumentBuffer(buffer);
            return true;
        } else {
            Log.log(Log.NOTICE, jsXe.class, "Loading file "+file.getName());
            try {
                buffer = new DocumentBuffer(file, properties);
                m_buffers.add(buffer);
                if (viewName != null) {
                    try {
                        view.addDocumentBuffer(buffer, viewName);
                    } catch (IOException ioe) {
                        /*
                        we can't open in the view we want try to open in
                        all views
                        */
                        view.addDocumentBuffer(buffer);
                    } catch (UnrecognizedPluginException e) {
                        /*
                        we can't open in the view we want try to open in
                        all views
                        */
                        view.addDocumentBuffer(buffer);
                    }
                } else {
                    view.addDocumentBuffer(buffer);
                }
                /*
                if there was only one untitled, clean buffer open then go
                ahead and close it so it doesn't clutter up the user's
                workspace.
                */
                DocumentBuffer[] buffers = getDocumentBuffers();
                if (buffers.length == 2 && buffers[0].isUntitled() && !buffers[0].getStatus(DocumentBuffer.DIRTY)) {
                    closeDocumentBuffer(view, buffers[0]);
                }
                return true;
            } catch (IOException ioe) {
                m_buffers.remove(buffer);
                throw ioe;
            }
        }
    }//}}}
    
    //{{{ openXMLDocument()
    /**
     * Attempts to open an XML document in the form of a String object as an
     * untitled document.
     * @param view The view to open the document in.
     * @param doc The String document to open.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(TabbedView view, String doc) throws IOException {
        return openXMLDocument(view, new ByteArrayInputStream(doc.getBytes("UTF-8")));
    }//}}}
    
    //{{{ openXMLDocument()
    /**
     * Attempts to open an XML document in the form of a Reader object as an
     * untitled document.
     * @param view The view to open the document in.
     * @param stream The stream to the document.
     * @return true if the file is opened successfully.
     * @throws IOException if the document does not validate or cannot be opened for some reason.
     */
    public static boolean openXMLDocument(TabbedView view, InputStream stream) throws IOException {
        Log.log(Log.NOTICE, jsXe.class, "Loading Untitled Document");
        DocumentBuffer buffer = new DocumentBuffer(stream);
        try {
            m_buffers.add(buffer);
            view.addDocumentBuffer(buffer);
            return true;
        } catch (IOException ioe) {
            //recover by removing the document
            m_buffers.remove(buffer);
            throw ioe;
        }
    }//}}}
    
    //{{{ getOpenBuffer()
    /**
     * Gets the DocumentBuffer for this file if the file is open already. Returns
     * null if the file is not open.
     * @param file The file that is open in jsXe
     * @return the DocumentBuffer for the given file or null if the file not open.
     */
    public static DocumentBuffer getOpenBuffer(File file) {
        
        boolean caseInsensitiveFilesystem = (File.separatorChar == '\\'
            || File.separatorChar == ':' /* Windows or MacOS */);
        
        for(int i=0; i < m_buffers.size();i++) {
            
            try {
                DocumentBuffer buffer = (DocumentBuffer)m_buffers.get(i);
                if (buffer.equalsOnDisk(file)) {
                    return buffer;
                }
            } catch (IOException ioe) {
                exiterror(null, ioe, 1);
            }
            
        }
        
        return null;
    }//}}}
    
    //{{{ closeDocumentBuffer()
    /**
     * Closes an open DocumentBuffer. If dirty then the user will be prompted to save.
     * @param view The view that contains the buffer.
     * @param buffer The buffer to close.
     * @return true if the buffer was closed successfully. May return false if
     *         user hits cancel when asked to save changes.
     * @throws IOException if the user chooses to save and the file cannot be saved
     *                     because of an I/O error.
     */
    public static boolean closeDocumentBuffer(TabbedView view, DocumentBuffer buffer) throws IOException {
        return closeDocumentBuffer(view, buffer, true);
    }//}}}
    
    //{{{ closeDocumentBuffer()
    /**
     * Overloaded version of closeDocumentBuffer() method. Checks to see if confirmClose is 
     * is true. If confirmClose is set to true and the file is dirty then the user 
     * will be prompted to confirm that they want to save the file and then they will be 
     * provided with a JFileChooser. If a file is dirty and confirmClose is set to false,
     * the user will be sent directly to the JFileChooser, they won't be asked first
     * if they want to save the file or not.
     * @param view The view that contains the buffer.
     * @param buffer The buffer to close.
     * @param confirmClose Whether or not user should be asked to confirm that they want to save file,
     *        before being sent to JFileChooser.
     * @return true if the buffer was closed successfully. May return false if
     *         user hits cancel when asked to save changes.
     * @throws IOException if the user chooses to save and the file cannot be saved
     *                     because of an I/O error.
     */
    public static boolean closeDocumentBuffer(TabbedView view, DocumentBuffer buffer, boolean confirmClose) throws IOException {
        if (m_buffers.contains(buffer)) {
            if (buffer.close(view, confirmClose)) {
                Log.log(Log.NOTICE, jsXe.class, "Closing "+buffer.getName());
                m_bufferHistory.setEntry(buffer, getPluginLoader().getPluginProperty(view.getDocumentView().getViewPlugin(), JARClassLoader.PLUGIN_NAME));
                view.removeDocumentBuffer(buffer);
                m_buffers.remove(buffer);
                
                if (view.getBufferCount() == 0) {
                    if (!m_exiting) {
                        try {
                            openXMLDocument(view, getDefaultDocument());
                        } catch (IOException ioe) {
                            exiterror(view, "Could not open default document.", 1);
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }//}}}
    
    //{{{ closeAllDocumentBuffers()
    /**
     * Closes all open DocumentBuffers. If there are any dirty files then the
     * user will be prompted to save or close them in a single dialog.
     * @param view the view that initiated the close
     * @throws IOException if the user chooses to save and the file cannot
     *                     be saved because of an I/O error
     */
    public static boolean closeAllDocumentBuffers(TabbedView view) throws IOException {
        DocumentBuffer[] buffers = jsXe.getDocumentBuffers();
        ArrayList dirtyBufferList = new ArrayList();
        
        //sequentially close all the document views
        for (int i=0; i < buffers.length; i++) {
            DocumentBuffer db= buffers[i];
            if (db.getStatus(DocumentBuffer.DIRTY)) {
                dirtyBufferList.add(db);
            }
        }
        
        boolean closeFiles = true;
        //produce Dialog box with the list of currently opened files in it
        if (dirtyBufferList.size() > 0) {
            if (dirtyBufferList.size() > 1) {
                
                /*
                If there are multiple dirty files then show
                the dirty files dialog
                */
                DirtyFilesDialog dirtyDialog = new DirtyFilesDialog(view, dirtyBufferList);
                dirtyDialog.setSize(200, 400);
                dirtyDialog.setResizable(true);
                closeFiles = !dirtyDialog.getCancelFlag();
                
            } else {
                /*
                if there is only one file then close the one file normally
                then close the rest of the clean buffers
                */
                closeFiles = closeDocumentBuffer(view, (DocumentBuffer)dirtyBufferList.get(0), true);
            }
        }
        
        if (closeFiles) {
            //get the buffers that are still open and close them.
            buffers = jsXe.getDocumentBuffers();
            for (int i=0; i < buffers.length; i++) {
                if (!closeDocumentBuffer(view, buffers[i], false)) {
                    return false;
                }
            }
        }
        return closeFiles;
    }//}}}
    
    //{{{ getBufferHistory()
    
    public static BufferHistory getBufferHistory() {
        return m_bufferHistory;
    }//}}}
    
    //{{{ getDefaultDocument()
    /**
     * Gets the default XML document in jsXe. This is necessary 
     * as XML documents cannot be blank files.
     * @return jsXe's default XML document.
     */
    public static InputStream getDefaultDocument() {
        try {
            return new ByteArrayInputStream(DefaultDocument.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.log(Log.ERROR, jsXe.class, "Broken JVM doesn't support UTF-8");
            Log.log(Log.ERROR, jsXe.class, e);
            return null;
        }
    }//}}}
    
    //{{{ getDocumentBuffers()
    /**
     * Gets an array of the open Buffers.
     * @return An array of DocumentBuffers that jsXe currently has open.
     */
    public static DocumentBuffer[] getDocumentBuffers() {
        DocumentBuffer[] buffers = new DocumentBuffer[m_buffers.size()];
        for (int i=0; i < m_buffers.size(); i++) {
            buffers[i] = (DocumentBuffer)m_buffers.get(i);
        }
        return buffers;
    }//}}}
    
   // //{{{ getBufferForDocument()
   // /**
   //  * Gets the DocumentBuffer for the document.
   //  * @param doc the XMLDocument
   //  * @return the DocumentBuffer for the XMLDocument or null if there is none registered
   //  */
   // public static DocumentBuffer getBufferForDocument(XMLDocument doc) {
   //     Iterator bufferItr = m_buffers.iterator();
   //     while (bufferItr.hasNext()) {
   //         DocumentBuffer buf = (DocumentBuffer)bufferItr.next();
   //         if (buf.getXMLDocument() == doc) {
   //             return buf;
   //         }
   //     }
   //     return null;
   // }//}}}
    
    //{{{ exit()
    /**
     * Called when exiting jsXe.
     * @param view The view from which the exit was called.
     */
    public static void exit(TabbedView view) {
        m_exiting = true;
        
        try {//saves properties
            //exit only if the view really wants to.
            if (view.close()) {
                Log.log(Log.NOTICE, jsXe.class, "Exiting");
                
                //Save the Catalog info
                CatalogManager.save();
                
                String settingsDirectory = getSettingsDirectory();
                
                try {
                    File properties = new File(settingsDirectory,"properties");
                    FileOutputStream filestream = new FileOutputStream(properties);
                    props.store(filestream, "Autogenerated jsXe properties"+System.getProperty("line.separator")+"#This file is not really meant to be edited.");
                } catch (IOException ioe) {
                    exiterror(view, "Could not save jsXe properites.\n"+ioe.toString(), 1);
                } catch (ClassCastException cce) {
                    exiterror(view, "Could not save jsXe properties.\n"+cce.toString(), 1);
                }
                
                try {
                    File recentFiles = new File(settingsDirectory, "recent.xml");
                    m_bufferHistory.save(recentFiles);
                } catch (IOException ioe) {
                    exiterror(view, "Could not save jsXe recent files list.\n"+ioe.toString(), 1);
                }
                
                //stop logging.
                Log.closeStream();
                
                System.exit(0);
            } else {
                m_exiting = false;
            }
        } catch (IOException ioe) {
            //failed save of a dirty buffer
            Log.log(Log.ERROR, jsXe.class, ioe);
            JOptionPane.showMessageDialog(view, ioe, Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
            m_exiting = false;
        }
    }//}}}
    
    //{{{ exiterror()
    /**
     * Called when crashing jsXe. jsXe prints an error message and
     * exits with the error code specifed.
     * @param view The view from which the exit was called.
     * @param error The error. Either a string or Exception.
     * @param errorcode The errorcode to exit with.
     */
    public static void exiterror(Object source, Object error, int errorcode) {
        String errorhdr = "jsXe has encountered a fatal error and is unable to continue.\n";
        errorhdr        +="This is most likely a bug and should be reported to the jsXe\n";
        errorhdr        +="developers. Please include your jsXe.log in a bug report at\n";
        errorhdr        +="http://www.sourceforge.net/projects/jsxe/\n\n";
        
        Log.log(Log.ERROR, source, errorhdr);
        Log.log(Log.ERROR, source, error);
        
        if (source != null && source instanceof Component) {
            JOptionPane.showMessageDialog((Component)source, errorhdr + error, "Fatal Error", JOptionPane.WARNING_MESSAGE);
        }
        
        //stop logging
        Log.closeStream();
        
        System.exit(errorcode);
    }//}}}
    
    //{{{ setProperty()
    /**
     * Sets a global property to jsXe.
     * @param key The key name for the property.
     * @param value The value to associate with the key.
     * @return The previous value for the key, or null if there was none.
     */
    public static Object setProperty(String key, String value) {
        if (value == null) {
            props.remove(key);
            return null;
        } else {
            return props.setProperty(key, value);
        }
    }//}}}
    
    //{{{ getDefaultProperty()
    /**
     * Gets a default global property. Returns null if there is no default
     * property for the given key.
     * 
     */
    public static final String getDefaultProperty(String key) {
        return defaultProps.getProperty(key);
    }
    
    //}}}
    
    //{{{ getProperty()
    /**
     * Gets a jsXe global property. This returns a user defined value for the
     * property, the default value of the property if no user defined value is
     * found or null if neither exist.
     * @param key The key of the property to get.
     * @return The value associated with the key or null if the key is not found.
     */
    public static final String getProperty(String key) {
        return getProperty(key, null);
    }//}}}
    
    //{{{ getProperty()
    /**
     * Gets a jsXe global property. This returns a user defined value for the
     * property, the default value of the property if no user defined value is
     * found or the default value given if neither exist.
     * @param key The key of the property to get.
     * @param defaultValue The default value to return when the key is not found.
     * @return The value associated with the key or the default value if the key is not found.
     */
    public static final String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultProps.getProperty(key, defaultValue));
    } //}}}
    
    //{{{ getIntegerProperty()
    /**
     * Returns the value of an integer property.
     * @param name The property
     * @param def The default value
     * @since jsXe 0.2 pre24
     */
    public static final int getIntegerProperty(String key, int defaultValue) {
        int intValue = defaultValue;
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value.trim());
            } catch(NumberFormatException nf) {
                return defaultValue;
            }
        }
    }//}}}
    
    //{{{ setIntegerProperty() method
    /**
     * Sets the value of an integer property.
     * @param name The property
     * @param value The value
     * @since jsXe 0.2 pre24
     */
    public static final void setIntegerProperty(String name, int value) {
        setProperty(name, String.valueOf(value));
    } //}}}
    
    //{{{ getBooleanProperty() method
    /**
     * Gets the value of an boolean property.
     * @param name The property
     * @param defaultValue The default value of the property
     * @since jsXe 0.4 pre3
     */
    public static final boolean getBooleanProperty(String name, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        String value = getProperty(name);
        if (value == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(value).booleanValue();
        }
    }//}}}
    
    //{{{ setBooleanProperty()
    /**
     * Sets the value of an boolean property.
     * @param name The property
     * @param value The value
     * @since jsXe 0.4 pre3
     */
    public static final void setBooleanProperty(String name, boolean value) {
        setProperty(name, String.valueOf(value));
    }//}}}
    
    //{{{ addActionSet()
    
    public static void addActionSet(ActionSet set) {
        m_actionSets.add(set);
    }//}}}
    
    //{{{ getAction()
    /**
     * Gets the action set with the given name
     */
    public static Action getAction(String name) {
        for (int i = 0; i < m_actionSets.size(); i++) {
            Action action = ((ActionSet)m_actionSets.get(i)).getAction(name);
            if (action != null) {
                return action;
            }
        }
        return null;
    }//}}}
    
    //{{{ getActionSets()
    /**
     * Gets all action sets that have been registered with jsXe
     * @return an ArrayList of ActionSet objects
     */
    public ArrayList getActionSets() {
        return m_actionSets;
    }//}}}
    
    //{{{ getOptionsPanel()
    /**
     * Gets the options panel for the jsXe application.
     * @return The OptionsPanel with the options for jsXe.
     */
    public static final OptionsPanel getOptionsPanel() {
        jsXeOptions = new jsXeOptionsPanel();
        return jsXeOptions;
    }//}}}
    
    //{{{ getPluginLoader()
    /**
     * Gets the plugin loader that is used to load
     * plugins into jsXe
     */
    public static JARClassLoader getPluginLoader() {
        return m_pluginLoader;
    }//}}}
    
    //{{{ isExiting()
    /**
     * Indicates whether jsXe is exiting i.e. in the exit method.
     * @return true if jsXe is exiting.
     */
    public static final boolean isExiting() {
        return m_exiting;
    }//}}}
    
    //{{{ jsXe constructor
    /**
     * This class cannot be instantiated.
     */
    private jsXe() {}
    //}}}
    
    // {{{ Private static members

    //{{{ openXMLDocuments()
    /**
     * Open the XML documents in the command line arguments.
     */
    private static boolean openXMLDocuments(TabbedView view, String args[]) {
    
        boolean success = false;
        for (int i = 0; i < args.length; i++) {
            //success becomes true if at least one document is opened
            //successfully.
            if (args[i] != null) {
                try {
                    Log.log(Log.NOTICE, jsXe.class, "Trying to open "+args[i]);
                    success = openXMLDocument(view, new File(args[i])) || success;
                } catch (IOException ioe) {
                    //I/O error doesn't change value of success
                    Log.log(Log.WARNING, jsXe.class, ioe);
                    JOptionPane.showMessageDialog(view, ioe, Messages.getMessage("IO.Error.Title"), JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        return success;
    
    }//}}}
    
    //{{{ initDefaultProps()
    /**
     * Initialize the Default properties.
     */
    private static void initDefaultProps() {
        
        //{{{ Load jsXe default properties
        InputStream inputstream = jsXe.class.getResourceAsStream("/net/sourceforge/jsxe/properties");
        try {
            defaultProps.load(inputstream);
        } catch (IOException ioe) {
            Log.log(Log.ERROR, jsXe.class, "**** Could not open default settings file ****");
            Log.log(Log.ERROR, jsXe.class, "**** jsXe was probably not built correctly ****");
            exiterror(null, ioe, 1);
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int)(screenSize.getWidth() / 2);
        int windowHeight = (int)(3 * screenSize.getHeight() / 4);        
        int x = (int)(screenSize.getWidth() / 4);
        int y = (int)(screenSize.getHeight() / 8);
        
        defaultProps.setProperty("tabbedview.height",Integer.toString(windowHeight));
        defaultProps.setProperty("tabbedview.width",Integer.toString(windowWidth));
        
        defaultProps.setProperty("tabbedview.x",Integer.toString(x));
        defaultProps.setProperty("tabbedview.y",Integer.toString(y));
        //}}}
        
        //{{{ Load build properties
        inputstream = jsXe.class.getResourceAsStream("/net/sourceforge/jsxe/build.properties");
        try {
            buildProps.load(inputstream);
        } catch (IOException ioe) {
            Log.log(Log.ERROR, jsXe.class, "**** Could not open build properties file ****");
            Log.log(Log.ERROR, jsXe.class, "**** jsXe was probably not built correctly ****");
            exiterror(null, ioe, 1);
        }
        //}}}
        
    }//}}}
    
    //{{{ jsXeOptionsPanel class
    
    private static class jsXeOptionsPanel extends OptionsPanel {
        
        //{{{ jsXeOptionsPanel constructor
        
        public jsXeOptionsPanel() {
            
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints constraints = new GridBagConstraints();
            
            setLayout(layout);
            
            int gridY = 0;
            
            int maxRecentFiles = 20;
            try {
                maxRecentFiles = Integer.parseInt(jsXe.getProperty("max.recent.files"));
            } catch (NumberFormatException nfe) {
                try {
                    maxRecentFiles = Integer.parseInt(jsXe.getDefaultProperty("max.recent.files"));
                } catch (NumberFormatException nfe2) {
                    Log.log(Log.ERROR, jsXe.class, "Could not read max.recent.files property");
                }
            }
            
            JLabel maxRecentFilesLabel = new JLabel(Messages.getMessage("Global.Options.Max.Recent.Files"));
            maxRecentFilesLabel.setToolTipText(Messages.getMessage("Global.Options.Max.Recent.Files.ToolTip"));
            
            Vector sizes = new Vector(4);
            sizes.add("10");
            sizes.add("20");
            sizes.add("30");
            sizes.add("40");
            maxRecentFilesComboBox = new JComboBox(sizes);
            maxRecentFilesComboBox.setEditable(true);
            maxRecentFilesComboBox.setSelectedItem(Integer.toString(maxRecentFiles));
            
            maxRecentFilesComboBox.setToolTipText(Messages.getMessage("Global.Options.Max.Recent.Files.ToolTip"));
            
            JLabel networkLabel = new JLabel(Messages.getMessage("Global.Options.network"));
            
            String[] networkValues = {
                Messages.getMessage("Global.Options.network-always"),
                Messages.getMessage("Global.Options.network-cache"),
                Messages.getMessage("Global.Options.network-off")

            };
            
            network = new JComboBox(networkValues);
            network.setSelectedIndex(jsXe.getIntegerProperty("xml.cache", 1));
            
            constraints.gridy      = gridY;
            constraints.gridx      = 0;
            constraints.gridheight = 1;
            constraints.gridwidth  = 1;
            constraints.weightx    = 1.0f;
            constraints.fill       = GridBagConstraints.BOTH;
            constraints.insets     = new Insets(1,0,1,0);
            
            layout.setConstraints(maxRecentFilesLabel, constraints);
            add(maxRecentFilesLabel);
            
            constraints.gridy      = gridY++;
            constraints.gridx      = 1;
            constraints.gridheight = 1;
            constraints.gridwidth  = 1;
            constraints.weightx    = 1.0f;
            constraints.fill       = GridBagConstraints.BOTH;
            constraints.insets     = new Insets(1,0,1,0);
            
            layout.setConstraints(maxRecentFilesComboBox, constraints);
            add(maxRecentFilesComboBox);
            
            constraints.gridy      = gridY;
            constraints.gridx      = 0;
            constraints.gridheight = 1;
            constraints.gridwidth  = 1;
            constraints.weightx    = 1.0f;
            constraints.fill       = GridBagConstraints.BOTH;
            constraints.insets     = new Insets(1,0,1,0);
            
            layout.setConstraints(networkLabel, constraints);
            add(networkLabel);
            
            constraints.gridy      = gridY++;
            constraints.gridx      = 1;
            constraints.gridheight = 1;
            constraints.gridwidth  = 1;
            constraints.weightx    = 1.0f;
            constraints.fill       = GridBagConstraints.BOTH;
            constraints.insets     = new Insets(1,0,1,0);
            
            layout.setConstraints(network, constraints);
            add(network);
        }//}}}
        
        //{{{ getName()
        
        public String getName() {
            return "jsxeoptions";
        }//}}}
        
        //{{{ save()
        public void save() {
            try {
                //don't need to set dirty, no change to text
                jsXe.setProperty("max.recent.files", (new Integer(maxRecentFilesComboBox.getSelectedItem().toString())).toString());
            } catch (NumberFormatException nfe) {
                //Bad input, don't save.
            }
            jsXe.setIntegerProperty("xml.cache",network.getSelectedIndex());
            CatalogManager.propertiesChanged();
        }//}}}
        
        //{{{ getTitle()
        
        public String getTitle() {
            return Messages.getMessage("Global.Options.Title");
        }//}}}
        
        //{{{ toString()
        
        public String toString() {
            return getTitle();
        }//}}}
        
        //{{{ Private Members
        private JComboBox maxRecentFilesComboBox;
        private JComboBox network;
        //}}}
        
    }//}}}
    
    //{{{ printUsage()
    
    private static void printUsage() {
        System.out.println("jsXe "+getVersion());
        System.out.println("The Java Simple XML Editor");
        System.out.println();
        System.out.println("Copyright 2004 Ian Lewis");
        System.out.println("This is free software; see the source for copying conditions. There is NO");
        System.out.println("warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.");
        System.out.println();
        System.out.println("Usage: jsXe [<options>] [<files>]");
       // System.out.println("  -v, --view <viewname>     open the files using the view specified");
       // System.out.println("                            valid views are:");
       // System.out.println("                                            tree");
       // System.out.println("                                            source");
        System.out.println("      --debug               print debug information");
        System.out.println("  -h, --help                display this help and exit");
        System.out.println("  -V, --version             print version information and exit");
        System.out.println();
        System.out.println("Report bugs to <ian_lewis@users.sourceforge.net>");
    }//}}}
    
    private static ArrayList m_buffers = new ArrayList();
    private static final String DefaultDocument = "<?xml version='1.0' encoding='UTF-8'?>\n<default_element>default_node</default_element>";
    private static final ImageIcon jsXeIcon = new ImageIcon(jsXe.class.getResource("/net/sourceforge/jsxe/icons/jsxe.jpg"), "jsXe");
    
    private static final Properties buildProps = new Properties();
    private static boolean m_exiting=false;
    private static final Properties defaultProps = new Properties();
    private static Properties props = new Properties();
    private static BufferHistory m_bufferHistory;
    private static ArrayList m_actionSets = new ArrayList();
    private static JARClassLoader m_pluginLoader;
    private static TabbedView m_activeView;
    private static String m_settingsDirectory;
    private static String m_homeDirectory;
    
    private static OptionsPanel jsXeOptions;
    //}}}
    
}
