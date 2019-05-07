/**
     * Creates a new CrawlSettingsSAXHandler.
     *
     * @param settings the settings object that should be updated from this
     *            handler.
     */
public CrawlSettingsSAXHandler(CrawlerSettings settings) {
    super();
    this.settings = settings;
    this.settingsHandler = settings.getSettingsHandler();
    handlers.put(XMLSettingsHandler.XML_ROOT_ORDER, new RootHandler());
    handlers.put(XMLSettingsHandler.XML_ROOT_HOST_SETTINGS, new RootHandler());
    handlers.put(XMLSettingsHandler.XML_ROOT_REFINEMENT, new RootHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_CONTROLLER, new ModuleHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_OBJECT, new ModuleHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_NEW_OBJECT, new NewModuleHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_META, new MetaHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_NAME, new NameHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_DESCRIPTION, new DescriptionHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_OPERATOR, new OperatorHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_ORGANIZATION, new OrganizationHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_AUDIENCE, new AudienceHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_DATE, new DateHandler());
    handlers.put(SettingsHandler.MAP, new MapHandler());
    handlers.put(SettingsHandler.INTEGER_LIST, new ListHandler());
    handlers.put(SettingsHandler.STRING_LIST, new ListHandler());
    handlers.put(SettingsHandler.DOUBLE_LIST, new ListHandler());
    handlers.put(SettingsHandler.FLOAT_LIST, new ListHandler());
    handlers.put(SettingsHandler.LONG_LIST, new ListHandler());
    handlers.put(SettingsHandler.STRING, new SimpleElementHandler());
    handlers.put(SettingsHandler.TEXT, new SimpleElementHandler());
    handlers.put(SettingsHandler.INTEGER, new SimpleElementHandler());
    handlers.put(SettingsHandler.FLOAT, new SimpleElementHandler());
    handlers.put(SettingsHandler.LONG, new SimpleElementHandler());
    handlers.put(SettingsHandler.BOOLEAN, new SimpleElementHandler());
    handlers.put(SettingsHandler.DOUBLE, new SimpleElementHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_REFINEMENTLIST, new RefinementListHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_REFINEMENT, new RefinementHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_REFERENCE, new ReferenceHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_LIMITS, new LimitsHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_TIMESPAN, new TimespanHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_PORTNUMBER, new PortnumberHandler());
    handlers.put(XMLSettingsHandler.XML_ELEMENT_URIMATCHES, new URIMatcherHandler());
}
