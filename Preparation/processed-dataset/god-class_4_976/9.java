public void setPageFont(Font pageFont) {
    pageFont_ = pageFont;
    // Build the header font; 
    headerFont_ = new Font(pageFont_.getName(), Font.BOLD, pageFont_.getSize());
}
