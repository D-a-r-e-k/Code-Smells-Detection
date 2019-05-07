private void sendCreditRequest(boolean urgent) {
    Message credit_req = new Message();
    // credit_req.setFlag(Message.OOB); // we need to receive the credit request after regular messages 
    byte type = urgent ? Header.URGENT_CREDIT_REQUEST : Header.CREDIT_REQUEST;
    credit_req.putHeader(this.id, new Header(type));
    num_credit_requests_sent++;
    down_prot.down(new Event(Event.MSG, credit_req));
}
