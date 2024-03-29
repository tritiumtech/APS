package entities;

import exceptions.ApsException;
import utils.WorkCalendar;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Job {
    public String id;
    public ZonedDateTime deliveryDt;
    public Skill skill;
    public int pieces;
    public ZonedDateTime startDt;
    public ZonedDateTime endDt;
    public float duration;
    public WorkCalendar calendar;

    public Job(String id, String deliveryDate, Skill skill, int pieces, WorkCalendar calendar) {
        this.id = id;
        this.deliveryDt = ZonedDateTime.parse(deliveryDate);
        this.skill = skill;
        this.pieces = pieces;
        this.calendar = calendar;
    }

    /**
     * 在分配任务到工组时必须调用此方法，以确定任务时间
     * TODO 未考虑时间曲线
     *
     * @param workGroup
     * @throws ApsException
     */
    public void calcWorkHours(WorkGroup workGroup) throws ApsException {
        // Do skill match
        List matches = new ArrayList<>();
        for (Skill skill : workGroup.skills.keySet()) {
            if (this.skill.equals(skill)) {
                matches.add(skill);
            }
        }
        if (matches.size() > 0) {
            Collections.sort(matches);
            Skill skill = (Skill) matches.get(0);
            duration = pieces * workGroup.skills.get(skill); // 件数*单件用时
        } else
            throw new ApsException("APS003: 不存在工组满足任务所需技能");

    }

    public ZonedDateTime calcEndDt(ZonedDateTime now, float duration) {
        return calendar.addWorkMinutes(now, duration);
    }

    public float workTimeDifference(ZonedDateTime t1EndDt, ZonedDateTime deliveryDate) {
        return calendar.workDaysBetween(t1EndDt, deliveryDate);
    }

    public String toString() {
        return this.id + " " + this.skill.name + " " + this.startDt + " " + this.endDt + " " + this.deliveryDt;
    }
}
