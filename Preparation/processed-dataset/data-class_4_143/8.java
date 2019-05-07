/**
   * Load the action listeners.
   */
private static void initActions() {
    actionHash = new HashMap();
    inputHandler = new DefaultInputHandler();
    inputHandler.addDefaultKeyBindings();
    // Python written actions 
    loadXMLActions(Jext.class.getResourceAsStream("jext.actions.xml"), "jext.actions.xml");
    // native Java actions 
    addAction(new BeginLine());
    addAction(new BoxComment());
    addAction(new CompleteWord());
    addAction(new CompleteWordAll());
    addAction(new CreateTemplate());
    addAction(new EndLine());
    addAction(new JoinAllLines());
    addAction(new JoinLines());
    addAction(new LeftIndent());
    addAction(new OpenUrl());
    addAction(new Print());
    //    addAction(new RemoveSpaces()); 
    addAction(new RemoveWhitespace());
    addAction(new RightIndent());
    addAction(new SimpleComment());
    addAction(new SimpleUnComment());
    addAction(new SpacesToTabs());
    addAction(new TabsToSpaces());
    addAction(new ToLowerCase());
    addAction(new ToUpperCase());
    addAction(new WingComment());
    addAction(new WordCount());
    // init OneClick! actions 
    addAction(new OneAutoIndent());
    // One Click ! 
    loadXMLOneClickActions(Jext.class.getResourceAsStream("jext.oneclickactions.xml"), "jext.oneclickactions.xml");
    //    addAction(new OneClickAction("one_paste",             "paste")); 
    //    addAction(new OneClickAction("one_reverse_paste",     "reverse_paste")); 
    //    addAction(new OneClickAction("one_delete_line",       "delete_line")); 
    //    addAction(new OneClickAction("one_join_lines",        "join_lines")); 
    //    addAction(new OneClickAction("one_right_indent",      "right_indent")); 
    //    addAction(new OneClickAction("one_left_indent",       "left_indent")); 
    //    addAction(new OneClickAction("one_simple_comment",    "simple_comment")); 
    //    addAction(new OneClickAction("one_simple_uncomment",  "simple_uncomment")); 
    //    addAction(new OneClickAction("one_complete_word",     "complete_word")); 
    // key bindings 
    addJextKeyBindings();
}
