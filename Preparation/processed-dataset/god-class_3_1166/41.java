/** Gets the name of the top level class in this source file by finding the first declaration of a class or interface.
    * @return The name of first class in the file
    * @throws ClassNameNotFoundException if no top level class found
    */
public String getFirstTopLevelClassName() throws ClassNameNotFoundException {
    return getNextTopLevelClassName(0, getLength());
}
