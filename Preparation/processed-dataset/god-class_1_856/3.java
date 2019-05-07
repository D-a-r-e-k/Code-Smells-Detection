private void dumpConstantPool() throws IOException {
    ConstantPool pool = clazz.constantPool;
    ConstantPoolItem pi = null;
    int poolCount = pool.getConstantPoolCount();
    out.writeShort(poolCount);
    for (int i = 1; i < poolCount; i++) {
        pi = pool.getConstant(i);
        out.writeByte(pi.tag);
        switch(pi.tag) {
            case Constants.CONSTANT_Utf8:
                out.writeUTF(((Constant_Utf8) pi).bytes);
                break;
            case Constants.CONSTANT_Integer:
                out.writeInt(((Constant_Integer) pi).value);
                break;
            case Constants.CONSTANT_Float:
                out.writeFloat(((Constant_Float) pi).value);
                break;
            case Constants.CONSTANT_Long:
                out.writeLong(((Constant_Long) pi).value);
                i++;
                break;
            case Constants.CONSTANT_Double:
                out.writeDouble(((Constant_Double) pi).value);
                i++;
                break;
            case Constants.CONSTANT_Class:
                out.writeShort(((Constant_Class) pi).name_index);
                break;
            case Constants.CONSTANT_Fieldref:
                out.writeShort(((Constant_Fieldref) pi).class_index);
                out.writeShort(((Constant_Fieldref) pi).name_and_type_index);
                break;
            case Constants.CONSTANT_String:
                out.writeShort(((Constant_String) pi).string_index);
                break;
            case Constants.CONSTANT_Methodref:
                out.writeShort(((Constant_Methodref) pi).class_index);
                out.writeShort(((Constant_Methodref) pi).name_and_type_index);
                break;
            case Constants.CONSTANT_InterfaceMethodref:
                out.writeShort(((Constant_InterfaceMethodref) pi).class_index);
                out.writeShort(((Constant_InterfaceMethodref) pi).name_and_type_index);
                break;
            case Constants.CONSTANT_NameAndType:
                out.writeShort(((Constant_NameAndType) pi).name_index);
                out.writeShort(((Constant_NameAndType) pi).descriptor_index);
                break;
            default:
                // TODO: throws exceptoin  
                int x = 9 / 0;
        }
    }
}
