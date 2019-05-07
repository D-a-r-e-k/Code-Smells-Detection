public void refresh(CellView view, boolean create) {
    if (view != null) {
        view.refresh(this, this, create);
        CellView[] children = view.getChildViews();
        for (int i = 0; i < children.length; i++) refresh(children[i], create);
    }
}
