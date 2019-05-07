/**
	 * Method existsLocally.
	 * 
	 * @param uid
	 * @param list
	 * @return boolean
	 * @throws Exception
	 */
protected boolean existsLocally(String uid, PersistantHeaderList list) throws Exception {
    for (Enumeration e = getHeaderList().keys(); e.hasMoreElements(); ) {
        String localUID = (String) e.nextElement();
        if (uid.equals(localUID)) {
            return true;
        }
    }
    return false;
}
