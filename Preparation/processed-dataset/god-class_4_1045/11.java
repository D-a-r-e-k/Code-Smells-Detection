/**
	 * Get the profile string with the overrides applied.
	 * @return
	 */
private String getHarvestProfileString(TargetInstance aTargetInstance) {
    String profileString = aTargetInstance.getTarget().getProfile().getProfile();
    //replace any ${TI_OID} tokens  
    profileString = profileString.replace("${TI_OID}", aTargetInstance.getOid().toString());
    HeritrixProfile heritrixProfile = HeritrixProfile.fromString(profileString);
    if (aTargetInstance.getProfileOverrides().hasOverrides()) {
        log.info("Applying Profile Overrides for " + aTargetInstance.getOid());
        aTargetInstance.getProfileOverrides().apply(heritrixProfile);
    }
    heritrixProfile.setToeThreads(targetManager.getSeeds(aTargetInstance).size() * 2);
    return heritrixProfile.toString();
}
