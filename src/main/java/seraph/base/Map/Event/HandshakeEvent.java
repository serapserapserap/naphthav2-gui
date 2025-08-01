package seraph.base.Map.Event;

import java.util.Map;

public class HandshakeEvent extends Event {
    public Map<String, String> modTags;
    public HandshakeEvent(Map<String, String> modTags){
        this.modTags = modTags;
    }
}
