/**
     Throws an exception if sanity check fails. Possible sanity check is uniqueness of all protocol names
     */
public static void sanityCheck(Vector<Protocol> protocols) throws Exception {
    Vector<String> names = new Vector<String>();
    Protocol prot;
    String name;
    Vector<ProtocolReq> req_list = new Vector<ProtocolReq>();
    // Checks for unique names 
    for (int i = 0; i < protocols.size(); i++) {
        prot = protocols.elementAt(i);
        name = prot.getName();
        for (int j = 0; j < names.size(); j++) {
            if (name.equals(names.elementAt(j))) {
                throw new Exception("Configurator.sanityCheck(): protocol name " + name + " has been used more than once; protocol names have to be unique !");
            }
        }
        names.addElement(name);
    }
    // check for unique IDs 
    Set<Short> ids = new HashSet<Short>();
    for (Protocol protocol : protocols) {
        short id = protocol.getId();
        if (id > 0 && ids.add(id) == false)
            throw new Exception("Protocol ID " + id + " (name=" + protocol.getName() + ") is duplicate; protocol IDs have to be unique");
    }
    // Checks whether all requirements of all layers are met 
    for (Protocol p : protocols) {
        req_list.add(new ProtocolReq(p));
    }
    for (ProtocolReq pr : req_list) {
        for (Integer evt_type : pr.up_reqs) {
            if (!providesDownServices(req_list, evt_type)) {
                throw new Exception("Configurator.sanityCheck(): event " + Event.type2String(evt_type) + " is required by " + pr.name + ", but not provided by any of the layers above");
            }
        }
        for (Integer evt_type : pr.down_reqs) {
            if (!providesUpServices(req_list, evt_type)) {
                throw new Exception("Configurator.sanityCheck(): event " + Event.type2String(evt_type) + " is required by " + pr.name + ", but not provided by any of the layers above");
            }
        }
    }
}
