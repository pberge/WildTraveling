package dev.wildtraveling.Domain;

import dev.wildtraveling.Util.Entity;

/**
 * Created by pere on 4/18/17.
 */
public class Destination implements Entity {

    private String id;
    private String name;

    public Destination(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Destination() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }
}
