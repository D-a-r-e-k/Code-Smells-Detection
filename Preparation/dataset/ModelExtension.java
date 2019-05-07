final class ModelExtension {
    private String id;
    private String pluginId;
    private String pointId;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameter> params =
        new LinkedList<ModelParameter>();
    
    ModelExtension() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    String getPluginId() {
        return pluginId;
    }
    
    void setPluginId(final String value) {
        pluginId = value;
    }
    
    String getPointId() {
        return pointId;
    }
    
    void setPointId(final String value) {
        pointId = value;
    }
    
    List<ModelParameter> getParams() {
        return params;
    }
}