public static void DumpStatesForState(java.io.PrintWriter ostr) {
    ostr.print("protected static final int[][][] statesForState = ");
    if (statesForState == null) {
        ostr.println("null;");
        return;
    } else
        ostr.println("{");
    for (int i = 0; i < statesForState.length; i++) {
        if (statesForState[i] == null) {
            ostr.println(" null,");
            continue;
        }
        ostr.println(" {");
        for (int j = 0; j < statesForState[i].length; j++) {
            int[] stateSet = statesForState[i][j];
            if (stateSet == null) {
                ostr.println("   { " + j + " },");
                continue;
            }
            ostr.print("   { ");
            for (int k = 0; k < stateSet.length; k++) ostr.print(stateSet[k] + ", ");
            ostr.println("},");
        }
        ostr.println(" },");
    }
    ostr.println("\n};");
}
