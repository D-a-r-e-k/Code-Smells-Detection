/**
     * Tries to swap an expert unit for another doing its job.
     *
     * @param expert The expert <code>Unit</code>.
     * @param others A list of other <code>Unit</code>s to test against.
     * @return The unit that was replaced by the expert, or null if none.
     */
private Unit trySwapExpert(Unit expert, List<Unit> others) {
    GoodsType work = expert.getType().getExpertProduction();
    GoodsType oldWork = expert.getWorkType();
    for (int i = 0; i < others.size(); i++) {
        Unit other = others.get(i);
        if (!other.isPerson())
            continue;
        if (other.getWorkType() == work && other.getType().getExpertProduction() != work) {
            Location l1 = expert.getLocation();
            Location l2 = other.getLocation();
            other.setLocation(colony.getTile());
            expert.setLocation(l2);
            expert.setWorkType(work);
            other.setLocation(l1);
            if (oldWork != null)
                other.setWorkType(oldWork);
            TypeCountMap<EquipmentType> equipment = expert.getEquipment();
            for (EquipmentType e : new ArrayList<EquipmentType>(equipment.keySet())) {
                int n = equipment.getCount(e);
                expert.changeEquipment(e, -n);
                other.changeEquipment(e, n);
            }
            return other;
        }
    }
    return null;
}
