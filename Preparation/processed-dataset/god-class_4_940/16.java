private void sendMessage(IRequest req, StringBuffer sb) {
    if (req.getValue("msg") == null) {
        sb.append("no message!");
    } else {
        AdminCore.messageToAll(req.getValue("msg"));
        sb.append("message sent!");
    }
}
