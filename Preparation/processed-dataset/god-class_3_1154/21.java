/** Checks whether any of the protocols 'above' provide evt_type */
static boolean providesDownServices(Vector<ProtocolReq> req_list, int evt_type) {
    for (ProtocolReq pr : req_list) {
        if (pr.providesDownService(evt_type))
            return true;
    }
    return false;
}
