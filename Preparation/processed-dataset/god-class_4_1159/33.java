/**
     * Installs suitable settlement names (and the capital if native)
     * into the player name cache.
     *
     * @param random An optional pseudo-random number source.
     */
private void initializeSettlementNames(Random random) {
    if (settlementNames == null) {
        settlementNames = new ArrayList<String>();
        settlementNames.addAll(Messages.getSettlementNames(this));
        if (isIndian()) {
            capitalName = settlementNames.remove(0);
            if (random != null) {
                Collections.shuffle(settlementNames, random);
            }
        } else {
            capitalName = null;
        }
        logger.info("Installed " + settlementNames.size() + " settlement names for player " + this);
    }
}
