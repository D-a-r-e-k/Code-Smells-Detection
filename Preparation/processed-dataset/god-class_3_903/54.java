private void DumpAsciiMoveForCompositeState(java.io.PrintWriter ostr, int byteNum, boolean elseNeeded) {
    boolean nextIntersects = selfLoop();
    for (int j = 0; j < allStates.size(); j++) {
        NfaState temp1 = (NfaState) allStates.get(j);
        if (this == temp1 || temp1.stateName == -1 || temp1.dummy || stateName == temp1.stateName || temp1.asciiMoves[byteNum] == 0L)
            continue;
        if (!nextIntersects && Intersect(temp1.next.epsilonMovesString, next.epsilonMovesString)) {
            nextIntersects = true;
            break;
        }
    }
    //System.out.println(stateName + " \'s nextIntersects : " + nextIntersects); 
    String prefix = "";
    if (asciiMoves[byteNum] != 0xffffffffffffffffL) {
        int oneBit = OnlyOneBitSet(asciiMoves[byteNum]);
        if (oneBit != -1)
            ostr.println("                  " + (elseNeeded ? "else " : "") + "if (curChar == " + (64 * byteNum + oneBit) + ")");
        else
            ostr.println("                  " + (elseNeeded ? "else " : "") + "if ((0x" + Long.toHexString(asciiMoves[byteNum]) + "L & l) != 0L)");
        prefix = "   ";
    }
    if (kindToPrint != Integer.MAX_VALUE) {
        if (asciiMoves[byteNum] != 0xffffffffffffffffL) {
            ostr.println("                  {");
        }
        ostr.println(prefix + "                  if (kind > " + kindToPrint + ")");
        ostr.println(prefix + "                     kind = " + kindToPrint + ";");
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
    if (asciiMoves[byteNum] != 0xffffffffffffffffL && kindToPrint != Integer.MAX_VALUE)
        ostr.println("                  }");
}
