/**
     * Returns a sorted list of all Colonies this player owns.
     *
     * @param c A comparator to operate on the colony list.
     * @return A fresh list of the colonies this player owns.
     */
public List<Colony> getSortedColonies(Comparator<Colony> c) {
    List<Colony> colonies = getColonies();
    Collections.sort(colonies, c);
    return colonies;
}
