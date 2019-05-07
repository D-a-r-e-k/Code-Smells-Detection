//}}} 
//{{{ getAction() 
/**
     * Gets the action set with the given name
     */
public static Action getAction(String name) {
    for (int i = 0; i < m_actionSets.size(); i++) {
        Action action = ((ActionSet) m_actionSets.get(i)).getAction(name);
        if (action != null) {
            return action;
        }
    }
    return null;
}
