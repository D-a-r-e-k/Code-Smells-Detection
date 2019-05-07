private void DumpAsciiMove(java.io.PrintWriter ostr, int byteNum, boolean dumped[]) {
    boolean nextIntersects = selfLoop() && isComposite;
    boolean onlyState = true;
    for (int j = 0; j < allStates.size(); j++) {
        NfaState temp1 = (NfaState) allStates.get(j);
        if (this == temp1 || temp1.stateName == -1 || temp1.dummy || stateName == temp1.stateName || temp1.asciiMoves[byteNum] == 0L)
            continue;
        if (onlyState && (asciiMoves[byteNum] & temp1.asciiMoves[byteNum]) != 0L)
            onlyState = false;
        if (!nextIntersects && Intersect(temp1.next.epsilonMovesString, next.epsilonMovesString))
            nextIntersects = true;
        if (!dumped[temp1.stateName] && !temp1.isComposite && asciiMoves[byteNum] == temp1.asciiMoves[byteNum] && kindToPrint == temp1.kindToPrint && (next.epsilonMovesString == temp1.next.epsilonMovesString || (next.epsilonMovesString != null && temp1.next.epsilonMovesString != null && next.epsilonMovesString.equals(temp1.next.epsilonMovesString)))) {
            dumped[temp1.stateName] = true;
            ostr.println("               case " + temp1.stateName + ":");
        }
    }
    //if (onlyState) 
    //nextIntersects = false; 
    int oneBit = OnlyOneBitSet(asciiMoves[byteNum]);
    if (asciiMoves[byteNum] != 0xffffffffffffffffL) {
        if ((next == null || next.usefulEpsilonMoves == 0) && kindToPrint != Integer.MAX_VALUE) {
            String kindCheck = "";
            if (!onlyState)
                kindCheck = " && kind > " + kindToPrint;
            if (oneBit != -1)
                ostr.println("                  if (curChar == " + (64 * byteNum + oneBit) + kindCheck + ")");
            else
                ostr.println("                  if ((0x" + Long.toHexString(asciiMoves[byteNum]) + "L & l) != 0L" + kindCheck + ")");
            ostr.println("                     kind = " + kindToPrint + ";");
            if (onlyState)
                ostr.println("                  break;");
            else
                ostr.println("                  break;");
            return;
        }
    }
    String prefix = "";
    if (kindToPrint != Integer.MAX_VALUE) {
        if (oneBit != -1) {
            ostr.println("                  if (curChar != " + (64 * byteNum + oneBit) + ")");
            ostr.println("                     break;");
        } else if (asciiMoves[byteNum] != 0xffffffffffffffffL) {
            ostr.println("                  if ((0x" + Long.toHexString(asciiMoves[byteNum]) + "L & l) == 0L)");
            ostr.println("                     break;");
        }
        if (onlyState) {
            ostr.println("                  kind = " + kindToPrint + ";");
        } else {
            ostr.println("                  if (kind > " + kindToPrint + ")");
            ostr.println("                     kind = " + kindToPrint + ";");
        }
    } else {
        if (oneBit != -1) {
            ostr.println("                  if (curChar == " + (64 * byteNum + oneBit) + ")");
            prefix = "   ";
        } else if (asciiMoves[byteNum] != 0xffffffffffffffffL) {
            ostr.println("                  if ((0x" + Long.toHexString(asciiMoves[byteNum]) + "L & l) != 0L)");
            prefix = "   ";
        }
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
    if (onlyState)
        ostr.println("                  break;");
    else
        ostr.println("                  break;");
}
