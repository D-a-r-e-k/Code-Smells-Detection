void DumpNonAsciiMoveMethod(java.io.PrintWriter ostr) {
    int j;
    ostr.println("private static final boolean jjCanMove_" + nonAsciiMethod + "(int hiByte, int i1, int i2, long l1, long l2)");
    ostr.println("{");
    ostr.println("   switch(hiByte)");
    ostr.println("   {");
    if (loByteVec != null && loByteVec.size() > 0) {
        for (j = 0; j < loByteVec.size(); j += 2) {
            ostr.println("      case " + ((Integer) loByteVec.get(j)).intValue() + ":");
            if (!AllBitsSet((String) allBitVectors.get(((Integer) loByteVec.get(j + 1)).intValue()))) {
                ostr.println("         return ((jjbitVec" + ((Integer) loByteVec.get(j + 1)).intValue() + "[i2" + "] & l2) != 0L);");
            } else
                ostr.println("            return true;");
        }
    }
    ostr.println("      default :");
    if (nonAsciiMoveIndices != null && (j = nonAsciiMoveIndices.length) > 0) {
        do {
            if (!AllBitsSet((String) allBitVectors.get(nonAsciiMoveIndices[j - 2])))
                ostr.println("         if ((jjbitVec" + nonAsciiMoveIndices[j - 2] + "[i1] & l1) != 0L)");
            if (!AllBitsSet((String) allBitVectors.get(nonAsciiMoveIndices[j - 1]))) {
                ostr.println("            if ((jjbitVec" + nonAsciiMoveIndices[j - 1] + "[i2] & l2) == 0L)");
                ostr.println("               return false;");
                ostr.println("            else");
            }
            ostr.println("            return true;");
        } while ((j -= 2) > 0);
    }
    ostr.println("         return false;");
    ostr.println("   }");
    ostr.println("}");
}
