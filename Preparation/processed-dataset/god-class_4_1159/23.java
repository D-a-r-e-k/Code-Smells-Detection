/**
     * Gets a fresh list of all colonies this player owns.
     * It is an error to call this on non-European players.
     *
     * @return A fresh list of the colonies this player owns.
     */
public List<Colony> getColonies() {
    ArrayList<Colony> colonies = new ArrayList<Colony>();
    for (Settlement s : settlements) {
        if (s instanceof Colony) {
            colonies.add((Colony) s);
        } else {
            throw new RuntimeException("getColonies can only be called for players whose settlements are colonies.");
        }
    }
    return colonies;
}
