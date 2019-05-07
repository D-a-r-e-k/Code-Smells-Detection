/**
     * Add the other goods types to the production list.  When this is
     * called the new world goods production is already present on the
     * produce list.  Ignores food which is treated separately.
     */
private void updateProductionList(final Map<GoodsType, Map<WorkLocation, Integer>> production) {
    final Comparator<GoodsType> productionComparator = new Comparator<GoodsType>() {

        public int compare(GoodsType g1, GoodsType g2) {
            int p1 = 0;
            for (Integer i : production.get(g1).values()) {
                p1 += i.intValue();
            }
            int p2 = 0;
            for (Integer i : production.get(g2).values()) {
                p2 += i.intValue();
            }
            return p2 - p1;
        }
    };
    List<GoodsType> toAdd = new ArrayList<GoodsType>();
    // If we need liberty put it before the new world production. 
    if (colony.getSoL() < 100) {
        for (GoodsType g : libertyGoodsTypes) {
            if (production.containsKey(g))
                toAdd.add(g);
        }
        Collections.sort(toAdd, productionComparator);
        produce.addAll(0, toAdd);
        toAdd.clear();
    }
    // Always add raw/building materials first. 
    Collections.sort(rawBuildingGoodsTypes, productionComparator);
    for (GoodsType g : buildingGoodsTypes) {
        if (production.containsKey(g)) {
            GoodsType raw = g.getRawMaterial();
            if (colony.getGoodsCount(raw) >= GoodsContainer.CARGO_SIZE / 2 || production.containsKey(raw)) {
                toAdd.add(g);
            }
        }
    }
    Collections.sort(toAdd, new Comparator<GoodsType>() {

        public int compare(GoodsType g1, GoodsType g2) {
            int i1 = rawBuildingGoodsTypes.indexOf(g1.getRawMaterial());
            int i2 = rawBuildingGoodsTypes.indexOf(g2.getRawMaterial());
            return i1 - i2;
        }
    });
    for (int i = toAdd.size() - 1; i >= 0; i--) {
        GoodsType make = toAdd.get(i);
        GoodsType raw = make.getRawMaterial();
        if (production.containsKey(raw)) {
            if (colony.getGoodsCount(raw) >= GoodsContainer.CARGO_SIZE / 2) {
                produce.add(raw);
                // Add at the end, enough in stock 
                produce.add(0, make);
            } else {
                produce.add(0, make);
                produce.add(0, raw);
            }
        } else {
            produce.add(0, make);
        }
    }
    toAdd.clear();
    // Military goods after lucrative production. 
    for (GoodsType g : militaryGoodsTypes) {
        if (production.containsKey(g))
            toAdd.add(g);
    }
    Collections.sort(toAdd, productionComparator);
    produce.addAll(toAdd);
    toAdd.clear();
    // Immigration last. 
    if (colony.getOwner().getEurope() != null) {
        for (GoodsType g : immigrationGoodsTypes) {
            if (production.containsKey(g))
                toAdd.add(g);
        }
        Collections.sort(toAdd, productionComparator);
        produce.addAll(toAdd);
        toAdd.clear();
    }
}
