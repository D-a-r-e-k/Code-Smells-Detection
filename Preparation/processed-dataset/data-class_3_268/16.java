/**
	 * Method existsRemotely.
	 * 
	 * @param uid
	 * @param startIndex
	 * @param uidList
	 * @return boolean
	 * @throws Exception
	 */
protected boolean existsRemotely(String uid, int startIndex, List uidList) throws Exception {
    for (Iterator it = uidList.iterator(); it.hasNext(); ) {
        String serverUID = (String) it.next();
        // for (int i = startIndex; i < uidList.size(); i++) { 
        // String serverUID = (String) uidList.get(i); 
        // System.out.println("server message uid: "+ serverUID ); 
        if (uid.equals(serverUID)) {
            // System.out.println("local uid exists remotely"); 
            return true;
        }
    }
    return false;
}
