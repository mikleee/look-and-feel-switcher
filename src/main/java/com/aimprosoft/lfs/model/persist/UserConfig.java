package com.aimprosoft.lfs.model.persist;

import javax.persistence.*;

/**
 * @author Mikhail Tkachenko
 */
@Entity
@Table(name = "ts_user_config")
public class UserConfig extends PersistModel {
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "config_key")
    private ConfigKey key;
    @Column(name = "config_value")
    private String value;


    public ConfigKey getKey() {
        return key;
    }

    public void setKey(ConfigKey key) {
        this.key = key;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
