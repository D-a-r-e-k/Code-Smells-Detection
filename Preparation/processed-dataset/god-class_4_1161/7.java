/**
     * Is a colony badly defended?
     * Deliberately does not require defenders for small colonies.
     *
     * @param colony The <code>Colony</code> to consider.
     * @return True if the colony needs more defenders.
     */
public static boolean isBadlyDefended(Colony colony) {
    return colony.getTotalDefencePower() < 1.25f * colony.getWorkLocationUnitCount() - 2.5f;
}
