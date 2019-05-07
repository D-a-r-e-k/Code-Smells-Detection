/**
	 * Sets the Collection schema dictionary.
	 * @param schema	an overview of the collection fields
	 */
public void setSchema(PdfCollectionSchema schema) {
    put(PdfName.SCHEMA, schema);
}
