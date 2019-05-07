static void build(java.io.PrintWriter ps) {
    NormalProduction p;
    JavaCodeProduction jp;
    Token t = null;
    ostr = ps;
    for (java.util.Iterator prodIterator = bnfproductions.iterator(); prodIterator.hasNext(); ) {
        p = (NormalProduction) prodIterator.next();
        if (p instanceof JavaCodeProduction) {
            jp = (JavaCodeProduction) p;
            t = (Token) (jp.getReturnTypeTokens().get(0));
            printTokenSetup(t);
            ccol = 1;
            printLeadingComments(t, ostr);
            ostr.print("  " + staticOpt() + (p.getAccessMod() != null ? p.getAccessMod() + " " : ""));
            cline = t.beginLine;
            ccol = t.beginColumn;
            printTokenOnly(t, ostr);
            for (int i = 1; i < jp.getReturnTypeTokens().size(); i++) {
                t = (Token) (jp.getReturnTypeTokens().get(i));
                printToken(t, ostr);
            }
            printTrailingComments(t, ostr);
            ostr.print(" " + jp.getLhs() + "(");
            if (jp.getParameterListTokens().size() != 0) {
                printTokenSetup((Token) (jp.getParameterListTokens().get(0)));
                for (java.util.Iterator it = jp.getParameterListTokens().iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    printToken(t, ostr);
                }
                printTrailingComments(t, ostr);
            }
            ostr.print(") throws ParseException");
            for (java.util.Iterator it = jp.getThrowsList().iterator(); it.hasNext(); ) {
                ostr.print(", ");
                java.util.List name = (java.util.List) it.next();
                for (java.util.Iterator it2 = name.iterator(); it2.hasNext(); ) {
                    t = (Token) it2.next();
                    ostr.print(t.image);
                }
            }
            ostr.print(" {");
            if (Options.getDebugParser()) {
                ostr.println("");
                ostr.println("    trace_call(\"" + jp.getLhs() + "\");");
                ostr.print("    try {");
            }
            if (jp.getCodeTokens().size() != 0) {
                printTokenSetup((Token) (jp.getCodeTokens().get(0)));
                cline--;
                printTokenList(jp.getCodeTokens(), ostr);
            }
            ostr.println("");
            if (Options.getDebugParser()) {
                ostr.println("    } finally {");
                ostr.println("      trace_return(\"" + jp.getLhs() + "\");");
                ostr.println("    }");
            }
            ostr.println("  }");
            ostr.println("");
        } else {
            buildPhase1Routine((BNFProduction) p);
        }
    }
    for (int phase2index = 0; phase2index < phase2list.size(); phase2index++) {
        buildPhase2Routine((Lookahead) (phase2list.get(phase2index)));
    }
    int phase3index = 0;
    while (phase3index < phase3list.size()) {
        for (; phase3index < phase3list.size(); phase3index++) {
            setupPhase3Builds((Phase3Data) (phase3list.get(phase3index)));
        }
    }
    for (java.util.Enumeration enumeration = phase3table.elements(); enumeration.hasMoreElements(); ) {
        buildPhase3Routine((Phase3Data) (enumeration.nextElement()), false);
    }
}
