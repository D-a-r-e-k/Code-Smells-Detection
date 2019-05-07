private static void DumpAsciiMoves(java.io.PrintWriter ostr, int byteNum) {
    boolean[] dumped = new boolean[Math.max(generatedStates, dummyStateIndex + 1)];
    Enumeration e = compositeStateTable.keys();
    DumpHeadForCase(ostr, byteNum);
    while (e.hasMoreElements()) DumpCompositeStatesAsciiMoves(ostr, (String) e.nextElement(), byteNum, dumped);
    for (int i = 0; i < allStates.size(); i++) {
        NfaState temp = (NfaState) allStates.get(i);
        if (dumped[temp.stateName] || temp.lexState != LexGen.lexStateIndex || !temp.HasTransitions() || temp.dummy || temp.stateName == -1)
            continue;
        String toPrint = "";
        if (temp.stateForCase != null) {
            if (temp.inNextOf == 1)
                continue;
            if (dumped[temp.stateForCase.stateName])
                continue;
            toPrint = (temp.stateForCase.PrintNoBreak(ostr, byteNum, dumped));
            if (temp.asciiMoves[byteNum] == 0L) {
                if (toPrint.equals(""))
                    ostr.println("                  break;");
                continue;
            }
        }
        if (temp.asciiMoves[byteNum] == 0L)
            continue;
        if (!toPrint.equals(""))
            ostr.print(toPrint);
        dumped[temp.stateName] = true;
        ostr.println("               case " + temp.stateName + ":");
        temp.DumpAsciiMove(ostr, byteNum, dumped);
    }
    ostr.println("               default : break;");
    ostr.println("            }");
    ostr.println("         } while(i != startsAt);");
}
