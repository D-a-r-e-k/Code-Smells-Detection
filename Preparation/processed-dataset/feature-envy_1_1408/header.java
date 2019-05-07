void method0() { 
/** <code>{@link SAXBuilder}</code> instance to use */
SAXBuilder builder;
/** the destination directory */
private File destDir = null;
/** the base directory */
File baseDir = null;
/** the style= attribute */
private String style = null;
/** last modified of the style sheet */
private long styleSheetLastModified = 0;
/** the projectFile= attribute */
private String projectAttribute = null;
/** the File for the project.xml file */
private File projectFile = null;
/** last modified of the project file if it exists */
private long projectFileLastModified = 0;
/** check the last modified date on files. defaults to true */
private boolean lastModifiedCheck = true;
/** the default output extension is .html */
private String extension = ".html";
/** the template path */
private String templatePath = null;
/** the file to get the velocity properties file */
private File velocityPropertiesFile = null;
/** the VelocityEngine instance to use */
private VelocityEngine ve = new VelocityEngine();
/** the Velocity subcontexts */
private List contexts = new LinkedList();
}
