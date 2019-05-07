// Cloneable:  
/**
     * Performs a deep copy.
     */
public Object clone() {
    try {
        final DeclaredExceptionTable _clone = (DeclaredExceptionTable) super.clone();
        // deep clone:  
        _clone.m_exceptions = (IntVector) m_exceptions.clone();
        return _clone;
    } catch (CloneNotSupportedException e) {
        throw new InternalError(e.toString());
    }
}
