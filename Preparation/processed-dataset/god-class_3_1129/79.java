@Override
public String toString() {
    try {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(this.getUrl().toString());
        // Append body if it is a post or put 
        if (HTTPConstants.POST.equals(getMethod()) || HTTPConstants.PUT.equals(getMethod())) {
            stringBuffer.append("\nQuery Data: ");
            stringBuffer.append(getQueryString());
        }
        return stringBuffer.toString();
    } catch (MalformedURLException e) {
        return "";
    }
}
