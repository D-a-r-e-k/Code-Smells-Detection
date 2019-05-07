void method0() { 
/** JDK 1.4+ logging framework logger, used for logging. */
private static final Logger LOG = Logger.getLogger("org.columba.mail.folder.imap");
/**
	 * 
	 */
private IMAPServer store;
/**
	 * 
	 */
protected boolean existsOnServer = true;
private boolean readOnly;
private PersistantHeaderList headerList;
private boolean lazyFlagSync = false;
}
