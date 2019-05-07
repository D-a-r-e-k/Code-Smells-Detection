/**
	 * This is called from the UpdateFlagCommand which gets triggered from an
	 * unexpected flags updated.
	 * 
	 * @param flag
	 * @throws IOException
	 * @throws CommandCancelledException
	 * @throws IMAPException
	 */
public void updateFlag(IMAPFlags flag) throws Exception, CommandCancelledException {
    if (getServer().isSelected(this)) {
        Integer uid = new Integer(getServer().fetchUid(new SequenceSet(flag.getIndex()), this));
        flag.setUid(uid);
        setFlags(new Flags[] { flag });
    }
}
