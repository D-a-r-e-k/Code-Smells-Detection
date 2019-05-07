private void sendMessageToUser(IRequest req, StringBuffer sb) {
    if (req.getValue("msg") == null || req.getValue("username") == null) {
        sb.append("no message or username!");
    } else {
        AdminCore.messageToUser(req.getValue("msg"), req.getValue("username"));
        sb.append("message sent!");
    }
}
