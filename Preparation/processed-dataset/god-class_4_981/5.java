/**
     * <p>
     * Set whether String-only properties will be handled in JobDataMaps.
     * </p>
     */
public void setUseProperties(String useProp) {
    if (useProp == null) {
        useProp = "false";
    }
    this.useProperties = Boolean.valueOf(useProp).booleanValue();
}
