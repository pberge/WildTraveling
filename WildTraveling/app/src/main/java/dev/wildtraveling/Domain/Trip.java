package dev.wildtraveling.Domain;

import java.util.Date;
import java.util.List;

import dev.wildtraveling.Util.Entity;

/**
 * Created by pere on 3/30/17.
 */
public class Trip implements Entity {
    private String id;
    private String name;
    private String destination;
    private Date initDate;
    private Date finalDate;
    private List<String> participants;
    private String userId;


    public Trip(String name, String destination, Date initDate, Date finalDate, List<String> participants, String userId) {
        this.name = name;
        this.destination = destination;
        this.initDate = initDate;
        this.finalDate = finalDate;
        this.participants = participants;
        this.userId = userId;

    }

    public Trip() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

