/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MessageStatisticsBean.java,v 1.8 2007/10/09 11:09:19 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.8 $
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
 */
package com.mvnforum.db;

import java.sql.Timestamp;

/*
 * Included columns: FromID, ToID, MessageCreationDate, MessageAttachCount, MessageType,
 *                   MessageOption, MessageStatus
 * Excluded columns:
 */
public class MessageStatisticsBean {
    private int fromID;
    private int toID;
    private Timestamp messageCreationDate;
    private int messageAttachCount;
    private int messageType;
    private int messageOption;
    private int messageStatus;

    public int getFromID() {
        return fromID;
    }
    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToID() {
        return toID;
    }
    public void setToID(int toID) {
        this.toID = toID;
    }

    public Timestamp getMessageCreationDate() {
        return messageCreationDate;
    }
    public void setMessageCreationDate(Timestamp messageCreationDate) {
        this.messageCreationDate = messageCreationDate;
    }

    public int getMessageAttachCount() {
        return messageAttachCount;
    }
    public void setMessageAttachCount(int messageAttachCount) {
        this.messageAttachCount = messageAttachCount;
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

} //end of class MessageStatisticsBean
