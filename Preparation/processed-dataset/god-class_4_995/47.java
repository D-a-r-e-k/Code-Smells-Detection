/**
     *  Like original handleOrderedList() and handleUnorderedList()
     *  however handles both ordered ('#') and unordered ('*') mixed together.
     */
// FIXME: Refactor this; it's a bit messy. 
private Element handleGeneralList() throws IOException {
    startBlockLevel();
    String strBullets = readWhile("*#");
    // String strBulletsRaw = strBullets;      // to know what was original before phpwiki style substitution 
    int numBullets = strBullets.length();
    // override the beginning portion of bullet pattern to be like the previous 
    // to simulate PHPWiki style lists 
    if (m_allowPHPWikiStyleLists) {
        // only substitute if different 
        if (!(strBullets.substring(0, Math.min(numBullets, m_genlistlevel)).equals(m_genlistBulletBuffer.substring(0, Math.min(numBullets, m_genlistlevel))))) {
            if (numBullets <= m_genlistlevel) {
                // Substitute all but the last character (keep the expressed bullet preference) 
                strBullets = (numBullets > 1 ? m_genlistBulletBuffer.substring(0, numBullets - 1) : "") + strBullets.substring(numBullets - 1, numBullets);
            } else {
                strBullets = m_genlistBulletBuffer + strBullets.substring(m_genlistlevel, numBullets);
            }
        }
    }
    // 
    //  Check if this is still of the same type 
    // 
    if (strBullets.substring(0, Math.min(numBullets, m_genlistlevel)).equals(m_genlistBulletBuffer.substring(0, Math.min(numBullets, m_genlistlevel)))) {
        if (numBullets > m_genlistlevel) {
            pushElement(new Element(getListType(strBullets.charAt(m_genlistlevel++))));
            for (; m_genlistlevel < numBullets; m_genlistlevel++) {
                // bullets are growing, get from new bullet list 
                pushElement(new Element("li"));
                pushElement(new Element(getListType(strBullets.charAt(m_genlistlevel))));
            }
        } else if (numBullets < m_genlistlevel) {
            //  Close the previous list item. 
            // buf.append( m_renderer.closeListItem() ); 
            popElement("li");
            for (; m_genlistlevel > numBullets; m_genlistlevel--) {
                // bullets are shrinking, get from old bullet list 
                popElement(getListType(m_genlistBulletBuffer.charAt(m_genlistlevel - 1)));
                if (m_genlistlevel > 0) {
                    popElement("li");
                }
            }
        } else {
            if (m_genlistlevel > 0) {
                popElement("li");
            }
        }
    } else {
        // 
        //  The pattern has changed, unwind and restart 
        // 
        int numEqualBullets;
        int numCheckBullets;
        // find out how much is the same 
        numEqualBullets = 0;
        numCheckBullets = Math.min(numBullets, m_genlistlevel);
        while (numEqualBullets < numCheckBullets) {
            // if the bullets are equal so far, keep going 
            if (strBullets.charAt(numEqualBullets) == m_genlistBulletBuffer.charAt(numEqualBullets))
                numEqualBullets++;
            else
                break;
        }
        //unwind 
        for (; m_genlistlevel > numEqualBullets; m_genlistlevel--) {
            popElement(getListType(m_genlistBulletBuffer.charAt(m_genlistlevel - 1)));
            if (m_genlistlevel > numBullets) {
                popElement("li");
            }
        }
        //rewind 
        pushElement(new Element(getListType(strBullets.charAt(numEqualBullets++))));
        for (int i = numEqualBullets; i < numBullets; i++) {
            pushElement(new Element("li"));
            pushElement(new Element(getListType(strBullets.charAt(i))));
        }
        m_genlistlevel = numBullets;
    }
    // 
    //  Push a new list item, and eat away any extra whitespace 
    // 
    pushElement(new Element("li"));
    readWhile(" ");
    // work done, remember the new bullet list (in place of old one) 
    m_genlistBulletBuffer.setLength(0);
    m_genlistBulletBuffer.append(strBullets);
    return m_currentElement;
}
