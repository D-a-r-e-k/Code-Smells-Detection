/*
     * (non-Javadoc)
     * 
     * @see megamek.common.GameListener#gamePhaseChange(megamek.common.GamePhaseChangeEvent)
     */
public void gamePhaseChange(GamePhaseChangeEvent e) {
    if (bot.game.getPhase() == IGame.Phase.PHASE_LOUNGE || bot.game.getPhase() == IGame.Phase.PHASE_STARTING_SCENARIO) {
        notifyOfBot();
    }
}
