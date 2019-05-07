public PageList findUsers(Query q) throws Exception {
    LdapContext ctx = null;
    List users = new ArrayList();
    String pageSizeString = (String) OrganizationServiceImpl.properties.get("ldap.page.size");
    String pageSleepString = (String) OrganizationServiceImpl.properties.get("ldap.page.sleep");
    int pageSize = 100;
    long pageSleep = 0;
    if (pageSizeString != null) {
        pageSize = Integer.valueOf(pageSizeString).intValue();
    }
    if (pageSleepString != null) {
        pageSleep = Long.valueOf(pageSleepString).longValue();
    }
    int itemCounter = pageSize;
    NamingEnumeration enumer;
    try {
        ctx = ldapService.getLdapContext();
        String filter = null;
        ArrayList list = new ArrayList();
        if (q.getUserName() != null && q.getUserName().length() > 0) {
            list.add("(" + (String) OrganizationServiceImpl.properties.get("ldap.user.username.attribute") + "=" + q.getUserName() + ")");
        }
        if (q.getFirstName() != null && q.getFirstName().length() > 0) {
            list.add("(" + (String) OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute") + "=" + q.getFirstName() + ")");
        }
        if (q.getLastName() != null && q.getLastName().length() > 0) {
            list.add("(" + (String) OrganizationServiceImpl.properties.get("ldap.user.lastname.attribute") + "=" + q.getLastName() + ")");
        }
        if (q.getEmail() != null && q.getEmail().length() > 0) {
            list.add("(" + (String) OrganizationServiceImpl.properties.get("ldap.user.mail.attribute") + "=" + q.getEmail() + ")");
        }
        if (list.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("(&");
            if (list.size() > 1) {
                for (int x = 0; x < list.size(); x++) {
                    if (x == (list.size() - 1)) {
                        buffer.append(list.get(x));
                    } else {
                        buffer.append(list.get(x) + " || ");
                    }
                }
            } else {
                buffer.append(list.get(0));
            }
            buffer.append(" (" + (String) OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter") + ") )");
            filter = buffer.toString();
        } else {
            filter = (String) OrganizationServiceImpl.properties.get("ldap.user.objectclass.filter");
        }
        String searchBase = (String) OrganizationServiceImpl.properties.get("ldap.base.url");
        Control control[] = new Control[2];
        String keys[] = { (String) OrganizationServiceImpl.properties.get("ldap.user.username.attribute") };
        SortControl sortControl = new SortControl(keys, false);
        control[0] = sortControl;
        PagedResultsControl pagedResultsControl = new PagedResultsControl(pageSize);
        control[1] = pagedResultsControl;
        ctx.setRequestControls(control);
        while (itemCounter >= pageSize) {
            itemCounter = 0;
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            enumer = ctx.search(searchBase, filter, constraints);
            while (enumer.hasMore()) {
                itemCounter += 1;
                SearchResult result = (SearchResult) enumer.next();
                Attributes attrs = result.getAttributes();
                // Create user object and add to arraylist  
                User user = new UserImpl();
                user.setUserName(this.getAttribute(attrs, (String) OrganizationServiceImpl.properties.get("ldap.user.username.attribute")));
                user.setFirstName(this.getAttribute(attrs, (String) OrganizationServiceImpl.properties.get("ldap.user.firstname.attribute")));
                user.setLastName(this.getAttribute(attrs, (String) OrganizationServiceImpl.properties.get("ldap.user.lasttname.attribute")));
                user.setEmail(this.getAttribute(attrs, (String) OrganizationServiceImpl.properties.get("ldap.user.mail.attribute")));
                user.setCreatedDate(new Date());
                user.setLastLoginTime(new Date());
                user.setPassword("PASSWORD");
                users.add(user);
            }
            if (itemCounter >= pageSize) {
                Control[] responseControls = ctx.getResponseControls();
                for (int i = 0; i < responseControls.length; i++) {
                    if (responseControls[i] instanceof PagedResultsResponseControl) {
                        pagedResultsControl = new PagedResultsControl(pageSize, ((PagedResultsResponseControl) responseControls[i]).getCookie(), true);
                        ctx.setRequestControls(new Control[] { pagedResultsControl });
                    }
                }
            }
            if (pageSleep > 0) {
                Thread.sleep(pageSleep);
            }
        }
    } catch (PartialResultException e) {
    } finally {
        ctx.close();
    }
    return new ObjectPageList(users, 20);
}
