/**
     * Initialize the highSeas.
     * Needs to be public until the backward compatibility code in
     * FreeColServer is gone.
     */
public void initializeHighSeas() {
    Game game = getGame();
    highSeas = new HighSeas(game);
    if (europe != null)
        highSeas.addDestination(europe);
    if (game.getMap() != null)
        highSeas.addDestination(game.getMap());
}
