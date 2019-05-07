// IClassFormatOutput:  
public void writeInClassFormat(final UDataOutputStream out) throws IOException {
    int number_of_exceptions = m_exceptions.size();
    // use size() if class becomes non-final  
    out.writeU2(number_of_exceptions);
    for (int i = 0; i < number_of_exceptions; i++) {
        out.writeU2(get(i));
    }
}
