public String getTermName(int termId) {
    Object term = fElemMap[termId];
    return (term != null) ? term.toString() : null;
}
