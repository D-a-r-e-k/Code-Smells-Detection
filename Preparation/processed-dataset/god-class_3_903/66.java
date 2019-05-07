public static void DumpMoveNfa(java.io.PrintWriter ostr) {
    //if (!boilerPlateDumped) 
    //   PrintBoilerPlate(ostr); 
    //boilerPlateDumped = true; 
    int i;
    int[] kindsForStates = null;
    if (kinds == null) {
        kinds = new int[LexGen.maxLexStates][];
        statesForState = new int[LexGen.maxLexStates][][];
    }
    ReArrange();
    for (i = 0; i < allStates.size(); i++) {
        NfaState temp = (NfaState) allStates.get(i);
        if (temp.lexState != LexGen.lexStateIndex || !temp.HasTransitions() || temp.dummy || temp.stateName == -1)
            continue;
        if (kindsForStates == null) {
            kindsForStates = new int[generatedStates];
            statesForState[LexGen.lexStateIndex] = new int[Math.max(generatedStates, dummyStateIndex + 1)][];
        }
        kindsForStates[temp.stateName] = temp.lookingFor;
        statesForState[LexGen.lexStateIndex][temp.stateName] = temp.compositeStates;
        temp.GenerateNonAsciiMoves(ostr);
    }
    Enumeration e = stateNameForComposite.keys();
    while (e.hasMoreElements()) {
        String s = (String) e.nextElement();
        int state = ((Integer) stateNameForComposite.get(s)).intValue();
        if (state >= generatedStates)
            statesForState[LexGen.lexStateIndex][state] = (int[]) allNextStates.get(s);
    }
    if (stateSetsToFix.size() != 0)
        FixStateSets();
    kinds[LexGen.lexStateIndex] = kindsForStates;
    ostr.println((Options.getStatic() ? "static " : "") + "private int " + "jjMoveNfa" + LexGen.lexStateSuffix + "(int startState, int curPos)");
    ostr.println("{");
    if (generatedStates == 0) {
        ostr.println("   return curPos;");
        ostr.println("}");
        return;
    }
    if (LexGen.mixed[LexGen.lexStateIndex]) {
        ostr.println("   int strKind = jjmatchedKind;");
        ostr.println("   int strPos = jjmatchedPos;");
        ostr.println("   int seenUpto;");
        ostr.println("   input_stream.backup(seenUpto = curPos + 1);");
        ostr.println("   try { curChar = input_stream.readChar(); }");
        ostr.println("   catch(java.io.IOException e) { throw new Error(\"Internal Error\"); }");
        ostr.println("   curPos = 0;");
    }
    ostr.println("   int startsAt = 0;");
    ostr.println("   jjnewStateCnt = " + generatedStates + ";");
    ostr.println("   int i = 1;");
    ostr.println("   jjstateSet[0] = startState;");
    if (Options.getDebugTokenManager())
        ostr.println("      debugStream.println(\"   Starting NFA to match one of : \" + " + "jjKindsForStateVector(curLexState, jjstateSet, 0, 1));");
    if (Options.getDebugTokenManager())
        ostr.println("      debugStream.println(" + (LexGen.maxLexStates > 1 ? "\"<\" + lexStateNames[curLexState] + \">\" + " : "") + "\"Current character : \" + " + "TokenMgrError.addEscapes(String.valueOf(curChar)) + \" (\" + (int)curChar + \") " + "at line \" + input_stream.getEndLine() + \" column \" + input_stream.getEndColumn());");
    ostr.println("   int kind = 0x" + Integer.toHexString(Integer.MAX_VALUE) + ";");
    ostr.println("   for (;;)");
    ostr.println("   {");
    ostr.println("      if (++jjround == 0x" + Integer.toHexString(Integer.MAX_VALUE) + ")");
    ostr.println("         ReInitRounds();");
    ostr.println("      if (curChar < 64)");
    ostr.println("      {");
    DumpAsciiMoves(ostr, 0);
    ostr.println("      }");
    ostr.println("      else if (curChar < 128)");
    ostr.println("      {");
    DumpAsciiMoves(ostr, 1);
    ostr.println("      }");
    ostr.println("      else");
    ostr.println("      {");
    DumpCharAndRangeMoves(ostr);
    ostr.println("      }");
    ostr.println("      if (kind != 0x" + Integer.toHexString(Integer.MAX_VALUE) + ")");
    ostr.println("      {");
    ostr.println("         jjmatchedKind = kind;");
    ostr.println("         jjmatchedPos = curPos;");
    ostr.println("         kind = 0x" + Integer.toHexString(Integer.MAX_VALUE) + ";");
    ostr.println("      }");
    ostr.println("      ++curPos;");
    if (Options.getDebugTokenManager()) {
        ostr.println("      if (jjmatchedKind != 0 && jjmatchedKind != 0x" + Integer.toHexString(Integer.MAX_VALUE) + ")");
        ostr.println("         debugStream.println(" + "\"   Currently matched the first \" + (jjmatchedPos + 1) + \" characters as" + " a \" + tokenImage[jjmatchedKind] + \" token.\");");
    }
    ostr.println("      if ((i = jjnewStateCnt) == (startsAt = " + generatedStates + " - (jjnewStateCnt = startsAt)))");
    if (LexGen.mixed[LexGen.lexStateIndex])
        ostr.println("         break;");
    else
        ostr.println("         return curPos;");
    if (Options.getDebugTokenManager())
        ostr.println("      debugStream.println(\"   Possible kinds of longer matches : \" + " + "jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));");
    ostr.println("      try { curChar = input_stream.readChar(); }");
    if (LexGen.mixed[LexGen.lexStateIndex])
        ostr.println("      catch(java.io.IOException e) { break; }");
    else
        ostr.println("      catch(java.io.IOException e) { return curPos; }");
    if (Options.getDebugTokenManager())
        ostr.println("      debugStream.println(" + (LexGen.maxLexStates > 1 ? "\"<\" + lexStateNames[curLexState] + \">\" + " : "") + "\"Current character : \" + " + "TokenMgrError.addEscapes(String.valueOf(curChar)) + \" (\" + (int)curChar + \") " + "at line \" + input_stream.getEndLine() + \" column \" + input_stream.getEndColumn());");
    ostr.println("   }");
    if (LexGen.mixed[LexGen.lexStateIndex]) {
        ostr.println("   if (jjmatchedPos > strPos)");
        ostr.println("      return curPos;");
        ostr.println("");
        ostr.println("   int toRet = Math.max(curPos, seenUpto);");
        ostr.println("");
        ostr.println("   if (curPos < toRet)");
        ostr.println("      for (i = toRet - Math.min(curPos, seenUpto); i-- > 0; )");
        ostr.println("         try { curChar = input_stream.readChar(); }");
        ostr.println("         catch(java.io.IOException e) { " + "throw new Error(\"Internal Error : Please send a bug report.\"); }");
        ostr.println("");
        ostr.println("   if (jjmatchedPos < strPos)");
        ostr.println("   {");
        ostr.println("      jjmatchedKind = strKind;");
        ostr.println("      jjmatchedPos = strPos;");
        ostr.println("   }");
        ostr.println("   else if (jjmatchedPos == strPos && jjmatchedKind > strKind)");
        ostr.println("      jjmatchedKind = strKind;");
        ostr.println("");
        ostr.println("   return toRet;");
    }
    ostr.println("}");
    allStates.clear();
}
