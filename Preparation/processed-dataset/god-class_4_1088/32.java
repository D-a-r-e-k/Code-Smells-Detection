//}}}  
//{{{ fileProperties() method  
/**
	 * Show selected file's properties.
	 */
public void fileProperties(VFSFile[] files) {
    new FilePropertiesDialog(view, this, files);
}
