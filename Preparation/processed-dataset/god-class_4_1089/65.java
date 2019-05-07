//}}}  
//{{{ toString() method  
/**
	 * Returns a string representation of this buffer.
	 * This simply returns the path name.
	 */
@Override
public String toString() {
    return name + " (" + MiscUtilities.abbreviate(directory) + ')';
}
