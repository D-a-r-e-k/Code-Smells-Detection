/*
 * @(#)$Id: ParenthesizedAST.java,v 1.2 2004/04/02 05:48:48 james Exp $
 *
 * JParse: a freely available Java parser
 * Copyright (C) 2000,2004 Jeremiah W. James
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
package jparse.expr;

import antlr.Token;
import jparse.JavaTokenTypes;
import jparse.Type;
import jparse.VarList;

/**
 * An AST node that represents a parenthesized expression
 *
 * @version $Revision: 1.2 $, $Date: 2004/04/02 05:48:48 $
 * @author Jerry James
 */
public final class ParenthesizedAST extends ExpressionAST
    implements JavaTokenTypes {

    /**
     * The parenthesized expression
     */
    private ExpressionAST parenthesized;

    /**
     * Create a new parenthesized expression AST
     *
     * @param token the token represented by this AST node
     */
    public ParenthesizedAST(final Token token) {
	super(token);
	setType(PAREN_EXPR);
    }

    public void parseComplete() {
	parenthesized = (ExpressionAST)getFirstChild();
	parenthesized.parseComplete();
    }

    protected Type computeType() {
	return parenthesized.retrieveType();	// JLS 15.8.5
    }

    protected Type[] computeExceptions() {
	return parenthesized.getExceptionTypes();
    }

    protected Object computeValue() {
	return parenthesized.getValue();
    }

    public VarList getVarList() {
	return parenthesized.getVarList();
    }

    /**
     * Get the expression in the parentheses
     *
     * @return the parenthesized expression
     */
    public ExpressionAST getParenExpression() {
	return parenthesized;
    }
}
