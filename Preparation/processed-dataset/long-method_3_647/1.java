public String newPost(String blogid, String userid, String password, Hashtable struct, boolean publish) throws Exception {
    mLogger.debug("newPost() Called ===========[ SUPPORTED ]=====");
    mLogger.debug("     BlogId: " + blogid);
    mLogger.debug("     UserId: " + userid);
    mLogger.debug("    Publish: " + publish);
    Weblog website = validate(blogid, userid, password);
    Hashtable postcontent = struct;
    String description = (String) postcontent.get("description");
    String title = (String) postcontent.get("title");
    if (StringUtils.isEmpty(title) && StringUtils.isEmpty(description)) {
        throw new XmlRpcException(BLOGGERAPI_INCOMPLETE_POST, "Must specify title or description");
    }
    if (StringUtils.isEmpty(title)) {
        title = Utilities.truncateNicely(description, 15, 15, "...");
    }
    Date dateCreated = (Date) postcontent.get("dateCreated");
    if (dateCreated == null)
        dateCreated = (Date) postcontent.get("pubDate");
    if (dateCreated == null)
        dateCreated = new Date();
    mLogger.debug("      Title: " + title);
    try {
        Weblogger roller = WebloggerFactory.getWeblogger();
        WeblogManager weblogMgr = roller.getWeblogManager();
        User user = roller.getUserManager().getUserByUserName(userid);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        WeblogEntry entry = new WeblogEntry();
        entry.setTitle(title);
        entry.setText(description);
        entry.setLocale(website.getLocale());
        entry.setPubTime(new Timestamp(dateCreated.getTime()));
        entry.setUpdateTime(current);
        entry.setWebsite(website);
        entry.setCreator(user);
        entry.setCommentDays(new Integer(website.getDefaultCommentDays()));
        entry.setAllowComments(website.getDefaultAllowComments());
        // apply weblog default plugins 
        if (website.getDefaultPlugins() != null) {
            entry.setPlugins(website.getDefaultPlugins());
        }
        if (Boolean.valueOf(publish).booleanValue()) {
            entry.setStatus(WeblogEntry.PUBLISHED);
        } else {
            entry.setStatus(WeblogEntry.DRAFT);
        }
        // MetaWeblog supports multiple cats, Weblogger supports one/entry 
        // so here we take accept the first category that exists 
        WeblogCategory rollerCat = null;
        if (postcontent.get("categories") != null) {
            Object[] cats = (Object[]) postcontent.get("categories");
            if (cats != null && cats.length > 0) {
                mLogger.debug("cats type - " + cats[0].getClass().getName());
                mLogger.debug("cat to string - " + cats[0].toString());
                for (int i = 0; i < cats.length; i++) {
                    Object cat = cats[i];
                    rollerCat = weblogMgr.getWeblogCategoryByPath(website, (String) cat);
                    if (rollerCat != null) {
                        entry.setCategory(rollerCat);
                        break;
                    }
                }
            }
        }
        if (rollerCat == null) {
            // or we fall back to the default Blogger API category 
            entry.setCategory(website.getBloggerCategory());
        }
        // save the entry 
        weblogMgr.saveWeblogEntry(entry);
        roller.flush();
        // notify cache 
        flushPageCache(entry.getWebsite());
        // TODO: Weblogger timestamps need better than 1 second accuracy 
        // Until then, we can't allow more than one post per second 
        Thread.sleep(1000);
        return entry.getId();
    } catch (Exception e) {
        String msg = "ERROR in MetaWeblogAPIHandler.newPost";
        mLogger.error(msg, e);
        throw new XmlRpcException(UNKNOWN_EXCEPTION, msg);
    }
}
