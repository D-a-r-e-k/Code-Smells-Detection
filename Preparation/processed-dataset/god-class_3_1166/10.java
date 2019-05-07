/** Gets fully qualified class name of the top level class enclosing the given position. */
public String getQualifiedClassName(int pos) throws ClassNameNotFoundException {
    return getPackageQualifier() + getEnclosingTopLevelClassName(pos);
}
