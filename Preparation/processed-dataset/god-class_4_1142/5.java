// end parseSchema  
private void validateAnnotations(ArrayList annotationInfo) {
    if (fAnnotationValidator == null) {
        createAnnotationValidator();
    }
    final int size = annotationInfo.size();
    final XMLInputSource src = new XMLInputSource(null, null, null);
    fGrammarBucketAdapter.refreshGrammars(fGrammarBucket);
    for (int i = 0; i < size; i += 2) {
        src.setSystemId((String) annotationInfo.get(i));
        XSAnnotationInfo annotation = (XSAnnotationInfo) annotationInfo.get(i + 1);
        while (annotation != null) {
            src.setCharacterStream(new StringReader(annotation.fAnnotation));
            try {
                fAnnotationValidator.parse(src);
            } catch (IOException exc) {
            }
            annotation = annotation.next;
        }
    }
}
