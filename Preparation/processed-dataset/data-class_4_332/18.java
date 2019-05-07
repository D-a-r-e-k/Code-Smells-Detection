/**
     * Creates a list of the goods which should be shipped out of this colony.
     */
public void updateAIGoods() {
    if (colony.hasAbility(Ability.EXPORT)) {
        while (!aiGoods.isEmpty()) {
            AIGoods ag = aiGoods.remove(0);
            goodsLog(ag, "customizes");
            dropGoods(ag);
        }
        return;
    }
    int i = 0;
    while (i < aiGoods.size()) {
        AIGoods ag = aiGoods.get(i);
        if (ag == null) {
            aiGoods.remove(i);
        } else if (!ag.checkIntegrity()) {
            goodsLog(ag, "reaps");
            dropGoods(ag);
        } else if (ag.getGoods().getLocation() != colony) {
            // On its way, no longer of interest here, but do not dispose 
            // as that will happen when delivered. 
            goodsLog(ag, "sends");
            aiGoods.remove(i);
        } else if (colony.getAdjustedNetProductionOf(ag.getGoods().getType()) < 0) {
            goodsLog(ag, "needs");
            dropGoods(ag);
        } else {
            i++;
        }
    }
    final Europe europe = colony.getOwner().getEurope();
    final int capacity = colony.getWarehouseCapacity();
    List<AIGoods> newAIGoods = new ArrayList<AIGoods>();
    for (GoodsType g : getSpecification().getGoodsTypeList()) {
        if (colony.getAdjustedNetProductionOf(g) < 0)
            continue;
        int count = colony.getGoodsCount(g);
        int exportAmount = (fullExport.contains(g)) ? count : (partExport.contains(g)) ? count - colony.getExportData(g).getExportLevel() : -1;
        int priority = (exportAmount >= capacity) ? Transportable.IMPORTANT_DELIVERY : (exportAmount >= GoodsContainer.CARGO_SIZE) ? Transportable.FULL_DELIVERY : 0;
        // Find all existing AI goods of type g 
        //   update amount of goods to export 
        //   reduce exportAmount at each step, dropping excess exports 
        i = 0;
        while (i < aiGoods.size()) {
            AIGoods ag = aiGoods.get(i);
            Goods goods = ag.getGoods();
            if (goods.getType() != g) {
                i++;
                continue;
            }
            int amount = goods.getAmount();
            if (amount <= exportAmount) {
                if (amount < GoodsContainer.CARGO_SIZE) {
                    amount = Math.min(exportAmount, GoodsContainer.CARGO_SIZE);
                    goods.setAmount(amount);
                    ag.setTransportPriority(priority);
                }
                goodsLog(ag, "exports");
            } else if (exportAmount >= EXPORT_MINIMUM) {
                goods.setAmount(exportAmount);
                goodsLog(ag, "clamps");
            } else {
                goodsLog(ag, "unexports");
                dropGoods(ag);
                continue;
            }
            exportAmount -= amount;
            i++;
        }
        // Export new goods, to Europe if possible. 
        Location destination = (colony.getOwner().canTrade(g)) ? europe : null;
        while (exportAmount >= EXPORT_MINIMUM) {
            int amount = Math.min(exportAmount, GoodsContainer.CARGO_SIZE);
            AIGoods newGoods = new AIGoods(getAIMain(), colony, g, amount, destination);
            newGoods.setTransportPriority(priority);
            newAIGoods.add(newGoods);
            goodsLog(newGoods, "makes");
            exportAmount -= amount;
        }
    }
    aiGoods.addAll(newAIGoods);
    Collections.sort(aiGoods, Transportable.transportableComparator);
}
