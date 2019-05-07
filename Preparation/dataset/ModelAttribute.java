final class ModelAttribute {
    private String id;
    private String value;
    private ModelDocumentation documentation;
    private LinkedList<ModelAttribute> attributes =
        new LinkedList<ModelAttribute>();
    
    ModelAttribute() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation aValue) {
        documentation = aValue;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String aValue) {
        id = aValue;
    }
    
    String getValue() {
        return value;
    }
    
    void setValue(final String aValue) {
        value = aValue;
    }
    
    List<ModelAttribute> getAttributes() {
        return attributes;
    }
}