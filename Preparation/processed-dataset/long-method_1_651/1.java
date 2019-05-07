public void prepareAdd(GenericRequest request) throws DatabaseException, AuthenticationException {
    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnablePrivateMessage() == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
        throw new IllegalStateException(localizedMessage);
    }
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseMessage();
}
