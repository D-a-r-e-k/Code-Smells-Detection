package org.lnicholls.galleon.database;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * Auto-generated using Hibernate hbm2java tool.
 * Copyright (C) 2005, 2006 Leon Nicholls
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * See the file "COPYING" for more details.
 *     
*/
public class Application implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String clazz;

    /** persistent field */
    private String name;

    /** persistent field */
    private String version;

    /** persistent field */
    private int total;

    /** nullable persistent field */
    private Date dateInstalled;

    /** nullable persistent field */
    private Date dateRemoved;

    /** nullable persistent field */
    private Date lastUsed;

    /** nullable persistent field */
    private Boolean shared;

    /** full constructor */
    public Application(String clazz, String name, String version, int total, Date dateInstalled, Date dateRemoved, Date lastUsed, Boolean shared) {
        this.clazz = clazz;
        this.name = name;
        this.version = version;
        this.total = total;
        this.dateInstalled = dateInstalled;
        this.dateRemoved = dateRemoved;
        this.lastUsed = lastUsed;
        this.shared = shared;
    }

    /** default constructor */
    public Application() {
    }

    /** minimal constructor */
    public Application(String clazz, String name, String version, int total) {
        this.clazz = clazz;
        this.name = name;
        this.version = version;
        this.total = total;
    }

    public Integer getId() {
        return this.id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getDateInstalled() {
        return this.dateInstalled;
    }

    public void setDateInstalled(Date dateInstalled) {
        this.dateInstalled = dateInstalled;
    }

    public Date getDateRemoved() {
        return this.dateRemoved;
    }

    public void setDateRemoved(Date dateRemoved) {
        this.dateRemoved = dateRemoved;
    }

    public Date getLastUsed() {
        return this.lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Boolean getShared() {
        return this.shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

}
