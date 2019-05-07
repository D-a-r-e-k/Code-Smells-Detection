StatementSchema compileCreateTrigger() {
    Table table;
    Boolean isForEachRow = null;
    boolean isNowait = false;
    boolean hasQueueSize = false;
    int queueSize = 0;
    int beforeOrAfterType;
    int operationType;
    String className;
    TriggerDef td;
    HsqlName name;
    HsqlName otherName = null;
    OrderedHashSet columns = null;
    int[] updateColumnIndexes = null;
    read();
    name = readNewSchemaObjectName(SchemaObject.TRIGGER, true);
    switch(token.tokenType) {
        case Tokens.INSTEAD:
            beforeOrAfterType = TriggerDef.getTiming(Tokens.INSTEAD);
            read();
            readThis(Tokens.OF);
            break;
        case Tokens.BEFORE:
        case Tokens.AFTER:
            beforeOrAfterType = TriggerDef.getTiming(token.tokenType);
            read();
            break;
        default:
            throw unexpectedToken();
    }
    switch(token.tokenType) {
        case Tokens.INSERT:
        case Tokens.DELETE:
            operationType = TriggerDef.getOperationType(token.tokenType);
            read();
            break;
        case Tokens.UPDATE:
            operationType = TriggerDef.getOperationType(token.tokenType);
            read();
            if (token.tokenType == Tokens.OF && beforeOrAfterType != TriggerDef.INSTEAD) {
                read();
                columns = new OrderedHashSet();
                readColumnNameList(columns, null, false);
            }
            break;
        default:
            throw unexpectedToken();
    }
    readThis(Tokens.ON);
    table = readTableName();
    if (token.tokenType == Tokens.BEFORE) {
        read();
        checkIsSimpleName();
        otherName = readNewSchemaObjectName(SchemaObject.TRIGGER, true);
    }
    name.setSchemaIfNull(table.getSchemaName());
    checkSchemaUpdateAuthorisation(name.schema);
    if (beforeOrAfterType == TriggerDef.INSTEAD) {
        if (!table.isView() || ((View) table).getCheckOption() == SchemaObject.ViewCheckModes.CHECK_CASCADE) {
            throw Error.error(ErrorCode.X_42538, name.schema.name);
        }
    } else {
        if (table.isView()) {
            throw Error.error(ErrorCode.X_42538, name.schema.name);
        }
    }
    if (name.schema != table.getSchemaName()) {
        throw Error.error(ErrorCode.X_42505, name.schema.name);
    }
    name.parent = table.getName();
    database.schemaManager.checkSchemaObjectNotExists(name);
    if (columns != null) {
        updateColumnIndexes = table.getColumnIndexes(columns);
        for (int i = 0; i < updateColumnIndexes.length; i++) {
            if (updateColumnIndexes[i] == -1) {
                throw Error.error(ErrorCode.X_42544, (String) columns.get(i));
            }
        }
    }
    Expression condition = null;
    String oldTableName = null;
    String newTableName = null;
    String oldRowName = null;
    String newRowName = null;
    Table[] transitions = new Table[4];
    RangeVariable[] rangeVars = new RangeVariable[4];
    String conditionSQL = null;
    if (token.tokenType == Tokens.REFERENCING) {
        read();
        if (token.tokenType != Tokens.OLD && token.tokenType != Tokens.NEW) {
            throw unexpectedToken();
        }
        while (true) {
            if (token.tokenType == Tokens.OLD) {
                if (operationType == StatementTypes.INSERT) {
                    throw unexpectedToken();
                }
                read();
                if (token.tokenType == Tokens.TABLE) {
                    if (Boolean.TRUE.equals(isForEachRow) || oldTableName != null || beforeOrAfterType == TriggerDef.BEFORE) {
                        throw unexpectedToken();
                    }
                    read();
                    readIfThis(Tokens.AS);
                    checkIsSimpleName();
                    read();
                    oldTableName = token.tokenString;
                    String n = oldTableName;
                    if (n.equals(newTableName) || n.equals(oldRowName) || n.equals(newRowName)) {
                        throw unexpectedToken();
                    }
                    isForEachRow = Boolean.FALSE;
                    HsqlName hsqlName = database.nameManager.newHsqlName(table.getSchemaName(), n, isDelimitedIdentifier(), SchemaObject.TRANSITION);
                    Table transition = new Table(table, hsqlName);
                    RangeVariable range = new RangeVariable(transition, null, null, null, compileContext);
                    transitions[TriggerDef.OLD_TABLE] = transition;
                    rangeVars[TriggerDef.OLD_TABLE] = range;
                } else {
                    if (Boolean.FALSE.equals(isForEachRow) || oldRowName != null) {
                        throw unexpectedToken();
                    }
                    readIfThis(Tokens.ROW);
                    readIfThis(Tokens.AS);
                    checkIsSimpleName();
                    oldRowName = token.tokenString;
                    read();
                    String n = oldRowName;
                    if (n.equals(newTableName) || n.equals(oldTableName) || n.equals(newRowName)) {
                        throw unexpectedToken();
                    }
                    isForEachRow = Boolean.TRUE;
                    HsqlName hsqlName = database.nameManager.newHsqlName(table.getSchemaName(), n, isDelimitedIdentifier(), SchemaObject.TRANSITION);
                    Table transition = new Table(table, hsqlName);
                    RangeVariable range = new RangeVariable(transition, null, null, null, compileContext);
                    transitions[TriggerDef.OLD_ROW] = transition;
                    rangeVars[TriggerDef.OLD_ROW] = range;
                }
            } else if (token.tokenType == Tokens.NEW) {
                if (operationType == StatementTypes.DELETE_WHERE) {
                    throw unexpectedToken();
                }
                read();
                if (token.tokenType == Tokens.TABLE) {
                    if (Boolean.TRUE.equals(isForEachRow) || newTableName != null || beforeOrAfterType == TriggerDef.BEFORE) {
                        throw unexpectedToken();
                    }
                    read();
                    readIfThis(Tokens.AS);
                    checkIsSimpleName();
                    newTableName = token.tokenString;
                    read();
                    isForEachRow = Boolean.FALSE;
                    String n = newTableName;
                    if (n.equals(oldTableName) || n.equals(oldRowName) || n.equals(newRowName)) {
                        throw unexpectedToken();
                    }
                    HsqlName hsqlName = database.nameManager.newHsqlName(table.getSchemaName(), n, isDelimitedIdentifier(), SchemaObject.TRANSITION);
                    Table transition = new Table(table, hsqlName);
                    RangeVariable range = new RangeVariable(transition, null, null, null, compileContext);
                    transitions[TriggerDef.NEW_TABLE] = transition;
                    rangeVars[TriggerDef.NEW_TABLE] = range;
                } else {
                    if (Boolean.FALSE.equals(isForEachRow) || newRowName != null) {
                        throw unexpectedToken();
                    }
                    readIfThis(Tokens.ROW);
                    readIfThis(Tokens.AS);
                    checkIsSimpleName();
                    newRowName = token.tokenString;
                    read();
                    isForEachRow = Boolean.TRUE;
                    String n = newRowName;
                    if (n.equals(oldTableName) || n.equals(newTableName) || n.equals(oldRowName)) {
                        throw unexpectedToken();
                    }
                    HsqlName hsqlName = database.nameManager.newHsqlName(table.getSchemaName(), n, isDelimitedIdentifier(), SchemaObject.TRANSITION);
                    Table transition = new Table(table, hsqlName);
                    RangeVariable range = new RangeVariable(transition, null, null, null, compileContext);
                    transitions[TriggerDef.NEW_ROW] = transition;
                    rangeVars[TriggerDef.NEW_ROW] = range;
                }
            } else {
                break;
            }
        }
    }
    if (Boolean.TRUE.equals(isForEachRow) && token.tokenType != Tokens.FOR) {
        throw unexpectedTokenRequire(Tokens.T_FOR);
    }
    if (token.tokenType == Tokens.FOR) {
        read();
        readThis(Tokens.EACH);
        if (token.tokenType == Tokens.ROW) {
            if (Boolean.FALSE.equals(isForEachRow)) {
                throw unexpectedToken();
            }
            isForEachRow = Boolean.TRUE;
        } else if (token.tokenType == Tokens.STATEMENT) {
            if (Boolean.TRUE.equals(isForEachRow) || beforeOrAfterType == TriggerDef.BEFORE) {
                throw unexpectedToken();
            }
            isForEachRow = Boolean.FALSE;
        } else {
            throw unexpectedToken();
        }
        read();
    }
    // 
    if (rangeVars[TriggerDef.OLD_TABLE] != null) {
    }
    if (rangeVars[TriggerDef.NEW_TABLE] != null) {
    }
    // 
    if (Tokens.T_QUEUE.equals(token.tokenString)) {
        read();
        queueSize = readInteger();
        hasQueueSize = true;
    }
    if (Tokens.T_NOWAIT.equals(token.tokenString)) {
        read();
        isNowait = true;
    }
    if (token.tokenType == Tokens.WHEN && beforeOrAfterType != TriggerDef.INSTEAD) {
        read();
        readThis(Tokens.OPENBRACKET);
        int position = getPosition();
        isCheckOrTriggerCondition = true;
        condition = XreadBooleanValueExpression();
        conditionSQL = getLastPart(position);
        isCheckOrTriggerCondition = false;
        readThis(Tokens.CLOSEBRACKET);
        HsqlList unresolved = condition.resolveColumnReferences(rangeVars, null);
        ExpressionColumn.checkColumnsResolved(unresolved);
        condition.resolveTypes(session, null);
        if (condition.getDataType() != Type.SQL_BOOLEAN) {
            throw Error.error(ErrorCode.X_42568);
        }
    }
    if (isForEachRow == null) {
        isForEachRow = Boolean.FALSE;
    }
    if (token.tokenType == Tokens.CALL) {
        int position = getPosition();
        try {
            read();
            checkIsSimpleName();
            checkIsDelimitedIdentifier();
            className = token.tokenString;
            read();
            td = new TriggerDef(name, beforeOrAfterType, operationType, isForEachRow.booleanValue(), table, transitions, rangeVars, condition, conditionSQL, updateColumnIndexes, className, isNowait, queueSize);
            String sql = getLastPart();
            Object[] args = new Object[] { td, otherName };
            return new StatementSchema(sql, StatementTypes.CREATE_TRIGGER, args, null, table.getName());
        } catch (HsqlException e) {
            rewind(position);
        }
    }
    // 
    if (hasQueueSize) {
        throw unexpectedToken(Tokens.T_QUEUE);
    }
    if (isNowait) {
        throw unexpectedToken(Tokens.T_NOWAIT);
    }
    Routine routine = compileTriggerRoutine(table, rangeVars, beforeOrAfterType, operationType);
    td = new TriggerDefSQL(name, beforeOrAfterType, operationType, isForEachRow.booleanValue(), table, transitions, rangeVars, condition, conditionSQL, updateColumnIndexes, routine);
    String sql = getLastPart();
    Object[] args = new Object[] { td, otherName };
    return new StatementSchema(sql, StatementTypes.CREATE_TRIGGER, args, null, table.getName());
}
