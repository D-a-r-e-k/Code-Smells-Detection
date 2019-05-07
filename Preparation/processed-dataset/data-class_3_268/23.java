/**
	 * Method getImapPath.
	 * 
	 * @return String
	 */
public String getImapPath() throws IOException, IMAPException, CommandCancelledException {
    StringBuffer path = new StringBuffer();
    path.append(getName());
    IMailFolder child = this;
    while (true) {
        child = (IMailFolder) child.getParent();
        if (child instanceof IMAPRootFolder) {
            break;
        }
        String n = ((IMAPFolder) child).getName();
        path.insert(0, n + getServer().getDelimiter());
    }
    return path.toString();
}
