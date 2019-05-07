/**
     * Set if the operating systems end-of-line line separator should
     * be used when serializing.  If set false NL character 
     * (decimal 10) is left alone, otherwise the new-line will be replaced on
     * output with the systems line separator. For example on UNIX this is
     * NL, while on Windows it is two characters, CR NL, where CR is the
     * carriage-return (decimal 13).
     *  
     * @param use_sytem_line_break True if an input NL is replaced with the 
     * operating systems end-of-line separator.
     * @return The previously set value of the serializer.
     */
public boolean setLineSepUse(boolean use_sytem_line_break) {
    boolean oldValue = m_lineSepUse;
    m_lineSepUse = use_sytem_line_break;
    return oldValue;
}
