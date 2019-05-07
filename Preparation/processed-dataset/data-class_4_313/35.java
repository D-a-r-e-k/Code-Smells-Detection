public String schemaDocument2SystemId(XSDocumentInfo schemaDoc) {
    return (String) fDoc2SystemId.get(schemaDoc.fSchemaElement);
}
