public void prepareList(GenericRequest request) throws DatabaseException, AuthenticationException {
    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnablePrivateMessage() == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
        throw new IllegalStateException(localizedMessage);
    }
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseMessage();
    int memberID = onlineUser.getMemberID();
    Collection messageFolderBeans = DAOFactory.getMessageFolderDAO().getMessageFolders_inMember(memberID);
    for (Iterator iter = messageFolderBeans.iterator(); iter.hasNext(); ) {
        MessageFolderBean messageFolder = (MessageFolderBean) iter.next();
        int messageCount;
        int unreadMessageCount;
        if (messageFolder.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {
            // also get the draft public messages  
            messageCount = DAOFactory.getMessageDAO().getNumberOfAllMessages_inMember_inFolder(memberID, messageFolder.getFolderName());
            unreadMessageCount = DAOFactory.getMessageDAO().getNumberOfUnreadAllMessages_inMember_inFolder(memberID, messageFolder.getFolderName());
        } else {
            messageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember_inFolder(memberID, messageFolder.getFolderName());
            unreadMessageCount = DAOFactory.getMessageDAO().getNumberOfUnreadNonPublicMessages_inMember_inFolder(memberID, messageFolder.getFolderName());
        }
        messageFolder.setMessageCount(messageCount);
        messageFolder.setUnreadMessageCount(unreadMessageCount);
    }
    request.setAttribute("FolderMessageBeans", messageFolderBeans);
}
