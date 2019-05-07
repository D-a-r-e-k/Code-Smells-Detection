/**
     * Explores any neighbouring LCRs.
     * Choose non-expert persons for the exploration.
     */
private void exploreLCRs() {
    final Tile tile = colony.getTile();
    List<Unit> explorers = new ArrayList<Unit>();
    for (Unit u : tile.getUnitList()) {
        if (u.isPerson() && (u.getType().getSkill() <= 0 || u.hasAbility("model.ability.expertScout"))) {
            explorers.add(u);
        }
    }
    Collections.sort(explorers, scoutComparator);
    for (Tile t : tile.getSurroundingTiles(1)) {
        if (t.hasLostCityRumour()) {
            Direction direction = tile.getDirection(t);
            for (; ; ) {
                if (explorers.isEmpty())
                    return;
                Unit u = explorers.remove(0);
                if (!u.getMoveType(t).isProgress())
                    continue;
                if (getAIUnit(u).move(direction) && !t.hasLostCityRumour()) {
                    u.setDestination(tile);
                    break;
                }
            }
        }
    }
}
