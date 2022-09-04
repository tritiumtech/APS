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
    public HashMap<Skill, List<WorkGroup>> skillGroupMapping = new HashMap<>();
    public List<WorkGroup> workGroups = new ArrayList<>();
    public List<Job> jobs = new ArrayList<>();
    public WorkCalendar calendar;
    public ZonedDateTime startDateTime; // 排程时间窗口的起始

    public Environment(WorkCalendar calendar) {
        this.calendar = calendar;
    }

    /**
     * 随机选取两个技能上兼容的工组
     * @return 包含两个工组的List
     * @throws ApsException
     */
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
                return selectedWorkGroups.subList(0, 2);
            } else {
                throw new ApsException("APS002: 技能集均无两个以上工组供选择");
            }
        } else {
            throw new ApsException("APS001: 车间未设置工组");
        }
    }
}
