/**
     *  Handles constructs of type %%(style) and %%class
     * @param newLine
     * @return An Element containing the div or span, depending on the situation.
     * @throws IOException
     */
private Element handleDiv(boolean newLine) throws IOException {
    int ch = nextToken();
    Element el = null;
    if (ch == '%') {
        String style = null;
        String clazz = null;
        ch = nextToken();
        // 
        //  Style or class? 
        // 
        if (ch == '(') {
            style = readBraceContent('(', ')');
        } else if (Character.isLetter((char) ch)) {
            pushBack(ch);
            clazz = readUntil(" \t\n\r");
            ch = nextToken();
            // 
            //  Pop out only spaces, so that the upcoming EOL check does not check the 
            //  next line. 
            // 
            if (ch == '\n' || ch == '\r') {
                pushBack(ch);
            }
        } else {
            // 
            // Anything else stops. 
            // 
            pushBack(ch);
            try {
                Boolean isSpan = m_styleStack.pop();
                if (isSpan == null) {
                } else if (isSpan.booleanValue()) {
                    el = popElement("span");
                } else {
                    el = popElement("div");
                }
            } catch (EmptyStackException e) {
                log.debug("Page '" + m_context.getName() + "' closes a %%-block that has not been opened.");
                return m_currentElement;
            }
            return el;
        }
        // 
        //  Check if there is an attempt to do something nasty 
        // 
        try {
            style = StringEscapeUtils.unescapeHtml(style);
            if (style != null && style.indexOf("javascript:") != -1) {
                log.debug("Attempt to output javascript within CSS:" + style);
                ResourceBundle rb = m_context.getBundle(InternationalizationManager.CORE_BUNDLE);
                return addElement(makeError(rb.getString("markupparser.error.javascriptattempt")));
            }
        } catch (NumberFormatException e) {
            // 
            //  If there are unknown entities, we don't want the parser to stop. 
            // 
            ResourceBundle rb = m_context.getBundle(InternationalizationManager.CORE_BUNDLE);
            Object[] args = { e.getMessage() };
            String msg = MessageFormat.format(rb.getString("markupparser.error.parserfailure"), args);
            return addElement(makeError(msg));
        }
        // 
        //  Decide if we should open a div or a span? 
        // 
        String eol = peekAheadLine();
        if (eol.trim().length() > 0) {
            // There is stuff after the class 
            el = new Element("span");
            m_styleStack.push(Boolean.TRUE);
        } else {
            startBlockLevel();
            el = new Element("div");
            m_styleStack.push(Boolean.FALSE);
        }
        if (style != null)
            el.setAttribute("style", style);
        if (clazz != null)
            el.setAttribute("class", clazz);
        el = pushElement(el);
        return el;
    }
    pushBack(ch);
    return el;
}
