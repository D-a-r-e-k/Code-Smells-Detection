/**
	 * Returns the default {@link DataMode} for the ClientHandler
	 * @since 1.4.6
	 */
public DataMode getDefaultDataMode(DataType dataType) {
    if (dataType == DataType.IN)
        return defaultDataModeIN;
    if (dataType == DataType.OUT)
        return defaultDataModeOUT;
    else
        throw new IllegalArgumentException("Unknown DataType: " + dataType);
}
