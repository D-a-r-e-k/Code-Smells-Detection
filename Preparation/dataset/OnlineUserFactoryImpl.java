/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/OnlineUserFactoryImpl.java,v 1.52 2008/06/27 10:24:37 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.52 $
 * $Date: 2008/06/27 10:24:37 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.auth;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.db.*;

public class OnlineUserFactoryImpl implements OnlineUserFactory {

    private static final Log log = LogFactory.getLog(OnlineUserFactoryImpl.class);

    public OnlineUserFactoryImpl() {
    }

    public OnlineUser getAuthenticatedUser(HttpServletRequest request,
                                           HttpServletResponse response,
                                           String loginName, String password,
                                           boolean isEncodedPassword)
        throws AuthenticationException, DatabaseException {

        GenericRequest req = new GenericRequestServletImpl(request);

        return getAuthenticatedUser(req, null, loginName, password, isEncodedPassword);
    }

    public OnlineUser getAuthenticatedUser(GenericRequest request,
                                           GenericResponse response,
                                           String loginName, String password,
                                           boolean isEncodedPassword)
        throws AuthenticationException, DatabaseException {

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
                if (memberID != MVNForumConstant.MEMBER_ID_OF_ADMIN) {// Admin cannot be disabled
                    throw new AuthenticationException(NotLoginException.ACCOUNT_DISABLED);
                }
            }
            boolean enablePortlet = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet();
            if (enablePortlet == false) {

                if (DAOFactory.getMemberDAO().getActivateCode(memberID).equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED) == false) {
                    // not activated
                    if (MVNForumConfig.getRequireActivation()) {
                        if (memberID != MVNForumConstant.MEMBER_ID_OF_ADMIN) {// Admin don't have to activate to login
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
            invisible  = memberBean.isInvisible();
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

            OnlineUserImpl authenticatedUser = new OnlineUserImpl(request, false/*isGuest*/);

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
            throw new AuthenticationException(NotLoginException.WRONG_NAME);//we don't want this line to happen
        } catch (DatabaseException e) {
            MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().setShouldRun(false, "Assertion in OnlineUserFactoryImpl.");
            log.error("Unexpected error validating user", e);
            throw new AuthenticationException(NotLoginException.NOT_LOGIN);//we don't want this line to happen
        }
    }

    public OnlineUser getAnonymousUser(HttpServletRequest req)
        throws DatabaseException {

        GenericRequest request = new GenericRequestServletImpl(req);
        return this.getAnonymousUser(request);
    }

    public OnlineUser getAnonymousUser(GenericRequest request)
        throws DatabaseException {

        int memberID = MVNForumConstant.MEMBER_ID_OF_GUEST;
        String memberName = MVNForumConfig.getDefaultGuestName();
        double timeZone = MVNForumConfig.getDefaultGuestTimeZone();
        String localeName = "";
        Timestamp lastLogon = null;
        String lastLogonIP = null;
        int postsPerPage = MVNForumConfig.getRowsPerPage();
        
        // we define this constant here for improve performance. Later if we support 
        // Guest in database, then we change below variable to true (in this case, we should cache)
        final boolean GUEST_IN_DATABASE = false;

        try {
            MemberBean memberBean;
            if (GUEST_IN_DATABASE) {
                memberBean = DAOFactory.getMemberDAO().getMember(memberID);
            } else {
                throw new ObjectNotFoundException("Guest does not in database");
            }
            if (memberBean.getMemberStatus() != MemberBean.MEMBER_STATUS_ENABLE) {
                //@todo: for now, Guest is always enabled
            }
            memberName = memberBean.getMemberName();
            timeZone = memberBean.getMemberTimeZone();
            localeName = memberBean.getMemberLanguage();
            lastLogon = memberBean.getMemberLastLogon();
            postsPerPage = memberBean.getMemberPostsPerPage();
            lastLogonIP = memberBean.getMemberLastIP();

            //@todo: Should we update LastLogon? I think we should, so we know when we had last guest visiting the site.
            Timestamp now = DateUtil.getCurrentGMTTimestamp();
            //@todo should we remember these information for the Guest
            DAOFactory.getMemberDAO().updateLastLogon(memberID, now, request.getRemoteAddr());

            OnlineUserImpl anonymousUser = new OnlineUserImpl(request, true/*isGuest*/);
            anonymousUser.setMemberID(memberID);
            anonymousUser.setMemberName(memberName);
            anonymousUser.setTimeZone(timeZone);
            MVNForumPermission permission = MVNForumPermissionFactory.getAnonymousPermission();
            anonymousUser.setPermission(permission);
            anonymousUser.setLocaleName(localeName);
            anonymousUser.setLastLogonTimestamp(lastLogon);
            anonymousUser.setLastLogonIP(lastLogonIP);
            //no gender; anonymousUser.setGender(memberBean.getMemberGender() != 0);
            anonymousUser.setPostsPerPage(postsPerPage);
            return anonymousUser;
        } catch (ObjectNotFoundException e) {
            OnlineUserImpl anonymousUser = new OnlineUserImpl(request, true/*isGuest*/);
            //anonymousUser.setMemberID(MVNForumConstant.MEMBER_ID_OF_GUEST);
            //anonymousUser.setMemberName(MVNForumConfig.getDefaultGuestName());
            MVNForumPermission permission = MVNForumPermissionFactory.getAnonymousPermission();
            anonymousUser.setPermission(permission);
            anonymousUser.setLocaleName("");
            anonymousUser.setLastLogonTimestamp(new Timestamp(0));
            anonymousUser.setPostsPerPage(postsPerPage);
            anonymousUser.setTimeZone(timeZone);
            return anonymousUser;
        } catch (DatabaseException e) {
            OnlineUserImpl anonymousUser = new OnlineUserImpl(request, true/*isGuest*/);
            //anonymousUser.setMemberID(MVNForumConstant.MEMBER_ID_OF_GUEST);
            //anonymousUser.setMemberName(MVNForumConfig.getDefaultGuestName());
            MVNForumPermission permission = MVNForumPermissionFactory.getAnonymousPermission();
            anonymousUser.setPermission(permission);
            anonymousUser.setLocaleName("");
            anonymousUser.setLastLogonTimestamp(new Timestamp(0));
            anonymousUser.setPostsPerPage(postsPerPage);
            anonymousUser.setTimeZone(timeZone);
            return anonymousUser;
        }
    }

    public void postLogin(HttpServletRequest request, HttpServletResponse response, OnlineUser onlineUser)
        throws DatabaseException {
        
        if (onlineUser.isGuest()) {
            throw new IllegalStateException("Cannot process a postLogin for a Guest.");
        }

        // We create default Message Folder for this user
        int folderOption = 0;
        int folderType = 0;
        int folderStatus = 0;
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        try {
            try {
                DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, onlineUser.getMemberID(),
                                            0/*folderOrder*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException ex) {
                // Already existed, just go ahead
            }

            try {
                DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_DRAFT, onlineUser.getMemberID(),
                                            1/*folderOrder*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException ex) {
                // Already existed, just go ahead
            }

            try {
                DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, onlineUser.getMemberID(),
                                            2/*folderOrder*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException ex) {
                // Already existed, just go ahead
            }

            try {
                DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_TRASH, onlineUser.getMemberID(),
                                            3/*folderOrder*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException ex) {
                // Already existed, just go ahead
            }
        } catch (CreateException ce) {
            throw new DatabaseException("Cannot created Message Folder.");
        } catch (ForeignKeyNotFoundException fe) {
            throw new DatabaseException("Cannot created Message Folder because the foreign key is not existed.");
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        //do nothing
    }

    public void logout(GenericRequest request, GenericResponse response) {
        //do nothing
    }

    public String getEncodedPassword(String loginName, String password) {
        return Encoder.getMD5_Base64(password);
    }

    public boolean validatePassword(String loginName, String password, boolean isEncodedPassword)
        throws AuthenticationException {

        try {
            int memberId = DAOFactory.getMemberDAO().getMemberIDFromMemberName(loginName);
            if ((memberId == 0) || (memberId == MVNForumConstant.MEMBER_ID_OF_GUEST)) {
                return true;
            }

            String encodedPassword;
            if (isEncodedPassword) {
                encodedPassword = password;
            } else {
                encodedPassword = getEncodedPassword(loginName, password);
            }

            if (isEncodedPassword && password.equals(OnlineUserManager.PASSWORD_OF_METHOD_REALM)) {
                if (MVNForumConfig.getEnableLoginInfoInRealm()) {
                    return true;
                }
            }
            if (isEncodedPassword && password.equals(OnlineUserManager.PASSWORD_OF_METHOD_CAS)) {
                if (MVNForumConfig.getEnableLoginInfoInCAS()) {
                    return true;
                }
            }
            if (isEncodedPassword && password.equals(OnlineUserManager.PASSWORD_OF_METHOD_CUSTOMIZATION)) {
                if (MVNForumConfig.getEnableLoginInfoInCustomization()) {
                    return true;
                }
            }
            return encodedPassword.equals(DAOFactory.getMemberDAO().getPassword(memberId));
        } catch (ObjectNotFoundException e) {
            throw new AuthenticationException(NotLoginException.WRONG_NAME);
        } catch (Exception e) {
            /** @todo find a better one than NotLoginException.NOT_LOGIN */
            throw new AuthenticationException(NotLoginException.NOT_LOGIN);
        }
    }

    public void ensureCorrectPassword(String loginName, String password, boolean isEncodedPassword)
        throws AuthenticationException {

        boolean isCorrectPassword = validatePassword(loginName, password, isEncodedPassword);
        if (isCorrectPassword == false) {
            throw new AuthenticationException(NotLoginException.WRONG_PASSWORD);
        }
    }
}
