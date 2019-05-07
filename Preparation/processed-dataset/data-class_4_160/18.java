/**
     *  Returns true, if this user should be ignored.  For example, admin users.
     *
     * @param context
     * @return True, if this users should be ignored.
     */
private boolean ignoreThisUser(WikiContext context) {
    if (context.hasAdminPermissions()) {
        return true;
    }
    if (m_ignoreAuthenticated && context.getWikiSession().isAuthenticated()) {
        return true;
    }
    if (context.getVariable("captcha") != null) {
        return true;
    }
    return false;
}
