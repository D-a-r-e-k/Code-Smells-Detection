// $ANTLR 2.7.4: "java.g" -> "JavaParser.java"$

/*
 * @(#)$Id: java.g,v 1.11 2004/07/14 14:50:40 bdg534 Exp $
 *
 * JParse: a freely available Java parser
 * Copyright (C) 2000,2004 Jeremiah W. James, except for portions derived
 * from the public domain Java grammar written by John Mitchell, Terence
 * Parr, John Lilley, Scott Stanchfield, Markus Mohnen, and Peter Williams.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Author: Jerry James
 * Email: james@eecs.ku.edu, james@ittc.ku.edu, jamesj@acm.org
 * Address: EECS Department - University of Kansas
 *          Eaton Hall
 *          1520 W 15th St, Room 2001
 *          Lawrence, KS  66045-7621
 */
package jparse;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import jparse.expr.*;
import jparse.stmt.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/**
 * A Java 1.4 parser.  This parser is based on, but differs significantly
 * from, version 1.22 of the public domain ANTLR parser written by:
 *
 *		John Mitchell		johnm@non.net
 *		Terence Parr		parrt@magelang.com
 *		John Lilley			jlilley@empathy.com
 *		Scott Stanchfield	thetick@magelang.com
 *		Markus Mohnen       mohnen@informatik.rwth-aachen.de
 *      Peter Williams      pete.williams@sun.com
 *      Allan Jacobs        Allan.Jacobs@eng.sun.com
 *      Steve Messick       messick@redhills.com
 *      John Pybus			john@pybus.org
 *
 * Note that this parser does not suffer from the bugs identified on the ANTLR
 * mailing list for the public domain parser.
 */
public class JavaParser extends antlr.LLkParser       implements JavaTokenTypes
 {

	/**
	 * The file to read from.
	 */
	private File file;

	/**
	 * Set file to read from, and the reader to use for fetching lines from
	 * that file
	 *
	 * @param f the file to read from 
	 * @param in the reader
	 */
	void setFile(final File f) {
		file = f;
	}

	/**
	 * Report an error to the user in the format used by Sun's javac
	 *
	 * @param ex an exception detailing the problem
	 */
	public void reportError(final RecognitionException ex) {
		// Report the error itself
		System.err.println(ex.toString());
		//open a new BufferedReader to search for error line
		final BufferedReader error;
        try {
            error =
                new BufferedReader(new InputStreamReader
                                   (new FileInputStream(file), "ISO8859-1"));
        } catch (FileNotFoundException brex) {
            /* This cannot happen; we know the file exists */
            return;
        } catch (UnsupportedEncodingException encex) {
            /* This cannot happen; we used this encoding for input */
            return;
        }

		// Now search through read buffer and print the line containing the error
		String line = null;
        try {
            for(int j = 0; j < ex.line; j++)
                line = error.readLine();
            if (line != null) {
                System.err.println(line);
                for (int i = 0; i < ex.column - 1; i++)
                    System.err.print(' ');
                System.err.println('^');
            }
            error.close();
        } catch (IOException ioex) {
            // Do nothing; this should not be possible
        }
	}

	/**
	 * Report an error to the user in the format used by Sun's javac
	 *
	 * @param msg the message to print
	 */
	public void reportError(final String msg) {
		final String filename = getFilename();
		if (filename != null) {
			System.err.print(filename);
			System.err.print(": ");
		}
		System.err.print(msg);
	}

protected JavaParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public JavaParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected JavaParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public JavaParser(TokenStream lexer) {
  this(lexer,2);
}

public JavaParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void compilationUnit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST compilationUnit_AST = null;
		JavaAST im_AST = null;
		JavaAST typ_AST = null;
		
			ArrayList imports = new ArrayList();
			ArrayList types = new ArrayList();
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				FileAST.currFile = new FileAST(file);
			}
			{
			switch ( LA(1)) {
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
			_loop4:
			do {
				if ((LA(1)==LITERAL_import)) {
					importDefinition();
					im_AST = (JavaAST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						imports.add(((IdentifierAST)im_AST.getFirstChild()).getName());
					}
				}
				else {
					break _loop4;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
							FileAST.currFile.imports = new String[imports.size()];
							imports.toArray(FileAST.currFile.imports);
							imports = null;  // Feed the garbage collector
						
			}
			{
			_loop6:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					typeDefinition();
					typ_AST = (JavaAST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						if (typ_AST.getType() != SEMI) types.add(typ_AST);
					}
				}
				else {
					break _loop6;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
							FileAST.currFile.types = new jparse.TypeAST[types.size()];
							types.toArray(FileAST.currFile.types);
							types = null;  // Feed the garbage collector
						
			}
			match(Token.EOF_TYPE);
			if ( inputState.guessing==0 ) {
				compilationUnit_AST = (JavaAST)currentAST.root;
				compilationUnit_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(FileAST.currFile).add(compilationUnit_AST));
				currentAST.root = compilationUnit_AST;
				currentAST.child = compilationUnit_AST!=null &&compilationUnit_AST.getFirstChild()!=null ?
					compilationUnit_AST.getFirstChild() : compilationUnit_AST;
				currentAST.advanceChildToEnd();
			}
			compilationUnit_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		returnAST = compilationUnit_AST;
	}
	
	protected final void packageDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST packageDefinition_AST = null;
		JavaAST id_AST = null;
		
		try {      // for error handling
			JavaAST tmp2_AST = null;
			tmp2_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp2_AST);
			match(LITERAL_package);
			identifier();
			id_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			JavaAST tmp3_AST = null;
			tmp3_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp3_AST);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				FileAST.currFile.pkg = ((IdentifierAST)id_AST).getName();
			}
			packageDefinition_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = packageDefinition_AST;
	}
	
	protected final void importDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST importDefinition_AST = null;
		
		try {      // for error handling
			JavaAST tmp4_AST = null;
			tmp4_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp4_AST);
			match(LITERAL_import);
			identifierStar();
			astFactory.addASTChild(currentAST, returnAST);
			JavaAST tmp5_AST = null;
			tmp5_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp5_AST);
			match(SEMI);
			importDefinition_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = importDefinition_AST;
	}
	
	protected final void typeDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST typeDefinition_AST = null;
		JavaAST m_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
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
				modifiers();
				m_AST = (JavaAST)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_class:
				{
					classDefinition((ModifierAST)m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LITERAL_interface:
				{
					interfaceDefinition((ModifierAST)m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				typeDefinition_AST = (JavaAST)currentAST.root;
				break;
			}
			case SEMI:
			{
				JavaAST tmp6_AST = null;
				tmp6_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(SEMI);
				typeDefinition_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_3);
			} else {
			  throw ex;
			}
		}
		returnAST = typeDefinition_AST;
	}
	
	protected final void identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST identifier_AST = null;
		Token  id = null;
		jparse.expr.IdentifierAST id_AST = null;
		Token  d = null;
		jparse.expr.IdentifierAST d_AST = null;
		Token  i = null;
		JavaAST i_AST = null;
		StringBuffer buf = new StringBuffer();
		
		try {      // for error handling
			id = LT(1);
			id_AST = (jparse.expr.IdentifierAST)astFactory.create(id,"jparse.expr.IdentifierAST");
			astFactory.addASTChild(currentAST, id_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				buf.append(id_AST.getText());
			}
			{
			_loop11:
			do {
				if ((LA(1)==DOT)) {
					d = LT(1);
					d_AST = (jparse.expr.IdentifierAST)astFactory.create(d,"jparse.expr.IdentifierAST");
					astFactory.makeASTRoot(currentAST, d_AST);
					match(DOT);
					i = LT(1);
					i_AST = (JavaAST)astFactory.create(i);
					astFactory.addASTChild(currentAST, i_AST);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						buf.append(d_AST.getText()); buf.append(i_AST.getText());
					}
				}
				else {
					break _loop11;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				identifier_AST = (JavaAST)currentAST.root;
				((IdentifierAST)identifier_AST).setName(buf.toString());
			}
			identifier_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = identifier_AST;
	}
	
	protected final void identifierStar() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST identifierStar_AST = null;
		Token  id = null;
		jparse.expr.IdentifierAST id_AST = null;
		Token  d1 = null;
		jparse.expr.IdentifierAST d1_AST = null;
		Token  i = null;
		JavaAST i_AST = null;
		Token  d2 = null;
		jparse.expr.IdentifierAST d2_AST = null;
		Token  s = null;
		JavaAST s_AST = null;
		StringBuffer buf = new StringBuffer();
		
		try {      // for error handling
			id = LT(1);
			id_AST = (jparse.expr.IdentifierAST)astFactory.create(id,"jparse.expr.IdentifierAST");
			astFactory.addASTChild(currentAST, id_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				buf.append(id_AST.getText());
			}
			{
			_loop14:
			do {
				if ((LA(1)==DOT) && (LA(2)==IDENT)) {
					d1 = LT(1);
					d1_AST = (jparse.expr.IdentifierAST)astFactory.create(d1,"jparse.expr.IdentifierAST");
					astFactory.makeASTRoot(currentAST, d1_AST);
					match(DOT);
					i = LT(1);
					i_AST = (JavaAST)astFactory.create(i);
					astFactory.addASTChild(currentAST, i_AST);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						buf.append(d1_AST.getText()); buf.append(i_AST.getText());
					}
				}
				else {
					break _loop14;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case DOT:
			{
				d2 = LT(1);
				d2_AST = (jparse.expr.IdentifierAST)astFactory.create(d2,"jparse.expr.IdentifierAST");
				astFactory.makeASTRoot(currentAST, d2_AST);
				match(DOT);
				s = LT(1);
				s_AST = (JavaAST)astFactory.create(s);
				astFactory.addASTChild(currentAST, s_AST);
				match(STAR);
				if ( inputState.guessing==0 ) {
					buf.append(d2_AST.getText()); buf.append(s_AST.getText());
				}
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				identifierStar_AST = (JavaAST)currentAST.root;
				((IdentifierAST)identifierStar_AST).setName(buf.toString());
			}
			identifierStar_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_5);
			} else {
			  throw ex;
			}
		}
		returnAST = identifierStar_AST;
	}
	
	protected final void modifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST modifiers_AST = null;
		int m, mods = 0;
		
		try {      // for error handling
			{
			_loop20:
			do {
				if (((LA(1) >= LITERAL_public && LA(1) <= LITERAL_strictfp))) {
					m=modifier();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						mods |= m;
					}
				}
				else {
					break _loop20;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				modifiers_AST = (JavaAST)currentAST.root;
				
							AST mod = new ModifierAST(mods);
							modifiers_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(mod).add(modifiers_AST));
						
				currentAST.root = modifiers_AST;
				currentAST.child = modifiers_AST!=null &&modifiers_AST.getFirstChild()!=null ?
					modifiers_AST.getFirstChild() : modifiers_AST;
				currentAST.advanceChildToEnd();
			}
			modifiers_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = modifiers_AST;
	}
	
	protected final void classDefinition(
		ModifierAST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST classDefinition_AST = null;
		Token  c = null;
		jparse.TypeAST c_AST = null;
		Token  id = null;
		jparse.expr.IdentifierAST id_AST = null;
		jparse.TypeAST oldType = jparse.TypeAST.currType;
		
		try {      // for error handling
			c = LT(1);
			c_AST = (jparse.TypeAST)astFactory.create(c,"jparse.TypeAST");
			match(LITERAL_class);
			id = LT(1);
			id_AST = (jparse.expr.IdentifierAST)astFactory.create(id,"jparse.expr.IdentifierAST");
			astFactory.addASTChild(currentAST, id_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				
							final String newName = (oldType == null)
								? id_AST.getText()
								: oldType.name + '$' + id_AST.getText();
							c_AST.setInfo(FileAST.currFile.pkg, newName, oldType, modifiers);
							jparse.TypeAST.currType = c_AST;
						
			}
			{
			switch ( LA(1)) {
			case LITERAL_extends:
			{
				superClassClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_implements:
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
			implementsClause();
			astFactory.addASTChild(currentAST, returnAST);
			classBlock();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				classDefinition_AST = (JavaAST)currentAST.root;
				
							classDefinition_AST = (JavaAST)astFactory.make( (new ASTArray(3)).add(c_AST).add(modifiers).add(classDefinition_AST));
							if (oldType != null) {
								oldType.addInner(jparse.TypeAST.currType);
							}
							jparse.TypeAST.currType = oldType;
							JavaAST.currSymTable = JavaAST.currSymTable.parent;
						
				currentAST.root = classDefinition_AST;
				currentAST.child = classDefinition_AST!=null &&classDefinition_AST.getFirstChild()!=null ?
					classDefinition_AST.getFirstChild() : classDefinition_AST;
				currentAST.advanceChildToEnd();
			}
			classDefinition_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_7);
			} else {
			  throw ex;
			}
		}
		returnAST = classDefinition_AST;
	}
	
	protected final void interfaceDefinition(
		ModifierAST modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST interfaceDefinition_AST = null;
		Token  i = null;
		jparse.TypeAST i_AST = null;
		Token  id = null;
		jparse.expr.IdentifierAST id_AST = null;
		jparse.TypeAST oldType = jparse.TypeAST.currType;
		
		try {      // for error handling
			i = LT(1);
			i_AST = (jparse.TypeAST)astFactory.create(i,"jparse.TypeAST");
			match(LITERAL_interface);
			id = LT(1);
			id_AST = (jparse.expr.IdentifierAST)astFactory.create(id,"jparse.expr.IdentifierAST");
			astFactory.addASTChild(currentAST, id_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				
							final String newName = (oldType == null)
								? id_AST.getText()
								: oldType.name + '$' + id_AST.getText();
							i_AST.setInfo(FileAST.currFile.pkg, newName, oldType, modifiers);
							jparse.TypeAST.currType = i_AST; // TODO: Can this ever be non-null??
						
			}
			interfaceExtends();
			astFactory.addASTChild(currentAST, returnAST);
			classBlock();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				interfaceDefinition_AST = (JavaAST)currentAST.root;
				
							modifiers.setInterface();
							interfaceDefinition_AST = (JavaAST)astFactory.make( (new ASTArray(3)).add(i_AST).add(modifiers).add(interfaceDefinition_AST));
							if (oldType != null) {
								oldType.addInner(jparse.TypeAST.currType);
							}
							jparse.TypeAST.currType = oldType;
							JavaAST.currSymTable = JavaAST.currSymTable.parent;
						
				currentAST.root = interfaceDefinition_AST;
				currentAST.child = interfaceDefinition_AST!=null &&interfaceDefinition_AST.getFirstChild()!=null ?
					interfaceDefinition_AST.getFirstChild() : interfaceDefinition_AST;
				currentAST.advanceChildToEnd();
			}
			interfaceDefinition_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		returnAST = interfaceDefinition_AST;
	}
	
	protected final int  modifier() throws RecognitionException, TokenStreamException {
		int value;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST modifier_AST = null;
		value = 0;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_public:
			{
				JavaAST tmp7_AST = null;
				tmp7_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp7_AST);
				match(LITERAL_public);
				if ( inputState.guessing==0 ) {
					value = Modifier.PUBLIC;			
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_private:
			{
				JavaAST tmp8_AST = null;
				tmp8_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
				match(LITERAL_private);
				if ( inputState.guessing==0 ) {
					value = Modifier.PRIVATE;			
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_protected:
			{
				JavaAST tmp9_AST = null;
				tmp9_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(LITERAL_protected);
				if ( inputState.guessing==0 ) {
					value = Modifier.PROTECTED;		
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_static:
			{
				JavaAST tmp10_AST = null;
				tmp10_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(LITERAL_static);
				if ( inputState.guessing==0 ) {
					value = Modifier.STATIC;			
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_final:
			{
				JavaAST tmp11_AST = null;
				tmp11_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(LITERAL_final);
				if ( inputState.guessing==0 ) {
					value = Modifier.FINAL;			
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_synchronized:
			{
				JavaAST tmp12_AST = null;
				tmp12_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(LITERAL_synchronized);
				if ( inputState.guessing==0 ) {
					value = Modifier.SYNCHRONIZED;	
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_volatile:
			{
				JavaAST tmp13_AST = null;
				tmp13_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp13_AST);
				match(LITERAL_volatile);
				if ( inputState.guessing==0 ) {
					value = Modifier.VOLATILE;		
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_transient:
			{
				JavaAST tmp14_AST = null;
				tmp14_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp14_AST);
				match(LITERAL_transient);
				if ( inputState.guessing==0 ) {
					value = Modifier.TRANSIENT;		
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_native:
			{
				JavaAST tmp15_AST = null;
				tmp15_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp15_AST);
				match(LITERAL_native);
				if ( inputState.guessing==0 ) {
					value = Modifier.NATIVE;			
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_abstract:
			{
				JavaAST tmp16_AST = null;
				tmp16_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp16_AST);
				match(LITERAL_abstract);
				if ( inputState.guessing==0 ) {
					value = Modifier.ABSTRACT;		
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_strictfp:
			{
				JavaAST tmp17_AST = null;
				tmp17_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp17_AST);
				match(LITERAL_strictfp);
				if ( inputState.guessing==0 ) {
					value = Modifier.STRICT;			
				}
				modifier_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = modifier_AST;
		return value;
	}
	
	protected final void superClassClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST superClassClause_AST = null;
		JavaAST id_AST = null;
		
		try {      // for error handling
			JavaAST tmp18_AST = null;
			tmp18_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp18_AST);
			match(LITERAL_extends);
			identifier();
			id_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				jparse.TypeAST.currType.superclass =
							((IdentifierAST)id_AST).getName();
			}
			superClassClause_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = superClassClause_AST;
	}
	
	protected final void implementsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST implementsClause_AST = null;
		JavaAST id1_AST = null;
		JavaAST id2_AST = null;
		ArrayList idents = new ArrayList();
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_implements:
			{
				JavaAST tmp19_AST = null;
				tmp19_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp19_AST);
				match(LITERAL_implements);
				identifier();
				id1_AST = (JavaAST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					idents.add(((IdentifierAST)id1_AST).getName());
				}
				{
				_loop28:
				do {
					if ((LA(1)==COMMA)) {
						JavaAST tmp20_AST = null;
						tmp20_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp20_AST);
						match(COMMA);
						identifier();
						id2_AST = (JavaAST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							idents.add(((IdentifierAST)id2_AST).getName());
						}
					}
					else {
						break _loop28;
					}
					
				} while (true);
				}
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
			if ( inputState.guessing==0 ) {
				
							jparse.TypeAST.currType.interfaces = new String[idents.size()];
							idents.toArray(jparse.TypeAST.currType.interfaces);
						
			}
			implementsClause_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = implementsClause_AST;
	}
	
	protected final void classBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST classBlock_AST = null;
		Token  lc = null;
		JavaAST lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (JavaAST)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(OBJBLOCK);
			}
			{
			_loop36:
			do {
				if ((_tokenSet_12.member(LA(1)))) {
					field();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop36;
				}
				
			} while (true);
			}
			JavaAST tmp21_AST = null;
			tmp21_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp21_AST);
			match(RCURLY);
			classBlock_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = classBlock_AST;
	}
	
	protected final void interfaceExtends() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST interfaceExtends_AST = null;
		JavaAST id1_AST = null;
		JavaAST id2_AST = null;
		final ArrayList idents = new ArrayList();
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_extends:
			{
				JavaAST tmp22_AST = null;
				tmp22_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp22_AST);
				match(LITERAL_extends);
				identifier();
				id1_AST = (JavaAST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					idents.add(((IdentifierAST)id1_AST).getName());
				}
				{
				_loop33:
				do {
					if ((LA(1)==COMMA)) {
						JavaAST tmp23_AST = null;
						tmp23_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp23_AST);
						match(COMMA);
						identifier();
						id2_AST = (JavaAST)returnAST;
						astFactory.addASTChild(currentAST, returnAST);
						if ( inputState.guessing==0 ) {
							idents.add(((IdentifierAST)id2_AST).getName());
						}
					}
					else {
						break _loop33;
					}
					
				} while (true);
				}
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
			if ( inputState.guessing==0 ) {
				
							final int size = idents.size();
							jparse.TypeAST.currType.interfaces = new String[size];
							if (size > 0)
								idents.toArray(jparse.TypeAST.currType.interfaces);
						
			}
			interfaceExtends_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = interfaceExtends_AST;
	}
	
	protected final void field() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST field_AST = null;
		JavaAST mods_AST = null;
		JavaAST cparams_AST = null;
		JavaAST cthrows_AST = null;
		JavaAST cbody_AST = null;
		JavaAST t_AST = null;
		Token  name = null;
		jparse.expr.IdentifierAST name_AST = null;
		JavaAST mparams_AST = null;
		JavaAST d_AST = null;
		JavaAST mthrows_AST = null;
		JavaAST mbody_AST = null;
		JavaAST vars_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LCURLY:
			{
				compoundStatement();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					field_AST = (JavaAST)currentAST.root;
					field_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add((JavaAST)astFactory.create(INSTANCE_INIT,"INSTANCE_INIT")).add(field_AST));
					currentAST.root = field_AST;
					currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
						field_AST.getFirstChild() : field_AST;
					currentAST.advanceChildToEnd();
				}
				field_AST = (JavaAST)currentAST.root;
				break;
			}
			case SEMI:
			{
				JavaAST tmp24_AST = null;
				tmp24_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp24_AST);
				match(SEMI);
				field_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
				if ((_tokenSet_9.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
					modifiers();
					mods_AST = (JavaAST)returnAST;
					{
					switch ( LA(1)) {
					case LITERAL_class:
					{
						classDefinition((ModifierAST)mods_AST);
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case LITERAL_interface:
					{
						interfaceDefinition((ModifierAST)mods_AST);
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					default:
						if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
							if ( inputState.guessing==0 ) {
								JavaAST.currSymTable = new SymbolTable();
							}
							jparse.expr.IdentifierAST tmp25_AST = null;
							tmp25_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
							astFactory.addASTChild(currentAST, tmp25_AST);
							match(IDENT);
							JavaAST tmp26_AST = null;
							tmp26_AST = (JavaAST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp26_AST);
							match(LPAREN);
							parameterDeclarationList();
							cparams_AST = (JavaAST)returnAST;
							astFactory.addASTChild(currentAST, returnAST);
							JavaAST tmp27_AST = null;
							tmp27_AST = (JavaAST)astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp27_AST);
							match(RPAREN);
							{
							switch ( LA(1)) {
							case LITERAL_throws:
							{
								throwsClause();
								cthrows_AST = (JavaAST)returnAST;
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
							cbody_AST = (JavaAST)returnAST;
							astFactory.addASTChild(currentAST, returnAST);
							if ( inputState.guessing==0 ) {
								field_AST = (JavaAST)currentAST.root;
								
												JavaAST.currSymTable = JavaAST.currSymTable.parent;
												final ConstrAST constr = 
													new ConstrAST((ModifierAST)mods_AST, cparams_AST, cthrows_AST,
														(CompoundAST)cbody_AST);
												field_AST = (JavaAST)astFactory.make( (new ASTArray(3)).add(constr).add(mods_AST).add(field_AST));
											
								currentAST.root = field_AST;
								currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
									field_AST.getFirstChild() : field_AST;
								currentAST.advanceChildToEnd();
							}
						}
						else if ((_tokenSet_15.member(LA(1))) && (_tokenSet_16.member(LA(2)))) {
							typeSpec();
							t_AST = (JavaAST)returnAST;
							astFactory.addASTChild(currentAST, returnAST);
							{
							if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
								name = LT(1);
								name_AST = (jparse.expr.IdentifierAST)astFactory.create(name,"jparse.expr.IdentifierAST");
								astFactory.addASTChild(currentAST, name_AST);
								match(IDENT);
								if ( inputState.guessing==0 ) {
									JavaAST.currSymTable = new SymbolTable();
								}
								JavaAST tmp28_AST = null;
								tmp28_AST = (JavaAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp28_AST);
								match(LPAREN);
								parameterDeclarationList();
								mparams_AST = (JavaAST)returnAST;
								astFactory.addASTChild(currentAST, returnAST);
								JavaAST tmp29_AST = null;
								tmp29_AST = (JavaAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp29_AST);
								match(RPAREN);
								declaratorBrackets();
								d_AST = (JavaAST)returnAST;
								astFactory.addASTChild(currentAST, returnAST);
								{
								switch ( LA(1)) {
								case LITERAL_throws:
								{
									throwsClause();
									mthrows_AST = (JavaAST)returnAST;
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
								switch ( LA(1)) {
								case LCURLY:
								{
									compoundStatement();
									mbody_AST = (JavaAST)returnAST;
									astFactory.addASTChild(currentAST, returnAST);
									break;
								}
								case SEMI:
								{
									JavaAST tmp30_AST = null;
									tmp30_AST = (JavaAST)astFactory.create(LT(1));
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
								if ( inputState.guessing==0 ) {
									field_AST = (JavaAST)currentAST.root;
									
														JavaAST.currSymTable = JavaAST.currSymTable.parent;
														final ModifierAST mod = (ModifierAST)mods_AST;
														if (jparse.TypeAST.currType.modifiers.isInterface())
															mod.setInterfaceMethod();
														final MethAST meth =
															new MethAST(mod, (jparse.expr.TypeAST)t_AST, name_AST,
																mparams_AST, d_AST, mthrows_AST, (CompoundAST)mbody_AST);
														field_AST = (JavaAST)astFactory.make( (new ASTArray(3)).add(meth).add(mod).add(field_AST));
													
									currentAST.root = field_AST;
									currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
										field_AST.getFirstChild() : field_AST;
									currentAST.advanceChildToEnd();
								}
							}
							else if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
								variableDefinitions();
								vars_AST = (JavaAST)returnAST;
								astFactory.addASTChild(currentAST, returnAST);
								JavaAST tmp31_AST = null;
								tmp31_AST = (JavaAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp31_AST);
								match(SEMI);
								if ( inputState.guessing==0 ) {
									field_AST = (JavaAST)currentAST.root;
									
														final DeclarationAST decl =
															new DeclarationAST((ModifierAST)mods_AST,
																			   (jparse.expr.TypeAST)t_AST, vars_AST);
														field_AST = (JavaAST)astFactory.make( (new ASTArray(3)).add(decl).add(mods_AST).add(field_AST));
													
									currentAST.root = field_AST;
									currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
										field_AST.getFirstChild() : field_AST;
									currentAST.advanceChildToEnd();
								}
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}
							
							}
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					field_AST = (JavaAST)currentAST.root;
				}
				else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
					JavaAST tmp32_AST = null;
					tmp32_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp32_AST);
					match(LITERAL_static);
					compoundStatement();
					astFactory.addASTChild(currentAST, returnAST);
					field_AST = (JavaAST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_18);
			} else {
			  throw ex;
			}
		}
		returnAST = field_AST;
	}
	
	protected final void parameterDeclarationList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST parameterDeclarationList_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
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
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				parameterDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop74:
				do {
					if ((LA(1)==COMMA)) {
						JavaAST tmp33_AST = null;
						tmp33_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp33_AST);
						match(COMMA);
						parameterDeclaration();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop74;
					}
					
				} while (true);
				}
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				parameterDeclarationList_AST = (JavaAST)currentAST.root;
				parameterDeclarationList_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add((JavaAST)astFactory.create(PARAMETERS,"PARAMETERS")).add(parameterDeclarationList_AST));
				currentAST.root = parameterDeclarationList_AST;
				currentAST.child = parameterDeclarationList_AST!=null &&parameterDeclarationList_AST.getFirstChild()!=null ?
					parameterDeclarationList_AST.getFirstChild() : parameterDeclarationList_AST;
				currentAST.advanceChildToEnd();
			}
			parameterDeclarationList_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclarationList_AST;
	}
	
	protected final void throwsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST throwsClause_AST = null;
		
		try {      // for error handling
			JavaAST tmp34_AST = null;
			tmp34_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp34_AST);
			match(LITERAL_throws);
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop45:
			do {
				if ((LA(1)==COMMA)) {
					JavaAST tmp35_AST = null;
					tmp35_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp35_AST);
					match(COMMA);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop45;
				}
				
			} while (true);
			}
			throwsClause_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_20);
			} else {
			  throw ex;
			}
		}
		returnAST = throwsClause_AST;
	}
	
	protected final void compoundStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST compoundStatement_AST = null;
		
		try {      // for error handling
			jparse.stmt.CompoundAST tmp36_AST = null;
			tmp36_AST = (jparse.stmt.CompoundAST)astFactory.create(LT(1),"jparse.stmt.CompoundAST");
			astFactory.makeASTRoot(currentAST, tmp36_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				JavaAST.currSymTable = new SymbolTable();
			}
			{
			_loop78:
			do {
				if ((_tokenSet_21.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop78;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				JavaAST.currSymTable = JavaAST.currSymTable.parent;
			}
			JavaAST tmp37_AST = null;
			tmp37_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp37_AST);
			match(RCURLY);
			compoundStatement_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_22);
			} else {
			  throw ex;
			}
		}
		returnAST = compoundStatement_AST;
	}
	
	protected final void typeSpec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST typeSpec_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				classTypeSpec();
				astFactory.addASTChild(currentAST, returnAST);
				typeSpec_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				builtInTypeSpec();
				astFactory.addASTChild(currentAST, returnAST);
				typeSpec_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = typeSpec_AST;
	}
	
	protected final void declaratorBrackets() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST declaratorBrackets_AST = null;
		Token  lb = null;
		JavaAST lb_AST = null;
		
		try {      // for error handling
			{
			_loop62:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (JavaAST)astFactory.create(lb);
					astFactory.addASTChild(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(ARRAY_DECLARATOR);
					}
					JavaAST tmp38_AST = null;
					tmp38_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp38_AST);
					match(RBRACK);
				}
				else {
					break _loop62;
				}
				
			} while (true);
			}
			declaratorBrackets_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_24);
			} else {
			  throw ex;
			}
		}
		returnAST = declaratorBrackets_AST;
	}
	
	protected final void variableDefinitions() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST variableDefinitions_AST = null;
		
		try {      // for error handling
			variableDeclarator();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop57:
			do {
				if ((LA(1)==COMMA)) {
					JavaAST tmp39_AST = null;
					tmp39_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp39_AST);
					match(COMMA);
					variableDeclarator();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop57;
				}
				
			} while (true);
			}
			variableDefinitions_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_5);
			} else {
			  throw ex;
			}
		}
		returnAST = variableDefinitions_AST;
	}
	
	protected final void classTypeSpec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST classTypeSpec_AST = null;
		Token  lb = null;
		jparse.expr.IdentifierAST lb_AST = null;
		
		try {      // for error handling
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop49:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (jparse.expr.IdentifierAST)astFactory.create(lb,"jparse.expr.IdentifierAST");
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						
										lb_AST.setType(ARRAY_DECLARATOR);
										lb_AST.setName(((IdentifierAST)lb_AST.getFirstChild()).getName() +
													"[]");
									
					}
					JavaAST tmp40_AST = null;
					tmp40_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp40_AST);
					match(RBRACK);
				}
				else {
					break _loop49;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				classTypeSpec_AST = (JavaAST)currentAST.root;
				
							jparse.expr.TypeAST t =
								new jparse.expr.TypeAST(((IdentifierAST)classTypeSpec_AST)
														.getName());
							classTypeSpec_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(t).add(classTypeSpec_AST));
						
				currentAST.root = classTypeSpec_AST;
				currentAST.child = classTypeSpec_AST!=null &&classTypeSpec_AST.getFirstChild()!=null ?
					classTypeSpec_AST.getFirstChild() : classTypeSpec_AST;
				currentAST.advanceChildToEnd();
			}
			classTypeSpec_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = classTypeSpec_AST;
	}
	
	protected final void builtInTypeSpec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST builtInTypeSpec_AST = null;
		Token  lb = null;
		jparse.expr.IdentifierAST lb_AST = null;
		
		try {      // for error handling
			builtInType();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop52:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (jparse.expr.IdentifierAST)astFactory.create(lb,"jparse.expr.IdentifierAST");
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						
										lb_AST.setType(ARRAY_DECLARATOR);
										lb_AST.setName(((IdentifierAST)lb_AST.getFirstChild()).getName() +
													"[]");
									
					}
					JavaAST tmp41_AST = null;
					tmp41_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp41_AST);
					match(RBRACK);
				}
				else {
					break _loop52;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				builtInTypeSpec_AST = (JavaAST)currentAST.root;
				
							jparse.expr.TypeAST t =
								new jparse.expr.TypeAST(((IdentifierAST)builtInTypeSpec_AST)
														.getName());
							builtInTypeSpec_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(t).add(builtInTypeSpec_AST));
						
				currentAST.root = builtInTypeSpec_AST;
				currentAST.child = builtInTypeSpec_AST!=null &&builtInTypeSpec_AST.getFirstChild()!=null ?
					builtInTypeSpec_AST.getFirstChild() : builtInTypeSpec_AST;
				currentAST.advanceChildToEnd();
			}
			builtInTypeSpec_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = builtInTypeSpec_AST;
	}
	
	protected final void builtInType() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST builtInType_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_void:
			{
				jparse.expr.IdentifierAST tmp42_AST = null;
				tmp42_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(LITERAL_void);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_boolean:
			{
				jparse.expr.IdentifierAST tmp43_AST = null;
				tmp43_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp43_AST);
				match(LITERAL_boolean);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_byte:
			{
				jparse.expr.IdentifierAST tmp44_AST = null;
				tmp44_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(LITERAL_byte);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_char:
			{
				jparse.expr.IdentifierAST tmp45_AST = null;
				tmp45_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(LITERAL_char);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_short:
			{
				jparse.expr.IdentifierAST tmp46_AST = null;
				tmp46_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(LITERAL_short);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_int:
			{
				jparse.expr.IdentifierAST tmp47_AST = null;
				tmp47_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp47_AST);
				match(LITERAL_int);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_float:
			{
				jparse.expr.IdentifierAST tmp48_AST = null;
				tmp48_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp48_AST);
				match(LITERAL_float);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_long:
			{
				jparse.expr.IdentifierAST tmp49_AST = null;
				tmp49_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp49_AST);
				match(LITERAL_long);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_double:
			{
				jparse.expr.IdentifierAST tmp50_AST = null;
				tmp50_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp50_AST);
				match(LITERAL_double);
				builtInType_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_25);
			} else {
			  throw ex;
			}
		}
		returnAST = builtInType_AST;
	}
	
	protected final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST type_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				type_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				builtInType();
				astFactory.addASTChild(currentAST, returnAST);
				type_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
		returnAST = type_AST;
	}
	
	protected final void variableDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST variableDeclarator_AST = null;
		
		try {      // for error handling
			jparse.expr.VarAST tmp51_AST = null;
			tmp51_AST = (jparse.expr.VarAST)astFactory.create(LT(1),"jparse.expr.VarAST");
			astFactory.addASTChild(currentAST, tmp51_AST);
			match(IDENT);
			declaratorBrackets();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case ASSIGN:
			{
				varInitializer();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case COMMA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				variableDeclarator_AST = (JavaAST)currentAST.root;
				variableDeclarator_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add((JavaAST)astFactory.create(VARIABLE_DEF,"VARIABLE_DEF")).add(variableDeclarator_AST));
				currentAST.root = variableDeclarator_AST;
				currentAST.child = variableDeclarator_AST!=null &&variableDeclarator_AST.getFirstChild()!=null ?
					variableDeclarator_AST.getFirstChild() : variableDeclarator_AST;
				currentAST.advanceChildToEnd();
			}
			variableDeclarator_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_27);
			} else {
			  throw ex;
			}
		}
		returnAST = variableDeclarator_AST;
	}
	
	protected final void varInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST varInitializer_AST = null;
		
		try {      // for error handling
			jparse.expr.InitializerAST tmp52_AST = null;
			tmp52_AST = (jparse.expr.InitializerAST)astFactory.create(LT(1),"jparse.expr.InitializerAST");
			astFactory.makeASTRoot(currentAST, tmp52_AST);
			match(ASSIGN);
			initializer();
			astFactory.addASTChild(currentAST, returnAST);
			varInitializer_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_27);
			} else {
			  throw ex;
			}
		}
		returnAST = varInitializer_AST;
	}
	
	protected final void initializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST initializer_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (JavaAST)currentAST.root;
				break;
			}
			case LCURLY:
			{
				arrayInitializer();
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_28);
			} else {
			  throw ex;
			}
		}
		returnAST = initializer_AST;
	}
	
	protected final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST expression_AST = null;
		
		try {      // for error handling
			assignmentExpression();
			astFactory.addASTChild(currentAST, returnAST);
			expression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_29);
			} else {
			  throw ex;
			}
		}
		returnAST = expression_AST;
	}
	
	protected final void arrayInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST arrayInitializer_AST = null;
		
		try {      // for error handling
			jparse.expr.ArrayInitAST tmp53_AST = null;
			tmp53_AST = (jparse.expr.ArrayInitAST)astFactory.create(LT(1),"jparse.expr.ArrayInitAST");
			astFactory.makeASTRoot(currentAST, tmp53_AST);
			match(LCURLY);
			{
			switch ( LA(1)) {
			case IDENT:
			case LCURLY:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					commaInitializer();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			JavaAST tmp54_AST = null;
			tmp54_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp54_AST);
			match(RCURLY);
			arrayInitializer_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = arrayInitializer_AST;
	}
	
	protected final void commaInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST commaInitializer_AST = null;
		
		try {      // for error handling
			JavaAST tmp55_AST = null;
			tmp55_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp55_AST);
			match(COMMA);
			{
			switch ( LA(1)) {
			case IDENT:
			case LCURLY:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					commaInitializer();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			commaInitializer_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_31);
			} else {
			  throw ex;
			}
		}
		returnAST = commaInitializer_AST;
	}
	
	protected final void parameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST parameterDeclaration_AST = null;
		JavaAST m_AST = null;
		JavaAST t_AST = null;
		Token  v = null;
		jparse.expr.VarAST v_AST = null;
		
		try {      // for error handling
			modifiers();
			m_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec();
			t_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			v = LT(1);
			v_AST = (jparse.expr.VarAST)astFactory.create(v,"jparse.expr.VarAST");
			astFactory.addASTChild(currentAST, v_AST);
			match(IDENT);
			declaratorBrackets();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				parameterDeclaration_AST = (JavaAST)currentAST.root;
				
							final ParameterAST param = new ParameterAST((ModifierAST)m_AST,
														(jparse.expr.TypeAST)t_AST, v_AST);
							parameterDeclaration_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(param).add(parameterDeclaration_AST));
						
				currentAST.root = parameterDeclaration_AST;
				currentAST.child = parameterDeclaration_AST!=null &&parameterDeclaration_AST.getFirstChild()!=null ?
					parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
				currentAST.advanceChildToEnd();
			}
			parameterDeclaration_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_32);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclaration_AST;
	}
	
	protected final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST statement_AST = null;
		JavaAST mods_AST = null;
		JavaAST def_AST = null;
		JavaAST e_AST = null;
		Token  label = null;
		jparse.stmt.LabelAST label_AST = null;
		JavaAST st_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LCURLY:
			{
				compoundStatement();
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_if:
			{
				jparse.stmt.IfElseAST tmp56_AST = null;
				tmp56_AST = (jparse.stmt.IfElseAST)astFactory.create(LT(1),"jparse.stmt.IfElseAST");
				astFactory.makeASTRoot(currentAST, tmp56_AST);
				match(LITERAL_if);
				JavaAST tmp57_AST = null;
				tmp57_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp57_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp58_AST = null;
				tmp58_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(RPAREN);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				{
				if ((LA(1)==LITERAL_else) && (_tokenSet_21.member(LA(2)))) {
					JavaAST tmp59_AST = null;
					tmp59_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp59_AST);
					match(LITERAL_else);
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_33.member(LA(1))) && (_tokenSet_34.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_for:
			{
				jparse.stmt.ForAST tmp60_AST = null;
				tmp60_AST = (jparse.stmt.ForAST)astFactory.create(LT(1),"jparse.stmt.ForAST");
				astFactory.makeASTRoot(currentAST, tmp60_AST);
				match(LITERAL_for);
				if ( inputState.guessing==0 ) {
					JavaAST.currSymTable = new SymbolTable();
				}
				JavaAST tmp61_AST = null;
				tmp61_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp61_AST);
				match(LPAREN);
				forInit();
				astFactory.addASTChild(currentAST, returnAST);
				forCond();
				astFactory.addASTChild(currentAST, returnAST);
				forIter();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp62_AST = null;
				tmp62_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp62_AST);
				match(RPAREN);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					JavaAST.currSymTable = JavaAST.currSymTable.parent;
				}
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_while:
			{
				jparse.stmt.WhileAST tmp63_AST = null;
				tmp63_AST = (jparse.stmt.WhileAST)astFactory.create(LT(1),"jparse.stmt.WhileAST");
				astFactory.makeASTRoot(currentAST, tmp63_AST);
				match(LITERAL_while);
				JavaAST tmp64_AST = null;
				tmp64_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp65_AST = null;
				tmp65_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp65_AST);
				match(RPAREN);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_do:
			{
				jparse.stmt.DoWhileAST tmp66_AST = null;
				tmp66_AST = (jparse.stmt.DoWhileAST)astFactory.create(LT(1),"jparse.stmt.DoWhileAST");
				astFactory.makeASTRoot(currentAST, tmp66_AST);
				match(LITERAL_do);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp67_AST = null;
				tmp67_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp67_AST);
				match(LITERAL_while);
				JavaAST tmp68_AST = null;
				tmp68_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp68_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp69_AST = null;
				tmp69_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(RPAREN);
				JavaAST tmp70_AST = null;
				tmp70_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp70_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_break:
			{
				jparse.stmt.BreakAST tmp71_AST = null;
				tmp71_AST = (jparse.stmt.BreakAST)astFactory.create(LT(1),"jparse.stmt.BreakAST");
				astFactory.makeASTRoot(currentAST, tmp71_AST);
				match(LITERAL_break);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					jparse.expr.IdentifierAST tmp72_AST = null;
					tmp72_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
					astFactory.addASTChild(currentAST, tmp72_AST);
					match(IDENT);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaAST tmp73_AST = null;
				tmp73_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_continue:
			{
				jparse.stmt.ContinueAST tmp74_AST = null;
				tmp74_AST = (jparse.stmt.ContinueAST)astFactory.create(LT(1),"jparse.stmt.ContinueAST");
				astFactory.makeASTRoot(currentAST, tmp74_AST);
				match(LITERAL_continue);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					jparse.expr.IdentifierAST tmp75_AST = null;
					tmp75_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
					astFactory.addASTChild(currentAST, tmp75_AST);
					match(IDENT);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaAST tmp76_AST = null;
				tmp76_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_return:
			{
				jparse.stmt.ReturnAST tmp77_AST = null;
				tmp77_AST = (jparse.stmt.ReturnAST)astFactory.create(LT(1),"jparse.stmt.ReturnAST");
				astFactory.makeASTRoot(currentAST, tmp77_AST);
				match(LITERAL_return);
				{
				switch ( LA(1)) {
				case IDENT:
				case LPAREN:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_this:
				case LITERAL_super:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_null:
				case LITERAL_new:
				case NUM_INT:
				case CHAR_LITERAL:
				case STRING_LITERAL:
				case NUM_FLOAT:
				{
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaAST tmp78_AST = null;
				tmp78_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp78_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_switch:
			{
				jparse.stmt.SwitchAST tmp79_AST = null;
				tmp79_AST = (jparse.stmt.SwitchAST)astFactory.create(LT(1),"jparse.stmt.SwitchAST");
				astFactory.makeASTRoot(currentAST, tmp79_AST);
				match(LITERAL_switch);
				JavaAST tmp80_AST = null;
				tmp80_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp80_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp81_AST = null;
				tmp81_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp81_AST);
				match(RPAREN);
				JavaAST tmp82_AST = null;
				tmp82_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp82_AST);
				match(LCURLY);
				{
				_loop87:
				do {
					if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default)) {
						casesGroup();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop87;
					}
					
				} while (true);
				}
				JavaAST tmp83_AST = null;
				tmp83_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp83_AST);
				match(RCURLY);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_try:
			{
				tryBlock();
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_throw:
			{
				jparse.stmt.ThrowAST tmp84_AST = null;
				tmp84_AST = (jparse.stmt.ThrowAST)astFactory.create(LT(1),"jparse.stmt.ThrowAST");
				astFactory.makeASTRoot(currentAST, tmp84_AST);
				match(LITERAL_throw);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp85_AST = null;
				tmp85_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp85_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_assert:
			{
				jparse.stmt.AssertAST tmp86_AST = null;
				tmp86_AST = (jparse.stmt.AssertAST)astFactory.create(LT(1),"jparse.stmt.AssertAST");
				astFactory.makeASTRoot(currentAST, tmp86_AST);
				match(LITERAL_assert);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COLON:
				{
					JavaAST tmp87_AST = null;
					tmp87_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp87_AST);
					match(COLON);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaAST tmp88_AST = null;
				tmp88_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp88_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			case SEMI:
			{
				jparse.stmt.EmptyAST tmp89_AST = null;
				tmp89_AST = (jparse.stmt.EmptyAST)astFactory.create(LT(1),"jparse.stmt.EmptyAST");
				astFactory.addASTChild(currentAST, tmp89_AST);
				match(SEMI);
				statement_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched81 = false;
				if (((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2))))) {
					int _m81 = mark();
					synPredMatched81 = true;
					inputState.guessing++;
					try {
						{
						modifiers();
						typeSpec();
						match(IDENT);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched81 = false;
					}
					rewind(_m81);
					inputState.guessing--;
				}
				if ( synPredMatched81 ) {
					declaration();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (JavaAST)currentAST.root;
				}
				else if (((LA(1) >= LITERAL_public && LA(1) <= LITERAL_class)) && (_tokenSet_37.member(LA(2)))) {
					modifiers();
					mods_AST = (JavaAST)returnAST;
					classDefinition((ModifierAST)mods_AST);
					def_AST = (JavaAST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						statement_AST = (JavaAST)currentAST.root;
						
									final ClassAST type = new ClassAST((TypeAST)def_AST);
									statement_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(type).add(statement_AST));
								
						currentAST.root = statement_AST;
						currentAST.child = statement_AST!=null &&statement_AST.getFirstChild()!=null ?
							statement_AST.getFirstChild() : statement_AST;
						currentAST.advanceChildToEnd();
					}
					statement_AST = (JavaAST)currentAST.root;
				}
				else if ((_tokenSet_38.member(LA(1))) && (_tokenSet_39.member(LA(2)))) {
					expression();
					e_AST = (JavaAST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					JavaAST tmp90_AST = null;
					tmp90_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp90_AST);
					match(SEMI);
					if ( inputState.guessing==0 ) {
						statement_AST = (JavaAST)currentAST.root;
						
									final jparse.stmt.ExpressionAST expr =
										new jparse.stmt.ExpressionAST((jparse.expr.ExpressionAST)e_AST);
									statement_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(expr).add(statement_AST));
								
						currentAST.root = statement_AST;
						currentAST.child = statement_AST!=null &&statement_AST.getFirstChild()!=null ?
							statement_AST.getFirstChild() : statement_AST;
						currentAST.advanceChildToEnd();
					}
					statement_AST = (JavaAST)currentAST.root;
				}
				else if ((LA(1)==IDENT) && (LA(2)==COLON)) {
					label = LT(1);
					label_AST = (jparse.stmt.LabelAST)astFactory.create(label,"jparse.stmt.LabelAST");
					astFactory.makeASTRoot(currentAST, label_AST);
					match(IDENT);
					JavaAST tmp91_AST = null;
					tmp91_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp91_AST);
					match(COLON);
					statement();
					st_AST = (JavaAST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						label_AST.symTable.addLabel(label_AST.getText(), st_AST);
					}
					statement_AST = (JavaAST)currentAST.root;
				}
				else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
					jparse.stmt.SynchronizedAST tmp92_AST = null;
					tmp92_AST = (jparse.stmt.SynchronizedAST)astFactory.create(LT(1),"jparse.stmt.SynchronizedAST");
					astFactory.makeASTRoot(currentAST, tmp92_AST);
					match(LITERAL_synchronized);
					JavaAST tmp93_AST = null;
					tmp93_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp93_AST);
					match(LPAREN);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					JavaAST tmp94_AST = null;
					tmp94_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp94_AST);
					match(RPAREN);
					compoundStatement();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (JavaAST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
		returnAST = statement_AST;
	}
	
	protected final void declaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST declaration_AST = null;
		JavaAST m_AST = null;
		JavaAST t_AST = null;
		JavaAST v_AST = null;
		
		try {      // for error handling
			modifiers();
			m_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			typeSpec();
			t_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			variableDefinitions();
			v_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			JavaAST tmp95_AST = null;
			tmp95_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp95_AST);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				declaration_AST = (JavaAST)currentAST.root;
				
							final DeclarationAST decl =
								new DeclarationAST((ModifierAST)m_AST,(jparse.expr.TypeAST)t_AST,v_AST);
							declaration_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(decl).add(declaration_AST));
						
				currentAST.root = declaration_AST;
				currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
					declaration_AST.getFirstChild() : declaration_AST;
				currentAST.advanceChildToEnd();
			}
			declaration_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
		returnAST = declaration_AST;
	}
	
	protected final void forInit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST forInit_AST = null;
		
		try {      // for error handling
			{
			boolean synPredMatched99 = false;
			if (((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2))))) {
				int _m99 = mark();
				synPredMatched99 = true;
				inputState.guessing++;
				try {
					{
					modifiers();
					typeSpec();
					match(IDENT);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched99 = false;
				}
				rewind(_m99);
				inputState.guessing--;
			}
			if ( synPredMatched99 ) {
				declaration();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_40.member(LA(1))) && (_tokenSet_41.member(LA(2)))) {
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp96_AST = null;
				tmp96_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp96_AST);
				match(SEMI);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				forInit_AST = (JavaAST)currentAST.root;
				forInit_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add((JavaAST)astFactory.create(FOR_INIT,"FOR_INIT")).add(forInit_AST));
				currentAST.root = forInit_AST;
				currentAST.child = forInit_AST!=null &&forInit_AST.getFirstChild()!=null ?
					forInit_AST.getFirstChild() : forInit_AST;
				currentAST.advanceChildToEnd();
			}
			forInit_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_40);
			} else {
			  throw ex;
			}
		}
		returnAST = forInit_AST;
	}
	
	protected final void forCond() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST forCond_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			JavaAST tmp97_AST = null;
			tmp97_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp97_AST);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				forCond_AST = (JavaAST)currentAST.root;
				forCond_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add((JavaAST)astFactory.create(FOR_CONDITION,"FOR_CONDITION")).add(forCond_AST));
				currentAST.root = forCond_AST;
				currentAST.child = forCond_AST!=null &&forCond_AST.getFirstChild()!=null ?
					forCond_AST.getFirstChild() : forCond_AST;
				currentAST.advanceChildToEnd();
			}
			forCond_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_42);
			} else {
			  throw ex;
			}
		}
		returnAST = forCond_AST;
	}
	
	protected final void forIter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST forIter_AST = null;
		
		try {      // for error handling
			expressionList();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				forIter_AST = (JavaAST)currentAST.root;
				forIter_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add((JavaAST)astFactory.create(FOR_ITERATOR,"FOR_ITERATOR")).add(forIter_AST));
				currentAST.root = forIter_AST;
				currentAST.child = forIter_AST!=null &&forIter_AST.getFirstChild()!=null ?
					forIter_AST.getFirstChild() : forIter_AST;
				currentAST.advanceChildToEnd();
			}
			forIter_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = forIter_AST;
	}
	
	protected final void casesGroup() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST casesGroup_AST = null;
		
		try {      // for error handling
			{
			int _cnt93=0;
			_loop93:
			do {
				if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default) && (_tokenSet_43.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case LITERAL_case:
					{
						JavaAST tmp98_AST = null;
						tmp98_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp98_AST);
						match(LITERAL_case);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case LITERAL_default:
					{
						JavaAST tmp99_AST = null;
						tmp99_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp99_AST);
						match(LITERAL_default);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					JavaAST tmp100_AST = null;
					tmp100_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp100_AST);
					match(COLON);
				}
				else {
					if ( _cnt93>=1 ) { break _loop93; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt93++;
			} while (true);
			}
			{
			_loop95:
			do {
				if ((_tokenSet_21.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop95;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				casesGroup_AST = (JavaAST)currentAST.root;
				
							final CaseGroupAST group = new CaseGroupAST();
							casesGroup_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(group).add(casesGroup_AST));
						
				currentAST.root = casesGroup_AST;
				currentAST.child = casesGroup_AST!=null &&casesGroup_AST.getFirstChild()!=null ?
					casesGroup_AST.getFirstChild() : casesGroup_AST;
				currentAST.advanceChildToEnd();
			}
			casesGroup_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		returnAST = casesGroup_AST;
	}
	
	protected final void tryBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST tryBlock_AST = null;
		
		try {      // for error handling
			jparse.stmt.TryAST tmp101_AST = null;
			tmp101_AST = (jparse.stmt.TryAST)astFactory.create(LT(1),"jparse.stmt.TryAST");
			astFactory.makeASTRoot(currentAST, tmp101_AST);
			match(LITERAL_try);
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop105:
			do {
				if ((LA(1)==LITERAL_catch)) {
					handler();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop105;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_finally:
			{
				JavaAST tmp102_AST = null;
				tmp102_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp102_AST);
				match(LITERAL_finally);
				compoundStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case IDENT:
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
			case LCURLY:
			case RCURLY:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_assert:
			case LITERAL_case:
			case LITERAL_default:
			case LITERAL_try:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			tryBlock_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
		returnAST = tryBlock_AST;
	}
	
	protected final void expressionList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST expressionList_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop112:
				do {
					if ((LA(1)==COMMA)) {
						JavaAST tmp103_AST = null;
						tmp103_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp103_AST);
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop112;
					}
					
				} while (true);
				}
				break;
			}
			case SEMI:
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				expressionList_AST = (JavaAST)currentAST.root;
				
							final ListAST list = new ListAST(expressionList_AST);
							expressionList_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(list).add(expressionList_AST));
						
				currentAST.root = expressionList_AST;
				currentAST.child = expressionList_AST!=null &&expressionList_AST.getFirstChild()!=null ?
					expressionList_AST.getFirstChild() : expressionList_AST;
				currentAST.advanceChildToEnd();
			}
			expressionList_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_45);
			} else {
			  throw ex;
			}
		}
		returnAST = expressionList_AST;
	}
	
	protected final void handler() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST handler_AST = null;
		
		try {      // for error handling
			jparse.stmt.CatchAST tmp104_AST = null;
			tmp104_AST = (jparse.stmt.CatchAST)astFactory.create(LT(1),"jparse.stmt.CatchAST");
			astFactory.makeASTRoot(currentAST, tmp104_AST);
			match(LITERAL_catch);
			if ( inputState.guessing==0 ) {
				JavaAST.currSymTable = new SymbolTable();
			}
			JavaAST tmp105_AST = null;
			tmp105_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp105_AST);
			match(LPAREN);
			parameterDeclaration();
			astFactory.addASTChild(currentAST, returnAST);
			JavaAST tmp106_AST = null;
			tmp106_AST = (JavaAST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp106_AST);
			match(RPAREN);
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				JavaAST.currSymTable = JavaAST.currSymTable.parent;
			}
			handler_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		returnAST = handler_AST;
	}
	
	protected final void assignmentExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST assignmentExpression_AST = null;
		
		try {      // for error handling
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case ASSIGN:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			{
				{
				switch ( LA(1)) {
				case ASSIGN:
				{
					jparse.expr.AssignAST tmp107_AST = null;
					tmp107_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp107_AST);
					match(ASSIGN);
					break;
				}
				case PLUS_ASSIGN:
				{
					jparse.expr.AssignAST tmp108_AST = null;
					tmp108_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp108_AST);
					match(PLUS_ASSIGN);
					break;
				}
				case MINUS_ASSIGN:
				{
					jparse.expr.AssignAST tmp109_AST = null;
					tmp109_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp109_AST);
					match(MINUS_ASSIGN);
					break;
				}
				case STAR_ASSIGN:
				{
					jparse.expr.AssignAST tmp110_AST = null;
					tmp110_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp110_AST);
					match(STAR_ASSIGN);
					break;
				}
				case DIV_ASSIGN:
				{
					jparse.expr.AssignAST tmp111_AST = null;
					tmp111_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp111_AST);
					match(DIV_ASSIGN);
					break;
				}
				case MOD_ASSIGN:
				{
					jparse.expr.AssignAST tmp112_AST = null;
					tmp112_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp112_AST);
					match(MOD_ASSIGN);
					break;
				}
				case SR_ASSIGN:
				{
					jparse.expr.AssignAST tmp113_AST = null;
					tmp113_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp113_AST);
					match(SR_ASSIGN);
					break;
				}
				case BSR_ASSIGN:
				{
					jparse.expr.AssignAST tmp114_AST = null;
					tmp114_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp114_AST);
					match(BSR_ASSIGN);
					break;
				}
				case SL_ASSIGN:
				{
					jparse.expr.AssignAST tmp115_AST = null;
					tmp115_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp115_AST);
					match(SL_ASSIGN);
					break;
				}
				case BAND_ASSIGN:
				{
					jparse.expr.AssignAST tmp116_AST = null;
					tmp116_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp116_AST);
					match(BAND_ASSIGN);
					break;
				}
				case BXOR_ASSIGN:
				{
					jparse.expr.AssignAST tmp117_AST = null;
					tmp117_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp117_AST);
					match(BXOR_ASSIGN);
					break;
				}
				case BOR_ASSIGN:
				{
					jparse.expr.AssignAST tmp118_AST = null;
					tmp118_AST = (jparse.expr.AssignAST)astFactory.create(LT(1),"jparse.expr.AssignAST");
					astFactory.makeASTRoot(currentAST, tmp118_AST);
					match(BOR_ASSIGN);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case COMMA:
			case RCURLY:
			case RPAREN:
			case RBRACK:
			case COLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			assignmentExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_29);
			} else {
			  throw ex;
			}
		}
		returnAST = assignmentExpression_AST;
	}
	
	protected final void conditionalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST conditionalExpression_AST = null;
		
		try {      // for error handling
			logicalOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case QUESTION:
			{
				jparse.expr.ConditionalAST tmp119_AST = null;
				tmp119_AST = (jparse.expr.ConditionalAST)astFactory.create(LT(1),"jparse.expr.ConditionalAST");
				astFactory.makeASTRoot(currentAST, tmp119_AST);
				match(QUESTION);
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp120_AST = null;
				tmp120_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp120_AST);
				match(COLON);
				conditionalExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case COMMA:
			case RCURLY:
			case RPAREN:
			case RBRACK:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			conditionalExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_47);
			} else {
			  throw ex;
			}
		}
		returnAST = conditionalExpression_AST;
	}
	
	protected final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST logicalOrExpression_AST = null;
		
		try {      // for error handling
			logicalAndExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop120:
			do {
				if ((LA(1)==LOR)) {
					jparse.expr.BooleanAST tmp121_AST = null;
					tmp121_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
					astFactory.makeASTRoot(currentAST, tmp121_AST);
					match(LOR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop120;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_48);
			} else {
			  throw ex;
			}
		}
		returnAST = logicalOrExpression_AST;
	}
	
	protected final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST logicalAndExpression_AST = null;
		
		try {      // for error handling
			inclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop123:
			do {
				if ((LA(1)==LAND)) {
					jparse.expr.BooleanAST tmp122_AST = null;
					tmp122_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
					astFactory.makeASTRoot(currentAST, tmp122_AST);
					match(LAND);
					inclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop123;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_49);
			} else {
			  throw ex;
			}
		}
		returnAST = logicalAndExpression_AST;
	}
	
	protected final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST inclusiveOrExpression_AST = null;
		
		try {      // for error handling
			exclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop126:
			do {
				if ((LA(1)==BOR)) {
					jparse.expr.BitwiseAST tmp123_AST = null;
					tmp123_AST = (jparse.expr.BitwiseAST)astFactory.create(LT(1),"jparse.expr.BitwiseAST");
					astFactory.makeASTRoot(currentAST, tmp123_AST);
					match(BOR);
					exclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop126;
				}
				
			} while (true);
			}
			inclusiveOrExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_50);
			} else {
			  throw ex;
			}
		}
		returnAST = inclusiveOrExpression_AST;
	}
	
	protected final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST exclusiveOrExpression_AST = null;
		
		try {      // for error handling
			andExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop129:
			do {
				if ((LA(1)==BXOR)) {
					jparse.expr.BitwiseAST tmp124_AST = null;
					tmp124_AST = (jparse.expr.BitwiseAST)astFactory.create(LT(1),"jparse.expr.BitwiseAST");
					astFactory.makeASTRoot(currentAST, tmp124_AST);
					match(BXOR);
					andExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop129;
				}
				
			} while (true);
			}
			exclusiveOrExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_51);
			} else {
			  throw ex;
			}
		}
		returnAST = exclusiveOrExpression_AST;
	}
	
	protected final void andExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST andExpression_AST = null;
		
		try {      // for error handling
			equalityExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop132:
			do {
				if ((LA(1)==BAND)) {
					jparse.expr.BitwiseAST tmp125_AST = null;
					tmp125_AST = (jparse.expr.BitwiseAST)astFactory.create(LT(1),"jparse.expr.BitwiseAST");
					astFactory.makeASTRoot(currentAST, tmp125_AST);
					match(BAND);
					equalityExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop132;
				}
				
			} while (true);
			}
			andExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_52);
			} else {
			  throw ex;
			}
		}
		returnAST = andExpression_AST;
	}
	
	protected final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST equalityExpression_AST = null;
		
		try {      // for error handling
			relationalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop136:
			do {
				if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
					{
					switch ( LA(1)) {
					case NOT_EQUAL:
					{
						jparse.expr.BooleanAST tmp126_AST = null;
						tmp126_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
						astFactory.makeASTRoot(currentAST, tmp126_AST);
						match(NOT_EQUAL);
						break;
					}
					case EQUAL:
					{
						jparse.expr.BooleanAST tmp127_AST = null;
						tmp127_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
						astFactory.makeASTRoot(currentAST, tmp127_AST);
						match(EQUAL);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					relationalExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop136;
				}
				
			} while (true);
			}
			equalityExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_53);
			} else {
			  throw ex;
			}
		}
		returnAST = equalityExpression_AST;
	}
	
	protected final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST relationalExpression_AST = null;
		
		try {      // for error handling
			shiftExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case SEMI:
			case COMMA:
			case RCURLY:
			case RPAREN:
			case RBRACK:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				{
				_loop141:
				do {
					if (((LA(1) >= LT && LA(1) <= GE))) {
						{
						switch ( LA(1)) {
						case LT:
						{
							jparse.expr.BooleanAST tmp128_AST = null;
							tmp128_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
							astFactory.makeASTRoot(currentAST, tmp128_AST);
							match(LT);
							break;
						}
						case GT:
						{
							jparse.expr.BooleanAST tmp129_AST = null;
							tmp129_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
							astFactory.makeASTRoot(currentAST, tmp129_AST);
							match(GT);
							break;
						}
						case LE:
						{
							jparse.expr.BooleanAST tmp130_AST = null;
							tmp130_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
							astFactory.makeASTRoot(currentAST, tmp130_AST);
							match(LE);
							break;
						}
						case GE:
						{
							jparse.expr.BooleanAST tmp131_AST = null;
							tmp131_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
							astFactory.makeASTRoot(currentAST, tmp131_AST);
							match(GE);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						shiftExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop141;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_instanceof:
			{
				jparse.expr.BooleanAST tmp132_AST = null;
				tmp132_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
				astFactory.makeASTRoot(currentAST, tmp132_AST);
				match(LITERAL_instanceof);
				typeSpec();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			relationalExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_54);
			} else {
			  throw ex;
			}
		}
		returnAST = relationalExpression_AST;
	}
	
	protected final void shiftExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST shiftExpression_AST = null;
		
		try {      // for error handling
			additiveExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop145:
			do {
				if (((LA(1) >= SL && LA(1) <= BSR))) {
					{
					switch ( LA(1)) {
					case SL:
					{
						jparse.expr.ShiftAST tmp133_AST = null;
						tmp133_AST = (jparse.expr.ShiftAST)astFactory.create(LT(1),"jparse.expr.ShiftAST");
						astFactory.makeASTRoot(currentAST, tmp133_AST);
						match(SL);
						break;
					}
					case SR:
					{
						jparse.expr.ShiftAST tmp134_AST = null;
						tmp134_AST = (jparse.expr.ShiftAST)astFactory.create(LT(1),"jparse.expr.ShiftAST");
						astFactory.makeASTRoot(currentAST, tmp134_AST);
						match(SR);
						break;
					}
					case BSR:
					{
						jparse.expr.ShiftAST tmp135_AST = null;
						tmp135_AST = (jparse.expr.ShiftAST)astFactory.create(LT(1),"jparse.expr.ShiftAST");
						astFactory.makeASTRoot(currentAST, tmp135_AST);
						match(BSR);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					additiveExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop145;
				}
				
			} while (true);
			}
			shiftExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_55);
			} else {
			  throw ex;
			}
		}
		returnAST = shiftExpression_AST;
	}
	
	protected final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST additiveExpression_AST = null;
		
		try {      // for error handling
			multiplicativeExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop149:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS)) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						jparse.expr.ArithmeticAST tmp136_AST = null;
						tmp136_AST = (jparse.expr.ArithmeticAST)astFactory.create(LT(1),"jparse.expr.ArithmeticAST");
						astFactory.makeASTRoot(currentAST, tmp136_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						jparse.expr.ArithmeticAST tmp137_AST = null;
						tmp137_AST = (jparse.expr.ArithmeticAST)astFactory.create(LT(1),"jparse.expr.ArithmeticAST");
						astFactory.makeASTRoot(currentAST, tmp137_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					multiplicativeExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop149;
				}
				
			} while (true);
			}
			additiveExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_56);
			} else {
			  throw ex;
			}
		}
		returnAST = additiveExpression_AST;
	}
	
	protected final void multiplicativeExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST multiplicativeExpression_AST = null;
		
		try {      // for error handling
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop153:
			do {
				if ((_tokenSet_57.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						jparse.expr.ArithmeticAST tmp138_AST = null;
						tmp138_AST = (jparse.expr.ArithmeticAST)astFactory.create(LT(1),"jparse.expr.ArithmeticAST");
						astFactory.makeASTRoot(currentAST, tmp138_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						jparse.expr.ArithmeticAST tmp139_AST = null;
						tmp139_AST = (jparse.expr.ArithmeticAST)astFactory.create(LT(1),"jparse.expr.ArithmeticAST");
						astFactory.makeASTRoot(currentAST, tmp139_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						jparse.expr.ArithmeticAST tmp140_AST = null;
						tmp140_AST = (jparse.expr.ArithmeticAST)astFactory.create(LT(1),"jparse.expr.ArithmeticAST");
						astFactory.makeASTRoot(currentAST, tmp140_AST);
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop153;
				}
				
			} while (true);
			}
			multiplicativeExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_58);
			} else {
			  throw ex;
			}
		}
		returnAST = multiplicativeExpression_AST;
	}
	
	protected final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST unaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INC:
			{
				jparse.expr.UnaryArithAST tmp141_AST = null;
				tmp141_AST = (jparse.expr.UnaryArithAST)astFactory.create(LT(1),"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, tmp141_AST);
				match(INC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case DEC:
			{
				jparse.expr.UnaryArithAST tmp142_AST = null;
				tmp142_AST = (jparse.expr.UnaryArithAST)astFactory.create(LT(1),"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, tmp142_AST);
				match(DEC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case MINUS:
			{
				jparse.expr.UnaryArithAST tmp143_AST = null;
				tmp143_AST = (jparse.expr.UnaryArithAST)astFactory.create(LT(1),"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, tmp143_AST);
				match(MINUS);
				if ( inputState.guessing==0 ) {
					tmp143_AST.setType(UNARY_MINUS);
				}
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case PLUS:
			{
				jparse.expr.UnaryArithAST tmp144_AST = null;
				tmp144_AST = (jparse.expr.UnaryArithAST)astFactory.create(LT(1),"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, tmp144_AST);
				match(PLUS);
				if ( inputState.guessing==0 ) {
					tmp144_AST.setType(UNARY_PLUS);
				}
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case IDENT:
			case LPAREN:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case BNOT:
			case LNOT:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				unaryExpressionNotPlusMinus();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_59);
			} else {
			  throw ex;
			}
		}
		returnAST = unaryExpression_AST;
	}
	
	protected final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST unaryExpressionNotPlusMinus_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BNOT:
			{
				jparse.expr.UnaryArithAST tmp145_AST = null;
				tmp145_AST = (jparse.expr.UnaryArithAST)astFactory.create(LT(1),"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, tmp145_AST);
				match(BNOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (JavaAST)currentAST.root;
				break;
			}
			case LNOT:
			{
				jparse.expr.BooleanAST tmp146_AST = null;
				tmp146_AST = (jparse.expr.BooleanAST)astFactory.create(LT(1),"jparse.expr.BooleanAST");
				astFactory.makeASTRoot(currentAST, tmp146_AST);
				match(LNOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched157 = false;
				if (((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_double)))) {
					int _m157 = mark();
					synPredMatched157 = true;
					inputState.guessing++;
					try {
						{
						match(LPAREN);
						builtInTypeSpec();
						match(RPAREN);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched157 = false;
					}
					rewind(_m157);
					inputState.guessing--;
				}
				if ( synPredMatched157 ) {
					jparse.expr.TypecastAST tmp147_AST = null;
					tmp147_AST = (jparse.expr.TypecastAST)astFactory.create(LT(1),"jparse.expr.TypecastAST");
					astFactory.makeASTRoot(currentAST, tmp147_AST);
					match(LPAREN);
					builtInTypeSpec();
					astFactory.addASTChild(currentAST, returnAST);
					JavaAST tmp148_AST = null;
					tmp148_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp148_AST);
					match(RPAREN);
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
					unaryExpressionNotPlusMinus_AST = (JavaAST)currentAST.root;
				}
				else {
					boolean synPredMatched159 = false;
					if (((LA(1)==LPAREN) && (LA(2)==IDENT))) {
						int _m159 = mark();
						synPredMatched159 = true;
						inputState.guessing++;
						try {
							{
							match(LPAREN);
							classTypeSpec();
							match(RPAREN);
							unaryExpressionNotPlusMinus();
							}
						}
						catch (RecognitionException pe) {
							synPredMatched159 = false;
						}
						rewind(_m159);
						inputState.guessing--;
					}
					if ( synPredMatched159 ) {
						jparse.expr.TypecastAST tmp149_AST = null;
						tmp149_AST = (jparse.expr.TypecastAST)astFactory.create(LT(1),"jparse.expr.TypecastAST");
						astFactory.makeASTRoot(currentAST, tmp149_AST);
						match(LPAREN);
						classTypeSpec();
						astFactory.addASTChild(currentAST, returnAST);
						JavaAST tmp150_AST = null;
						tmp150_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp150_AST);
						match(RPAREN);
						unaryExpressionNotPlusMinus();
						astFactory.addASTChild(currentAST, returnAST);
						unaryExpressionNotPlusMinus_AST = (JavaAST)currentAST.root;
					}
					else if ((_tokenSet_60.member(LA(1))) && (_tokenSet_61.member(LA(2)))) {
						postfixExpression();
						astFactory.addASTChild(currentAST, returnAST);
						unaryExpressionNotPlusMinus_AST = (JavaAST)currentAST.root;
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					consume();
					consumeUntil(_tokenSet_59);
				} else {
				  throw ex;
				}
			}
			returnAST = unaryExpressionNotPlusMinus_AST;
		}
		
	protected final void postfixExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST postfixExpression_AST = null;
		Token  dot1 = null;
		jparse.expr.IdentifierAST dot1_AST = null;
		Token  id = null;
		jparse.expr.IdentifierAST id_AST = null;
		Token  th = null;
		JavaAST th_AST = null;
		Token  cl1 = null;
		JavaAST cl1_AST = null;
		Token  su = null;
		jparse.expr.IdentifierAST su_AST = null;
		Token  lbc = null;
		jparse.expr.IdentifierAST lbc_AST = null;
		Token  dot2 = null;
		jparse.expr.IdentifierAST dot2_AST = null;
		Token  cl2 = null;
		JavaAST cl2_AST = null;
		Token  lp2 = null;
		jparse.expr.MethodCallAST lp2_AST = null;
		Token  in = null;
		jparse.expr.UnaryArithAST in_AST = null;
		Token  de = null;
		jparse.expr.UnaryArithAST de_AST = null;
		String name = null;
		
		try {      // for error handling
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop165:
			do {
				switch ( LA(1)) {
				case DOT:
				{
					dot1 = LT(1);
					dot1_AST = (jparse.expr.IdentifierAST)astFactory.create(dot1,"jparse.expr.IdentifierAST");
					astFactory.makeASTRoot(currentAST, dot1_AST);
					match(DOT);
					if ( inputState.guessing==0 ) {
						
										final AST child = dot1_AST.getFirstChild();
										name = ((child instanceof IdentifierAST)
												? ((IdentifierAST)child).getName() : "") +
											   dot1_AST.getText();
									
					}
					{
					switch ( LA(1)) {
					case IDENT:
					{
						id = LT(1);
						id_AST = (jparse.expr.IdentifierAST)astFactory.create(id,"jparse.expr.IdentifierAST");
						astFactory.addASTChild(currentAST, id_AST);
						match(IDENT);
						if ( inputState.guessing==0 ) {
							dot1_AST.setName(name + id_AST.getText());
						}
						break;
					}
					case LITERAL_this:
					{
						th = LT(1);
						th_AST = (JavaAST)astFactory.create(th);
						astFactory.addASTChild(currentAST, th_AST);
						match(LITERAL_this);
						if ( inputState.guessing==0 ) {
							dot1_AST.setName(name + th_AST.getText());
						}
						break;
					}
					case LITERAL_class:
					{
						cl1 = LT(1);
						cl1_AST = (JavaAST)astFactory.create(cl1);
						astFactory.addASTChild(currentAST, cl1_AST);
						match(LITERAL_class);
						if ( inputState.guessing==0 ) {
							dot1_AST.setName(name + cl1_AST.getText());
						}
						break;
					}
					case LITERAL_new:
					{
						newExpression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case LITERAL_super:
					{
						su = LT(1);
						su_AST = (jparse.expr.IdentifierAST)astFactory.create(su,"jparse.expr.IdentifierAST");
						astFactory.addASTChild(currentAST, su_AST);
						match(LITERAL_super);
						jparse.expr.MethodCallAST tmp151_AST = null;
						tmp151_AST = (jparse.expr.MethodCallAST)astFactory.create(LT(1),"jparse.expr.MethodCallAST");
						astFactory.makeASTRoot(currentAST, tmp151_AST);
						match(LPAREN);
						expressionList();
						astFactory.addASTChild(currentAST, returnAST);
						JavaAST tmp152_AST = null;
						tmp152_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp152_AST);
						match(RPAREN);
						if ( inputState.guessing==0 ) {
							
												dot1_AST.setName(name + su_AST.getText());
												dot1_AST.setMethod();
											
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case LPAREN:
				{
					lp2 = LT(1);
					lp2_AST = (jparse.expr.MethodCallAST)astFactory.create(lp2,"jparse.expr.MethodCallAST");
					astFactory.makeASTRoot(currentAST, lp2_AST);
					match(LPAREN);
					expressionList();
					astFactory.addASTChild(currentAST, returnAST);
					JavaAST tmp153_AST = null;
					tmp153_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp153_AST);
					match(RPAREN);
					if ( inputState.guessing==0 ) {
						((IdentifierAST)lp2_AST.getFirstChild()).setMethod();
					}
					break;
				}
				default:
					if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
						{
						int _cnt164=0;
						_loop164:
						do {
							if ((LA(1)==LBRACK)) {
								lbc = LT(1);
								lbc_AST = (jparse.expr.IdentifierAST)astFactory.create(lbc,"jparse.expr.IdentifierAST");
								astFactory.makeASTRoot(currentAST, lbc_AST);
								match(LBRACK);
								if ( inputState.guessing==0 ) {
									
														lbc_AST.setType(ARRAY_DECLARATOR);
														final AST child = lbc_AST.getFirstChild();
														name = ((child instanceof IdentifierAST)
															? ((IdentifierAST)child).getName() : "") + "[]";
														lbc_AST.setName(name);
													
								}
								JavaAST tmp154_AST = null;
								tmp154_AST = (JavaAST)astFactory.create(LT(1));
								astFactory.addASTChild(currentAST, tmp154_AST);
								match(RBRACK);
							}
							else {
								if ( _cnt164>=1 ) { break _loop164; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt164++;
						} while (true);
						}
						if ( inputState.guessing==0 ) {
							postfixExpression_AST = (JavaAST)currentAST.root;
							
							jparse.expr.TypeAST t =
							new jparse.expr.TypeAST(lbc_AST.getName());
							postfixExpression_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(t).add(postfixExpression_AST));
							
							currentAST.root = postfixExpression_AST;
							currentAST.child = postfixExpression_AST!=null &&postfixExpression_AST.getFirstChild()!=null ?
								postfixExpression_AST.getFirstChild() : postfixExpression_AST;
							currentAST.advanceChildToEnd();
						}
						dot2 = LT(1);
						dot2_AST = (jparse.expr.IdentifierAST)astFactory.create(dot2,"jparse.expr.IdentifierAST");
						astFactory.makeASTRoot(currentAST, dot2_AST);
						match(DOT);
						cl2 = LT(1);
						cl2_AST = (JavaAST)astFactory.create(cl2);
						astFactory.addASTChild(currentAST, cl2_AST);
						match(LITERAL_class);
						if ( inputState.guessing==0 ) {
							
											name += dot2_AST.getText() + cl2_AST.getText();
											dot2_AST.setName(name);
										
						}
					}
					else if ((LA(1)==LBRACK) && (_tokenSet_38.member(LA(2)))) {
						jparse.expr.IndexAST tmp155_AST = null;
						tmp155_AST = (jparse.expr.IndexAST)astFactory.create(LT(1),"jparse.expr.IndexAST");
						astFactory.makeASTRoot(currentAST, tmp155_AST);
						match(LBRACK);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						JavaAST tmp156_AST = null;
						tmp156_AST = (JavaAST)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp156_AST);
						match(RBRACK);
					}
				else {
					break _loop165;
				}
				}
			} while (true);
			}
			{
			switch ( LA(1)) {
			case INC:
			{
				in = LT(1);
				in_AST = (jparse.expr.UnaryArithAST)astFactory.create(in,"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, in_AST);
				match(INC);
				if ( inputState.guessing==0 ) {
					in_AST.setType(POST_INC);
				}
				break;
			}
			case DEC:
			{
				de = LT(1);
				de_AST = (jparse.expr.UnaryArithAST)astFactory.create(de,"jparse.expr.UnaryArithAST");
				astFactory.makeASTRoot(currentAST, de_AST);
				match(DEC);
				if ( inputState.guessing==0 ) {
					de_AST.setType(POST_DEC);
				}
				break;
			}
			case SEMI:
			case STAR:
			case COMMA:
			case RCURLY:
			case RPAREN:
			case RBRACK:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			postfixExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_59);
			} else {
			  throw ex;
			}
		}
		returnAST = postfixExpression_AST;
	}
	
	protected final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST primaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				jparse.expr.IdentifierAST tmp157_AST = null;
				tmp157_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp157_AST);
				match(IDENT);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			{
				constant();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_true:
			{
				jparse.expr.BooleanLiteralAST tmp158_AST = null;
				tmp158_AST = (jparse.expr.BooleanLiteralAST)astFactory.create(LT(1),"jparse.expr.BooleanLiteralAST");
				astFactory.addASTChild(currentAST, tmp158_AST);
				match(LITERAL_true);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_false:
			{
				jparse.expr.BooleanLiteralAST tmp159_AST = null;
				tmp159_AST = (jparse.expr.BooleanLiteralAST)astFactory.create(LT(1),"jparse.expr.BooleanLiteralAST");
				astFactory.addASTChild(currentAST, tmp159_AST);
				match(LITERAL_false);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_null:
			{
				jparse.expr.NullLiteralAST tmp160_AST = null;
				tmp160_AST = (jparse.expr.NullLiteralAST)astFactory.create(LT(1),"jparse.expr.NullLiteralAST");
				astFactory.addASTChild(currentAST, tmp160_AST);
				match(LITERAL_null);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_new:
			{
				newExpression();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_this:
			{
				jparse.expr.ThisLiteralAST tmp161_AST = null;
				tmp161_AST = (jparse.expr.ThisLiteralAST)astFactory.create(LT(1),"jparse.expr.ThisLiteralAST");
				astFactory.addASTChild(currentAST, tmp161_AST);
				match(LITERAL_this);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_super:
			{
				jparse.expr.IdentifierAST tmp162_AST = null;
				tmp162_AST = (jparse.expr.IdentifierAST)astFactory.create(LT(1),"jparse.expr.IdentifierAST");
				astFactory.addASTChild(currentAST, tmp162_AST);
				match(LITERAL_super);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LPAREN:
			{
				jparse.expr.ParenthesizedAST tmp163_AST = null;
				tmp163_AST = (jparse.expr.ParenthesizedAST)astFactory.create(LT(1),"jparse.expr.ParenthesizedAST");
				astFactory.makeASTRoot(currentAST, tmp163_AST);
				match(LPAREN);
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp164_AST = null;
				tmp164_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp164_AST);
				match(RPAREN);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				builtInType();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = primaryExpression_AST;
	}
	
	protected final void newExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST newExpression_AST = null;
		JavaAST typ_AST = null;
		
		try {      // for error handling
			jparse.expr.NewAST tmp165_AST = null;
			tmp165_AST = (jparse.expr.NewAST)astFactory.create(LT(1),"jparse.expr.NewAST");
			astFactory.makeASTRoot(currentAST, tmp165_AST);
			match(LITERAL_new);
			type();
			typ_AST = (JavaAST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				JavaAST tmp166_AST = null;
				tmp166_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp166_AST);
				match(LPAREN);
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
				JavaAST tmp167_AST = null;
				tmp167_AST = (JavaAST)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp167_AST);
				match(RPAREN);
				{
				switch ( LA(1)) {
				case LCURLY:
				{
					anonymousClassBlock((IdentifierAST)typ_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				case DOT:
				case STAR:
				case COMMA:
				case RCURLY:
				case LPAREN:
				case RPAREN:
				case LBRACK:
				case RBRACK:
				case ASSIGN:
				case COLON:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case QUESTION:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case NOT_EQUAL:
				case EQUAL:
				case LT:
				case GT:
				case LE:
				case GE:
				case LITERAL_instanceof:
				case SL:
				case SR:
				case BSR:
				case PLUS:
				case MINUS:
				case DIV:
				case MOD:
				case INC:
				case DEC:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case LBRACK:
			{
				newArrayDeclarator();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case LCURLY:
				{
					arrayInitializer();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				case DOT:
				case STAR:
				case COMMA:
				case RCURLY:
				case LPAREN:
				case RPAREN:
				case LBRACK:
				case RBRACK:
				case ASSIGN:
				case COLON:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case QUESTION:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case NOT_EQUAL:
				case EQUAL:
				case LT:
				case GT:
				case LE:
				case GE:
				case LITERAL_instanceof:
				case SL:
				case SR:
				case BSR:
				case PLUS:
				case MINUS:
				case DIV:
				case MOD:
				case INC:
				case DEC:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			newExpression_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = newExpression_AST;
	}
	
	protected final void constant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST constant_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			{
				jparse.expr.NumLiteralAST tmp168_AST = null;
				tmp168_AST = (jparse.expr.NumLiteralAST)astFactory.create(LT(1),"jparse.expr.NumLiteralAST");
				astFactory.addASTChild(currentAST, tmp168_AST);
				match(NUM_INT);
				constant_AST = (JavaAST)currentAST.root;
				break;
			}
			case CHAR_LITERAL:
			{
				jparse.expr.CharLiteralAST tmp169_AST = null;
				tmp169_AST = (jparse.expr.CharLiteralAST)astFactory.create(LT(1),"jparse.expr.CharLiteralAST");
				astFactory.addASTChild(currentAST, tmp169_AST);
				match(CHAR_LITERAL);
				constant_AST = (JavaAST)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			{
				jparse.expr.StringLiteralAST tmp170_AST = null;
				tmp170_AST = (jparse.expr.StringLiteralAST)astFactory.create(LT(1),"jparse.expr.StringLiteralAST");
				astFactory.addASTChild(currentAST, tmp170_AST);
				match(STRING_LITERAL);
				constant_AST = (JavaAST)currentAST.root;
				break;
			}
			case NUM_FLOAT:
			{
				jparse.expr.FloatLiteralAST tmp171_AST = null;
				tmp171_AST = (jparse.expr.FloatLiteralAST)astFactory.create(LT(1),"jparse.expr.FloatLiteralAST");
				astFactory.addASTChild(currentAST, tmp171_AST);
				match(NUM_FLOAT);
				constant_AST = (JavaAST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = constant_AST;
	}
	
	protected final void anonymousClassBlock(
		IdentifierAST superclass
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST anonymousClassBlock_AST = null;
		JavaAST blk_AST = null;
		jparse.TypeAST type = null, oldType = jparse.TypeAST.currType;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				
							type = new jparse.TypeAST(superclass.getName());
							oldType.addAnonymous(FileAST.currFile.pkg, type);
							jparse.TypeAST.currType = type;
						
			}
			classBlock();
			blk_AST = (JavaAST)returnAST;
			if ( inputState.guessing==0 ) {
				anonymousClassBlock_AST = (JavaAST)currentAST.root;
				
							JavaAST.currSymTable = JavaAST.currSymTable.parent;
							anonymousClassBlock_AST = (JavaAST)astFactory.make( (new ASTArray(2)).add(type).add(blk_AST));
							jparse.TypeAST.currType = oldType;
						
				currentAST.root = anonymousClassBlock_AST;
				currentAST.child = anonymousClassBlock_AST!=null &&anonymousClassBlock_AST.getFirstChild()!=null ?
					anonymousClassBlock_AST.getFirstChild() : anonymousClassBlock_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = anonymousClassBlock_AST;
	}
	
	protected final void newArrayDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaAST newArrayDeclarator_AST = null;
		Token  lb = null;
		JavaAST lb_AST = null;
		
		try {      // for error handling
			{
			int _cnt176=0;
			_loop176:
			do {
				if ((LA(1)==LBRACK) && (_tokenSet_62.member(LA(2)))) {
					lb = LT(1);
					lb_AST = (JavaAST)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(ARRAY_DECLARATOR);
					}
					{
					switch ( LA(1)) {
					case IDENT:
					case LPAREN:
					case LITERAL_void:
					case LITERAL_boolean:
					case LITERAL_byte:
					case LITERAL_char:
					case LITERAL_short:
					case LITERAL_int:
					case LITERAL_float:
					case LITERAL_long:
					case LITERAL_double:
					case PLUS:
					case MINUS:
					case INC:
					case DEC:
					case BNOT:
					case LNOT:
					case LITERAL_this:
					case LITERAL_super:
					case LITERAL_true:
					case LITERAL_false:
					case LITERAL_null:
					case LITERAL_new:
					case NUM_INT:
					case CHAR_LITERAL:
					case STRING_LITERAL:
					case NUM_FLOAT:
					{
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RBRACK:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					JavaAST tmp172_AST = null;
					tmp172_AST = (JavaAST)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp172_AST);
					match(RBRACK);
				}
				else {
					if ( _cnt176>=1 ) { break _loop176; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt176++;
			} while (true);
			}
			newArrayDeclarator_AST = (JavaAST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		returnAST = newArrayDeclarator_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"FILE",
		"VARIABLE_DEFS",
		"MODIFIERS",
		"ARRAY_DECLARATOR",
		"TYPE",
		"EXTENDS_CLAUSE",
		"OBJBLOCK",
		"IMPLEMENTS_CLAUSE",
		"CTOR_DEF",
		"METHOD_DEF",
		"INSTANCE_INIT",
		"VARIABLE_DEF",
		"ARRAY_INIT",
		"PARAMETERS",
		"PARAMETER_DEF",
		"SLIST",
		"TYPE_STAT",
		"EXPRESSION_STAT",
		"LABELED_STAT",
		"EMPTY_STAT",
		"CASE_GROUP",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"ELIST",
		"CONCAT_ASSIGN",
		"CONCATENATION",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"TYPECAST",
		"INDEX_OP",
		"METHOD_CALL",
		"CONSTRUCTOR_CALL",
		"POST_INC",
		"POST_DEC",
		"PAREN_EXPR",
		"\"package\"",
		"SEMI",
		"\"import\"",
		"IDENT",
		"DOT",
		"STAR",
		"\"public\"",
		"\"private\"",
		"\"protected\"",
		"\"static\"",
		"\"final\"",
		"\"synchronized\"",
		"\"volatile\"",
		"\"transient\"",
		"\"native\"",
		"\"abstract\"",
		"\"strictfp\"",
		"\"class\"",
		"\"extends\"",
		"\"implements\"",
		"COMMA",
		"\"interface\"",
		"LCURLY",
		"RCURLY",
		"LPAREN",
		"RPAREN",
		"\"throws\"",
		"LBRACK",
		"RBRACK",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"ASSIGN",
		"COLON",
		"\"if\"",
		"\"else\"",
		"\"for\"",
		"\"while\"",
		"\"do\"",
		"\"break\"",
		"\"continue\"",
		"\"return\"",
		"\"switch\"",
		"\"throw\"",
		"\"assert\"",
		"\"case\"",
		"\"default\"",
		"\"try\"",
		"\"finally\"",
		"\"catch\"",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"SR_ASSIGN",
		"BSR_ASSIGN",
		"SL_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"QUESTION",
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"BAND",
		"NOT_EQUAL",
		"EQUAL",
		"LT",
		"GT",
		"LE",
		"GE",
		"\"instanceof\"",
		"SL",
		"SR",
		"BSR",
		"PLUS",
		"MINUS",
		"DIV",
		"MOD",
		"INC",
		"DEC",
		"BNOT",
		"LNOT",
		"\"this\"",
		"\"super\"",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"\"new\"",
		"NUM_INT",
		"CHAR_LITERAL",
		"STRING_LITERAL",
		"NUM_FLOAT",
		"\"const\"",
		"\"goto\"",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2594005215644483584L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 2594009613690994690L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 2594005215644483586L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { -2882292766400839680L, 2251795518767131L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 2199023255552L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 2449966993382572032L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { -2017672006689882110L, -7493989778870812703L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { -2017672006689882110L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 2594011812714250240L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 5188146770730811392L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 4611686018427387904L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 7205700030164893696L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { -864697725524901886L, -3221225477L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 2594029404900294656L, 16361L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 8796093022208L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 26388279066624L, 8L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 1152923703630102528L, 16392L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { -2017672006689882112L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 0L, 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 4611688217450643456L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 4899857020951199744L, -7493989779273596959L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { -2017672006689882112L, -7493989775649587231L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { -8070439537131651072L, 2251795518767122L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 5764609722057490432L, 16390L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { -8070386760573517824L, -4294918117L, 1L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 0L, 9L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 1152923703630102528L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { -8070448333224673280L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { -8070448333224673280L, 32786L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { -8070395556666540032L, -4294918117L, 1L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { -9223372036854775808L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 1152921504606846976L, 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { -4323515015903576064L, -7493989778870812703L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { -2017619230131748864L, -23L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 144053615424700416L, 16352L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 144071207610744832L, 16360L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 288168803500556288L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 8796093022208L, -7493989779944488991L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 63771674411008L, -4294934551L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { 10995116277760L, -7493989779944488991L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { 1152985276281257984L, -4294934551L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 8796093022208L, -7493989779944488989L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 8796093022208L, -7493989779944456223L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { -9223372036854775808L, 402653184L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { 2199023255552L, 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = { -4323515015903576064L, -7493989775649587231L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { -8070448333224673280L, 8791798104082L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = { -8070448333224673280L, 17587891126290L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	private static final long[] mk_tokenSet_49() {
		long[] data = { -8070448333224673280L, 35180077170706L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
	private static final long[] mk_tokenSet_50() {
		long[] data = { -8070448333224673280L, 70364449259538L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());
	private static final long[] mk_tokenSet_51() {
		long[] data = { -8070448333224673280L, 140733193437202L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());
	private static final long[] mk_tokenSet_52() {
		long[] data = { -8070448333224673280L, 281470681792530L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());
	private static final long[] mk_tokenSet_53() {
		long[] data = { -8070448333224673280L, 562945658503186L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());
	private static final long[] mk_tokenSet_54() {
		long[] data = { -8070448333224673280L, 2251795518767122L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());
	private static final long[] mk_tokenSet_55() {
		long[] data = { -8070448333224673280L, 72057589743009810L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());
	private static final long[] mk_tokenSet_56() {
		long[] data = { -8070448333224673280L, 576460748008505362L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());
	private static final long[] mk_tokenSet_57() {
		long[] data = { 35184372088832L, 6917529027641081856L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());
	private static final long[] mk_tokenSet_58() {
		long[] data = { -8070448333224673280L, 2305843004918775826L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());
	private static final long[] mk_tokenSet_59() {
		long[] data = { -8070413148852584448L, 9223372032559857682L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());
	private static final long[] mk_tokenSet_60() {
		long[] data = { 8796093022208L, 16353L, 8184L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());
	private static final long[] mk_tokenSet_61() {
		long[] data = { -8070386760573517824L, -4294901765L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());
	private static final long[] mk_tokenSet_62() {
		long[] data = { 8796093022208L, -7493989779944488975L, 8191L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());
	private static final long[] mk_tokenSet_63() {
		long[] data = { -3458709538239152128L, -4294918117L, 1L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());
	
	}
