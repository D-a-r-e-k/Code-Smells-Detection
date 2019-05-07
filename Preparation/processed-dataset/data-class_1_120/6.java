// MUTATORS:  
public int add(final int exception_index) {
    final int newoffset = m_exceptions.size();
    // use size() if class becomes non-final  
    m_exceptions.add(exception_index);
    return newoffset;
}
