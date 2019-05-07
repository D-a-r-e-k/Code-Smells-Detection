public Object up(Event evt) {
    switch(evt.getType()) {
        case Event.MSG:
            Message msg = (Message) evt.getArg();
            Header hdr = (Header) msg.getHeader(this.id);
            Address sender = msg.getSrc();
            if (hdr != null) {
                switch(hdr.type) {
                    case Header.CREDIT_REQUEST:
                        handleCreditRequest(sender, false);
                        break;
                    case Header.URGENT_CREDIT_REQUEST:
                        handleCreditRequest(sender, true);
                        break;
                    case Header.REPLENISH:
                        handleCreditResponse(sender);
                        break;
                    default:
                        if (log.isErrorEnabled())
                            log.error("unknown header type " + hdr.type);
                        break;
                }
                return null;
            }
            Address dest = msg.getDest();
            if (dest != null && !dest.isMulticastAddress())
                // we don't handle unicast messages 
                break;
            handleMessage(msg, sender);
            break;
        case Event.VIEW_CHANGE:
            handleViewChange((View) evt.getArg());
            break;
        case Event.SUSPECT:
            handleSuspect((Address) evt.getArg());
            break;
        case Event.CONFIG:
            Map<String, Object> map = (Map<String, Object>) evt.getArg();
            handleConfigEvent(map);
            break;
    }
    return up_prot.up(evt);
}
