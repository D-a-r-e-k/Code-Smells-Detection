protected int defineSymbol(Movie movie, SWFTagTypes timelineWriter, SWFTagTypes definitionWriter) throws IOException {
    int id = getNextId(movie);
    SWFShape shape = definitionWriter.tagDefineMorphShape(id, shape1.getRect(), shape2.getRect());
    shape1.hasAlpha = true;
    shape2.hasAlpha = true;
    shape1.writeShape(shape);
    shape2.writeShape(shape);
    return id;
}
