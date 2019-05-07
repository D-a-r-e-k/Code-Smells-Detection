/**
     *  This checks whether an invisible field is available in the request, and
     *  whether it's contents are suspected spam.
     *
     *  @param context
     *  @param change
     * @throws RedirectException
     */
private void checkBotTrap(WikiContext context, Change change) throws RedirectException {
    HttpServletRequest request = context.getHttpRequest();
    if (request != null) {
        String unspam = request.getParameter(getBotFieldName());
        if (unspam != null && unspam.length() > 0) {
            String uid = log(context, REJECT, REASON_BOT_TRAP, change.toString());
            log.info("SPAM:BotTrap (" + uid + ").  Wildly behaving bot detected.");
            checkStrategy(context, REASON_BOT_TRAP, "Spamming attempt detected. (Incident code " + uid + ")");
        }
    }
}
