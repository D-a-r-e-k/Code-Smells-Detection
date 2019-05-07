/**
     * Installs suitable ships names into the player name cache.
     * Optionally shuffles all but the first name so so as to provide
     * a stable starting ship name.
     *
     * @param random A <code>Random</code> number source.
     */
private void initializeShipNames(Random random) {
    if (shipNames == null) {
        shipNames = new ArrayList<String>();
        shipNames.addAll(Messages.getShipNames(this));
        shipFallback = (shipNames.isEmpty()) ? null : shipNames.remove(0);
        String startingShip = (shipNames.isEmpty()) ? null : shipNames.remove(0);
        if (random != null) {
            Collections.shuffle(shipNames, random);
        }
        if (startingShip != null)
            shipNames.add(0, startingShip);
        logger.info("Installed " + shipNames.size() + " ship names for player " + this.toString());
    }
}
