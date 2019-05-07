private StatementSchema compileRoleGrantOrRevoke(boolean grant) {
    Grantee grantor = session.getGrantee();
    OrderedHashSet roleList = new OrderedHashSet();
    OrderedHashSet granteeList = new OrderedHashSet();
    boolean cascade = false;
    if (!grant && token.tokenType == Tokens.ADMIN) {
        throw unsupportedFeature();
    }
    while (true) {
        checkIsSimpleName();
        roleList.add(token.tokenString);
        read();
        if (token.tokenType == Tokens.COMMA) {
            read();
            continue;
        }
        break;
    }
    if (grant) {
        readThis(Tokens.TO);
    } else {
        readThis(Tokens.FROM);
    }
    while (true) {
        checkIsSimpleName();
        granteeList.add(token.tokenString);
        read();
        if (token.tokenType == Tokens.COMMA) {
            read();
        } else {
            break;
        }
    }
    if (grant) {
        if (token.tokenType == Tokens.WITH) {
            throw unsupportedFeature();
        }
    }
    if (token.tokenType == Tokens.GRANTED) {
        read();
        readThis(Tokens.BY);
        if (token.tokenType == Tokens.CURRENT_USER) {
            read();
        } else {
            readThis(Tokens.CURRENT_ROLE);
            if (session.getRole() == null) {
                throw Error.error(ErrorCode.X_0P000);
            }
            grantor = session.getRole();
        }
    }
    if (!grant) {
        if (token.tokenType == Tokens.CASCADE) {
            cascade = true;
            read();
        } else {
            readThis(Tokens.RESTRICT);
        }
    }
    int type = grant ? StatementTypes.GRANT_ROLE : StatementTypes.REVOKE_ROLE;
    Object[] args = new Object[] { granteeList, roleList, grantor, Boolean.valueOf(cascade) };
    String sql = getLastPart();
    StatementSchema cs = new StatementSchema(sql, type, args);
    return cs;
}
