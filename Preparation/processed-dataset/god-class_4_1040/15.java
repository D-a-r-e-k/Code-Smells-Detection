/**
     * gets the User oid of the user who issued this action
     * @return the User Oid
     * @hibernate.property column="AUD_USER_OID" not-null="false"
     */
public Long getUserOid() {
    return userOid;
}
