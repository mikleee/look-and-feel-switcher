package com.aimprosoft.look_and_feel_switcher.model.persist;

import java.io.Serializable;

/**
 * The representation of the entities which could be serialized
 *
 * @author Mikhail Tkachenko
 */
public interface PersistModel<T extends Serializable> extends Serializable {

    T getId();

    void setId(T id);

}