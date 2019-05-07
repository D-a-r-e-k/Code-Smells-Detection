private void dumpAttribute(Attribute attribute) throws IOException {
    out.writeShort(attribute.attribute_name_index);
    out.writeInt(attribute.attribute_length);
    switch(attribute.attribute_tag) {
        case Constants.ATTRIBUTE_SourceFile:
            out.writeShort(((Attribute_SourceFile) attribute).sourcefile_index);
            break;
        case Constants.ATTRIBUTE_ConstantValue:
            out.writeShort(((Attribute_ConstantValue) attribute).constant_value_index);
            break;
        case Constants.ATTRIBUTE_Code:
            Attribute_Code code = (Attribute_Code) attribute;
            byte[][] operands;
            out.writeShort(code.max_stack);
            out.writeShort(code.max_locals);
            out.writeInt(code.code_length);
            // codes  
            Attribute_Code.Opcode op;
            for (int i = 0; i < code.codes.length; i++) {
                op = code.codes[i];
                out.writeByte(op.opcode);
                operands = op.operands;
                if (operands != null && operands.length != 0) {
                    for (int j = 0; j < operands.length; j++) {
                        if (operands[j] != null) {
                            out.write(operands[j]);
                        }
                    }
                }
            }
            out.writeShort(code.exception_table_length);
            // exception table  
            Attribute_Code.ExceptionTableItem exc;
            for (int i = 0; i < code.exception_table_length; i++) {
                exc = code.exception_table[i];
                out.writeShort(exc.start_pc);
                out.writeShort(exc.end_pc);
                out.writeShort(exc.handler_pc);
                out.writeShort(exc.catch_type);
            }
            // attributes  
            out.writeShort(code.attributes_count);
            for (int i = 0; i < code.attributes_count; i++) {
                dumpAttribute(code.attributes[i]);
            }
            break;
        case Constants.ATTRIBUTE_Exceptions:
            Attribute_Exceptions excep = (Attribute_Exceptions) attribute;
            out.writeShort(excep.number_of_exceptions);
            for (int i = 0; i < excep.number_of_exceptions; i++) {
                out.writeShort(excep.exception_index_table[i]);
            }
            break;
        case Constants.ATTRIBUTE_InnerClasses:
            Attribute_InnerClasses innerClasses = (Attribute_InnerClasses) attribute;
            Attribute_InnerClasses.InnerClass cla;
            out.writeShort(innerClasses.number_of_classes);
            for (int i = 0; i < innerClasses.number_of_classes; i++) {
                cla = innerClasses.innerClasses[i];
                out.writeShort(cla.inner_class_info_index);
                out.writeShort(cla.outer_class_info_index);
                out.writeShort(cla.inner_name_index);
                out.writeShort(cla.inner_class_access_flags);
            }
            break;
        case Constants.ATTRIBUTE_Deprecated:
        case Constants.ATTRIBUTE_Synthetic:
            // nothing to write  
            break;
        case Constants.ATTRIBUTE_LineNumberTable:
            // TODO: not supported yet  
            break;
        case Constants.ATTRIBUTE_LocalVariableTable:
            Attribute_LocalVariableTable lvt = (Attribute_LocalVariableTable) attribute;
            Attribute_LocalVariableTable.LocalVariable lv;
            out.writeShort(lvt.local_variable_table_length);
            for (int i = 0; i < lvt.local_variable_table_length; i++) {
                lv = lvt.local_variable_table[i];
                out.writeShort(lv.start_pc);
                out.writeShort(lv.length);
                out.writeShort(lv.name_index);
                out.writeShort(lv.descriptor_index);
                out.writeShort(lv.index);
            }
            break;
    }
}
