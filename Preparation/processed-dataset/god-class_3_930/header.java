void method0() { 
static final String PACKAGE_NAME = "org.java.plugin.registry.xml";
//$NON-NLS-1$  
private static final char UNIQUE_SEPARATOR = '@';
private static final Log log = LogFactory.getLog(PluginRegistryImpl.class);
private final List<ReportItem> registrationReport = new LinkedList<ReportItem>();
private final Map<String, PluginDescriptor> registeredPlugins = new HashMap<String, PluginDescriptor>();
private final Map<String, PluginFragment> registeredFragments = new HashMap<String, PluginFragment>();
private final List<RegistryChangeListener> listeners = Collections.synchronizedList(new LinkedList<RegistryChangeListener>());
private ManifestParser manifestParser;
private boolean stopOnError = false;
}
