public static void DumpStateSets(java.io.PrintWriter ostr) {
    int cnt = 0;
    ostr.print("static final int[] jjnextStates = {");
    for (int i = 0; i < orderedStateSet.size(); i++) {
        int[] set = (int[]) orderedStateSet.get(i);
        for (int j = 0; j < set.length; j++) {
            if (cnt++ % 16 == 0)
                ostr.print("\n   ");
            ostr.print(set[j] + ", ");
        }
    }
    ostr.println("\n};");
}
