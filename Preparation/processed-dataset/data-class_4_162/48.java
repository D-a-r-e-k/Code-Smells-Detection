private Element unwindGeneralList() {
    //unwind 
    for (; m_genlistlevel > 0; m_genlistlevel--) {
        popElement("li");
        popElement(getListType(m_genlistBulletBuffer.charAt(m_genlistlevel - 1)));
    }
    m_genlistBulletBuffer.setLength(0);
    return null;
}
