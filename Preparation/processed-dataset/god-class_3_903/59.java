private final void DumpNonAsciiMove(java.io.PrintWriter ostr, boolean dumped[]) {
    boolean nextIntersects = selfLoop() && isComposite;
    for (int j = 0; j < allStates.size(); j++) {
        NfaState temp1 = (NfaState) allStates.get(j);
        if (this == temp1 || temp1.stateName == -1 || temp1.dummy || stateName == temp1.stateName || (temp1.nonAsciiMethod == -1))
            continue;
        if (!nextIntersects && Intersect(temp1.next.epsilonMovesString, next.epsilonMovesString))
            nextIntersects = true;
        if (!dumped[temp1.stateName] && !temp1.isComposite && nonAsciiMethod == temp1.nonAsciiMethod && kindToPrint == temp1.kindToPrint && (next.epsilonMovesString == temp1.next.epsilonMovesString || (next.epsilonMovesString != null && temp1.next.epsilonMovesString != null && next.epsilonMovesString.equals(temp1.next.epsilonMovesString)))) {
            dumped[temp1.stateName] = true;
            ostr.println("               case " + temp1.stateName + ":");
        }
    }
    if (next == null || next.usefulEpsilonMoves <= 0) {
        String kindCheck = " && kind > " + kindToPrint;
        if (!Options.getJavaUnicodeEscape() && !unicodeWarningGiven) {
            if (loByteVec != null && loByteVec.size() > 1)
                ostr.println("                  if ((jjbitVec" + ((Integer) loByteVec.get(1)).intValue() + "[i2" + "] & l2) != 0L" + kindCheck + ")");
        } else {
            ostr.println("                  if (jjCanMove_" + nonAsciiMethod + "(hiByte, i1, i2, l1, l2)" + kindCheck + ")");
        }
        ostr.println("                     kind = " + kindToPrint + ";");
        ostr.println("                  break;");
        return;
    }
    String prefix = "   ";
    if (kindToPrint != Integer.MAX_VALUE) {
        if (!Options.getJavaUnicodeEscape() && !unicodeWarningGiven) {
            if (loByteVec != null && loByteVec.size() > 1) {
                ostr.println("                  if ((jjbitVec" + ((Integer) loByteVec.get(1)).intValue() + "[i2" + "] & l2) == 0L)");
                ostr.println("                     break;");
            }
        } else {
            ostr.println("                  if (!jjCanMove_" + nonAsciiMethod + "(hiByte, i1, i2, l1, l2))");
            ostr.println("                     break;");
        }
        ostr.println("                  if (kind > " + kindToPrint + ")");
        ostr.println("                     kind = " + kindToPrint + ";");
        prefix = "";
    } else if (!Options.getJavaUnicodeEscape() && !unicodeWarningGiven) {
        if (loByteVec != null && loByteVec.size() > 1)
            ostr.println("                  if ((jjbitVec" + ((Integer) loByteVec.get(1)).intValue() + "[i2" + "] & l2) != 0L)");
    } else {
        ostr.println("                  if (jjCanMove_" + nonAsciiMethod + "(hiByte, i1, i2, l1, l2))");
    }
    if (next != null && next.usefulEpsilonMoves > 0) {
        int[] stateNames = (int[]) allNextStates.get(next.epsilonMovesString);
        if (next.usefulEpsilonMoves == 1) {
            int name = stateNames[0];
            if (nextIntersects)
                ostr.println(prefix + "                  jjCheckNAdd(" + name + ");");
            else
                ostr.println(prefix + "                  jjstateSet[jjnewStateCnt++] = " + name + ";");
        } else if (next.usefulEpsilonMoves == 2 && nextIntersects) {
            ostr.println(prefix + "                  jjCheckNAddTwoStates(" + stateNames[0] + ", " + stateNames[1] + ");");
        } else {
            int[] indices = GetStateSetIndicesForUse(next.epsilonMovesString);
            boolean notTwo = (indices[0] + 1 != indices[1]);
            if (nextIntersects) {
                ostr.print(prefix + "                  jjCheckNAddStates(" + indices[0]);
                if (notTwo) {
                    jjCheckNAddStatesDualNeeded = true;
                    ostr.print(", " + indices[1]);
                } else {
                    jjCheckNAddStatesUnaryNeeded = true;
                }
                ostr.println(");");
            } else
                ostr.println(prefix + "                  jjAddStates(" + indices[0] + ", " + indices[1] + ");");
        }
    }
    ostr.println("                  break;");
}
