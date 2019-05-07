private static void generate3R(Expansion e, Phase3Data inf) {
    Expansion seq = e;
    if (e.internal_name.equals("")) {
        while (true) {
            if (seq instanceof Sequence && ((Sequence) seq).units.size() == 2) {
                seq = (Expansion) ((Sequence) seq).units.get(1);
            } else if (seq instanceof NonTerminal) {
                NonTerminal e_nrw = (NonTerminal) seq;
                NormalProduction ntprod = (NormalProduction) (production_table.get(e_nrw.getName()));
                if (ntprod instanceof JavaCodeProduction) {
                    break;
                } else {
                    seq = ntprod.getExpansion();
                }
            } else
                break;
        }
        if (seq instanceof RegularExpression) {
            e.internal_name = "jj_scan_token(" + ((RegularExpression) seq).ordinal + ")";
            return;
        }
        gensymindex++;
        //    if (gensymindex == 100) 
        //    { 
        //    new Error().printStackTrace(); 
        //    System.out.println(" ***** seq: " + seq.internal_name + "; size: " + ((Sequence)seq).units.size()); 
        //    } 
        e.internal_name = "R_" + gensymindex;
    }
    Phase3Data p3d = (Phase3Data) (phase3table.get(e));
    if (p3d == null || p3d.count < inf.count) {
        p3d = new Phase3Data(e, inf.count);
        phase3list.add(p3d);
        phase3table.put(e, p3d);
    }
}
