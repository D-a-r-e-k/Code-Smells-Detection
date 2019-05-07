//  /** Returns whether this document is currently untitled 
//    * (indicating whether it has a file yet or not). 
//    * @return true if the document is untitled and has no file 
//    */ 
//  public boolean isUntitled() { return (_file == null); } 
//  /** Returns the file for this document.  If the document 
//   * is untitled and has no file, it throws an IllegalStateException. 
//   * @return the file for this document 
//   * @throws IllegalStateException if file has not been set 
//   * @throws FileMovedException if file has been moved or deleted from its previous location 
//   */ 
//  public File getFilex() throws IllegalStateException , FileMovedException { 
//    if (_file == null) { 
//      throw new IllegalStateException("This document does not yet have a file."); 
//    } 
//    //does the file actually exist? 
//    if (_file.exists()) return _file; 
//    else throw new FileMovedException(_file, "This document's file has been moved or deleted."); 
//  } 
// 
//  /** Returns the name of this file, or "(untitled)" if no file. */ 
//  public String getFilenamex() { 
//    String filename = "(Untitled)"; 
//    try { 
//      File file = getFilex(); 
//      filename = file.getName(); 
//    } 
//    catch (IllegalStateException ise) { 
//      // No file, leave as "untitled" 
//    } 
//    catch (FileMovedException fme) { 
//      // Recover, even though file has been deleted 
//      File file = fme.getFile(); 
//      filename = file.getName(); 
//    } 
//    return filename; 
//  } 
//  public void setFile(File file) { 
//    _file = file; 
// 
//    //jim: maybe need lock 
//    if (_file != null) { 
//      _timestamp = _file.lastModified(); 
//    } 
//  } 
// 
//  public long getTimestamp() { 
//    return _timestamp; 
//  } 
/** Gets the package and main class/interface name of this OpenDefinitionsDocument
    * @return the qualified main class/interface name
    */
public String getQualifiedClassName() throws ClassNameNotFoundException {
    return getPackageQualifier() + getMainClassName();
}
