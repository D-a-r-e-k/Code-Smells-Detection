private boolean isValid() {
    if (!isValid)
        return false;
    if (con == null || cleanedUp)
        return false;
    if (!hasBeenUsed)
        return true;
    if (sCnt > dbp.maxStmtPerCon) {
        Server.log(this, "invalid because max-statements/connection has been reached " + dbp.maxStmtPerCon, Server.MSG_AUTH, Server.LVL_VERBOSE);
        isValid = false;
        return false;
    }
    if (validUntil <= System.currentTimeMillis()) {
        Server.log(this, "invalid because connection ttl has been reached " + dbp.conTTL, Server.MSG_AUTH, Server.LVL_VERBOSE);
        isValid = false;
        return false;
    }
    return true;
}
