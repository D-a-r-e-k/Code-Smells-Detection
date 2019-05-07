/**
     * gets the Audit messgae
     * @return the message
     * @hibernate.property column="AUD_MESSAGE" not-null="true" length="2000"
     */
public String getMessage() {
    return message;
}
