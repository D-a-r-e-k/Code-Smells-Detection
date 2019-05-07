public void processAdd(GenericRequest request) throws CreateException, DuplicateKeyException, DatabaseException, BadInputException, AuthenticationException, ForeignKeyNotFoundException {
    SecurityUtil.checkHttpPostMethod(request);
    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnablePrivateMessage() == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
        throw new IllegalStateException(localizedMessage);
    }
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseMessage();
    int folderStatus = 0;
    int folderOption = 0;
    int folderType = 0;
    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    String folderName = GenericParamUtil.getParameterSafe(request, "FolderName", true);
    StringUtil.checkGoodName(folderName);
    int folderOrder = DAOFactory.getMessageFolderDAO().getMaxFolderOrder(onlineUser.getMemberID());
    folderOrder++;
    // One value more than the current max value  
    if (folderOrder < 10)
        folderOrder = 10;
    // Reserve order for special folders  
    DAOFactory.getMessageFolderDAO().create(folderName, onlineUser.getMemberID(), folderOrder, folderStatus, folderOption, folderType, now, now);
}
