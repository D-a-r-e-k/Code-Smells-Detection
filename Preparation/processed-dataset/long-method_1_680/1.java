/**
	 * Gets the Collection schema dictionary.
	 * @return schema	an overview of the collection fields
	 */
public PdfCollectionSchema getSchema() {
    return (PdfCollectionSchema) get(PdfName.SCHEMA);
}
