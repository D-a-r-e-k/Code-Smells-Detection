/**
     * Gets the total percentage of rebels in all this player's colonies.
     *
     * @return The total percentage of rebels in all this player's colonies.
     */
public int getSoL() {
    int sum = 0;
    int number = 0;
    for (Colony c : getColonies()) {
        sum += c.getSoL();
        number++;
    }
    if (number > 0) {
        return sum / number;
    } else {
        return 0;
    }
}
