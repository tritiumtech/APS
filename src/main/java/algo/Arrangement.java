package algo;

import entities.Job;
import entities.Skill;
import entities.WorkGroup;
import exceptions.ApsException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amos Zhou
 * <p>排产方案的染色体类。编码方案是一维数组，长度为任务数，仅记录任务所在工组，具体次序和时间轴上的位置不在染色体中记录。通过
 * crossover和mutation可达到以下效果：</p>
 * <ul>
 *     <li>crossover：两个排产方案直接互换基因。互换基因实际意味着互换任务和工组的绑定关系</li>
 *     <li>mutation操作1：相兼容的工组互换整段基因。</li>
 *     <li>mutation操作2：跨工组插入（不对调）。</li>
 * </ul>
 * 在所有操作中，均需考量品类能力、交期等约束，减少计算量。
 */

public class Arrangement implements Chromosome {
    public static final int JIT = 0;
    public int chromosome[];
    public Environment env;

    public Arrangement(Environment env) {
        chromosome = new int[env.jobs.size()];
        this.env = env;
    }

    public static Arrangement generateInstance(Environment env) throws ApsException {
        Arrangement arrangement = new Arrangement(env);
        // 考虑工组品类能力
        for (int i = 0; i < env.jobs.size(); i++) {
            Job job = env.jobs.get(i);
            Skill skillRoot = job.skill.getAncestor();
            List<WorkGroup> groups = env.skillGroupMapping.get(skillRoot);
            if (groups.size() > 0) {
                // 随机选取
                int selected = (int) (Math.random() * groups.size());
                WorkGroup group = groups.get(selected);
                arrangement.chromosome[i] = env.workGroups.indexOf(group);
            } else {
                throw new ApsException("APS003: 不存在工组满足任务所需技能");
            }
        }
        return arrangement;
    }

    /**
     * 进行基因互换操作。
     *
     * @param other
     * @return
     */
    @Override
    public List<Arrangement> crossover(Arrangement other) {
        // 1 随机选取起始点和结束点
        int i = (int) (Math.random() * chromosome.length);
        int j = (int) (Math.random() * chromosome.length);
        // 确保不会截了个寂寞
        while (i == j) {
            j = (int) (Math.random() * chromosome.length);
        }
        System.out.println(i + "," + j);

        // 2 互换
        Arrangement var1 = new Arrangement(env);
        Arrangement var2 = new Arrangement(env);

        int x = Math.min(i, j), y = Math.max(i, j) + 1, m = 0;
        for (; m < x; m++) {
            var1.chromosome[m] = chromosome[m];
            var2.chromosome[m] = other.chromosome[m];
        }
        for (; x < y; x++) {
            var1.chromosome[x] = other.chromosome[x];
            var2.chromosome[x] = chromosome[x];
        }
        for (m = y; m < chromosome.length; m++) {
            var1.chromosome[m] = chromosome[m];
            var2.chromosome[m] = other.chromosome[m];
        }

        List<Arrangement> newPair = new ArrayList<>();
        newPair.add(var1);
        newPair.add(var2);
        return newPair;
    }

    @Override
    public List<Arrangement> mutate() {
        return null;
    }

    @Override
    public double cost(Environment env) {
        double cost = 0;
        for (WorkGroup workgroup : env.workGroups) {
            arrangeGroup(workgroup);
            cost += workgroup.calculateCost(PlanMode.ExpiryJIT, env.startDateTime);
        }
        return cost;
    }

    private void arrangeGroup(WorkGroup workgroup) {
        workgroup.clearJobs();
        int groupIndex = env.workGroups.indexOf(workgroup);
        for(int i = 0; i < chromosome.length; i ++) {
            if(chromosome[i] == groupIndex) {
                workgroup.addJob(env.jobs.get(i));
            }
        }
        System.out.println(workgroup);
    }

    public String toString() {
        String toReturn = "";
        for (int i = 0; i < chromosome.length; i++) {
            toReturn += (chromosome[i] + " ");
        }
        return toReturn;
    }
}
