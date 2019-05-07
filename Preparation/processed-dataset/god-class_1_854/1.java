public String getMessage() {
    StringBuffer buf = new StringBuffer(super.getMessage());
    if (offset != -1 || line != -1 || column != -1) {
        buf.append(" [");
        if (offset != -1) {
            buf.append("offset:" + offset + ",");
        }
        if (line != -1) {
            buf.append("line:" + (line + 1) + ",");
        }
        if (column != -1) {
            buf.append("column:" + (column + 1));
        }
        if (buf.charAt(buf.length() - 1) == ',') {
            buf.deleteCharAt(buf.length() - 1);
        }
        buf.append(']');
    }
    return buf.toString();
}
