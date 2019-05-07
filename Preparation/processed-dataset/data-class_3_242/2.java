public void setAction(Action action) {
    this.action = action;
    if (action instanceof MenuItemEnabledListener) {
        MenuItemEnabledListener listener = (MenuItemEnabledListener) action;
        setEnabledListener(listener);
    }
    if (action instanceof MenuItemSelectedListener) {
        MenuItemSelectedListener listener = (MenuItemSelectedListener) action;
        setSelectedListener(listener);
    }
}
