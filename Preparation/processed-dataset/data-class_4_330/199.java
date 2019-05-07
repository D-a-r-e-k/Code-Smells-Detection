protected void writeAttributes(XMLStreamWriter out, Player player, boolean showAll, boolean toSavedGame) throws XMLStreamException {
    out.writeAttribute(ID_ATTRIBUTE, getId());
    out.writeAttribute("username", name);
    out.writeAttribute("nationID", nationID);
    if (nationType != null) {
        out.writeAttribute("nationType", nationType.getId());
    }
    out.writeAttribute("admin", Boolean.toString(admin));
    out.writeAttribute("ready", Boolean.toString(ready));
    out.writeAttribute("dead", Boolean.toString(dead));
    out.writeAttribute("bankrupt", Boolean.toString(bankrupt));
    out.writeAttribute("playerType", playerType.toString());
    out.writeAttribute("ai", Boolean.toString(ai));
    out.writeAttribute("tax", Integer.toString(tax));
    // @compat 0.9.x 
    out.writeAttribute("numberOfSettlements", Integer.toString(getNumberOfSettlements()));
    // end compatibility code 
    if (showAll || toSavedGame || equals(player)) {
        out.writeAttribute("gold", Integer.toString(gold));
        out.writeAttribute("immigration", Integer.toString(immigration));
        out.writeAttribute("liberty", Integer.toString(liberty));
        out.writeAttribute("interventionBells", Integer.toString(interventionBells));
        if (currentFather != null) {
            out.writeAttribute("currentFather", currentFather.getId());
        }
        out.writeAttribute("immigrationRequired", Integer.toString(immigrationRequired));
        out.writeAttribute("attackedByPrivateers", Boolean.toString(attackedByPrivateers));
        out.writeAttribute("oldSoL", Integer.toString(oldSoL));
        out.writeAttribute("score", Integer.toString(score));
    } else {
        out.writeAttribute("gold", Integer.toString(-1));
        out.writeAttribute("immigration", Integer.toString(-1));
        out.writeAttribute("liberty", Integer.toString(-1));
        out.writeAttribute("immigrationRequired", Integer.toString(-1));
    }
    if (newLandName != null) {
        out.writeAttribute("newLandName", newLandName);
    }
    if (independentNationName != null) {
        out.writeAttribute("independentNationName", independentNationName);
    }
    if (entryLocation != null) {
        out.writeAttribute("entryLocation", entryLocation.getId());
    }
    for (RegionType regionType : RegionType.values()) {
        String key = regionType.getNameIndexKey();
        int index = getNameIndex(key);
        if (index > 0)
            out.writeAttribute(key, Integer.toString(index));
    }
}
