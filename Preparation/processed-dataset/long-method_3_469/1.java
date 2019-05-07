protected final void field() throws RecognitionException, TokenStreamException {
    returnAST = null;
    ASTPair currentAST = new ASTPair();
    JavaAST field_AST = null;
    JavaAST mods_AST = null;
    JavaAST cparams_AST = null;
    JavaAST cthrows_AST = null;
    JavaAST cbody_AST = null;
    JavaAST t_AST = null;
    Token name = null;
    jparse.expr.IdentifierAST name_AST = null;
    JavaAST mparams_AST = null;
    JavaAST d_AST = null;
    JavaAST mthrows_AST = null;
    JavaAST mbody_AST = null;
    JavaAST vars_AST = null;
    try {
        // for error handling 
        switch(LA(1)) {
            case LCURLY:
                {
                    compoundStatement();
                    astFactory.addASTChild(currentAST, returnAST);
                    if (inputState.guessing == 0) {
                        field_AST = (JavaAST) currentAST.root;
                        field_AST = (JavaAST) astFactory.make((new ASTArray(2)).add((JavaAST) astFactory.create(INSTANCE_INIT, "INSTANCE_INIT")).add(field_AST));
                        currentAST.root = field_AST;
                        currentAST.child = field_AST != null && field_AST.getFirstChild() != null ? field_AST.getFirstChild() : field_AST;
                        currentAST.advanceChildToEnd();
                    }
                    field_AST = (JavaAST) currentAST.root;
                    break;
                }
            case SEMI:
                {
                    JavaAST tmp24_AST = null;
                    tmp24_AST = (JavaAST) astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp24_AST);
                    match(SEMI);
                    field_AST = (JavaAST) currentAST.root;
                    break;
                }
            default:
                if ((_tokenSet_9.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
                    modifiers();
                    mods_AST = (JavaAST) returnAST;
                    {
                        switch(LA(1)) {
                            case LITERAL_class:
                                {
                                    classDefinition((ModifierAST) mods_AST);
                                    astFactory.addASTChild(currentAST, returnAST);
                                    break;
                                }
                            case LITERAL_interface:
                                {
                                    interfaceDefinition((ModifierAST) mods_AST);
                                    astFactory.addASTChild(currentAST, returnAST);
                                    break;
                                }
                            default:
                                if ((LA(1) == IDENT) && (LA(2) == LPAREN)) {
                                    if (inputState.guessing == 0) {
                                        JavaAST.currSymTable = new SymbolTable();
                                    }
                                    jparse.expr.IdentifierAST tmp25_AST = null;
                                    tmp25_AST = (jparse.expr.IdentifierAST) astFactory.create(LT(1), "jparse.expr.IdentifierAST");
                                    astFactory.addASTChild(currentAST, tmp25_AST);
                                    match(IDENT);
                                    JavaAST tmp26_AST = null;
                                    tmp26_AST = (JavaAST) astFactory.create(LT(1));
                                    astFactory.addASTChild(currentAST, tmp26_AST);
                                    match(LPAREN);
                                    parameterDeclarationList();
                                    cparams_AST = (JavaAST) returnAST;
                                    astFactory.addASTChild(currentAST, returnAST);
                                    JavaAST tmp27_AST = null;
                                    tmp27_AST = (JavaAST) astFactory.create(LT(1));
                                    astFactory.addASTChild(currentAST, tmp27_AST);
                                    match(RPAREN);
                                    {
                                        switch(LA(1)) {
                                            case LITERAL_throws:
                                                {
                                                    throwsClause();
                                                    cthrows_AST = (JavaAST) returnAST;
                                                    astFactory.addASTChild(currentAST, returnAST);
                                                    break;
                                                }
                                            case LCURLY:
                                                {
                                                    break;
                                                }
                                            default:
                                                {
                                                    throw new NoViableAltException(LT(1), getFilename());
                                                }
                                        }
                                    }
                                    compoundStatement();
                                    cbody_AST = (JavaAST) returnAST;
                                    astFactory.addASTChild(currentAST, returnAST);
                                    if (inputState.guessing == 0) {
                                        field_AST = (JavaAST) currentAST.root;
                                        JavaAST.currSymTable = JavaAST.currSymTable.parent;
                                        final ConstrAST constr = new ConstrAST((ModifierAST) mods_AST, cparams_AST, cthrows_AST, (CompoundAST) cbody_AST);
                                        field_AST = (JavaAST) astFactory.make((new ASTArray(3)).add(constr).add(mods_AST).add(field_AST));
                                        currentAST.root = field_AST;
                                        currentAST.child = field_AST != null && field_AST.getFirstChild() != null ? field_AST.getFirstChild() : field_AST;
                                        currentAST.advanceChildToEnd();
                                    }
                                } else if ((_tokenSet_15.member(LA(1))) && (_tokenSet_16.member(LA(2)))) {
                                    typeSpec();
                                    t_AST = (JavaAST) returnAST;
                                    astFactory.addASTChild(currentAST, returnAST);
                                    {
                                        if ((LA(1) == IDENT) && (LA(2) == LPAREN)) {
                                            name = LT(1);
                                            name_AST = (jparse.expr.IdentifierAST) astFactory.create(name, "jparse.expr.IdentifierAST");
                                            astFactory.addASTChild(currentAST, name_AST);
                                            match(IDENT);
                                            if (inputState.guessing == 0) {
                                                JavaAST.currSymTable = new SymbolTable();
                                            }
                                            JavaAST tmp28_AST = null;
                                            tmp28_AST = (JavaAST) astFactory.create(LT(1));
                                            astFactory.addASTChild(currentAST, tmp28_AST);
                                            match(LPAREN);
                                            parameterDeclarationList();
                                            mparams_AST = (JavaAST) returnAST;
                                            astFactory.addASTChild(currentAST, returnAST);
                                            JavaAST tmp29_AST = null;
                                            tmp29_AST = (JavaAST) astFactory.create(LT(1));
                                            astFactory.addASTChild(currentAST, tmp29_AST);
                                            match(RPAREN);
                                            declaratorBrackets();
                                            d_AST = (JavaAST) returnAST;
                                            astFactory.addASTChild(currentAST, returnAST);
                                            {
                                                switch(LA(1)) {
                                                    case LITERAL_throws:
                                                        {
                                                            throwsClause();
                                                            mthrows_AST = (JavaAST) returnAST;
                                                            astFactory.addASTChild(currentAST, returnAST);
                                                            break;
                                                        }
                                                    case SEMI:
                                                    case LCURLY:
                                                        {
                                                            break;
                                                        }
                                                    default:
                                                        {
                                                            throw new NoViableAltException(LT(1), getFilename());
                                                        }
                                                }
                                            }
                                            {
                                                switch(LA(1)) {
                                                    case LCURLY:
                                                        {
                                                            compoundStatement();
                                                            mbody_AST = (JavaAST) returnAST;
                                                            astFactory.addASTChild(currentAST, returnAST);
                                                            break;
                                                        }
                                                    case SEMI:
                                                        {
                                                            JavaAST tmp30_AST = null;
                                                            tmp30_AST = (JavaAST) astFactory.create(LT(1));
                                                            astFactory.addASTChild(currentAST, tmp30_AST);
                                                            match(SEMI);
                                                            break;
                                                        }
                                                    default:
                                                        {
                                                            throw new NoViableAltException(LT(1), getFilename());
                                                        }
                                                }
                                            }
                                            if (inputState.guessing == 0) {
                                                field_AST = (JavaAST) currentAST.root;
                                                JavaAST.currSymTable = JavaAST.currSymTable.parent;
                                                final ModifierAST mod = (ModifierAST) mods_AST;
                                                if (jparse.TypeAST.currType.modifiers.isInterface())
                                                    mod.setInterfaceMethod();
                                                final MethAST meth = new MethAST(mod, (jparse.expr.TypeAST) t_AST, name_AST, mparams_AST, d_AST, mthrows_AST, (CompoundAST) mbody_AST);
                                                field_AST = (JavaAST) astFactory.make((new ASTArray(3)).add(meth).add(mod).add(field_AST));
                                                currentAST.root = field_AST;
                                                currentAST.child = field_AST != null && field_AST.getFirstChild() != null ? field_AST.getFirstChild() : field_AST;
                                                currentAST.advanceChildToEnd();
                                            }
                                        } else if ((LA(1) == IDENT) && (_tokenSet_17.member(LA(2)))) {
                                            variableDefinitions();
                                            vars_AST = (JavaAST) returnAST;
                                            astFactory.addASTChild(currentAST, returnAST);
                                            JavaAST tmp31_AST = null;
                                            tmp31_AST = (JavaAST) astFactory.create(LT(1));
                                            astFactory.addASTChild(currentAST, tmp31_AST);
                                            match(SEMI);
                                            if (inputState.guessing == 0) {
                                                field_AST = (JavaAST) currentAST.root;
                                                final DeclarationAST decl = new DeclarationAST((ModifierAST) mods_AST, (jparse.expr.TypeAST) t_AST, vars_AST);
                                                field_AST = (JavaAST) astFactory.make((new ASTArray(3)).add(decl).add(mods_AST).add(field_AST));
                                                currentAST.root = field_AST;
                                                currentAST.child = field_AST != null && field_AST.getFirstChild() != null ? field_AST.getFirstChild() : field_AST;
                                                currentAST.advanceChildToEnd();
                                            }
                                        } else {
                                            throw new NoViableAltException(LT(1), getFilename());
                                        }
                                    }
                                } else {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    field_AST = (JavaAST) currentAST.root;
                } else if ((LA(1) == LITERAL_static) && (LA(2) == LCURLY)) {
                    JavaAST tmp32_AST = null;
                    tmp32_AST = (JavaAST) astFactory.create(LT(1));
                    astFactory.makeASTRoot(currentAST, tmp32_AST);
                    match(LITERAL_static);
                    compoundStatement();
                    astFactory.addASTChild(currentAST, returnAST);
                    field_AST = (JavaAST) currentAST.root;
                } else {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
    } catch (RecognitionException ex) {
        if (inputState.guessing == 0) {
            reportError(ex);
            consume();
            consumeUntil(_tokenSet_18);
        } else {
            throw ex;
        }
    }
    returnAST = field_AST;
}
