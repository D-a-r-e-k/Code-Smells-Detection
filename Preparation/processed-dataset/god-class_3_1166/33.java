//  public static boolean log = true; 
/** Searches backwards to find the name of the enclosing named class or interface. NB: ignores comments.
    * WARNING: In long source files and when contained in anonymous inner classes, this function might take a LONG time.
    * @param pos Position to start from
    * @param qual true to find the fully qualified class name
    * @return name of the enclosing named class or interface
    */
public String getEnclosingClassName(int pos, boolean qual) throws BadLocationException, ClassNameNotFoundException {
    return _getEnclosingClassName(pos, qual);
}
