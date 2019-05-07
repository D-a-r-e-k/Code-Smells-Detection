public static void start() throws MetaParseException {
    if (JavaCCErrors.get_error_count() != 0)
        throw new MetaParseException();
    if (Options.getLookahead() > 1 && !Options.getForceLaCheck() && Options.getSanityCheck()) {
        JavaCCErrors.warning("Lookahead adequacy checking not being performed since option LOOKAHEAD " + "is more than 1.  Set option FORCE_LA_CHECK to true to force checking.");
    }
    /*
     * The following walks the entire parse tree to convert all LOOKAHEAD's
     * that are not at choice points (but at beginning of sequences) and converts
     * them to trivial choices.  This way, their semantic lookahead specification
     * can be evaluated during other lookahead evaluations.
     */
    for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
        ExpansionTreeWalker.postOrderWalk(((NormalProduction) it.next()).getExpansion(), new LookaheadFixer());
    }
    /*
     * The following loop populates "production_table"
     */
    for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
        NormalProduction p = (NormalProduction) it.next();
        if (production_table.put(p.getLhs(), p) != null) {
            JavaCCErrors.semantic_error(p, p.getLhs() + " occurs on the left hand side of more than one production.");
        }
    }
    /*
     * The following walks the entire parse tree to make sure that all
     * non-terminals on RHS's are defined on the LHS.
     */
    for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
        ExpansionTreeWalker.preOrderWalk(((NormalProduction) it.next()).getExpansion(), new ProductionDefinedChecker());
    }
    /*
     * The following loop ensures that all target lexical states are
     * defined.  Also piggybacking on this loop is the detection of
     * <EOF> and <name> in token productions.  After reporting an
     * error, these entries are removed.  Also checked are definitions
     * on inline private regular expressions.
     * This loop works slightly differently when USER_TOKEN_MANAGER
     * is set to true.  In this case, <name> occurrences are OK, while
     * regular expression specs generate a warning.
     */
    for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
        TokenProduction tp = (TokenProduction) (it.next());
        List respecs = tp.respecs;
        for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
            RegExprSpec res = (RegExprSpec) (it1.next());
            if (res.nextState != null) {
                if (lexstate_S2I.get(res.nextState) == null) {
                    JavaCCErrors.semantic_error(res.nsTok, "Lexical state \"" + res.nextState + "\" has not been defined.");
                }
            }
            if (res.rexp instanceof REndOfFile) {
                //JavaCCErrors.semantic_error(res.rexp, "Badly placed <EOF>."); 
                if (tp.lexStates != null)
                    JavaCCErrors.semantic_error(res.rexp, "EOF action/state change must be specified for all states, " + "i.e., <*>TOKEN:.");
                if (tp.kind != TokenProduction.TOKEN)
                    JavaCCErrors.semantic_error(res.rexp, "EOF action/state change can be specified only in a " + "TOKEN specification.");
                if (nextStateForEof != null || actForEof != null)
                    JavaCCErrors.semantic_error(res.rexp, "Duplicate action/state change specification for <EOF>.");
                actForEof = res.act;
                nextStateForEof = res.nextState;
                prepareToRemove(respecs, res);
            } else if (tp.isExplicit && Options.getUserTokenManager()) {
                JavaCCErrors.warning(res.rexp, "Ignoring regular expression specification since " + "option USER_TOKEN_MANAGER has been set to true.");
            } else if (tp.isExplicit && !Options.getUserTokenManager() && res.rexp instanceof RJustName) {
                JavaCCErrors.warning(res.rexp, "Ignoring free-standing regular expression reference.  " + "If you really want this, you must give it a different label as <NEWLABEL:<" + res.rexp.label + ">>.");
                prepareToRemove(respecs, res);
            } else if (!tp.isExplicit && res.rexp.private_rexp) {
                JavaCCErrors.semantic_error(res.rexp, "Private (#) regular expression cannot be defined within " + "grammar productions.");
            }
        }
    }
    removePreparedItems();
    /*
     * The following loop inserts all names of regular expressions into
     * "named_tokens_table" and "ordered_named_tokens".
     * Duplications are flagged as errors.
     */
    for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
        TokenProduction tp = (TokenProduction) (it.next());
        List respecs = tp.respecs;
        for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
            RegExprSpec res = (RegExprSpec) (it1.next());
            if (!(res.rexp instanceof RJustName) && !res.rexp.label.equals("")) {
                String s = res.rexp.label;
                Object obj = named_tokens_table.put(s, res.rexp);
                if (obj != null) {
                    JavaCCErrors.semantic_error(res.rexp, "Multiply defined lexical token name \"" + s + "\".");
                } else {
                    ordered_named_tokens.add(res.rexp);
                }
                if (lexstate_S2I.get(s) != null) {
                    JavaCCErrors.semantic_error(res.rexp, "Lexical token name \"" + s + "\" is the same as " + "that of a lexical state.");
                }
            }
        }
    }
    /*
     * The following code merges multiple uses of the same string in the same
     * lexical state and produces error messages when there are multiple
     * explicit occurrences (outside the BNF) of the string in the same
     * lexical state, or when within BNF occurrences of a string are duplicates
     * of those that occur as non-TOKEN's (SKIP, MORE, SPECIAL_TOKEN) or private
     * regular expressions.  While doing this, this code also numbers all
     * regular expressions (by setting their ordinal values), and populates the
     * table "names_of_tokens".
     */
    tokenCount = 1;
    for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
        TokenProduction tp = (TokenProduction) (it.next());
        List respecs = tp.respecs;
        if (tp.lexStates == null) {
            tp.lexStates = new String[lexstate_I2S.size()];
            int i = 0;
            for (Enumeration enum1 = lexstate_I2S.elements(); enum1.hasMoreElements(); ) {
                tp.lexStates[i++] = (String) (enum1.nextElement());
            }
        }
        Hashtable table[] = new Hashtable[tp.lexStates.length];
        for (int i = 0; i < tp.lexStates.length; i++) {
            table[i] = (Hashtable) simple_tokens_table.get(tp.lexStates[i]);
        }
        for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
            RegExprSpec res = (RegExprSpec) (it1.next());
            if (res.rexp instanceof RStringLiteral) {
                RStringLiteral sl = (RStringLiteral) res.rexp;
                // This loop performs the checks and actions with respect to each lexical state. 
                for (int i = 0; i < table.length; i++) {
                    // Get table of all case variants of "sl.image" into table2. 
                    Hashtable table2 = (Hashtable) (table[i].get(sl.image.toUpperCase()));
                    if (table2 == null) {
                        // There are no case variants of "sl.image" earlier than the current one. 
                        // So go ahead and insert this item. 
                        if (sl.ordinal == 0) {
                            sl.ordinal = tokenCount++;
                        }
                        table2 = new Hashtable();
                        table2.put(sl.image, sl);
                        table[i].put(sl.image.toUpperCase(), table2);
                    } else if (hasIgnoreCase(table2, sl.image)) {
                        // hasIgnoreCase sets "other" if it is found. 
                        // Since IGNORE_CASE version exists, current one is useless and bad. 
                        if (!sl.tpContext.isExplicit) {
                            // inline BNF string is used earlier with an IGNORE_CASE. 
                            JavaCCErrors.semantic_error(sl, "String \"" + sl.image + "\" can never be matched " + "due to presence of more general (IGNORE_CASE) regular expression " + "at line " + other.getLine() + ", column " + other.getColumn() + ".");
                        } else {
                            // give the standard error message. 
                            JavaCCErrors.semantic_error(sl, "Duplicate definition of string token \"" + sl.image + "\" " + "can never be matched.");
                        }
                    } else if (sl.tpContext.ignoreCase) {
                        // This has to be explicit.  A warning needs to be given with respect 
                        // to all previous strings. 
                        String pos = "";
                        int count = 0;
                        for (Enumeration enum2 = table2.elements(); enum2.hasMoreElements(); ) {
                            RegularExpression rexp = (RegularExpression) (enum2.nextElement());
                            if (count != 0)
                                pos += ",";
                            pos += " line " + rexp.getLine();
                            count++;
                        }
                        if (count == 1) {
                            JavaCCErrors.warning(sl, "String with IGNORE_CASE is partially superceded by string at" + pos + ".");
                        } else {
                            JavaCCErrors.warning(sl, "String with IGNORE_CASE is partially superceded by strings at" + pos + ".");
                        }
                        // This entry is legitimate.  So insert it. 
                        if (sl.ordinal == 0) {
                            sl.ordinal = tokenCount++;
                        }
                        table2.put(sl.image, sl);
                    } else {
                        // The rest of the cases do not involve IGNORE_CASE. 
                        RegularExpression re = (RegularExpression) table2.get(sl.image);
                        if (re == null) {
                            if (sl.ordinal == 0) {
                                sl.ordinal = tokenCount++;
                            }
                            table2.put(sl.image, sl);
                        } else if (tp.isExplicit) {
                            // This is an error even if the first occurrence was implicit. 
                            if (tp.lexStates[i].equals("DEFAULT")) {
                                JavaCCErrors.semantic_error(sl, "Duplicate definition of string token \"" + sl.image + "\".");
                            } else {
                                JavaCCErrors.semantic_error(sl, "Duplicate definition of string token \"" + sl.image + "\" in lexical state \"" + tp.lexStates[i] + "\".");
                            }
                        } else if (re.tpContext.kind != TokenProduction.TOKEN) {
                            JavaCCErrors.semantic_error(sl, "String token \"" + sl.image + "\" has been defined as a \"" + TokenProduction.kindImage[re.tpContext.kind] + "\" token.");
                        } else if (re.private_rexp) {
                            JavaCCErrors.semantic_error(sl, "String token \"" + sl.image + "\" has been defined as a private regular expression.");
                        } else {
                            // This is now a legitimate reference to an existing RStringLiteral. 
                            // So we assign it a number and take it out of "rexprlist". 
                            // Therefore, if all is OK (no errors), then there will be only unequal 
                            // string literals in each lexical state.  Note that the only way 
                            // this can be legal is if this is a string declared inline within the 
                            // BNF.  Hence, it belongs to only one lexical state - namely "DEFAULT". 
                            sl.ordinal = re.ordinal;
                            prepareToRemove(respecs, res);
                        }
                    }
                }
            } else if (!(res.rexp instanceof RJustName)) {
                res.rexp.ordinal = tokenCount++;
            }
            if (!(res.rexp instanceof RJustName) && !res.rexp.label.equals("")) {
                names_of_tokens.put(new Integer(res.rexp.ordinal), res.rexp.label);
            }
            if (!(res.rexp instanceof RJustName)) {
                rexps_of_tokens.put(new Integer(res.rexp.ordinal), res.rexp);
            }
        }
    }
    removePreparedItems();
    /*
     * The following code performs a tree walk on all regular expressions
     * attaching links to "RJustName"s.  Error messages are given if
     * undeclared names are used, or if "RJustNames" refer to private
     * regular expressions or to regular expressions of any kind other
     * than TOKEN.  In addition, this loop also removes top level
     * "RJustName"s from "rexprlist".
     * This code is not executed if Options.getUserTokenManager() is set to
     * true.  Instead the following block of code is executed.
     */
    if (!Options.getUserTokenManager()) {
        FixRJustNames frjn = new FixRJustNames();
        for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
            TokenProduction tp = (TokenProduction) (it.next());
            List respecs = tp.respecs;
            for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
                RegExprSpec res = (RegExprSpec) (it1.next());
                frjn.root = res.rexp;
                ExpansionTreeWalker.preOrderWalk(res.rexp, frjn);
                if (res.rexp instanceof RJustName) {
                    prepareToRemove(respecs, res);
                }
            }
        }
    }
    removePreparedItems();
    /*
     * The following code is executed only if Options.getUserTokenManager() is
     * set to true.  This code visits all top-level "RJustName"s (ignores
     * "RJustName"s nested within regular expressions).  Since regular expressions
     * are optional in this case, "RJustName"s without corresponding regular
     * expressions are given ordinal values here.  If "RJustName"s refer to
     * a named regular expression, their ordinal values are set to reflect this.
     * All but one "RJustName" node is removed from the lists by the end of
     * execution of this code.
     */
    if (Options.getUserTokenManager()) {
        for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
            TokenProduction tp = (TokenProduction) (it.next());
            List respecs = tp.respecs;
            for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
                RegExprSpec res = (RegExprSpec) (it1.next());
                if (res.rexp instanceof RJustName) {
                    RJustName jn = (RJustName) res.rexp;
                    RegularExpression rexp = (RegularExpression) named_tokens_table.get(jn.label);
                    if (rexp == null) {
                        jn.ordinal = tokenCount++;
                        named_tokens_table.put(jn.label, jn);
                        ordered_named_tokens.add(jn);
                        names_of_tokens.put(new Integer(jn.ordinal), jn.label);
                    } else {
                        jn.ordinal = rexp.ordinal;
                        prepareToRemove(respecs, res);
                    }
                }
            }
        }
    }
    removePreparedItems();
    /*
     * The following code is executed only if Options.getUserTokenManager() is
     * set to true.  This loop labels any unlabeled regular expression and
     * prints a warning that it is doing so.  These labels are added to
     * "ordered_named_tokens" so that they may be generated into the ...Constants
     * file.
     */
    if (Options.getUserTokenManager()) {
        for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
            TokenProduction tp = (TokenProduction) (it.next());
            List respecs = tp.respecs;
            for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
                RegExprSpec res = (RegExprSpec) (it1.next());
                Integer ii = new Integer(res.rexp.ordinal);
                if (names_of_tokens.get(ii) == null) {
                    JavaCCErrors.warning(res.rexp, "Unlabeled regular expression cannot be referred to by " + "user generated token manager.");
                }
            }
        }
    }
    if (JavaCCErrors.get_error_count() != 0)
        throw new MetaParseException();
    // The following code sets the value of the "emptyPossible" field of NormalProduction 
    // nodes.  This field is initialized to false, and then the entire list of 
    // productions is processed.  This is repeated as long as at least one item 
    // got updated from false to true in the pass. 
    boolean emptyUpdate = true;
    while (emptyUpdate) {
        emptyUpdate = false;
        for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
            NormalProduction prod = (NormalProduction) it.next();
            if (emptyExpansionExists(prod.getExpansion())) {
                if (!prod.isEmptyPossible()) {
                    emptyUpdate = prod.setEmptyPossible(true);
                }
            }
        }
    }
    if (Options.getSanityCheck() && JavaCCErrors.get_error_count() == 0) {
        // The following code checks that all ZeroOrMore, ZeroOrOne, and OneOrMore nodes 
        // do not contain expansions that can expand to the empty token list. 
        for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
            ExpansionTreeWalker.preOrderWalk(((NormalProduction) it.next()).getExpansion(), new EmptyChecker());
        }
        // The following code goes through the productions and adds pointers to other 
        // productions that it can expand to without consuming any tokens.  Once this is 
        // done, a left-recursion check can be performed. 
        for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
            NormalProduction prod = (NormalProduction) it.next();
            addLeftMost(prod, prod.getExpansion());
        }
        // Now the following loop calls a recursive walk routine that searches for 
        // actual left recursions.  The way the algorithm is coded, once a node has 
        // been determined to participate in a left recursive loop, it is not tried 
        // in any other loop. 
        for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
            NormalProduction prod = (NormalProduction) it.next();
            if (prod.getWalkStatus() == 0) {
                prodWalk(prod);
            }
        }
        // Now we do a similar, but much simpler walk for the regular expression part of 
        // the grammar.  Here we are looking for any kind of loop, not just left recursions, 
        // so we only need to do the equivalent of the above walk. 
        // This is not done if option USER_TOKEN_MANAGER is set to true. 
        if (!Options.getUserTokenManager()) {
            for (Iterator it = rexprlist.iterator(); it.hasNext(); ) {
                TokenProduction tp = (TokenProduction) (it.next());
                List respecs = tp.respecs;
                for (Iterator it1 = respecs.iterator(); it1.hasNext(); ) {
                    RegExprSpec res = (RegExprSpec) (it1.next());
                    RegularExpression rexp = res.rexp;
                    if (rexp.walkStatus == 0) {
                        rexp.walkStatus = -1;
                        if (rexpWalk(rexp)) {
                            loopString = "..." + rexp.label + "... --> " + loopString;
                            JavaCCErrors.semantic_error(rexp, "Loop in regular expression detected: \"" + loopString + "\"");
                        }
                        rexp.walkStatus = 1;
                    }
                }
            }
        }
        /*
       * The following code performs the lookahead ambiguity checking.
       */
        if (JavaCCErrors.get_error_count() == 0) {
            for (Iterator it = bnfproductions.iterator(); it.hasNext(); ) {
                ExpansionTreeWalker.preOrderWalk(((NormalProduction) it.next()).getExpansion(), new LookaheadChecker());
            }
        }
    }
    // matches "if (Options.getSanityCheck()) {" 
    if (JavaCCErrors.get_error_count() != 0)
        throw new MetaParseException();
}
