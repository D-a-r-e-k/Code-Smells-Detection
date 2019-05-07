/**
     * {@inheritDoc}
     */
public String execute(WikiContext context, Map params) throws PluginException {
    int since = TextUtil.parseIntParameter((String) params.get("since"), DEFAULT_DAYS);
    int spacing = 4;
    boolean showAuthor = true;
    boolean showChangenote = true;
    int tablewidth = 4;
    WikiEngine engine = context.getEngine();
    // 
    //  Which format we want to see? 
    // 
    if ("compact".equals(params.get(PARAM_FORMAT))) {
        spacing = 0;
        showAuthor = false;
        showChangenote = false;
        tablewidth = 2;
    }
    Calendar sincedate = new GregorianCalendar();
    sincedate.add(Calendar.DAY_OF_MONTH, -since);
    log.debug("Calculating recent changes from " + sincedate.getTime());
    // FIXME: Should really have a since date on the getRecentChanges 
    // method. 
    Collection changes = engine.getRecentChanges();
    super.initialize(context, params);
    changes = super.filterCollection(changes);
    if (changes != null) {
        Date olddate = new Date(0);
        DateFormat fmt = getDateFormat(context, params);
        DateFormat tfmt = getTimeFormat(context, params);
        table rt = new table();
        rt.setCellPadding(spacing).setClass("recentchanges");
        for (Iterator i = changes.iterator(); i.hasNext(); ) {
            WikiPage pageref = (WikiPage) i.next();
            Date lastmod = pageref.getLastModified();
            if (lastmod.before(sincedate.getTime())) {
                break;
            }
            if (!isSameDay(lastmod, olddate)) {
                tr row = new tr();
                td col = new td();
                col.setColSpan(tablewidth).setClass("date");
                col.addElement(new b().addElement(fmt.format(lastmod)));
                rt.addElement(row);
                row.addElement(col);
                olddate = lastmod;
            }
            String link = context.getURL(pageref instanceof Attachment ? WikiContext.ATTACH : WikiContext.VIEW, pageref.getName());
            a linkel = new a(link, engine.beautifyTitle(pageref.getName()));
            tr row = new tr();
            td col = new td().setWidth("30%").addElement(linkel);
            // 
            //  Add the direct link to the attachment info. 
            // 
            if (pageref instanceof Attachment) {
                linkel = new a().setHref(context.getURL(WikiContext.INFO, pageref.getName()));
                linkel.setClass("infolink");
                linkel.addElement(new img().setSrc(context.getURL(WikiContext.NONE, "images/attachment_small.png")));
                col.addElement(linkel);
            }
            row.addElement(col);
            rt.addElement(row);
            if (pageref instanceof Attachment) {
                row.addElement(new td(tfmt.format(lastmod)).setClass("lastchange"));
            } else {
                td infocol = (td) new td().setClass("lastchange");
                infocol.addElement(new a(context.getURL(WikiContext.DIFF, pageref.getName(), "r1=-1"), tfmt.format(lastmod)));
                row.addElement(infocol);
            }
            // 
            //  Display author information. 
            // 
            if (showAuthor) {
                String author = pageref.getAuthor();
                td authorinfo = new td();
                authorinfo.setClass("author");
                if (author != null) {
                    if (engine.pageExists(author)) {
                        authorinfo.addElement(new a(context.getURL(WikiContext.VIEW, author), author));
                    } else {
                        authorinfo.addElement(author);
                    }
                } else {
                    authorinfo.addElement(context.getBundle(InternationalizationManager.CORE_BUNDLE).getString("common.unknownauthor"));
                }
                row.addElement(authorinfo);
            }
            // Change note 
            if (showChangenote) {
                String changenote = (String) pageref.getAttribute(WikiPage.CHANGENOTE);
                row.addElement(new td(changenote != null ? changenote : "").setClass("changenote"));
            }
        }
        rt.setPrettyPrint(true);
        return rt.toString();
    }
    return "";
}
