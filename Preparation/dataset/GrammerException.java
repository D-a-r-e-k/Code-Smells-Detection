package com.jasml.compiler;

public class GrammerException extends ParsingException {

	public GrammerException(int offset, String msg) {
		super(offset, msg);
	}

	public GrammerException(int offset, int line, int column, String msg) {
		super(offset, line, column, msg);
	}

	public GrammerException(String msg, Exception e) {
		super(msg, e);
	}

	public GrammerException(int line, int column, String msg) {
		super(line, column, msg);
	}
}
