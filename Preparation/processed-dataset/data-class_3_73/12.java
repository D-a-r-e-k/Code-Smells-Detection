static void setupPhase3Builds(Phase3Data inf) {
    Expansion e = inf.exp;
    if (e instanceof RegularExpression) {
        ;
    } else if (e instanceof NonTerminal) {
        // All expansions of non-terminals have the "name" fields set.  So 
        // there's no need to check it below for "e_nrw" and "ntexp".  In 
        // fact, we rely here on the fact that the "name" fields of both these 
        // variables are the same. 
        NonTerminal e_nrw = (NonTerminal) e;
        NormalProduction ntprod = (NormalProduction) (production_table.get(e_nrw.getName()));
        if (ntprod instanceof JavaCodeProduction) {
            ;
        } else {
            generate3R(ntprod.getExpansion(), inf);
        }
    } else if (e instanceof Choice) {
        Choice e_nrw = (Choice) e;
        for (int i = 0; i < e_nrw.getChoices().size(); i++) {
            generate3R((Expansion) (e_nrw.getChoices().get(i)), inf);
        }
    } else if (e instanceof Sequence) {
        Sequence e_nrw = (Sequence) e;
        // We skip the first element in the following iteration since it is the 
        // Lookahead object. 
        int cnt = inf.count;
        for (int i = 1; i < e_nrw.units.size(); i++) {
            Expansion eseq = (Expansion) (e_nrw.units.get(i));
            setupPhase3Builds(new Phase3Data(eseq, cnt));
            cnt -= minimumSize(eseq);
            if (cnt <= 0)
                break;
        }
    } else if (e instanceof TryBlock) {
        TryBlock e_nrw = (TryBlock) e;
        setupPhase3Builds(new Phase3Data(e_nrw.exp, inf.count));
    } else if (e instanceof OneOrMore) {
        OneOrMore e_nrw = (OneOrMore) e;
        generate3R(e_nrw.expansion, inf);
    } else if (e instanceof ZeroOrMore) {
        ZeroOrMore e_nrw = (ZeroOrMore) e;
        generate3R(e_nrw.expansion, inf);
    } else if (e instanceof ZeroOrOne) {
        ZeroOrOne e_nrw = (ZeroOrOne) e;
        generate3R(e_nrw.expansion, inf);
    }
}
