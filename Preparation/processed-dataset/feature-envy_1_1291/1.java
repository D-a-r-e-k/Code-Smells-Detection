public final void compilationUnit() throws RecognitionException, TokenStreamException {
    returnAST = null;
    ASTPair currentAST = new ASTPair();
    JavaAST compilationUnit_AST = null;
    JavaAST im_AST = null;
    JavaAST typ_AST = null;
    ArrayList imports = new ArrayList();
    ArrayList types = new ArrayList();
    try {
        // for error handling 
        if (inputState.guessing == 0) {
            FileAST.currFile = new FileAST(file);
        }
        {
            switch(LA(1)) {
                case LITERAL_package:
                    {
                        packageDefinition();
                        astFactory.addASTChild(currentAST, returnAST);
                        break;
                    }
                case EOF:
                case SEMI:
                case LITERAL_import:
                case LITERAL_public:
                case LITERAL_private:
                case LITERAL_protected:
                case LITERAL_static:
                case LITERAL_final:
                case LITERAL_synchronized:
                case LITERAL_volatile:
                case LITERAL_transient:
                case LITERAL_native:
                case LITERAL_abstract:
                case LITERAL_strictfp:
                case LITERAL_class:
                case LITERAL_interface:
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
            _loop4: do {
                if ((LA(1) == LITERAL_import)) {
                    importDefinition();
                    im_AST = (JavaAST) returnAST;
                    astFactory.addASTChild(currentAST, returnAST);
                    if (inputState.guessing == 0) {
                        imports.add(((IdentifierAST) im_AST.getFirstChild()).getName());
                    }
                } else {
                    break _loop4;
                }
            } while (true);
        }
        if (inputState.guessing == 0) {
            FileAST.currFile.imports = new String[imports.size()];
            imports.toArray(FileAST.currFile.imports);
            imports = null;
        }
        {
            _loop6: do {
                if ((_tokenSet_0.member(LA(1)))) {
                    typeDefinition();
                    typ_AST = (JavaAST) returnAST;
                    astFactory.addASTChild(currentAST, returnAST);
                    if (inputState.guessing == 0) {
                        if (typ_AST.getType() != SEMI)
                            types.add(typ_AST);
                    }
                } else {
                    break _loop6;
                }
            } while (true);
        }
        if (inputState.guessing == 0) {
            FileAST.currFile.types = new jparse.TypeAST[types.size()];
            types.toArray(FileAST.currFile.types);
            types = null;
        }
        match(Token.EOF_TYPE);
        if (inputState.guessing == 0) {
            compilationUnit_AST = (JavaAST) currentAST.root;
            compilationUnit_AST = (JavaAST) astFactory.make((new ASTArray(2)).add(FileAST.currFile).add(compilationUnit_AST));
            currentAST.root = compilationUnit_AST;
            currentAST.child = compilationUnit_AST != null && compilationUnit_AST.getFirstChild() != null ? compilationUnit_AST.getFirstChild() : compilationUnit_AST;
            currentAST.advanceChildToEnd();
        }
        compilationUnit_AST = (JavaAST) currentAST.root;
    } catch (RecognitionException ex) {
        if (inputState.guessing == 0) {
            reportError(ex);
            consume();
            consumeUntil(_tokenSet_1);
        } else {
            throw ex;
        }
    }
    returnAST = compilationUnit_AST;
}
