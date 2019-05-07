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
        switch(scanner.tokenType()) {
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
            case Instruction:
                {
                    opinfo = OpcodeHelper.getOpcodeInfo(scanner.token());
                    switch(opinfo.opcode) {
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
                            operands[2] = Util.getBytes(0, 1);
                            // this byte is aways 0  
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
                            } else if (tokenType == Number_Float || tokenType == Number_Float_Positive_Infinity || tokenType == Number_Float_Negativ_Infinity || tokenType == Number_Float_NaN) {
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
                            } else if (tokenType == Number_Float || tokenType == Number_Float_NaN || tokenType == Number_Float_Negativ_Infinity || tokenType == Number_Float_Positive_Infinity) {
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
                            } else if (tokenType == Number_Double || tokenType == Number_Double_NaN || tokenType == Number_Double_Negativ_Infinity || tokenType == Number_Double_Positive_Infinity) {
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
                            operands[1] = Util.getBytes(i, 1);
                            // dimension					  
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
