/**
	 * 
	 * @param label
	 * @param map
	 * @param countingInstructionLength if false, will return the starting offset of this insctruction.
	 *  else will return the end offset of this instruction 
	 * @return
	 */
private int getOffset(String label, Hashtable map, boolean countingInstructionLength) throws GrammerException {
    Attribute_Code.Opcode op = (Attribute_Code.Opcode) map.get(label);
    if (op == null) {
        return -1;
    }
    if (countingInstructionLength == false) {
        return op.offset;
    } else {
        return op.offset + Constants.NO_OF_OPERANDS[op.opcode & 0xFF] + 1;
    }
}
