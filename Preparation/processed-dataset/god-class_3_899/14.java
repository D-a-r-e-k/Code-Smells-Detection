static void buildPhase3Routine(Phase3Data inf, boolean recursive_call) {
    Expansion e = inf.exp;
    Token t = null;
    if (e.internal_name.startsWith("jj_scan_token"))
        return;
    if (!recursive_call) {
        ostr.println("  " + staticOpt() + "private boolean jj_3" + e.internal_name + "() {");
        xsp_declared = false;
        if (Options.getDebugLookahead() && e.parent instanceof NormalProduction) {
            ostr.print("    ");
            if (Options.getErrorReporting()) {
                ostr.print("if (!jj_rescan) ");
            }
            ostr.println("trace_call(\"" + ((NormalProduction) e.parent).getLhs() + "(LOOKING AHEAD...)\");");
            jj3_expansion = e;
        } else {
            jj3_expansion = null;
        }
    }
    if (e instanceof RegularExpression) {
        RegularExpression e_nrw = (RegularExpression) e;
        if (e_nrw.label.equals("")) {
            Object label = names_of_tokens.get(new Integer(e_nrw.ordinal));
            if (label != null) {
                ostr.println("    if (jj_scan_token(" + (String) label + ")) " + genReturn(true));
            } else {
                ostr.println("    if (jj_scan_token(" + e_nrw.ordinal + ")) " + genReturn(true));
            }
        } else {
            ostr.println("    if (jj_scan_token(" + e_nrw.label + ")) " + genReturn(true));
        }
    } else if (e instanceof NonTerminal) {
        // All expansions of non-terminals have the "name" fields set.  So 
        // there's no need to check it below for "e_nrw" and "ntexp".  In 
        // fact, we rely here on the fact that the "name" fields of both these 
        // variables are the same. 
        NonTerminal e_nrw = (NonTerminal) e;
        NormalProduction ntprod = (NormalProduction) (production_table.get(e_nrw.getName()));
        if (ntprod instanceof JavaCodeProduction) {
            ostr.println("    if (true) { jj_la = 0; jj_scanpos = jj_lastpos; " + genReturn(false) + "}");
        } else {
            Expansion ntexp = ntprod.getExpansion();
            //ostr.println("    if (jj_3" + ntexp.internal_name + "()) " + genReturn(true)); 
            ostr.println("    if (" + genjj_3Call(ntexp) + ") " + genReturn(true));
        }
    } else if (e instanceof Choice) {
        Sequence nested_seq;
        Choice e_nrw = (Choice) e;
        if (e_nrw.getChoices().size() != 1) {
            if (!xsp_declared) {
                xsp_declared = true;
                ostr.println("    Token xsp;");
            }
            ostr.println("    xsp = jj_scanpos;");
        }
        for (int i = 0; i < e_nrw.getChoices().size(); i++) {
            nested_seq = (Sequence) (e_nrw.getChoices().get(i));
            Lookahead la = (Lookahead) (nested_seq.units.get(0));
            if (la.getActionTokens().size() != 0) {
                // We have semantic lookahead that must be evaluated. 
                lookaheadNeeded = true;
                ostr.println("    jj_lookingAhead = true;");
                ostr.print("    jj_semLA = ");
                printTokenSetup((Token) (la.getActionTokens().get(0)));
                for (Iterator it = la.getActionTokens().iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    printToken(t, ostr);
                }
                printTrailingComments(t, ostr);
                ostr.println(";");
                ostr.println("    jj_lookingAhead = false;");
            }
            ostr.print("    if (");
            if (la.getActionTokens().size() != 0) {
                ostr.print("!jj_semLA || ");
            }
            if (i != e_nrw.getChoices().size() - 1) {
                //ostr.println("jj_3" + nested_seq.internal_name + "()) {"); 
                ostr.println(genjj_3Call(nested_seq) + ") {");
                ostr.println("    jj_scanpos = xsp;");
            } else {
                //ostr.println("jj_3" + nested_seq.internal_name + "()) " + genReturn(true)); 
                ostr.println(genjj_3Call(nested_seq) + ") " + genReturn(true));
            }
        }
        for (int i = 1; i < e_nrw.getChoices().size(); i++) {
            //ostr.println("    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) " + genReturn(false)); 
            ostr.println("    }");
        }
    } else if (e instanceof Sequence) {
        Sequence e_nrw = (Sequence) e;
        // We skip the first element in the following iteration since it is the 
        // Lookahead object. 
        int cnt = inf.count;
        for (int i = 1; i < e_nrw.units.size(); i++) {
            Expansion eseq = (Expansion) (e_nrw.units.get(i));
            buildPhase3Routine(new Phase3Data(eseq, cnt), true);
            //      System.out.println("minimumSize: line: " + eseq.line + ", column: " + eseq.column + ": " + 
            //      minimumSize(eseq));//Test Code 
            cnt -= minimumSize(eseq);
            if (cnt <= 0)
                break;
        }
    } else if (e instanceof TryBlock) {
        TryBlock e_nrw = (TryBlock) e;
        buildPhase3Routine(new Phase3Data(e_nrw.exp, inf.count), true);
    } else if (e instanceof OneOrMore) {
        if (!xsp_declared) {
            xsp_declared = true;
            ostr.println("    Token xsp;");
        }
        OneOrMore e_nrw = (OneOrMore) e;
        Expansion nested_e = e_nrw.expansion;
        //ostr.println("    if (jj_3" + nested_e.internal_name + "()) " + genReturn(true)); 
        ostr.println("    if (" + genjj_3Call(nested_e) + ") " + genReturn(true));
        //ostr.println("    if (jj_la == 0 && jj_scanpos == jj_lastpos) " + genReturn(false)); 
        ostr.println("    while (true) {");
        ostr.println("      xsp = jj_scanpos;");
        //ostr.println("      if (jj_3" + nested_e.internal_name + "()) { jj_scanpos = xsp; break; }"); 
        ostr.println("      if (" + genjj_3Call(nested_e) + ") { jj_scanpos = xsp; break; }");
        //ostr.println("      if (jj_la == 0 && jj_scanpos == jj_lastpos) " + genReturn(false)); 
        ostr.println("    }");
    } else if (e instanceof ZeroOrMore) {
        if (!xsp_declared) {
            xsp_declared = true;
            ostr.println("    Token xsp;");
        }
        ZeroOrMore e_nrw = (ZeroOrMore) e;
        Expansion nested_e = e_nrw.expansion;
        ostr.println("    while (true) {");
        ostr.println("      xsp = jj_scanpos;");
        //ostr.println("      if (jj_3" + nested_e.internal_name + "()) { jj_scanpos = xsp; break; }"); 
        ostr.println("      if (" + genjj_3Call(nested_e) + ") { jj_scanpos = xsp; break; }");
        //ostr.println("      if (jj_la == 0 && jj_scanpos == jj_lastpos) " + genReturn(false)); 
        ostr.println("    }");
    } else if (e instanceof ZeroOrOne) {
        if (!xsp_declared) {
            xsp_declared = true;
            ostr.println("    Token xsp;");
        }
        ZeroOrOne e_nrw = (ZeroOrOne) e;
        Expansion nested_e = e_nrw.expansion;
        ostr.println("    xsp = jj_scanpos;");
        //ostr.println("    if (jj_3" + nested_e.internal_name + "()) jj_scanpos = xsp;"); 
        ostr.println("    if (" + genjj_3Call(nested_e) + ") jj_scanpos = xsp;");
    }
    if (!recursive_call) {
        ostr.println("    " + genReturn(false));
        ostr.println("  }");
        ostr.println("");
    }
}
