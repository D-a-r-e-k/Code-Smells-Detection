/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/OnlineUser.java,v 1.37 2008/06/12 08:33:46 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.37 $
 * $Date: 2008/06/12 08:33:46 $
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
import java.util.*;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.exception.BadInputException;

public interface OnlineUser {

    /**************************************************************
     * Constants
     **************************************************************/
    public static final int AUTHENTICATION_TYPE_UNAUTHENTICATED = 0;

    public static final int AUTHENTICATION_TYPE_HTML_FORM       = 1;

    public static final int AUTHENTICATION_TYPE_SESSION         = 2;

    public static final int AUTHENTICATION_TYPE_COOKIE          = 3;

    public static final int AUTHENTICATION_TYPE_REALM           = 4;

    public static final int AUTHENTICATION_TYPE_CUSTOMIZATION   = 5;
    
    public static final int AUTHENTICATION_TYPE_CAS             = 6;

    /**************************************************************
     * Methods
     **************************************************************/
    /**
     * Get the numeric member id of this user
     *
     * @return int the numeric member id of this user
     */
    public int getMemberID();

    /**
     * Get the memberName that is used to login
     *
     * @return String the memberName that is used to login
     */
    public String getMemberName();

    /**
     * Check if this user is guest (not login yet) or already authenticated
     *
     * @return boolean true if this user is guest (not login yet)
     */
    public boolean isGuest();

    /**
     * Check if this user is already authenticated
     *
     * @return boolean true if this user is already authenticated
     */
    public boolean isMember();

    /**
     * Check if this user prefer the invisible mode
     *
     * @return boolean true if this user prefer the invisible mode
     */
    public boolean isInvisibleMember();

    /**
     * Check if this member's password is expired. For single sign on, this method should always return false
     *
     * @return boolean true if password is expired
     */
    public boolean isPasswordExpired();

    /**
     * Get the authentication type that user did used to login
     *
     * @return int the authentication type
     */
    public int getAuthenticationType();

    /**
     * Get the permission that this user currently has
     *
     * @return MVNForumPermission
     */
    public MVNForumPermission getPermission();

    /**
     * Reload the permission from the underlying database
     */
    public void reloadPermission();

    /**
     * Reload the user's profile from the underlying database
     */
    public void reloadProfile();

    /**
     * Get the current action of this user in the system
     *
     * @return OnlineUserAction
     */
    public OnlineUserAction getOnlineUserAction();

    public java.util.Date convertGMTDate(java.util.Date gmtDate);

    public Timestamp convertGMTTimestamp(Timestamp gmtTimestamp);

    public String getGMTDateFormat(java.util.Date gmtDate);

    public String getGMTDateFormat(java.util.Date gmtDate, boolean adjustTimeZone);

    public String getGMTTimestampFormat(Timestamp gmtTimestamp);

    public String getGMTTimestampFormat(Timestamp gmtTimestamp, boolean adjustTimeZone);

    public String getTimeZoneFormat();

    /**
     * Get the current timezone of this user
     * @return current timezone of this user
     */
    public double getTimeZone();

    /**
     * Get the current locale of this user
     *
     * @return Locale the current locale of this user
     */
    public Locale getLocale();

    /**
     * Get the current locale name of this user
     *
     * @return String the current locale name of this user
     */
    public String getLocaleName();

    /**
     * Set the locale name for the current online user
     * @param localeName String
     */
    public void setLocaleName(String localeName);

    //public boolean getGender();

    /**
     * Get the timestampt of the last time this user login
     *
     * @return Timestamp
     */
    public Timestamp getLastLogonTimestamp();

    /**
     * Get the IP of the last time this user login
     *
     * @return String
     */
    public String getLastLogonIP();

    /**
     * Get the number of items that shown in one page for this user
     *
     * @return int the number of items that shown in one page
     */
    public int getPostsPerPage();

    /**
     * Get the number of new private messages of this user.
     * @return int the number of new private messages of this user
     */
    public int getNewMessageCount();

    /**
     * Update the new message count value. The implementation
     * should provide an effectively method if parameter force is false
     * since this method is called many time.<p>
     * In case force is true, update it immediately
     * @return boolean values. It's true if the new message count
     *         greater than the current message count
     */
    public boolean updateNewMessageCount(boolean force);

    /**
     * Get the number of private messages that shown in one page for this user
     *
     * @return int the number of items that shown in one page
     */
    public int getMessagesPerPage();

    /**
     * Get the full ABSOLUTE path of the css file
     *
     * @return String the full ABSOLUTE path of the css file
     */
    public String getCssPath();

    /**
     * Get the full ABSOLUTE path of the css file
     *
     * @return String the full ABSOLUTE path of the css file,
     *         this method can customize the path based on parameter in request
     */
    public String getCssPath(HttpServletRequest request);

    /**
     * Get the full ABSOLUTE path of the logo file
     *
     * @return String the full ABSOLUTE path of the logo file
     */
    public String getLogoPath();

    /**
     * Build a new captcha, this method must be called before using some
     * action that need captcha validation.
     */
    public void buildNewCaptcha();

    /**
     * Destroy the current captcha, this method must be called after validate
     * the captcha
     */
    public void destroyCurrentCaptcha();

    /**
     * Get the captcha image to challenge the user
     *
     * @return BufferedImage the captcha image to challenge the user
     */
    public BufferedImage getCurrentCaptchaImage();

    /**
     * Validate the anwser of the captcha from user
     *
     * @param anwser String the captcha anwser from user
     * @return boolean true if the answer is valid, otherwise return false
     */
    public boolean validateCaptchaResponse(String anwser);

    /**
     * Check to make sure that the captcha answer is correct
     *
     * @param answer String the captcha answer to check
     * @throws BadInputException in case the captcha answer is not correct
     */
    public void ensureCorrectCaptchaResponse(String answer) throws BadInputException;

    public void setCssPath(String path);

    public void setLogoPath(String path);

    public void setXMPPConnection(Object conn);

    public Object getXMPPConnection();

    public void setParticipants(Object conn);

    public Set getParticipants();

    public void setWaitingList(Object conn);

    public Set getWaitingList();

    public void removeParticipant(Object conn);

    public void removeWaiting(Object conn);

}
