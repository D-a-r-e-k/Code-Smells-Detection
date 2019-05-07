/**
     * gets the Users firstname
     * @return the users firstname
     * @hibernate.property column="AUD_FIRSTNAME" not-null="false" length="50"
     */
public String getFirstname() {
    return firstname;
}
