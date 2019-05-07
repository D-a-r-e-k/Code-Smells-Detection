final class ModelExtensionPoint {
    private String id;
    private String parentPluginId;
    private String parentPointId;
    private ExtensionMultiplicity extensionMultiplicity =
        ExtensionMultiplicity.ONE;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameterDef> paramDefs =
        new LinkedList<ModelParameterDef>();

    ModelExtensionPoint() {
        // no-op
    }
    
    ModelDocumentation getDocumentation() {
        return documentation;
    }
    
    void setDocumentation(final ModelDocumentation value) {
        documentation = value;
    }
    
    ExtensionMultiplicity getExtensionMultiplicity() {
        return extensionMultiplicity;
    }
    
    void setExtensionMultiplicity(final ExtensionMultiplicity value) {
        extensionMultiplicity = value;
    }
    
    String getId() {
        return id;
    }
    
    void setId(final String value) {
        id = value;
    }
    
    String getParentPluginId() {
        return parentPluginId;
    }
    
    void setParentPluginId(final String value) {
        parentPluginId = value;
    }
    
    String getParentPointId() {
        return parentPointId;
    }
    
    void setParentPointId(final String value) {
        parentPointId = value;
    }
    
    List<ModelParameterDef> getParamDefs() {
        return paramDefs;
    }
}