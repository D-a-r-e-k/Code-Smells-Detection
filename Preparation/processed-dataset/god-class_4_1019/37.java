public boolean isLoaded() {
    return (getFolder() != null && (!(getFolder() instanceof FolderProxy)) && cache != null);
}
