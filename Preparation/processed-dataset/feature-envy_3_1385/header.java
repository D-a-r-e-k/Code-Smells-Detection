void method0() { 
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
