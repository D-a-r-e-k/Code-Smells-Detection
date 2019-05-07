/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/AttachmentWebHandler.java,v 1.128.2.1 2010/08/17 04:28:32 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.128.2.1 $
 * $Date: 2010/08/17 04:28:32 $
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

import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.fileupload.FileItem;
import net.myvietnam.mvncore.web.fileupload.FileUploadException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.categorytree.*;
import com.mvnforum.common.PostChecker;
import com.mvnforum.db.*;
import com.mvnforum.search.attachment.AttachmentIndexer;
import com.mvnforum.search.attachment.AttachmentSearchQuery;
import com.mvnforum.search.post.PostIndexer;
import com.mvnforum.service.*;

public class AttachmentWebHandler {

    private static final Log log = LogFactory.getLog(AttachmentWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();
    
    private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();

    private BinaryStorageService binaryStorageService = MvnCoreServiceFactory.getMvnCoreService().getBinaryStorageService();

    private FileUploadParserService fileUploadParserService = MvnCoreServiceFactory.getMvnCoreService().getFileUploadParserService();

    private URLResolverService urlResolverService = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    public AttachmentWebHandler() {
    }

    public void prepareAdd(GenericRequest request, GenericResponse response)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);
        if (MVNForumConfig.getEnableAttachment() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.attachment_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Attachment because Attachment feature is disabled by administrator.");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        /* was: permission.ensureIsAuthenticated();
         * That didn't allow guests to add attachments even if admin tried to
         * explicitly allow them to. So, we only need ensureCanAddAttachment(forumID),
         * and the admin will be responsible if he gets flooded (as he has to
         * explicitly allow them that anyway).
         * Same goes for processAdd() method below.
         */

        // primary key column(s)
        int postID  = GenericParamUtil.getParameterInt(request, "post");

        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);// can throw DatabaseException
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        int forumID = postBean.getForumID();
        ForumBean forumBean = ForumCache.getInstance().getBean(forumID);
        permission.ensureCanAddAttachment(forumID);

        forumBean.ensureNotDisabledForum(locale);
        forumBean.ensureNotLockedForum(locale);
        forumBean.ensureNotClosedForum(locale);

        // check edit constraints
        PostChecker.checkEditPost(onlineUser, postBean, forumBean);
        request.setAttribute("PostBean", postBean);

        String display = MVNForumResourceBundle.getString(locale, "mvnforum.user.addattachment.title");

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, null, null, display);
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

    }

    public void processAdd(GenericRequest request, GenericResponse response)
        throws BadInputException, CreateException, DatabaseException, IOException, ForeignKeyNotFoundException,
        AuthenticationException, ObjectNotFoundException, InterceptorException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnableAttachment() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.attachment_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Attachment because Attachment feature is disabled by administrator.");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        /* was: permission.ensureIsAuthenticated();
         * See prepareAdd() method above.
         */

        MyUtil.saveVNTyperMode(request, response);

        String tempDir = MVNForumConfig.getTempDir();
        log.debug("AttachmentWebHandler : process upload with temp dir = " + tempDir);

        final int UNLIMITED = -1;
        int sizeMax = permission.canAdminSystem() ? UNLIMITED : MVNForumConfig.getMaxAttachmentSize();
        int sizeThreshold = 100000;

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
        int offset                  = 0;
        int postID                  = 0;
        String attachFilename       = null;
        int attachFileSize          = 0;
        String attachMimeType       = null;
        String attachDesc           = null;
        ArrayList attachFileItems = new ArrayList();
        boolean attachMore = false;

        for (int i = 0; i < fileItems.size(); i++ ) {
            FileItem currentFileItem = (FileItem)fileItems.get(i);
            String fieldName = currentFileItem.getFieldName();
            if (fieldName.equals("offset")) {
                String content = currentFileItem.getString("utf-8");
                offset = Integer.parseInt(content);
                log.debug("offset = " + offset);
            } else if (fieldName.equals("AttachMore")) {
                String content = currentFileItem.getString("utf-8");
                attachMore = (content.length() > 0);
                log.debug("attachMore = " + attachMore);
            } else if (fieldName.equals("PostID")) {
                String content = currentFileItem.getString("utf-8");
                postID = Integer.parseInt(content);
                log.debug("postID = " + postID);
            } else if (fieldName.equals("AttachDesc")) {
                String content = currentFileItem.getString("utf-8");
                attachDesc = DisableHtmlTagFilter.filter(content);
                log.debug("attachDesc = " + attachDesc);
                attachDesc = InterceptorService.getInstance().validateContent(attachDesc);

            } else if (fieldName.equals("vnselector")) {
                //ignore
            } else if (fieldName.equals(urlResolverService.getActionParam())) {
                //ignore ACTION_PARAM if exists
            } else if (fieldName.startsWith("AttachFilename")) { // fields has prefix AttachFileName
            //else if (fieldName.equals("AttachFilename")) {
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

                // now store into attachFileItem
                attachFileItems.add(currentFileItem);
           } else {
                // maybe, we don't care about the redundant fields.
                // Should we uncomment the exception statement ?
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_field_name", new Object[] {fieldName});
                throw new AssertionError(localizedMessage);
                //throw new AssertionError("Cannot process field name = " + fieldName);
            }
        }

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        // check constraint
        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);// can throw DatabaseException
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int forumID = postBean.getForumID();
        ForumBean forumBean = ForumCache.getInstance().getBean(forumID);
        permission.ensureCanAddAttachment(forumID);

        forumBean.ensureNotDisabledForum(locale);
        forumBean.ensureNotLockedForum(locale);
        forumBean.ensureNotClosedForum(locale);

        int    logonMemberID    = onlineUser.getMemberID();
        PostChecker.checkEditPost(onlineUser, postBean, forumBean);

        // now all constraints/permission have been checked
        // values that we can initialize now
        String attachCreationIP     = request.getRemoteAddr();
        Timestamp attachCreationDate= now;
        Timestamp attachModifiedDate= now;
        int attachDownloadCount     = 0;
        int attachOption            = 0;// check it
        int attachStatus            = 0;// check it

        for (Iterator iter = attachFileItems.iterator(); iter.hasNext();) {
            FileItem currentFileItem = (FileItem) iter.next();

            String fullFilePath = currentFileItem.getName();
            attachFilename = FileUtil.getFileName(fullFilePath);
            attachFilename = DisableHtmlTagFilter.filter(attachFilename);

            now = DateUtil.getCurrentGMTTimestamp();
            attachCreationDate = now;
            attachModifiedDate = now;
            attachFileSize = (int)currentFileItem.getSize();
            attachMimeType = currentFileItem.getContentType();
            attachMimeType = DisableHtmlTagFilter.filter(attachMimeType);
            int attachID = DAOFactory.getAttachmentDAO().createAttachment(postID, logonMemberID, attachFilename,
                                                                          attachFileSize, attachMimeType, attachDesc,
                                                                          attachCreationIP, attachCreationDate, attachModifiedDate,
                                                                          attachDownloadCount, attachOption, attachStatus);
            try {
                binaryStorageService.storeData(BinaryStorageService.CATEGORY_POST_ATTACHMENT, String.valueOf(attachID), attachFilename,
                                        currentFileItem.getInputStream(), attachFileSize, 0, 0, attachMimeType, attachCreationIP);
            } catch (Exception ex) {
                log.error("Cannot save the attachment file", ex);
                DAOFactory.getAttachmentDAO().delete(attachID);
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_save_attach_file");
                throw new IOException(localizedMessage);
                //throw new IOException("Cannot save the attachment file to the file system.");
            }
            
            // now update the Lucene index
            AttachmentBean attachBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
            AttachmentIndexer.scheduleAddAttachmentTask(attachBean);
        }

        int threadID = postBean.getThreadID();
        int attachCount = DAOFactory.getAttachmentDAO().getNumberOfAttachments_inPost(postID);
        DAOFactory.getPostDAO().updateAttachCount(postID, attachCount);

        // Now update the post because the attachment count in this post is changed
        postBean.setPostAttachCount(attachCount);
        PostIndexer.scheduleUpdatePostTask(postBean);

        int attachCountInThread = DAOFactory.getAttachmentDAO().getNumberOfAttachments_inThread(threadID);
        DAOFactory.getThreadDAO().updateThreadAttachCount(threadID, attachCountInThread);

        // Now clear the cache
        PostCache.getInstance().clear();

        // we don't want the exception to throw below this
        request.setAttribute("ForumID", String.valueOf(forumID));
        request.setAttribute("ThreadID", String.valueOf(threadID));
        request.setAttribute("PostID", String.valueOf(postID));
        request.setAttribute("offset", String.valueOf(offset));
        request.setAttribute("AttachMore", Boolean.valueOf(attachMore));
    }
    
    public void addSuccessForRender(GenericRequest request, GenericResponse response) 
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {
        
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        int forumID = (Integer.valueOf((String)request.getAttribute("ForumID"))).intValue();

        String addSuccessLabel = MVNForumResourceBundle.getString(locale, "mvnforum.user.addattachmentsuccess.title");

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, null, null, addSuccessLabel);
        categorytree.addCategeoryTreeListener(treelistener);

        request.setAttribute("tree", categorytree.build());
    }

    public void prepareEdit(GenericRequest request, GenericResponse response)
        throws ObjectNotFoundException, BadInputException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int attachID = GenericParamUtil.getParameterInt(request, "attach");
        AttachmentBean attachmentBean = null;
        try {
            attachmentBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.attachmentid_not_exists", new Object[] {new Integer(attachID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        int postID = attachmentBean.getPostID();
        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);// can throw DatabaseException
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // now, check the permission
        permission.ensureCanEditPost(postBean.getForumID());

        ForumBean forumBean = ForumCache.getInstance().getBean(postBean.getForumID());
        forumBean.ensureNotDisabledForum(locale);
        forumBean.ensureNotLockedForum(locale);

        request.setAttribute("AttachmentBean", attachmentBean);
        request.setAttribute("PostBean", postBean);

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.editattachment.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(attachmentBean.getAttachFilename());

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

    }

    public void processEdit(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();

        // primary key column(s)
        int attachID = GenericParamUtil.getParameterInt(request, "attach");

        AttachmentBean attachmentBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
        int postID = attachmentBean.getPostID();

        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);// can throw DatabaseException
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        ForumCache.getInstance().getBean(postBean.getForumID()).ensureNotDisabledForum(locale);
        ForumCache.getInstance().getBean(postBean.getForumID()).ensureNotLockedForum(locale);

        // now, check the permission
        permission.ensureCanEditPost(postBean.getForumID());

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);
        int threadID = postBean.getThreadID();

        // delete in database
        String newDesc = GenericParamUtil.getParameter(request, "newdesc");
        DAOFactory.getAttachmentDAO().updateAttachDesc(attachID, newDesc);

        // now update the Lucene index
        AttachmentBean justUpdatedAttachBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
        AttachmentIndexer.scheduleUpdateAttachmentTask(justUpdatedAttachBean);

        request.setAttribute("ThreadID", String.valueOf(threadID));
        request.setAttribute("ForumID", new Integer(postBean.getForumID()));
    }
    
    public void editSuccessForRender(GenericRequest request, GenericResponse response) 
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {
        
        Locale locale = I18nUtil.getLocaleInRequest(request);

        int forumID = ((Integer)request.getAttribute("ForumID")).intValue();

        String editSuccessLabel = MVNForumResourceBundle.getString(locale, "mvnforum.user.editattachmentsuccess.title");

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, null, null, editSuccessLabel);
        categorytree.addCategeoryTreeListener(treelistener);
        
        request.setAttribute("tree", categorytree.build());
    }

    public void prepareDelete(GenericRequest request, GenericResponse response)
        throws ObjectNotFoundException, BadInputException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int attachID = GenericParamUtil.getParameterInt(request, "attach");
        AttachmentBean attachmentBean = null;
        try {
            attachmentBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.attachmentid_not_exists", new Object[] {new Integer(attachID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        int postID = attachmentBean.getPostID();
        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);// can throw DatabaseException
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // now, check the permission
        permission.ensureCanDeletePost(postBean.getForumID());

        ForumBean forumBean = ForumCache.getInstance().getBean(postBean.getForumID());

        forumBean.ensureNotDisabledForum(locale);
        forumBean.ensureNotLockedForum(locale);

        request.setAttribute("AttachmentBean", attachmentBean);
        request.setAttribute("PostBean", postBean);

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.deleteattachment.title");
        StringBuffer stb = new StringBuffer();
        stb.append(title).append(": ").append(attachmentBean.getAttachFilename());

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumBean.getForumID(), null, null, stb.toString());
        categorytree.addCategeoryTreeListener(treelistener);
        request.setAttribute("tree", categorytree.build());

    }

    public void processDelete(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        // user must have been authenticated before he can delete
        permission.ensureIsAuthenticated();

        // primary key column(s)
        int attachID = GenericParamUtil.getParameterInt(request, "attach");

        AttachmentBean attachmentBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
        int postID = attachmentBean.getPostID();

        PostBean postBean = null;
        try {
            postBean = DAOFactory.getPostDAO().getPost(postID);// can throw DatabaseException
        } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.postid_not_exists", new Object[] {new Integer(postID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        ForumCache.getInstance().getBean(postBean.getForumID()).ensureNotDisabledForum(locale);
        ForumCache.getInstance().getBean(postBean.getForumID()).ensureNotLockedForum(locale);

        // now, check the permission
        permission.ensureCanDeletePost(postBean.getForumID());

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        // delete on disk
        //AttachmentUtil.deleteAttachFilenameOnDisk(attachID);
        try {
            binaryStorageService.deleteData(BinaryStorageService.CATEGORY_POST_ATTACHMENT, String.valueOf(attachID), null);
        } catch (IOException ex) {
            log.error("Cannot delete file", ex);
            // actually this exception is never existed
        }

        // delete in database
        DAOFactory.getAttachmentDAO().delete(attachID);

        // Then delete in Lucene index
        AttachmentIndexer.scheduleDeleteAttachmentTask(attachID);

        // we don't want the exception to throw below this
        int attachCount = DAOFactory.getAttachmentDAO().getNumberOfAttachments_inPost(postID);
        DAOFactory.getPostDAO().updateAttachCount(postID, attachCount);

        // Now update the post because the attachment count in this post is changed
        postBean.setPostAttachCount(attachCount);
        PostIndexer.scheduleUpdatePostTask(postBean);

        int threadID = postBean.getThreadID();
        int attachCountInThread = DAOFactory.getAttachmentDAO().getNumberOfAttachments_inThread(threadID);
        DAOFactory.getThreadDAO().updateThreadAttachCount(threadID, attachCountInThread);

        // Now clear the cache
        PostCache.getInstance().clear();

        request.setAttribute("ThreadID", String.valueOf(threadID));
        request.setAttribute("ForumID", new Integer(postBean.getForumID()));
    }

    public void deleteSuccessForRender(GenericRequest request, GenericResponse response) 
        throws AuthenticationException, DatabaseException, ObjectNotFoundException {
        
        Locale locale = I18nUtil.getLocaleInRequest(request);

        int forumID = ((Integer)request.getAttribute("ForumID")).intValue();

        String deleteSuccessLabel = MVNForumResourceBundle.getString(locale, "mvnforum.user.deleteattachmentsuccess.title");

        CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree categorytree = new CategoryTree(treebuilder);
        CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response, forumID, null, null, deleteSuccessLabel);
        categorytree.addCategeoryTreeListener(treelistener);
        
        request.setAttribute("tree", categorytree.build());
    }
    
    /*
     * @todo find a way to cache the file based on the http protocal
     * @todo check permission
     */
    public void downloadAttachment(GenericRequest request, HttpServletResponse response)
        throws BadInputException, DatabaseException, ObjectNotFoundException, IOException,
        AuthenticationException  {

        Locale locale = I18nUtil.getLocaleInRequest(request);
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        int attachID = GenericParamUtil.getParameterInt(request, "attach");
        AttachmentBean attachBean = null;
        try {
            attachBean = DAOFactory.getAttachmentDAO().getAttachment(attachID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.attachmentid_not_exists", new Object[] {new Integer(attachID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        int postID = attachBean.getPostID();

        PostBean postBean = DAOFactory.getPostDAO().getPost(postID);
        int forumID = postBean.getForumID();
        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
        //ForumCache.getInstance().getBean(forumID).ensureNotLockedForum(); // lock forum should allow download

        if (MVNForumConfig.getEnableGuestViewImageAttachment() &&
            attachBean.getAttachMimeType().startsWith("image/")) {
            // When guest can view image attachment AND this attachment is image
            // This is for security, at least in this case user must have permission to view post
            permission.ensureCanReadPost(forumID);
        } else {
            // Please note that user does not have to have read permission
            permission.ensureCanGetAttachment(forumID);
        }

        InputStream inputStream = binaryStorageService.getInputStream(BinaryStorageService.CATEGORY_POST_ATTACHMENT, String.valueOf(attachID), null);

        /*
        String attachFilename = AttachmentUtil.getAttachFilenameOnDisk(attachID);
        File attachFile = new File(attachFilename);
        if ((!attachFile.exists()) || (!attachFile.isFile())) {
            log.error("Can't find a file " + attachFile + " to be downloaded (or maybe it's directory).");
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.not_exist_or_not_file_to_be_downloaded");
            throw new IOException(localizedMessage + " (AttachID=" + attachID + ")");
            //throw new IOException("Can't find a file to be downloaded (or maybe it's directory).");
        }
        */

        // we should not call this method after done the outputStream
        // because we don't want exception after download
        DAOFactory.getAttachmentDAO().increaseDownloadCount(attachID);

        OutputStream outputStream = null;
        try {
            response.setContentType(attachBean.getAttachMimeType());
            //response.setHeader("Location", attachBean.getAttachFilename());

            // now use Cache-Control if the MIME type are image
            if (attachBean.getAttachMimeType().startsWith("image/")) {
                long cacheTime = DateUtil.DAY * 30 / 1000;// 30 days
                response.setHeader("Cache-Control", "max-age=" + cacheTime);
            }
            //added by Dejan
            //response.setHeader("Content-Disposition", "attachment; filename=" + attachBean.getAttachFilename());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(attachBean.getAttachFilename(), "UTF-8") + "\"");
            
            // now, the header inited, just write the file content on the output
            outputStream = response.getOutputStream();

            boolean thumbnail = GenericParamUtil.getParameterBoolean(request, "thumbnail");
            try {
                if (thumbnail) {
                    ImageUtil.createThumbnail(inputStream, outputStream, MVNForumConfig.getThumbnailWidth(), MVNForumConfig.getThumbnailHeight());
                } else {
                    //write using popFile when the file size's so large
                    //FileUtil.popFile(attachFile, outputStream);
                    IOUtils.copy(inputStream, outputStream);
                }
            } catch (IOException ex) {
                // CANNOT throw Exception after we output to the response
                log.error("Error while trying to send attachment file from server: attachID = " + attachID + ".", ex);
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
                outputStream = null;// no close twice
            }
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

    /**
     * NOTE: This method should be called before any attemp to delete a post
     * because it require the post is exited
     * After calling this method, go ahead and delete the post
     */
    static void deleteAttachments_inPost(int postID) throws DatabaseException {

        BinaryStorageService binaryStorage = MvnCoreServiceFactory.getMvnCoreService().getBinaryStorageService();

        // First, try to delete attachment in database
        Collection attachmentBeans = DAOFactory.getAttachmentDAO().getAttachments_inPost(postID);
        DAOFactory.getAttachmentDAO().delete_inPost(postID);

        //now delete files on disk
        for (Iterator iter = attachmentBeans.iterator(); iter.hasNext(); ) {
            AttachmentBean attachmentBean = (AttachmentBean)iter.next();
            int attachID = attachmentBean.getAttachID();
            //AttachmentUtil.deleteAttachFilenameOnDisk(attachmentBean.getAttachID());
            try {
                binaryStorage.deleteData(BinaryStorageService.CATEGORY_POST_ATTACHMENT, String.valueOf(attachID), null);
            } catch (IOException ex) {
                log.error("Cannot delete file", ex);
                // actually this exception is never existed
            }
            
            // Then delete in Lucene index
            AttachmentIndexer.scheduleDeleteAttachmentTask(attachID);
        }
    }

    /**
     * NOTE: This method should be called before any attemp to delete a thread
     * because it require the thread is exited
     * After calling this method, go ahead and delete the thread
     */
    static void deleteAttachments_inThread(int threadID) throws DatabaseException {

        BinaryStorageService binaryStorage = MvnCoreServiceFactory.getMvnCoreService().getBinaryStorageService();

        // First, try to delete attachment in database
        Collection attachmentBeans = DAOFactory.getAttachmentDAO().getAttachments_inThread(threadID);

        //now delete files on disk
        for (Iterator iter = attachmentBeans.iterator(); iter.hasNext(); ) {
            AttachmentBean attachmentBean = (AttachmentBean)iter.next();
            int attachID = attachmentBean.getAttachID();
            //AttachmentUtil.deleteAttachFilenameOnDisk(attachID);
            try {
                binaryStorage.deleteData(BinaryStorageService.CATEGORY_POST_ATTACHMENT, String.valueOf(attachID), null);
            } catch (IOException ex) {
                log.error("Cannot delete file", ex);
                // actually this exception is never existed
            }
            
            try {
                DAOFactory.getAttachmentDAO().delete(attachID);
            } catch (Exception ex) {
                log.warn("Cannot delete attachment (id = " + attachID + ") in database", ex);
            }

            // Then delete in Lucene index
            AttachmentIndexer.scheduleDeleteAttachmentTask(attachID);
        }
    }

    public void prepareListAttachments(GenericRequest request, GenericResponse response)
        throws DatabaseException, BadInputException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        permission.ensureCanGetAttachmentInAnyForum();

       // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) sort = "AttachFilename";
        if (order.length()== 0) order = "DESC";
        
        int postsPerPage = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        Locale locale = I18nUtil.getLocaleInRequest(request);

        int category = -1;
        String inputCategory = GenericParamUtil.getParameter(request, "category");
        if (inputCategory.length() > 0) {
            category = GenericParamUtil.getParameterUnsignedInt(request, "category");
        }

        int forum = -1;
        String inputForum = GenericParamUtil.getParameter(request, "forum");
        if (inputForum.length() > 0) {
            forum = GenericParamUtil.getParameterUnsignedInt(request, "forum");
        }

        int totalAttachments = DAOFactory.getAttachmentDAO().getNumberOfAttachments(category, forum);

        log.debug("totalAttachments : " + totalAttachments);
        log.debug("offset : " + offset);
        log.debug("postsPerPage : " + postsPerPage);

        if (offset > totalAttachments) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection attachmentBeans = DAOFactory.getAttachmentDAO().getAttachments_withSortSupport_limit(offset, postsPerPage, sort, order, category, forum);

        for (Iterator iter  = attachmentBeans.iterator(); iter.hasNext();) {
            AttachmentBean bean = (AttachmentBean)iter.next();
            if (permission.canGetAttachment(bean.getForumID()) == false) {
                attachmentBeans.remove(bean);
            } else if (ForumCache.getInstance().getBean(bean.getForumID()).getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
                attachmentBeans.remove(bean);
            }
        }

        request.setAttribute("AttachmentBeans", attachmentBeans);
        request.setAttribute("TotalAttachments", new Integer(totalAttachments));

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "listattachments", forum, category);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void processSearchAttachments(GenericRequest request, GenericResponse response)
        throws AuthenticationException, DatabaseException, BadInputException, IOException, ObjectNotFoundException {

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

        String key  = GenericParamUtil.getParameter(request, "key");
        String attachmentName = GenericParamUtil.getParameter(request, "attachmentname");
        
        if ( (key.length() == 0) && (attachmentName.length() == 0) ) {
            return;
        }

        int forumID = GenericParamUtil.getParameterInt(request, "forum", 0);//negative means category
        int offset  = GenericParamUtil.getParameterUnsignedInt(request, "offset", 0);
        int rows    = GenericParamUtil.getParameterUnsignedInt(request, "rows", 20);
        if (rows == 0) {
            rows = 20;// fix NullPointerException when rows = 0
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

        int searchDate        = GenericParamUtil.getParameterUnsignedInt(request, "date", AttachmentSearchQuery.SEARCH_ANY_DATE);
        int searchBeforeAfter = GenericParamUtil.getParameterInt(request, "beforeafter", AttachmentSearchQuery.SEARCH_NEWER);

        if ((searchDate != AttachmentSearchQuery.SEARCH_ANY_DATE) && (searchDate < 365 * 10)) { // 10 years
            long deltaTime = DateUtil.DAY * searchDate;

            Timestamp now = DateUtil.getCurrentGMTTimestamp();
            Timestamp from = null;
            Timestamp to = null;

            long currentTime = now.getTime();

            if (searchBeforeAfter == AttachmentSearchQuery.SEARCH_NEWER) {
                from = new Timestamp(currentTime - deltaTime);
            } else {// older
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
            // forumID equals to 0, it mean global searching
            // just do nothing, Lucene will search all forums (globally)
        }

        query.searchDocuments(offset, rows, permission);
        int hitCount = query.getHitCount();
        Collection result = query.getAttachmentResult();
        
        // Remove attachments that current user do not have permission (actually the AttachmentSearchQuery already return correct value)
        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            AttachmentBean attachBean = (AttachmentBean)iter.next();
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
            //throw new BadInputException("Cannot search with offset > total posts");
        }

        request.setAttribute("rows", new Integer(rows));
        request.setAttribute("TotalAttachs", new Integer(hitCount));
        request.setAttribute("AttachBeans", result);

    }
}
