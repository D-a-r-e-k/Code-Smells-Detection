/**
     * Adds a terrain bonus with a probability determined by the
     * <code>MapGeneratorOptions</code>.
     *
     * @param t The <code>Tile</code> to add bonuses to.
     * @param generateBonus Generate the bonus or not.
     */
private void perhapsAddBonus(Tile t, boolean generateBonus) {
    final Specification spec = t.getSpecification();
    TileImprovementType fishBonusLandType = spec.getTileImprovementType("model.improvement.fishBonusLand");
    TileImprovementType fishBonusRiverType = spec.getTileImprovementType("model.improvement.fishBonusRiver");
    final int bonusNumber = mapOptions.getInteger("model.option.bonusNumber");
    if (t.isLand()) {
        if (generateBonus && random.nextInt(100) < bonusNumber) {
            // Create random Bonus Resource 
            t.addResource(createResource(t));
        }
    } else {
        int adjacentLand = 0;
        boolean adjacentRiver = false;
        for (Direction direction : Direction.values()) {
            Tile otherTile = t.getNeighbourOrNull(direction);
            if (otherTile != null && otherTile.isLand()) {
                adjacentLand++;
                if (otherTile.hasRiver()) {
                    adjacentRiver = true;
                }
            }
        }
        // In Col1, ocean tiles with less than 3 land neighbours 
        // produce 2 fish, all others produce 4 fish 
        if (adjacentLand > 2) {
            t.add(new TileImprovement(t.getGame(), t, fishBonusLandType));
        }
        // In Col1, the ocean tile in front of a river mouth would 
        // get an additional +1 bonus 
        // TODO: This probably has some false positives, means 
        // river tiles that are NOT a river mouth next to this tile! 
        if (!t.hasRiver() && adjacentRiver) {
            t.add(new TileImprovement(t.getGame(), t, fishBonusRiverType));
        }
        if (t.getType().isHighSeasConnected()) {
            if (generateBonus && adjacentLand > 1 && random.nextInt(10 - adjacentLand) == 0) {
                t.addResource(createResource(t));
            }
        } else {
            if (random.nextInt(100) < bonusNumber) {
                // Create random Bonus Resource 
                t.addResource(createResource(t));
            }
        }
    }
}
