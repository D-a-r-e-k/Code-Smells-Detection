/** Create the source model after determining which input language */
protected void determineLanguage() {
    OntModelSpec s = null;
    if (m_options.hasLangDamlOption()) {
        // DAML language specified  
        if (m_options.hasUseInfOption()) {
            s = OntModelSpec.DAML_MEM_RULE_INF;
        } else {
            s = OntModelSpec.DAML_MEM;
        }
    } else if (m_options.hasLangRdfsOption()) {
        // RDFS language specified  
        if (m_options.hasUseInfOption()) {
            s = OntModelSpec.RDFS_MEM_RDFS_INF;
        } else {
            s = OntModelSpec.RDFS_MEM;
        }
    } else {
        // owl is the default  
        // s = OntModelSpec.getDefaultSpec( ProfileRegistry.OWL_LANG );  
        if (m_options.hasUseInfOption()) {
            s = OntModelSpec.OWL_MEM_RULE_INF;
        } else {
            s = OntModelSpec.OWL_MEM;
        }
    }
    m_source = ModelFactory.createOntologyModel(s, null);
    m_source.getDocumentManager().setProcessImports(false);
    // turn off strict checking on request  
    if (m_options.hasNoStrictOption()) {
        m_source.setStrictMode(false);
    }
}
