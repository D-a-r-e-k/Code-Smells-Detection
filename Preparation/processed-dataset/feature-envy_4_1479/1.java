public void processAdd(GenericRequest request, GenericResponse response) throws BadInputException, CreateException, DatabaseException, IOException, ForeignKeyNotFoundException, AuthenticationException, ObjectNotFoundException, InterceptorException {
    SecurityUtil.checkHttpPostMethod(request);
    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableMessageAttachment() == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.message_attachment_is_disabled");
        throw new IllegalStateException(localizedMessage);
    }
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseMessage();
    permission.ensureCanAddMessageAttachment();
    MyUtil.saveVNTyperMode(request, response);
    final int UNLIMITED = -1;
    int sizeMax = permission.canAdminSystem() ? UNLIMITED : MVNForumConfig.getMaxMessageAttachmentSize();
    int sizeThreshold = 100000;
    String tempDir = MVNForumConfig.getTempDir();
    log.debug("PmAttachmentWebHandler : process upload with temp dir = " + tempDir);
    List fileItems;
    try {
        fileItems = fileUploadParserService.parseRequest(request, sizeMax, sizeThreshold, tempDir, "UTF-8");
    } catch (FileUploadException ex) {
        log.error("Cannot upload", ex);
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_upload", new Object[] { ex.getMessage() });
        throw new IOException(localizedMessage);
    }
    // values that must get from the form  
    int messageID = 0;
    String attachFilename = null;
    int attachFileSize = 0;
    String attachMimeType = null;
    String attachDesc = null;
    boolean attachMore = false;
    boolean markAsQuote = false;
    boolean addToSentFolder = false;
    FileItem attachFileItem = null;
    String actionParam = urlResolverService.getActionParam();
    for (int i = 0; i < fileItems.size(); i++) {
        FileItem currentFileItem = (FileItem) fileItems.get(i);
        String fieldName = currentFileItem.getFieldName();
        if (fieldName.equals("AddToSentFolder")) {
            String content = currentFileItem.getString("utf-8");
            addToSentFolder = (content.length() > 0);
            log.debug("addToSentFolder = " + addToSentFolder);
        } else if (fieldName.equals("AttachMore")) {
            String content = currentFileItem.getString("utf-8");
            attachMore = (content.length() > 0);
            log.debug("attachMore = " + attachMore);
        } else if (fieldName.equals("MarkAsQuote")) {
            String content = currentFileItem.getString("utf-8");
            markAsQuote = (content.length() > 0);
            log.debug("markAsQuote = " + markAsQuote);
        } else if (fieldName.equals("MessageID")) {
            String content = currentFileItem.getString("utf-8");
            messageID = Integer.parseInt(content);
            log.debug("messageID = " + messageID);
        } else if (fieldName.equals("AttachDesc")) {
            String content = currentFileItem.getString("utf-8");
            attachDesc = DisableHtmlTagFilter.filter(content);
            log.debug("attachDesc = " + attachDesc);
            attachDesc = InterceptorService.getInstance().validateContent(attachDesc);
        } else if (fieldName.equals("vnselector")) {
        } else if (fieldName.equals("AttachFilename")) {
            if (currentFileItem.isFormField()) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_uploaded_attach_file_with_form_field");
                throw new AssertionError(localizedMessage);
            }
            attachMimeType = currentFileItem.getContentType();
            attachMimeType = DisableHtmlTagFilter.filter(attachMimeType);
            attachFileSize = (int) currentFileItem.getSize();
            if (attachFileSize == 0) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_process_upload_with_file_size_is_zero");
                throw new BadInputException(localizedMessage);
            }
            String fullFilePath = currentFileItem.getName();
            attachFilename = FileUtil.getFileName(fullFilePath);
            attachFilename = DisableHtmlTagFilter.filter(attachFilename);
            log.debug("attachFilename = " + attachFilename);
            // now save to attachFileItem  
            attachFileItem = currentFileItem;
        } else if (fieldName.equals(actionParam)) {
        } else {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_field_name", new Object[] { fieldName });
            throw new AssertionError(localizedMessage);
        }
    }
    // end for  
    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    // check constraint  
    MessageBean messageBean = null;
    try {
        messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
    } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] { new Integer(messageID) });
        throw new ObjectNotFoundException(localizedMessage);
    }
    if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT) == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_add_attachment.pm_does_not_in_folder_draft");
        throw new BadInputException(localizedMessage);
    }
    int logonMemberID = onlineUser.getMemberID();
    // Check if the message is from this member  
    if (messageBean.getMemberID() != logonMemberID) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
        throw new BadInputException(localizedMessage);
    }
    // Check if the message is from this logon member  
    AssertionUtil.doAssert(messageBean.getMessageSenderID() == logonMemberID, "Assertion: The MessageSenderID must equals the current logined user.");
    AssertionUtil.doAssert(messageBean.getMessageSenderName().equals(onlineUser.getMemberName()), "Assertion: The MessageSenderName must equals the current logined user.");
    //String logonMemberName  = onlineUser.getMemberName();  
    // now all contraints/permission have been checked  
    // values that we can init now  
    String creationIP = request.getRemoteAddr();
    Timestamp attachCreationDate = now;
    Timestamp attachModifiedDate = now;
    int attachDownloadCount = 0;
    int attachOption = 0;
    // check it  
    int attachStatus = 0;
    // check it  
    int attachID = DAOFactory.getPmAttachmentDAO().create(logonMemberID, attachFilename, attachFileSize, attachMimeType, attachDesc, creationIP, attachCreationDate, attachModifiedDate, attachDownloadCount, attachOption, attachStatus);
    try {
        DAOFactory.getPmAttachMessageDAO().create(messageID, attachID, 0, 0, 0);
    } catch (DuplicateKeyException ex) {
        // this should never happen  
        AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
    }
    try {
        //            String filename = AttachmentUtil.getPmAttachFilenameOnDisk(attachID);  
        //            log.debug("Message's attachment filename to save to file system = " + filename);  
        //            attachFileItem.write(new File(filename));  
        binaryStorageService.storeData(BinaryStorageService.CATEGORY_PM_ATTACHMENT, String.valueOf(attachID), attachFilename, attachFileItem.getInputStream(), attachFileSize, 0, 0, attachMimeType, creationIP);
    } catch (Exception ex) {
        log.error("Cannot save the attachment file", ex);
        DAOFactory.getPmAttachMessageDAO().delete(messageID, attachID);
        DAOFactory.getPmAttachmentDAO().delete(attachID);
        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_save_attach_file");
        throw new IOException(localizedMessage);
    }
    // Update AttachCount in Message table  
    int attachCount = DAOFactory.getPmAttachMessageDAO().getNumberOfBeans_inMessage(messageID);
    try {
        DAOFactory.getMessageDAO().updateAttachCount(messageID, attachCount);
    } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] { new Integer(messageID) });
        throw new ObjectNotFoundException(localizedMessage);
    }
    int maxPrivateMessage = MVNForumConfig.getMaxPrivateMessages();
    // We will check AttachMore parameter here to REALLY send message to receivers  
    if (attachMore == false) {
        String[] receivedMembers = StringUtil.getStringArrays(messageBean.getMessageToList(), messageBean.getMessageCcList(), messageBean.getMessageBccList(), ";");
        Hashtable receivers = MyUtil.checkMembers(receivedMembers, locale);
        int messageType = messageBean.getMessageType();
        if (markAsQuote) {
            // that is, quote cannot be a public message  
            // Actually, if this is a public message, then quote option is disabled from the jsp file  
            messageType = MessageBean.MESSAGE_TYPE_QUOTE;
        }
        Collection attachBeans = DAOFactory.getPmAttachmentDAO().getPmAttachments_inMessage(messageBean.getMessageID());
        //messageBean is original message  
        StringBuffer overQuotaReceivers = new StringBuffer(128);
        for (Enumeration enumeration = receivers.keys(); enumeration.hasMoreElements(); ) {
            int receivedMemberID = ((Integer) enumeration.nextElement()).intValue();
            String receivedMemberName = (String) receivers.get(new Integer(receivedMemberID));
            int receiverMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(receivedMemberID);
            if (receiverMessageCount >= maxPrivateMessage) {
                if (overQuotaReceivers.length() > 0) {
                    overQuotaReceivers.append(", ");
                }
                overQuotaReceivers.append(receivedMemberName);
                continue;
            }
            // Create REAL message for receivers when finish. It means we have new messageID for each new receiver  
            int eachMessageID = DAOFactory.getMessageDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, receivedMemberID, logonMemberID, messageBean.getMessageSenderName(), messageBean.getMessageToList(), messageBean.getMessageCcList(), messageBean.getMessageBccList(), messageBean.getMessageTopic(), messageBean.getMessageBody(), messageType, messageBean.getMessageOption(), messageBean.getMessageStatus(), MessageBean.MESSAGE_READ_STATUS_DEFAULT, messageBean.getMessageNotify(), messageBean.getMessageIcon(), attachCount, creationIP, now);
            // Add to statistics  
            if (logonMemberID != receivedMemberID) {
                DAOFactory.getMessageStatisticsDAO().create(logonMemberID, receivedMemberID, now, messageBean.getMessageAttachCount(), messageBean.getMessageType(), messageBean.getMessageOption(), messageBean.getMessageStatus());
            }
            // We must create a loop to create Attach for many receivers and many attachments  
            for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                PmAttachmentBean pmAttachBean = (PmAttachmentBean) attachIter.next();
                try {
                    DAOFactory.getPmAttachMessageDAO().create(eachMessageID, pmAttachBean.getPmAttachID(), 0, 0, 0);
                } catch (DuplicateKeyException ex) {
                    // this should never happen  
                    AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                }
            }
        }
        // end of for on receivers  
        request.setAttribute("OverQuotaReceivers", overQuotaReceivers.toString());
        if (addToSentFolder) {
            int senderMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(logonMemberID);
            if (senderMessageCount < maxPrivateMessage) {
                messageType = MessageBean.MESSAGE_TYPE_DEFAULT;
                // always a default type in the Sent folder  
                int sentMessageID = DAOFactory.getMessageDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, logonMemberID, logonMemberID, messageBean.getMessageSenderName(), messageBean.getMessageToList(), messageBean.getMessageCcList(), messageBean.getMessageBccList(), messageBean.getMessageTopic(), messageBean.getMessageBody(), messageType, messageBean.getMessageOption(), messageBean.getMessageStatus(), MessageBean.MESSAGE_READ_STATUS_DEFAULT, messageBean.getMessageNotify(), messageBean.getMessageIcon(), attachCount, creationIP, now);
                for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                    PmAttachmentBean pmAttachBean = (PmAttachmentBean) attachIter.next();
                    try {
                        DAOFactory.getPmAttachMessageDAO().create(sentMessageID, pmAttachBean.getPmAttachID(), 0, 0, 0);
                    } catch (DuplicateKeyException ex) {
                        // this should never happen  
                        AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                    }
                }
            } else {
                request.setAttribute("AddSentFolderOverQuota", Boolean.TRUE);
            }
        }
        // if add to sent folder  
        // Now delete the message in the draft  
        PrivateMessageUtil.deleteMessageInDatabase(messageID, logonMemberID);
    }
    // if not attach more file  
    request.setAttribute("MessageID", String.valueOf(messageID));
    request.setAttribute("AttachMore", Boolean.valueOf(attachMore));
    request.setAttribute("AddToSentFolder", Boolean.valueOf(addToSentFolder));
    request.setAttribute("MarkAsQuote", Boolean.valueOf(markAsQuote));
}
