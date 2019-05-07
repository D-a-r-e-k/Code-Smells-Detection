//}}}  
//{{{ getPath() method  
/**
	  * @param shortVersion if true, replaces home path with ~/ on unix
	  */
public String getPath(Boolean shortVersion) {
    return shortVersion ? MiscUtilities.abbreviate(path) : getPath();
}
