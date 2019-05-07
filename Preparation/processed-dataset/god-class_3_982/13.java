protected String getExpressionSetSummary(java.util.Set set) {
    if (set.contains(NO_SPEC)) {
        return "?";
    }
    if (set.contains(ALL_SPEC)) {
        return "*";
    }
    StringBuffer buf = new StringBuffer();
    Iterator itr = set.iterator();
    boolean first = true;
    while (itr.hasNext()) {
        Integer iVal = (Integer) itr.next();
        String val = iVal.toString();
        if (!first) {
            buf.append(",");
        }
        buf.append(val);
        first = false;
    }
    return buf.toString();
}
