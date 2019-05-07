private StatementSchema compileRightGrantOrRevoke(boolean grant) {
    OrderedHashSet granteeList = new OrderedHashSet();
    Grantee grantor = null;
    Right right = null;
    //        SchemaObject   schemaObject; 
    HsqlName objectName = null;
    boolean isTable = false;
    boolean isUsage = false;
    boolean isExec = false;
    boolean isAll = false;
    boolean isGrantOption = false;
    boolean cascade = false;
    if (!grant) {
        if (token.tokenType == Tokens.GRANT) {
            read();
            readThis(Tokens.OPTION);
            readThis(Tokens.FOR);
            isGrantOption = true;
        } else if (token.tokenType == Tokens.HIERARCHY) {
            throw unsupportedFeature();
        }
    }
    // ALL means all the rights the grantor can grant 
    if (token.tokenType == Tokens.ALL) {
        read();
        if (token.tokenType == Tokens.PRIVILEGES) {
            read();
        }
        right = Right.fullRights;
        isAll = true;
    } else {
        right = new Right();
        boolean loop = true;
        while (loop) {
            checkIsNotQuoted();
            int rightType = GranteeManager.getCheckSingleRight(token.tokenString);
            int grantType = token.tokenType;
            OrderedHashSet columnSet = null;
            read();
            switch(grantType) {
                case Tokens.REFERENCES:
                case Tokens.SELECT:
                case Tokens.INSERT:
                case Tokens.UPDATE:
                    if (token.tokenType == Tokens.OPENBRACKET) {
                        columnSet = readColumnNames(false);
                    }
                // fall through 
                case Tokens.DELETE:
                case Tokens.TRIGGER:
                    if (right == null) {
                        right = new Right();
                    }
                    right.set(rightType, columnSet);
                    isTable = true;
                    break;
                case Tokens.USAGE:
                    if (isTable) {
                        throw unexpectedToken();
                    }
                    right = Right.fullRights;
                    isUsage = true;
                    loop = false;
                    continue;
                case Tokens.EXECUTE:
                    if (isTable) {
                        throw unexpectedToken();
                    }
                    right = Right.fullRights;
                    isExec = true;
                    loop = false;
                    continue;
            }
            if (token.tokenType == Tokens.COMMA) {
                read();
                continue;
            }
            break;
        }
    }
    readThis(Tokens.ON);
    int objectType = 0;
    switch(token.tokenType) {
        case Tokens.CLASS:
            if (!isExec && !isAll) {
                throw unexpectedToken();
            }
            read();
            if (!isSimpleName() || !isDelimitedIdentifier()) {
                throw Error.error(ErrorCode.X_42569);
            }
            objectType = SchemaObject.FUNCTION;
            objectName = readNewSchemaObjectName(SchemaObject.FUNCTION, false);
            break;
        case Tokens.SPECIFIC:
            {
                if (!isExec && !isAll) {
                    throw unexpectedToken();
                }
                read();
                switch(token.tokenType) {
                    case Tokens.ROUTINE:
                    case Tokens.PROCEDURE:
                    case Tokens.FUNCTION:
                        read();
                        break;
                    default:
                        throw unexpectedToken();
                }
                objectType = SchemaObject.SPECIFIC_ROUTINE;
                break;
            }
        case Tokens.FUNCTION:
            if (!isExec && !isAll) {
                throw unexpectedToken();
            }
            read();
            objectType = SchemaObject.FUNCTION;
            break;
        case Tokens.PROCEDURE:
            if (!isExec && !isAll) {
                throw unexpectedToken();
            }
            read();
            objectType = SchemaObject.PROCEDURE;
            break;
        case Tokens.ROUTINE:
            if (!isExec && !isAll) {
                throw unexpectedToken();
            }
            read();
            objectType = SchemaObject.ROUTINE;
            break;
        case Tokens.TYPE:
            if (!isUsage && !isAll) {
                throw unexpectedToken();
            }
            read();
            objectType = SchemaObject.TYPE;
            break;
        case Tokens.DOMAIN:
            if (!isUsage && !isAll) {
                throw unexpectedToken();
            }
            read();
            objectType = SchemaObject.DOMAIN;
            break;
        case Tokens.SEQUENCE:
            if (!isUsage && !isAll) {
                throw unexpectedToken();
            }
            read();
            objectType = SchemaObject.SEQUENCE;
            break;
        case Tokens.CHARACTER:
            if (!isUsage && !isAll) {
                throw unexpectedToken();
            }
            read();
            readThis(Tokens.SET);
            objectType = SchemaObject.CHARSET;
            break;
        case Tokens.TABLE:
        default:
            if (!isTable && !isAll) {
                throw unexpectedToken();
            }
            readIfThis(Tokens.TABLE);
            objectType = SchemaObject.TABLE;
    }
    objectName = readNewSchemaObjectName(objectType, false);
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
            read();
            readThis(Tokens.GRANT);
            readThis(Tokens.OPTION);
            isGrantOption = true;
        }
        /** @todo - implement */
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
    } else {
        if (token.tokenType == Tokens.CASCADE) {
            cascade = true;
            read();
        } else {
            readThis(Tokens.RESTRICT);
        }
    }
    int typee = grant ? StatementTypes.GRANT : StatementTypes.REVOKE;
    Object[] args = new Object[] { granteeList, objectName, right, grantor, Boolean.valueOf(cascade), Boolean.valueOf(isGrantOption) };
    String sql = getLastPart();
    StatementSchema cs = new StatementSchema(sql, typee, args);
    return cs;
}
