private void checkUTF8(WikiContext context, Change change) throws RedirectException {
    HttpServletRequest request = context.getHttpRequest();
    if (request != null) {
        String utf8field = request.getParameter("encodingcheck");
        if (utf8field != null && !utf8field.equals("ぁ")) {
            String uid = log(context, REJECT, REASON_UTF8_TRAP, change.toString());
            log.info("SPAM:UTF8Trap (" + uid + ").  Wildly posting dumb bot detected.");
            checkStrategy(context, REASON_UTF8_TRAP, "Spamming attempt detected. (Incident code " + uid + ")");
        }
    }
}
