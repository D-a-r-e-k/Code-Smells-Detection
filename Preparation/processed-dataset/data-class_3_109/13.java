/**
     * Check if the field is a time field.
     *
     * @return true if the field is a time field
     */
public String isTime() {
    if ("java.sql.Time".equals(getType())) {
        return "true";
    }
    if ("java.sql.Timestamp".equals(getType())) {
        return "true";
    }
    return "false";
}
