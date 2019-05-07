/**
     * Is this tile claimable for a colony center tile under the
     * special provisions of the model.option.buildOnNativeLand option.
     *
     * @param tile The <code>Tile</code> to try to claim.
     * @return True if the tile can be claimed.
     */
private boolean canClaimFreeCenterTile(Tile tile) {
    final Specification spec = getGame().getSpecification();
    String build = spec.getString("model.option.buildOnNativeLand");
    return isEuropean() && tile.getOwner() != null && tile.getOwner().isIndian() && ("model.option.buildOnNativeLand.always".equals(build) || ("model.option.buildOnNativeLand.first".equals(build) && hasZeroSettlements()) || ("model.option.buildOnNativeLand.firstAndUncontacted".equals(build) && hasZeroSettlements() && (tile.getOwner() == null || tile.getOwner().getStance(this) == Stance.UNCONTACTED)));
}
