public OnlineUser getAuthenticatedUser(GenericRequest request, GenericResponse response, String loginName, String password, boolean isEncodedPassword) throws AuthenticationException, DatabaseException {
    int memberID = 0;
    double timeZone = 0;
    boolean invisible = false;
    String localeName = "";
    Timestamp lastLogon = null;
    String lastLogonIP = null;
    int postsPerPage = 10;
    try {
        memberID = MemberCache.getInstance().getMemberIDFromMemberName(loginName);
    } catch (ObjectNotFoundException e) {
        throw new AuthenticationException(NotLoginException.WRONG_NAME);
    } catch (Exception e) {
        log.error("Unexpected error validating user", e);
        /** @todo find a better one than NotLoginException.NOT_LOGIN */
        throw new AuthenticationException(NotLoginException.NOT_LOGIN);
    }
    try {
        MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        if (memberBean.getMemberStatus() != MemberBean.MEMBER_STATUS_ENABLE) {
            if (memberID != MVNForumConstant.MEMBER_ID_OF_ADMIN) {
                // Admin cannot be disabled  
                throw new AuthenticationException(NotLoginException.ACCOUNT_DISABLED);
            }
        }
        boolean enablePortlet = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet();
        if (enablePortlet == false) {
            if (DAOFactory.getMemberDAO().getActivateCode(memberID).equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED) == false) {
                // not activated  
                if (MVNForumConfig.getRequireActivation()) {
                    if (memberID != MVNForumConstant.MEMBER_ID_OF_ADMIN) {
                        // Admin don't have to activate to login  
                        throw new AuthenticationException(NotLoginException.NOT_ACTIVATED);
                    }
                }
            }
            if (validatePassword(loginName, password, isEncodedPassword) == false) {
                if ((MVNForumConfig.getEnablePasswordlessAuth() == false) || (password.length() > 0)) {
                    throw new AuthenticationException(NotLoginException.WRONG_PASSWORD);
                }
            }
        }
        if ((request.getRemoteAddr() != null) && (request.getRemoteAddr().equals(MVNCoreGlobal.UN_KNOWN_IP) == false)) {
            // now we have checked the authentication, then we update the lastlogon date  
            Timestamp now = DateUtil.getCurrentGMTTimestamp();
            DAOFactory.getMemberDAO().updateLastLogon(memberID, now, request.getRemoteAddr());
        }
        timeZone = memberBean.getMemberTimeZone();
        localeName = memberBean.getMemberLanguage();
        lastLogon = memberBean.getMemberLastLogon();
        postsPerPage = memberBean.getMemberPostsPerPage();
        lastLogonIP = memberBean.getMemberLastIP();
        invisible = memberBean.isInvisible();
        Timestamp creationDate = memberBean.getMemberCreationDate();
        Timestamp expireDate = memberBean.getMemberPasswordExpireDate();
        // check password is expired or not  
        boolean passwordExpired = false;
        if (MVNForumConfig.getMaxPasswordDays() == 0) {
            passwordExpired = false;
        } else {
            if (expireDate == null) {
                expireDate = creationDate;
                passwordExpired = true;
            }
            if (expireDate.after(creationDate)) {
                if (DateUtil.getCurrentGMTTimestamp().after(expireDate)) {
                    passwordExpired = true;
                }
            }
        }
        // next, get the correct name from database  
        // Eg: if in database the MemberName is "Admin", and user enter "admin"  
        // We will convert "admin" to "Admin"  
        String memberName = memberBean.getMemberName();
        OnlineUserImpl authenticatedUser = new OnlineUserImpl(request, false);
        authenticatedUser.setMemberID(memberID);
        authenticatedUser.setPasswordExpired(passwordExpired);
        authenticatedUser.setMemberName(memberName);
        authenticatedUser.setInvisible(invisible);
        authenticatedUser.setTimeZone(timeZone);
        //NOTE: This MUST be the only way to get permission for a member,  
        // so we prevent getPermission for one user and set for other user  
        MVNForumPermission permission = MVNForumPermissionFactory.getAuthenticatedPermission(memberBean);
        authenticatedUser.setPermission(permission);
        authenticatedUser.setLocaleName(localeName);
        authenticatedUser.setLastLogonTimestamp(lastLogon);
        authenticatedUser.setLastLogonIP(lastLogonIP);
        authenticatedUser.setGender(memberBean.getMemberGender() != 0);
        authenticatedUser.setPostsPerPage(postsPerPage);
        return authenticatedUser;
    } catch (ObjectNotFoundException e) {
        throw new AuthenticationException(NotLoginException.WRONG_NAME);
    } catch (DatabaseException e) {
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().setShouldRun(false, "Assertion in OnlineUserFactoryImpl.");
        log.error("Unexpected error validating user", e);
        throw new AuthenticationException(NotLoginException.NOT_LOGIN);
    }
}
