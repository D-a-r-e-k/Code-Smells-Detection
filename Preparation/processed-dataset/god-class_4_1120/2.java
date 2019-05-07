/**
     * For use with CHECK constraints. Under development.
     *
     * Currently supports a subset of expressions and is suitable for CHECK
     * search conditions that refer only to the inserted/updated row.
     *
     * For full DDL reporting of VIEW select queries and CHECK search
     * conditions, future improvements here are dependent upon improvements to
     * SELECT query parsing, so that it is performed in a number of passes.
     * An early pass should result in the query turned into an Expression tree
     * that contains the information in the original SQL without any
     * alterations, and with tables and columns all resolved. This Expression
     * can then be preserved for future use. Table and column names that
     * are not user-defined aliases should be kept as the HsqlName structures
     * so that table or column renaming is reflected in the precompiled
     * query.
     */
public String getSQL() {
    StringBuffer sb = new StringBuffer(64);
    switch(opType) {
        case OpTypes.VALUE:
            if (valueData == null) {
                return Tokens.T_NULL;
            }
            return dataType.convertToSQLString(valueData);
        case OpTypes.ROW:
            sb.append('(');
            for (int i = 0; i < nodes.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(nodes[i].getSQL());
            }
            sb.append(')');
            return sb.toString();
        // 
        case OpTypes.TABLE:
            for (int i = 0; i < nodes.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(nodes[i].getSQL());
            }
            return sb.toString();
    }
    switch(opType) {
        case OpTypes.ARRAY:
            sb.append(Tokens.T_ARRAY).append('[');
            for (int i = 0; i < nodes.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(nodes[i].getSQL());
            }
            sb.append(']');
            break;
        case OpTypes.ARRAY_SUBQUERY:
        // 
        case OpTypes.ROW_SUBQUERY:
        case OpTypes.TABLE_SUBQUERY:
            sb.append('(');
            sb.append(')');
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "Expression");
    }
    return sb.toString();
}
