package entities;

import algo.PlanMode;
import exceptions.ApsException;
import utils.ComparatorByExpiryJIT;
import utils.ComparatorByExpirySEQ;
import utils.WorkCalendar;

import java.time.ZonedDateTime;
import java.util.*;

public class WorkGroup {
    public String id;
    public Skill groupType;
    public HashMap<Skill, Float> skills; //in minutes
    public WorkCalendar calendar;
    public List<Job> jobs;

    public WorkGroup(String id, Skill rootSkill, WorkCalendar calendar) {
        this.id = id;
        skills = new HashMap<>();
        this.groupType = rootSkill;
        jobs = new ArrayList<>();
        this.calendar = calendar;
    }

    public void addSkill(Skill newSkill, float timePerPiece) {
        skills.put(newSkill, timePerPiece);
    }

    /**
     * 把一个任务加到一个工组。
     *
     * @param job 被添加的任务
     */
    public void addJob(Job job) {
        this.jobs.add(job);
    }

    public void autoAdjust(PlanMode mode, ZonedDateTime startDateTime) {
        try {
            switch (mode) {
                case ExpiryJIT:
                    setJobsByExpiryJIT(startDateTime);
                    break;
                case ExpirySEQ:
                    setJobsByExpirySEQ();
                    break;
                default:
                    break;
            }
        } catch (ApsException e) {
            e.printStackTrace();
        }
    }

    public void setJobsByExpiryJIT(ZonedDateTime startDateTime) throws ApsException {
        Comparator<Job> comparator = new ComparatorByExpiryJIT();
        jobs.sort(comparator);

        for (Job job : jobs) {
            // 计算任务所需耗费时间
            job.calcWorkHours(this);
            // 设置启动和结束
            job.startDt = startDateTime;
            job.endDt = calendar.addWorkHours(job.startDt, job.duration / 60.0f);
            startDateTime = job.endDt;
        }
    }

    public void setJobsByExpirySEQ() {
        Comparator<Job> comparator = new ComparatorByExpirySEQ();
        jobs.sort(comparator);
    }

    /**
     * 确定两个工组是否兼容。兼容，定义为能够互换整段基因（crossover）。比较两个工组的根节点技能是否一致。以下场景需要考虑：
     * <ul>
     *     <li>两个工组根节点一致，子节点可能不同，但因为最终都能用根节点推算效率，故无虞</li>
     *     <li>两个工组根节点不一致，但能力有交叠，比如组A是全能工组，组B仅做Bra，这种情况A的任务不一定能放在B做，故不可
     *     互换，视为不兼容。这种情况下，采用第三类操作（跨工组插入）来达到遗传算法的空间搜索</li>
     * </ul>
     *
     * @param other 被比较的另一个工组
     * @return 是则两工组兼容，否则不兼容
     */
    public boolean compatibleWith(WorkGroup other) {
        return this.groupType.hashCode() == other.groupType.hashCode();
    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder(groupType.name + ":");
        for (Skill skill : skills.keySet()) {
            toReturn.append(skill.name).append("(").append(skills.get(skill)).append(") ");
        }
        toReturn.append("\n");
        for (Job job : jobs) {
            toReturn.append(job).append("\n");
        }
        return toReturn.toString();
    }

    /**
     * The higher the cost, the lower the fitness
     *
     * @param mode 工组内排任务的计划模式
     * @param startDateTime 排计划的起始时间
     * @return 该工组内的综合成本（包括延迟、翻单等）
     */
    public double calculateCost(PlanMode mode, ZonedDateTime startDateTime) {
        autoAdjust(mode, startDateTime);
        double cost = 0;
        for (Job job : jobs) {
            float lateDays = calendar.workDaysBetween(job.deliveryDt, job.endDt);
            cost += lateDays > 0 ? lateDays : 0;
        }
        return cost;
    }

    public void clearJobs() {
        jobs.clear();
    }
}
