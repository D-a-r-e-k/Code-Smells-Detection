/**
   * Get a parameter that was explicitly set with setParameter
   * or setParameters.
   *
   * <p>This method does not return a default parameter value, which
   * cannot be determined until the node context is evaluated during
   * the transformation process.
   *
   *
   * @param name Name of the parameter.
   * @return A parameter that has been set with setParameter.
   */
public Object getParameter(String name) {
    if (null == m_params)
        return null;
    return m_params.get(name);
}
