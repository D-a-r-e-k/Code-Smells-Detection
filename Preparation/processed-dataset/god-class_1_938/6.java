public void finalize() {
    if (Server.TRACE_CREATE_AND_FINALIZE)
        Server.log(this, "----------------------------------------FINALIZED", Server.MSG_STATE, Server.LVL_VERY_VERBOSE);
}
