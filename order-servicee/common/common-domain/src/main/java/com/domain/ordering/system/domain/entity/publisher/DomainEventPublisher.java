package com.domain.ordering.system.domain.entity.publisher;

import com.domain.ordering.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent) ;
}
