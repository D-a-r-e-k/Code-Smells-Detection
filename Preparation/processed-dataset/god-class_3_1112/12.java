// Trims only the end 
private void trimBuffer() {
    int len = commandBuffer.length();
    commandBuffer.setLength(len - ((len > 1 && commandBuffer.charAt(len - 2) == '\r') ? 2 : 1));
}
