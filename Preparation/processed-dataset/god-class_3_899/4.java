/**
   * This method takes two parameters - an array of Lookahead's
   * "conds", and an array of String's "actions".  "actions" contains
   * exactly one element more than "conds".  "actions" are Java source
   * code, and "conds" translate to conditions - so lets say
   * "f(conds[i])" is true if the lookahead required by "conds[i]" is
   * indeed the case.  This method returns a string corresponding to
   * the Java code for:
   *
   *   if (f(conds[0]) actions[0]
   *   else if (f(conds[1]) actions[1]
   *   . . .
   *   else actions[action.length-1]
   *
   * A particular action entry ("actions[i]") can be null, in which
   * case, a noop is generated for that action.
   */
static String buildLookaheadChecker(Lookahead[] conds, String[] actions) {
    // The state variables. 
    int state = NOOPENSTM;
    int indentAmt = 0;
    boolean[] casedValues = new boolean[tokenCount];
    String retval = "";
    Lookahead la;
    Token t = null;
    int tokenMaskSize = (tokenCount - 1) / 32 + 1;
    int[] tokenMask = null;
    // Iterate over all the conditions. 
    int index = 0;
    while (index < conds.length) {
        la = conds[index];
        jj2LA = false;
        if ((la.getAmount() == 0) || Semanticize.emptyExpansionExists(la.getLaExpansion()) || javaCodeCheck(la.getLaExpansion())) {
            // This handles the following cases: 
            // . If syntactic lookahead is not wanted (and hence explicitly specified 
            //   as 0). 
            // . If it is possible for the lookahead expansion to recognize the empty 
            //   string - in which case the lookahead trivially passes. 
            // . If the lookahead expansion has a JAVACODE production that it directly 
            //   expands to - in which case the lookahead trivially passes. 
            if (la.getActionTokens().size() == 0) {
                // In addition, if there is no semantic lookahead, then the 
                // lookahead trivially succeeds.  So break the main loop and 
                // treat this case as the default last action. 
                break;
            } else {
                // This case is when there is only semantic lookahead 
                // (without any preceding syntactic lookahead).  In this 
                // case, an "if" statement is generated. 
                switch(state) {
                    case NOOPENSTM:
                        retval += "\n" + "if (";
                        indentAmt++;
                        break;
                    case OPENIF:
                        retval += "\n" + "} else if (";
                        break;
                    case OPENSWITCH:
                        retval += "\n" + "default:" + "";
                        if (Options.getErrorReporting()) {
                            retval += "\njj_la1[" + maskindex + "] = jj_gen;";
                            maskindex++;
                        }
                        maskVals.add(tokenMask);
                        retval += "\n" + "if (";
                        indentAmt++;
                }
                printTokenSetup((Token) (la.getActionTokens().get(0)));
                for (Iterator it = la.getActionTokens().iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    retval += printToken(t);
                }
                retval += printTrailingComments(t);
                retval += ") {" + actions[index];
                state = OPENIF;
            }
        } else if (la.getAmount() == 1 && la.getActionTokens().size() == 0) {
            // Special optimal processing when the lookahead is exactly 1, and there 
            // is no semantic lookahead. 
            if (firstSet == null) {
                firstSet = new boolean[tokenCount];
            }
            for (int i = 0; i < tokenCount; i++) {
                firstSet[i] = false;
            }
            // jj2LA is set to false at the beginning of the containing "if" statement. 
            // It is checked immediately after the end of the same statement to determine 
            // if lookaheads are to be performed using calls to the jj2 methods. 
            genFirstSet(la.getLaExpansion());
            // genFirstSet may find that semantic attributes are appropriate for the next 
            // token.  In which case, it sets jj2LA to true. 
            if (!jj2LA) {
                // This case is if there is no applicable semantic lookahead and the lookahead 
                // is one (excluding the earlier cases such as JAVACODE, etc.). 
                switch(state) {
                    case OPENIF:
                        retval += "\n" + "} else {";
                    // Control flows through to next case. 
                    case NOOPENSTM:
                        retval += "\n" + "switch (";
                        if (Options.getCacheTokens()) {
                            retval += "jj_nt.kind) {";
                        } else {
                            retval += "(jj_ntk==-1)?jj_ntk():jj_ntk) {";
                        }
                        for (int i = 0; i < tokenCount; i++) {
                            casedValues[i] = false;
                        }
                        indentAmt++;
                        tokenMask = new int[tokenMaskSize];
                        for (int i = 0; i < tokenMaskSize; i++) {
                            tokenMask[i] = 0;
                        }
                }
                for (int i = 0; i < tokenCount; i++) {
                    if (firstSet[i]) {
                        if (!casedValues[i]) {
                            casedValues[i] = true;
                            retval += "\ncase ";
                            int j1 = i / 32;
                            int j2 = i % 32;
                            tokenMask[j1] |= 1 << j2;
                            String s = (String) (names_of_tokens.get(new Integer(i)));
                            if (s == null) {
                                retval += i;
                            } else {
                                retval += s;
                            }
                            retval += ":";
                        }
                    }
                }
                retval += actions[index];
                retval += "\nbreak;";
                state = OPENSWITCH;
            }
        } else {
            // This is the case when lookahead is determined through calls to 
            // jj2 methods.  The other case is when lookahead is 1, but semantic 
            // attributes need to be evaluated.  Hence this crazy control structure. 
            jj2LA = true;
        }
        if (jj2LA) {
            // In this case lookahead is determined by the jj2 methods. 
            switch(state) {
                case NOOPENSTM:
                    retval += "\n" + "if (";
                    indentAmt++;
                    break;
                case OPENIF:
                    retval += "\n" + "} else if (";
                    break;
                case OPENSWITCH:
                    retval += "\n" + "default:" + "";
                    if (Options.getErrorReporting()) {
                        retval += "\njj_la1[" + maskindex + "] = jj_gen;";
                        maskindex++;
                    }
                    maskVals.add(tokenMask);
                    retval += "\n" + "if (";
                    indentAmt++;
            }
            jj2index++;
            // At this point, la.la_expansion.internal_name must be "". 
            la.getLaExpansion().internal_name = "_" + jj2index;
            phase2list.add(la);
            retval += "jj_2" + la.getLaExpansion().internal_name + "(" + la.getAmount() + ")";
            if (la.getActionTokens().size() != 0) {
                // In addition, there is also a semantic lookahead.  So concatenate 
                // the semantic check with the syntactic one. 
                retval += " && (";
                printTokenSetup((Token) (la.getActionTokens().get(0)));
                for (Iterator it = la.getActionTokens().iterator(); it.hasNext(); ) {
                    t = (Token) it.next();
                    retval += printToken(t);
                }
                retval += printTrailingComments(t);
                retval += ")";
            }
            retval += ") {" + actions[index];
            state = OPENIF;
        }
        index++;
    }
    // Generate code for the default case.  Note this may not 
    // be the last entry of "actions" if any condition can be 
    // statically determined to be always "true". 
    switch(state) {
        case NOOPENSTM:
            retval += actions[index];
            break;
        case OPENIF:
            retval += "\n" + "} else {" + actions[index];
            break;
        case OPENSWITCH:
            retval += "\n" + "default:" + "";
            if (Options.getErrorReporting()) {
                retval += "\njj_la1[" + maskindex + "] = jj_gen;";
                maskVals.add(tokenMask);
                maskindex++;
            }
            retval += actions[index];
    }
    for (int i = 0; i < indentAmt; i++) {
        retval += "\n}";
    }
    return retval;
}
