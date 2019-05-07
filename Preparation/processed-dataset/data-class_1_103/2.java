/**
	 * creates an connectionpool-element
	 * @return the PoolElement connected to the jdbc-datasource
	 * @throws Exception
	 */
private PoolElement createPoolElement() throws Exception {
    idCnt++;
    if (idCnt == Integer.MAX_VALUE) {
        idCnt = 0;
    }
    Connection con = DriverManager.getConnection(dbProps.url, dbProps.conProps);
    return new PoolElement(this, con, dbProps, idCnt);
}
