protected boolean processGeneralTag(CharSequence element, CharSequence cs) {
    Matcher attr = TextUtils.getMatcher(EACH_ATTRIBUTE_EXTRACTOR, cs);
    // Just in case it's an OBJECT or APPLET tag 
    String codebase = null;
    ArrayList<String> resources = null;
    long tally = next.size();
    while (attr.find()) {
        int valueGroup = (attr.start(12) > -1) ? 12 : (attr.start(13) > -1) ? 13 : 14;
        int start = attr.start(valueGroup);
        int end = attr.end(valueGroup);
        CharSequence value = cs.subSequence(start, end);
        if (attr.start(2) > -1) {
            // HREF 
            CharSequence context = Link.elementContext(element, attr.group(2));
            if (element.toString().equalsIgnoreCase(LINK)) {
                // <LINK> elements treated as embeds (css, ico, etc) 
                processEmbed(value, context);
            } else {
                if (element.toString().equalsIgnoreCase(BASE)) {
                    try {
                        base = UURIFactory.getInstance(value.toString());
                    } catch (URIException e) {
                        extractErrorListener.noteExtractError(e, source, value);
                    }
                }
                // other HREFs treated as links 
                processLink(value, context);
            }
        } else if (attr.start(3) > -1) {
            // ACTION 
            CharSequence context = Link.elementContext(element, attr.group(3));
            processLink(value, context);
        } else if (attr.start(4) > -1) {
            // ON____ 
            processScriptCode(value);
        } else if (attr.start(5) > -1) {
            // SRC etc. 
            CharSequence context = Link.elementContext(element, attr.group(5));
            processEmbed(value, context);
        } else if (attr.start(6) > -1) {
            // CODEBASE 
            // TODO: more HTML deescaping? 
            codebase = TextUtils.replaceAll(ESCAPED_AMP, value, AMP);
            CharSequence context = Link.elementContext(element, attr.group(6));
            processEmbed(codebase, context);
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
            if (element.toString().toLowerCase().equals(APPLET) && !value.toString().toLowerCase().endsWith(CLASSEXT)) {
                resources.add(value.toString() + CLASSEXT);
            } else {
                resources.add(value.toString());
            }
        } else if (attr.start(10) > -1) {
            // VALUE 
            if (TextUtils.matches(LIKELY_URI_PATH, value)) {
                CharSequence context = Link.elementContext(element, attr.group(10));
                processLink(value, context);
            }
        } else if (attr.start(11) > -1) {
        }
    }
    TextUtils.recycleMatcher(attr);
    // handle codebase/resources 
    if (resources == null) {
        return (tally - next.size()) > 0;
    }
    Iterator iter = resources.iterator();
    UURI codebaseURI = null;
    String res = null;
    try {
        if (codebase != null) {
            // TODO: Pass in the charset. 
            codebaseURI = UURIFactory.getInstance(base, codebase);
        }
        while (iter.hasNext()) {
            res = iter.next().toString();
            // TODO: more HTML deescaping? 
            res = TextUtils.replaceAll(ESCAPED_AMP, res, AMP);
            if (codebaseURI != null) {
                res = codebaseURI.resolve(res).toString();
            }
            processEmbed(res, element);
        }
    } catch (URIException e) {
        extractErrorListener.noteExtractError(e, source, codebase);
    } catch (IllegalArgumentException e) {
        DevUtils.logger.log(Level.WARNING, "processGeneralTag()\n" + "codebase=" + codebase + " res=" + res + "\n" + DevUtils.extraInfo(), e);
    }
    return (tally - next.size()) > 0;
}
