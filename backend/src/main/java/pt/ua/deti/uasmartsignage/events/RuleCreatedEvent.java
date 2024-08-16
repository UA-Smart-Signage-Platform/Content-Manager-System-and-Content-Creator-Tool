package pt.ua.deti.uasmartsignage.events;

import org.springframework.context.ApplicationEvent;
import pt.ua.deti.uasmartsignage.models.Rule;

public class RuleCreatedEvent extends ApplicationEvent {

    private final Rule rule;

    public RuleCreatedEvent(Object source, Rule rule) {
        super(source);
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }
}
