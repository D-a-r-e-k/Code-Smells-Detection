/**
     * Makes an XML-representation of this object.
     *
     * @param out The output stream.
     * @throws XMLStreamException if there are any problems writing to the
     *             stream.
     */
protected void toXMLImpl(XMLStreamWriter out) throws XMLStreamException {
    // Start element: 
    out.writeStartElement(getXMLElementTagName());
    // Add attributes: 
    out.writeAttribute(FreeColObject.ID_ATTRIBUTE_TAG, getId());
    if (difficultyLevel != null) {
        out.writeAttribute("difficultyLevel", difficultyLevel);
    }
    // copy the order of section in specification.xml 
    writeSection(out, "modifiers", specialModifiers);
    writeSection(out, "events", events);
    writeSection(out, "disasters", disasters);
    writeSection(out, "goods-types", goodsTypeList);
    writeSection(out, "resource-types", resourceTypeList);
    writeSection(out, "tile-types", tileTypeList);
    writeSection(out, "roles", roles);
    writeSection(out, "equipment-types", equipmentTypes);
    writeSection(out, "tileimprovement-types", tileImprovementTypeList);
    writeSection(out, "unit-types", unitTypeList);
    writeSection(out, "building-types", buildingTypeList);
    writeSection(out, "founding-fathers", foundingFathers);
    writeSection(out, "european-nation-types", europeanNationTypes);
    writeSection(out, "european-nation-types", REFNationTypes);
    writeSection(out, "indian-nation-types", indianNationTypes);
    writeSection(out, "nations", nations);
    // option tree has been flattened 
    out.writeStartElement("options");
    for (OptionGroup item : allOptionGroups.values()) {
        if ("".equals(item.getGroup())) {
            item.toXML(out);
        }
    }
    out.writeEndElement();
    // End element: 
    out.writeEndElement();
}
