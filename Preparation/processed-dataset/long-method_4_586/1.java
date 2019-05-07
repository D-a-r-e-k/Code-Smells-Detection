/**
         * Count a page hit, present a pages' counter or output a list of
         * pagecounts.
         * 
         * @param context the wiki context
         * @param params the plugin parameters
         * @return String Wiki page snippet
         * @throws PluginException Malformed pattern parameter.
         */
public String execute(WikiContext context, Map params) throws PluginException {
    WikiEngine engine = context.getEngine();
    WikiPage page = context.getPage();
    String result = STR_EMPTY;
    if (page != null) {
        // get parameters 
        String pagename = page.getName();
        String count = (String) params.get(PARAM_COUNT);
        String show = (String) params.get(PARAM_SHOW);
        int entries = TextUtil.parseIntParameter((String) params.get(PARAM_MAX_ENTRIES), Integer.MAX_VALUE);
        final int max = TextUtil.parseIntParameter((String) params.get(PARAM_MAX_COUNT), Integer.MAX_VALUE);
        final int min = TextUtil.parseIntParameter((String) params.get(PARAM_MIN_COUNT), Integer.MIN_VALUE);
        String sort = (String) params.get(PARAM_SORT);
        String body = (String) params.get(PluginManager.PARAM_BODY);
        Pattern[] exclude = compileGlobs(PARAM_EXCLUDE, (String) params.get(PARAM_EXCLUDE));
        Pattern[] include = compileGlobs(PARAM_INCLUDE, (String) params.get(PARAM_INCLUDE));
        Pattern[] refer = compileGlobs(PARAM_REFER, (String) params.get(PARAM_REFER));
        PatternMatcher matcher = (null != exclude || null != include || null != refer) ? new Perl5Matcher() : null;
        boolean increment = false;
        // increment counter? 
        if (STR_YES.equals(count)) {
            increment = true;
        } else {
            count = null;
        }
        // default increment counter? 
        if ((show == null || STR_NONE.equals(show)) && count == null) {
            increment = true;
        }
        // filter on referring pages? 
        Collection<String> referrers = null;
        if (refer != null) {
            ReferenceManager refManager = engine.getReferenceManager();
            Iterator iter = refManager.findCreated().iterator();
            while (iter != null && iter.hasNext()) {
                String name = (String) iter.next();
                boolean use = false;
                for (int n = 0; !use && n < refer.length; n++) {
                    use = matcher.matches(name, refer[n]);
                }
                if (use) {
                    Collection<String> refs = engine.getReferenceManager().findReferrers(name);
                    if (refs != null && !refs.isEmpty()) {
                        if (referrers == null) {
                            referrers = new HashSet<String>();
                        }
                        referrers.addAll(refs);
                    }
                }
            }
        }
        synchronized (this) {
            Counter counter = m_counters.get(pagename);
            // only count in view mode, keep storage values in sync 
            if (increment && WikiContext.VIEW.equalsIgnoreCase(context.getRequestContext())) {
                if (counter == null) {
                    counter = new Counter();
                    m_counters.put(pagename, counter);
                }
                counter.increment();
                m_storage.setProperty(pagename, counter.toString());
                m_dirty = true;
            }
            if (show == null || STR_NONE.equals(show)) {
            } else if (PARAM_COUNT.equals(show)) {
                // show page count 
                result = counter.toString();
            } else if (body != null && 0 < body.length() && STR_LIST.equals(show)) {
                // show list of counts 
                String header = STR_EMPTY;
                String line = body;
                String footer = STR_EMPTY;
                int start = body.indexOf(STR_SEPARATOR);
                // split body into header, line, footer on ---- 
                // separator 
                if (0 < start) {
                    header = body.substring(0, start);
                    start = skipWhitespace(start + STR_SEPARATOR.length(), body);
                    int end = body.indexOf(STR_SEPARATOR, start);
                    if (start >= end) {
                        line = body.substring(start);
                    } else {
                        line = body.substring(start, end);
                        end = skipWhitespace(end + STR_SEPARATOR.length(), body);
                        footer = body.substring(end);
                    }
                }
                // sort on name or count? 
                Map<String, Counter> sorted = m_counters;
                if (sort != null && PARAM_COUNT.equals(sort)) {
                    sorted = new TreeMap<String, Counter>(m_compareCountDescending);
                    sorted.putAll(m_counters);
                }
                // build a messagebuffer with the list in wiki markup 
                StringBuffer buf = new StringBuffer(header);
                MessageFormat fmt = new MessageFormat(line);
                Object[] args = new Object[] { pagename, STR_EMPTY, STR_EMPTY };
                Iterator iter = sorted.entrySet().iterator();
                while (iter != null && 0 < entries && iter.hasNext()) {
                    Entry entry = (Entry) iter.next();
                    String name = (String) entry.getKey();
                    // check minimum count 
                    final int value = ((Counter) entry.getValue()).getValue();
                    boolean use = min <= value && value <= max;
                    // did we specify a refer-to page? 
                    if (use && referrers != null) {
                        use = referrers.contains(name);
                    }
                    // did we specify what pages to include? 
                    if (use && include != null) {
                        use = false;
                        for (int n = 0; !use && n < include.length; n++) {
                            use = matcher.matches(name, include[n]);
                        }
                    }
                    // did we specify what pages to exclude? 
                    if (use && null != exclude) {
                        for (int n = 0; use && n < exclude.length; n++) {
                            use &= !matcher.matches(name, exclude[n]);
                        }
                    }
                    if (use) {
                        args[1] = engine.beautifyTitle(name);
                        args[2] = entry.getValue();
                        fmt.format(args, buf, null);
                        entries--;
                    }
                }
                buf.append(footer);
                // let the engine render the list 
                result = engine.textToHTML(context, buf.toString());
            }
        }
    }
    return result;
}
