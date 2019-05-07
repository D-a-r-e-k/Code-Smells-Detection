/**
	 * Sets an indirect object reference to a RichMediaConfiguration
	 * dictionary that shall also be referenced by the Configurations
	 * array in the RichMediaContent dictionary (which is part of
	 * the RichMediaAnnotation object).
	 * @param	configuration	an indirect reference
	 */
public void setConfiguration(PdfIndirectReference configuration) {
    put(PdfName.CONFIGURATION, configuration);
}
