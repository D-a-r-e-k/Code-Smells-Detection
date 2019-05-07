PoolElement(ConnectionPool pool, Connection con, DbProperties dbp, int id) throws Exception {
    if (con == null)
        throw new Exception("no connection supplied");
    this.pool = pool;
    this.dbp = dbp;
    this.id = id;
    this.con = con;
    con.setAutoCommit(false);
    validUntil = System.currentTimeMillis() + dbp.conTTL;
    Server.log("SqlAuthenticator", "Created new Connetion " + this.toString(), Server.MSG_AUTH, Server.LVL_MAJOR);
    if (Server.TRACE_CREATE_AND_FINALIZE)
        Server.log(this, "++++++++++++++++++++++++++++++++++++++++CREATE", Server.MSG_STATE, Server.LVL_VERY_VERBOSE);
}
