Result getResult(Session session) {
    SchemaManager schemaManager = session.database.schemaManager;
    if (this.isExplain) {
        return Result.newSingleColumnStringResult("OPERATION", describe(session));
    }
    switch(type) {
        case StatementTypes.RENAME_OBJECT:
            {
                HsqlName name = (HsqlName) arguments[0];
                HsqlName newName = (HsqlName) arguments[1];
                SchemaObject object;
                if (name.type == SchemaObject.CATALOG) {
                    try {
                        session.checkAdmin();
                        session.checkDDLWrite();
                        name.rename(newName);
                        break;
                    } catch (HsqlException e) {
                        return Result.newErrorResult(e, sql);
                    }
                } else if (name.type == SchemaObject.SCHEMA) {
                    /**
                     * @todo 1.9.0 - review for schemas referenced in
                     *  external view or trigger definitions
                     */
                    checkSchemaUpdateAuthorisation(session, name);
                    schemaManager.checkSchemaNameCanChange(name);
                    schemaManager.renameSchema(name, newName);
                    break;
                }
                try {
                    name.setSchemaIfNull(session.getCurrentSchemaHsqlName());
                    if (name.type == SchemaObject.COLUMN) {
                        Table table = schemaManager.getUserTable(session, name.parent);
                        int index = table.getColumnIndex(name.name);
                        object = table.getColumn(index);
                    } else {
                        object = schemaManager.getSchemaObject(name);
                        if (object == null) {
                            throw Error.error(ErrorCode.X_42501, name.name);
                        }
                        name = object.getName();
                    }
                    checkSchemaUpdateAuthorisation(session, name.schema);
                    newName.setSchemaIfNull(name.schema);
                    if (name.schema != newName.schema) {
                        HsqlException e = Error.error(ErrorCode.X_42505);
                        return Result.newErrorResult(e, sql);
                    }
                    newName.parent = name.parent;
                    switch(object.getType()) {
                        case SchemaObject.COLUMN:
                            HsqlName parent = object.getName().parent;
                            schemaManager.checkColumnIsReferenced(parent, object.getName());
                            Table table = schemaManager.getUserTable(session, parent);
                            table.renameColumn((ColumnSchema) object, newName);
                            break;
                        default:
                            schemaManager.renameSchemaObject(name, newName);
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.ALTER_SEQUENCE:
            {
                try {
                    NumberSequence sequence = (NumberSequence) arguments[0];
                    NumberSequence settings = (NumberSequence) arguments[1];
                    checkSchemaUpdateAuthorisation(session, sequence.getSchemaName());
                    sequence.reset(settings);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.ALTER_DOMAIN:
        case StatementTypes.ALTER_ROUTINE:
        case StatementTypes.ALTER_TYPE:
        case StatementTypes.ALTER_TABLE:
        case StatementTypes.ALTER_TRANSFORM:
            {
                try {
                    session.parser.reset(sql);
                    session.parser.read();
                    session.parser.processAlter();
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.ALTER_VIEW:
            {
                View view = (View) arguments[0];
                try {
                    checkSchemaUpdateAuthorisation(session, view.getSchemaName());
                    View oldView = (View) schemaManager.getSchemaObject(view.getName());
                    if (oldView == null) {
                        throw Error.error(ErrorCode.X_42501, view.getName().name);
                    }
                    view.setName(oldView.getName());
                    view.compile(session, null);
                    OrderedHashSet dependents = schemaManager.getReferencingObjectNames(oldView.getName());
                    if (dependents.getCommonElementCount(view.getReferences()) > 0) {
                        throw Error.error(ErrorCode.X_42502);
                    }
                    int i = schemaManager.getTableIndex(oldView);
                    schemaManager.setTable(i, view);
                    OrderedHashSet set = new OrderedHashSet();
                    set.add(view);
                    try {
                        schemaManager.recompileDependentObjects(set);
                    } catch (HsqlException e) {
                        schemaManager.setTable(i, oldView);
                        schemaManager.recompileDependentObjects(set);
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.DROP_COLUMN:
            {
                try {
                    HsqlName name = (HsqlName) arguments[0];
                    int objectType = ((Integer) arguments[1]).intValue();
                    boolean cascade = ((Boolean) arguments[2]).booleanValue();
                    boolean ifExists = ((Boolean) arguments[3]).booleanValue();
                    Table table = schemaManager.getUserTable(session, name.parent);
                    int colindex = table.getColumnIndex(name.name);
                    if (table.getColumnCount() == 1) {
                        throw Error.error(ErrorCode.X_42591);
                    }
                    checkSchemaUpdateAuthorisation(session, table.getSchemaName());
                    session.commit(false);
                    TableWorks tableWorks = new TableWorks(session, table);
                    tableWorks.dropColumn(colindex, cascade);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.DROP_ASSERTION:
        case StatementTypes.DROP_CHARACTER_SET:
        case StatementTypes.DROP_COLLATION:
        case StatementTypes.DROP_TYPE:
        case StatementTypes.DROP_DOMAIN:
        case StatementTypes.DROP_ROLE:
        case StatementTypes.DROP_USER:
        case StatementTypes.DROP_ROUTINE:
        case StatementTypes.DROP_SCHEMA:
        case StatementTypes.DROP_SEQUENCE:
        case StatementTypes.DROP_TABLE:
        case StatementTypes.DROP_TRANSFORM:
        case StatementTypes.DROP_TRANSLATION:
        case StatementTypes.DROP_TRIGGER:
        case StatementTypes.DROP_CAST:
        case StatementTypes.DROP_ORDERING:
        case StatementTypes.DROP_VIEW:
        case StatementTypes.DROP_INDEX:
        case StatementTypes.DROP_CONSTRAINT:
            {
                try {
                    HsqlName name = (HsqlName) arguments[0];
                    int objectType = ((Integer) arguments[1]).intValue();
                    boolean cascade = ((Boolean) arguments[2]).booleanValue();
                    boolean ifExists = ((Boolean) arguments[3]).booleanValue();
                    switch(type) {
                        case StatementTypes.DROP_ROLE:
                        case StatementTypes.DROP_USER:
                            session.checkAdmin();
                            session.checkDDLWrite();
                            break;
                        case StatementTypes.DROP_SCHEMA:
                            checkSchemaUpdateAuthorisation(session, name);
                            if (!schemaManager.schemaExists(name.name)) {
                                if (ifExists) {
                                    return Result.updateZeroResult;
                                }
                            }
                            break;
                        default:
                            if (name.schema == null) {
                                name.schema = session.getCurrentSchemaHsqlName();
                            } else {
                                if (!schemaManager.schemaExists(name.schema.name)) {
                                    if (ifExists) {
                                        return Result.updateZeroResult;
                                    }
                                }
                            }
                            name.schema = schemaManager.getUserSchemaHsqlName(name.schema.name);
                            checkSchemaUpdateAuthorisation(session, name.schema);
                            SchemaObject object = schemaManager.getSchemaObject(name);
                            if (object == null) {
                                if (ifExists) {
                                    return Result.updateZeroResult;
                                }
                                throw Error.error(ErrorCode.X_42501, name.name);
                            }
                            if (name.type == SchemaObject.SPECIFIC_ROUTINE) {
                                name = ((Routine) object).getSpecificName();
                            } else {
                                name = object.getName();
                            }
                    }
                    if (!cascade) {
                        schemaManager.checkObjectIsReferenced(name);
                    }
                    switch(type) {
                        case StatementTypes.DROP_ROLE:
                            dropRole(session, name, cascade);
                            break;
                        case StatementTypes.DROP_USER:
                            dropUser(session, name, cascade);
                            break;
                        case StatementTypes.DROP_SCHEMA:
                            dropSchema(session, name, cascade);
                            break;
                        case StatementTypes.DROP_ASSERTION:
                            break;
                        case StatementTypes.DROP_CHARACTER_SET:
                        case StatementTypes.DROP_COLLATION:
                        case StatementTypes.DROP_SEQUENCE:
                        case StatementTypes.DROP_TRIGGER:
                            dropObject(session, name, cascade);
                            break;
                        case StatementTypes.DROP_TYPE:
                            dropType(session, name, cascade);
                            break;
                        case StatementTypes.DROP_DOMAIN:
                            dropDomain(session, name, cascade);
                            break;
                        case StatementTypes.DROP_ROUTINE:
                            dropRoutine(session, name, cascade);
                            break;
                        case StatementTypes.DROP_TABLE:
                        case StatementTypes.DROP_VIEW:
                            dropTable(session, name, cascade);
                            break;
                        case StatementTypes.DROP_TRANSFORM:
                        case StatementTypes.DROP_TRANSLATION:
                        case StatementTypes.DROP_CAST:
                        case StatementTypes.DROP_ORDERING:
                            break;
                        case StatementTypes.DROP_INDEX:
                            checkSchemaUpdateAuthorisation(session, name.schema);
                            schemaManager.dropIndex(session, name);
                            break;
                        case StatementTypes.DROP_CONSTRAINT:
                            checkSchemaUpdateAuthorisation(session, name.schema);
                            schemaManager.dropConstraint(session, name, cascade);
                            break;
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.GRANT:
        case StatementTypes.REVOKE:
            {
                try {
                    boolean grant = type == StatementTypes.GRANT;
                    OrderedHashSet granteeList = (OrderedHashSet) arguments[0];
                    HsqlName name = (HsqlName) arguments[1];
                    this.setSchemaName(session, null, name);
                    name = schemaManager.getSchemaObjectName(name.schema, name.name, name.type, true);
                    SchemaObject schemaObject = schemaManager.getSchemaObject(name);
                    Right right = (Right) arguments[2];
                    Grantee grantor = (Grantee) arguments[3];
                    boolean cascade = ((Boolean) arguments[4]).booleanValue();
                    boolean isGrantOption = ((Boolean) arguments[5]).booleanValue();
                    if (grantor == null) {
                        grantor = isSchemaDefinition ? schemaName.owner : session.getGrantee();
                    }
                    GranteeManager gm = session.database.granteeManager;
                    switch(schemaObject.getType()) {
                        case SchemaObject.CHARSET:
                            System.out.println("grant charset!");
                            break;
                        case SchemaObject.VIEW:
                        case SchemaObject.TABLE:
                            {
                                Table t = (Table) schemaObject;
                                right.setColumns(t);
                                if (t.getTableType() == TableBase.TEMP_TABLE && !right.isFull()) {
                                    return Result.newErrorResult(Error.error(ErrorCode.X_42595), sql);
                                }
                            }
                    }
                    if (grant) {
                        gm.grant(granteeList, schemaObject, right, grantor, isGrantOption);
                    } else {
                        gm.revoke(granteeList, schemaObject, right, grantor, isGrantOption, cascade);
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.GRANT_ROLE:
        case StatementTypes.REVOKE_ROLE:
            {
                try {
                    boolean grant = type == StatementTypes.GRANT_ROLE;
                    OrderedHashSet granteeList = (OrderedHashSet) arguments[0];
                    OrderedHashSet roleList = (OrderedHashSet) arguments[1];
                    Grantee grantor = (Grantee) arguments[2];
                    boolean cascade = ((Boolean) arguments[3]).booleanValue();
                    GranteeManager gm = session.database.granteeManager;
                    gm.checkGranteeList(granteeList);
                    for (int i = 0; i < granteeList.size(); i++) {
                        String grantee = (String) granteeList.get(i);
                        gm.checkRoleList(grantee, roleList, grantor, grant);
                    }
                    if (grant) {
                        for (int i = 0; i < granteeList.size(); i++) {
                            String grantee = (String) granteeList.get(i);
                            for (int j = 0; j < roleList.size(); j++) {
                                String roleName = (String) roleList.get(j);
                                gm.grant(grantee, roleName, grantor);
                            }
                        }
                    } else {
                        for (int i = 0; i < granteeList.size(); i++) {
                            String grantee = (String) granteeList.get(i);
                            for (int j = 0; j < roleList.size(); j++) {
                                gm.revoke(grantee, (String) roleList.get(j), grantor);
                            }
                        }
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_ASSERTION:
            {
                return Result.updateZeroResult;
            }
        case StatementTypes.CREATE_CHARACTER_SET:
            {
                Charset charset = (Charset) arguments[0];
                try {
                    setOrCheckObjectName(session, null, charset.getName(), true);
                    schemaManager.addSchemaObject(charset);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_COLLATION:
            {
                return Result.updateZeroResult;
            }
        case StatementTypes.CREATE_ROLE:
            {
                try {
                    session.checkAdmin();
                    session.checkDDLWrite();
                    HsqlName name = (HsqlName) arguments[0];
                    session.database.getGranteeManager().addRole(name);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_USER:
            {
                HsqlName name = (HsqlName) arguments[0];
                String password = (String) arguments[1];
                Grantee grantor = (Grantee) arguments[2];
                boolean admin = ((Boolean) arguments[3]).booleanValue();
                try {
                    session.checkAdmin();
                    session.checkDDLWrite();
                    session.database.getUserManager().createUser(name, password);
                    if (admin) {
                        session.database.getGranteeManager().grant(name.name, SqlInvariants.DBA_ADMIN_ROLE_NAME, grantor);
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_SCHEMA:
            {
                HsqlName name = (HsqlName) arguments[0];
                Grantee owner = (Grantee) arguments[1];
                try {
                    session.checkDDLWrite();
                    if (schemaManager.schemaExists(name.name)) {
                        if (session.isProcessingScript && SqlInvariants.PUBLIC_SCHEMA.equals(name.name)) {
                        } else {
                            throw Error.error(ErrorCode.X_42504, name.name);
                        }
                    } else {
                        schemaManager.createSchema(name, owner);
                        // always include authorization 
                        Schema schema = schemaManager.findSchema(name.name);
                        this.sql = schema.getSQL();
                        if (session.isProcessingScript() && session.database.getProperties().isVersion18()) {
                            session.setSchema(schema.getName().name);
                        }
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_ROUTINE:
            {
                Routine routine = (Routine) arguments[0];
                try {
                    routine.resolve(session);
                    setOrCheckObjectName(session, null, routine.getName(), false);
                    schemaManager.addSchemaObject(routine);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_ALIAS:
            {
                HsqlName name = (HsqlName) arguments[0];
                Routine[] routines = (Routine[]) arguments[1];
                try {
                    session.checkAdmin();
                    session.checkDDLWrite();
                    if (name != null) {
                        for (int i = 0; i < routines.length; i++) {
                            routines[i].setName(name);
                            schemaManager.addSchemaObject(routines[i]);
                        }
                    }
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_SEQUENCE:
            {
                NumberSequence sequence = (NumberSequence) arguments[0];
                try {
                    setOrCheckObjectName(session, null, sequence.getName(), true);
                    schemaManager.addSchemaObject(sequence);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_DOMAIN:
            {
                Type type = (Type) arguments[0];
                Constraint[] constraints = type.userTypeModifier.getConstraints();
                try {
                    setOrCheckObjectName(session, null, type.getName(), true);
                    for (int i = 0; i < constraints.length; i++) {
                        Constraint c = constraints[i];
                        setOrCheckObjectName(session, type.getName(), c.getName(), true);
                        schemaManager.addSchemaObject(c);
                    }
                    schemaManager.addSchemaObject(type);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_TABLE:
            {
                Table table = (Table) arguments[0];
                HsqlArrayList tempConstraints = (HsqlArrayList) arguments[1];
                StatementDMQL statement = (StatementDMQL) arguments[2];
                HsqlArrayList foreignConstraints = null;
                try {
                    setOrCheckObjectName(session, null, table.getName(), true);
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
                try {
                    if (isSchemaDefinition) {
                        foreignConstraints = new HsqlArrayList();
                    }
                    if (tempConstraints != null) {
                        table = ParserDDL.addTableConstraintDefinitions(session, table, tempConstraints, foreignConstraints, true);
                        arguments[1] = foreignConstraints;
                    }
                    table.compile(session, null);
                    schemaManager.addSchemaObject(table);
                    if (statement != null) {
                        Result result = statement.execute(session);
                        table.insertIntoTable(session, result);
                    }
                    if (table.hasLobColumn) {
                        RowIterator it = table.rowIterator(session);
                        while (it.hasNext()) {
                            Row row = it.getNextRow();
                            Object[] data = row.getData();
                            session.sessionData.adjustLobUsageCount(table, data, 1);
                        }
                    }
                    return Result.updateZeroResult;
                } catch (HsqlException e) {
                    schemaManager.removeExportedKeys(table);
                    schemaManager.removeDependentObjects(table.getName());
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_TRANSFORM:
            return Result.updateZeroResult;
        case StatementTypes.CREATE_TRANSLATION:
            return Result.updateZeroResult;
        case StatementTypes.CREATE_TRIGGER:
            {
                TriggerDef trigger = (TriggerDef) arguments[0];
                HsqlName otherName = (HsqlName) arguments[1];
                try {
                    checkSchemaUpdateAuthorisation(session, trigger.getSchemaName());
                    schemaManager.checkSchemaObjectNotExists(trigger.getName());
                    if (otherName != null) {
                        if (schemaManager.getSchemaObject(otherName) == null) {
                            throw Error.error(ErrorCode.X_42501, otherName.name);
                        }
                    }
                    trigger.table.addTrigger(trigger, otherName);
                    schemaManager.addSchemaObject(trigger);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_CAST:
            return Result.updateZeroResult;
        case StatementTypes.CREATE_TYPE:
            {
                Type type = (Type) arguments[0];
                try {
                    setOrCheckObjectName(session, null, type.getName(), true);
                    schemaManager.addSchemaObject(type);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_ORDERING:
            return Result.updateZeroResult;
        case StatementTypes.CREATE_VIEW:
            {
                View view = (View) arguments[0];
                try {
                    checkSchemaUpdateAuthorisation(session, view.getSchemaName());
                    schemaManager.checkSchemaObjectNotExists(view.getName());
                    view.compile(session, null);
                    schemaManager.addSchemaObject(view);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.CREATE_INDEX:
            {
                Table table;
                HsqlName name;
                int[] indexColumns;
                boolean unique;
                table = (Table) arguments[0];
                indexColumns = (int[]) arguments[1];
                name = (HsqlName) arguments[2];
                unique = ((Boolean) arguments[3]).booleanValue();
                try {
                    /*
                            Index index        = table.getIndexForColumns(indexColumns);

                            if (index != null
                                    && ArrayUtil.areEqual(indexColumns, index.getColumns(),
                                                          indexColumns.length, unique)) {
                                if (index.isUnique() || !unique) {
                                    return;
                                }
                            }
                    */
                    setOrCheckObjectName(session, table.getName(), name, true);
                    TableWorks tableWorks = new TableWorks(session, table);
                    tableWorks.addIndex(indexColumns, name, unique);
                    break;
                } catch (HsqlException e) {
                    return Result.newErrorResult(e, sql);
                }
            }
        case StatementTypes.COMMENT:
            {
                HsqlName name = (HsqlName) arguments[0];
                String comment = (String) arguments[1];
                switch(name.type) {
                    case SchemaObject.COLUMN:
                        {
                            Table table = (Table) schemaManager.getSchemaObject(name.parent.name, name.parent.schema.name, SchemaObject.TABLE);
                            if (!session.getGrantee().isFullyAccessibleByRole(table.getName())) {
                                throw Error.error(ErrorCode.X_42501);
                            }
                            int index = table.getColumnIndex(name.name);
                            if (index < 0) {
                                throw Error.error(ErrorCode.X_42501);
                            }
                            ColumnSchema column = table.getColumn(index);
                            column.getName().comment = comment;
                            break;
                        }
                    case SchemaObject.ROUTINE:
                        {
                            RoutineSchema routine = (RoutineSchema) schemaManager.getSchemaObject(name.name, name.schema.name, SchemaObject.ROUTINE);
                            if (!session.getGrantee().isFullyAccessibleByRole(routine.getName())) {
                                throw Error.error(ErrorCode.X_42501);
                            }
                            routine.getName().comment = comment;
                            break;
                        }
                    case SchemaObject.TABLE:
                        {
                            Table table = (Table) schemaManager.getSchemaObject(name.name, name.schema.name, SchemaObject.TABLE);
                            if (!session.getGrantee().isFullyAccessibleByRole(table.getName())) {
                                throw Error.error(ErrorCode.X_42501);
                            }
                            table.getName().comment = comment;
                            break;
                        }
                }
                break;
            }
        // for logging only 
        case StatementTypes.LOG_SCHEMA_STATEMENT:
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "CompiledStateemntSchema");
    }
    return Result.updateZeroResult;
}
