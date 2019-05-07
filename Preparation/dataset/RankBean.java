/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/RankBean.java,v 1.10 2007/10/09 11:09:19 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.10 $
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

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: RankID, RankMinPosts, RankLevel, RankTitle, RankImage,
 *                   RankType, RankOption
 * Excluded columns:
 */
public class RankBean {
    private int rankID;
    private int rankMinPosts;
    private int rankLevel;
    private String rankTitle;
    private String rankImage;
    private int rankType;
    private int rankOption;

    public int getRankID() {
        return rankID;
    }
    public void setRankID(int rankID) {
        this.rankID = rankID;
    }

    public int getRankMinPosts() {
        return rankMinPosts;
    }
    public void setRankMinPosts(int rankMinPosts) {
        this.rankMinPosts = rankMinPosts;
    }

    public int getRankLevel() {
        return rankLevel;
    }
    public void setRankLevel(int rankLevel) {
        this.rankLevel = rankLevel;
    }

    public String getRankTitle() {
        return rankTitle;
    }
    public void setRankTitle(String rankTitle) {
        this.rankTitle = StringUtil.getEmptyStringIfNull(rankTitle);
    }

    public String getRankImage() {
        return rankImage;
    }
    public void setRankImage(String rankImage) {
        this.rankImage = StringUtil.getEmptyStringIfNull(rankImage);
    }

    public int getRankType() {
        return rankType;
    }
    public void setRankType(int rankType) {
        this.rankType = rankType;
    }

    public int getRankOption() {
        return rankOption;
    }
    public void setRankOption(int rankOption) {
        this.rankOption = rankOption;
    }

} //end of class RankBean
