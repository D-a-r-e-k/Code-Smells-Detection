public void notifyOfBot() {
    if (GUIPreferences.getInstance().getNagForBotReadme() && !WarningShown) {
        WarningShown = true;
        String title = Messages.getString("BotGUI.notifyOfBot.title");
        //$NON-NLS-1$  
        String body = Messages.getString("BotGUI.notifyOfBot.message");
        //$NON-NLS-1$  
        Dimension screenSize = frame.getToolkit().getScreenSize();
        frame.pack();
        frame.setLocation(screenSize.width / 2 - frame.getSize().width / 2, screenSize.height / 2 - frame.getSize().height / 2);
        ConfirmDialog confirm = new ConfirmDialog(frame, title, body, true);
        confirm.setVisible(true);
        if (!confirm.getShowAgain()) {
            GUIPreferences.getInstance().setNagForBotReadme(false);
        }
        if (confirm.getAnswer()) {
            File helpfile = new File("docs/ai-readme.txt");
            //$NON-NLS-1$  
            new CommonHelpDialog(frame, helpfile).setVisible(true);
        }
    }
}
