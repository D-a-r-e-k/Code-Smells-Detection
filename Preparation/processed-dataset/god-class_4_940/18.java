private void sendMessageToGroup(IRequest req, StringBuffer sb) {
    if (req.getValue("msg") == null || req.getValue("groupname") == null) {
        sb.append("no message or groupname!");
    } else {
        AdminCore.messageToGroup(req.getValue("msg"), req.getValue("groupname"));
        sb.append("message sent!");
    }
}
