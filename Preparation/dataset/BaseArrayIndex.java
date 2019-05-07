 /*
 * $Id: BaseArrayIndex.java,v 1.12 2003/05/02 13:25:42 rwald Exp $
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

package org.axiondb.engine;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.axiondb.AxionException;
import org.axiondb.Column;
import org.axiondb.ComparisonOperator;
import org.axiondb.Index;
import org.axiondb.IndexLoader;
import org.axiondb.Row;
import org.axiondb.RowIterator;
import org.axiondb.RowSource;
import org.axiondb.Table;
import org.axiondb.engine.rowiterators.EmptyRowIterator;
import org.axiondb.engine.rowiterators.LazyRowRowIterator;
import org.axiondb.engine.rowiterators.SingleRowIterator;
import org.axiondb.event.RowEvent;

/**
 * Abstract base implemenation for {@link Index indices}
 * that maintain an in-memory, sorted array of key values 
 * (and their associated row identifiers).
 * This type of index is fast to read, relatively slow to write
 * and somewhat memory expensive when very large.
 * 
 * @version $Revision: 1.12 $ $Date: 2003/05/02 13:25:42 $
 * @author Rodney Waldhoff
 * @author Chuck Burdick
 */
public abstract class BaseArrayIndex extends BaseIndex implements Index {
    public abstract IndexLoader getIndexLoader();
    protected abstract int find(Object value, boolean required);
    protected abstract int insertKey(Object value) throws AxionException;
    protected abstract int removeKey(Object value) throws AxionException;
    protected abstract void removeKeyAt(int index) throws AxionException;
    protected abstract List getKeyList();
    protected abstract List getKeyList(int minIndex, int maxIndex);

    public BaseArrayIndex(String name, Column column, boolean unique) {
        super(name, column, unique);
    }

    public BaseArrayIndex(String name, Column column, boolean unique, IntList values) {
        super(name, column, unique);
        _rowIds = values;
    }
    
    public boolean supportsOperator(ComparisonOperator op) {
        if(ComparisonOperator.EQUAL.equals(op)) {
            if(isUnique()) {
                return true;
            } else {
                return getIndexedColumn().getDataType().supportsSuccessor();
            }
        } else if(ComparisonOperator.GREATER_THAN.equals(op)) {
            return getIndexedColumn().getDataType().supportsSuccessor();
        } else if(ComparisonOperator.GREATER_THAN_OR_EQUAL.equals(op)) {
            return true;
        } else if(ComparisonOperator.LESS_THAN.equals(op)) {
            return true;
        } else if(ComparisonOperator.LESS_THAN_OR_EQUAL.equals(op)) {
            return getIndexedColumn().getDataType().supportsSuccessor();
        } else {
            return false;
        }
    }

    public RowIterator getRowIterator(RowSource source, ComparisonOperator op, Object value) throws AxionException {
        Object convertedValue = getIndexedColumn().getDataType().convert(value);
        
        if(null == convertedValue) {
            // null fails all comparisions I support
            return EmptyRowIterator.INSTANCE;
        }
        
        int minindex = 0;
        int maxindex = _rowIds.size();
        
        if(ComparisonOperator.EQUAL.equals(op)) {
            minindex = find(convertedValue,true);
            if(minindex >= 0) {
                if(!isUnique()) {
                    maxindex = find(getIndexedColumn().getDataType().successor(convertedValue),false);
                } else {
                    maxindex = minindex+1;
                }
            } else {
                maxindex = -1;
            }
        } else if(ComparisonOperator.GREATER_THAN.equals(op)) {
            minindex = find(getIndexedColumn().getDataType().successor(convertedValue),false);
        } else if(ComparisonOperator.GREATER_THAN_OR_EQUAL.equals(op)) {
            minindex = find(convertedValue,false);
        } else if(ComparisonOperator.LESS_THAN.equals(op)) {
            maxindex = find(convertedValue,false);
        } else if(ComparisonOperator.LESS_THAN_OR_EQUAL.equals(op)) {
            maxindex = find(getIndexedColumn().getDataType().successor(convertedValue),false);
        } else {
            throw new AxionException("Unsupported operator " + op);
        }

        if(minindex < 0 || minindex >= _rowIds.size() || maxindex <= 0 || minindex == maxindex) {
            return EmptyRowIterator.INSTANCE;
        } else if(minindex+1 == maxindex) {
            // if the operator was EQUAL and we matched one row, we know the key value
            if(ComparisonOperator.EQUAL.equals(op)) {
                return new SingleRowIterator(new LazyRow(source,
                                                         _rowIds.get(minindex),
                                                         source.getColumnIndex(getIndexedColumn().getName()),
                                                         value));
            } else {
                // otherwise we don't really know the key value, so just return the lazy row
                return new SingleRowIterator(new LazyRow(source,_rowIds.get(minindex)));
            }
        } else {
            return new LazyRowRowIterator(source,
                                          _rowIds.subList(minindex,maxindex).listIterator(),
                                          source.getColumnIndex(getIndexedColumn().getName()),
                                          getKeyList(minindex,maxindex).listIterator());
        }
    }
    
    
    public void rowInserted(RowEvent event) throws AxionException {
        int colnum = event.getTable().getColumnIndex(getIndexedColumn().getName());
        Object key = event.getNewRow().get(colnum);
        if(null == key) {
            // null values aren't indexed
        } else {
            int index = insertKey(key);
            _rowIds.add(index,event.getNewRow().getIdentifier());
        }
    }

    public void rowDeleted(RowEvent event) throws AxionException {
        if(_log.isDebugEnabled()) { _log.debug(getName() + ": rowDeleted(TableModifiedEvent): " + event); }
        int colnum = event.getTable().getColumnIndex(getIndexedColumn().getName());
        Object key = event.getOldRow().get(colnum);
        if(null == key) {
            // null values aren't indexed
        } else {
            if(isUnique()) {
                // if we're unique, just remove the entry at key
                int index = removeKey(key);
                if(-1 != index) {
                    _rowIds.removeElement(index);
                }
            } else {
                // if we're not unique, scroll thru to find the right row to remove
                int index = find(key,true);
                if(-1 != index) {
                    if(_log.isDebugEnabled()) { _log.debug(getName() + ": Seeking row " + event.getOldRow().getIdentifier()); }
                    while(_rowIds.get(index) != event.getOldRow().getIdentifier()) {
                        if(_log.isDebugEnabled()) { _log.debug(getName() + ": [" + index + "]" + _rowIds.get(index) + " != " + event.getOldRow().getIdentifier()); }
                        index++;
                    }
                    _rowIds.removeElementAt(index);
                    removeKeyAt(index);
                }
            }
        }
    }

    public void rowUpdated(RowEvent event) throws AxionException {
        int colnum = event.getTable().getColumnIndex(getIndexedColumn().getName());
        Object newkey = event.getNewRow().get(colnum);
        Object oldkey = event.getOldRow().get(colnum);
        if(null == newkey ? null == oldkey : newkey.equals(oldkey)) {
            return;
        } else {
            rowDeleted(event);
            rowInserted(event);
        }
    }

    public void save(File dataDirectory) throws AxionException {
        getIndexLoader().saveIndex(this,dataDirectory);
    }

    protected IntList getValueList() {
        return _rowIds;
    }

    public void changeRowId(Table table, Row row, int oldId, int newId) throws AxionException {
        int colnum = table.getColumnIndex(getIndexedColumn().getName());
        Object key = row.get(colnum);
        if(null == key) {
            // null values aren't indexed
        } else {
            int index = find(key,true);
            for(int i=index;i<_rowIds.size();i++) {
                if(oldId == _rowIds.get(i)) {
                    _rowIds.set(i,newId);
                    break;
                }
            }
        }
    }

    private IntList _rowIds = new ArrayIntList();
    private static Log _log = LogFactory.getLog(BaseArrayIndex.class);

}
