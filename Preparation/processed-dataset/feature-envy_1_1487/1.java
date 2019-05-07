protected void processGeneralTag(CrawlURI curi, CharSequence element, CharSequence cs) {
    Matcher attr = TextUtils.getMatcher(EACH_ATTRIBUTE_EXTRACTOR, cs);
    // Just in case it's an OBJECT or APPLET tag 
    String codebase = null;
    ArrayList<String> resources = null;
    // Just in case it's a FORM 
    CharSequence action = null;
    CharSequence actionContext = null;
    CharSequence method = null;
    // Just in case it's a VALUE whose interpretation depends on accompanying NAME 
    CharSequence valueVal = null;
    CharSequence valueContext = null;
    CharSequence nameVal = null;
    final boolean framesAsEmbeds = ((Boolean) getUncheckedAttribute(curi, ATTR_TREAT_FRAMES_AS_EMBED_LINKS)).booleanValue();
    final boolean ignoreFormActions = ((Boolean) getUncheckedAttribute(curi, ATTR_IGNORE_FORM_ACTION_URLS)).booleanValue();
    final boolean extractValueAttributes = ((Boolean) getUncheckedAttribute(curi, EXTRACT_VALUE_ATTRIBUTES)).booleanValue();
    final String elementStr = element.toString();
    while (attr.find()) {
        int valueGroup = (attr.start(14) > -1) ? 14 : (attr.start(15) > -1) ? 15 : 16;
        int start = attr.start(valueGroup);
        int end = attr.end(valueGroup);
        assert start >= 0 : "Start is: " + start + ", " + curi;
        assert end >= 0 : "End is :" + end + ", " + curi;
        CharSequence value = cs.subSequence(start, end);
        CharSequence attrName = cs.subSequence(attr.start(1), attr.end(1));
        value = TextUtils.unescapeHtml(value);
        if (attr.start(2) > -1) {
            // HREF 
            CharSequence context = Link.elementContext(element, attr.group(2));
            if (elementStr.equalsIgnoreCase(LINK)) {
                // <LINK> elements treated as embeds (css, ico, etc) 
                processEmbed(curi, value, context);
            } else {
                // other HREFs treated as links 
                processLink(curi, value, context);
            }
            if (elementStr.equalsIgnoreCase(BASE)) {
                try {
                    curi.setBaseURI(value.toString());
                } catch (URIException e) {
                    if (getController() != null) {
                        // Controller can be null: e.g. when running 
                        // ExtractorTool. 
                        getController().logUriError(e, curi.getUURI(), value.toString());
                    } else {
                        logger.info("Failed set base uri: " + curi + ", " + value.toString() + ": " + e.getMessage());
                    }
                }
            }
        } else if (attr.start(3) > -1) {
            // ACTION 
            if (!ignoreFormActions) {
                action = value;
                actionContext = Link.elementContext(element, attr.group(3));
            }
        } else if (attr.start(4) > -1) {
            // ON____ 
            processScriptCode(curi, value);
        } else if (attr.start(5) > -1) {
            // SRC etc. 
            CharSequence context = Link.elementContext(element, attr.group(5));
            // true, if we expect another HTML page instead of an image etc. 
            // TODO: add explicit 'F'rame hop type? (it's not really L, and 
            // different enough from other 'E's) 
            final char hopType;
            if (!framesAsEmbeds && (elementStr.equalsIgnoreCase(FRAME) || elementStr.equalsIgnoreCase(IFRAME))) {
                hopType = Link.NAVLINK_HOP;
            } else {
                hopType = Link.EMBED_HOP;
            }
            processEmbed(curi, value, context, hopType);
        } else if (attr.start(6) > -1) {
            // CODEBASE 
            codebase = (value instanceof String) ? (String) value : value.toString();
            CharSequence context = Link.elementContext(element, attr.group(6));
            processEmbed(curi, codebase, context);
        } else if (attr.start(7) > -1) {
            // CLASSID, DATA 
            if (resources == null) {
                resources = new ArrayList<String>();
            }
            resources.add(value.toString());
        } else if (attr.start(8) > -1) {
            // ARCHIVE 
            if (resources == null) {
                resources = new ArrayList<String>();
            }
            String[] multi = TextUtils.split(WHITESPACE, value);
            for (int i = 0; i < multi.length; i++) {
                resources.add(multi[i]);
            }
        } else if (attr.start(9) > -1) {
            // CODE 
            if (resources == null) {
                resources = new ArrayList<String>();
            }
            // If element is applet and code value does not end with 
            // '.class' then append '.class' to the code value. 
            if (elementStr.equalsIgnoreCase(APPLET) && !value.toString().toLowerCase().endsWith(CLASSEXT)) {
                resources.add(value.toString() + CLASSEXT);
            } else {
                resources.add(value.toString());
            }
        } else if (attr.start(10) > -1) {
            // VALUE, with possibility of URI 
            // store value, context for handling at end 
            valueVal = value;
            valueContext = Link.elementContext(element, attr.group(10));
        } else if (attr.start(11) > -1) {
            // STYLE inline attribute 
            // then, parse for URIs 
            this.numberOfLinksExtracted += ExtractorCSS.processStyleCode(curi, value, getController());
        } else if (attr.start(12) > -1) {
            // METHOD 
            method = value;
        } else if (attr.start(13) > -1) {
            if ("NAME".equalsIgnoreCase(attrName.toString())) {
                // remember 'name' for end-analysis 
                nameVal = value;
            }
            if ("FLASHVARS".equalsIgnoreCase(attrName.toString())) {
                // consider FLASHVARS attribute immediately 
                valueContext = Link.elementContext(element, attr.group(13));
                considerQueryStringValues(curi, value, valueContext, Link.SPECULATIVE_HOP);
            }
        }
    }
    TextUtils.recycleMatcher(attr);
    // finish handling codebase/resources now that all available 
    if (resources != null) {
        Iterator<String> iter = resources.iterator();
        UURI codebaseURI = null;
        String res = null;
        try {
            if (codebase != null) {
                // TODO: Pass in the charset. 
                codebaseURI = UURIFactory.getInstance(curi.getUURI(), codebase);
            }
            while (iter.hasNext()) {
                res = iter.next().toString();
                res = (String) TextUtils.unescapeHtml(res);
                if (codebaseURI != null) {
                    res = codebaseURI.resolve(res).toString();
                }
                processEmbed(curi, res, element);
            }
        } catch (URIException e) {
            curi.addLocalizedError(getName(), e, "BAD CODEBASE " + codebase);
        } catch (IllegalArgumentException e) {
            DevUtils.logger.log(Level.WARNING, "processGeneralTag()\n" + "codebase=" + codebase + " res=" + res + "\n" + DevUtils.extraInfo(), e);
        }
    }
    // finish handling form action, now method is available 
    if (action != null) {
        if (method == null || "GET".equalsIgnoreCase(method.toString()) || !((Boolean) getUncheckedAttribute(curi, ATTR_EXTRACT_ONLY_FORM_GETS)).booleanValue()) {
            processLink(curi, action, actionContext);
        }
    }
    // finish handling VALUE 
    if (valueVal != null) {
        if ("PARAM".equalsIgnoreCase(elementStr) && "flashvars".equalsIgnoreCase(nameVal.toString())) {
            // special handling for <PARAM NAME='flashvars" VALUE=""> 
            String queryStringLike = valueVal.toString();
            // treat value as query-string-like "key=value[;key=value]*" pairings 
            considerQueryStringValues(curi, queryStringLike, valueContext, Link.SPECULATIVE_HOP);
        } else {
            // regular VALUE handling 
            if (extractValueAttributes) {
                considerIfLikelyUri(curi, valueVal, valueContext, Link.NAVLINK_HOP);
            }
        }
    }
}
