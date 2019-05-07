/*
 * $Id: SelectCommand.java,v 1.39 2003/07/01 13:42:23 rwald Exp $
 * =======================================================================
 * Copyright (c) 2002-2003 Axion Development Team.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The names "Tigris", "Axion", nor the names of its contributors may
 *    not be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * 4. Products derived from this software may not be called "Axion", nor
 *    may "Tigris" or "Axion" appear in their names without specific prior
 *    written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * =======================================================================
 */

package org.axiondb.engine.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.axiondb.AxionException;
import org.axiondb.ColumnIdentifier;
import org.axiondb.ComparisonOperator;
import org.axiondb.Database;
import org.axiondb.FromNode;
import org.axiondb.Index;
import org.axiondb.LeafWhereNode;
import org.axiondb.Literal;
import org.axiondb.OrderNode;
import org.axiondb.Row;
import org.axiondb.RowComparator;
import org.axiondb.RowDecorator;
import org.axiondb.RowIterator;
import org.axiondb.Selectable;
import org.axiondb.Table;
import org.axiondb.TableIdentifier;
import org.axiondb.WhereNode;
import org.axiondb.engine.ReferencesOtherTablesWhereNodeVisitor;
import org.axiondb.engine.SimpleRow;
import org.axiondb.engine.rowiterators.ChangingIndexedRowIterator;
import org.axiondb.engine.rowiterators.DistinctRowIterator;
import org.axiondb.engine.rowiterators.FilteringRowIterator;
import org.axiondb.engine.rowiterators.IndexJoinedRowIterator;
import org.axiondb.engine.rowiterators.LimitingRowIterator;
import org.axiondb.engine.rowiterators.ListIteratorRowIterator;
import org.axiondb.engine.rowiterators.RowIteratorRowDecoratorIterator;
import org.axiondb.engine.rowiterators.SimpleJoinedRowIterator;
import org.axiondb.engine.rowiterators.SingleRowIterator;
import org.axiondb.functions.AggregateFunction;
import org.axiondb.jdbc.AxionResultSet;

/**
 * A <tt>SELECT</tt> query.
 *
 * @version $Revision: 1.39 $ $Date: 2003/07/01 13:42:23 $
 * @author Morgan Delagrange
 * @author Rodney Waldhoff
 * @author Chuck Burdick
 * @author Amrish Lal
 */
public class SelectCommand extends BaseAxionCommand {

    //------------------------------------------------------------ Constructors

    public SelectCommand() {
    }

    //-------------------------------------------------------------- Properties

    public void setSelect(List columns) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _select = columns;
    }

    /**
     * Adds a {@link Selectable} to the list of items being selected.
     * @param column the {@link Selectable} to add
     * @throws IllegalStateException if I have already been resolved
     */
    public void addSelect(Selectable column) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _select.add(column);
    }

    /**
     * Gets the <i>i</i><sup>th</sup> {@link Selectable} being selected.
     * Clients should treat the returned value as immutable.
     * @param i the zero-based index
     */
    public Selectable getSelect(int i) {
        return (Selectable)(_select.get(i));
    }

    /**
     * Sets the <i>i</i><sup>th</sup> {@link Selectable} being selected.
     * @param i the zero-based index
     * @param sel the new {@link Selectable}
     * @throws IllegalStateException if I have already been resolved
     */
    public void setSelect(int i, Selectable sel) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _select.set(i,sel);
    }

    /**
     * Gets the number of {@link Selectable}s being selected.
     */
    public int getSelectCount() {
        return _select.size();
    }

    /**
     * Sets the root {@link FromNode} for the select statement.
     */
    public void setFrom(FromNode from) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _from = from;
    }

    /**
     * Adds a {@link TableIdentifier} to the list
     * of tables being selected from.
     * @param table a {@link TableIdentifier}
     * @throws IllegalStateException if I have already been resolved
     */
    public void addFrom(TableIdentifier table) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        if (_from == null) {
            _from = new FromNode();
            _from.setType(FromNode.TYPE_SINGLE);
        }        

        if (_from.getLeft() == null) {
            _from.setLeft(table);
        } else if (_from.getRight() == null) {
            _from.setRight(table);
            _from.setType(FromNode.TYPE_INNER);
        } else {
            FromNode from = new FromNode();
            from.setLeft(_from);
            from.setRight(table);
            from.setType(FromNode.TYPE_INNER);
            _from = from;
        }
    }

    /**
     * Gets the root {@link FromNode} for the select statement.
     */
    public FromNode getFrom() {
        return _from;
    }

    /**
     * Gets the <i>i</i><sup>th</sup> table being selected.
     * Clients should treat the returned value as immutable.
     * @param i the zero-based index
     */
    public TableIdentifier getFrom(int i) {
        TableIdentifier[] tableIDs = _from.toTableArray();
        return (tableIDs[i]);
    }

    /**
     * Gets the number of tables being from.
     */
    public int getFromCount() {
        return _from.getTableCount();
    }

    /**
     * Sets the {@link WhereNode where tree} for this query.
     * @param where a {@link WhereNode}
     * @throws IllegalStateException if I have already been resolved
     */
    public void setWhere(WhereNode where) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _where = where;
    }

    /**
     * Returns the {@link WhereNode where tree} for this query.
     * Clients should treat the returned value as immutable.
     * @return the {@link WhereNode where tree} for this query, or <tt>null</tt>.
     */
    public WhereNode getWhere() {
        return _where;
    }

    /**
     * Sets the order by clause for this query.
     * @param orderby a {@link List} of {@link OrderNode}s.
     * @throws IllegalStateException if I have already been resolved
     */
    public void setOrderBy(List orderby) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _orderBy = orderby;
    }

    /**
     * Appends an {@link OrderNode} to the order by clause for this query
     * @param orderby an {@link OrderNode} to append
     * @throws IllegalStateException if I have already been resolved
     */
    public void addOrderBy(OrderNode orderby) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _orderBy.add(orderby);
    }

    /**
     * Gets the <i>i</i><sup>th</sup> {@link OrderNode} in my order by clause.
     * Clients should treat the returned value as immutable.
     * @param i the zero-based index
     */
    public OrderNode getOrderBy(int i) {
        return (OrderNode)(_orderBy.get(i));
    }

    /**
     * Gets the number of {@link OrderNode}s in my query.
     */
    public int getOrderByCount() {
        return (null == _orderBy ? 0 : _orderBy.size());
    }

    /**
     * Determines if the {@link java.sql.ResultSet} generated
     * from this object will contain distinct tuples (default is false).
     * @param distinct true for distinct tuples
     */
    public void setDistinct(boolean distinct) {
        if(_resolved) { throw new IllegalStateException("Already resolved."); }
        _distinct = distinct;
    }

    /**
     * Indicates if the {@link java.sql.ResultSet} generated from
     * this object will contain distinct tuples.
     * @return <code>true</code> for distinct tuples
     */
    public boolean getDistinct() {
        return _distinct;
    }

    public void setLimit(Literal limit) {
        _limit = limit;
    }

    public Literal getLimit() {
        return _limit;
    }

    public void setOffset(Literal offset) {
        _offset = offset;
    }

    public Literal getOffset() {
        return _offset;
    }

    /** @deprecated */
    private TableIdentifier[] getFromArray() {
        if (_from != null) {
            return (_from.toTableArray());
        }
        return (null);
    }

    //-------------------------------------------------- Command Implementation
    
    /**
     * Execute this command, returning a {@link java.sql.ResultSet}.
     * @return the {@link java.sql.ResultSet} generated by this command.
     * @throws SQLException
     */
    public AxionResultSet executeQuery(Database db) throws AxionException {
        resolve(db);
        if(_currentDatabase != db) {
            processQuery(db);
        } else {
            _rows.reset();
        }
        RowIterator rows = _rows;

        // apply aggregate function, if any
        if(_foundAggregateFunction) {
            AggregateFunction vfn = (AggregateFunction)(getSelect(0));
            SimpleRow row = new SimpleRow(1);
            row.set(0,vfn.evaluate(new RowIteratorRowDecoratorIterator(rows,new RowDecorator(_colIdToFieldMap))));
            SingleRowIterator fniter = new SingleRowIterator(row);
            Selectable[] selarray = new Selectable[] { new ColumnIdentifier(new TableIdentifier(),vfn.getName(),null,vfn.getDataType()) };
            HashMap fieldmap = new HashMap();
            fieldmap.put(selarray[0],new Integer(0));
            return new AxionResultSet(new RowIteratorRowDecoratorIterator(fniter,new RowDecorator(fieldmap)),selarray);
        } else {
            return new AxionResultSet(new RowIteratorRowDecoratorIterator(rows,new RowDecorator(_colIdToFieldMap)),_selected);
        }
    }

    protected Iterator getBindVariableIterator() {
        List list = new ArrayList();
        for(int i=0;i<_select.size();i++) {
            appendBindVariables(getSelect(i),list);
        }
        appendBindVariables(getWhere(),list);
        appendBindVariables(getLimit(),list);
        appendBindVariables(getOffset(),list);
        return list.iterator();
    }
    
    private void processQuery(Database db) throws AxionException {
        _currentDatabase = db;

        // the map of column identifiers to field locations (index of column in row's array)
        _colIdToFieldMap = new HashMap();
        _indexOffset = 0;
        _literals = createLiteralList();
        
        RowIterator rows = null;

        _unappliedWhereNodes = processWhereTree(getWhere());
        
        rows = processFromTree(_from, db);

        // And apply any remaining where nodes to the join.
        Iterator unappliedWhereNodeIter = _unappliedWhereNodes.iterator();
        while(unappliedWhereNodeIter.hasNext()) {
            WhereNode node = (WhereNode)(unappliedWhereNodeIter.next());
            rows = new FilteringRowIterator(rows,
                                            new RowDecorator(_colIdToFieldMap),
                                            node);
        }

        _selected = generateSelectArrayForResultSet(db);

        // apply distinct, if needed
        if(_distinct) {
            rows = new DistinctRowIterator(rows,_colIdToFieldMap, _selected); 
        }
            
        // apply the ORDER BY if any
        if(getOrderByCount() > 0) {
            ComparatorChain orderChain = generateOrderChain(_colIdToFieldMap);
            ArrayList list = new ArrayList();
            while(rows.hasNext()) {
                list.add(rows.next());
            }
            Collections.sort(list, orderChain);
            rows = new ListIteratorRowIterator(list.listIterator());
        }
        
        // if there's a limit, apply it
        if(null != getLimit() || null != getOffset()) {
            rows = new LimitingRowIterator(rows,getLimit(),getOffset());
        }

        // We're done.
        _rows = rows;
    }

    private RowIterator processFromTree(FromNode from, Database db) 
    throws AxionException {
        TableIdentifier temp = null;
        RowIterator leftiter = null;
        RowIterator rightiter = null;
        RowIterator literaliter = null;
        RowIterator row = null;
        int iterCount = 0;
        int lcolpos = -1;
        int rcolcount = -1;
        ColumnIdentifier rcol = null;
        ColumnIdentifier lcol = null;

        if (from != null) {
            WhereNode condition = from.getCondition();
            
            // determine if we can use index to carry out join (faster).
            if (from.getTableCount() > 1 && (from.getType() == FromNode.TYPE_INNER || from.getType() == FromNode.TYPE_LEFT)) {
                if (from.getRight() instanceof TableIdentifier && condition != null && condition instanceof LeafWhereNode) {
                    TableIdentifier tid = (TableIdentifier) from.getRight();
                    lcol = (ColumnIdentifier) ((LeafWhereNode) condition).getLeft();
                    rcol = (ColumnIdentifier) ((LeafWhereNode) condition).getRight();
                    if (lcol.equals(rcol) == false) {
                        if (tid.equals(lcol.getTableIdentifier())) {
                            ColumnIdentifier tempcid = lcol;
                            lcol = rcol;
                            rcol = tempcid;
                        }
                    }
                    //System.out.println("Table : " + tid.toString());
                    //System.out.println("lcol  : " + lcol.toString());
                    //System.out.println("rcol  : " + rcol.toString());

                    // check if table has index on rcol.
                    Table table = db.getTable(tid);
                    if (table.getIndexForColumn(table.getColumn(rcol.getName())) == null) {
                        // table does not have index on rcol. Index access cannot be carried out on the table.
                        //System.out.println("Table " + tid.toString() + " does not have index on " + rcol.toString());
                        lcol = null;
                        rcol = null;
                    }
                }
            }
            
            // new syntax join is being processed; hence, apply all the conditions in the 
            // WHERE clause after doing join.
            if (condition != null) {
                _applyWhereNodesAfterJoin = true;
            }

        }
        // Get row iterator from left subtree.
        if (from != null && from.getLeft() != null) {
            Object leftChild = from.getLeft();
            if (leftChild instanceof FromNode) {
                leftiter = processFromTree((FromNode) leftChild, db);
                if (lcol != null) {
                    lcolpos = ((Integer)_colIdToFieldMap.get(lcol)).intValue();
                }
            } else if (leftChild instanceof TableIdentifier) {
                temp = (TableIdentifier) from.getLeft();
                Table left = db.getTable(temp);
                if (left == null) {
                    throw new AxionException("Table " + temp + " not found.");
                }
                if (lcol != null) {
                    lcolpos = left.getColumnIndex(lcol.getName());
                }
                leftiter = processTable(temp, db, lcol, 0);
            } else {
                throw new AxionException("From clause is badly formed");
            }
            iterCount++;
        }
        
        // Get row iterator from right subtree.
        if (from != null && from.getRight() != null) {
            Object rightChild = from.getRight();
            if (rightChild instanceof FromNode) {
                rightiter = processFromTree((FromNode) rightChild, db);
            } else if (rightChild instanceof TableIdentifier) {
                temp = (TableIdentifier) from.getRight();
                Table right = db.getTable(temp);
                if (right == null) {
                    throw new AxionException("Table " + temp + " not found.");
                }
                rightiter = processTable(temp, db, rcol, 1);
                rcolcount = right.getColumnCount();
            } else {
                throw new AxionException("From clause is badly formed");
            }
            iterCount++;
        }


        // Get row iterator for literals, if any, in the select list
        if(null != _literals) {
            Row litrow = new SimpleRow(_literals.size());
            Iterator iter = _literals.iterator();
            for(int i=0;iter.hasNext();i++) {
                Literal literal = (Literal)iter.next();
                _colIdToFieldMap.put(literal,new Integer(_indexOffset+i));
            }
            _indexOffset += _literals.size();
            literaliter = new SingleRowIterator(litrow);
            iterCount++;
            _literals = null; // set _literals to null, so that they are not processed
                              // by each recursive call to processFromTree.
        }


        if (iterCount > 1) {
            if (literaliter == null && rightiter instanceof ChangingIndexedRowIterator && from.getType() != FromNode.TYPE_RIGHT) {
                //System.out.println("IndexJoinedRowIterator being created.");
                IndexJoinedRowIterator joinedrow = new IndexJoinedRowIterator(lcolpos, rcolcount);
                joinedrow.addRowIterator(leftiter);
                joinedrow.addRowIterator(rightiter);
                joinedrow.setJoinType(from.getType());
                joinedrow.setJoinCondition(new RowDecorator(_colIdToFieldMap), from.getCondition());
                row = joinedrow;
            } else {
                SimpleJoinedRowIterator joinedrow = new SimpleJoinedRowIterator();
            if (from.getType() == FromNode.TYPE_RIGHT) {
                // Join is carried out using nested loop algorithm; hence, in case of
                // of right outer join, we make the right table as the outer table of
                // the nested loop algorithm. (Note that no change is made to _colIdToFieldmap).
                joinedrow.addRowIterator(rightiter);
                joinedrow.addRowIterator(leftiter);
            } else {
                if (leftiter != null) {
                    joinedrow.addRowIterator(leftiter);
                }
                
                if (rightiter != null) {
                    joinedrow.addRowIterator(rightiter);
                }
            }

            // if literals exist, add them to the join.
            if (literaliter != null) {
                joinedrow.addRowIterator(literaliter);
            }

            // set join type and join condition.
                joinedrow.setJoinType(from.getType());
            if (rightiter != null && leftiter != null && from.getCondition() != null) {
                joinedrow.setJoinCondition(new RowDecorator(_colIdToFieldMap), from.getCondition());
                }             
                row = joinedrow;
            }
        } else {
            // no join
            if (leftiter != null) {
                row = leftiter;
            }

            // statements of type "select 'literal'" are allowed.
            if (literaliter != null) {
                row = literaliter;
            }
        }

        return (row);
    }

    private RowIterator processTable(TableIdentifier tableident, Database db, ColumnIdentifier col, int pos) 
    throws AxionException {
        Table table = db.getTable(tableident);
        // Create a references to the RowIterator for this table.
        RowIterator tableiter = null;
        // And create  the set of WhereNodes that apply to this table.
        Set whereNodesForTable = null;
        
        if (col != null && pos == 1) {
            Index index = table.getIndexForColumn(table.getColumn(col.getName()));
            if (index != null) {
                tableiter = new ChangingIndexedRowIterator(index, table, ComparisonOperator.EQUAL);
            }
        }
        if (_applyWhereNodesAfterJoin == false) {
            for (Iterator whereNodeIter = _unappliedWhereNodes.iterator();whereNodeIter.hasNext();) {
                WhereNode node = (WhereNode) (whereNodeIter.next());
                // If the node only references this table...
                if (onlyReferencesTable(tableident,node)) {
                    // .. and we haven't yet applied an index,...
                    if (null == tableiter) {
                        // ...then try to find an index for this node.
                        tableiter = table.getIndexedRows(node,true);
                        // If we still don't have an iterator,
                        // then no index is available, so add the node
                        // to the whereNodesForTable set...
                        if(null == tableiter) {
                            if(null == whereNodesForTable) { whereNodesForTable = new HashSet(); }
                            whereNodesForTable.add(node);
                        }                    
                    } else {
                        // Else if we've already applied an index,
                        // then add the node to the whereNodesForTable
                        if(null == whereNodesForTable) { whereNodesForTable = new HashSet(); }
                        whereNodesForTable.add(node);
                    }
                    // Remove the WhereNode from the unapplied where nodes, 
                    // since we've either added it to the whereNodesForTable set,
                    // or we applied it via the index.
                    whereNodeIter.remove();                
                }
            }
        }

        // If we still don't have a RowIterator for this table,
        // then we'll use a table scan.
        if(null == tableiter) {
            tableiter = table.getRowIterator(true);
        }

        // Apply any unapplied whereNodesForTable.
        if(null != whereNodesForTable && !whereNodesForTable.isEmpty()) {
            Map localmap = new HashMap();
            populateColumnIdToFieldMap(localmap,tableident,0,db);
            Iterator whereNodesForTableIter = whereNodesForTable.iterator();
            while(whereNodesForTableIter.hasNext()) {
                WhereNode node = (WhereNode)(whereNodesForTableIter.next());
                tableiter = new FilteringRowIterator(tableiter,
                                                     new RowDecorator(localmap),
                                                     node);
            }
        }

        populateColumnIdToFieldMap(_colIdToFieldMap, tableident, _indexOffset, db);
        _indexOffset += table.getColumnCount();

        return (tableiter);
    }
    
    private Selectable[] generateSelectArrayForResultSet(Database db) throws AxionException {
        List list = new ArrayList(getSelectCount());
        TableIdentifier[] tables = getFromArray(); 
        for(int i=0;i<getSelectCount();i++) {
            Selectable sel = getSelect(i);
            if(sel instanceof ColumnIdentifier) {
                ColumnIdentifier colid = (ColumnIdentifier)sel;
                if("*".equals(colid.getName())) {
                    if(null == colid.getTableName()) {
                        for(int j=0;j<getFromCount();j++) {
                            TableIdentifier tableID = tables[j];
                            Table table = db.getTable(tableID);
                            for(Iterator iter = table.getColumnIdentifiers(); iter.hasNext();) {
                                ColumnIdentifier colId = (ColumnIdentifier)iter.next();
                                colId.setTableIdentifier(tableID);
                                list.add(colId);
                            }
                        }
                    } else {
                        Table table = db.getTable(colid.getTableIdentifier());
                        for(Iterator iter = table.getColumnIdentifiers();iter.hasNext();) {
                            list.add(iter.next());
                        }
                    }
                } else {
                    list.add(colid);
                }
            } else {
                list.add(sel);
            }
        }
        return(Selectable[])(list.toArray(new Selectable[list.size()]));
    }

    /** Unsupported */
    public int executeUpdate(Database database) throws AxionException {
      throw new UnsupportedOperationException("Use executeQuery, not executeUpdate.");
    }

    public boolean execute(Database database) throws AxionException {
        setResultSet(executeQuery(database));
        return (getResultSet() != null);
    }

    private void resolve(Database db) throws AxionException {
        if(!_resolved) {

            // resolve the Seletables

            TableIdentifier[] tables = getFromArray();
            // resolve SELECT part
            for(int i=0;i<getSelectCount();i++) {
                setSelect(i,db.resolveSelectable(getSelect(i),tables));
            }
            db.resolveFromNode(getFrom(), tables);
            // resolve WHERE part
            db.resolveWhereNode(getWhere(),tables);
            
            // resolve ORDER BY part
            if(null != _orderBy) {
                for(int i=0;i<_orderBy.size();i++) {
                    OrderNode ob = (OrderNode)(_orderBy.get(i));
                    ob.setSelectable(db.resolveSelectable(ob.getSelectable(),tables));
                }
            }

            _resolved = true;
               
            // check for aggregate functions
            boolean foundScalar = false;
            for(int i=0;i<getSelectCount();i++) {
                if(getSelect(i) instanceof AggregateFunction) {
                    if(foundScalar) {
                        throw new AxionException("Can't select both scalar values and aggregate functions.");
                    } else if(_foundAggregateFunction) {
                        throw new AxionException("Currently can't select more than one aggregate function at a time.");
                    } else {
                        _foundAggregateFunction = true;
                    }
                } else {
                    if(_foundAggregateFunction) {
                        throw new AxionException("Can't select both scalar values and aggregate functions.");
                    }
                    foundScalar = true;
                }
            }
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT ");
        if(_distinct) {
            buf.append("DISTINCT ");
        }
        {
            Iterator iter = _select.iterator();
            while(iter.hasNext()) {
                buf.append(iter.next());
                if(iter.hasNext()) {
                    buf.append(", ");
                }
            }
        }
        if (null != _from) {
            buf.append(" FROM ");
            buf.append(_from);
        }
        
        if(null != _where) {
            buf.append(" WHERE ");
            buf.append(_where);
        }
        if(null != _orderBy && !_orderBy.isEmpty()) {
            buf.append(" ORDER BY ");
            {
                Iterator iter = _orderBy.iterator();
                while(iter.hasNext()) {
                    buf.append(iter.next());
                    if(iter.hasNext()) {
                        buf.append(", ");
                    }
                }
            }
        }
        return buf.toString();
    }

    /** 
     * Create a list of all the literals that have been selected,
     * returning null if there aren't any.
     */
    private List createLiteralList() throws AxionException{
        List literals = null;
        for(int i=0;i<this.getSelectCount();i++) {
            if(getSelect(i) instanceof Literal) {
                if(null == literals) {
                    literals = new ArrayList();
                }
                literals.add(this.getSelect(i));
            }
        }
        return literals;
    }

    private boolean onlyReferencesTable(TableIdentifier table, WhereNode node) {
        ReferencesOtherTablesWhereNodeVisitor v = new ReferencesOtherTablesWhereNodeVisitor(table);
        v.visit(node);
        return v.getResult();
    }

    private ComparatorChain generateOrderChain(Map indexMap) {
        ComparatorChain chain = new ComparatorChain();
        for(int i=0;i<getOrderByCount();i++) {
            if(getOrderBy(i).isDescending()) {
                chain.setReverseSort(i);
            }
            chain.addComparator(new RowComparator(getOrderBy(i).getSelectable(),new RowDecorator(indexMap)));

        }
        return chain;
    }

    private void populateColumnIdToFieldMap(Map indexMap, TableIdentifier tableIdent, int offset, Database db)
    throws AxionException {
        Table table = db.getTable(tableIdent);
        if(null == table) { 
            throw new AxionException("Table " + tableIdent + " not found.");
        }
        for(int j=0,J=table.getColumnCount();j<J;j++) {
            ColumnIdentifier id = null;
            // determine which selected column id matches, if any
            for(int k=0,K=getSelectCount();k<K;k++) {
                Selectable sel = getSelect(k);
                if(sel instanceof ColumnIdentifier) {
                    ColumnIdentifier cSel = (ColumnIdentifier)sel;
                    if(tableIdent.equals(cSel.getTableIdentifier()) &&
                        cSel.getName().equals(table.getColumn(j).getName())) {
                        id = cSel;
                        break;
                    }
                }
            }
            if(null == id) {
                id = new ColumnIdentifier(tableIdent, table.getColumn(j).getName());
            }
            indexMap.put(id, new Integer(offset + j));
        }
    }

    //----------------------------------------------------------------- Members

    private List _select = new ArrayList();
    private FromNode _from = null;
    private WhereNode _where = null;
    private List _orderBy = new ArrayList();
    private boolean _distinct = false;
    private boolean _resolved = false;
    private boolean _foundAggregateFunction = false;
    private Literal _limit = null;
    private Literal _offset = null;
    private Database _currentDatabase = null;
    //---
    private Map _colIdToFieldMap = null;
    private int _indexOffset = 0;
    private Set _unappliedWhereNodes = null;
    private List _literals = null;
    private RowIterator _rows = null;
    private Selectable[] _selected = null;
    private boolean _applyWhereNodesAfterJoin = false;
}
