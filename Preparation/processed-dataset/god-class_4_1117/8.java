public String getSQL() {
    switch(funcType) {
        case FUNC_EXTRACT:
        case FUNC_TRIM_CHAR:
        case FUNC_OVERLAY_CHAR:
            return super.getSQL();
        case FUNC_DATABASE:
        case FUNC_ISAUTOCOMMIT:
        case FUNC_ISREADONLYSESSION:
        case FUNC_ISREADONLYDATABASE:
        case FUNC_ISREADONLYDATABASEFILES:
        case FUNC_ISOLATION_LEVEL:
        case FUNC_SESSION_ISOLATION_LEVEL:
        case FUNC_DATABASE_ISOLATION_LEVEL:
        case FUNC_TRANSACTION_CONTROL:
        case FUNC_TIMEZONE:
        case FUNC_SESSION_TIMEZONE:
        case FUNC_DATABASE_TIMEZONE:
        case FUNC_DATABASE_VERSION:
        case FUNC_PI:
        case FUNC_IDENTITY:
            return new StringBuffer(name).append(Tokens.T_OPENBRACKET).append(Tokens.T_CLOSEBRACKET).toString();
        case FUNC_TIMESTAMPADD:
            {
                String token = Tokens.getSQLTSIString(((Number) nodes[0].getValue(null)).intValue());
                return // 
                // 
                // 
                new StringBuffer(Tokens.T_TIMESTAMPADD).append(Tokens.T_OPENBRACKET).append(token).append(Tokens.T_COMMA).append(nodes[1].getSQL()).append(Tokens.T_COMMA).append(nodes[2].getSQL()).append(Tokens.T_CLOSEBRACKET).toString();
            }
        case FUNC_TIMESTAMPDIFF:
            {
                String token = Tokens.getSQLTSIString(((Number) nodes[0].getValue(null)).intValue());
                return // 
                // 
                // 
                new StringBuffer(Tokens.T_TIMESTAMPDIFF).append(Tokens.T_OPENBRACKET).append(token).append(Tokens.T_COMMA).append(nodes[1].getSQL()).append(Tokens.T_COMMA).append(nodes[2].getSQL()).append(Tokens.T_CLOSEBRACKET).toString();
            }
        case FUNC_RAND:
            {
                StringBuffer sb = new StringBuffer(name).append('(');
                if (nodes[0] != null) {
                    sb.append(nodes[0].getSQL());
                }
                sb.append(')');
                return sb.toString();
            }
        case FUNC_ASCII:
        case FUNC_ACOS:
        case FUNC_ASIN:
        case FUNC_ATAN:
        case FUNC_CHAR:
        case FUNC_COS:
        case FUNC_COT:
        case FUNC_DEGREES:
        case FUNC_SIN:
        case FUNC_TAN:
        case FUNC_LOG10:
        case FUNC_RADIANS:
        case FUNC_ROUNDMAGIC:
        case FUNC_SIGN:
        case FUNC_SOUNDEX:
        case FUNC_SPACE:
        case FUNC_REVERSE:
        case FUNC_HEXTORAW:
        case FUNC_RAWTOHEX:
            {
                return // 
                new StringBuffer(name).append('(').append(nodes[0].getSQL()).append(')').toString();
            }
        case FUNC_ATAN2:
        case FUNC_BITAND:
        case FUNC_BITOR:
        case FUNC_BITXOR:
        case FUNC_DIFFERENCE:
        case FUNC_REPEAT:
        case FUNC_LEFT:
        case FUNC_RIGHT:
        case FUNC_ROUND:
        case FUNC_CRYPT_KEY:
        case FUNC_TRUNCATE:
        case FUNC_TIMESTAMP:
        case FUNC_TO_CHAR:
        case FUNC_REGEXP_MATCHES:
            {
                return // 
                // 
                new StringBuffer(name).append('(').append(nodes[0].getSQL()).append(Tokens.T_COMMA).append(nodes[1].getSQL()).append(')').toString();
            }
        case FUNC_REPLACE:
            {
                return // 
                // 
                // 
                new StringBuffer(name).append('(').append(nodes[0].getSQL()).append(Tokens.T_COMMA).append(nodes[1].getSQL()).append(Tokens.T_COMMA).append(nodes[2].getSQL()).append(')').toString();
            }
        default:
            return super.getSQL();
    }
}
