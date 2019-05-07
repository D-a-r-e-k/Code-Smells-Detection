/** Check whether any of the protocols 'below' provide evt_type */
static boolean providesUpServices(Vector<ProtocolReq> req_list, int evt_type) {
    for (ProtocolReq pr : req_list) {
        if (pr.providesUpService(evt_type))
            return true;
    }
    return false;
}
