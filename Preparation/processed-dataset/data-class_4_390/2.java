@Override
public String[] getLocationNames() {
    if (!isInitialized || isClan()) {
        return CLAN_LOCATION_NAMES;
    }
    return IS_LOCATION_NAMES;
}
