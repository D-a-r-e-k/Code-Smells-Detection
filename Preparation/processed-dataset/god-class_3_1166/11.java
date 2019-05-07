/** Gets an appropriate prefix to fully qualify a class name. Returns this class's package followed by a dot, or the
    * empty string if no package name is found.
    */
private String getPackageQualifier() {
    String packageName = getPackageName();
    if ((packageName != null) && (!packageName.equals(""))) {
        packageName = packageName + ".";
    }
    return packageName;
}
