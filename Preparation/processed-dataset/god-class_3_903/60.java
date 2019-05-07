public static void DumpCharAndRangeMoves(java.io.PrintWriter ostr) {
    boolean[] dumped = new boolean[Math.max(generatedStates, dummyStateIndex + 1)];
    Enumeration e = compositeStateTable.keys();
    int i;
    DumpHeadForCase(ostr, -1);
    while (e.hasMoreElements()) DumpCompositeStatesNonAsciiMoves(ostr, (String) e.nextElement(), dumped);
    for (i = 0; i < allStates.size(); i++) {
        NfaState temp = (NfaState) allStates.get(i);
        if (temp.stateName == -1 || dumped[temp.stateName] || temp.lexState != LexGen.lexStateIndex || !temp.HasTransitions() || temp.dummy)
            continue;
        String toPrint = "";
        if (temp.stateForCase != null) {
            if (temp.inNextOf == 1)
                continue;
            if (dumped[temp.stateForCase.stateName])
                continue;
            toPrint = (temp.stateForCase.PrintNoBreak(ostr, -1, dumped));
            if (temp.nonAsciiMethod == -1) {
                if (toPrint.equals(""))
                    ostr.println("                  break;");
                continue;
            }
        }
        if (temp.nonAsciiMethod == -1)
            continue;
        if (!toPrint.equals(""))
            ostr.print(toPrint);
        dumped[temp.stateName] = true;
        //System.out.println("case : " + temp.stateName); 
        ostr.println("               case " + temp.stateName + ":");
        temp.DumpNonAsciiMove(ostr, dumped);
    }
    ostr.println("               default : break;");
    ostr.println("            }");
    ostr.println("         } while(i != startsAt);");
}
