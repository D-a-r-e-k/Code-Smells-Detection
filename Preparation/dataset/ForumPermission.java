/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/ForumPermission.java,v 1.7 2007/10/09 11:09:12 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.7 $
 * $Date: 2007/10/09 11:09:12 $
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

/**
 * This class is used in MVNForumPermissionImpl to imnplement forum-specific permission
 * In detail: this class contains 2 properties: forumID and permission so represent
 * the permission in the forum
 */
class ForumPermission {

    int forumID;

    int permission;

    public int getForumID() {
        return forumID;
    }
    void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public int getPermission() {
        return permission;
    }
    void setPermission(int permission) {
        this.permission = permission;
    }
}
