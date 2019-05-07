/**
     * gets the Username of the user who issued the action
     * @return the Username
     * @hibernate.property column="AUD_USERNAME" not-null="false" length="80"
     */
public String getUserName() {
    return userName;
}
