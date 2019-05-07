/**
   * The phase 1 routines generates their output into String's and dumps
   * these String's once for each method.  These String's contain the
   * special characters '' to indicate a positive indent, and ''
   * to indicate a negative indent.  '\n' is used to indicate a line terminator.
   * The characters '' and '' are used to delineate portions of
   * text where '\n's should not be followed by an indentation.
   */
/**
   * Returns true if there is a JAVACODE production that the argument expansion
   * may directly expand to (without consuming tokens or encountering lookahead).
   */
private static boolean javaCodeCheck(Expansion exp) {
    if (exp instanceof RegularExpression) {
        return false;
    } else if (exp instanceof NonTerminal) {
        NormalProduction prod = ((NonTerminal) exp).getProd();
        if (prod instanceof JavaCodeProduction) {
            return true;
        } else {
            return javaCodeCheck(prod.getExpansion());
        }
    } else if (exp instanceof Choice) {
        Choice ch = (Choice) exp;
        for (int i = 0; i < ch.getChoices().size(); i++) {
            if (javaCodeCheck((Expansion) (ch.getChoices().get(i)))) {
                return true;
            }
        }
        return false;
    } else if (exp instanceof Sequence) {
        Sequence seq = (Sequence) exp;
        for (int i = 0; i < seq.units.size(); i++) {
            Expansion[] units = (Expansion[]) seq.units.toArray(new Expansion[seq.units.size()]);
            if (units[i] instanceof Lookahead && ((Lookahead) units[i]).isExplicit()) {
                // An explicit lookahead (rather than one generated implicitly). Assume 
                // the user knows what he / she is doing, e.g. 
                //    "A" ( "B" | LOOKAHEAD("X") jcode() | "C" )* "D" 
                return false;
            } else if (javaCodeCheck((units[i]))) {
                return true;
            } else if (!Semanticize.emptyExpansionExists(units[i])) {
                return false;
            }
        }
        return false;
    } else if (exp instanceof OneOrMore) {
        OneOrMore om = (OneOrMore) exp;
        return javaCodeCheck(om.expansion);
    } else if (exp instanceof ZeroOrMore) {
        ZeroOrMore zm = (ZeroOrMore) exp;
        return javaCodeCheck(zm.expansion);
    } else if (exp instanceof ZeroOrOne) {
        ZeroOrOne zo = (ZeroOrOne) exp;
        return javaCodeCheck(zo.expansion);
    } else if (exp instanceof TryBlock) {
        TryBlock tb = (TryBlock) exp;
        return javaCodeCheck(tb.exp);
    } else {
        return false;
    }
}
