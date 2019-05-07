public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT ");
    if (_distinct) {
        buf.append("DISTINCT ");
    }
    {
        Iterator iter = _select.iterator();
        while (iter.hasNext()) {
            buf.append(iter.next());
            if (iter.hasNext()) {
                buf.append(", ");
            }
        }
    }
    if (null != _from) {
        buf.append(" FROM ");
        buf.append(_from);
    }
    if (null != _where) {
        buf.append(" WHERE ");
        buf.append(_where);
    }
    if (null != _orderBy && !_orderBy.isEmpty()) {
        buf.append(" ORDER BY ");
        {
            Iterator iter = _orderBy.iterator();
            while (iter.hasNext()) {
                buf.append(iter.next());
                if (iter.hasNext()) {
                    buf.append(", ");
                }
            }
        }
    }
    return buf.toString();
}
