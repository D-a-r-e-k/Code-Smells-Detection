public void update(CellView view) {
    if (view != null) {
        view.update(this);
        CellView[] children = view.getChildViews();
        for (int i = 0; i < children.length; i++) update(children[i]);
    }
}
