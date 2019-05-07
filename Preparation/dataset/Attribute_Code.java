/*
 * Author jyang Created on 2006-4-3 19:19:48
 */
package com.jasml.classes;

public class Attribute_Code extends Attribute {

	public int max_stack;

	public int max_locals;

	public int code_length;

	public Opcode[] codes;

	public int exception_table_length;

	public ExceptionTableItem[] exception_table;

	public int attributes_count;

	public Attribute[] attributes;

	public Attribute_Code() {
		super(Constants.ATTRIBUTE_Code, 0);
	}

	public Attribute_Code(int attrLength, int max_stack, int max_locals, int code_length, Opcode[] codes, int exception_table_length,
			ExceptionTableItem[] exception_Table, int attributes_count, Attribute[] attributes) {
		super(Constants.ATTRIBUTE_Code, attrLength);
		this.max_locals = max_locals;
		this.max_stack = max_stack;
		this.code_length = code_length;
		this.codes = codes;
		this.exception_table_length = exception_table_length;
		this.exception_table = exception_Table;
		this.attributes_count = attributes_count;
		this.attributes = attributes;
	}

	public static class ExceptionTableItem {
		public int start_pc;

		public int end_pc;

		public int handler_pc;

		public int catch_type;

		public ExceptionTableItem(int start_pc, int end_pc, int handler_pc, int catch_type) {
			this.start_pc = start_pc;
			this.end_pc = end_pc;
			this.handler_pc = handler_pc;
			this.catch_type = catch_type;
		}
	}

	public static class Opcode {
		public byte opcode;

		public byte[][] operands;

		public int offset;

		/*
		 * parameter operands store the operands used by the opcode.
		 * the value of first dimension of the operands array decides the number operands,
		 * the second dimesion the respective operands.       
		 */
		public Opcode(int offset, byte opcode, byte[][] operands) {
			this.opcode = opcode;
			this.operands = operands;
			this.offset = offset;
		}

		public Opcode() {

		}
	}

}