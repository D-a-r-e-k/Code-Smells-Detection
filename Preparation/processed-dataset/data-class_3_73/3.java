private static void dumpLookaheads(Lookahead[] conds, String[] actions) {
    for (int i = 0; i < conds.length; i++) {
        System.err.println("Lookahead: " + i);
        System.err.println(conds[i].dump(0, new HashSet()));
        System.err.println();
    }
}
