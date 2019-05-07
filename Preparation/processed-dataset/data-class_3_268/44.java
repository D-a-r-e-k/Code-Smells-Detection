/* (non-Javadoc)
	 * @see org.columba.mail.folder.AbstractMessageFolder#save()
	 */
public void save() throws Exception {
    super.save();
    headerList.persist();
}
