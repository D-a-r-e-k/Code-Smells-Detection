package com.jasml.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import com.jasml.classes.Attribute;
import com.jasml.classes.Attribute_Code;
import com.jasml.classes.Attribute_ConstantValue;
import com.jasml.classes.Attribute_Deprecated;
import com.jasml.classes.Attribute_Exceptions;
import com.jasml.classes.Attribute_InnerClasses;
import com.jasml.classes.Attribute_LocalVariableTable;
import com.jasml.classes.Attribute_SourceFile;
import com.jasml.classes.Attribute_Synthetic;
import com.jasml.classes.Constants;
import com.jasml.classes.Field;
import com.jasml.classes.JavaClass;
import com.jasml.classes.Method;
import com.jasml.helper.IntegerArray;
import com.jasml.helper.OpcodeHelper;
import com.jasml.helper.OpcodeInfo;
import com.jasml.helper.Util;

public class SourceCodeParser implements Scannable {
	private Scanner scanner;

	JavaClass javaClass;

	ConstantPoolGenerator cpl = new ConstantPoolGenerator();

	boolean precompile = false;

	public SourceCodeParser(File file) throws ParsingException {
		scanner = new Scanner(file);
	}

	public SourceCodeParser(String content) throws ParsingException {
		scanner = new Scanner(content);
	}

	public JavaClass parse() throws ParsingException {
		javaClass = new JavaClass();
		cpl = new ConstantPoolGenerator();
		if (precompile == true) {
			preprocessConstantValues();
		}
		parseClass();
		return javaClass;
	}

	private void preprocessConstantValues() throws ParsingException {
		scanner.mark();
		String t;
		while (scanner.nextToken() != EOF) {
			switch (scanner.tokenType()) {
			case String:
				t = scanner.token();
				cpl.addString(Util.parseViewableString(t.substring(1, t.length() - 1)));
				break;
			case Number_Double:
				t = scanner.token();
				cpl.addDouble( parseDouble(t ));
			case Number_Long:
				t = scanner.token();
				cpl.addDouble(parseLong(t ));
				break;
			}
		}
		scanner.restore();
	}

	private void parseClass() throws ParsingException, GrammerException {
		scanner.nextToken();
		if (scanner.tokenType() == Attribute) {
			parseMajorOrMinor();
		}
		parseClassSignature();
		parseFields();
		parseMethods();
		parseClassAttributes();
		if (scanner.tokenType() != Bracket_Right) {
			exception(scanner, "'}'.expected.here");
		}
		if (scanner.nextToken() != EOF) {
			exception(scanner, "end.of.class.expected.here");
		}
		javaClass.constantPool = cpl.getConstantPool();
		javaClass.constant_pool_count = javaClass.constantPool.getConstantPoolCount();
	}

	private void parseClassSignature() throws ParsingException {
		// access flags
		int acc = 0;
		while (scanner.tokenType() == AccessFlag) {
			acc = acc | Util.getAccessFlag_Class(scanner.token());
			scanner.nextToken();
		}
		if (acc == 0) {
			exception(scanner, "\"class\".expected.here");
		}
		javaClass.access_flags = (short) acc;

		// class name
		javaClass.this_class = cpl.addClass(scanner.token());
		scanner.nextToken();

		//interfaces and super classes
		while (scanner.tokenType() != Bracket_Left && scanner.tokenType() != EOF) {
			if ("extends".equals(scanner.token()) == true) {
				scanner.nextToken();
				javaClass.super_class = cpl.addClass(scanner.token());
				scanner.nextToken();
			} else if ("implements".equals(scanner.token()) == true) {
				scanner.nextToken();
				IntegerArray array = new IntegerArray(5);
				while (scanner.tokenType() != Bracket_Left && scanner.tokenType() != EOF) {
					array.add(cpl.addClass(scanner.token()));
					scanner.nextToken();
					if (scanner.tokenType() == Comma) {
						scanner.nextToken();
					}
				}
				javaClass.interfaces = array.getAll();
				javaClass.interfaces_count = javaClass.interfaces.length;
			} else {
				exception(scanner, "unexpected.character.here");
			}
		}
		scanner.nextToken();
	}

	private void parseFields() throws ParsingException, GrammerException {
		ArrayList fields = new ArrayList(10);
		Object field;
		do {
			field = parseField();
			if (field != null) {
				fields.add(field);
			}
		} while (field != null);
		javaClass.fields = (Field[]) fields.toArray(new Field[fields.size()]);
		javaClass.fields_count = javaClass.fields.length;
	}

	private Field parseField() throws ParsingException, GrammerException {
		if (scanner.tokenType() == Attribute || scanner.tokenType() == Bracket_Right) {
			return null;
		}
		scanner.mark();
		int acc = 0;
		while (scanner.tokenType() == AccessFlag) {
			acc = acc | Util.getAccessFlag_Field(scanner.token());
			scanner.nextToken();
		}
		String fieldType = scanner.token();
		scanner.nextToken();
		String fieldName = scanner.token();
		scanner.nextToken();
		String maybeEuqal = scanner.token(); // the next char may be a '=', marks the presence of a constant value attribute

		if (fieldType.indexOf('(') != -1 || fieldName.indexOf('(') != -1 || maybeEuqal.indexOf('(') != -1) {
			// the presence of '(' marks an method declaration
			scanner.restore();
			return null;
		}
		ArrayList attributes = new ArrayList(3);
		fieldType = Util.toInnerType(fieldType);

		if (scanner.tokenType() == Equal) {
			scanner.nextToken();
			String constValue = scanner.token();
			int const_index;

			Attribute_ConstantValue con = null;
			switch (fieldType.charAt(0)) {
			case 'B':
			case 'C':
			case 'I':
			case 'S':
			case 'Z':
				const_index = cpl.addInteger(parseInteger(constValue));
				con = new Attribute_ConstantValue(2, const_index);
				break;
			case 'D':
				const_index = cpl.addDouble(parseDouble(constValue));
				con = new Attribute_ConstantValue(2, const_index);
				break;
			case 'F':
				const_index = cpl.addFloat(parseFloat(constValue));
				con = new Attribute_ConstantValue(2, const_index);
				break;
			case 'J':
				const_index = cpl.addLong(parseLong(constValue));
				con = new Attribute_ConstantValue(2, const_index);
				break;
			case 'L':
				if (fieldType.equals("Ljava/lang/String;") == true) {
					const_index = cpl.addString(Util.parseViewableString(constValue.substring(1, constValue.length() - 1))); // trim
					// the
					// '"'
					con = new Attribute_ConstantValue(2, const_index);
					break;
				}
			default:
				exception(scanner, "can.not.assign.contant.value.to.this.field.type.only.primitive.types.and.string.allowed");
			}
			con.attribute_name_index = cpl.addUtf8("ConstantValue");
			attributes.add(con);
			scanner.nextToken();
		}
		while (scanner.tokenType() == Attribute) {
			attributes.add(parseAttribute());
		}

		Field ret = new Field(acc, cpl.addUtf8(fieldName), cpl.addUtf8(fieldType), attributes.size(), (Attribute[]) attributes
				.toArray(new Attribute[attributes.size()]));

		return ret;

	}

	private void parseMethods() throws ParsingException, GrammerException {
		ArrayList methods = new ArrayList(10);
		Object method;
		do {
			method = parseMethod();
			if (method != null) {
				methods.add(method);
			}
		} while (method != null);
		javaClass.methods = (Method[]) methods.toArray(new Method[methods.size()]);
		javaClass.methods_count = javaClass.methods.length;
	}

	private Method parseMethod() throws ParsingException, GrammerException {
		if (scanner.tokenType() == Attribute || scanner.tokenType() == Bracket_Right || scanner.tokenType() == EOF) {
			return null;
		}
		Method method = new Method(0, 0, 0, 0, new Attribute[0]);
		LabeledInstructions li;

		ArrayList attributes = new ArrayList(4), codeAttributes;
		parseMethodSignature(method, attributes);
		if (Util.hasMethodBody((short) method.access_flags) == true) {
			codeAttributes = new ArrayList(4);
			li = parseMethodInstructions(method);
			Attribute_Code code = new Attribute_Code();
			code.attribute_name_index = cpl.addUtf8("Code");
			code.codes = li.codes;
			parseMethodAttributes(method, attributes, li, code, codeAttributes);
			code.code_length = li.codeLength;
			code.attributes = (Attribute[]) codeAttributes.toArray(new Attribute[codeAttributes.size()]);
			code.attributes_count = code.attributes.length;
			code.attribute_length = 2/*max_stack*/+ 2/*max_locals*/+ 4/*code_length*/+ code.code_length/*code*/+ 2/*exception_table_length*/
					+ code.exception_table_length * 8 + 2/*attribute_count*/;
			for (int i = 0; i < code.attributes_count; i++) {
				code.attribute_length += code.attributes[i].attribute_length + 6;
			}
			attributes.add(code);
		} else {
			parseMethodAttributes(method, attributes, null, null, null);
		}
		scanner.nextToken();
		method.attributes = (Attribute[]) attributes.toArray(new Attribute[attributes.size()]);
		method.attributes_count = method.attributes.length;
		return method;
	}

	/**
	 * this method will parse method attribute: Deprecated, Synthetic
	 * and some of the attributes belongs to code:  Max Locals, Max Stack,Local variale table, Exception table
	 * 
	 * @param method
	 * @param attributes
	 */
	private void parseMethodAttributes(Method method, ArrayList attributes, LabeledInstructions li, Attribute_Code code, ArrayList codeAttributes)
			throws GrammerException, ParsingException {
		String temp;
		while (scanner.tokenType() == Attribute) {
			temp = scanner.token();
			if (temp.indexOf(Constants.ATTRIBUTE_NAME_LOCAL_VARIABLE) != -1) {
				codeAttributes.add(parseLocalVariableTable(temp, li.labels));
				scanner.nextToken();
			} else if (temp.indexOf(Constants.ATTRIBUTE_NAME_EXCEPTION_TABLE) != -1) {
				code.exception_table = parseExceptionTable(temp, li.labels);
				code.exception_table_length = code.exception_table.length;
				scanner.nextToken();
			} else if (temp.indexOf(Constants.ATTRIBUTE_NAME_MAX_STACK) != -1) {
				parseMaxStackOrLocals(code);
				scanner.nextToken();
			} else if (temp.indexOf(Constants.ATTRIBUTE_NAME_MAX_LOCAL) != -1) {
				parseMaxStackOrLocals(code);
				scanner.nextToken();
			} else if (temp.indexOf(Constants.ATTRIBUTE_NAME_DEPRECATED) != -1) {
				attributes.add(parseAttribute());
			} else if (temp.indexOf(Constants.ATTRIBUTE_NAME_SYNTHETIC) != -1) {
				attributes.add(parseAttribute());
			} else if (temp.indexOf(Constants.ATTRIBUTE_NAME_LINE_NUMBER_TABLE) != -1) {
				scanner.nextToken();
				if (false) {
					parseLineNumbers(null);
				}
			} else {
				exception(scanner, "unexpected.attribute." + scanner.token());
			}
		}

		//		// dose a code attribute must have an local variable attribute? TODO:
		//		boolean isLocalVariableDefined = false;
		//		Attribute_LocalVariableTable lv;
		//		for (int i = 0; i < codeAttributes.size(); i++) {
		//			if (codeAttributes.get(i) instanceof Attribute_LocalVariableTable) {
		//				isLocalVariableDefined = true;
		//				lv = (Attribute_LocalVariableTable) codeAttributes.get(i);
		//				break;
		//			}
		//		}
		//		if (isLocalVariableDefined == false) {
		//			lv = new Attribute_LocalVariableTable(2, 0, null);
		//			lv.attribute_name_index = cpl.addUtf8("LocalVariableTable");
		//			codeAttributes.add(lv);
		//		}
	}

	private Attribute parseLineNumbers(String s) {
		return null;
	}

	private LabeledInstructions parseMethodInstructions(Method method) throws ParsingException, GrammerException {
		Hashtable labelMap = new Hashtable();
		ArrayList toUpdate = new ArrayList();
		ArrayList codes = new ArrayList(), info;
		Attribute_Code.Opcode op = null;
		OpcodeInfo opinfo;
		String temp, retType, type, label = null;
		StringBuffer paras = new StringBuffer();
		int t = 0, i = 0, j = 0, high, low, npairs, counter, tokenType, offset = 0, codeLength = 0;

		byte[][] operands = null;
		boolean isWide = false, record = false;

		while (scanner.tokenType() != EOF && scanner.tokenType() != Attribute && scanner.tokenType() != Bracket_Right) {
			switch (scanner.tokenType()) {
			case JavaName:
				// label met
				record = true;
				label = scanner.token();

				if (scanner.nextToken() != Colon) {
					exception(scanner, "expecting.':'.after.label.name");
				}
				if (scanner.nextToken() != Instruction) {
					exception(scanner, "expecting.instruction.after.label");
				}
			case Instruction: {
				opinfo = OpcodeHelper.getOpcodeInfo(scanner.token());
				switch (opinfo.opcode) {
				case Constants.TABLESWITCH:
					// like default=line1, low=1, high=2, jump_table:line32,line34
					scanner.nextToken();
					info = new ArrayList();
					if (scanner.token().equals("default") == false) {
						exception(scanner, "'default'.expected.here");
					}
					if (scanner.nextToken() != Equal) {
						exception(scanner, "'='.expected.here.");
					}
					scanner.nextToken();
					info.add(scanner.token());
					if (scanner.nextToken() != Comma) {
						exception(scanner, "','.expected.here.");
					}
					scanner.nextToken();
					if (scanner.token().equals("low") == false) {
						exception(scanner, "'low'.expected.here.");
					}
					if (scanner.nextToken() != Equal) {
						exception(scanner, "'='.expected.here.");
					}
					if (scanner.nextToken() != Number_Integer) {
						exception(scanner, "number.expected.here.");
					}
					low = parseInteger(scanner.token());
					if (scanner.nextToken() != Comma) {
						exception(scanner, "','.expected.here.");
					}
					scanner.nextToken();
					if (scanner.token().equals("high") == false) {
						exception(scanner, "'high'.expected.here.");
					}
					if (scanner.nextToken() != Equal) {
						exception(scanner, "'='.expected.here.");
					}
					if (scanner.nextToken() != Number_Integer) {
						exception(scanner, "number.expected.here.");
					}
					high = parseInteger(scanner.token());
					if (scanner.nextToken() != Comma) {
						exception(scanner, "','.expected.here.");
					}
					scanner.nextToken();
					if (scanner.token().equals("jump_table") == false) {
						exception(scanner, "'jump_table'.expected.here.");
					}
					if (scanner.nextToken() != Colon) {
						exception(scanner, "':'.expected.here.");
					}
					scanner.nextToken();
					counter = 0;
					while (scanner.tokenType() != EOF) {
						if (scanner.tokenType() != JavaName) {
							exception(scanner, "label.name.expected.here");
						}
						info.add(scanner.token());
						if (scanner.nextToken() != Comma) {
							break;
						}
						scanner.nextToken();
					}
					operands = new byte[high - low + 5][];
					operands[0] = new byte[3 - offset % 4];
					for (i = 0; i < operands[0].length; i++) {
						operands[0][i] = (byte) 0;
					}

					operands[2] = Util.getBytes(low, 4);
					operands[3] = Util.getBytes(high, 4);
					op = new OpcodeWrapper(offset, opinfo.opcode, operands, info);
					toUpdate.add(op);
					codeLength = 1 + operands[0].length + operands.length * 4 - 4;
					info = null;
					break;
				case Constants.LOOKUPSWITCH:
					// like default=line58, npairs=3, jump_table:-1->line40,200->line46,2100->52					
					scanner.nextToken();
					info = new ArrayList();
					if (scanner.token().equals("default") == false) {
						exception(scanner, "'default'.expected.here");
					}
					if (scanner.nextToken() != Equal) {
						exception(scanner, "'='.expected.here.");
					}
					scanner.nextToken();
					info.add(scanner.token());
					if (scanner.nextToken() != Comma) {
						exception(scanner, "','.expected.here.");
					}
					scanner.nextToken();
					if (scanner.token().equals("npairs") == false) {
						exception(scanner, "'npairs'.expected.here.");
					}
					if (scanner.nextToken() != Equal) {
						exception(scanner, "'='.expected.here.");
					}
					if (scanner.nextToken() != Number_Integer) {
						exception(scanner, "number.expected.here.");
					}
					npairs = parseInteger(scanner.token());
					if (scanner.nextToken() != Comma) {
						exception(scanner, "','.expected.here.");
					}
					scanner.nextToken();
					if (scanner.token().equals("jump_table") == false) {
						exception(scanner, "'jump_table'.expected.here.");
					}
					if (scanner.nextToken() != Colon) {
						exception(scanner, "':'.expected.here.");
					}
					scanner.nextToken();

					operands = new byte[npairs * 2 + 3][];
					operands[0] = new byte[3 - offset % 4];

					for (i = 0; i < operands[0].length; i++) {
						operands[0][i] = (byte) 0;
					}
					operands[2] = Util.getBytes(npairs, 4);
					counter = 3;
					while (scanner.tokenType() != EOF) {
						if (scanner.tokenType() != Number_Integer) {
							exception(scanner, "number.expected.here");
						}
						operands[counter] = Util.getBytes(parseInteger(scanner.token()), 4);
						counter = counter + 2;
						if (scanner.nextToken() != Pointer) {
							exception(scanner, "->.expected.here");
						}
						scanner.nextToken();
						info.add(scanner.token());
						if (scanner.nextToken() != Comma) {
							break;
						}
						scanner.nextToken();
					}
					op = new OpcodeWrapper(offset, opinfo.opcode, operands, info);
					codeLength = 1 + operands[0].length + operands.length * 4 - 4;
					toUpdate.add(op);
					info = null;
					break;
				case Constants.GETFIELD:
				case Constants.GETSTATIC:
				case Constants.PUTFIELD:
				case Constants.PUTSTATIC:
					// like : getstatic java.io.PrintStream java.lang.System.out			
					scanner.nextToken();
					operands = new byte[1][];
					type = scanner.token();
					scanner.nextToken();
					temp = scanner.token();
					i = temp.lastIndexOf('.');
					i = cpl.addFieldref(temp.substring(i + 1), temp.substring(0, i), type);
					operands[0] = Util.getBytes(i, 2);
					codeLength = 3;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.INVOKESPECIAL:
				case Constants.INVOKESTATIC:
				case Constants.INVOKEVIRTUAL:
					// like invokespecial void java.lang.Object.<init>()			
					operands = new byte[1][];
					scanner.nextToken();
					retType = scanner.token();
					scanner.nextToken();
					temp = scanner.token();
					i = temp.lastIndexOf('.');
					if ((scanner.nextToken() == SBracket_Left) == false) {
						exception(scanner, "'('.expected.here");
					}
					if (scanner.nextToken() != SBracket_Right) {
						while (scanner.tokenType() != SBracket_Right && scanner.tokenType() != EOF) {
							paras.append(scanner.token());
							if (scanner.nextToken() == Comma) {
								paras.append(',');
								scanner.nextToken();
							}
						}
						if (scanner.tokenType() != SBracket_Right) {
							exception(scanner, "')'.expected.here");
						}
					} else {
						paras.append("");
					}
					operands[0] = Util.getBytes(cpl.addMethodref(temp.substring(i + 1), temp.substring(0, i), retType, paras.toString()), 2);
					paras.delete(0, paras.length());
					codeLength = 3;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.INVOKEINTERFACE:
					// like invokeinterface void jce.aa.bb(int,double) 4
					scanner.nextToken();
					operands = new byte[3][];
					retType = scanner.token();
					scanner.nextToken();
					temp = scanner.token();
					i = temp.lastIndexOf('.');
					if (scanner.nextToken() == SBracket_Left == false) {
						exception(scanner, "'('.expected.here");
					}
					if (scanner.nextToken() != SBracket_Right) {
						while (scanner.tokenType() != SBracket_Right && scanner.tokenType() != EOF) {
							paras.append(scanner.token());
							if (scanner.nextToken() == Comma) {
								paras.append(',');
								scanner.nextToken();
							}
						}
						if (scanner.tokenType() != SBracket_Right) {
							exception(scanner, "')'.expected.here");
						}
					} else {
						paras.append("");
					}

					if (scanner.nextToken() != Number_Integer) {
						exception(scanner, "number.expected.here");
					}
					t = parseInteger(scanner.token());
					operands[0] = Util.getBytes(cpl.addInterfaceMethodref(temp.substring(i + 1), temp.substring(0, i), retType, paras.toString()), 2);
					operands[1] = Util.getBytes(t, 1);
					operands[2] = Util.getBytes(0, 1); // this byte is aways 0
					codeLength = 5;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					paras.delete(0, paras.length());
					break;
				/*
				 * Operands are references to classes in constant pool
				 */
				case Constants.NEW:
				// like: new java.lang.Object
				case Constants.CHECKCAST:
				// like: checkcast java.lang.String
				case Constants.INSTANCEOF:
					// like: instanceof java.lang.String			
					scanner.nextToken();
					operands = new byte[1][];
					operands[0] = Util.getBytes(cpl.addClass(scanner.token()), 2);
					codeLength = 3;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.LDC:
					/*
					 *  like 4:ldc 1411111 or 4:ldc 1411111f or 4:ldc "abcde" 
					 *  or a special case ldc Infinity, this is to load a Infinity double value
					 */

					scanner.nextToken();
					operands = new byte[1][];
					temp = scanner.token();
					tokenType = scanner.tokenType();
					if (tokenType == String) {
						i = cpl.addString(Util.parseViewableString(temp.substring(1, temp.length() - 1)));
					} else if (tokenType == Number_Float || tokenType == Number_Float_Positive_Infinity || tokenType == Number_Float_Negativ_Infinity
							|| tokenType == Number_Float_NaN) {
						i = cpl.addFloat(parseFloat(temp));
					} else if (tokenType == Number_Integer) {
						i = cpl.addInteger(parseInteger(temp));
					} else {
						exception(scanner, "expecting.integer.or.string.or.float.here");
					}
					if (i < 255) {
						operands[0] = Util.getBytes(i, 1);
						codeLength = 2;
						op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					} else {
						// the index value for LDC is one byte, for those greater than one byte, have to use LDC_W
						operands[0] = Util.getBytes(i, 2);
						op = new Attribute_Code.Opcode(offset, Constants.LDC_W, operands);
						codeLength = 3;
					}
					scanner.nextToken();
					break;
				case Constants.LDC_W:
					// the same as LDC, except that it's index value are two bytes
					scanner.nextToken();
					operands = new byte[1][];
					temp = scanner.token();
					tokenType = scanner.tokenType();
					if (tokenType == String) {
						i = cpl.addString(Util.parseViewableString(temp.substring(1, temp.length() - 1)));
					} else if (tokenType == Number_Float || tokenType == Number_Float_NaN || tokenType == Number_Float_Negativ_Infinity
							|| tokenType == Number_Float_Positive_Infinity) {
						i = cpl.addFloat(parseFloat(temp));
					} else if (tokenType == Number_Integer) {
						i = cpl.addInteger(parseInteger(temp));
					} else {
						exception(scanner, "expecting.integer.or.string.or.float.here");
					}
					operands[0] = Util.getBytes(i, 2);
					codeLength = 3;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.LDC2_W:
					// like ldc2_w 14l, load a long/double value from constant pool
					scanner.nextToken();
					operands = new byte[1][];
					temp = scanner.token();
					tokenType = scanner.tokenType();
					if (tokenType == Number_Long) {
						i = cpl.addLong(parseLong(temp));
					} else if (tokenType == Number_Double || tokenType == Number_Double_NaN || tokenType == Number_Double_Negativ_Infinity
							|| tokenType == Number_Double_Positive_Infinity) {
						i = cpl.addDouble(parseDouble(temp));
					} else {
						exception(scanner, "expecting.long.or.double.here");
					}
					operands[0] = Util.getBytes(i, 2);
					codeLength = 3;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.NEWARRAY:
					// 5:newarray int
					scanner.nextToken();
					operands = new byte[1][1];
					operands[0][0] = Util.getPrimitiveTypeCode(scanner.token());
					codeLength = 2;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.ANEWARRAY:
					// 9:anewarray java.lang.String
					scanner.nextToken();
					operands = new byte[1][];
					operands[0] = Util.getBytes(cpl.addClass(scanner.token()), 2);
					codeLength = 3;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.MULTIANEWARRAY:
					// multianewarray int[][][] 3
					scanner.nextToken();
					operands = new byte[2][];
					type = scanner.token();
					if (scanner.nextToken() != Number_Integer) {
						exception(scanner, "dimesion.number.expected.here");
					}
					i = parseInteger(scanner.token());

					operands[1] = Util.getBytes(i, 1); // dimension					
					operands[0] = Util.getBytes(cpl.addClass(type), 2);
					codeLength = 4;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.WIDE:
					isWide = true;
					codeLength = 1;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
				case Constants.IINC:
					// like :iinc t(3) -1  or iinc 3 -1
					operands = new byte[2][];

					scanner.nextToken();
					if (scanner.tokenType() == Number_Integer) {
						i = parseInteger(scanner.token());
					} else {
						if (scanner.nextToken() != SBracket_Left) {
							exception(scanner, "'('.expected.here");
						}
						if (scanner.nextToken() != Number_Integer) {
							exception(scanner, "local.variable.index.expected.here");
						}
						i = parseInteger(scanner.token());
						if (scanner.nextToken() != SBracket_Right) {
							exception(scanner, "')'.expected.here");
						}
					}
					scanner.nextToken();
					if (scanner.tokenType() != Number_Integer) {
						exception(scanner, "increment.amount.expected.here");
					}
					j = parseInteger(scanner.token());
					if (isWide == true) {
						operands[0] = Util.getBytes(i, 2);
						operands[1] = Util.getBytes(j, 2);
						codeLength = 5;
					} else {
						operands[0] = Util.getBytes(i, 1);
						operands[1] = Util.getBytes(j, 1);
						codeLength = 3;
					}
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
					break;
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
					// like:istore a(8) or istore 8
					operands = new byte[1][];
					scanner.nextToken();
					if (scanner.tokenType() == Number_Integer) {
						i = parseInteger(scanner.token());
					} else {
						if (scanner.nextToken() != SBracket_Left) {
							exception(scanner, "'('.expected.here");
						}
						if (scanner.nextToken() != Number_Integer) {
							exception(scanner, "local.variable.index.expected.here");
						}
						i = parseInteger(scanner.token());
						if (scanner.nextToken() != SBracket_Right) {
							exception(scanner, "')'.expected.here");
						}
					}
					if (isWide == true) {
						operands[0] = Util.getBytes(i, 2);
						codeLength = 2;
						isWide = false;
					} else {
						operands[0] = Util.getBytes(i, 1);
						codeLength = 2;
					}
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
					scanner.nextToken();
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
					scanner.nextToken();
					operands = new byte[1][];
					codeLength = 3;
					op = new OpcodeWrapper(offset, opinfo.opcode, operands, scanner.token());
					toUpdate.add(op);
					scanner.nextToken();
					break;
				case Constants.BIPUSH:
				default:
					operands = new byte[opinfo.operandsCount][];
					for (i = 0; i < opinfo.operandsCount; i++) {
						if (scanner.nextToken() != Number_Integer) {
							exception(scanner, "number.expected.here");
						}
						operands[i] = Util.getBytes(parseInteger(scanner.token()), opinfo.operandsLength[i]);
						codeLength = codeLength + opinfo.operandsLength[i];
					}
					scanner.nextToken();
					codeLength++;
					op = new Attribute_Code.Opcode(offset, opinfo.opcode, operands);
				}
				break;
			}
			case Attribute:
				break;
			default:
				exception(scanner, "label.name.or.instructions.expected.here");
			}
			offset = offset + codeLength;
			codes.add(op);
			if (record) {
				labelMap.put(label, op);
				label = null;
			}
			record = false;
			operands = null;
			codeLength = 0;
		}
		updateLabelLinks(labelMap, toUpdate);
		return new LabeledInstructions((Attribute_Code.Opcode[]) codes.toArray(new Attribute_Code.Opcode[codes.size()]), labelMap, offset);
	}

	private void updateLabelLinks(Hashtable labels, ArrayList toUpdate) throws GrammerException {
		OpcodeWrapper op;
		ArrayList list;
		String label;
		int counter;
		byte[][] operands;
		for (int i = 0; i < toUpdate.size(); i++) {
			op = (OpcodeWrapper) toUpdate.get(i);
			operands = op.operands;
			switch (op.opcode) {
			case Constants.TABLESWITCH:
				list = (ArrayList) op.info;
				counter = operands.length;
				operands[1] = Util.getBytes(getOffset((String) list.get(0), labels, false) - op.offset, 4);// default value
				counter = 1;
				for (int j = 4; j < operands.length; j++) {
					operands[j] = Util.getBytes(getOffset((String) list.get(counter++), labels, false) - op.offset, 4);
				}
				break;
			case Constants.LOOKUPSWITCH:
				list = (ArrayList) op.info;
				counter = operands.length;
				operands[1] = Util.getBytes(getOffset((String) list.get(0), labels, false) - op.offset, 4);// default value
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

	/**
	 * parse method declaration, and the throws clause , if any.
	 * @param method
	 * @throws ParsingException
	 */
	private void parseMethodSignature(Method method, ArrayList attributes) throws ParsingException, GrammerException {
		int acc = 0;
		String methodName, retType;
		StringBuffer para = new StringBuffer(15);
		while (scanner.tokenType() == AccessFlag) {
			acc = acc | Util.getAccessFlag_Method(scanner.token());
			scanner.nextToken();
		}

		retType = scanner.token();
		scanner.nextToken();
		methodName = scanner.token();
		scanner.nextToken();
		if (scanner.tokenType() != SBracket_Left) {
			exception(scanner, "'('.expected.here");
		}
		scanner.nextToken();
		if (scanner.tokenType() == SBracket_Right) {
			//void paras
			para.append("");
		} else {
			while (scanner.tokenType() != EOF && scanner.tokenType() != SBracket_Right) {
				para = para.append(scanner.token());
				if (scanner.nextToken() == Comma) {
					para.append(',');
					scanner.nextToken();
				}
			}
			//validate the next token		
			if (scanner.tokenType() != SBracket_Right) {
				throw new ParsingException(scanner.getOffset(), "')'.expected.here");
			}
		}
		retType = Util.toInnerType(retType);
		method.descriptor_index = cpl.addUtf8("(" + Util.toInnerParameterTypes(para.toString()) + ")" + retType);
		method.name_index = cpl.addUtf8(methodName);
		method.access_flags = acc;
		scanner.nextToken();
		// throws clause, if any	 
		if ("throws".equals(scanner.token()) == true) {
			IntegerArray thr = new IntegerArray(4);
			while (scanner.tokenType() != Bracket_Left && scanner.tokenType() != EOF) {
				scanner.nextToken();
				thr.add(cpl.addClass(scanner.token()));
				scanner.nextToken();
				if (scanner.tokenType() != Bracket_Left && scanner.tokenType() != Comma) {
					exception(scanner, "invalid.throw.clause");
				}
			}
			Attribute att = new Attribute_Exceptions(2 + 2 * thr.getAll().length, thr.getAll().length, thr.getAll());
			att.attribute_name_index = cpl.addUtf8("Exceptions");
			attributes.add(att);
		} else if (scanner.tokenType() == Bracket_Left) {

		} else {
			exception(scanner, "'{'.expected.here");
		}
		scanner.nextToken();
	}

	/**
	 * like:jce.TestClass this  start=line0, end=line0, index=0
	 * @param s
	 * @param map
	 * @throws ParsingException
	 * @throws GrammerException
	 */
	private Attribute_LocalVariableTable parseLocalVariableTable(String s, Hashtable map) throws ParsingException, GrammerException {
		Scanner sc;
		sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1,
				scanner.getLineNumberStart());
		ArrayList lvts = new ArrayList();
		String type, name, index;
		int start, end;
		sc.nextToken();
		if (sc.nextToken() != Colon) {
			exception(sc, "':'.expected");
		}
		sc.nextToken();
		while (sc.tokenType() != EOF) {
			type = sc.token();
			sc.nextToken();
			name = sc.token();
			sc.nextToken();
			if ("start".equals(sc.token()) == false) {
				exception(sc, "'start'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			start = getOffset(sc.token(), map, false);
			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("end".equals(sc.token()) == false) {
				exception(sc, "'end'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			end = getOffset(sc.token(), map, true);
			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("index".equals(sc.token()) == false) {
				exception(sc, "'index'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			if (sc.nextToken() != Number_Integer) {
				exception(sc, "local.variable.index.expected.here");
			}
			index = sc.token();
			lvts.add(new Attribute_LocalVariableTable.LocalVariable(start, end - start, cpl.addUtf8(name), cpl.addUtf8(Util.toInnerType(type)),
					parseInteger(index)));
			sc.nextToken();
		}
		Attribute_LocalVariableTable.LocalVariable[] lvs = (Attribute_LocalVariableTable.LocalVariable[]) lvts
				.toArray(new Attribute_LocalVariableTable.LocalVariable[lvts.size()]);
		Attribute_LocalVariableTable lvt = new Attribute_LocalVariableTable(10 * lvs.length + 2, lvs.length, lvs);
		lvt.attribute_name_index = cpl.addUtf8("LocalVariableTable");
		return lvt;
	}

	/**
	 * like :
	 * [Exception Table:
	 * start=line73 , end=line78 , handler=line78 , catch_type=java.lang.Exception]
	 * @param s
	 * @param map
	 * @return
	 * @throws ParsingException
	 * @throws GrammerException
	 * TODO: error reporting missing labels
	 */
	private Attribute_Code.ExceptionTableItem[] parseExceptionTable(String s, Hashtable map) throws ParsingException, GrammerException {
		Scanner sc;
		sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1,
				scanner.getLineNumberStart());
		ArrayList excs = new ArrayList();
		int start, end, handler, catch_type;
		sc.nextToken();

		if (sc.nextToken() != Colon) {
			exception(sc, "':'.expected");
		}
		sc.nextToken();
		while (sc.tokenType() != EOF) {
			if ("start".equals(sc.token()) == false) {
				exception(sc, "'start'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(scanner, "'='.expected.here");
			}
			sc.nextToken();
			start = getOffset(sc.token(), map, false);
			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("end".equals(sc.token()) == false) {
				exception(sc, "'end'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			end = getOffset(sc.token(), map, false);
			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("handler".equals(sc.token()) == false) {
				exception(sc, "'handler'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			handler = getOffset(sc.token(), map, false);
			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("catch_type".equals(sc.token()) == false) {
				exception(sc, "'catch_type'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			if ("0".equals(sc.token())) {
				catch_type = 0;
			} else {
				catch_type = cpl.addClass(sc.token());
			}
			excs.add(new Attribute_Code.ExceptionTableItem(start, end, handler, catch_type));
			sc.nextToken();
		}
		return (Attribute_Code.ExceptionTableItem[]) excs.toArray(new Attribute_Code.ExceptionTableItem[excs.size()]);
	}

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

	private Attribute parseAttribute() throws GrammerException, ParsingException {
		String s = scanner.token();
		Attribute att;
		if (s.indexOf(Constants.ATTRIBUTE_NAME_DEPRECATED) != -1) {
			att = new Attribute_Deprecated();
			att.attribute_name_index = cpl.addUtf8("Deprecated");
			scanner.nextToken();
			return att;
		} else if (s.indexOf(Constants.ATTRIBUTE_NAME_SYNTHETIC) != -1) {
			att = new Attribute_Synthetic();
			att.attribute_name_index = cpl.addUtf8("Synthetic");
			scanner.nextToken();
			return att;
		} else if (s.indexOf(Constants.ATTRIBUTE_NAME_SOURCE_FILE) != -1) {
			att = new Attribute_SourceFile(2, cpl.addUtf8(s.substring(s.lastIndexOf(':') + 1, s.length() - 1).trim()));
			att.attribute_name_index = cpl.addUtf8("SourceFile");
			scanner.nextToken();
			return att;
		} else {
			exception(scanner, "can.not.process.attribute");
		}
		return null;

	}

	/**
	 * like :
	 * [Inner Classes :
	 * access = final class , name = 0 , fullname = jce.TestClass$1 , outername = 0]
	 * @param s
	 * @return
	 */
	private Attribute_InnerClasses parseInnerClasses() throws ParsingException, GrammerException {
		Scanner sc;//
		sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2, scanner.getColumnNumberStart() + 1,
				scanner.getLineNumberStart());
		ArrayList ins = new ArrayList();
		int access_flag = 0, inner_name_index, inner_class_info, outer_class_info;
		sc.nextToken();
		if (sc.nextToken() != Colon) {
			exception(sc, "':'.expected");
		}
		sc.nextToken();
		while (sc.tokenType() != EOF) {
			if ("access".equals(sc.token()) == false) {
				exception(sc, "'access'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}

			while (sc.nextToken() == AccessFlag) {
				access_flag = Util.getAccessFlag_Class(sc.token()) | access_flag;
			}
			if (sc.tokenType() != Comma) {
				exception(sc, "','.expected.here");
			}

			sc.nextToken();
			if ("name".equals(sc.token()) == false) {
				exception(sc, "'name'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();

			if ("0".equals(sc.token()) == true) {
				inner_name_index = 0;
			} else {
				inner_name_index = cpl.addUtf8(sc.token());
			}

			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("fullname".equals(sc.token()) == false) {
				exception(sc, "'fullname'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			inner_class_info = cpl.addClass(sc.token());
			if (sc.nextToken() != Comma) {
				exception(sc, "','.expected.here");
			}
			sc.nextToken();
			if ("outername".equals(sc.token()) == false) {
				exception(sc, "'outername'.expected.here");
			}
			if (sc.nextToken() != Equal) {
				exception(sc, "'='.expected.here");
			}
			sc.nextToken();
			if ("0".equals(sc.token())) {
				outer_class_info = 0;
			} else {
				outer_class_info = cpl.addClass(sc.token());
			}
			sc.nextToken();
			ins.add(new Attribute_InnerClasses.InnerClass(inner_class_info, outer_class_info, inner_name_index, access_flag));
		}

		Attribute_InnerClasses ret = new Attribute_InnerClasses(8 * ins.size() + 2, ins.size(), (Attribute_InnerClasses.InnerClass[]) ins
				.toArray(new Attribute_InnerClasses.InnerClass[ins.size()]));
		ret.attribute_name_index = cpl.addUtf8("InnerClasses");
		return ret;

	}

	private void parseClassAttributes() throws GrammerException, ParsingException {
		String s;
		ArrayList attributes = new ArrayList(4);
		int colonIndex , nameIndex;
		while (scanner.tokenType() == Attribute) {
			s = scanner.token();
			colonIndex = s.indexOf(':');
			nameIndex = s.indexOf(Constants.ATTRIBUTE_NAME_INNER_CLASSES);			
			if (nameIndex!=-1 && nameIndex<colonIndex) {
				// this is necessary, or  [SourceFile : Attribute_InnerClasses.java] will be parsed as innerclass
				attributes.add(parseInnerClasses());
				scanner.nextToken();
			} else {
				attributes.add(parseAttribute());
			}
		}
		javaClass.attributes = (Attribute[]) attributes.toArray(new Attribute[attributes.size()]);
		javaClass.attributes_count = attributes.size();
	}

	private void parseMaxStackOrLocals(Attribute_Code code) throws ParsingException {
		Scanner sc = Scanner.partialScanner(scanner.getContent(), scanner.getOffset() + 1, scanner.getLength() - 2,
				scanner.getColumnNumberStart() + 1, scanner.getLineNumberStart());
		sc.nextToken();
		if (sc.token().equals(Constants.ATTRIBUTE_NAME_MAX_STACK) == true) {
			if (sc.nextToken() != Colon) {
				exception(sc, "':'.expected.here");
			}
			if (sc.nextToken() != Number_Integer) {
				exception(sc, "invalid.max.stack.value");
			}
			code.max_stack = parseInteger(sc.token());

		} else if (sc.token().equals(Constants.ATTRIBUTE_NAME_MAX_LOCAL) == true) {
			if (sc.nextToken() != Colon) {
				exception(sc, "':'.expected.here");
			}
			if (sc.nextToken() != Number_Integer) {
				exception(sc, "invalid.max.local.value");
			}
			code.max_locals = parseInteger(sc.token());
		}
	}

	private void parseMajorOrMinor() throws GrammerException, ParsingException {
		String s;
		while (scanner.tokenType() == Attribute) {
			s = scanner.token();

			if (s.indexOf(Constants.ATTRIBUTE_NAME_MAJOR_VERSION) != -1) {
				try {
					javaClass.major_version = parseInteger(s.substring(s.indexOf(':') + 1, s.lastIndexOf(']')).trim());
				} catch (NumberFormatException ne) {
					exception(scanner, "invalid.major.version.definition");
				}
			} else if (s.indexOf(Constants.ATTRIBUTE_NAME_MINOR_VERSION) != -1) {
				try {
					javaClass.minor_version = parseInteger(s.substring(s.indexOf(':') + 1, s.lastIndexOf(']')).trim());
				} catch (NumberFormatException ne) {
					exception(scanner, "invalid.minor.version.definition");
				}
			} else {
				exception(scanner, "unexpected.attribute.here");
			}
			scanner.nextToken();
		}
	}

	private static int parseInteger(String s) {
		if (s.startsWith("0x") || s.startsWith("0X")) {
			return Integer.parseInt(s.substring(2), 16);
		} else {
			return Integer.parseInt(s);
		}
	}

	private static long parseLong(String s) {
		if (s.endsWith("l") || s.endsWith("L")) {
			s = s.substring(0, s.length() - 1);
		}
		if (s.startsWith("0x") || s.startsWith("0X")) {
			return Long.parseLong(s.substring(2), 16);
		} else {
			return Long.parseLong(s);
		}
	}

	private static float parseFloat(String s) {
		if (s.endsWith("f") || s.endsWith("F")) {
			s = s.substring(0, s.length() - 1);
		}
		return Float.parseFloat(s);
	}

	private static double parseDouble(String s) {
		if (s.endsWith("d") || s.endsWith("D")) {
			s = s.substring(0, s.length() - 1);
		}
		return Double.parseDouble(s);
	}

	private static void exception(Scanner sc, String msg) throws ParsingException {
		throw new GrammerException(sc.getOffset(), sc.getLineNumberStart(), sc.getColumnNumberStart(), msg);
	}

	private static class OpcodeWrapper extends Attribute_Code.Opcode {
		public Object info = null;

		public OpcodeWrapper(Attribute_Code.Opcode op) {
			super.offset = op.offset;
			super.opcode = op.opcode;
			super.operands = op.operands;
		}

		public OpcodeWrapper(int offset, byte opcode, byte[][] operands, Object info) {
			super(offset, opcode, operands);
			this.info = info;
		}
	}

	private class LabeledInstructions {
		Attribute_Code.Opcode[] codes;

		Hashtable labels;

		int codeLength;

		public LabeledInstructions(Attribute_Code.Opcode[] codes, Hashtable labels, int code_length) {
			this.codes = codes;
			this.labels = labels;
			this.codeLength = code_length;
		}
	}

	public static void main(String[] args) throws Exception {
		SourceCodeParser pa = new SourceCodeParser("e:\\work\\TestClass.jc");
		pa.parse();
	}
}
