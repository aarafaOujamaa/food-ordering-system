package com.domain.ordering.system.domain.entity;

import java.util.Objects;

public abstract class BaseEntity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }


    // each entity must have a unique id, and equals and hashCode should be implemented
    /*
    En DDD, chaque entité est identifiée de manière unique par un ID. La redéfinition de equals et hashCode garantit qu'on
     ne considère comme égales que les entités ayant le même ID.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
