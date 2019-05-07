/**
   * Sets up the array "firstSet" above based on the Expansion argument
   * passed to it.  Since this is a recursive function, it assumes that
   * "firstSet" has been reset before the first call.
   */
private static void genFirstSet(Expansion exp) {
    if (exp instanceof RegularExpression) {
        firstSet[((RegularExpression) exp).ordinal] = true;
    } else if (exp instanceof NonTerminal) {
        if (!(((NonTerminal) exp).getProd() instanceof JavaCodeProduction)) {
            genFirstSet(((BNFProduction) (((NonTerminal) exp).getProd())).getExpansion());
        }
    } else if (exp instanceof Choice) {
        Choice ch = (Choice) exp;
        for (int i = 0; i < ch.getChoices().size(); i++) {
            genFirstSet((Expansion) (ch.getChoices().get(i)));
        }
    } else if (exp instanceof Sequence) {
        Sequence seq = (Sequence) exp;
        Object obj = seq.units.get(0);
        if ((obj instanceof Lookahead) && (((Lookahead) obj).getActionTokens().size() != 0)) {
            jj2LA = true;
        }
        for (int i = 0; i < seq.units.size(); i++) {
            Expansion unit = (Expansion) seq.units.get(i);
            // Javacode productions can not have FIRST sets. Instead we generate the FIRST set 
            // for the preceding LOOKAHEAD (the semantic checks should have made sure that 
            // the LOOKAHEAD is suitable). 
            if (unit instanceof NonTerminal && ((NonTerminal) unit).getProd() instanceof JavaCodeProduction) {
                if (i > 0 && seq.units.get(i - 1) instanceof Lookahead) {
                    Lookahead la = (Lookahead) seq.units.get(i - 1);
                    genFirstSet(la.getLaExpansion());
                }
            } else {
                genFirstSet((Expansion) (seq.units.get(i)));
            }
            if (!Semanticize.emptyExpansionExists((Expansion) (seq.units.get(i)))) {
                break;
            }
        }
    } else if (exp instanceof OneOrMore) {
        OneOrMore om = (OneOrMore) exp;
        genFirstSet(om.expansion);
    } else if (exp instanceof ZeroOrMore) {
        ZeroOrMore zm = (ZeroOrMore) exp;
        genFirstSet(zm.expansion);
    } else if (exp instanceof ZeroOrOne) {
        ZeroOrOne zo = (ZeroOrOne) exp;
        genFirstSet(zo.expansion);
    } else if (exp instanceof TryBlock) {
        TryBlock tb = (TryBlock) exp;
        genFirstSet(tb.exp);
    }
}
