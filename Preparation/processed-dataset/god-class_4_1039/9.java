/**
     * Returns the name of the owner of the AbstractTarget.
     * @return the name of the owner of the AbstractTarget.
     * @hibernate.property column="USR_USERNAME" length="80"
     */
public String getOwnerName() {
    return ownerName;
}
