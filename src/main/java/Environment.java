import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author      Amos Zhou
 */
public class Environment {
    public HashMap<Skill, List<WorkGroup>> workGroups = new HashMap<Skill, List<WorkGroup>>();
    public List<Job> jobs = new ArrayList<>();

    public double evaluate(Arrangement arrangement) {
        return -1;
    }

    public int numOfWorkGroups() {
        int sum = 0;
        for(Skill skill: workGroups.keySet()) {
            sum += workGroups.get(skill).size();
        }
        return sum;
    }

    public List<WorkGroup> randomPair() throws ApsException {
        // 1 Select skill
        if(workGroups.keySet().size() > 0) {
            List<Skill> skillCategories = new ArrayList<>(workGroups.keySet());
            Collections.shuffle(skillCategories);
            Skill selected = null;
            for(Skill skill : skillCategories) {
                // 需要至少两个工组才能选出一对
                if(workGroups.get(skill).size() > 1) {
                    selected = skill;
                }
            }
            if(selected != null) {
                // 2 Select two work groups from the skill category
                List<WorkGroup> selectedWorkGroups = workGroups.get(selected);
                Collections.shuffle(selectedWorkGroups);
                List<WorkGroup> pair = selectedWorkGroups.subList(0,2);
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
