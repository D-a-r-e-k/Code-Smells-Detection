/** 
	 * Makes the pool of Database Objects
	 * @since 1.3
	 */
private void makeDBObjectPool() throws Exception {
    if (getConfig().getDBObjectPoolConfig() != null) {
        logger.fine("Creating DBObject Pool");
        //logger.finest("Got:\n"+getConfig().getDBObjectPoolConfig().toXML(null));  
        Class dbPoolUtilClass = getClass(getConfig().getDBObjectPoolConfig().getDbPoolUtil(), true);
        dBPoolUtil = (DBPoolUtil) dbPoolUtilClass.newInstance();
        dBPoolUtil.setDatabaseConnections(getConfig().getDBObjectPoolConfig().getDatabaseConnectionSet().iterator());
        dBPoolUtil.initPool();
    }
}
