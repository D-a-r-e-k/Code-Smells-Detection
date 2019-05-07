/**
	 * Method updateFlags.
	 * 
	 * @param flagsList
	 */
protected void setFlags(Flags[] flagsList) throws Exception {
    for (int i = 0; i < flagsList.length; i++) {
        IMAPFlags flags = (IMAPFlags) flagsList[i];
        Integer uid = (Integer) flags.getUid();
        IColumbaHeader header = headerList.get(uid);
        Flags localFlags = header.getFlags();
        localFlags.setFlags(flags.getFlags());
        // Junk flag 
        header.getAttributes().put("columba.spam", Boolean.valueOf(flags.get(IMAPFlags.JUNK)));
    }
}
