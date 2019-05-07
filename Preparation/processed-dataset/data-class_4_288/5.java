public String getSQL() {
    StringBuffer sb = new StringBuffer(64);
    if (opType == OpTypes.VALUE) {
        return super.getSQL();
    }
    String left = getContextSQL(nodes[LEFT]);
    String right = getContextSQL(nodes.length > 1 ? nodes[RIGHT] : null);
    switch(opType) {
        case OpTypes.NOT:
            if (nodes[LEFT].opType == OpTypes.IS_NULL) {
                sb.append(getContextSQL(nodes[LEFT].nodes[LEFT])).append(' ').append(Tokens.T_IS).append(' ').append(Tokens.T_NOT).append(' ').append(Tokens.T_NULL);
                return sb.toString();
            }
            if (nodes[LEFT].opType == OpTypes.NOT_DISTINCT) {
                sb.append(getContextSQL(nodes[LEFT].nodes[LEFT])).append(' ').append(Tokens.T_IS).append(' ').append(Tokens.T_DISTINCT).append(' ').append(Tokens.T_FROM).append(' ').append(getContextSQL(nodes[LEFT].nodes[RIGHT]));
                return sb.toString();
            }
            sb.append(Tokens.T_NOT).append(' ').append(left);
            return sb.toString();
        case OpTypes.NOT_DISTINCT:
            sb.append(Tokens.T_NOT).append(' ').append(getContextSQL(nodes[LEFT].nodes[LEFT])).append(' ').append(Tokens.T_IS).append(' ').append(Tokens.T_DISTINCT).append(' ').append(Tokens.T_FROM).append(' ').append(getContextSQL(nodes[LEFT].nodes[RIGHT]));
            return sb.toString();
        case OpTypes.IS_NULL:
            sb.append(left).append(' ').append(Tokens.T_IS).append(' ').append(Tokens.T_NULL);
            return sb.toString();
        case OpTypes.UNIQUE:
            sb.append(' ').append(Tokens.T_UNIQUE).append(' ');
            break;
        case OpTypes.EXISTS:
            sb.append(' ').append(Tokens.T_EXISTS).append(' ');
            break;
        case OpTypes.EQUAL:
            sb.append(left).append('=').append(right);
            return sb.toString();
        case OpTypes.GREATER_EQUAL:
            sb.append(left).append(">=").append(right);
            return sb.toString();
        case OpTypes.GREATER:
            sb.append(left).append('>').append(right);
            return sb.toString();
        case OpTypes.SMALLER:
            sb.append(left).append('<').append(right);
            return sb.toString();
        case OpTypes.SMALLER_EQUAL:
            sb.append(left).append("<=").append(right);
            return sb.toString();
        case OpTypes.NOT_EQUAL:
            if (Tokens.T_NULL.equals(right)) {
                sb.append(left).append(" IS NOT ").append(right);
            } else {
                sb.append(left).append("!=").append(right);
            }
            return sb.toString();
        case OpTypes.AND:
            sb.append(left).append(' ').append(Tokens.T_AND).append(' ').append(right);
            return sb.toString();
        case OpTypes.OR:
            sb.append(left).append(' ').append(Tokens.T_OR).append(' ').append(right);
            return sb.toString();
        case OpTypes.IN:
            sb.append(left).append(' ').append(Tokens.T_IN).append(' ').append(right);
            return sb.toString();
        case OpTypes.MATCH_SIMPLE:
            sb.append(left).append(' ').append(Tokens.T_MATCH).append(' ').append(right);
            return sb.toString();
        case OpTypes.MATCH_PARTIAL:
            sb.append(left).append(' ').append(Tokens.T_MATCH).append(' ').append(Tokens.PARTIAL).append(right);
            return sb.toString();
        case OpTypes.MATCH_FULL:
            sb.append(left).append(' ').append(Tokens.T_MATCH).append(' ').append(Tokens.FULL).append(right);
            return sb.toString();
        case OpTypes.MATCH_UNIQUE_SIMPLE:
            sb.append(left).append(' ').append(Tokens.T_MATCH).append(' ').append(Tokens.UNIQUE).append(right);
            return sb.toString();
        case OpTypes.MATCH_UNIQUE_PARTIAL:
            sb.append(left).append(' ').append(Tokens.T_MATCH).append(' ').append(Tokens.UNIQUE).append(' ').append(Tokens.PARTIAL).append(right);
            return sb.toString();
        case OpTypes.MATCH_UNIQUE_FULL:
            sb.append(left).append(' ').append(Tokens.T_MATCH).append(' ').append(Tokens.UNIQUE).append(' ').append(Tokens.FULL).append(right);
            return sb.toString();
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
    }
    return sb.toString();
}
