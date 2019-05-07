public void processSearchAttachments(GenericRequest request, GenericResponse response) throws AuthenticationException, DatabaseException, BadInputException, IOException, ObjectNotFoundException {
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanGetAttachmentInAnyForum();
    Locale locale = I18nUtil.getLocaleInRequest(request);
    MyUtil.saveVNTyperMode(request, response);
    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "searchattachments");
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());
    String key = GenericParamUtil.getParameter(request, "key");
    String attachmentName = GenericParamUtil.getParameter(request, "attachmentname");
    if ((key.length() == 0) && (attachmentName.length() == 0)) {
        return;
    }
    int forumID = GenericParamUtil.getParameterInt(request, "forum", 0);
    //negative means category  
    int offset = GenericParamUtil.getParameterUnsignedInt(request, "offset", 0);
    int rows = GenericParamUtil.getParameterUnsignedInt(request, "rows", 20);
    if (rows == 0) {
        rows = 20;
    }
    // offset should be even when divide with rowsToReturn  
    offset = (offset / rows) * rows;
    AttachmentSearchQuery query = new AttachmentSearchQuery();
    if (key.length() > 0) {
        query.setSearchString(key);
    }
    if (attachmentName.length() > 0) {
        query.setSearchFileName(attachmentName);
    }
    int searchDate = GenericParamUtil.getParameterUnsignedInt(request, "date", AttachmentSearchQuery.SEARCH_ANY_DATE);
    int searchBeforeAfter = GenericParamUtil.getParameterInt(request, "beforeafter", AttachmentSearchQuery.SEARCH_NEWER);
    if ((searchDate != AttachmentSearchQuery.SEARCH_ANY_DATE) && (searchDate < 365 * 10)) {
        // 10 years  
        long deltaTime = DateUtil.DAY * searchDate;
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Timestamp from = null;
        Timestamp to = null;
        long currentTime = now.getTime();
        if (searchBeforeAfter == AttachmentSearchQuery.SEARCH_NEWER) {
            from = new Timestamp(currentTime - deltaTime);
        } else {
            // older  
            to = new Timestamp(currentTime - deltaTime);
        }
        query.setFromDate(from);
        query.setToDate(to);
    }
    if (forumID > 0) {
        query.setForumId(forumID);
    } else if (forumID < 0) {
        // choose to search in a category  
        query.setForumId(forumID);
    } else {
    }
    query.searchDocuments(offset, rows, permission);
    int hitCount = query.getHitCount();
    Collection result = query.getAttachmentResult();
    // Remove attachments that current user do not have permission (actually the AttachmentSearchQuery already return correct value)  
    for (Iterator iter = result.iterator(); iter.hasNext(); ) {
        AttachmentBean attachBean = (AttachmentBean) iter.next();
        int currentForumID = attachBean.getForumID();
        if (permission.canGetAttachment(currentForumID) == false) {
            iter.remove();
        } else if (ForumCache.getInstance().getBean(currentForumID).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
            iter.remove();
        }
    }
    if (offset > hitCount) {
        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
        throw new BadInputException(localizedMessage);
    }
    request.setAttribute("rows", new Integer(rows));
    request.setAttribute("TotalAttachs", new Integer(hitCount));
    request.setAttribute("AttachBeans", result);
}
