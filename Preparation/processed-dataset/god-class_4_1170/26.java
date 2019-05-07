/** Adds all of the components for the JVMs panel of the preferences window. */
private void _setupJVMsPanel(ConfigPanel panel) {
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.MASTER_JVM_XMX, "Maximum Heap Size for Main JVM in MB", this, "The maximum heap the Main JVM can use. Select blank for default."));
    addOptionComponent(panel, new StringOptionComponent(OptionConstants.MASTER_JVM_ARGS, "JVM Args for Main JVM", this, "The command-line arguments to pass to the Main JVM."));
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.SLAVE_JVM_XMX, "Maximum Heap Size for Interactions JVM in MB", this, "The maximum heap the Interactions JVM can use. Select blank for default"));
    addOptionComponent(panel, new StringOptionComponent(OptionConstants.SLAVE_JVM_ARGS, "JVM Args for Interactions JVM", this, "The command-line arguments to pass to the Interactions JVM."));
    panel.displayComponents();
}
