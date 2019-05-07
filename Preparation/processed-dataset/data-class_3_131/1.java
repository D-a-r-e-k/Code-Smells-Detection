//-------------------------------------------------------------- Properties 
public void setSelect(List columns) {
    if (_resolved) {
        throw new IllegalStateException("Already resolved.");
    }
    _select = columns;
}
