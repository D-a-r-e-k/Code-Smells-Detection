public static void DumpStatesForKind(java.io.PrintWriter ostr) {
    DumpStatesForState(ostr);
    boolean moreThanOne = false;
    int cnt = 0;
    ostr.print("protected static final int[][] kindForState = ");
    if (kinds == null) {
        ostr.println("null;");
        return;
    } else
        ostr.println("{");
    for (int i = 0; i < kinds.length; i++) {
        if (moreThanOne)
            ostr.println(",");
        moreThanOne = true;
        if (kinds[i] == null)
            ostr.println("null");
        else {
            cnt = 0;
            ostr.print("{ ");
            for (int j = 0; j < kinds[i].length; j++) {
                if (cnt++ > 0)
                    ostr.print(",");
                if (cnt % 15 == 0)
                    ostr.print("\n  ");
                else if (cnt > 1)
                    ostr.print(" ");
                ostr.print(kinds[i][j]);
            }
            ostr.print("}");
        }
    }
    ostr.println("\n};");
}
