/**
     * gets the affected subjectType type, this is most likely a class
     * @return the subjectType type
     * @hibernate.property column="AUD_SUBJECT_TYPE" not-null="true" length="255"
     */
public String getSubjectType() {
    return subjectType;
}
