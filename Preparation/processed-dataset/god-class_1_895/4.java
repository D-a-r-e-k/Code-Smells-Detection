/**
     * Overridden. Sets the date of both entries.
     */
public void setDate(Date aDate) {
    if (date != null && date.equals(aDate))
        return;
    date = aDate;
    changeSupport.firePropertyChange("date", null, date);
    if (other == null)
        return;
    other.date = aDate;
    other.changeSupport.firePropertyChange("date", null, date);
}
