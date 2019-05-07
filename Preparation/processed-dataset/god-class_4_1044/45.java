/**
     * Get the seeds for the given target instance.
     * @param aTargetInstance
     * @return The set of Seeds.
     */
public Set<Seed> getSeeds(TargetInstance aTargetInstance) {
    return getSeeds(aTargetInstance.getTarget(), aTargetInstance.getOwner().getAgency().getOid());
}
