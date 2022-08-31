package entities;

import java.time.ZonedDateTime;

public class Job {
    public String id;
    public ZonedDateTime deliveryDate;
    public Skill skill;
    public int pieces;
    public ZonedDateTime startDt;
    public ZonedDateTime endDt;
    public float duration;

    public Job(String id, String deliveryDate, Skill skill, int pieces) {
        this.id = id;
        this.deliveryDate = ZonedDateTime.parse(deliveryDate);
        this.skill = skill;
        this.pieces = pieces;
    }
}
