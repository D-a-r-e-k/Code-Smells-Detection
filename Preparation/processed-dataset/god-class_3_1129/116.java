/**
     * @see org.apache.jmeter.samplers.AbstractSampler#applies(org.apache.jmeter.config.ConfigTestElement)
     */
@Override
public boolean applies(ConfigTestElement configElement) {
    String guiClass = configElement.getProperty(TestElement.GUI_CLASS).getStringValue();
    return APPLIABLE_CONFIG_CLASSES.contains(guiClass);
}
