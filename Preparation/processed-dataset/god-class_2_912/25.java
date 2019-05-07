protected Object add(Column column, Object total, Object value) {
    if (value == null) {
        return total;
    } else if (value instanceof Number) {
        Number oldTotal = new Double(0);
        if (total != null) {
            oldTotal = (Number) total;
        }
        return new Double(oldTotal.doubleValue() + ((Number) value).doubleValue());
    } else {
        throw new UnsupportedOperationException("Cannot add a value of " + value + " in column " + column.getHeaderCell().getTitle());
    }
}
