/**
     * Check if the field is a date field.
     *
     * @return true if the field is a date field
     */
public String isDate() {
    if ("java.sql.Date".equals(getType())) {
        return "true";
    }
    if ("java.util.Date".equals(getType())) {
        return "true";
    }
    return "false";
}
