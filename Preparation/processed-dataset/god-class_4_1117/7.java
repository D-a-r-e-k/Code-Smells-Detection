public void resolveTypes(Session session, Expression parent) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            nodes[i].resolveTypes(session, this);
        }
    }
    switch(funcType) {
        case FUNC_EXTRACT:
        case FUNC_TRIM_CHAR:
        case FUNC_OVERLAY_CHAR:
            super.resolveTypes(session, parent);
            return;
        case FUNC_DATABASE:
            dataType = Type.SQL_VARCHAR_DEFAULT;
            return;
        case FUNC_ISAUTOCOMMIT:
        case FUNC_ISREADONLYSESSION:
        case FUNC_ISREADONLYDATABASE:
        case FUNC_ISREADONLYDATABASEFILES:
            dataType = Type.SQL_BOOLEAN;
            return;
        case FUNC_ISOLATION_LEVEL:
        case FUNC_SESSION_ISOLATION_LEVEL:
        case FUNC_DATABASE_ISOLATION_LEVEL:
        case FUNC_TRANSACTION_CONTROL:
        case FUNC_DATABASE_VERSION:
            dataType = Type.SQL_VARCHAR_DEFAULT;
            return;
        case FUNC_TIMEZONE:
        case FUNC_SESSION_TIMEZONE:
        case FUNC_DATABASE_TIMEZONE:
            dataType = Type.SQL_INTERVAL_HOUR_TO_MINUTE;
            return;
        case FUNC_IDENTITY:
            dataType = Type.SQL_BIGINT;
            return;
        case FUNC_DATEADD:
            {
                int part;
                if (!nodes[0].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                if ("yy".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_YEAR;
                } else if ("mm".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_MONTH;
                } else if ("dd".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_DAY;
                } else if ("hh".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_HOUR;
                } else if ("mi".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_MINUTE;
                } else if ("ss".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_SECOND;
                } else if ("ms".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_FRAC_SECOND;
                } else {
                    throw Error.error(ErrorCode.X_42561);
                }
                nodes[0].valueData = ValuePool.getInt(part);
                nodes[0].dataType = Type.SQL_INTEGER;
                funcType = FUNC_TIMESTAMPADD;
            }
        // fall through 
        case FUNC_TIMESTAMPADD:
            if (nodes[1].dataType == null) {
                nodes[1].dataType = Type.SQL_BIGINT;
            }
            if (nodes[2].dataType == null) {
                nodes[2].dataType = Type.SQL_TIMESTAMP;
            }
            if (!nodes[1].dataType.isIntegralType()) {
                throw Error.error(ErrorCode.X_42561);
            }
            if (nodes[2].dataType.typeCode != Types.SQL_DATE && nodes[2].dataType.typeCode != Types.SQL_TIMESTAMP && nodes[2].dataType.typeCode != Types.SQL_TIMESTAMP_WITH_TIME_ZONE) {
                throw Error.error(ErrorCode.X_42561);
            }
            dataType = nodes[2].dataType;
            return;
        case FUNC_DATEDIFF:
            {
                int part;
                if (!nodes[0].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42563);
                }
                if ("yy".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_YEAR;
                } else if ("mm".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_MONTH;
                } else if ("dd".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_DAY;
                } else if ("hh".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_HOUR;
                } else if ("mi".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_MINUTE;
                } else if ("ss".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_SECOND;
                } else if ("ms".equalsIgnoreCase((String) nodes[0].valueData)) {
                    part = Tokens.SQL_TSI_FRAC_SECOND;
                } else {
                    throw Error.error(ErrorCode.X_22511, (String) nodes[0].valueData);
                }
                nodes[0].valueData = ValuePool.getInt(part);
                nodes[0].dataType = Type.SQL_INTEGER;
                funcType = FUNC_TIMESTAMPDIFF;
            }
        // fall through 
        case FUNC_TIMESTAMPDIFF:
            {
                if (nodes[1].dataType == null) {
                    nodes[1].dataType = nodes[2].dataType;
                }
                if (nodes[2].dataType == null) {
                    nodes[2].dataType = nodes[1].dataType;
                }
                if (nodes[1].dataType == null) {
                    nodes[1].dataType = Type.SQL_TIMESTAMP;
                    nodes[2].dataType = Type.SQL_TIMESTAMP;
                }
                switch(nodes[1].dataType.typeCode) {
                    case Types.SQL_DATE:
                        if (nodes[2].dataType.typeCode != Types.SQL_DATE) {
                            throw Error.error(ErrorCode.X_42563);
                        }
                        switch(((Integer) nodes[0].valueData).intValue()) {
                            case Tokens.SQL_TSI_DAY:
                            case Tokens.SQL_TSI_WEEK:
                            case Tokens.SQL_TSI_MONTH:
                            case Tokens.SQL_TSI_QUARTER:
                            case Tokens.SQL_TSI_YEAR:
                                break;
                            default:
                                throw Error.error(ErrorCode.X_42563);
                        }
                        break;
                    case Types.SQL_TIMESTAMP:
                    case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                        if (nodes[2].dataType.typeCode != Types.SQL_TIMESTAMP && nodes[2].dataType.typeCode != Types.SQL_TIMESTAMP_WITH_TIME_ZONE) {
                            throw Error.error(ErrorCode.X_42563);
                        }
                        break;
                    default:
                        throw Error.error(ErrorCode.X_42563);
                }
                dataType = Type.SQL_BIGINT;
                return;
            }
        case FUNC_TRUNCATE:
            {
                if (nodes[0].dataType == null) {
                    throw Error.error(ErrorCode.X_42567);
                }
                if (nodes[1].dataType == null) {
                    nodes[1].dataType = Type.SQL_INTEGER;
                } else if (!nodes[1].dataType.isIntegralType()) {
                    throw Error.error(ErrorCode.X_42563);
                }
                if (!nodes[0].dataType.isNumberType()) {
                    throw Error.error(ErrorCode.X_42563);
                }
                dataType = nodes[0].dataType;
                return;
            }
        case FUNC_TO_CHAR:
            {
                if (nodes[0].dataType == null) {
                    throw Error.error(ErrorCode.X_42567);
                }
                if (nodes[1].dataType == null || !nodes[1].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42567);
                }
                if (!nodes[0].dataType.isExactNumberType() && !nodes[0].dataType.isDateTimeType()) {
                    throw Error.error(ErrorCode.X_42563);
                }
                // fixed maximum as format is a variable 
                dataType = CharacterType.getCharacterType(Types.SQL_VARCHAR, 40);
                if (nodes[1].opType == OpTypes.VALUE) {
                    nodes[1].setAsConstantValue(session);
                }
                return;
            }
        case FUNC_TIMESTAMP:
            {
                Type argType = nodes[0].dataType;
                if (nodes[1] == null) {
                    if (argType == null) {
                        argType = nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
                    }
                    if (argType.isCharacterType() || argType.typeCode == Types.SQL_TIMESTAMP || argType.typeCode == Types.SQL_TIMESTAMP_WITH_TIME_ZONE) {
                    } else {
                        throw Error.error(ErrorCode.X_42561);
                    }
                } else {
                    if (argType == null) {
                        if (nodes[1].dataType == null) {
                            argType = nodes[0].dataType = nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
                        } else {
                            if (nodes[1].dataType.isCharacterType()) {
                                argType = nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
                            } else {
                                argType = nodes[0].dataType = Type.SQL_DATE;
                            }
                        }
                    }
                    if (nodes[1].dataType == null) {
                        if (argType.isCharacterType()) {
                            nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
                        } else if (argType.typeCode == Types.SQL_DATE) {
                            nodes[1].dataType = Type.SQL_TIME;
                        }
                    }
                    if ((argType.typeCode == Types.SQL_DATE && nodes[1].dataType.typeCode == Types.SQL_TIME) || argType.isCharacterType() && nodes[1].dataType.isCharacterType()) {
                    } else {
                        throw Error.error(ErrorCode.X_42561);
                    }
                }
                dataType = Type.SQL_TIMESTAMP;
                return;
            }
        case FUNC_PI:
            dataType = Type.SQL_DOUBLE;
            break;
        case FUNC_RAND:
            {
                if (nodes[0] != null) {
                    if (nodes[0].dataType == null) {
                        nodes[0].dataType = Type.SQL_BIGINT;
                    } else if (!nodes[0].dataType.isExactNumberType()) {
                        throw Error.error(ErrorCode.X_42563);
                    }
                }
                dataType = Type.SQL_DOUBLE;
                break;
            }
        case FUNC_ROUND:
            if (nodes[1].dataType == null) {
                nodes[1].dataType = Type.SQL_INTEGER;
            }
            if (!nodes[1].dataType.isExactNumberType()) {
                throw Error.error(ErrorCode.X_42561);
            }
        // fall through 
        case FUNC_ACOS:
        case FUNC_ASIN:
        case FUNC_ATAN:
        case FUNC_COS:
        case FUNC_COT:
        case FUNC_DEGREES:
        case FUNC_SIN:
        case FUNC_TAN:
        case FUNC_LOG10:
        case FUNC_RADIANS:
        case FUNC_ROUNDMAGIC:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_DOUBLE;
                }
                if (!nodes[0].dataType.isNumberType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = Type.SQL_DOUBLE;
                break;
            }
        case FUNC_SIGN:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_DOUBLE;
                }
                if (!nodes[0].dataType.isNumberType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = Type.SQL_INTEGER;
                break;
            }
        case FUNC_ATAN2:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_DOUBLE;
                }
                if (nodes[1].dataType == null) {
                    nodes[1].dataType = Type.SQL_DOUBLE;
                }
                if (!nodes[0].dataType.isNumberType() || !nodes[1].dataType.isNumberType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = Type.SQL_DOUBLE;
                break;
            }
        case FUNC_SOUNDEX:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_VARCHAR;
                }
                if (!nodes[0].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = Type.getType(Types.SQL_VARCHAR, 0, 4, 0);
                break;
            }
        case FUNC_BITAND:
        case FUNC_BITOR:
        case FUNC_BITXOR:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = nodes[1].dataType;
                }
                if (nodes[1].dataType == null) {
                    nodes[1].dataType = nodes[0].dataType;
                }
                for (int i = 0; i < nodes.length; i++) {
                    if (nodes[i].dataType == null) {
                        nodes[i].dataType = Type.SQL_INTEGER;
                    }
                }
                dataType = nodes[0].dataType.getAggregateType(nodes[1].dataType);
                switch(dataType.typeCode) {
                    case Types.SQL_BIGINT:
                    case Types.SQL_INTEGER:
                    case Types.SQL_SMALLINT:
                    case Types.TINYINT:
                        break;
                    case Types.SQL_BIT:
                    case Types.SQL_BIT_VARYING:
                        break;
                    default:
                        throw Error.error(ErrorCode.X_42561);
                }
                break;
            }
        case FUNC_ASCII:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_VARCHAR;
                }
                if (!nodes[0].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = Type.SQL_INTEGER;
                break;
            }
        case FUNC_CHAR:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_INTEGER;
                }
                if (!nodes[0].dataType.isExactNumberType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = Type.getType(Types.SQL_VARCHAR, 0, 1, 0);
                break;
            }
        case FUNC_DIFFERENCE:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_VARCHAR;
                }
                if (nodes[1].dataType == null) {
                    nodes[1].dataType = Type.SQL_VARCHAR;
                }
                dataType = Type.SQL_INTEGER;
                break;
            }
        case FUNC_HEXTORAW:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_VARCHAR;
                }
                if (!nodes[0].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = nodes[0].dataType.precision == 0 ? Type.SQL_VARBINARY_DEFAULT : Type.getType(Types.SQL_VARBINARY, 0, nodes[0].dataType.precision / 2, 0);
                break;
            }
        case FUNC_RAWTOHEX:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_VARBINARY;
                }
                if (!nodes[0].dataType.isBinaryType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = nodes[0].dataType.precision == 0 ? Type.SQL_VARCHAR_DEFAULT : Type.getType(Types.SQL_VARCHAR, 0, nodes[0].dataType.precision * 2, 0);
                break;
            }
        case FUNC_REPEAT:
            {
                if (nodes[0].dataType == null) {
                    nodes[0].dataType = Type.SQL_VARCHAR;
                }
                boolean isChar = nodes[0].dataType.isCharacterType();
                if (!isChar && !nodes[0].dataType.isBinaryType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                if (!nodes[1].dataType.isExactNumberType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                dataType = isChar ? (Type) Type.SQL_VARCHAR_DEFAULT : (Type) Type.SQL_VARBINARY_DEFAULT;
                break;
            }
        case FUNC_REPLACE:
            {
                for (int i = 0; i < nodes.length; i++) {
                    if (nodes[i].dataType == null) {
                        nodes[i].dataType = Type.SQL_VARCHAR;
                    } else if (!nodes[i].dataType.isCharacterType()) {
                        throw Error.error(ErrorCode.X_42561);
                    }
                }
                dataType = Type.SQL_VARCHAR_DEFAULT;
                break;
            }
        case FUNC_LEFT:
        case FUNC_RIGHT:
            if (nodes[0].dataType == null) {
                nodes[0].dataType = Type.SQL_VARCHAR;
            }
            if (!nodes[0].dataType.isCharacterType()) {
                throw Error.error(ErrorCode.X_42561);
            }
            if (nodes[1].dataType == null) {
                nodes[1].dataType = Type.SQL_INTEGER;
            }
            if (!nodes[1].dataType.isExactNumberType()) {
                throw Error.error(ErrorCode.X_42561);
            }
            dataType = nodes[0].dataType.precision == 0 ? Type.SQL_VARCHAR_DEFAULT : Type.getType(Types.SQL_VARCHAR, 0, nodes[0].dataType.precision, 0);
            break;
        case FUNC_SPACE:
            if (nodes[0].dataType == null) {
                nodes[0].dataType = Type.SQL_INTEGER;
            }
            if (!nodes[0].dataType.isIntegralType()) {
                throw Error.error(ErrorCode.X_42561);
            }
            dataType = Type.SQL_VARCHAR_DEFAULT;
            break;
        case FUNC_REVERSE:
            if (nodes[0].dataType == null) {
                nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
            }
            dataType = nodes[0].dataType;
            if (!dataType.isCharacterType() || dataType.isLobType()) {
                throw Error.error(ErrorCode.X_42561);
            }
            break;
        case FUNC_REGEXP_MATCHES:
            if (nodes[0].dataType == null) {
                nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
            }
            if (nodes[1].dataType == null) {
                nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
            }
            if (!nodes[0].dataType.isCharacterType() || !nodes[1].dataType.isCharacterType() || nodes[1].dataType.isLobType()) {
                throw Error.error(ErrorCode.X_42561);
            }
            dataType = Type.SQL_BOOLEAN;
            break;
        case FUNC_CRYPT_KEY:
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].dataType == null) {
                    nodes[i].dataType = Type.SQL_VARCHAR;
                } else if (!nodes[i].dataType.isCharacterType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
            }
            dataType = Type.SQL_VARCHAR_DEFAULT;
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "FunctionCustom");
    }
}
