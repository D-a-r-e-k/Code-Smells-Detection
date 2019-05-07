/**
     *  Gobbles up all hyperlinks that are encased in square brackets.
     */
private Element handleHyperlinks(String linktext, int pos) {
    ResourceBundle rb = m_context.getBundle(InternationalizationManager.CORE_BUNDLE);
    StringBuilder sb = new StringBuilder(linktext.length() + 80);
    if (isAccessRule(linktext)) {
        return handleAccessRule(linktext);
    }
    if (isMetadata(linktext)) {
        return handleMetadata(linktext);
    }
    if (PluginManager.isPluginLink(linktext)) {
        try {
            PluginContent pluginContent = m_engine.getPluginManager().parsePluginLine(m_context, linktext, pos);
            // 
            //  This might sometimes fail, especially if there is something which looks 
            //  like a plugin invocation but is really not. 
            // 
            if (pluginContent != null) {
                addElement(pluginContent);
                pluginContent.executeParse(m_context);
            }
        } catch (PluginException e) {
            log.info("Failed to insert plugin: " + e.getMessage());
            //log.info( "Root cause:",e.getRootThrowable() ); 
            if (!m_wysiwygEditorMode) {
                ResourceBundle rbPlugin = m_context.getBundle(WikiPlugin.CORE_PLUGINS_RESOURCEBUNDLE);
                Object[] args = { e.getMessage() };
                return addElement(makeError(MessageFormat.format(rbPlugin.getString("plugin.error.insertionfailed"), args)));
            }
        }
        return m_currentElement;
    }
    try {
        LinkParser.Link link = m_linkParser.parse(linktext);
        linktext = link.getText();
        String linkref = link.getReference();
        // 
        //  Yes, we now have the components separated. 
        //  linktext = the text the link should have 
        //  linkref  = the url or page name. 
        // 
        //  In many cases these are the same.  [linktext|linkref]. 
        // 
        if (VariableManager.isVariableLink(linktext)) {
            Content el = new VariableContent(linktext);
            addElement(el);
        } else if (isExternalLink(linkref)) {
            // It's an external link, out of this Wiki 
            callMutatorChain(m_externalLinkMutatorChain, linkref);
            if (isImageLink(linkref)) {
                handleImageLink(linkref, linktext, link.hasReference());
            } else {
                makeLink(EXTERNAL, linkref, linktext, null, link.getAttributes());
                addElement(outlinkImage());
            }
        } else if (link.isInterwikiLink()) {
            // It's an interwiki link 
            // InterWiki links also get added to external link chain 
            // after the links have been resolved. 
            // FIXME: There is an interesting issue here:  We probably should 
            //        URLEncode the wikiPage, but we can't since some of the 
            //        Wikis use slashes (/), which won't survive URLEncoding. 
            //        Besides, we don't know which character set the other Wiki 
            //        is using, so you'll have to write the entire name as it appears 
            //        in the URL.  Bugger. 
            String extWiki = link.getExternalWiki();
            String wikiPage = link.getExternalWikiPage();
            if (m_wysiwygEditorMode) {
                makeLink(INTERWIKI, extWiki + ":" + wikiPage, linktext, null, link.getAttributes());
            } else {
                String urlReference = m_engine.getInterWikiURL(extWiki);
                if (urlReference != null) {
                    urlReference = TextUtil.replaceString(urlReference, "%s", wikiPage);
                    urlReference = callMutatorChain(m_externalLinkMutatorChain, urlReference);
                    if (isImageLink(urlReference)) {
                        handleImageLink(urlReference, linktext, link.hasReference());
                    } else {
                        makeLink(INTERWIKI, urlReference, linktext, null, link.getAttributes());
                    }
                    if (isExternalLink(urlReference)) {
                        addElement(outlinkImage());
                    }
                } else {
                    Object[] args = { extWiki };
                    addElement(makeError(MessageFormat.format(rb.getString("markupparser.error.nointerwikiref"), args)));
                }
            }
        } else if (linkref.startsWith("#")) {
            // It defines a local footnote 
            makeLink(LOCAL, linkref, linktext, null, link.getAttributes());
        } else if (TextUtil.isNumber(linkref)) {
            // It defines a reference to a local footnote 
            makeLink(LOCALREF, linkref, linktext, null, link.getAttributes());
        } else {
            int hashMark = -1;
            // 
            //  Internal wiki link, but is it an attachment link? 
            // 
            String attachment = findAttachment(linkref);
            if (attachment != null) {
                callMutatorChain(m_attachmentLinkMutatorChain, attachment);
                if (isImageLink(linkref)) {
                    attachment = m_context.getURL(WikiContext.ATTACH, attachment);
                    sb.append(handleImageLink(attachment, linktext, link.hasReference()));
                } else {
                    makeLink(ATTACHMENT, attachment, linktext, null, link.getAttributes());
                }
            } else if ((hashMark = linkref.indexOf('#')) != -1) {
                // It's an internal Wiki link, but to a named section 
                String namedSection = linkref.substring(hashMark + 1);
                linkref = linkref.substring(0, hashMark);
                linkref = MarkupParser.cleanLink(linkref);
                callMutatorChain(m_localLinkMutatorChain, linkref);
                String matchedLink;
                if ((matchedLink = linkExists(linkref)) != null) {
                    String sectref = "section-" + m_engine.encodeName(matchedLink) + "-" + wikifyLink(namedSection);
                    sectref = sectref.replace('%', '_');
                    makeLink(READ, matchedLink, linktext, sectref, link.getAttributes());
                } else {
                    makeLink(EDIT, linkref, linktext, null, link.getAttributes());
                }
            } else {
                // It's an internal Wiki link 
                linkref = MarkupParser.cleanLink(linkref);
                callMutatorChain(m_localLinkMutatorChain, linkref);
                String matchedLink = linkExists(linkref);
                if (matchedLink != null) {
                    makeLink(READ, matchedLink, linktext, null, link.getAttributes());
                } else {
                    makeLink(EDIT, linkref, linktext, null, link.getAttributes());
                }
            }
        }
    } catch (ParseException e) {
        log.info("Parser failure: ", e);
        Object[] args = { e.getMessage() };
        addElement(makeError(MessageFormat.format(rb.getString("markupparser.error.parserfailure"), args)));
    }
    return m_currentElement;
}
