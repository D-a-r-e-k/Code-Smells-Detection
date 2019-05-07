/**
     *  Returns true, if the link in question is an access
     *  rule.
     */
private static boolean isAccessRule(String link) {
    return link.startsWith("{ALLOW") || link.startsWith("{DENY");
}
