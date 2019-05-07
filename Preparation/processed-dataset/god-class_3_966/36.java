private ComparatorChain generateOrderChain(Map indexMap) {
    ComparatorChain chain = new ComparatorChain();
    for (int i = 0; i < getOrderByCount(); i++) {
        if (getOrderBy(i).isDescending()) {
            chain.setReverseSort(i);
        }
        chain.addComparator(new RowComparator(getOrderBy(i).getSelectable(), new RowDecorator(indexMap)));
    }
    return chain;
}
