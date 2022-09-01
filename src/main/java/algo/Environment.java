package algo;

import entities.Job;
import entities.Skill;
import entities.WorkGroup;
import exceptions.ApsException;
import utils.WorkCalendar;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Amos Zhou
 */
public class Environment {
    public HashMap<Skill, List<WorkGroup>> skillGroupMapping = new HashMap<Skill, List<WorkGroup>>();
    public List<WorkGroup> workGroups = new ArrayList<>();
    public List<Job> jobs = new ArrayList<>();
    public WorkCalendar calendar;
    public ZonedDateTime startDateTime; // 排程时间窗口的起始

    public Environment(WorkCalendar calendar) {
        this.calendar = calendar;
    }

    public double evaluate(Arrangement arrangement) {
        return -1;
    }

    public int numOfWorkGroups() {
        int sum = 0;
        for (Skill skill : skillGroupMapping.keySet()) {
            sum += skillGroupMapping.get(skill).size();
        }
        return sum;
    }

    public List<WorkGroup> randomPair() throws ApsException {
        // 1 Select skill
        if (skillGroupMapping.keySet().size() > 0) {
            List<Skill> skillCategories = new ArrayList<>(skillGroupMapping.keySet());
            Collections.shuffle(skillCategories);
            Skill selected = null;
            for (Skill skill : skillCategories) {
                // 需要至少两个工组才能选出一对
                if (skillGroupMapping.get(skill).size() > 1) {
                    selected = skill;
                }
            }
            if (selected != null) {
                // 2 Select two work groups from the skill category
                List<WorkGroup> selectedWorkGroups = skillGroupMapping.get(selected);
                Collections.shuffle(selectedWorkGroups);
                List<WorkGroup> pair = selectedWorkGroups.subList(0, 2);
                System.out.println(pair);
                return pair;
            } else {
                throw new ApsException("APS002: 技能集均无两个以上工组供选择");
            }
        } else {
            throw new ApsException("APS001: 车间未设置工组");
        }
    }
}
