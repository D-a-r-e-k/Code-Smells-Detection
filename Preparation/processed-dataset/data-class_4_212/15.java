/* (non-Javadoc)
	 * @see org.webcurator.core.targets.TargetManager#loadPermission(org.webcurator.ui.target.TargetEditorContext, java.lang.String)
	 */
public Permission loadPermission(TargetEditorContext ctx, String identity) {
    Permission p = (Permission) ctx.getObject(Permission.class, identity);
    if (p == null) {
        p = siteDao.loadPermission(Long.parseLong(identity));
        ctx.putObject(p);
    }
    return p;
}
