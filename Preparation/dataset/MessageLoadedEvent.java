package net.suberic.pooka.event;

import net.suberic.pooka.thread.LoadMessageThread;

public class MessageLoadedEvent {
  LoadMessageThread source;
  int type;
  int loadedMessageCount = 0;
  int numMessages = 0;
  
  public static int LOADING_STARTING = 0;
  public static int LOADING_COMPLETE = 1;
  public static int MESSAGES_LOADED = 2;
  
  public MessageLoadedEvent(LoadMessageThread sourceThread, int eventType, int count, int max) {
    source = sourceThread;
    type = eventType;
    loadedMessageCount = count;
    numMessages = max;
  }
  
  public LoadMessageThread getSource() {
    return source;
  }
  
  public int getType() {
    return type;
  }
  
  public int getLoadedMessageCount() {
    return loadedMessageCount;
  }
  
  public int getNumMessages() {
    return numMessages;
  }
  
}
