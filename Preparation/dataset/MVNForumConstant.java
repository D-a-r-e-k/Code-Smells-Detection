/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/MVNForumConstant.java,v 1.18 2007/10/09 11:09:22 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.18 $
 * $Date: 2007/10/09 11:09:22 $
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
 * @author: Igor Manic
 */
package com.mvnforum;

public final class MVNForumConstant {

    /** Cannot instantiate. */
    private MVNForumConstant() {
    }

/*************************************************************************
 * NOTE: below constants MUST NOT be changed IN ALL CASES,
 *       or it will break the compatibility
 *************************************************************************/

    /** Guest/anonymous site visitor. */
    public static final int MEMBER_ID_OF_GUEST             = 0;
    /** System administrator. */
    public static final int MEMBER_ID_OF_ADMIN             = 1;
    /**
     * The highest reserved MemberID.
     * All IDs from 0 through this value should not be used for "regular" members.
     */
    public static final int LAST_RESERVED_MEMBER_ID        = 1;

    /**
     * This is a hard code constant and CANNOT be changed in any case.
     * it could be use to store memberName in the table mvnforumPost, mvnforumThread if
     * it is a guest's post
     */
    public static final String MEMBER_NAME_OF_GUEST        = "guest";

    /* IMPORTANT: When we have a group without group owner, GroupOwnerID is set to 0.
     * Similiar is for other IDs in the database - 0 means there is no reference.
     * Also, the other reason why MemberID=0 should not be used for Guest is
     * that DBMS could refuse to insert a record with 0 in that field, since it's
     * marked as non-null autoincrement primary key.
     */

    /** Unused GroupID. */
    public static final int GROUP_ID_UNUSED0               = 0;
    /**
     * Unused GroupID. In the previous versions of mvnForum it was used for some
     * special purposes, but should not be used anymore.
     */
    public static final int GROUP_ID_OF_GUEST              = 1;
    /** "Registered Members" virtual group. All members are listed in this group. */
    public static final int GROUP_ID_OF_REGISTERED_MEMBERS = 2;
    /**
     * The highest reserved GroupID.
     * All IDs from 0 through this value should not be used for "regular" groups.
     */
    public static final int LAST_RESERVED_GROUP_ID         = 2;

    /** "Inbox" message folder created by default for each member. */
    public static final String MESSAGE_FOLDER_INBOX        = "Inbox";
    /** "Sent" message folder created by default for each member. */
    public static final String MESSAGE_FOLDER_SENT         = "Sent";
    /** "Draft" message folder created by default for each member. */
    public static final String MESSAGE_FOLDER_DRAFT        = "Draft";
    /** "Trash" message folder created by default for each member. */
    public static final String MESSAGE_FOLDER_TRASH        = "Trash";

    public static final String dtdschemaDecl="<!DOCTYPE mvnforum SYSTEM \"http://www.mvnforum.com/dtd/mvnforum_1_0_rc2.dtd\">";

    public static final String VN_TYPER_MODE = "mvnforum.vntypermode";

    public static final String EVENT_LOG_MAIN_MODULE        = "mvnForum";

    public static final String EVENT_LOG_SUB_MODULE_USER    = "User";

    public static final String EVENT_LOG_SUB_MODULE_ADMIN   = "Admin";
}
