/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MessageBean.java,v 1.16 2007/10/09 11:09:19 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.16 $
 * $Date: 2007/10/09 11:09:19 $
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
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName,
 *                   MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody,
 *                   MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify,
 *                   MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate
 * Excluded columns:
 */
public class MessageBean {
    /*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/
    /**
     * The default value means this message has NOT been read
     */
    public final static int MESSAGE_READ_STATUS_DEFAULT  = 0;

    /**
     * This value means this message has been read
     */
    public final static int MESSAGE_READ_STATUS_READ = 1;

    /**
     * The default value means this message type is normal
     */
    public final static int MESSAGE_TYPE_DEFAULT  = 0;

    /**
     * This value means this message has been mark as Quote [Marco]
     */
    public final static int MESSAGE_TYPE_QUOTE = 1;

    /**
     * This value means this message is a public message
     */
    public final static int MESSAGE_TYPE_PUBLIC = 2;

    private int messageID;
    private String folderName;
    private int memberID;
    private int messageSenderID;
    private String messageSenderName;
    private String messageToList;
    private String messageCcList;
    private String messageBccList;
    private String messageTopic;
    private String messageBody;
    private int messageType;
    private int messageOption;
    private int messageStatus;
    private int messageReadStatus;
    private int messageNotify;
    private String messageIcon;
    private int messageAttachCount;
    private String messageIP;
    private Timestamp messageCreationDate;

    public int getMessageID() {
        return messageID;
    }
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getMessageSenderID() {
        return messageSenderID;
    }
    public void setMessageSenderID(int messageSenderID) {
        this.messageSenderID = messageSenderID;
    }

    public String getMessageSenderName() {
        return messageSenderName;
    }
    public void setMessageSenderName(String messageSenderName) {
        this.messageSenderName = messageSenderName;
    }

    public String getMessageToList() {
        return messageToList;
    }
    public void setMessageToList(String messageToList) {
        this.messageToList = StringUtil.getEmptyStringIfNull(messageToList);
    }

    public String getMessageCcList() {
        return messageCcList;
    }
    public void setMessageCcList(String messageCcList) {
        this.messageCcList = StringUtil.getEmptyStringIfNull(messageCcList);
    }

    public String getMessageBccList() {
        return messageBccList;
    }
    public void setMessageBccList(String messageBccList) {
        this.messageBccList = StringUtil.getEmptyStringIfNull(messageBccList);
    }

    public String getMessageTopic() {
        return messageTopic;
    }
    public void setMessageTopic(String messageTopic) {
        this.messageTopic = StringUtil.getEmptyStringIfNull(messageTopic);
    }

    public String getMessageBody() {
        return messageBody;
    }
    public void setMessageBody(String messageBody) {
        this.messageBody = StringUtil.getEmptyStringIfNull(messageBody);
    }

    public int getMessageType() {
        return messageType;
    }
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageOption() {
        return messageOption;
    }
    public void setMessageOption(int messageOption) {
        this.messageOption = messageOption;
    }

    public int getMessageStatus() {
        return messageStatus;
    }
    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageReadStatus() {
        return messageReadStatus;
    }
    public void setMessageReadStatus(int messageReadStatus) {
        this.messageReadStatus = messageReadStatus;
    }

    public int getMessageNotify() {
        return messageNotify;
    }
    public void setMessageNotify(int messageNotify) {
        this.messageNotify = messageNotify;
    }

    public String getMessageIcon() {
        return messageIcon;
    }
    public void setMessageIcon(String messageIcon) {
        this.messageIcon = StringUtil.getEmptyStringIfNull(messageIcon);
    }

    public int getMessageAttachCount() {
        return messageAttachCount;
    }
    public void setMessageAttachCount(int messageAttachCount) {
        this.messageAttachCount = messageAttachCount;
    }

    public String getMessageIP() {
        return messageIP;
    }
    public void setMessageIP(String messageIP) {
        this.messageIP = messageIP;
    }

    public Timestamp getMessageCreationDate() {
        return messageCreationDate;
    }
    public void setMessageCreationDate(Timestamp messageCreationDate) {
        this.messageCreationDate = messageCreationDate;
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private MemberBean memberBean = null;
    private Collection attachmentBeans = null;

    public MemberBean getMemberBean() {
        return memberBean;
    }
    public void setMemberBean(MemberBean memberBean) {
        this.memberBean = memberBean;
    }

    public Collection getAttachmentBeans() {
        return attachmentBeans;
    }
    public void setAttachmentBeans(Collection attachmentBeans) {
        this.attachmentBeans = attachmentBeans;
    }

} //end of class MessageBean
