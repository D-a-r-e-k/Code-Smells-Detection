public String process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String requestURI = StringUtil.getEmptyStringIfNull(request.getPathInfo());
    String responseURI = null;
    OnlineUser onlineUser = null;
    //TODO : [POLL] check here  
    if (requestURI == null || requestURI.length() == 0) {
        requestURI = ParamUtil.getParameter(request, "nameOfURI");
    }
    long start = 0;
    if (log.isDebugEnabled()) {
        start = System.currentTimeMillis();
        log.debug("UserModuleProcessor : requestURI  = " + requestURI);
    }
    GenericRequest genericRequest = new GenericRequestServletImpl(request, servletContext);
    GenericResponse genericResponse = new GenericResponseServletImpl(response);
    // step 1: some command need to be processed before we do the URI mapping (of the MODAL)  
    // MODAL processing  
    try {
        //          @todo could throw Exception, so onlineUser will be null, caused NPE later  
        onlineUser = onlineUserManager.getOnlineUser(genericRequest);
        onlineUser.updateNewMessageCount(false);
        if (requestURI.equals("/index")) {
            if (MVNForumConfig.getEnablePortalLikeIndexPage()) {
                forumWebHandler.prepareListIndex(genericRequest, genericResponse, requestURI);
            } else {
                forumWebHandler.prepareList(genericRequest, genericResponse, requestURI);
            }
        } else if (requestURI.equals("/listforums")) {
            forumWebHandler.prepareList(genericRequest, genericResponse, requestURI);
        } else if (requestURI.equals("/listthreads")) {
            threadWebHandler.prepareList_limit(genericRequest, genericResponse, requestURI);
        } else if (requestURI.equals("/listunansweredthreads")) {
            threadWebHandler.prepareListUnansweredThreads(genericRequest, genericResponse);
        } else if (requestURI.equals("/listrecentthreads")) {
            threadWebHandler.prepareListRecentThreads_limit(genericRequest, genericResponse);
        } else if (requestURI.equals("/viewthread") || requestURI.startsWith("/viewthread_")) {
            postWebHandler.prepareViewThread(genericRequest, genericResponse, requestURI);
        } else if (requestURI.equals("/printthread") || requestURI.startsWith("/printthread_")) {
            postWebHandler.prepareViewThread(genericRequest, genericResponse, requestURI);
        } else if (requestURI.equals("/splitthread")) {
            threadWebHandler.prepareSplit(genericRequest, genericResponse);
        } else if (requestURI.equals("/splitthreadprocess")) {
            threadWebHandler.processSplit(genericRequest);
        } else if (requestURI.equals("/deletethread")) {
            threadWebHandler.prepareDelete(genericRequest, genericResponse);
        } else if (requestURI.equals("/deletethreadprocess")) {
            threadWebHandler.processDelete(genericRequest);
            threadWebHandler.deleteSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/movethread")) {
            threadWebHandler.prepareMoveThread(genericRequest, genericResponse);
        } else if (requestURI.equals("/movethreadprocess")) {
            threadWebHandler.processMoveThread(genericRequest);
        } else if (requestURI.equals("/editthreadstatus")) {
            threadWebHandler.prepareEditThreadStatus(genericRequest, genericResponse);
        } else if (requestURI.equals("/editthreadstatusprocess")) {
            threadWebHandler.processEditThreadStatus(genericRequest);
        } else if (requestURI.equals("/editthreadtype")) {
            threadWebHandler.prepareEditThreadType(genericRequest, genericResponse);
        } else if (requestURI.equals("/editthreadtypeprocess")) {
            threadWebHandler.processEditThreadType(genericRequest);
        } else if (requestURI.equals("/modcp")) {
            threadWebHandler.prepareModerationControlPanel(genericRequest, genericResponse);
        } else if (requestURI.equals("/listrecentpendingthreads")) {
            threadWebHandler.prepareListRecentDisabledThreads_limit(genericRequest);
        } else if (requestURI.equals("/listthreadswithpendingposts")) {
            threadWebHandler.prepareListEnableThreadsWithPendingPosts_inForum_limit(genericRequest);
        } else if (requestURI.equals("/listrecentthreadswithpendingposts")) {
            threadWebHandler.prepareListRecentEnableThreadsWithPendingPosts_limit(genericRequest);
        } else if (requestURI.equals("/moderatependingthreads")) {
            threadWebHandler.prepareModeratePendingThreads_inForum_limit(genericRequest);
        } else if (requestURI.equals("/moderatependingthreadsprocess")) {
            threadWebHandler.processModeratePendingThreads(genericRequest);
        } else if (requestURI.equals("/moderatependingposts")) {
            postWebHandler.prepareModeratePendingPosts_limit(genericRequest);
        } else if (requestURI.equals("/moderatependingpostsprocess")) {
            postWebHandler.processModeratePendingPosts(genericRequest);
        } else if (requestURI.equals("/listpendingthreadsxml")) {
            threadWebHandler.prepareListDisabledThreads_limit_xml(genericRequest);
        } else if (requestURI.equals("/addpost")) {
            postWebHandler.prepareAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/addpostprocess")) {
            postWebHandler.processAdd(genericRequest, genericResponse);
            postWebHandler.addPostSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/editpost")) {
            postWebHandler.prepareEdit(genericRequest, genericResponse);
        } else if (requestURI.equals("/updatepost")) {
            postWebHandler.processUpdate(genericRequest);
            postWebHandler.updatePostSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/printpost") || requestURI.startsWith("/printpost_")) {
            postWebHandler.preparePrintPost(genericRequest, requestURI);
        } else if (requestURI.equals("/deletepost")) {
            postWebHandler.prepareDelete(genericRequest, genericResponse);
        } else if (requestURI.equals("/deletepostprocess")) {
            postWebHandler.processDelete(genericRequest);
            postWebHandler.deleteSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/addattachment")) {
            attachmentWebHandler.prepareAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/addattachmentprocess")) {
            attachmentWebHandler.processAdd(genericRequest, genericResponse);
            attachmentWebHandler.addSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/getattachment")) {
            attachmentWebHandler.downloadAttachment(genericRequest, response);
            return null;
        } else if (requestURI.equals("/deleteattachment")) {
            attachmentWebHandler.prepareDelete(genericRequest, genericResponse);
        } else if (requestURI.equals("/deleteattachmentprocess")) {
            attachmentWebHandler.processDelete(genericRequest);
            attachmentWebHandler.deleteSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/editattachment")) {
            attachmentWebHandler.prepareEdit(genericRequest, genericResponse);
        } else if (requestURI.equals("/editattachmentprocess")) {
            attachmentWebHandler.processEdit(genericRequest);
            attachmentWebHandler.editSuccessForRender(genericRequest, genericResponse);
        } else if (requestURI.equals("/listattachments")) {
            attachmentWebHandler.prepareListAttachments(genericRequest, genericResponse);
        } else if (requestURI.equals("/myfavoritethread")) {
            threadWebHandler.prepareList_inFavorite(genericRequest);
        } else if (requestURI.equals("/addfavoritethreadprocess")) {
            favoriteThreadWebHandler.processAdd(genericRequest);
        } else if (requestURI.equals("/deletefavoritethreadprocess")) {
            favoriteThreadWebHandler.processDelete(genericRequest);
        } else if (requestURI.equals("/registermember")) {
            memberWebHandler.prepareAdd(genericRequest);
        } else if (requestURI.equals("/registermemberprocess")) {
            memberWebHandler.processAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/viewmember")) {
            memberWebHandler.prepareView_forPublic(genericRequest);
        } else if (requestURI.equals("/listmembers")) {
            memberWebHandler.prepareListMembers_forPublic(genericRequest);
        } else if (requestURI.equals("/editmember")) {
            memberWebHandler.prepareEdit_forCurrentMember(genericRequest);
        } else if (requestURI.equals("/updatemember")) {
            memberWebHandler.processUpdate(genericRequest, genericResponse);
        } else if (requestURI.equals("/myprofile")) {
            memberWebHandler.prepareView_forCurrentMember(genericRequest);
        } else if (requestURI.equals("/changemypassword")) {
            onlineUser.getPermission().ensureIsAuthenticated();
        } else if (requestURI.equals("/changemypasswordprocess")) {
            memberWebHandler.processUpdatePassword(genericRequest);
        } else if (requestURI.equals("/changeemail")) {
            memberWebHandler.prepareEditEmail(genericRequest);
        } else if (requestURI.equals("/changeemailprocess")) {
            memberWebHandler.processUpdateEmail(genericRequest);
        } else if (requestURI.equals("/changesignature")) {
            memberWebHandler.prepareEditSignature(genericRequest);
        } else if (requestURI.equals("/changesignatureprocess")) {
            memberWebHandler.processUpdateSignature(genericRequest, genericResponse);
        } else if (requestURI.equals("/changeavatar")) {
            memberWebHandler.prepareEditAvatar(genericRequest);
        } else if (requestURI.equals("/uploadavatar")) {
            memberWebHandler.uploadAvatar(mainServlet.getServletConfig(), genericRequest);
        } else if (requestURI.equals("/updateavatar")) {
            memberWebHandler.updateMemberAvatar(genericRequest);
        } else if (requestURI.equals("/mywatch")) {
            watchWebHandler.prepareList(genericRequest);
        } else if (requestURI.equals("/addwatch")) {
            watchWebHandler.prepareAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/addwatchprocess")) {
            watchWebHandler.processAdd(genericRequest);
        } else if (requestURI.equals("/deletewatchprocess")) {
            watchWebHandler.processDelete(genericRequest);
        } else if (requestURI.equals("/editwatch")) {
            watchWebHandler.prepareEdit(genericRequest);
        } else if (requestURI.equals("/editwatchprocess")) {
            watchWebHandler.processEdit(genericRequest);
        } else if (requestURI.equals("/mymessage")) {
            messageWebHandler.prepareList(genericRequest);
        } else if (requestURI.equals("/addmessage")) {
            messageWebHandler.prepareAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/addmessageprocess")) {
            messageWebHandler.processAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/viewmessage")) {
            messageWebHandler.prepareViewMessage(genericRequest);
        } else if (requestURI.equals("/sendmessageprocess")) {
            messageWebHandler.processSendMessage(genericRequest);
        } else if (requestURI.equals("/deletemessageprocess")) {
            messageWebHandler.processDelete(genericRequest);
        } else if (requestURI.equals("/processmessage")) {
            messageWebHandler.processMessage(genericRequest);
        } else if (requestURI.equals("/sendmail")) {
            mailWebHandler.prepareSendMail(genericRequest);
        } else if (requestURI.equals("/sendmailprocess")) {
            mailWebHandler.sendEmailProcess(genericRequest);
        } else if (requestURI.equals("/addmessageattachment")) {
            pmAttachmentWebHandler.prepareAdd(genericRequest);
        } else if (requestURI.equals("/addmessageattachmentprocess")) {
            pmAttachmentWebHandler.processAdd(genericRequest, genericResponse);
        } else if (requestURI.equals("/getpmattachment")) {
            pmAttachmentWebHandler.downloadAttachment(request, response);
            return null;
        } else if (requestURI.equals("/mymessagefolder")) {
            messageFolderWebHandler.prepareList(genericRequest);
        } else if (requestURI.equals("/addmessagefolder")) {
            messageFolderWebHandler.prepareAdd(genericRequest);
        } else if (requestURI.equals("/addmessagefolderprocess")) {
            messageFolderWebHandler.processAdd(genericRequest);
        } else if (requestURI.equals("/deletemessagefolder")) {
            messageFolderWebHandler.prepareDelete(genericRequest);
        } else if (requestURI.equals("/deletemessagefolderprocess")) {
            messageFolderWebHandler.processDelete(genericRequest);
        } else if (requestURI.equals("/updatefolderorder")) {
            messageFolderWebHandler.processUpdateOrder(genericRequest);
        } else if (requestURI.equals("/search")) {
            postWebHandler.processSearch(genericRequest, genericResponse);
        } else if (requestURI.equals("/rsssummary")) {
            threadWebHandler.prepareRSSSummary(genericRequest, genericResponse);
        } else if (requestURI.equals("/atom")) {
            threadWebHandler.prepareListRSS(genericRequest);
        } else if (requestURI.equals("/rss")) {
            threadWebHandler.prepareListRSS(genericRequest);
        } else if (requestURI.equals("/rss2")) {
            threadWebHandler.prepareListRSS(genericRequest);
        } else if (requestURI.equals("/searchattachments")) {
            attachmentWebHandler.processSearchAttachments(genericRequest, genericResponse);
        } else if (requestURI.equals("/getmvncoreimage")) {
            MyUtil.writeMvnCoreImage(response);
            return null;
        } else if (requestURI.equals("/getmvnforumimage")) {
            MyUtil.writeMvnForumImage(response);
            return null;
        } else if (requestURI.equals("/captchaimage")) {
            MVNCaptchaService.getInstance().writeCaptchaImage(request, response);
            return null;
        } else if (requestURI.equals("/iforgotpasswords")) {
            memberWebHandler.prepareForgotPassword(genericRequest);
        } else if (requestURI.equals("/forgotpasswordprocess")) {
            memberWebHandler.forgotPassword(genericRequest);
        } else if (requestURI.equals("/resetpasswordprocess")) {
            memberWebHandler.resetPassword(genericRequest);
        } else if (requestURI.equals("/sendactivationcodeprocess")) {
            memberWebHandler.sendActivateCode(genericRequest);
        } else if (requestURI.equals("/activatememberprocess")) {
            memberWebHandler.activateMember(genericRequest);
        } else if (requestURI.equals("/listonlineusers")) {
            memberWebHandler.prepareListOnlineUsers(genericRequest, requestURI);
        } else if (requestURI.equals("/login")) {
            if (MVNForumConfig.getRedirectLoginURL().equals("default") == false) {
                // if not checking, we will have recursive bug  
                responseURI = MVNForumConfig.getRedirectLoginURL();
            }
        } else if (requestURI.equals("/loginprocess")) {
            if (MVNForumConfig.getEnableLogin() == false) {
                throw new AuthenticationException(NotLoginException.LOGIN_DISABLED);
            }
            onlineUserManager.processLogin(request, response);
            String referer = request.getParameter("referer");
            referer = StringUtil.getEmptyStringIfNull(referer);
            if (referer.length() > 0) {
                responseURI = referer;
            }
            String url = ParamUtil.getParameter(request, "url");
            if (onlineUserManager.getOnlineUser(request).isPasswordExpired()) {
                responseURI = UserModuleConfig.getUrlPattern() + "/changemypassword";
            } else if (url.length() > 0) {
                responseURI = url;
            } else {
                String originalRequest = ParamUtil.getAttribute(request.getSession(), ORIGINAL_REQUEST);
                if (originalRequest.length() > 0) {
                    genericRequest.setSessionAttribute(ORIGINAL_REQUEST, "");
                    responseURI = originalRequest;
                }
            }
        } else if (requestURI.equals("/logout")) {
            onlineUserManager.logout(request, response);
            request.setAttribute("Reason", "Logout successfully.");
            if (MVNForumConfig.getRedirectLogoutURL().equals("default") == false) {
                // if not checking, we will have recursive bug  
                responseURI = MVNForumConfig.getRedirectLogoutURL();
            }
        } else if (requestURI.equals("/deletecookieprocess")) {
            SecurityUtil.checkHttpReferer(request);
            onlineUserManager.deleteCookie(response);
        } else if (requestURI.equals("/getavatar")) {
            memberWebHandler.getAvatar(request, response);
            return null;
        }
    } catch (AuthenticationException e) {
        if (e.getReason() == NotLoginException.NOT_ACTIVATED) {
            requestURI = "/sendactivationcode";
        } else {
            // make sure not from login page, we cannot set original request in this situation  
            // and also make sure the request's method must be GET to set the OriginalRequest  
            boolean shouldSaveOriginalRequest = (e.getReason() == NotLoginException.NOT_LOGIN) || (e.getReason() == NotLoginException.NOT_ENOUGH_RIGHTS);
            if (shouldSaveOriginalRequest && (request.getMethod().equals("GET"))) {
                String url = UserModuleConfig.getUrlPattern() + requestURI;
                if (request.getQueryString() != null) {
                    url = url + "?" + request.getQueryString();
                }
                request.getSession().setAttribute(ORIGINAL_REQUEST, url);
            }
            if (MVNForumConfig.getRedirectLoginURL().equals(MVNForumConfig.DEFAULT)) {
                requestURI = "/login";
            } else {
                requestURI = MVNForumConfig.getRedirectLoginURL();
            }
            request.setAttribute("Reason", e.getReasonExplanation(onlineUser.getLocale()));
        }
    } catch (Throwable e) {
        if (e instanceof BadInputException) {
            // we log in WARN level if this is the exception from user input  
            log.warn("Exception in UserModuleProcessor e = " + e.getMessage(), e);
        } else if (e instanceof AssertionError) {
            // we log in FATAL level if this is the exception from user input  
            log.fatal("Exception in UserModuleProcessor e = " + e.getMessage(), e);
        } else {
            log.error("Exception in UserModuleProcessor [" + e.getClass().getName() + "] : " + e.getMessage(), e);
        }
        requestURI = "/error";
        String message = StringUtil.getEmptyStringIfNull(e.getMessage());
        if (message.length() == 0) {
            message = e.getClass().getName();
        }
        request.getSession().setAttribute("ErrorMessage", DisableHtmlTagFilter.filter(message));
    }
    // step 2: map the URI (of the CONTROLLER)  
    try {
        // NOTE 1:  there is one situation when responseURI != null (after login successfully for the first time),  
        //          but since it will make a NEW request via sendRedirect, so we don't count  
        // NOTE 2:  there are 2 situation when requestURI is different from the original requestURI  
        //          that is /login and /error, because of this so we must pass the requestURI  
        /* @todo Could below the MapHandler ??? */
        Action action = new ActionInUserModule(genericRequest, requestURI);
        // may throw MissingURLMapEntryException  
        onlineUserManager.updateOnlineUserAction(genericRequest, action);
        // now updateOnlineUserAction is ok, we go ahead  
        if (responseURI == null) {
            URLMap map = urlMapHandler.getMap(requestURI, genericRequest, onlineUser.getLocaleName());
            responseURI = map.getResponse();
        }
    } catch (MissingURLMapEntryException e) {
        log.error("Exception: missing urlmap entry in forum module: requestURI = " + requestURI);
        responseURI = "/mvnplugin/mvnforum/user/error.jsp";
        request.getSession().setAttribute("ErrorMessage", DisableHtmlTagFilter.filter(e.getMessage()));
    } catch (Throwable e) {
        // This will catch AuthenticationException, AssertionError, DatabaseException  
        // in the method onlineUserManager.updateOnlineUserAction(request, action)  
        responseURI = "/mvnplugin/mvnforum/user/error.jsp";
        request.getSession().setAttribute("ErrorMessage", DisableHtmlTagFilter.filter(e.getMessage()));
    }
    // step 3: forward or dispatch to the VIEW  
    if (log.isDebugEnabled()) {
        long duration = System.currentTimeMillis() - start;
        log.debug("UserModuleProcessor : responseURI = " + responseURI + ". (" + duration + " ms)");
    }
    return responseURI;
}
