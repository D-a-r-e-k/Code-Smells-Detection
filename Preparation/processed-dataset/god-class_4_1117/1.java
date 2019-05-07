public static FunctionSQL newCustomFunction(String token, int tokenType) {
    int id = customRegularFuncMap.get(tokenType, -1);
    if (id == -1) {
        id = customValueFuncMap.get(tokenType, -1);
    }
    if (id == -1) {
        return null;
    }
    switch(tokenType) {
        case Tokens.BITLENGTH:
        case Tokens.LCASE:
        case Tokens.LENGTH:
        case Tokens.LOG:
        case Tokens.OCTETLENGTH:
        case Tokens.TODAY:
        case Tokens.SYSDATE:
        case Tokens.UCASE:
            return new FunctionSQL(id);
        case Tokens.NOW:
            {
                FunctionSQL function = new FunctionSQL(id);
                function.parseList = optionalNoParamList;
                return function;
            }
        case Tokens.CURDATE:
        case Tokens.CURTIME:
            {
                FunctionSQL function = new FunctionSQL(id);
                function.parseList = emptyParamList;
                return function;
            }
        case Tokens.SUBSTR:
            {
                FunctionSQL function = new FunctionSQL(id);
                function.parseList = tripleParamList;
                return function;
            }
        case Tokens.LOCATE:
            FunctionSQL function = new FunctionSQL(id);
            function.parseList = new short[] { Tokens.OPENBRACKET, Tokens.QUESTION, Tokens.COMMA, Tokens.QUESTION, Tokens.X_OPTION, 2, Tokens.COMMA, Tokens.QUESTION, Tokens.CLOSEBRACKET };
            return function;
    }
    FunctionCustom function = new FunctionCustom(id);
    if (id == FUNC_TRIM_CHAR) {
        switch(tokenType) {
            case Tokens.LTRIM:
                function.extractSpec = Tokens.LEADING;
                break;
            case Tokens.RTRIM:
                function.extractSpec = Tokens.TRAILING;
                break;
        }
    }
    if (id == FUNC_EXTRACT) {
        switch(tokenType) {
            case Tokens.DAYNAME:
                function.extractSpec = Tokens.DAY_NAME;
                break;
            case Tokens.MONTHNAME:
                function.extractSpec = Tokens.MONTH_NAME;
                break;
            case Tokens.DAYOFMONTH:
                function.extractSpec = Tokens.DAY_OF_MONTH;
                break;
            case Tokens.DAYOFWEEK:
                function.extractSpec = Tokens.DAY_OF_WEEK;
                break;
            case Tokens.DAYOFYEAR:
                function.extractSpec = Tokens.DAY_OF_YEAR;
                break;
            default:
                function.extractSpec = tokenType;
        }
    }
    if (function.name == null) {
        function.name = token;
    }
    return function;
}
