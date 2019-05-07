/**
	 * Transfer Seeds from Permission A to Permission B.
	 * @param fromPermission The oid of the permission to transfer seeds from.
	 * @param toPermission The oid of the permission to transfer the seeds to.
	 * @return The number of seeds transferred.
	 */
public int transferSeeds(Permission fromPermission, Permission toPermission) {
    List<Seed> seeds = targetDao.getLinkedSeeds(fromPermission);
    for (Seed s : seeds) {
        s.removePermission(fromPermission);
        s.addPermission(toPermission);
    }
    targetDao.saveAll(seeds);
    return seeds.size();
}
