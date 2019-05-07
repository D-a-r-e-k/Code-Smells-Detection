/*
     * Get the OID of the AbstractTargetScheduleView record.
     * @return Returns the oid.
     * @hibernate.column unique="false"
     * @hibernate.id column="AT_OID" generator-class="org.hibernate.id.MultipleHiLoPerTableGenerator"
     * @hibernate.generator-param name="table" value="ID_GENERATOR"
     * @hibernate.generator-param name="primary_key_column" value="IG_TYPE"
     * @hibernate.generator-param name="value_column" value="IG_VALUE"
     * @hibernate.generator-param name="primary_key_value" value="General" 
     */
/*
	public Long getOid() {
        return oid;
    }
    */
/*
     * Hibernate method to set the OID.
     * @param aOid The oid to set.
     */
/*
	public void setOid(Long aOid) {
        this.oid = aOid;
    }
    */
/**
	 * Returns the object type description ('Target' or 'Group').
	 * @return The object type description.
	 * @hibernate.property column="AT_OBJECT_TYPE_DESC"
	 */
public String getObjectTypeDesc() {
    return objectTypeDesc;
}
