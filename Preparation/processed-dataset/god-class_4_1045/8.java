private void prepareHarvest(TargetInstance aTargetInstance) {
    BusinessObjectFactory factory = new BusinessObjectFactory();
    Set<String> originalSeeds = new HashSet<String>();
    Set<SeedHistory> seedHistory = new HashSet<SeedHistory>();
    for (Seed seed : targetManager.getSeeds(aTargetInstance)) {
        originalSeeds.add(seed.getSeed());
        if (targetInstanceManager.getStoreSeedHistory()) {
            SeedHistory history = factory.newSeedHistory(aTargetInstance, seed);
            seedHistory.add(history);
        }
    }
    //Note that seed history should eventually supplant original seeds. Original seeds  
    //has been implemented as a Hibernate collection of String and thus the   
    //target_instance_orig_seeds table has no id column, preventing further expansion  
    //of the collection. The seed history needs to include the primary column. The  
    //originalSeeds are used by the quality review (prune) tool to generate the to level  
    //tree view. The original seeds has been left in for this purpose to support legacy   
    //target instances created prior to this release. Future releases may see the removal   
    //of this functionality in favour of the SeedHistory; meanwhile this can be turned off   
    //via the targetInstanceManager bean in wct_core.xml  
    aTargetInstance.setOriginalSeeds(originalSeeds);
    if (targetInstanceManager.getStoreSeedHistory()) {
        aTargetInstance.setSeedHistory(seedHistory);
    }
    // Generate some of the SIP.  
    Map<String, String> sipParts = sipBuilder.buildSipSections(aTargetInstance);
    aTargetInstance.setSipParts(sipParts);
    // Save the sip parts and seeds to the database.  
    targetInstanceDao.save(aTargetInstance);
}
