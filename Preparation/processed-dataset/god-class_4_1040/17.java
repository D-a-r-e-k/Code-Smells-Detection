/** 
     * gets the affected Subject Oid, this can be used in conjunction with
     * the subject type to reconstruct the affected object at a later stage
     * @return the subject oid
     * @hibernate.property column="AUD_SUBJECT_OID" not-null="false"
     */
public Long getSubjectOid() {
    return subjectOid;
}
