public PermissionCollection getPermissions(CodeSource codeSource) {
    return (Permissions) permissionsMap.get(codeSource.getLocation());
}
