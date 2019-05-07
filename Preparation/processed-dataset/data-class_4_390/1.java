@Override
public String[] getLocationAbbrs() {
    if (!isInitialized || isClan()) {
        return CLAN_LOCATION_ABBRS;
    }
    return IS_LOCATION_ABBRS;
}
