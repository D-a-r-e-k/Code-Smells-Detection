/**
     * Common utility routine to retrieve animation speed.
     *
     * @param unit The <code>Unit</code> to be animated.
     * @return The animation speed.
     */
public int getAnimationSpeed(Unit unit) {
    String key = (freeColClient.getMyPlayer() == unit.getOwner()) ? ClientOptions.MOVE_ANIMATION_SPEED : ClientOptions.ENEMY_MOVE_ANIMATION_SPEED;
    return freeColClient.getClientOptions().getInteger(key);
}
