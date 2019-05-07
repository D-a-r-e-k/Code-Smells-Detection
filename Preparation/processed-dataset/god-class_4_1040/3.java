/**
     * gets the Date and Time of the Audit entry
     * @return the DateTime
     * @hibernate.property type="timestamp"
     * @hibernate.column name="AUD_DATE" not-null="true" sql-type="TIMESTAMP(9)"
     */
public Date getDateTime() {
    return dateTime;
}
