private static String log(WikiContext ctx, int type, String source, String message) {
    message = TextUtil.replaceString(message, "\r\n", "\\r\\n");
    message = TextUtil.replaceString(message, "\"", "\\\"");
    String uid = getUniqueID();
    String page = ctx.getPage().getName();
    String reason = "UNKNOWN";
    String addr = ctx.getHttpRequest() != null ? ctx.getHttpRequest().getRemoteAddr() : "-";
    switch(type) {
        case REJECT:
            reason = "REJECTED";
            break;
        case ACCEPT:
            reason = "ACCEPTED";
            break;
        case NOTE:
            reason = "NOTE";
            break;
        default:
            throw new InternalWikiException("Illegal type " + type);
    }
    c_spamlog.info(reason + " " + source + " " + uid + " " + addr + " \"" + page + "\" " + message);
    return uid;
}
