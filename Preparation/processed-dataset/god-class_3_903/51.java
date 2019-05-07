private String PrintNoBreak(java.io.PrintWriter ostr, int byteNum, boolean[] dumped) {
    if (inNextOf != 1)
        throw new Error("JavaCC Bug: Please send mail to sankar@cs.stanford.edu");
    dumped[stateName] = true;
    if (byteNum >= 0) {
        if (asciiMoves[byteNum] != 0L) {
            ostr.println("               case " + stateName + ":");
            DumpAsciiMoveForCompositeState(ostr, byteNum, false);
            return "";
        }
    } else if (nonAsciiMethod != -1) {
        ostr.println("               case " + stateName + ":");
        DumpNonAsciiMoveForCompositeState(ostr);
        return "";
    }
    return ("               case " + stateName + ":\n");
}
