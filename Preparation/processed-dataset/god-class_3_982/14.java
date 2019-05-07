protected String getExpressionSetSummary(java.util.ArrayList list) {
    if (list.contains(NO_SPEC)) {
        return "?";
    }
    if (list.contains(ALL_SPEC)) {
        return "*";
    }
    StringBuffer buf = new StringBuffer();
    Iterator itr = list.iterator();
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
