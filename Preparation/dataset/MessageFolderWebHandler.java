/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/MessageFolderWebHandler.java,v 1.31.2.1 2008/11/17 07:56:16 nguyendnc Exp $
 * $Author: nguyendnc $
 * $Revision: 1.31.2.1 $
 * $Date: 2008/11/17 07:56:16 $
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
package com.mvnforum.user;

import java.sql.Timestamp;
import java.util.*;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.common.PrivateMessageUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MessageFolderBean;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageFolderWebHandler {

    private static final Log log = LogFactory.getLog(MessageFolderWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public MessageFolderWebHandler() {
    }

    public void prepareAdd(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();
    }

    public void processAdd(GenericRequest request)
        throws CreateException, DuplicateKeyException, DatabaseException, BadInputException,
        AuthenticationException, ForeignKeyNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        int folderStatus = 0;
        int folderOption = 0;
        int folderType   = 0;
        Timestamp now    = DateUtil.getCurrentGMTTimestamp();
        String folderName = GenericParamUtil.getParameterSafe(request, "FolderName", true);
        StringUtil.checkGoodName(folderName);

        int folderOrder = DAOFactory.getMessageFolderDAO().getMaxFolderOrder(onlineUser.getMemberID());
        folderOrder++; // One value more than the current max value
        if (folderOrder < 10) folderOrder = 10; // Reserve order for special folders

        DAOFactory.getMessageFolderDAO().create(folderName, onlineUser.getMemberID(),
                                                folderOrder, folderStatus, folderOption, folderType, now, now);
    }

    public void prepareList(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
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

    public void prepareDelete(GenericRequest request)
        throws DatabaseException, AuthenticationException, BadInputException, ObjectNotFoundException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        String folderName = GenericParamUtil.getParameterSafe(request, "folder", true);
        StringUtil.checkGoodName(folderName);

        if ((folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_INBOX)) ||
            (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_SENT)) ||
            (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) ||
            (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_TRASH))) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_default_folder");
            throw new BadInputException(localizedMessage);
        }

        //Checking if this Folder belong to current Member
        MessageFolderBean messageFolderBean = null;
        try {
            messageFolderBean = DAOFactory.getMessageFolderDAO().getMessageFolder(folderName, onlineUser.getMemberID());
        } catch (ObjectNotFoundException onf) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messagefolder_not_exists", new Object[] {folderName});
            throw new ObjectNotFoundException(localizedMessage);
        }

        int numberOfMessages = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember_inFolder(onlineUser.getMemberID(), folderName);
        int numberOfUnreadMessages = DAOFactory.getMessageDAO().getNumberOfUnreadNonPublicMessages_inMember_inFolder(onlineUser.getMemberID(), folderName);

        request.setAttribute("MessageFolderBean", messageFolderBean);
        request.setAttribute("NumberOfMessages", new Integer(numberOfMessages));
        request.setAttribute("NumberOfUnreadMessages", new Integer(numberOfUnreadMessages));
    }

    public void processDelete(GenericRequest request)
        throws DatabaseException, AuthenticationException,
        BadInputException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        int memberID = onlineUser.getMemberID();
        String folderName = GenericParamUtil.getParameterSafe(request, "folder", true);
        StringUtil.checkGoodName(folderName);

        if ((folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_INBOX)) ||
            (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_SENT)) ||
            (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) ||
            (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_TRASH))) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_default_folder");
            throw new BadInputException(localizedMessage);
        }

        try {
           DAOFactory.getMessageFolderDAO().findByPrimaryKey(folderName, memberID);
        } catch (ObjectNotFoundException onf) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messagefolder_not_exists", new Object[] {folderName});
            throw new ObjectNotFoundException(localizedMessage);
        }

        PrivateMessageUtil.deleteMessageFolderInDatabase(folderName, memberID);
    }

    public void processUpdateOrder(GenericRequest request)
        throws DatabaseException, AuthenticationException,
        BadInputException, ObjectNotFoundException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        SecurityUtil.checkHttpReferer(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        int memberID = onlineUser.getMemberID();
        String action = GenericParamUtil.getParameterSafe(request, "action", true);
        String folderName = GenericParamUtil.getParameterSafe(request, "folder", true);
        StringUtil.checkGoodName(folderName);

        try {
           DAOFactory.getMessageFolderDAO().findByPrimaryKey(folderName, memberID);
        } catch (ObjectNotFoundException onf) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messagefolder_not_exists", new Object[] {folderName});
            throw new ObjectNotFoundException(localizedMessage);
        }

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        if (action.equals("up")) {
            DAOFactory.getMessageFolderDAO().decreaseFolderOrder(folderName, memberID, now);
        } else if (action.equals("down")) {
            DAOFactory.getMessageFolderDAO().increaseFolderOrder(folderName, memberID, now);
        } else {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_update_order.unknown_action", new Object[] {action});
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot update Order: unknown action: " + action);
        }
    }

}
