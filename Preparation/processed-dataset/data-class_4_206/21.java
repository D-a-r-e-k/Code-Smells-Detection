/**
     * gets the Agency oid of the user who issued this action
     * @return the Agency Oid
     * @hibernate.property column="AUD_AGENCY_OID" not-null="false"
     */
public Long getAgencyOid() {
    return agencyOid;
}
