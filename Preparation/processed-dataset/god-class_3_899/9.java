static void buildPhase2Routine(Lookahead la) {
    Expansion e = la.getLaExpansion();
    ostr.println("  " + staticOpt() + "private boolean jj_2" + e.internal_name + "(int xla) {");
    ostr.println("    jj_la = xla; jj_lastpos = jj_scanpos = token;");
    ostr.println("    try { return !jj_3" + e.internal_name + "(); }");
    ostr.println("    catch(LookaheadSuccess ls) { return true; }");
    if (Options.getErrorReporting())
        ostr.println("    finally { jj_save(" + (Integer.parseInt(e.internal_name.substring(1)) - 1) + ", xla); }");
    ostr.println("  }");
    ostr.println("");
    Phase3Data p3d = new Phase3Data(e, la.getAmount());
    phase3list.add(p3d);
    phase3table.put(e, p3d);
}
