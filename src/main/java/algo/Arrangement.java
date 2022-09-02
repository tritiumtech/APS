package algo;

import entities.Job;
import entities.Skill;
import entities.WorkGroup;
import exceptions.ApsException;

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

public class Arrangement implements Chromosome, Cloneable {
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

    /**
     * 进行变异操作。同一条染色体内兼容工组互换基因。仅和Environment进行数据交互，不依赖实体类WorkGroup中的数据结构，
     * 以免增加计算成本
     *
     * @return
     */
    @Override
    public Arrangement mutate() {
        double modeProb = 0.6; //The probability of selecting mode 1
        try {
            Arrangement mutated = this.clone();
//            System.out.println("\n----------------\n");
//            System.out.println("    变异前：" + " " + mutated);
            // 1. 随机选取两个兼容工组
            List<WorkGroup> workGroups = env.randomPair();
            int groupOneIndex = env.workGroups.indexOf(workGroups.get(0));
            int groupTwoIndex = env.workGroups.indexOf(workGroups.get(1));
//            System.out.println(">>> 工组选取：" + groupOneIndex + " 和 " + groupTwoIndex);

            // 2. 随机选取起始点（不考虑顺序，起始点以WorkGroup类中jobs列表的索引为据）
            List<Job> groupOneJobs = parseGroupJobs(groupOneIndex);
            List<Job> groupTwoJobs = parseGroupJobs(groupTwoIndex);
            int groupOneSlicePoint = (int) (groupOneJobs.size() * Math.random());
            int groupTwoSlicePoint = (int) (groupTwoJobs.size() * Math.random());
//            System.out.println(">>> 切点选取：" + groupOneSlicePoint + " 和 " + groupTwoSlicePoint);

            // 3. 互换
            double mode = Math.random() <= modeProb ? 0 : 1;
            if (mode == 0) {
                // 3.1 模式一 整段互换 从起始点到工组结尾互相交换
                for (int i = groupOneSlicePoint; i < groupOneJobs.size(); i++) {
                    Job job = groupOneJobs.get(i);
                    int jobIndex = env.jobs.indexOf(job);
                    mutated.chromosome[jobIndex] = groupTwoIndex;
//                    System.out.println("        模式一: 组" + groupOneIndex + "->组" + groupTwoIndex + " " + mutated);
//                    System.out.println("        对照: 本染色体" + this);
                }
                for (int i = groupTwoSlicePoint; i < groupTwoJobs.size(); i++) {
                    Job job = groupTwoJobs.get(i);
                    int jobIndex = env.jobs.indexOf(job);
                    mutated.chromosome[jobIndex] = groupOneIndex;
//                    System.out.println("        模式一: 组" + groupTwoIndex + "->组" + groupOneIndex + " " + mutated);
                }
//                System.out.println("    完成模式一变异: " + mutated);
            } else {
                // 3.2 模式二 单点插入 从1组的起始点取一个job放入2组
                Job job = groupOneJobs.get(groupOneSlicePoint);
                int jobIndex = env.jobs.indexOf(job);
                mutated.chromosome[jobIndex] = groupTwoIndex;
//                System.out.println("        模式二: 任务" + jobIndex + " " + mutated);
//                System.out.println("    完成模式二变异: " + mutated);
            }
//            System.out.println("    完成变异：" + " " + mutated);
            return mutated;
        } catch (ApsException e) {
            // Print error message and skip this round
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Job> parseGroupJobs(int groupOneIndex) throws ApsException {
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i] == groupOneIndex) {
                jobs.add(env.jobs.get(i));
            }
        }
        if(jobs.size() == 0) {
            throw new ApsException("APS004 从染色体中未解析到该工组的任务");
        }
        return jobs;
    }

    public Arrangement clone() {
        Arrangement newInstance = new Arrangement(env);
        newInstance.chromosome = chromosome.clone();
        return newInstance;
    }

    @Override
    public double cost(Environment env) {
        double cost = 0;
        for (WorkGroup workgroup : env.workGroups) {
            arrangeGroup(workgroup);
            cost += workgroup.calculateCost(PlanMode.ExpiryJIT, env.startDateTime);
            System.out.println(workgroup);
        }
        return cost;
    }

    private void arrangeGroup(WorkGroup workgroup) {
        workgroup.clearJobs();
        int groupIndex = env.workGroups.indexOf(workgroup);
        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i] == groupIndex) {
                workgroup.addJob(env.jobs.get(i));
            }
        }
    }

    public String toString() {
        String toReturn = "";
        for (int i = 0; i < chromosome.length; i++) {
            toReturn += (chromosome[i] + " ");
        }
        return toReturn;
    }
}
