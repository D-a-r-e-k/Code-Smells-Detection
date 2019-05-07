public void readChildren(XMLStreamReader xsr) throws XMLStreamException {
    while (xsr.nextTag() != XMLStreamConstants.END_ELEMENT) {
        Modifier modifier = new Modifier(xsr, Specification.this);
        Specification.this.addModifier(modifier);
        Specification.this.specialModifiers.add(modifier);
    }
}
