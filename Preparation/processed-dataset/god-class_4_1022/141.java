public UserProfile getDefaultProfile() {
    if (defaultProfile != null) {
        return defaultProfile;
    } else if (parentFolder != null) {
        return parentFolder.getDefaultProfile();
    } else if (parentStore != null) {
        return parentStore.getDefaultProfile();
    } else {
        return null;
    }
}
