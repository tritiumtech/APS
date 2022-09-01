import algo.Environment;
import entities.Job;
import entities.Skill;
import entities.WorkGroup;
import utils.WorkCalendar;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TestEnvironment {
    public static Environment testEnv() {
        WorkCalendar calendar = new WorkCalendar(LocalTime.parse("09:00"), LocalTime.parse("18:00"));
        Environment env = new Environment(calendar);
        env.startDateTime = ZonedDateTime.parse("2022-10-01T09:00:00.000000+08:00[Asia/Shanghai]");
        List<Skill> allSkills = new ArrayList<>();
        seedWorkGroups(env, allSkills);
        System.out.println("Work groups initialised: " + env.skillGroupMapping.size());
        seedJobs(env, allSkills);
        System.out.println("Jobs initialised: " + env.jobs.size());
        return env;
    }

    public static void main(String args[]) {
        Environment env = TestEnvironment.testEnv();
        List<Skill> allSkills = new ArrayList<>();
        seedWorkGroups(env, allSkills);
        seedJobs(env, allSkills);
    }

    private static void seedWorkGroups(Environment env, List<Skill> allSkills) {
        /* Add a few test skills */
        Skill braTypeA = new Skill("文胸类型A", 2);
        Skill braTypeB = new Skill("文胸类型B", 2);
        Skill braTypeC = new Skill("文胸类型C", 2);
        Skill braTypeSuper = new Skill("文胸", 1);
        braTypeA.setParent(braTypeSuper);
        braTypeB.setParent(braTypeSuper);
        braTypeC.setParent(braTypeSuper);
        Skill pantyTypeA = new Skill("小裤类型A", 2);
        Skill pantyTypeB = new Skill("小裤类型B", 2);
        Skill pantyTypeC = new Skill("小裤类型C", 2);
        Skill pantyTypeSuper = new Skill("小裤", 1);
        pantyTypeA.setParent(pantyTypeSuper);
        pantyTypeB.setParent(pantyTypeSuper);
        pantyTypeC.setParent(pantyTypeSuper);
        allSkills.add(braTypeA);
        allSkills.add(braTypeB);
        allSkills.add(braTypeC);
        allSkills.add(braTypeSuper);
        allSkills.add(pantyTypeA);
        allSkills.add(pantyTypeA);
        allSkills.add(pantyTypeA);
        allSkills.add(pantyTypeSuper);
        /* End of adding test skills */

        /* Setting up work groups */
        WorkGroup bra1 = new WorkGroup("bra1", braTypeSuper, env.calendar);
        bra1.addSkill(braTypeA, 12.0f);
        bra1.addSkill(braTypeB, 11.3f);
        bra1.addSkill(braTypeSuper, 13.2f);
        WorkGroup bra2 = new WorkGroup("bra2", braTypeSuper, env.calendar);
        bra2.addSkill(braTypeB, 18.2f);
        bra2.addSkill(braTypeC, 12.0f);
        bra2.addSkill(braTypeSuper, 15.7f);
        WorkGroup bra3 = new WorkGroup("bra3", braTypeSuper, env.calendar);
        bra3.addSkill(braTypeB, 16.1f);
        bra3.addSkill(braTypeC, 14.9f);
        bra3.addSkill(braTypeSuper, 15.5f);
        WorkGroup panties1 = new WorkGroup("panties1", pantyTypeSuper, env.calendar);
        panties1.addSkill(pantyTypeA, 9f);
        panties1.addSkill(pantyTypeB, 10.7f);
        panties1.addSkill(pantyTypeC, 12f);
        panties1.addSkill(pantyTypeSuper, 11.1f);
        WorkGroup panties2 = new WorkGroup("panties2", pantyTypeSuper, env.calendar);
        panties2.addSkill(pantyTypeA, 5f);
        panties2.addSkill(pantyTypeB, 8.9f);
        panties2.addSkill(pantyTypeC, 11f);
        panties2.addSkill(pantyTypeSuper, 9.7f);
        /* End of setting up work groups */

        env.skillGroupMapping = new HashMap<Skill, List<WorkGroup>>();
        List<WorkGroup> braGroups = new ArrayList<WorkGroup>();
        braGroups.add(bra1);
        braGroups.add(bra2);
        braGroups.add(bra3);
        env.workGroups.add(bra1);
        env.workGroups.add(bra2);
        env.workGroups.add(bra3);
        env.skillGroupMapping.put(braTypeSuper, braGroups);
        List<WorkGroup> pantyGroups = new ArrayList<WorkGroup>();
        pantyGroups.add(panties1);
        pantyGroups.add(panties2);
        env.workGroups.add(panties1);
        env.workGroups.add(panties2);
        env.skillGroupMapping.put(pantyTypeSuper, pantyGroups);

        try {
            List<WorkGroup> pair = env.randomPair();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void seedJobs(Environment env, List<Skill> allSkills) {
        Collections.shuffle(allSkills);
        env.jobs.add(new Job("0001", "2022-12-03T08:30:00+08:00", allSkills.get(0), 1000, env.calendar));
        env.jobs.add(new Job("0002", "2022-09-12T13:30:00+10:00", allSkills.get(1), 1250, env.calendar));
        env.jobs.add(new Job("0003", "2022-10-15T09:30:00+11:00", allSkills.get(2), 2500, env.calendar));
        env.jobs.add(new Job("0004", "2022-11-19T09:30:00+08:00", allSkills.get(3), 300, env.calendar));
        env.jobs.add(new Job("0005", "2022-12-21T09:30:00-08:00", allSkills.get(4), 500, env.calendar));
        env.jobs.add(new Job("0006", "2022-11-14T09:30:00+08:00", allSkills.get(5), 900, env.calendar));
        env.jobs.add(new Job("0007", "2022-10-29T09:30:00-11:00", allSkills.get(6), 827, env.calendar));
        env.jobs.add(new Job("0008", "2022-11-05T09:30:00+08:00", allSkills.get(7), 4500, env.calendar));
        env.jobs.add(new Job("0009", "2022-12-11T09:30:00+10:00", allSkills.get(2), 2100, env.calendar));
        env.jobs.add(new Job("0010", "2023-01-23T09:30:00-01:00", allSkills.get(5), 1500, env.calendar));
    }
}
