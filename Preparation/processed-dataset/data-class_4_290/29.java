private void readSequenceOptions(NumberSequence sequence, boolean withType, boolean isAlter) {
    OrderedIntHashSet set = new OrderedIntHashSet();
    while (true) {
        boolean end = false;
        if (set.contains(token.tokenType)) {
            throw unexpectedToken();
        }
        switch(token.tokenType) {
            case Tokens.AS:
                {
                    if (withType) {
                        read();
                        Type type = readTypeDefinition(true);
                        sequence.setDefaults(sequence.getName(), type);
                        break;
                    }
                    throw unexpectedToken();
                }
            case Tokens.START:
                {
                    set.add(token.tokenType);
                    read();
                    readThis(Tokens.WITH);
                    long value = readBigint();
                    sequence.setStartValueNoCheck(value);
                    break;
                }
            case Tokens.RESTART:
                {
                    if (!isAlter) {
                        end = true;
                        break;
                    }
                    set.add(token.tokenType);
                    read();
                    if (readIfThis(Tokens.WITH)) {
                        long value = readBigint();
                        sequence.setCurrentValueNoCheck(value);
                    } else {
                        sequence.setStartValueDefault();
                    }
                    break;
                }
            case Tokens.INCREMENT:
                {
                    set.add(token.tokenType);
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
                set.add(token.tokenType);
                read();
                break;
            case Tokens.MAXVALUE:
                {
                    set.add(token.tokenType);
                    read();
                    long value = readBigint();
                    sequence.setMaxValueNoCheck(value);
                    break;
                }
            case Tokens.MINVALUE:
                {
                    set.add(token.tokenType);
                    read();
                    long value = readBigint();
                    sequence.setMinValueNoCheck(value);
                    break;
                }
            case Tokens.CYCLE:
                set.add(token.tokenType);
                read();
                sequence.setCycle(true);
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
}
