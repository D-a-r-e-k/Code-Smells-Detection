//}}} 
//{{{ getActiveView() 
/**
     * Gets the currently active view.
     * @return the currently active view.
     * @since jsXe 0.4 pre1
     */
public static TabbedView getActiveView() {
    /*
        there is only one view per instance of jsXe currently.
        Eventually there may be more.
        */
    return m_activeView;
}
