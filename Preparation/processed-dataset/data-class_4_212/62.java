/** @see TargetManager#getActivePermissions(TargetInstance). */
public Collection<PermissionSeedDTO> getActivePermissions(TargetInstance aTargetInstance) {
    HashMap<Long, PermissionSeedDTO> permissions = new HashMap<Long, PermissionSeedDTO>();
    Set<Seed> seeds = getSeeds(aTargetInstance);
    Date now = new Date();
    PermissionSeedDTO psdto = null;
    Set<Permission> ps = null;
    for (Seed seed : seeds) {
        ps = seed.getPermissions();
        for (Permission p : ps) {
            if (p.getStartDate().before(now) && (p.getEndDate() == null || p.getEndDate().after(now))) {
                if (permissions.containsKey(p.getOid())) {
                    psdto = (PermissionSeedDTO) permissions.get(p.getOid());
                } else {
                    psdto = new PermissionSeedDTO(p);
                }
                psdto.getSeeds().add(seed.getSeed());
                permissions.put(psdto.getPermissionOid(), psdto);
            }
        }
    }
    return permissions.values();
}
