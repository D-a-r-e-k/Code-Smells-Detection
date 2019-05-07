private static Menu buildMenu(ParsedMenu menuConfig, String currentAction, User user, Weblog weblog) {
    log.debug("creating menu for action - " + currentAction);
    Menu tabMenu = new Menu();
    // iterate over tabs from parsed config 
    ParsedTab configTab = null;
    Iterator tabsIter = menuConfig.getTabs().iterator();
    while (tabsIter.hasNext()) {
        configTab = (ParsedTab) tabsIter.next();
        log.debug("config tab = " + configTab.getName());
        // does this tab have an enabledProperty? 
        boolean includeTab = true;
        if (configTab.getEnabledProperty() != null) {
            includeTab = getBooleanProperty(configTab.getEnabledProperty());
        } else if (configTab.getDisabledProperty() != null) {
            includeTab = !getBooleanProperty(configTab.getDisabledProperty());
        }
        if (includeTab) {
            // user roles check 
            if (configTab.getRole() != null) {
                if (!user.hasRole(configTab.getRole())) {
                    includeTab = false;
                }
            }
        }
        if (includeTab) {
            // weblog permissions check 
            includeTab = isPermitted(configTab.getPerm(), user, weblog);
        }
        if (includeTab) {
            log.debug("tab allowed - " + configTab.getName());
            // all checks passed, tab should be included 
            MenuTab tab = new MenuTab();
            tab.setKey(configTab.getName());
            // setup tab items 
            boolean firstItem = true;
            ParsedTabItem configTabItem = null;
            Iterator itemsIter = configTab.getTabItems().iterator();
            while (itemsIter.hasNext()) {
                configTabItem = (ParsedTabItem) itemsIter.next();
                log.debug("config tab item = " + configTabItem.getName());
                boolean includeItem = true;
                if (configTabItem.getEnabledProperty() != null) {
                    includeItem = getBooleanProperty(configTabItem.getEnabledProperty());
                } else if (configTabItem.getDisabledProperty() != null) {
                    includeItem = !getBooleanProperty(configTabItem.getDisabledProperty());
                }
                if (includeItem) {
                    // user roles check 
                    if (configTabItem.getRole() != null) {
                        if (!user.hasRole(configTabItem.getRole())) {
                            includeItem = false;
                        }
                    }
                }
                if (includeItem) {
                    // weblog permissions check 
                    includeItem = isPermitted(configTabItem.getPerm(), user, weblog);
                }
                if (includeItem) {
                    log.debug("tab item allowed - " + configTabItem.getName());
                    // all checks passed, item should be included 
                    MenuTabItem tabItem = new MenuTabItem();
                    tabItem.setKey(configTabItem.getName());
                    tabItem.setAction(configTabItem.getAction());
                    // is this the selected item? 
                    if (isSelected(currentAction, configTabItem)) {
                        tabItem.setSelected(true);
                        tab.setSelected(true);
                    }
                    // the url for the tab is the url of the first item of the tab 
                    if (firstItem) {
                        tab.setAction(tabItem.getAction());
                        firstItem = false;
                    }
                    // add the item 
                    tab.addItem(tabItem);
                }
            }
            // add the tab 
            tabMenu.addTab(tab);
        }
    }
    return tabMenu;
}
