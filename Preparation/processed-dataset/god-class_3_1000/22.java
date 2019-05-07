public void refresh(CellView[] views, boolean create) {
    if (views != null)
        for (int i = 0; i < views.length; i++) refresh(views[i], create);
}
