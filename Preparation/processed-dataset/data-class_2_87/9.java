/**
     * Delete a WebMail user
     * @param user Name of the user to delete
     */
public void deleteUserData(String user, String domain) {
    String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain + System.getProperty("file.separator") + user + ".xml";
    File f = new File(path);
    if (!f.canWrite() || !f.delete()) {
        log(Storage.LOG_ERR, "SimpleStorage: Could not delete user " + user + " (" + path + ")!");
    } else {
        log(Storage.LOG_INFO, "SimpleStorage: Deleted user " + user + "!");
    }
    user_cache.remove(user + user_domain_separator + domain);
}
