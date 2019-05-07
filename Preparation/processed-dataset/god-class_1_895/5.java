/**
     * Overridden. Sets the description of both entries.
     */
public void setDescription(String aDescription) {
    if (description != null && description.equals(aDescription))
        return;
    description = aDescription.length() == 0 ? null : aDescription;
    changeSupport.firePropertyChange("description", null, description);
    if (other == null)
        return;
    other.description = description;
    other.changeSupport.firePropertyChange("description", null, description);
}
