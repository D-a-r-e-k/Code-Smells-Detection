/** 
     * Create a list of all the literals that have been selected,
     * returning null if there aren't any.
     */
private List createLiteralList() throws AxionException {
    List literals = null;
    for (int i = 0; i < this.getSelectCount(); i++) {
        if (getSelect(i) instanceof Literal) {
            if (null == literals) {
                literals = new ArrayList();
            }
            literals.add(this.getSelect(i));
        }
    }
    return literals;
}
