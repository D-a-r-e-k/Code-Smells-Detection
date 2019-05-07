static String phase1ExpansionGen(Expansion e) {
    String retval = "";
    Token t = null;
    Lookahead[] conds;
    String[] actions;
    if (e instanceof RegularExpression) {
        RegularExpression e_nrw = (RegularExpression) e;
        retval += "\n";
        if (e_nrw.lhsTokens.size() != 0) {
            printTokenSetup((Token) (e_nrw.lhsTokens.get(0)));
            for (java.util.Iterator it = e_nrw.lhsTokens.iterator(); it.hasNext(); ) {
                t = (Token) it.next();
                retval += printToken(t);
            }
            retval += printTrailingComments(t);
            retval += " = ";
        }
        String tail = e_nrw.rhsToken == null ? ");" : ")." + e_nrw.rhsToken.image + ";";
        if (e_nrw.label.equals("")) {
            Object label = names_of_tokens.get(new Integer(e_nrw.ordinal));
            if (label != null) {
                retval += "jj_consume_token(" + (String) label + tail;
            } else {
                retval += "jj_consume_token(" + e_nrw.ordinal + tail;
            }
        } else {
            retval += "jj_consume_token(" + e_nrw.label + tail;
        }
    } else if (e instanceof NonTerminal) {
        NonTerminal e_nrw = (NonTerminal) e;
        retval += "\n";
        if (e_nrw.getLhsTokens().size() != 0) {
            printTokenSetup((Token) (e_nrw.getLhsTokens().get(0)));
            for (java.util.Iterator it = e_nrw.getLhsTokens().iterator(); it.hasNext(); ) {
                t = (Token) it.next();
                retval += printToken(t);
            }
            retval += printTrailingComments(t);
            retval += " = ";
        }
        retval += e_nrw.getName() + "(";
        if (e_nrw.getArgumentTokens().size() != 0) {
            printTokenSetup((Token) (e_nrw.getArgumentTokens().get(0)));
            for (java.util.Iterator it = e_nrw.getArgumentTokens().iterator(); it.hasNext(); ) {
                t = (Token) it.next();
                retval += printToken(t);
            }
            retval += printTrailingComments(t);
        }
        retval += ");";
    } else if (e instanceof Action) {
        Action e_nrw = (Action) e;
        retval += "\n";
        if (e_nrw.getActionTokens().size() != 0) {
            printTokenSetup((Token) (e_nrw.getActionTokens().get(0)));
            ccol = 1;
            for (Iterator it = e_nrw.getActionTokens().iterator(); it.hasNext(); ) {
                t = (Token) it.next();
                retval += printToken(t);
            }
            retval += printTrailingComments(t);
        }
        retval += "";
    } else if (e instanceof Choice) {
        Choice e_nrw = (Choice) e;
        conds = new Lookahead[e_nrw.getChoices().size()];
        actions = new String[e_nrw.getChoices().size() + 1];
        actions[e_nrw.getChoices().size()] = "\n" + "jj_consume_token(-1);\n" + "throw new ParseException();";
        // In previous line, the "throw" never throws an exception since the 
        // evaluation of jj_consume_token(-1) causes ParseException to be 
        // thrown first. 
        Sequence nestedSeq;
        for (int i = 0; i < e_nrw.getChoices().size(); i++) {
            nestedSeq = (Sequence) (e_nrw.getChoices().get(i));
            actions[i] = phase1ExpansionGen(nestedSeq);
            conds[i] = (Lookahead) (nestedSeq.units.get(0));
        }
        retval = buildLookaheadChecker(conds, actions);
    } else if (e instanceof Sequence) {
        Sequence e_nrw = (Sequence) e;
        // We skip the first element in the following iteration since it is the 
        // Lookahead object. 
        for (int i = 1; i < e_nrw.units.size(); i++) {
            retval += phase1ExpansionGen((Expansion) (e_nrw.units.get(i)));
        }
    } else if (e instanceof OneOrMore) {
        OneOrMore e_nrw = (OneOrMore) e;
        Expansion nested_e = e_nrw.expansion;
        Lookahead la;
        if (nested_e instanceof Sequence) {
            la = (Lookahead) (((Sequence) nested_e).units.get(0));
        } else {
            la = new Lookahead();
            la.setAmount(Options.getLookahead());
            la.setLaExpansion(nested_e);
        }
        retval += "\n";
        int labelIndex = ++gensymindex;
        retval += "label_" + labelIndex + ":\n";
        retval += "while (true) {";
        retval += phase1ExpansionGen(nested_e);
        conds = new Lookahead[1];
        conds[0] = la;
        actions = new String[2];
        actions[0] = "\n;";
        actions[1] = "\nbreak label_" + labelIndex + ";";
        retval += buildLookaheadChecker(conds, actions);
        retval += "\n" + "}";
    } else if (e instanceof ZeroOrMore) {
        ZeroOrMore e_nrw = (ZeroOrMore) e;
        Expansion nested_e = e_nrw.expansion;
        Lookahead la;
        if (nested_e instanceof Sequence) {
            la = (Lookahead) (((Sequence) nested_e).units.get(0));
        } else {
            la = new Lookahead();
            la.setAmount(Options.getLookahead());
            la.setLaExpansion(nested_e);
        }
        retval += "\n";
        int labelIndex = ++gensymindex;
        retval += "label_" + labelIndex + ":\n";
        retval += "while (true) {";
        conds = new Lookahead[1];
        conds[0] = la;
        actions = new String[2];
        actions[0] = "\n;";
        actions[1] = "\nbreak label_" + labelIndex + ";";
        retval += buildLookaheadChecker(conds, actions);
        retval += phase1ExpansionGen(nested_e);
        retval += "\n" + "}";
    } else if (e instanceof ZeroOrOne) {
        ZeroOrOne e_nrw = (ZeroOrOne) e;
        Expansion nested_e = e_nrw.expansion;
        Lookahead la;
        if (nested_e instanceof Sequence) {
            la = (Lookahead) (((Sequence) nested_e).units.get(0));
        } else {
            la = new Lookahead();
            la.setAmount(Options.getLookahead());
            la.setLaExpansion(nested_e);
        }
        conds = new Lookahead[1];
        conds[0] = la;
        actions = new String[2];
        actions[0] = phase1ExpansionGen(nested_e);
        actions[1] = "\n;";
        retval += buildLookaheadChecker(conds, actions);
    } else if (e instanceof TryBlock) {
        TryBlock e_nrw = (TryBlock) e;
        Expansion nested_e = e_nrw.exp;
        java.util.List list;
        retval += "\n";
        retval += "try {";
        retval += phase1ExpansionGen(nested_e);
        retval += "\n" + "}";
        for (int i = 0; i < e_nrw.catchblks.size(); i++) {
            retval += " catch (";
            list = (java.util.List) (e_nrw.types.get(i));
            if (list.size() != 0) {
                printTokenSetup((Token) (list.get(0)));
                for (java.util.Iterator it = list.iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    retval += printToken(t);
                }
                retval += printTrailingComments(t);
            }
            retval += " ";
            t = (Token) (e_nrw.ids.get(i));
            printTokenSetup(t);
            retval += printToken(t);
            retval += printTrailingComments(t);
            retval += ") {\n";
            list = (java.util.List) (e_nrw.catchblks.get(i));
            if (list.size() != 0) {
                printTokenSetup((Token) (list.get(0)));
                ccol = 1;
                for (java.util.Iterator it = list.iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    retval += printToken(t);
                }
                retval += printTrailingComments(t);
            }
            retval += "\n" + "}";
        }
        if (e_nrw.finallyblk != null) {
            retval += " finally {\n";
            if (e_nrw.finallyblk.size() != 0) {
                printTokenSetup((Token) (e_nrw.finallyblk.get(0)));
                ccol = 1;
                for (java.util.Iterator it = e_nrw.finallyblk.iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    retval += printToken(t);
                }
                retval += printTrailingComments(t);
            }
            retval += "\n" + "}";
        }
    }
    return retval;
}
