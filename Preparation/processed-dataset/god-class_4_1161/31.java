/**
     * Can a colony produce certain goods?
     *
     * @param goodsType The <code>GoodsType</code> to check production of.
     * @return True if the colony can produce such goods.
     */
private boolean colonyCouldProduce(GoodsType goodsType) {
    if (goodsType.isBreedable()) {
        return colony.getGoodsCount(goodsType) >= goodsType.getBreedingNumber();
    }
    if (goodsType.isFarmed()) {
        for (ColonyTile colonyTile : colony.getColonyTiles()) {
            if (colonyTile.getWorkTile().potential(goodsType, null) > 0) {
                return true;
            }
        }
    } else {
        if (!colony.getBuildingsForProducing(goodsType).isEmpty()) {
            return (goodsType.getRawMaterial() == null) ? true : colonyCouldProduce(goodsType.getRawMaterial());
        }
    }
    return false;
}
