/**
	 * Create the default profile for the given agency.
	 * @param anAgency The agency to create the profile for.
	 */
public void createDefaultProfile(Agency anAgency) {
    Profile profile = new Profile();
    profile.setName("Default - " + anAgency.getName());
    profile.setDescription("Default profile created by new agency action");
    profile.setDefaultProfile(true);
    profile.setOwningAgency(anAgency);
    profile.setRequiredLevel(1);
    profile.setStatus(Profile.STATUS_ACTIVE);
    profile.setProfile(HeritrixProfile.create().toString());
    profileDao.saveOrUpdate(profile);
    auditor.audit(AuthUtil.getRemoteUserObject(), Profile.class.getName(), profile.getOid(), Auditor.ACTION_NEW_PROFILE, "A new profile " + profile.getName() + " has been created for " + profile.getOwningAgency().getName());
}
