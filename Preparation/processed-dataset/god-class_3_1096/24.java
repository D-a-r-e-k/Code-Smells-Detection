/**
	 * @see org.columba.mail.folder.FolderTreeNode#getDefaultProperties()
	 */
public static XmlElement getDefaultProperties() {
    XmlElement props = new XmlElement("property");
    props.addAttribute("accessrights", "user");
    props.addAttribute("subfolder", "true");
    return props;
}
