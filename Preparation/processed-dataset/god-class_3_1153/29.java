private void sendCreditResponse(Address dest) {
    Message credit_rsp = new Message(dest);
    credit_rsp.setFlag(Message.OOB);
    Header hdr = new Header(Header.REPLENISH);
    credit_rsp.putHeader(this.id, hdr);
    if (log.isTraceEnabled())
        log.trace("sending credit response to " + dest);
    num_replenishments_sent++;
    down_prot.down(new Event(Event.MSG, credit_rsp));
}
