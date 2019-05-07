final class ModelParameterDef {
    private String id;
    private ParameterMultiplicity multiplicity = ParameterMultiplicity.ONE;
    private ParameterType type = ParameterType.STRING;
    private String customData;
    private ModelDocumentation documentation;
    private LinkedList<ModelParameterDef> paramDefs =
        new LinkedList<ModelParameterDef>();
    private String defaultValue;
    
    ModelParameterDef() {
        // no-op
    }
    
    String getCustomData() {
        return customData;
    }
    
    void setCustomData(final String value) {
        customData = value;
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
    
    ParameterMultiplicity getMultiplicity() {
        return multiplicity;
    }
    
    void setMultiplicity(final ParameterMultiplicity value) {
        multiplicity = value;
    }
    
    ParameterType getType() {
        return type;
    }
    
    void setType(final ParameterType value) {
        type = value;
    }
    
    List<ModelParameterDef> getParamDefs() {
        return paramDefs;
    }
    
    String getDefaultValue() {
        return defaultValue;
    }
    
    void setDefaultValue(final String value) {
        defaultValue = value;
    }
}