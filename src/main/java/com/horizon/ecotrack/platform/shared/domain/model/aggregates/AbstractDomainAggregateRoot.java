package com.horizon.ecotrack.platform.shared.domain.model.aggregates;

import org.springframework.data.domain.AbstractAggregateRoot;
import java.util.Collection;

/**
 * Base class for EcoTrack domain aggregate roots.
 *
 * @param <T> aggregate root type
 */
public abstract class AbstractDomainAggregateRoot<
        T extends AbstractDomainAggregateRoot<T>>
        extends AbstractAggregateRoot<T> {

    protected void registerDomainEvent(Object event) {
        super.registerEvent(event);
    }

    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }

    @Override
    public void clearDomainEvents() {
        super.clearDomainEvents();
    }
}
