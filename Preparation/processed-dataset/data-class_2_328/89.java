/**
     * Applies the given difficulty level to the current
     * specification.
     *
     * @param level difficulty level to apply
     */
public void applyDifficultyLevel(OptionGroup level) {
    logger.fine("Applying difficulty level " + level.getId());
    addOptionGroup(level, true);
    for (FreeColGameObjectType type : allTypes.values()) {
        type.applyDifficultyLevel(level);
    }
    this.difficultyLevel = level.getId();
}
