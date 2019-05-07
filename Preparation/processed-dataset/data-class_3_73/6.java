static void buildPhase1Routine(BNFProduction p) {
    Token t;
    t = (Token) (p.getReturnTypeTokens().get(0));
    boolean voidReturn = false;
    if (t.kind == JavaCCParserConstants.VOID) {
        voidReturn = true;
    }
    printTokenSetup(t);
    ccol = 1;
    printLeadingComments(t, ostr);
    ostr.print("  " + staticOpt() + "final " + (p.getAccessMod() != null ? p.getAccessMod() : "public") + " ");
    cline = t.beginLine;
    ccol = t.beginColumn;
    printTokenOnly(t, ostr);
    for (int i = 1; i < p.getReturnTypeTokens().size(); i++) {
        t = (Token) (p.getReturnTypeTokens().get(i));
        printToken(t, ostr);
    }
    printTrailingComments(t, ostr);
    ostr.print(" " + p.getLhs() + "(");
    if (p.getParameterListTokens().size() != 0) {
        printTokenSetup((Token) (p.getParameterListTokens().get(0)));
        for (java.util.Iterator it = p.getParameterListTokens().iterator(); it.hasNext(); ) {
            t = (Token) it.next();
            printToken(t, ostr);
        }
        printTrailingComments(t, ostr);
    }
    ostr.print(") throws ParseException");
    for (java.util.Iterator it = p.getThrowsList().iterator(); it.hasNext(); ) {
        ostr.print(", ");
        java.util.List name = (java.util.List) it.next();
        for (java.util.Iterator it2 = name.iterator(); it2.hasNext(); ) {
            t = (Token) it2.next();
            ostr.print(t.image);
        }
    }
    ostr.print(" {");
    indentamt = 4;
    if (Options.getDebugParser()) {
        ostr.println("");
        ostr.println("    trace_call(\"" + p.getLhs() + "\");");
        ostr.print("    try {");
        indentamt = 6;
    }
    if (p.getDeclarationTokens().size() != 0) {
        printTokenSetup((Token) (p.getDeclarationTokens().get(0)));
        cline--;
        for (Iterator it = p.getDeclarationTokens().iterator(); it.hasNext(); ) {
            t = (Token) it.next();
            printToken(t, ostr);
        }
        printTrailingComments(t, ostr);
    }
    String code = phase1ExpansionGen(p.getExpansion());
    dumpFormattedString(code);
    ostr.println("");
    if (p.isJumpPatched() && !voidReturn) {
        ostr.println("    throw new Error(\"Missing return statement in function\");");
    }
    if (Options.getDebugParser()) {
        ostr.println("    } finally {");
        ostr.println("      trace_return(\"" + p.getLhs() + "\");");
        ostr.println("    }");
    }
    ostr.println("  }");
    ostr.println("");
}
