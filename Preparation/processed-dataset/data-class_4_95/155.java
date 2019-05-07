/**
	 * Sets the default {@link DataMode} for the ClientHandler
	 * @since 1.4.6
	 */
public void setDefaultDataMode(DefaultDataMode defaultDataMode) throws IOException {
    defaultDataModeIN = defaultDataMode.getDataMode(DataType.IN);
    defaultDataModeOUT = defaultDataMode.getDataMode(DataType.OUT);
    ;
}
