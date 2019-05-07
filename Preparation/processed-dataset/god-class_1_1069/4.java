/* (non-Javadoc)
	 * @see freemind.extensions.HookFactory#createNodeHook(java.lang.String)
	 */
public NodeHook createNodeHook(String hookName) {
    //		System.out.println("create node hook:"+hookName); 
    NodeHook hook;
    if (hookName.equals(ReminderHookBase.PLUGIN_LABEL)) {
        hook = new BrowseReminderHook();
    } else {
        hook = new PermanentNodeHookSubstituteUnknown(hookName);
    }
    // decorate hook. 
    hook.setProperties(new Properties());
    hook.setName(hookName);
    hook.setPluginBaseClass(null);
    return hook;
}
