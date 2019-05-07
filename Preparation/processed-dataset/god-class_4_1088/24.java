//}}}  
//{{{ getRootDirectory() method  
public static String getRootDirectory() {
    if (OperatingSystem.isMacOS() || OperatingSystem.isDOSDerived())
        return FileRootsVFS.PROTOCOL + ':';
    else
        return "/";
}
