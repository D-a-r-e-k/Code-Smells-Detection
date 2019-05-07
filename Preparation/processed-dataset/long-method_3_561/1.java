private RowIterator processFromTree(FromNode from, Database db) throws AxionException {
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
                lcolpos = ((Integer) _colIdToFieldMap.get(lcol)).intValue();
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
    if (null != _literals) {
        Row litrow = new SimpleRow(_literals.size());
        Iterator iter = _literals.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            Literal literal = (Literal) iter.next();
            _colIdToFieldMap.put(literal, new Integer(_indexOffset + i));
        }
        _indexOffset += _literals.size();
        literaliter = new SingleRowIterator(litrow);
        iterCount++;
        _literals = null;
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
