/**
	 * Returns an new instance of SugiyamaLayoutSettings
	 */
public JGraphLayoutSettings createSettings() {
    return new AnnealingLayoutSettings(this, false);
}
