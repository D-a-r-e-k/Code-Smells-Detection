/**
   * This handles the changes if the source property is modified.
   *
   * As defined in net.suberic.util.ValueChangeListener.
   */
public void valueChanged(String changedValue) {
    if (changedValue.equals(getFolderProperty() + ".autoCache") || changedValue.equals(getParentStore().getStoreProperty() + ".autoCache")) {
        if (!getCacheHeadersOnly()) {
            autoCache = Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getParentStore().getStoreProperty() + ".autoCache", Pooka.getProperty("Pooka.autoCache", "false")))).equalsIgnoreCase("true");
        }
    } else if (changedValue.equals(getFolderProperty() + ".cacheHeadersOnly") || changedValue.equals(getParentStore().getStoreProperty() + ".cacheHeadersOnly")) {
        if (!getCacheHeadersOnly()) {
            autoCache = Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getParentStore().getStoreProperty() + ".autoCache", Pooka.getProperty("Pooka.autoCache", "false"))).equalsIgnoreCase("true");
        }
        createFilters();
        if (getFolderNode() != null)
            getFolderNode().popupMenu = null;
    } else {
        super.valueChanged(changedValue);
    }
}
