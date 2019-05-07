public void prepareDelete(GenericRequest request) throws DatabaseException, AuthenticationException, BadInputException, ObjectNotFoundException {
    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnablePrivateMessage() == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
        throw new IllegalStateException(localizedMessage);
    }
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseMessage();
    String folderName = GenericParamUtil.getParameterSafe(request, "folder", true);
    StringUtil.checkGoodName(folderName);
    if ((folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_INBOX)) || (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_SENT)) || (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) || (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_TRASH))) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_default_folder");
        throw new BadInputException(localizedMessage);
    }
    //Checking if this Folder belong to current Member  
    MessageFolderBean messageFolderBean = null;
    try {
        messageFolderBean = DAOFactory.getMessageFolderDAO().getMessageFolder(folderName, onlineUser.getMemberID());
    } catch (ObjectNotFoundException onf) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messagefolder_not_exists", new Object[] { folderName });
        throw new ObjectNotFoundException(localizedMessage);
    }
    int numberOfMessages = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember_inFolder(onlineUser.getMemberID(), folderName);
    int numberOfUnreadMessages = DAOFactory.getMessageDAO().getNumberOfUnreadNonPublicMessages_inMember_inFolder(onlineUser.getMemberID(), folderName);
    request.setAttribute("MessageFolderBean", messageFolderBean);
    request.setAttribute("NumberOfMessages", new Integer(numberOfMessages));
    request.setAttribute("NumberOfUnreadMessages", new Integer(numberOfUnreadMessages));
}
