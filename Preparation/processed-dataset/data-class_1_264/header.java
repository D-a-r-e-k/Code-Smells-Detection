void method0() { 
public String tagName;
public StringBuffer lastStart;
public StringBuffer lastEnd;
public StringBuffer lastKeyword;
public String lastSetName;
public String lastEscape;
public ParserRuleSet lastDelegateSet;
public String lastNoWordSep = "_";
public ParserRuleSet rules;
public byte lastDefaultID = Token.NULL;
public byte lastTokenID;
public byte lastMatchType;
public int termChar = -1;
public boolean lastNoLineBreak;
public boolean lastNoWordBreak;
public boolean lastIgnoreCase = true;
public boolean lastHighlightDigits;
public boolean lastAtLineStart;
public boolean lastAtWhitespaceEnd;
public boolean lastAtWordStart;
public int lastStartPosMatch;
public int lastEndPosMatch;
public String lastDigitRE;
public String lastHashChar;
public String lastHashChars;
}
