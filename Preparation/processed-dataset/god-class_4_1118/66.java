void processAlterColumnSequenceOptions(ColumnSchema column) {
    OrderedIntHashSet set = new OrderedIntHashSet();
    NumberSequence sequence = column.getIdentitySequence().duplicate();
    while (true) {
        boolean end = false;
        switch(token.tokenType) {
            case Tokens.RESTART:
                {
                    if (!set.add(token.tokenType)) {
                        throw unexpectedToken();
                    }
                    read();
                    readThis(Tokens.WITH);
                    long value = readBigint();
                    sequence.setStartValue(value);
                    break;
                }
            case Tokens.SET:
                read();
                switch(token.tokenType) {
                    case Tokens.INCREMENT:
                        {
                            if (!set.add(token.tokenType)) {
                                throw unexpectedToken();
                            }
                            read();
                            readThis(Tokens.BY);
                            long value = readBigint();
                            sequence.setIncrement(value);
                            break;
                        }
                    case Tokens.NO:
                        read();
                        if (token.tokenType == Tokens.MAXVALUE) {
                            sequence.setDefaultMaxValue();
                        } else if (token.tokenType == Tokens.MINVALUE) {
                            sequence.setDefaultMinValue();
                        } else if (token.tokenType == Tokens.CYCLE) {
                            sequence.setCycle(false);
                        } else {
                            throw unexpectedToken();
                        }
                        if (!set.add(token.tokenType)) {
                            throw unexpectedToken();
                        }
                        read();
                        break;
                    case Tokens.MAXVALUE:
                        {
                            if (!set.add(token.tokenType)) {
                                throw unexpectedToken();
                            }
                            read();
                            long value = readBigint();
                            sequence.setMaxValueNoCheck(value);
                            break;
                        }
                    case Tokens.MINVALUE:
                        {
                            if (!set.add(token.tokenType)) {
                                throw unexpectedToken();
                            }
                            read();
                            long value = readBigint();
                            sequence.setMinValueNoCheck(value);
                            break;
                        }
                    case Tokens.CYCLE:
                        if (!set.add(token.tokenType)) {
                            throw unexpectedToken();
                        }
                        read();
                        sequence.setCycle(true);
                        break;
                    default:
                        throw super.unexpectedToken();
                }
                break;
            default:
                end = true;
                break;
        }
        if (end) {
            break;
        }
    }
    sequence.checkValues();
    column.getIdentitySequence().reset(sequence);
}
