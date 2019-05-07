/**
	 * @param presetConfig The presetConfig to set.
	 */
public void setPresetConfig(Properties presetConfig) {
    this.presetConfig = presetConfig;
    loadConfiguration(CONFIG_KEY_RUN);
}
