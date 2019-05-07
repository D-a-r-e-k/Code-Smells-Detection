void readExpression(HsqlArrayList exprList, short[] parseList, int start, int count, boolean isOption) {
    for (int i = start; i < start + count; i++) {
        int exprType = parseList[i];
        switch(exprType) {
            case Tokens.QUESTION:
                {
                    Expression e = null;
                    e = XreadAllTypesCommonValueExpression(false);
                    exprList.add(e);
                    continue;
                }
            case Tokens.X_POS_INTEGER:
                {
                    Expression e = null;
                    Integer value = readIntegerObject();
                    if (value.intValue() < 0) {
                        throw Error.error(ErrorCode.X_42592);
                    }
                    e = new ExpressionValue(value, Type.SQL_INTEGER);
                    exprList.add(e);
                    continue;
                }
            case Tokens.X_OPTION:
                {
                    i++;
                    int expressionCount = exprList.size();
                    int position = getPosition();
                    int elementCount = parseList[i++];
                    int initialExprIndex = exprList.size();
                    try {
                        readExpression(exprList, parseList, i, elementCount, true);
                    } catch (HsqlException ex) {
                        ex.setLevel(compileContext.subqueryDepth);
                        if (lastError == null || lastError.getLevel() < ex.getLevel()) {
                            lastError = ex;
                        }
                        rewind(position);
                        exprList.setSize(expressionCount);
                        for (int j = i; j < i + elementCount; j++) {
                            if (parseList[j] == Tokens.QUESTION || parseList[j] == Tokens.X_KEYSET || parseList[j] == Tokens.X_POS_INTEGER) {
                                exprList.add(null);
                            }
                        }
                        i += elementCount - 1;
                        continue;
                    }
                    if (initialExprIndex == exprList.size()) {
                        exprList.add(null);
                    }
                    i += elementCount - 1;
                    continue;
                }
            case Tokens.X_REPEAT:
                {
                    i++;
                    int elementCount = parseList[i++];
                    int parseIndex = i;
                    while (true) {
                        int initialExprIndex = exprList.size();
                        readExpression(exprList, parseList, parseIndex, elementCount, true);
                        if (exprList.size() == initialExprIndex) {
                            break;
                        }
                    }
                    i += elementCount - 1;
                    continue;
                }
            case Tokens.X_KEYSET:
                {
                    int elementCount = parseList[++i];
                    Expression e = null;
                    if (ArrayUtil.find(parseList, token.tokenType, i + 1, elementCount) == -1) {
                        if (!isOption) {
                            throw unexpectedToken();
                        }
                    } else {
                        e = new ExpressionValue(ValuePool.getInt(token.tokenType), Type.SQL_INTEGER);
                        read();
                    }
                    exprList.add(e);
                    i += elementCount;
                    continue;
                }
            case Tokens.OPENBRACKET:
            case Tokens.CLOSEBRACKET:
            case Tokens.COMMA:
            default:
                if (token.tokenType != exprType) {
                    throw unexpectedToken();
                }
                read();
                continue;
        }
    }
}
