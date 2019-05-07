/**
     * Reset the export settings.
     * This is always needed even when there is no customs house, because
     * updateAIGoods needs to know what to export by transport.
     * TODO: consider market prices?
     */
private void resetExports() {
    final Specification spec = getSpecification();
    if (fullExport.isEmpty()) {
        // Initialize the exportable sets. 
        // Luxury goods, non-raw materials (silver), and raw 
        // materials we do not produce (might have been gifted) 
        // should always be fully exported.  Other raw and 
        // manufactured goods should be exported only to the 
        // extent of not filling the warehouse. 
        for (GoodsType g : spec.getGoodsTypeList()) {
            if (g.isStorable() && !g.isFoodType() && !g.isBuildingMaterial() && !g.isMilitaryGoods() && !g.isTradeGoods()) {
                if (g.isRawMaterial()) {
                    partExport.add(g);
                } else {
                    fullExport.add(g);
                }
            }
        }
        for (EquipmentType e : spec.getEquipmentTypeList()) {
            for (AbstractGoods ag : e.getRequiredGoods()) {
                if (fullExport.contains(ag.getType())) {
                    fullExport.remove(ag.getType());
                    partExport.add(ag.getType());
                }
            }
        }
    }
    if (colony.getOwner().getMarket() == null) {
        // Do not export when there is no market! 
        for (GoodsType g : spec.getGoodsTypeList()) {
            colony.getExportData(g).setExported(false);
        }
    } else {
        int exportLevel = 4 * colony.getWarehouseCapacity() / 5;
        for (GoodsType g : spec.getGoodsTypeList()) {
            if (fullExport.contains(g)) {
                colony.getExportData(g).setExportLevel(0);
                colony.getExportData(g).setExported(true);
            } else if (partExport.contains(g)) {
                colony.getExportData(g).setExportLevel(exportLevel);
                colony.getExportData(g).setExported(true);
            } else {
                colony.getExportData(g).setExported(false);
            }
        }
    }
}
