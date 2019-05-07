/*
   * Returns the minimum number of tokens that can parse to this expansion.
   */
static int minimumSize(Expansion e, int oldMin) {
    int retval = 0;
    // should never be used.  Will be bad if it is. 
    if (e.inMinimumSize) {
        // recursive search for minimum size unnecessary. 
        return Integer.MAX_VALUE;
    }
    e.inMinimumSize = true;
    if (e instanceof RegularExpression) {
        retval = 1;
    } else if (e instanceof NonTerminal) {
        NonTerminal e_nrw = (NonTerminal) e;
        NormalProduction ntprod = (NormalProduction) (production_table.get(e_nrw.getName()));
        if (ntprod instanceof JavaCodeProduction) {
            retval = Integer.MAX_VALUE;
        } else {
            Expansion ntexp = ntprod.getExpansion();
            retval = minimumSize(ntexp);
        }
    } else if (e instanceof Choice) {
        int min = oldMin;
        Expansion nested_e;
        Choice e_nrw = (Choice) e;
        for (int i = 0; min > 1 && i < e_nrw.getChoices().size(); i++) {
            nested_e = (Expansion) (e_nrw.getChoices().get(i));
            int min1 = minimumSize(nested_e, min);
            if (min > min1)
                min = min1;
        }
        retval = min;
    } else if (e instanceof Sequence) {
        int min = 0;
        Sequence e_nrw = (Sequence) e;
        // We skip the first element in the following iteration since it is the 
        // Lookahead object. 
        for (int i = 1; i < e_nrw.units.size(); i++) {
            Expansion eseq = (Expansion) (e_nrw.units.get(i));
            int mineseq = minimumSize(eseq);
            if (min == Integer.MAX_VALUE || mineseq == Integer.MAX_VALUE) {
                min = Integer.MAX_VALUE;
            } else {
                min += mineseq;
                if (min > oldMin)
                    break;
            }
        }
        retval = min;
    } else if (e instanceof TryBlock) {
        TryBlock e_nrw = (TryBlock) e;
        retval = minimumSize(e_nrw.exp);
    } else if (e instanceof OneOrMore) {
        OneOrMore e_nrw = (OneOrMore) e;
        retval = minimumSize(e_nrw.expansion);
    } else if (e instanceof ZeroOrMore) {
        retval = 0;
    } else if (e instanceof ZeroOrOne) {
        retval = 0;
    } else if (e instanceof Lookahead) {
        retval = 0;
    } else if (e instanceof Action) {
        retval = 0;
    }
    e.inMinimumSize = false;
    return retval;
}
