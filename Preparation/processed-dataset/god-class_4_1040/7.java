/**
     * gets the Users lastname
     * @return the users lastname
     * @hibernate.property column="AUD_LASTNAME" not-null="false" length="50"
     */
public String getLastname() {
    return lastname;
}
