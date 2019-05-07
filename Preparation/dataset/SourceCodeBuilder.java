/*
 * Author jyang Created on 2006-4-5 10:30:53
 */
package com.jasml.decompiler;

import java.util.HashSet;

import com.jasml.classes.Attribute;
import com.jasml.classes.Attribute_Code;
import com.jasml.classes.Attribute_ConstantValue;
import com.jasml.classes.Attribute_Deprecated;
import com.jasml.classes.Attribute_Exceptions;
import com.jasml.classes.Attribute_InnerClasses;
import com.jasml.classes.Attribute_LineNumberTable;
import com.jasml.classes.Attribute_LocalVariableTable;
import com.jasml.classes.Attribute_SourceFile;
import com.jasml.classes.Attribute_Synthetic;
import com.jasml.classes.ConstantPool;
import com.jasml.classes.ConstantPoolItem;
import com.jasml.classes.Constant_Class;
import com.jasml.classes.Constant_Double;
import com.jasml.classes.Constant_Fieldref;
import com.jasml.classes.Constant_Float;
import com.jasml.classes.Constant_Integer;
import com.jasml.classes.Constant_InterfaceMethodref;
import com.jasml.classes.Constant_Long;
import com.jasml.classes.Constant_Methodref;
import com.jasml.classes.Constant_NameAndType;
import com.jasml.classes.Constant_String;
import com.jasml.classes.Constant_Utf8;
import com.jasml.classes.Constants;
import com.jasml.classes.Field;
import com.jasml.classes.JavaClass;
import com.jasml.classes.Method;
import com.jasml.classes.Attribute_LocalVariableTable.LocalVariable;
import com.jasml.helper.OpcodeHelper;
import com.jasml.helper.Util;

public class SourceCodeBuilder {
	ConstantPool cpl;

	SourceCodeBuilderConfiguration config;

	public SourceCodeBuilder() {
		config = new SourceCodeBuilderConfiguration();
	}

	public SourceCodeBuilder(SourceCodeBuilderConfiguration config) {
		this.config = config;
	}

	private String toString(Attribute_ConstantValue var) {
		return toString(cpl.getConstant(var.constant_value_index));
	}

	private String toString(Attribute_Deprecated var) {
		return "[" + Constants.ATTRIBUTE_NAME_DEPRECATED + "]";
	}

	private String toString(Attribute_Synthetic var) {
		return "[" + Constants.ATTRIBUTE_NAME_SYNTHETIC + "]";
	}

	private String toString(Attribute_SourceFile var) {
		return "[" + Constants.ATTRIBUTE_NAME_SOURCE_FILE + " : " + toString(cpl.getConstant(var.sourcefile_index)) + "]";
	}

	private String toString(Attribute_LocalVariableTable attr, Attribute_Code.Opcode[] ops) {
		if (attr.local_variable_table_length == 0)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append("[" + Constants.ATTRIBUTE_NAME_LOCAL_VARIABLE + " :");
		LocalVariable var;
		for (int i = 0; i < attr.local_variable_table_length; i++) {
			var = attr.local_variable_table[i];
			buf.append(Constants.LINE_SEPARATER);
			buf.append(Util.descriptorToString(toString(cpl.getConstant(var.descriptor_index)))); // descriptor
			buf.append(" ");
			buf.append(toString(cpl.getConstant(var.name_index))); // name
			buf.append("  ");
			buf.append("start=" + config.labelPrefix + var.start_pc); // start position
			buf.append(", ");
			buf.append("end=" + config.labelPrefix + (findPreviousInstruction(var.start_pc + var.length, ops)).offset); // valid scope
			buf.append(", ");
			buf.append("index=" + var.index); // index into runtime frame
		}
		buf.append("]");
		return buf.toString();
	}

	private String toString(Attribute_LineNumberTable attr) {
		if (attr.line_number_table_length == 0)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append("[" + Constants.ATTRIBUTE_NAME_LINE_NUMBER_TABLE + " :");
		for (int i = 0; i < attr.line_number_table_length; i++) {
			buf.append(Constants.LINE_SEPARATER);
			buf.append(config.labelPrefix + attr.lineNumberTable[i].start_pc + " ->  " + attr.lineNumberTable[i].line_number);
		}
		buf.append("]");
		return buf.toString();
	}

	private String toString(Attribute_InnerClasses attr) {
		StringBuffer buf = new StringBuffer();
		Attribute_InnerClasses.InnerClass innerClass;
		buf.append("[" + Constants.ATTRIBUTE_NAME_INNER_CLASSES + " :");
		for (int i = 0; i < attr.number_of_classes; i++) {
			buf.append(Constants.LINE_SEPARATER);
			innerClass = attr.innerClasses[i];
			// access flag
			buf.append("access = " + Util.accessFlagToString_Class((short) innerClass.inner_class_access_flags) + " , ");
			// inner class name
			buf.append("name = ");
			if (innerClass.inner_name_index == 0) {
				buf.append("0 , ");
			} else {
				buf.append(toString(cpl.getConstant(innerClass.inner_name_index)) + " , ");
			}

			// inner class info
			buf.append("fullname = ");
			buf.append(toString(cpl.getConstant(innerClass.inner_class_info_index)) + " , ");

			// outer class info
			buf.append("outername = ");
			if (innerClass.outer_class_info_index == 0) {
				buf.append("0");
			} else {
				buf.append(toString(cpl.getConstant(innerClass.outer_class_info_index)));
			}
		}
		buf.append(']');
		return buf.toString();
	}

	private String toString(Attribute_Exceptions attr) {
		if (attr.number_of_exceptions == 0)
			return "";

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < attr.number_of_exceptions; i++) {
			buf.append(toString(cpl.getConstant(attr.exception_index_table[i])) + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	private String getLocalVariableName(int index, int codeOffset, Attribute_LocalVariableTable.LocalVariable[] lvts) {
		if (lvts == null) {
			return "UnknowVariable";
		}
		int i;
		Attribute_LocalVariableTable.LocalVariable lv;
		for (i = 0; i < lvts.length; i++) {
			lv = lvts[i];
			if (lv.index == index && lv.start_pc <= codeOffset && (lv.start_pc + lv.length) >= codeOffset) {
				return toString(cpl.getConstant(lv.name_index));
			}
		}
		// no match found, TODO: why could this happen
		// search for the one with the same index value
		for (i = 0; i < lvts.length; i++) {
			lv = lvts[i];
			if (lv.index == index) {
				return toString(cpl.getConstant(lv.name_index));
			}
		}

		// still not found
		return "unknown_local_variable";

		// TODO: this is little tricky
	}

	private String toString(Attribute_Code code, HashSet referedLines) {
		StringBuffer buf = new StringBuffer();
		Attribute_Code.Opcode op;
		Attribute_Code.Opcode[] ops = code.codes;
		byte[][] operands;
		int ti, def, low, high, jump_count, npairs;
		String soffset;

		Attribute_LocalVariableTable.LocalVariable[] lvts = null;
		for (int i = 0; i < code.attributes_count; i++) {
			if (code.attributes[i] instanceof Attribute_LocalVariableTable) {
				lvts = ((Attribute_LocalVariableTable) code.attributes[i]).local_variable_table;
				break;
			}
		}

		// instructions
		if (code.code_length != 0) {
			for (int t = 0; t < ops.length; t++) {
				op = ops[t];
				operands = op.operands;
				// offset
				soffset = Integer.toString(op.offset);
				if (referedLines.contains(soffset) == true) {
					if (config.labelInSingleLine == true) {
						buf.append(config.labelPrefix + soffset + " : ");
						buf.append(Constants.LINE_SEPARATER);
						buf.append(config.instructionPadding);
					} else {
						buf.append(Util.padChar(config.labelPrefix + soffset, config.labelLength, ' ') + " : ");
					}
				} else {
					buf.append(config.instructionPadding);
				}
				// opcode name
				buf.append(Constants.OPCODE_NAMES[0xFF & op.opcode] + "  ");
				switch (op.opcode) {
				case Constants.TABLESWITCH:
					def = Util.getNum(operands[1]) + op.offset;
					low = Util.getNum(operands[2]);
					high = Util.getNum(operands[3]);
					jump_count = high - low + 1;

					buf.append("default=" + config.labelPrefix + def + ", low=" + low + ", high=" + high + ", jump_table:");

					for (int i = 0; i < jump_count; i++) {
						// jump address is calculated by adding with tableswitch offset.
						buf.append(config.labelPrefix + (Util.getNum(operands[i + 4]) + op.offset) + ",");
					}
					buf.deleteCharAt(buf.length() - 1);
					break;
				case Constants.LOOKUPSWITCH: {
					def = Util.getNum(operands[1]) + op.offset;
					npairs = Util.getNum(operands[2]);

					buf.append("default=" + config.labelPrefix + def + ", npairs=" + npairs + ", jump_table:");
					if (npairs != 0) {
						for (int i = 0; i < npairs; i++) {
							buf.append(Util.getNum(operands[i * 2 + 3]));
							buf.append("->");
							buf.append(config.labelPrefix + (Util.getNum(operands[i * 2 + 4]) + op.offset) + ",");

						}
						buf.deleteCharAt(buf.length() - 1);
					}
				}
					break;
				/*
				 * Two address bytes + offset from start of byte stream form the
				 * jump target
				 */
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
				/*
				 * 32-bit wide jumps
				 */
				case Constants.GOTO_W:
				case Constants.JSR_W:
					buf.append(config.labelPrefix + (Util.getSignedNum(operands[0]) + op.offset));
					break;
				/*
				 * Index byte references local variable
				 */
				case Constants.ALOAD:
				case Constants.ASTORE:
				case Constants.DLOAD:
				case Constants.DSTORE:
				case Constants.FLOAD:
				case Constants.FSTORE:
				case Constants.ILOAD:
				case Constants.ISTORE:
				case Constants.LLOAD:
				case Constants.LSTORE:
				case Constants.RET:
					ti = Util.getNum(operands[0]); // the index into local variable
					// table
					buf.append(getLocalVariableName(ti, op.offset, lvts) + "(" + ti + ")");
					break;
				/*
				 * Remember wide byte which is used to form a 16-bit address in the
				 * following instruction. Relies on that the method is called again
				 * with the following opcode.
				 */
				case Constants.WIDE:
					// TODO: testing
					break;
				/*
				 * Array of basic type.
				 */
				case Constants.NEWARRAY:
					buf.append(Constants.TYPE_NAMES[Util.getNum(operands[0])]);
					break;
				/*
				 * Access object/class fields.
				 */
				case Constants.GETFIELD:
				case Constants.GETSTATIC:
				case Constants.PUTFIELD:
				case Constants.PUTSTATIC:
				/*
				 * Operands are references to classes in constant pool
				 */
				case Constants.NEW:
				case Constants.CHECKCAST:
				case Constants.INSTANCEOF:
				/*
				 * Operands are references to methods in constant pool
				 */
				case Constants.INVOKESPECIAL:
				case Constants.INVOKESTATIC:
				case Constants.INVOKEVIRTUAL:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					break;

				case Constants.INVOKEINTERFACE:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					buf.append(" ");
					buf.append(Util.getNum(operands[1]));
					break;

				/*
				 * Operands are references to items in constant pool
				 */
				case Constants.LDC_W:
				case Constants.LDC2_W:
				case Constants.LDC:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					break;

				/*
				 * Array of references.
				 */
				case Constants.ANEWARRAY:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					break;
				/*
				 * Multidimensional array of references.
				 */
				case Constants.MULTIANEWARRAY:
					buf.append(toString(cpl.getConstant(Util.getNum(operands[0]))));
					buf.append(' ');					
					buf.append(Util.getNum(operands[1]));
					break;
				/*
				 * Increment local variable.
				 */
				case Constants.IINC:
					ti = Util.getNum(operands[0]);
					buf.append(getLocalVariableName(ti, op.offset, lvts) + "(" + ti + ") " + Util.getSignedNum(operands[1]));
					break;
				default:
					if (operands != null) {
						for (int i = 0; i < operands.length; i++) {
							buf.append(Util.getNum(operands[i]) + " ");
						}
					}
				}

				if (config.showInfo == true) {
					buf.append("   //");
					buf.append(OpcodeHelper.getOpcodeInfo(op.opcode).operation);
				}
				buf.append(Constants.LINE_SEPARATER);
			}
		}

		// Local variable table
		for (int i = 0; i < code.attributes_count; i++) {
			if (code.attributes[i] instanceof Attribute_LocalVariableTable
					&& ((Attribute_LocalVariableTable) code.attributes[i]).local_variable_table_length != 0) {
				buf.append(Constants.LINE_SEPARATER);
				buf.append(toString((Attribute_LocalVariableTable) code.attributes[i], ops));
				break;
			}
		}

		// Exception table
		if (code.exception_table_length != 0) {
			buf.append(Constants.LINE_SEPARATER);
			buf.append(Constants.LINE_SEPARATER);

			buf.append("[" + Constants.ATTRIBUTE_NAME_EXCEPTION_TABLE + ":");
			for (int i = 0; i < code.exception_table_length; i++) {
				buf.append(Constants.LINE_SEPARATER);
				buf.append("start=" + config.labelPrefix + code.exception_table[i].start_pc);
				buf.append(" , ");
				buf.append("end=" + config.labelPrefix + code.exception_table[i].end_pc);
				buf.append(" , ");
				buf.append("handler=" + config.labelPrefix + code.exception_table[i].handler_pc);
				buf.append(" , ");
				if (code.exception_table[i].catch_type != 0) {
					buf.append("catch_type=" + toString(cpl.getConstant(code.exception_table[i].catch_type)));
				} else {
					buf.append("catch_type=0");
				}
			}
			buf.append("]");
		}

		// Line number table
		if (config.showLineNumber == true) {
			for (int i = 0; i < code.attributes_count; i++) {
				if (code.attributes[i] instanceof Attribute_LineNumberTable
						&& ((Attribute_LineNumberTable) code.attributes[i]).line_number_table_length != 0) {
					buf.append(Constants.LINE_SEPARATER);
					buf.append(Constants.LINE_SEPARATER);
					buf.append(toString((Attribute_LineNumberTable) code.attributes[i]));
					break;
				}
			}
		}

		// max_stack
		buf.append(Constants.LINE_SEPARATER);
		buf.append(Constants.LINE_SEPARATER);
		buf.append("[" + Constants.ATTRIBUTE_NAME_MAX_STACK + " : " + code.max_stack + "]");
		// max_local
		buf.append(Constants.LINE_SEPARATER);
		buf.append("[" + Constants.ATTRIBUTE_NAME_MAX_LOCAL + " : " + code.max_locals + "]");
		return buf.toString();
	}

	private String toString(Attribute var) {
		switch (var.attribute_tag) {
		case Constants.ATTRIBUTE_SourceFile:
			return toString((Attribute_SourceFile) var);

		case Constants.ATTRIBUTE_ConstantValue:
			return toString((Attribute_ConstantValue) var);

		case Constants.ATTRIBUTE_Code:
			return toString((Attribute_Code) var);

		case Constants.ATTRIBUTE_Exceptions:
			return toString((Attribute_Exceptions) var);

		case Constants.ATTRIBUTE_InnerClasses:
			return toString((Attribute_InnerClasses) var);

		case Constants.ATTRIBUTE_Synthetic:
			return toString((Attribute_Synthetic) var);

		case Constants.ATTRIBUTE_LineNumberTable:
			return toString((Attribute_LineNumberTable) var);

		case Constants.ATTRIBUTE_LocalVariableTable:
			return toString((Attribute_LocalVariableTable) var);

		case Constants.ATTRIBUTE_Deprecated:
			return toString((Attribute_Deprecated) var);

		default: {
			StringBuffer buf = new StringBuffer();
			if (var.attribute_name == null) {
				// this is an unknow attribute
				buf.append(toString(cpl.getConstant(var.attribute_name_index)));
			} else {
				buf.append(var.attribute_name);
			}
			if (var.attrInfo != null) {
				buf.append(" = " + new String(var.attrInfo));
			}
			return buf.toString();
		}
		}

	}

	private String toString(Constant_Float var) {
		return Float.toString(var.value) + "F";
	}

	private String toString(Constant_Long var) {
		return Long.toString(var.value) + "L";
	}

	private String toString(Constant_Double var) {
		return Double.toString(var.value) + "D";
	}

	private String toString(Constant_Utf8 var) {
		return var.bytes;
	}

	private String toString(ConstantPoolItem var) {
		switch (var.tag) {
		case Constants.CONSTANT_Utf8:
			return toString((Constant_Utf8) var);
		case Constants.CONSTANT_Integer:
			return toString((Constant_Integer) var);

		case Constants.CONSTANT_Float:
			return toString((Constant_Float) var);

		case Constants.CONSTANT_Long:
			return toString((Constant_Long) var);

		case Constants.CONSTANT_Double:
			return toString((Constant_Double) var);

		case Constants.CONSTANT_Class:
			return toString((Constant_Class) var);

		case Constants.CONSTANT_Fieldref:
			return toString((Constant_Fieldref) var);

		case Constants.CONSTANT_String:
			return toString((Constant_String) var);

		case Constants.CONSTANT_Methodref:
			return toString((Constant_Methodref) var);

		case Constants.CONSTANT_InterfaceMethodref:
			return toString((Constant_InterfaceMethodref) var);

		case Constants.CONSTANT_NameAndType:
			return toString((Constant_NameAndType) var);
		default:
			return var.tagName;
		}
	}

	private String toString(Constant_Integer var) {
		return Integer.toString(var.value);
	}

	private String toString(Constant_Class var) {
		return Util.constantClassToString(toString((Constant_Utf8) cpl.getConstant(var.name_index)));
	}

	private String toString(Constant_Fieldref var) {
		String name, type, temp = toString(cpl.getConstant(var.name_and_type_index));
		int i = temp.indexOf(" ");
		name = temp.substring(0, i);
		type = temp.substring(i + 1);
		type = Util.descriptorToString(type);

		return type + " " + toString(cpl.getConstant(var.class_index)) + "." + name;
	}

	private String toString(Constant_InterfaceMethodref var) {
		String name, retType, para, temp = toString(cpl.getConstant(var.name_and_type_index));
		int i = temp.indexOf(" ");
		name = temp.substring(0, i);
		i = temp.indexOf((char) ')');
		para = temp.substring(temp.indexOf((char) '(') + 1, i);
		retType = temp.substring(i + 1, temp.length());

		para = Util.methodParameterToString(para);
		retType = Util.descriptorToString(retType);
		return retType + " " + toString(cpl.getConstant(var.class_index)) + "." + name + "(" + para + ")";
	}

	private String toString(Constant_Methodref var) {
		String name, retType, para = null, temp = toString(cpl.getConstant(var.name_and_type_index));
		int i = temp.indexOf(" ");
		name = temp.substring(0, i);
		i = temp.indexOf((char) ')');
		para = temp.substring(temp.indexOf((char) '(') + 1, i);
		retType = temp.substring(i + 1, temp.length());

		para = Util.methodParameterToString(para);
		retType = Util.descriptorToString(retType);
		return retType + " " + toString(cpl.getConstant(var.class_index)) + "." + name + "(" + para + ")";
	}

	private String toString(Constant_NameAndType var) {
		return toString(cpl.getConstant(var.name_index)) + " " + toString(cpl.getConstant(var.descriptor_index));
	}

	private String toString(Constant_String var) {
		return Util.toViewableString(toString(cpl.getConstant(var.string_index)));
	}

	public String toString(JavaClass clazz) {
		StringBuffer buf = new StringBuffer();
		this.cpl = clazz.constantPool;

		if (config.showVersion == true) {
			// Minor and Major version of the class
			buf.append("[Major : " + Integer.toString(clazz.major_version) + "]");
			buf.append(Constants.LINE_SEPARATER);
			buf.append("[Minor : " + Integer.toString(clazz.minor_version) + "]");
			buf.append(Constants.LINE_SEPARATER);
		}

		// class access flag + class name
		buf.append(Util.accessFlagToString_Class(clazz.access_flags) + " " + toString(cpl.getConstant(clazz.this_class)));
		// super classes
		if (clazz.super_class != 0) {
			// java.lang.Object dose not hava super class
			buf.append(" extends " + toString(cpl.getConstant(clazz.super_class)));
		}
		// implemented interfaces
		if (clazz.interfaces_count != 0) {
			buf.append(" implements ");
			for (int i = 0; i < clazz.interfaces_count; i++) {
				buf.append(toString(cpl.getConstant(clazz.interfaces[i])) + ",");
			}
			buf.deleteCharAt(buf.length() - 1);
		}

		buf.append("{");

		// fields
		if (clazz.fields_count != 0) {
			for (int i = 0; i < clazz.fields_count; i++) {
				buf.append(Constants.LINE_SEPARATER);
				buf.append(toString(clazz.fields[i]));
			}
		}

		// methods
		if (clazz.methods_count != 0) {
			for (int i = 0; i < clazz.methods_count; i++) {
				buf.append(Constants.LINE_SEPARATER);
				buf.append(toString(clazz.methods[i]));
			}
		}

		// attributes of this class
		if (clazz.attributes_count != 0) {
			buf.append(Constants.LINE_SEPARATER);
			for (int i = 0; i < clazz.attributes_count; i++) {
				buf.append(Constants.LINE_SEPARATER);
				String tx = toString(clazz.attributes[i]);
				buf.append(tx);
			}
		}

		buf.append(Constants.LINE_SEPARATER);
		buf.append("}");
		return buf.toString();
	}

	private String toString(Field field) {
		StringBuffer buf = new StringBuffer();

		// access flag
		buf.append(Util.accessFlagToString_Field((short) field.access_flags) + " ");
		// field descriptor
		buf.append(Util.descriptorToString(toString(cpl.getConstant(field.descriptor_index))) + " ");
		// field name
		buf.append(toString(cpl.getConstant(field.name_index)));
		// constant value
		if (field.attributes_count != 0) {
			for (int i = 0; i < field.attributes_count; i++) {
				if (field.attributes[i] instanceof Attribute_ConstantValue) {
					buf.append(" = ");
					buf.append(toString(field.attributes[i]));
				}
			}
		}

		// deprecated or synthetic attribute
		if (field.attributes_count != 0) {
			for (int i = 0; i < field.attributes_count; i++) {
				if ((field.attributes[i] instanceof Attribute_ConstantValue) == false) {
					buf.append('\t');
					buf.append(toString(field.attributes[i]));
				}
			}
		}
		return buf.toString().trim();
	}

	private String toString(Method method) {
		StringBuffer buf = new StringBuffer();

		// method access flag
		buf.append(Util.accessFlagToString_Method((short) method.access_flags) + " ");

		// method parameter and return type
		String retType, paras, temp = toString(cpl.getConstant(method.descriptor_index));
		int ti = temp.indexOf(")");
		paras = Util.methodParameterToString(temp.substring(1, ti));
		retType = Util.descriptorToString(temp.substring(ti + 1));

		// return type
		buf.append(retType + " ");
		// method name
		buf.append(toString(cpl.getConstant(method.name_index)) + " ");
		// method para
		buf.append("(" + paras + ")");

		// exception
		if (method.attributes_count != 0) {
			for (int i = 0; i < method.attributes_count; i++) {
				if (method.attributes[i].attribute_tag == Constants.ATTRIBUTE_Exceptions) {
					buf.append(" throws ");
					buf.append(toString(method.attributes[i]));
				}
			}
		}

		buf.append("{");
		// code
		if (method.attributes_count != 0) {
			for (int i = 0; i < method.attributes_count; i++) {
				if (method.attributes[i].attribute_tag == Constants.ATTRIBUTE_Code) {
					buf.append(Constants.LINE_SEPARATER);
					buf.append(toString((Attribute_Code) method.attributes[i], calculateReferences(method)));
				}
			}
		}
		if (method.attributes_count != 0) {
			for (int i = 0; i < method.attributes_count; i++) {
				if (method.attributes[i].attribute_tag != Constants.ATTRIBUTE_Code
						&& method.attributes[i].attribute_tag != Constants.ATTRIBUTE_Exceptions) {
					buf.append(Constants.LINE_SEPARATER);
					buf.append(toString(method.attributes[i]));
				}
			}

		}
		buf.append(Constants.LINE_SEPARATER);
		buf.append("}");

		return buf.toString();
	}

	/**
	 * generate a set containing all the line numbers which are refered inside
	 * the method.
	 * 
	 * @param meth
	 * @return
	 */
	private HashSet calculateReferences(Method meth) {
		HashSet set = new HashSet();
		Attribute att;
		Attribute_Code.Opcode[] ops = null;
		Attribute_Code.Opcode op;
		for (int i = 0; i < meth.attributes_count; i++) {
			if (meth.attributes[i].attribute_tag == Constants.ATTRIBUTE_Code) {
				ops = ((Attribute_Code) meth.attributes[i]).codes;
				break;
			}
		}
		if (ops == null) {
			return set;
		}

		for (int i = 0; i < meth.attributes_count; i++) {
			att = meth.attributes[i];
			if (att.attribute_tag == Constants.ATTRIBUTE_Code) {
				Attribute_Code code = (Attribute_Code) att;
				for (int j = 0; j < ops.length; j++) {
					op = ops[j];
					switch (op.opcode) {

					case Constants.LOOKUPSWITCH:
						set.add(Integer.toString(Util.getSignedNum(op.operands[1]) + op.offset)); //default
						for (int t = 4; t < op.operands.length; t++) {
							set.add(Integer.toString(Util.getSignedNum(op.operands[t++]) + op.offset));
						}
						break;
					case Constants.TABLESWITCH:
						set.add(Integer.toString(Util.getSignedNum(op.operands[1]) + op.offset)); //default
						for (int t = 4; t < op.operands.length; t++) {
							set.add(Integer.toString(Util.getSignedNum(op.operands[t]) + op.offset));
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
					case Constants.GOTO_W:
					case Constants.JSR_W:
						set.add(Integer.toString(Util.getSignedNum(op.operands[0]) + op.offset));
						break;

					}
				}
				if (code.exception_table_length != 0) {
					Attribute_Code.ExceptionTableItem[] exceptions = code.exception_table;
					Attribute_Code.ExceptionTableItem exc;
					for (int j = 0; j < exceptions.length; j++) {
						exc = exceptions[j];
						set.add(Integer.toString(exc.start_pc));
						set.add(Integer.toString(exc.end_pc));
						set.add(Integer.toString(exc.handler_pc));
					}
				}

				if (code.attributes_count != 0) {
					for (int j = 0; j < code.attributes_count; j++) {
						if (code.attributes[j].attribute_tag == Constants.ATTRIBUTE_LineNumberTable && config.showLineNumber == true) {
							Attribute_LineNumberTable lineNumberTable = (Attribute_LineNumberTable) code.attributes[j];
							Attribute_LineNumberTable.LineNumber[] lines = lineNumberTable.lineNumberTable;
							for (int x = 0; x < lineNumberTable.line_number_table_length; x++) {
								set.add(Integer.toString(lines[x].start_pc));
							}
						} else if (code.attributes[j].attribute_tag == Constants.ATTRIBUTE_LocalVariableTable) {
							Attribute_LocalVariableTable lvt = (Attribute_LocalVariableTable) code.attributes[j];
							if (lvt.local_variable_table_length != 0) {
								Attribute_LocalVariableTable.LocalVariable[] lvs = lvt.local_variable_table;
								Attribute_LocalVariableTable.LocalVariable lv;
								for (int x = 0; x < lvs.length; x++) {
									lv = lvs[x];
									set.add(Integer.toString(lv.start_pc));
									if (lv.length != 1) {
										op = findPreviousInstruction(lv.start_pc + lv.length, ops);
										if (op != null) {
											set.add(Integer.toString(op.offset));
										}
									}
								}
							}
						}
					}
				}
				break;
			}
		}
		return set;
	}

	/**
	 * given an offset, and a series of instructions, find the first instruction
	 * that is before the given offset
	 * 
	 * @param offset
	 * @param ops
	 * @return
	 */
	private Attribute_Code.Opcode findPreviousInstruction(int offset, Attribute_Code.Opcode[] ops) {
		for (int i = ops.length - 1; i > -1; i--) {
			if (ops[i].offset < offset) {
				return ops[i];
			}
		}
		return null;
	}

	public static String toString_Static(JavaClass clazz) {
		SourceCodeBuilder builder = new SourceCodeBuilder();
		return builder.toString(clazz);
	}

}