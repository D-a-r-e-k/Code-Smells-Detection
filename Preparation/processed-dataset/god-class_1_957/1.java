/**
     * Returns the name for this attribute within the constant pool context of 'cls'
     * class definition.
     * 
     * @param cls class that contains this attribute
     * @return attribute name
     */
public String getName(final ClassDef cls) {
    return ((CONSTANT_Utf8_info) cls.getConstants().get(m_name_index)).m_value;
}
