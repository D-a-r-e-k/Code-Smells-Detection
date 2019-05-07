public Object visit(final SourceFileAttribute_info attribute, final Object ctx) {
    m_classSrcFileName = attribute.getSourceFile(m_cls).m_value;
    return ctx;
}
