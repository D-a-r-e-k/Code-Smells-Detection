/**
     * Updates this object from an XML-representation of a
     * <code>ColonyPlan</code>.
     *
     * @param element The XML-representation.
     */
public void readFromXMLElement(Element element) {
    String colonyId = element.getAttribute(FreeColObject.ID_ATTRIBUTE);
    colony = getAIMain().getGame().getFreeColGameObject(colonyId, Colony.class);
    // TODO: serialize profile? 
    profileType = ProfileType.getProfileTypeFromSize(colony.getUnitCount());
}
