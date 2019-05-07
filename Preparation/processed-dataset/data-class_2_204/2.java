public List<AuthorisingAgent> getSortedAuthAgents() {
    List<AuthorisingAgent> agents = new LinkedList<AuthorisingAgent>();
    agents.addAll(site.getAuthorisingAgents());
    Collections.sort(agents, new AuthorisingAgent.AuthorisingAgentComparator());
    return agents;
}
