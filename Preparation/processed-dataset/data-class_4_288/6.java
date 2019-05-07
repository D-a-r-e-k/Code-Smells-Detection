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
        case OpTypes.NOT:
            if (nodes[LEFT].opType == OpTypes.NOT_DISTINCT) {
                sb.append(Tokens.T_DISTINCT);
                return sb.toString();
            }
            sb.append(Tokens.T_NOT);
            break;
        case OpTypes.NOT_DISTINCT:
            sb.append(Tokens.T_NOT).append(' ').append(Tokens.T_DISTINCT);
            break;
        case OpTypes.EQUAL:
            sb.append("EQUAL");
            break;
        case OpTypes.GREATER_EQUAL:
            sb.append("GREATER_EQUAL");
            break;
        case OpTypes.GREATER:
            sb.append("GREATER");
            break;
        case OpTypes.SMALLER:
            sb.append("SMALLER");
            break;
        case OpTypes.SMALLER_EQUAL:
            sb.append("SMALLER_EQUAL");
            break;
        case OpTypes.NOT_EQUAL:
            sb.append("NOT_EQUAL");
            break;
        case OpTypes.AND:
            sb.append(Tokens.T_AND);
            break;
        case OpTypes.OR:
            sb.append(Tokens.T_OR);
            break;
        case OpTypes.MATCH_SIMPLE:
        case OpTypes.MATCH_PARTIAL:
        case OpTypes.MATCH_FULL:
        case OpTypes.MATCH_UNIQUE_SIMPLE:
        case OpTypes.MATCH_UNIQUE_PARTIAL:
        case OpTypes.MATCH_UNIQUE_FULL:
            sb.append(Tokens.T_MATCH);
            break;
        case OpTypes.IS_NULL:
            sb.append(Tokens.T_IS).append(' ').append(Tokens.T_NULL);
            break;
        case OpTypes.UNIQUE:
            sb.append(Tokens.T_UNIQUE);
            break;
        case OpTypes.EXISTS:
            sb.append(Tokens.T_EXISTS);
            break;
        case OpTypes.OVERLAPS:
            sb.append(Tokens.T_OVERLAPS);
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
    }
    if (getLeftNode() != null) {
        sb.append(" arg_left=[");
        sb.append(nodes[LEFT].describe(session, blanks + 1));
        sb.append(']');
    }
    if (getRightNode() != null) {
        sb.append(" arg_right=[");
        sb.append(nodes[RIGHT].describe(session, blanks + 1));
        sb.append(']');
    }
    return sb.toString();
}
