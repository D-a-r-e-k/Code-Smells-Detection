/** @see TargetManager#isTargetHarvestable(TargetInstance). */
public boolean isTargetHarvestable(TargetInstance aTargetInstance) {
    boolean harvestable = false;
    boolean foundBadSeed = false;
    AbstractTarget aTarget = aTargetInstance.getTarget();
    if (aTarget.getObjectType() == AbstractTarget.TYPE_GROUP) {
        return getSeeds(aTargetInstance).size() > 0;
    } else {
        Target target = targetDao.load(aTarget.getOid(), true);
        Seed seed = null;
        Set<Seed> seeds = target.getSeeds();
        Iterator<Seed> it = seeds.iterator();
        while (it.hasNext()) {
            seed = (Seed) it.next();
            if (!seed.isHarvestable(new Date())) {
                foundBadSeed = true;
                break;
            }
        }
        if (!seeds.isEmpty() && !foundBadSeed) {
            harvestable = true;
        }
        return harvestable;
    }
}
