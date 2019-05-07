/**
     * Retrieves a CALL Statement from this parse context.
     */
// to do call argument name and type resolution 
StatementDMQL compileCallStatement(RangeVariable[] outerRanges, boolean isStrictlyProcedure) {
    read();
    if (isIdentifier()) {
        checkValidCatalogName(token.namePrePrefix);
        RoutineSchema routineSchema = (RoutineSchema) database.schemaManager.findSchemaObject(token.tokenString, session.getSchemaName(token.namePrefix), SchemaObject.PROCEDURE);
        if (routineSchema != null) {
            read();
            HsqlArrayList list = new HsqlArrayList();
            readThis(Tokens.OPENBRACKET);
            if (token.tokenType == Tokens.CLOSEBRACKET) {
                read();
            } else {
                while (true) {
                    Expression e = XreadValueExpression();
                    list.add(e);
                    if (token.tokenType == Tokens.COMMA) {
                        read();
                    } else {
                        readThis(Tokens.CLOSEBRACKET);
                        break;
                    }
                }
            }
            Expression[] arguments = new Expression[list.size()];
            list.toArray(arguments);
            Routine routine = routineSchema.getSpecificRoutine(arguments.length);
            compileContext.addProcedureCall(routine);
            HsqlList unresolved = null;
            for (int i = 0; i < arguments.length; i++) {
                Expression e = arguments[i];
                if (e.isUnresolvedParam()) {
                    e.setAttributesAsColumn(routine.getParameter(i), routine.getParameter(i).isWriteable());
                } else {
                    int paramMode = routine.getParameter(i).getParameterMode();
                    unresolved = arguments[i].resolveColumnReferences(outerRanges, unresolved);
                    if (paramMode != SchemaObject.ParameterModes.PARAM_IN) {
                        if (e.getType() != OpTypes.VARIABLE) {
                            throw Error.error(ErrorCode.X_42603);
                        }
                    }
                }
            }
            ExpressionColumn.checkColumnsResolved(unresolved);
            for (int i = 0; i < arguments.length; i++) {
                arguments[i].resolveTypes(session, null);
                if (!routine.getParameter(i).getDataType().canBeAssignedFrom(arguments[i].getDataType())) {
                    throw Error.error(ErrorCode.X_42561);
                }
            }
            StatementDMQL cs = new StatementProcedure(session, routine, arguments, compileContext);
            return cs;
        }
    }
    if (isStrictlyProcedure) {
        throw Error.error(ErrorCode.X_42501, token.tokenString);
    }
    Expression expression = this.XreadValueExpression();
    HsqlList unresolved = expression.resolveColumnReferences(outerRanges, null);
    ExpressionColumn.checkColumnsResolved(unresolved);
    expression.resolveTypes(session, null);
    //        expression.paramMode = PARAM_OUT; 
    StatementDMQL cs = new StatementProcedure(session, expression, compileContext);
    return cs;
}
