/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/PmAttachmentWebHandler.java,v 1.73 2008/06/01 17:22:07 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.73 $
 * $Date: 2008/06/01 17:22:07 $
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

import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.common.PrivateMessageUtil;
import com.mvnforum.db.*;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.*;
import net.myvietnam.mvncore.web.fileupload.FileItem;
import net.myvietnam.mvncore.web.fileupload.FileUploadException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PmAttachmentWebHandler {

    private static final Log log = LogFactory.getLog(PmAttachmentWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private URLResolverService urlResolverService = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    private BinaryStorageService binaryStorageService = MvnCoreServiceFactory.getMvnCoreService().getBinaryStorageService();

    private FileUploadParserService fileUploadParserService = MvnCoreServiceFactory.getMvnCoreService().getFileUploadParserService();

    public PmAttachmentWebHandler() {
    }

    public void prepareAdd(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnableMessageAttachment() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.message_attachment_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Attachment because Attachment in Private Message feature is disabled by administrator.");  // localization
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();
        permission.ensureCanAddMessageAttachment();

        int messageID  = GenericParamUtil.getParameterInt(request, "message");
        MessageBean messageBean = null;
        try {
            messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT) == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_add_attachment.pm_does_not_in_folder_draft");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot add attachment because this Private Message does not in the folder Draft");
        }

        if (messageBean.getMemberID() != onlineUser.getMemberID() ) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("This Private Message does not belong to you");
        }

        request.setAttribute("MessageBean", messageBean);
    }

    public void processAdd(GenericRequest request, GenericResponse response)
        throws BadInputException, CreateException, DatabaseException, IOException, ForeignKeyNotFoundException,
        AuthenticationException, ObjectNotFoundException, InterceptorException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnableMessageAttachment() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.message_attachment_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Attachment because Attachment in Private Message feature is disabled by administrator.");  // localization
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
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_upload", new Object[] {ex.getMessage()});
            throw new IOException(localizedMessage);
            //throw new IOException("Cannot upload. Detailed reason: " + ex.getMessage());
        }

        // values that must get from the form
        int messageID               = 0;
        String attachFilename       = null;
        int attachFileSize          = 0;
        String attachMimeType       = null;
        String attachDesc           = null;
        boolean attachMore          = false;
        boolean markAsQuote         = false;
        boolean addToSentFolder     = false;

        FileItem attachFileItem = null;

        String actionParam = urlResolverService.getActionParam();
        for (int i = 0; i < fileItems.size(); i++ ) {
            FileItem currentFileItem = (FileItem)fileItems.get(i);
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
                //ignore
            } else if (fieldName.equals("AttachFilename")) {
                if (currentFileItem.isFormField()) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_uploaded_attach_file_with_form_field");
                    throw new AssertionError(localizedMessage);
                    //throw new AssertionError("Cannot process uploaded attach file with a form field.");
                }
                attachMimeType = currentFileItem.getContentType();
                attachMimeType = DisableHtmlTagFilter.filter(attachMimeType);
                attachFileSize = (int)currentFileItem.getSize();
                if (attachFileSize == 0) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_process_upload_with_file_size_is_zero");
                    throw new BadInputException(localizedMessage);
                    //throw new BadInputException("Cannot process an attach file with size = 0. Please check the file size or check if your file is missing.");
                }
                String fullFilePath = currentFileItem.getName();
                attachFilename = FileUtil.getFileName(fullFilePath);
                attachFilename = DisableHtmlTagFilter.filter(attachFilename);
                log.debug("attachFilename = " + attachFilename);

                // now save to attachFileItem
                attachFileItem = currentFileItem;
            } else if (fieldName.equals(actionParam)) {
                // ignore
            } else {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_field_name", new Object[] {fieldName});
                throw new AssertionError(localizedMessage);
                //throw new AssertionError("Cannot process field name = " + fieldName);
            }
        } // end for
        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        // check constraint
        MessageBean messageBean = null;
        try {
            messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT) == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_add_attachment.pm_does_not_in_folder_draft");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot add attachment because this Private Message does not in the folder Draft");
        }

        int logonMemberID = onlineUser.getMemberID();

        // Check if the message is from this member
        if (messageBean.getMemberID() != logonMemberID ) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("This Private Message does not belong to you");
        }

        // Check if the message is from this logon member
        AssertionUtil.doAssert(messageBean.getMessageSenderID() == logonMemberID, "Assertion: The MessageSenderID must equals the current logined user.");
        AssertionUtil.doAssert(messageBean.getMessageSenderName().equals(onlineUser.getMemberName()), "Assertion: The MessageSenderName must equals the current logined user.");
        //String logonMemberName  = onlineUser.getMemberName();

        // now all contraints/permission have been checked
        // values that we can init now
        String creationIP           = request.getRemoteAddr();
        Timestamp attachCreationDate= now;
        Timestamp attachModifiedDate= now;
        int attachDownloadCount     = 0;
        int attachOption            = 0;// check it
        int attachStatus            = 0;// check it
        int attachID = DAOFactory.getPmAttachmentDAO().create(logonMemberID, attachFilename,
                                                     attachFileSize, attachMimeType, attachDesc,
                                                     creationIP, attachCreationDate, attachModifiedDate,
                                                     attachDownloadCount, attachOption, attachStatus);
        try {
            DAOFactory.getPmAttachMessageDAO().create(messageID, attachID, 0/*type*/, 0/*option*/, 0/*status*/);
        } catch (DuplicateKeyException ex) {
            // this should never happen
            AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
        }

        try {
//            String filename = AttachmentUtil.getPmAttachFilenameOnDisk(attachID);
//            log.debug("Message's attachment filename to save to file system = " + filename);
//            attachFileItem.write(new File(filename));
            binaryStorageService.storeData(BinaryStorageService.CATEGORY_PM_ATTACHMENT, String.valueOf(attachID), attachFilename,
                                    attachFileItem.getInputStream(), attachFileSize, 0, 0, attachMimeType, creationIP);

        } catch (Exception ex) {
            log.error("Cannot save the attachment file", ex);
            DAOFactory.getPmAttachMessageDAO().delete(messageID, attachID);
            DAOFactory.getPmAttachmentDAO().delete(attachID);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_save_attach_file");
            throw new IOException(localizedMessage);
            //throw new IOException("Cannot save the attachment file to the file system.");
        }

        // Update AttachCount in Message table
        int attachCount = DAOFactory.getPmAttachMessageDAO().getNumberOfBeans_inMessage(messageID);

        try {
            DAOFactory.getMessageDAO().updateAttachCount(messageID, attachCount);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
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
            Collection attachBeans = DAOFactory.getPmAttachmentDAO().getPmAttachments_inMessage(messageBean.getMessageID()); //messageBean is original message

            StringBuffer overQuotaReceivers = new StringBuffer(128);
            for (Enumeration enumeration = receivers.keys(); enumeration.hasMoreElements(); ) {
                int receivedMemberID = ((Integer)enumeration.nextElement()).intValue();
                String receivedMemberName = (String)receivers.get(new Integer(receivedMemberID));

                int receiverMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(receivedMemberID);
                if (receiverMessageCount >= maxPrivateMessage) {
                    if (overQuotaReceivers.length() > 0) {
                        overQuotaReceivers.append(", ");
                    }
                    overQuotaReceivers.append(receivedMemberName);
                    continue;
                }

                // Create REAL message for receivers when finish. It means we have new messageID for each new receiver
                int eachMessageID = DAOFactory.getMessageDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, receivedMemberID, logonMemberID,
                                                    messageBean.getMessageSenderName(), messageBean.getMessageToList(), messageBean.getMessageCcList(),
                                                    messageBean.getMessageBccList(), messageBean.getMessageTopic(), messageBean.getMessageBody(),
                                                    messageType, messageBean.getMessageOption(), messageBean.getMessageStatus(),
                                                    MessageBean.MESSAGE_READ_STATUS_DEFAULT, messageBean.getMessageNotify(), messageBean.getMessageIcon(),
                                                    attachCount, creationIP, now);

                // Add to statistics
                if (logonMemberID != receivedMemberID) {
                    DAOFactory.getMessageStatisticsDAO().create(logonMemberID, receivedMemberID, now,
                            messageBean.getMessageAttachCount(), messageBean.getMessageType(),
                            messageBean.getMessageOption(), messageBean.getMessageStatus());
                }
                // We must create a loop to create Attach for many receivers and many attachments
                for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                    PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                    try {
                        DAOFactory.getPmAttachMessageDAO().create(eachMessageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                    } catch (DuplicateKeyException ex) {
                        // this should never happen
                        AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                    }
                }
            } // end of for on receivers
            request.setAttribute("OverQuotaReceivers", overQuotaReceivers.toString());

            if (addToSentFolder) {
                int senderMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(logonMemberID);
                if (senderMessageCount < maxPrivateMessage) {
                    messageType = MessageBean.MESSAGE_TYPE_DEFAULT;// always a default type in the Sent folder
                    int sentMessageID = DAOFactory.getMessageDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, logonMemberID, logonMemberID,
                            messageBean.getMessageSenderName(), messageBean.getMessageToList(), messageBean.getMessageCcList(),
                            messageBean.getMessageBccList(), messageBean.getMessageTopic(), messageBean.getMessageBody(),
                            messageType, messageBean.getMessageOption(), messageBean.getMessageStatus(),
                            MessageBean.MESSAGE_READ_STATUS_DEFAULT, messageBean.getMessageNotify(), messageBean.getMessageIcon(),
                            attachCount, creationIP, now);

                    for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                        PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                        try {
                            DAOFactory.getPmAttachMessageDAO().create(sentMessageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                        } catch (DuplicateKeyException ex) {
                            // this should never happen
                            AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                        }
                    }
                } else {
                    request.setAttribute("AddSentFolderOverQuota", Boolean.TRUE);
                }
            }// if add to sent folder

            // Now delete the message in the draft
            PrivateMessageUtil.deleteMessageInDatabase(messageID, logonMemberID);
        }// if not attach more file

        request.setAttribute("MessageID", String.valueOf(messageID));
        request.setAttribute("AttachMore", Boolean.valueOf(attachMore));
        request.setAttribute("AddToSentFolder", Boolean.valueOf(addToSentFolder));
        request.setAttribute("MarkAsQuote", Boolean.valueOf(markAsQuote));
    }

    public void downloadAttachment(HttpServletRequest request, HttpServletResponse response)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        IOException, AuthenticationException  {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnableMessageAttachment() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.message_attachment_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Attachment because Attachment in Private Message feature is disabled by administrator.");  // localization
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        int attachID  = ParamUtil.getParameterInt(request, "attach");
        int messageID  = ParamUtil.getParameterInt(request, "message");

        PmAttachmentBean pmAttachBean = null;
        try {
            pmAttachBean = DAOFactory.getPmAttachmentDAO().getPmAttachment(attachID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.attachmentid_not_exists", new Object [] {new Integer(attachID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        MessageBean messageBean = null;
        try {
            messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // Check if the message is from this member
        boolean isPublicMessage = (messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_PUBLIC);

        if ((messageBean.getMemberID() != onlineUser.getMemberID()) && !isPublicMessage) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("This Private Message does not belong to you");
        }

        // check if the attachment is really in this message
        DAOFactory.getPmAttachMessageDAO().findByPrimaryKey(messageID, attachID);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            /*String attachFilename = AttachmentUtil.getPmAttachFilenameOnDisk(attachID);
            File attachFile = new File(attachFilename);
            if ((!attachFile.exists()) || (!attachFile.isFile())) {
                log.error("Can't find a file " + attachFile + " to be downloaded (or maybe it's directory).");
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.not_exist_or_not_file_to_be_downloaded");
                throw new IOException(localizedMessage + " (PmAttachID=" + attachID + ")");
                //throw new IOException("Can't find a file to be downloaded (or maybe it's directory).");
            }*/

            // we should not call this method after done the outputStream
            // because we don't want exception after download
            try {
                DAOFactory.getPmAttachmentDAO().increaseDownloadCount(attachID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.attachmentid_not_exists", new Object[] {new Integer(attachID)});
                throw new ObjectNotFoundException(localizedMessage);
            }

            response.setContentType(pmAttachBean.getPmAttachMimeType());
            response.setHeader("Location", pmAttachBean.getPmAttachFilename());

            // now use Cache-Control if the MIME type are image
            if (pmAttachBean.getPmAttachMimeType().startsWith("image/")) {
                long cacheTime = DateUtil.DAY * 30 / 1000;// 30 days
                response.setHeader("Cache-Control", "max-age=" + cacheTime);
            }

            //response.setHeader("Content-Disposition", "attachment; filename=" + pmAttachBean.getPmAttachFilename());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(pmAttachBean.getPmAttachFilename(), "UTF-8") + "\"");

            //outputStream.write(buffer);
            try {
                inputStream = binaryStorageService.getInputStream(BinaryStorageService.CATEGORY_PM_ATTACHMENT, String.valueOf(attachID), null);

                outputStream = response.getOutputStream();
                //FileUtil.popFile(attachFile, outputStream);
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException ex) {
                // cannot throw Exception after we output to the response
                log.error("Error while trying to send PM attachment file from server: attachID = " + attachID + ".", ex);
            }
            outputStream.flush();
            outputStream.close();
            outputStream = null;// no close twice
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) { }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) { }
            }
        }
    }

    public void deleteOrphanPmAttachment() throws DatabaseException {

        Collection attachBeans = DAOFactory.getPmAttachmentDAO().getOrphanPmAttachments();

        // now checking if they DO be orphan
        for (Iterator iter = attachBeans.iterator(); iter.hasNext(); ) {
            PmAttachmentBean pmAttachmentBean = (PmAttachmentBean)iter.next();
            int pmAttachID = pmAttachmentBean.getPmAttachID();
            int messageCount = DAOFactory.getPmAttachMessageDAO().getNumberOfBeans_inPmAttach(pmAttachID);
            AssertionUtil.doAssert(messageCount <= 0, "This PmAttachID [" + pmAttachID + "] is not orphan because MessageCount = " + messageCount + ". Please report this to mvnForum Developers");
        }

        // Now checking correct orphan is done, go ahead and delete the PmAttachment
        for (Iterator iter = attachBeans.iterator(); iter.hasNext(); ) {
            PmAttachmentBean pmAttachmentBean = (PmAttachmentBean)iter.next();
            int pmAttachID = pmAttachmentBean.getPmAttachID();
            log.debug("About to delete orphan PmAttachment with ID = " + pmAttachID);

            //this method already catch the exception
            //AttachmentUtil.deletePmAttachFilenameOnDisk(pmAttachID);
            try {
                binaryStorageService.deleteData(BinaryStorageService.CATEGORY_PM_ATTACHMENT, String.valueOf(pmAttachID), null);
            } catch (IOException e) {
                // exception should not occur
                log.warn("Cannot BinaryStorage.deleteData with PmAttachID = " + pmAttachID, e);
            }

            try {
                DAOFactory.getPmAttachmentDAO().delete(pmAttachID);
            } catch (Exception ex) {
                log.warn("Cannot delete message attachment in database with PmAttachID = " + pmAttachID, ex);
            }
        }
    }
}
