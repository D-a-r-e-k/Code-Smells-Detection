// IClassFormatOutput:  
public void writeInClassFormat(UDataOutputStream out) throws IOException {
    out.writeU2(m_name_index);
    out.writeU4(length() - 6);
}
