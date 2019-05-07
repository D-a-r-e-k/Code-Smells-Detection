/**
     *  Checks whether the UserProfile matches certain checks.
     *
     *  @param profile The profile to check
     *  @param context The WikiContext
     *  @return False, if this userprofile is suspect and should not be allowed to be added.
     *  @since 2.6.1
     */
public boolean isValidUserProfile(WikiContext context, UserProfile profile) {
    try {
        checkPatternList(context, profile.getEmail(), profile.getEmail());
        checkPatternList(context, profile.getFullname(), profile.getFullname());
        checkPatternList(context, profile.getLoginName(), profile.getLoginName());
    } catch (RedirectException e) {
        log.info("Detected attempt to create a spammer user account (see above for rejection reason)");
        return false;
    }
    return true;
}
