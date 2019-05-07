/** Adds all of the components for the Key Bindings panel of the preferences window.
   */
private void _setupKeyBindingsPanel(ConfigPanel panel) {
    // using a treemap because it automatically sorts element upon insertion 
    TreeMap<String, VectorKeyStrokeOptionComponent> _comps = new TreeMap<String, VectorKeyStrokeOptionComponent>();
    VectorKeyStrokeOptionComponent vksoc;
    for (KeyStrokeData ksd : KeyBindingManager.ONLY.getKeyStrokeData()) {
        if (ksd.getOption() != null) {
            // Get the tooltip, or default to its name, if none 
            Action a = ksd.getAction();
            // pick the short description as name, if available 
            String name = (String) a.getValue(Action.SHORT_DESCRIPTION);
            // if not available, pick the KeyStrokeData name instead 
            if (name == null || name.trim().equals(""))
                name = ksd.getName();
            // pick the long description as name, if available 
            String desc = (String) a.getValue(Action.LONG_DESCRIPTION);
            // if not available, pick the name from above instead 
            if (desc == null || desc.trim().equals(""))
                desc = name;
            // if the map already contains this name, use the description instead 
            if (_comps.containsKey(name)) {
                name = desc;
                // if the map already contains the description as well (bad developers!), then use the option's name 
                if (_comps.containsKey(name)) {
                    name = ksd.getOption().getName();
                }
            }
            vksoc = new VectorKeyStrokeOptionComponent(ksd.getOption(), name, this, desc);
            if (vksoc != null)
                _comps.put(name, vksoc);
        }
    }
    Iterator<VectorKeyStrokeOptionComponent> iter = _comps.values().iterator();
    while (iter.hasNext()) {
        VectorKeyStrokeOptionComponent x = iter.next();
        addOptionComponent(panel, x);
    }
    panel.displayComponents();
}
