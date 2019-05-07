public <X, C extends JComponent> void addOptionComponent(ConfigPanel panel, OptionComponent<X, C> oc) {
    panel.addComponent(oc);
    oc.addChangeListener(_changeListener);
}
