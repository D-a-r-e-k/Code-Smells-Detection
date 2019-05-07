/**
     * gets the Audit Action 
     * @return the Audit Action
     * @hibernate.property column="AUD_ACTION" not-null="true" length="40"
     */
public String getAction() {
    return action;
}
