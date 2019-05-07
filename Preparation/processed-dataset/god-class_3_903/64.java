//private static boolean boilerPlateDumped = false; 
static void PrintBoilerPlate(java.io.PrintWriter ostr) {
    ostr.println((Options.getStatic() ? "static " : "") + "private void " + "jjCheckNAdd(int state)");
    ostr.println("{");
    ostr.println("   if (jjrounds[state] != jjround)");
    ostr.println("   {");
    ostr.println("      jjstateSet[jjnewStateCnt++] = state;");
    ostr.println("      jjrounds[state] = jjround;");
    ostr.println("   }");
    ostr.println("}");
    ostr.println((Options.getStatic() ? "static " : "") + "private void " + "jjAddStates(int start, int end)");
    ostr.println("{");
    ostr.println("   do {");
    ostr.println("      jjstateSet[jjnewStateCnt++] = jjnextStates[start];");
    ostr.println("   } while (start++ != end);");
    ostr.println("}");
    ostr.println((Options.getStatic() ? "static " : "") + "private void " + "jjCheckNAddTwoStates(int state1, int state2)");
    ostr.println("{");
    ostr.println("   jjCheckNAdd(state1);");
    ostr.println("   jjCheckNAdd(state2);");
    ostr.println("}");
    ostr.println("");
    if (jjCheckNAddStatesDualNeeded) {
        ostr.println((Options.getStatic() ? "static " : "") + "private void " + "jjCheckNAddStates(int start, int end)");
        ostr.println("{");
        ostr.println("   do {");
        ostr.println("      jjCheckNAdd(jjnextStates[start]);");
        ostr.println("   } while (start++ != end);");
        ostr.println("}");
        ostr.println("");
    }
    if (jjCheckNAddStatesUnaryNeeded) {
        ostr.println((Options.getStatic() ? "static " : "") + "private void " + "jjCheckNAddStates(int start)");
        ostr.println("{");
        ostr.println("   jjCheckNAdd(jjnextStates[start]);");
        ostr.println("   jjCheckNAdd(jjnextStates[start + 1]);");
        ostr.println("}");
        ostr.println("");
    }
}
