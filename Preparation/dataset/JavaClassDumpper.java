/*
 * Author jyang
 * Created on 2006-4-12 17:36:22
 */
package com.jasml.compiler;

import java.io.*;

import com.jasml.classes.*;


public class JavaClassDumpper {
	DataOutputStream out = null;

	JavaClass clazz = null;

	File destFile = null;

	public JavaClassDumpper(JavaClass clazz, File destFile) {
		this.destFile = destFile;
		this.clazz = clazz;
	}

	public void dump() throws IOException {
		out = new DataOutputStream(new FileOutputStream(destFile));
		dumpClassHeader();
		dumpConstantPool();
		dumpClassInfo();
		dumpInterfaces();
		dumpFields();
		dumpMethods();
		dumpClassAttributes();
		out.close();
	}

	/**
	 * dump the magic, minor and major version
	 * 
	 * @throws IOException
	 */
	private void dumpClassHeader() throws IOException {
		out.writeInt(clazz.magic);
		out.writeShort(clazz.minor_version);
		out.writeShort(clazz.major_version);
	}

	private void dumpConstantPool() throws IOException {
		ConstantPool pool = clazz.constantPool;
		ConstantPoolItem pi = null;
		int poolCount = pool.getConstantPoolCount();
		out.writeShort(poolCount);
		for (int i = 1; i < poolCount; i++) {
			pi = pool.getConstant(i);
			out.writeByte(pi.tag);
			switch (pi.tag) {
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
			default: // TODO: throws exceptoin
				int x = 9 / 0;
			}
		}
	}

	private void dumpClassInfo() throws IOException {
		out.writeShort(clazz.access_flags);
		out.writeShort(clazz.this_class);
		out.writeShort(clazz.super_class);
	}

	/**
	 * dump interfaces implemented by this class
	 * 
	 * @throws IOException
	 */

	private void dumpInterfaces() throws IOException {
		out.writeShort(clazz.interfaces_count);
		for (int i = 0; i < clazz.interfaces_count; i++) {
			out.writeShort(clazz.interfaces[i]);
		}
	}

	private void dumpFields() throws IOException {
		Field field = null;
		out.writeShort(clazz.fields_count);
		for (int i = 0; i < clazz.fields_count; i++) {
			field = clazz.fields[i];
			out.writeShort(field.access_flags);
			out.writeShort(field.name_index);
			out.writeShort(field.descriptor_index);
			out.writeShort(field.attributes_count);
			for (int j = 0; j < field.attributes_count; j++) {
				dumpAttribute(field.attributes[j]);
			}
		}
	}

	private void dumpMethods() throws IOException {
		Method method = null;
		out.writeShort(clazz.methods_count);
		for (int i = 0; i < clazz.methods_count; i++) {
			method = clazz.methods[i];
			out.writeShort(method.access_flags);
			out.writeShort(method.name_index);
			out.writeShort(method.descriptor_index);
			out.writeShort(method.attributes_count);
			for (int j = 0; j < method.attributes_count; j++) {
				dumpAttribute(method.attributes[j]);
			}
		}
	}

	private void dumpClassAttributes() throws IOException {
		out.writeShort(clazz.attributes_count);
		for (int i = 0; i < clazz.attributes_count; i++) {
			dumpAttribute(clazz.attributes[i]);
		}
	}

	private void dumpAttribute(Attribute attribute) throws IOException {
		out.writeShort(attribute.attribute_name_index);
		out.writeInt(attribute.attribute_length);

		switch (attribute.attribute_tag) {
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

}