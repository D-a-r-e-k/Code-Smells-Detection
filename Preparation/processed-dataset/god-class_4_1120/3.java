protected String describe(Session session, int blanks) {
    StringBuffer sb = new StringBuffer(64);
    sb.append('\n');
    for (int i = 0; i < blanks; i++) {
        sb.append(' ');
    }
    switch(opType) {
        case OpTypes.VALUE:
            sb.append("VALUE = ").append(valueData);
            sb.append(", TYPE = ").append(dataType.getNameString());
            return sb.toString();
        case OpTypes.ARRAY:
            sb.append("ARRAY ");
            return sb.toString();
        case OpTypes.ARRAY_SUBQUERY:
            sb.append("ARRAY SUBQUERY");
            return sb.toString();
        // 
        case OpTypes.ROW_SUBQUERY:
        case OpTypes.TABLE_SUBQUERY:
            sb.append("QUERY ");
            sb.append(subQuery.queryExpression.describe(session, blanks));
            return sb.toString();
        case OpTypes.ROW:
            sb.append("ROW = ");
            for (int i = 0; i < nodes.length; i++) {
                sb.append(nodes[i].describe(session, blanks + 1));
                sb.append(' ');
            }
            break;
        case OpTypes.TABLE:
            sb.append("VALUELIST ");
            for (int i = 0; i < nodes.length; i++) {
                sb.append(nodes[i].describe(session, blanks + 1));
                sb.append(' ');
            }
            break;
    }
    return sb.toString();
}
