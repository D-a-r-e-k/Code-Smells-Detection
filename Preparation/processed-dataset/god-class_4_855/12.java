private void updateLabelLinks(Hashtable labels, ArrayList toUpdate) throws GrammerException {
    OpcodeWrapper op;
    ArrayList list;
    String label;
    int counter;
    byte[][] operands;
    for (int i = 0; i < toUpdate.size(); i++) {
        op = (OpcodeWrapper) toUpdate.get(i);
        operands = op.operands;
        switch(op.opcode) {
            case Constants.TABLESWITCH:
                list = (ArrayList) op.info;
                counter = operands.length;
                operands[1] = Util.getBytes(getOffset((String) list.get(0), labels, false) - op.offset, 4);
                // default value  
                counter = 1;
                for (int j = 4; j < operands.length; j++) {
                    operands[j] = Util.getBytes(getOffset((String) list.get(counter++), labels, false) - op.offset, 4);
                }
                break;
            case Constants.LOOKUPSWITCH:
                list = (ArrayList) op.info;
                counter = operands.length;
                operands[1] = Util.getBytes(getOffset((String) list.get(0), labels, false) - op.offset, 4);
                // default value  
                counter = 1;
                for (int j = 4; j < operands.length; j++) {
                    operands[j] = Util.getBytes(getOffset((String) list.get(counter++), labels, false) - op.offset, 4);
                    j++;
                }
                break;
            case Constants.GOTO:
            case Constants.IFEQ:
            case Constants.IFGE:
            case Constants.IFGT:
            case Constants.IFLE:
            case Constants.IFLT:
            case Constants.JSR:
            case Constants.IFNE:
            case Constants.IFNONNULL:
            case Constants.IFNULL:
            case Constants.IF_ACMPEQ:
            case Constants.IF_ACMPNE:
            case Constants.IF_ICMPEQ:
            case Constants.IF_ICMPGE:
            case Constants.IF_ICMPGT:
            case Constants.IF_ICMPLE:
            case Constants.IF_ICMPLT:
            case Constants.IF_ICMPNE:
                label = (String) op.info;
                operands[0] = Util.getBytes(getOffset(label, labels, false) - op.offset, 2);
                break;
            case Constants.GOTO_W:
            case Constants.JSR_W:
                label = (String) op.info;
                operands[0] = Util.getBytes(getOffset(label, labels, false) - op.offset, 4);
                break;
        }
    }
}
