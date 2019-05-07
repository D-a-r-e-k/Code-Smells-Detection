/**
     * Returns the name of the agency of the owner of the AbstractTarget.
     * @return the name of the agency of the owner of the AbstractTarget.
     * @hibernate.property column="AGC_NAME" length="80"
     */
public String getAgencyName() {
    return agencyName;
}
