package pt.ua.deti.uasmartsignage.events;

import org.springframework.context.ApplicationEvent;

public class RulesChangedEvent extends ApplicationEvent {

    private final long groupId;

    public RulesChangedEvent(Object source, long groupId) {
        super(source);
        this.groupId = groupId;
    }

    public long getGroupId() {
        return this.groupId;
    }
}
