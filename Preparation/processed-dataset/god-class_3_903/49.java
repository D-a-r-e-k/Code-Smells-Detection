private static void DumpHeadForCase(java.io.PrintWriter ostr, int byteNum) {
    if (byteNum == 0)
        ostr.println("         long l = 1L << curChar;");
    else if (byteNum == 1)
        ostr.println("         long l = 1L << (curChar & 077);");
    else {
        if (Options.getJavaUnicodeEscape() || unicodeWarningGiven) {
            ostr.println("         int hiByte = (int)(curChar >> 8);");
            ostr.println("         int i1 = hiByte >> 6;");
            ostr.println("         long l1 = 1L << (hiByte & 077);");
        }
        ostr.println("         int i2 = (curChar & 0xff) >> 6;");
        ostr.println("         long l2 = 1L << (curChar & 077);");
    }
    //ostr.println("         MatchLoop: do"); 
    ostr.println("         do");
    ostr.println("         {");
    ostr.println("            switch(jjstateSet[--i])");
    ostr.println("            {");
}
