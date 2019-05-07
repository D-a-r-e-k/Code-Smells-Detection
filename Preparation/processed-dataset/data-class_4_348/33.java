// ========== Direct SAX Dispatch, for optimization purposes ========  
/**
   * Returns whether the specified <var>ch</var> conforms to the XML 1.0 definition
   * of whitespace.  Refer to <A href="http://www.w3.org/TR/1998/REC-xml-19980210#NT-S">
   * the definition of <CODE>S</CODE></A> for details.
   * @param   ch      Character to check as XML whitespace.
   * @return          =true if <var>ch</var> is XML whitespace; otherwise =false.
   */
private static boolean isSpace(char ch) {
    return XMLCharacterRecognizer.isWhiteSpace(ch);
}
