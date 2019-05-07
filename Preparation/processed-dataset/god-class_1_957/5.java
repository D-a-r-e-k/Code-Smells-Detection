// TODO: use a hashmap lookup in this method + control which set of attrs get mapped to generic  
/**
     * Parses out a single Attribute_info element out of .class data in
     * 'bytes'.
     * 
     * @param constants constant pool for the parent class [may not be null; not validated]
     * @param bytes input .class data stream [may not be null; not validated]
     *  
     * @return a single parsed attribute
     * 
     * @throws IOException on input errors
     */
public static Attribute_info new_Attribute_info(final IConstantCollection constants, final UDataInputStream bytes) throws IOException {
    final int attribute_name_index = bytes.readU2();
    final long attribute_length = bytes.readU4();
    final CONSTANT_Utf8_info attribute_name = (CONSTANT_Utf8_info) constants.get(attribute_name_index);
    final String name = attribute_name.m_value;
    if (ATTRIBUTE_CODE.equals(name)) {
        return new CodeAttribute_info(constants, attribute_name_index, attribute_length, bytes);
    } else if (ATTRIBUTE_CONSTANT_VALUE.equals(name)) {
        return new ConstantValueAttribute_info(attribute_name_index, attribute_length, bytes);
    } else if (ATTRIBUTE_EXCEPTIONS.equals(name)) {
        return new ExceptionsAttribute_info(attribute_name_index, attribute_length, bytes);
    } else if (ATTRIBUTE_INNERCLASSES.equals(name)) {
        return new InnerClassesAttribute_info(attribute_name_index, attribute_length, bytes);
    } else if (ATTRIBUTE_SYNTHETIC.equals(name)) {
        return new SyntheticAttribute_info(attribute_name_index, attribute_length);
    } else if (ATTRIBUTE_BRIDGE.equals(name)) {
        return new BridgeAttribute_info(attribute_name_index, attribute_length);
    } else if (ATTRIBUTE_LINE_NUMBER_TABLE.equals(name)) {
        return new LineNumberTableAttribute_info(attribute_name_index, attribute_length, bytes);
    } else if (ATTRIBUTE_SOURCEFILE.equals(name)) {
        return new SourceFileAttribute_info(attribute_name_index, attribute_length, bytes);
    } else {
        // default:  
        return new GenericAttribute_info(attribute_name_index, attribute_length, bytes);
    }
}
