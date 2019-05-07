/**
	 * Sets the default {@link DataMode} for the ClientHandler
	 * @since 1.4.6
	 */
public void setDefaultDataMode(DataMode dataMode, DataType dataType) throws IOException {
    if (dataType == DataType.IN)
        this.defaultDataModeIN = dataMode;
    if (dataType == DataType.OUT)
        this.defaultDataModeOUT = dataMode;
}
