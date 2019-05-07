private final void DumpNonAsciiMoveForCompositeState(java.io.PrintWriter ostr) {
    boolean nextIntersects = selfLoop();
    for (int j = 0; j < allStates.size(); j++) {
        NfaState temp1 = (NfaState) allStates.get(j);
        if (this == temp1 || temp1.stateName == -1 || temp1.dummy || stateName == temp1.stateName || (temp1.nonAsciiMethod == -1))
            continue;
        if (!nextIntersects && Intersect(temp1.next.epsilonMovesString, next.epsilonMovesString)) {
            nextIntersects = true;
            break;
        }
    }
    if (!Options.getJavaUnicodeEscape() && !unicodeWarningGiven) {
        if (loByteVec != null && loByteVec.size() > 1)
            ostr.println("                  if ((jjbitVec" + ((Integer) loByteVec.get(1)).intValue() + "[i2" + "] & l2) != 0L)");
    } else {
        ostr.println("                  if (jjCanMove_" + nonAsciiMethod + "(hiByte, i1, i2, l1, l2))");
    }
    if (kindToPrint != Integer.MAX_VALUE) {
        ostr.println("                  {");
        ostr.println("                     if (kind > " + kindToPrint + ")");
        ostr.println("                        kind = " + kindToPrint + ";");
    }
    if (next != null && next.usefulEpsilonMoves > 0) {
        int[] stateNames = (int[]) allNextStates.get(next.epsilonMovesString);
        if (next.usefulEpsilonMoves == 1) {
            int name = stateNames[0];
            if (nextIntersects)
                ostr.println("                     jjCheckNAdd(" + name + ");");
            else
                ostr.println("                     jjstateSet[jjnewStateCnt++] = " + name + ";");
        } else if (next.usefulEpsilonMoves == 2 && nextIntersects) {
            ostr.println("                     jjCheckNAddTwoStates(" + stateNames[0] + ", " + stateNames[1] + ");");
        } else {
            int[] indices = GetStateSetIndicesForUse(next.epsilonMovesString);
            boolean notTwo = (indices[0] + 1 != indices[1]);
            if (nextIntersects) {
                ostr.print("                     jjCheckNAddStates(" + indices[0]);
                if (notTwo) {
                    jjCheckNAddStatesDualNeeded = true;
                    ostr.print(", " + indices[1]);
                } else {
                    jjCheckNAddStatesUnaryNeeded = true;
                }
                ostr.println(");");
            } else
                ostr.println("                     jjAddStates(" + indices[0] + ", " + indices[1] + ");");
        }
    }
    if (kindToPrint != Integer.MAX_VALUE)
        ostr.println("                  }");
}
