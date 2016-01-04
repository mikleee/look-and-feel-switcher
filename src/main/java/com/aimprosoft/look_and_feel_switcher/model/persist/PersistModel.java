package com.aimprosoft.look_and_feel_switcher.model.persist;

import java.io.Serializable;

/**
 * crated by m.tkachenko on 19.10.15 10:24
 */
public interface PersistModel<T extends Serializable> extends Serializable {

    T getId();

    void setId(T id);

}