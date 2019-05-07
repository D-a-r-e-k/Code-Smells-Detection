Object getValue(Session session, Object[] data) {
    switch(funcType) {
        case FUNC_EXTRACT:
        case FUNC_TRIM_CHAR:
        case FUNC_OVERLAY_CHAR:
            return super.getValue(session, data);
        case FUNC_DATABASE:
            return session.getDatabase().getPath();
        case FUNC_ISAUTOCOMMIT:
            return session.isAutoCommit() ? Boolean.TRUE : Boolean.FALSE;
        case FUNC_ISREADONLYSESSION:
            return session.isReadOnlyDefault() ? Boolean.TRUE : Boolean.FALSE;
        case FUNC_ISREADONLYDATABASE:
            return session.getDatabase().databaseReadOnly ? Boolean.TRUE : Boolean.FALSE;
        case FUNC_ISREADONLYDATABASEFILES:
            return session.getDatabase().isFilesReadOnly() ? Boolean.TRUE : Boolean.FALSE;
        case FUNC_ISOLATION_LEVEL:
            {
                return Session.getIsolationString(session.isolationLevel);
            }
        case FUNC_SESSION_ISOLATION_LEVEL:
            return Session.getIsolationString(session.isolationLevelDefault);
        case FUNC_DATABASE_ISOLATION_LEVEL:
            return Session.getIsolationString(session.database.getDefaultIsolationLevel());
        case FUNC_TRANSACTION_CONTROL:
            switch(session.database.txManager.getTransactionControl()) {
                case TransactionManager.MVCC:
                    return Tokens.T_MVCC;
                case TransactionManager.MVLOCKS:
                    return Tokens.T_MVLOCKS;
                case TransactionManager.LOCKS:
                default:
                    return Tokens.T_LOCKS;
            }
        case FUNC_TIMEZONE:
            return new IntervalSecondData(session.getZoneSeconds(), 0);
        case FUNC_SESSION_TIMEZONE:
            return new IntervalSecondData(session.sessionTimeZoneSeconds, 0);
        case FUNC_DATABASE_TIMEZONE:
            int sec = HsqlDateTime.getZoneSeconds(HsqlDateTime.tempCalDefault);
            return new IntervalSecondData(sec, 0);
        case FUNC_DATABASE_VERSION:
            return HsqlDatabaseProperties.THIS_FULL_VERSION;
        case FUNC_IDENTITY:
            {
                Number id = session.getLastIdentity();
                if (id instanceof Long) {
                    return id;
                } else {
                    return ValuePool.getLong(id.longValue());
                }
            }
        case FUNC_TIMESTAMPADD:
            {
                if (data[1] == null || data[2] == null) {
                    return null;
                }
                data[1] = Type.SQL_BIGINT.convertToType(session, data[1], nodes[1].getDataType());
                int part = ((Number) nodes[0].valueData).intValue();
                long units = ((Number) data[1]).longValue();
                TimestampData source = (TimestampData) data[2];
                IntervalType t;
                Object o;
                switch(part) {
                    case Tokens.SQL_TSI_FRAC_SECOND:
                        {
                            long seconds = units / DTIType.limitNanoseconds;
                            int nanos = (int) (units % DTIType.limitNanoseconds);
                            t = Type.SQL_INTERVAL_SECOND_MAX_FRACTION;
                            o = new IntervalSecondData(seconds, nanos, t);
                            return dataType.add(source, o, t);
                        }
                    case Tokens.SQL_TSI_SECOND:
                        t = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
                        o = IntervalSecondData.newIntervalSeconds(units, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_MINUTE:
                        t = Type.SQL_INTERVAL_MINUTE_MAX_PRECISION;
                        o = IntervalSecondData.newIntervalMinute(units, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_HOUR:
                        t = Type.SQL_INTERVAL_HOUR_MAX_PRECISION;
                        o = IntervalSecondData.newIntervalHour(units, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_DAY:
                        t = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
                        o = IntervalSecondData.newIntervalDay(units, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_WEEK:
                        t = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
                        o = IntervalSecondData.newIntervalDay(units * 7, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_MONTH:
                        t = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
                        o = IntervalMonthData.newIntervalMonth(units, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_QUARTER:
                        t = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
                        o = IntervalMonthData.newIntervalMonth(units * 3, t);
                        return dataType.add(source, o, t);
                    case Tokens.SQL_TSI_YEAR:
                        t = Type.SQL_INTERVAL_YEAR_MAX_PRECISION;
                        o = IntervalMonthData.newIntervalMonth(units * 12, t);
                        return dataType.add(source, o, t);
                    default:
                        throw Error.runtimeError(ErrorCode.U_S0500, "FunctionCustom");
                }
            }
        case FUNC_TIMESTAMPDIFF:
            {
                if (data[1] == null || data[2] == null) {
                    return null;
                }
                int part = ((Number) nodes[0].valueData).intValue();
                TimestampData a = (TimestampData) data[2];
                TimestampData b = (TimestampData) data[1];
                if (nodes[2].dataType.isDateTimeTypeWithZone()) {
                    a = (TimestampData) Type.SQL_TIMESTAMP.convertToType(session, a, Type.SQL_TIMESTAMP_WITH_TIME_ZONE);
                }
                if (nodes[1].dataType.isDateTimeTypeWithZone()) {
                    b = (TimestampData) Type.SQL_TIMESTAMP.convertToType(session, b, Type.SQL_TIMESTAMP_WITH_TIME_ZONE);
                }
                IntervalType t;
                switch(part) {
                    case Tokens.SQL_TSI_FRAC_SECOND:
                        t = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
                        IntervalSecondData interval = (IntervalSecondData) t.subtract(a, b, null);
                        return new Long(DTIType.limitNanoseconds * interval.getSeconds() + interval.getNanos());
                    case Tokens.SQL_TSI_SECOND:
                        t = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)));
                    case Tokens.SQL_TSI_MINUTE:
                        t = Type.SQL_INTERVAL_MINUTE_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)));
                    case Tokens.SQL_TSI_HOUR:
                        t = Type.SQL_INTERVAL_HOUR_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)));
                    case Tokens.SQL_TSI_DAY:
                        t = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)));
                    case Tokens.SQL_TSI_WEEK:
                        t = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)) / 7);
                    case Tokens.SQL_TSI_MONTH:
                        t = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)));
                    case Tokens.SQL_TSI_QUARTER:
                        t = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)) / 3);
                    case Tokens.SQL_TSI_YEAR:
                        t = Type.SQL_INTERVAL_YEAR_MAX_PRECISION;
                        return new Long(t.convertToLong(t.subtract(a, b, null)));
                    default:
                        throw Error.runtimeError(ErrorCode.U_S0500, "FunctionCustom");
                }
            }
        case FUNC_SECONDS_MIDNIGHT:
            {
                if (data[0] == null) {
                    return null;
                }
            }
        // fall through 
        case FUNC_TRUNCATE:
            {
                if (data[0] == null || data[1] == null) {
                    return null;
                }
                data[1] = Type.SQL_INTEGER.convertToType(session, data[1], nodes[1].getDataType());
                return ((NumberType) dataType).truncate(data[0], ((Number) data[1]).intValue());
            }
        case FUNC_TO_CHAR:
            {
                if (data[0] == null || data[1] == null) {
                    return null;
                }
                SimpleDateFormat format = session.getSimpleDateFormatGMT();
                String javaPattern = HsqlDateTime.toJavaDatePattern((String) data[1]);
                try {
                    format.applyPattern(javaPattern);
                } catch (Exception e) {
                    throw Error.error(ErrorCode.X_22511);
                }
                Date date = (Date) ((DateTimeType) nodes[0].dataType).convertSQLToJavaGMT(session, data[0]);
                return format.format(date);
            }
        case FUNC_TIMESTAMP:
            {
                boolean unary = nodes[1] == null;
                if (data[0] == null) {
                    return null;
                }
                if (unary) {
                    return Type.SQL_TIMESTAMP.convertToType(session, data[0], nodes[0].dataType);
                }
                if (data[1] == null) {
                    return null;
                }
                TimestampData date = (TimestampData) Type.SQL_DATE.convertToType(session, data[0], nodes[0].dataType);
                TimeData time = (TimeData) Type.SQL_TIME.convertToType(session, data[1], nodes[1].dataType);
                return new TimestampData(date.getSeconds() + time.getSeconds(), time.getNanos());
            }
        case FUNC_PI:
            return new Double(Math.PI);
        case FUNC_RAND:
            {
                if (nodes[0] == null) {
                    return new Double(session.random());
                } else {
                    data[0] = Type.SQL_BIGINT.convertToType(session, data[0], nodes[0].getDataType());
                    long seed = ((Number) data[0]).longValue();
                    return new Double(session.random(seed));
                }
            }
        case FUNC_ACOS:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.acos(d));
            }
        case FUNC_ASIN:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.asin(d));
            }
        case FUNC_ATAN:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.atan(d));
            }
        case FUNC_COS:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.cos(d));
            }
        case FUNC_COT:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                double c = 1.0 / java.lang.Math.tan(d);
                return new Double(c);
            }
        case FUNC_DEGREES:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.toDegrees(d));
            }
        case FUNC_SIN:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.sin(d));
            }
        case FUNC_TAN:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.tan(d));
            }
        case FUNC_LOG10:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.log10(d));
            }
        case FUNC_RADIANS:
            {
                if (data[0] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                return new Double(java.lang.Math.toRadians(d));
            }
        // 
        case FUNC_SIGN:
            {
                if (data[0] == null) {
                    return null;
                }
                int val = ((NumberType) nodes[0].dataType).compareToZero(data[0]);
                return ValuePool.getInt(val);
            }
        case FUNC_ATAN2:
            {
                if (data[0] == null) {
                    return null;
                }
                double a = NumberType.toDouble(data[0]);
                double b = NumberType.toDouble(data[1]);
                return new Double(java.lang.Math.atan2(a, b));
            }
        case FUNC_ASCII:
            {
                String arg;
                if (data[0] == null) {
                    return null;
                }
                if (nodes[0].dataType.isLobType()) {
                    arg = ((ClobData) data[0]).getSubString(session, 0, 1);
                } else {
                    arg = (String) data[0];
                }
                if (arg.length() == 0) {
                    return null;
                }
                return ValuePool.getInt(arg.charAt(0));
            }
        case FUNC_CHAR:
            if (data[0] == null) {
                return null;
            }
            data[0] = Type.SQL_INTEGER.convertToType(session, data[0], nodes[0].getDataType());
            int arg = ((Number) data[0]).intValue();
            if (Character.isValidCodePoint(arg) && Character.isValidCodePoint((char) arg)) {
                return String.valueOf((char) arg);
            }
            throw Error.error(ErrorCode.X_22511);
        case FUNC_ROUNDMAGIC:
        case FUNC_ROUND:
            {
                if (data[0] == null || data[1] == null) {
                    return null;
                }
                double d = NumberType.toDouble(data[0]);
                int e = ((Number) data[1]).intValue();
                double f = Math.pow(10., e);
                return new Double(Math.round(d * f) / f);
            }
        case FUNC_SOUNDEX:
            {
                if (data[0] == null) {
                    return null;
                }
                String s = (String) data[0];
                return new String(soundex(s), 0, 4);
            }
        case FUNC_BITAND:
        case FUNC_BITOR:
        case FUNC_BITXOR:
            {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == null) {
                        return null;
                    }
                }
                if (nodes[0].dataType.isIntegralType()) {
                    data[0] = Type.SQL_BIGINT.convertToType(session, data[0], nodes[0].getDataType());
                    data[1] = Type.SQL_BIGINT.convertToType(session, data[1], nodes[1].getDataType());
                    long v = 0;
                    long a = ((Number) data[0]).longValue();
                    long b = ((Number) data[1]).longValue();
                    switch(funcType) {
                        case FUNC_BITAND:
                            v = a & b;
                            break;
                        case FUNC_BITOR:
                            v = a | b;
                            break;
                        case FUNC_BITXOR:
                            v = a ^ b;
                            break;
                    }
                    switch(dataType.typeCode) {
                        case Types.SQL_NUMERIC:
                        case Types.SQL_DECIMAL:
                            return BigDecimal.valueOf(v);
                        case Types.SQL_BIGINT:
                            return ValuePool.getLong(v);
                        case Types.SQL_INTEGER:
                            return ValuePool.getInt((int) v);
                        case Types.SQL_SMALLINT:
                            return ValuePool.getInt((int) v & 0xffff);
                        case Types.TINYINT:
                            return ValuePool.getInt((int) v & 0xff);
                        default:
                            throw Error.error(ErrorCode.X_42561);
                    }
                } else {
                    byte[] a = ((BinaryData) data[0]).getBytes();
                    byte[] b = ((BinaryData) data[1]).getBytes();
                    byte[] v;
                    switch(funcType) {
                        case FUNC_BITAND:
                            v = BitMap.and(a, b);
                            break;
                        case FUNC_BITOR:
                            v = BitMap.or(a, b);
                            break;
                        case FUNC_BITXOR:
                            v = BitMap.xor(a, b);
                            break;
                        default:
                            throw Error.error(ErrorCode.X_42561);
                    }
                    return new BinaryData(v, dataType.precision);
                }
            }
        case FUNC_DIFFERENCE:
            {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == null) {
                        return null;
                    }
                }
                char[] s1 = soundex((String) data[0]);
                char[] s2 = soundex((String) data[1]);
                int e = 0;
                if (s1[0] == s2[0]) {
                    e++;
                }
                if (e == 4) {
                    return ValuePool.getInt(e);
                }
                int js = 1;
                for (int i = 1; i < 4; i++) {
                    for (int j = js; j < 4; j++) {
                        if (s1[j] == s2[i]) {
                            e++;
                            i++;
                            js++;
                        }
                    }
                }
                e = 0;
                return ValuePool.getInt(e);
            }
        case FUNC_HEXTORAW:
            {
                if (data[0] == null) {
                    return null;
                }
                return dataType.convertToType(session, data[0], nodes[0].dataType);
            }
        case FUNC_RAWTOHEX:
            {
                if (data[0] == null) {
                    return null;
                }
                return nodes[0].dataType.convertToString(data[0]);
            }
        case FUNC_REPEAT:
            {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == null) {
                        return null;
                    }
                }
                data[1] = Type.SQL_INTEGER.convertToType(session, data[1], nodes[1].getDataType());
                String string = (String) data[0];
                int i = ((Number) data[1]).intValue();
                StringBuffer sb = new StringBuffer(string.length() * i);
                while (i-- > 0) {
                    sb.append(string);
                }
                return sb.toString();
            }
        case FUNC_REPLACE:
            {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == null) {
                        return null;
                    }
                }
                String string = (String) data[0];
                String find = (String) data[1];
                String replace = (String) data[2];
                StringBuffer sb = new StringBuffer();
                int start = 0;
                while (true) {
                    int i = string.indexOf(find, start);
                    if (i == -1) {
                        sb.append(string.substring(start));
                        break;
                    }
                    sb.append(string.substring(start, i));
                    sb.append(replace);
                    start = i + find.length();
                }
                return sb.toString();
            }
        case FUNC_LEFT:
        case FUNC_RIGHT:
            {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == null) {
                        return null;
                    }
                }
                int count = ((Number) data[1]).intValue();
                return ((CharacterType) dataType).substring(session, data[0], 0, count, true, funcType == FUNC_RIGHT);
            }
        case FUNC_SPACE:
            {
                if (data[0] == null) {
                    return null;
                }
                data[0] = Type.SQL_INTEGER.convertToType(session, data[0], nodes[0].getDataType());
                int count = ((Number) data[0]).intValue();
                char[] array = new char[count];
                ArrayUtil.fillArray(array, 0, ' ');
                return String.valueOf(array);
            }
        case FUNC_REVERSE:
            {
                if (data[0] == null) {
                    return null;
                }
                StringBuffer sb = new StringBuffer((String) data[0]);
                sb = sb.reverse();
                return sb.toString();
            }
        case FUNC_REGEXP_MATCHES:
            {
                for (int i = 0; i < data.length; i++) {
                    if (data[i] == null) {
                        return null;
                    }
                }
                if (!data[1].equals(matchPattern)) {
                    matchPattern = (String) data[1];
                    pattern = Pattern.compile(matchPattern);
                }
                Matcher matcher = pattern.matcher((String) data[0]);
                return matcher.matches() ? Boolean.TRUE : Boolean.FALSE;
            }
        case FUNC_CRYPT_KEY:
            {
                byte[] bytes = Crypto.getNewKey((String) data[0], (String) data[1]);
                return StringConverter.byteArrayToHexString(bytes);
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "FunctionCustom");
    }
}
