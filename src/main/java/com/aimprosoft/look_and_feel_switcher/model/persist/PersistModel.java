package com.aimprosoft.look_and_feel_switcher.model.persist;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The representation of the entities which could be serialized
 *
 * @author Mikhail Tkachenko
 */
@MappedSuperclass
public abstract class PersistModel<T extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

}