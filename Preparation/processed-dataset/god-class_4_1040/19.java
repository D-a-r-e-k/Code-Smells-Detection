/**
     * gets the Audit message oid, this is its primary key
     * @return the Audit oid
     * @hibernate.id column="AUD_OID" generator-class="org.hibernate.id.MultipleHiLoPerTableGenerator"
     * @hibernate.generator-param name="table" value="ID_GENERATOR"
     * @hibernate.generator-param name="primary_key_column" value="IG_TYPE"
     * @hibernate.generator-param name="value_column" value="IG_VALUE"
     * @hibernate.generator-param name="primary_key_value" value="Audit"
     */
public Long getOid() {
    return oid;
}
