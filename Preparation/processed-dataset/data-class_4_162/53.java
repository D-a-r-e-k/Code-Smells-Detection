private Element handleSlash(boolean newLine) throws IOException {
    int ch = nextToken();
    pushBack(ch);
    if (ch == '%' && !m_styleStack.isEmpty()) {
        return handleDiv(newLine);
    }
    return null;
}
