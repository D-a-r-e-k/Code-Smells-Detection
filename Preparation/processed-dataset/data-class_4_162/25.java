private String makeSectionTitle(String title) {
    title = title.trim();
    String outTitle;
    try {
        JSPWikiMarkupParser dtr = getCleanTranslator();
        dtr.setInputReader(new StringReader(title));
        CleanTextRenderer ctt = new CleanTextRenderer(m_context, dtr.parse());
        outTitle = ctt.getString();
    } catch (IOException e) {
        log.fatal("CleanTranslator not working", e);
        throw new InternalWikiException("CleanTranslator not working as expected, when cleaning title" + e.getMessage());
    }
    return outTitle;
}
