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
        skills = new HashMap<Skill, Float>();
        this.groupType = rootSkill;
        jobs = new ArrayList<Job>();
        this.calendar = calendar;
    }

    public void addSkill(Skill newSkill, float timePerPiece) {
        skills.put(newSkill, timePerPiece);
    }

    /**
     * 把一个任务加到一个工组。
     *
     * @param job
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
            System.out.println();
        } catch (ApsException e) {
            e.printStackTrace();
        }
    }

    public void setJobsByExpiryJIT(ZonedDateTime startDateTime) throws ApsException {
        Comparator<Job> comparator = new ComparatorByExpiryJIT();
        Collections.sort(jobs, comparator);

        for (Job job : jobs) {
            // 计算任务所需耗费时间
            job.calcWorkHours(this);
            // 设置启动和结束
            job.startDt = startDateTime;
            job.endDt = calendar.addWorkHours(job.startDt,job.duration/60.0f);
            startDateTime = job.endDt;
        }
    }

    public void setJobsByExpirySEQ() {
        Comparator<Job> comparator = new ComparatorByExpirySEQ();
        Collections.sort(jobs, comparator);
    }

    /**
     * 依照起始时间把一个任务加到一个工组。当force为false时，如果从该起始时间开始任务会和其他任务产生冲突，报错抛异常。如果force为
     * true，则会将该时间点之前的任务往左推，将该时间点之后的任务往右推，强行挤出让当前任务能够恰好从在当下时间开始的空间。
     *
     * @param startTime
     * @param force
     */
    public void addJob(ZonedDateTime startTime, boolean force) {

    }

    /**
     * 确定两个工组是否兼容。兼容，定义为能够互换整段基因（crossover）。比较两个工组的根节点技能是否一致。以下场景需要考虑：
     * <ul>
     *     <li>两个工组根节点一致，子节点可能不同，但因为最终都能用根节点推算效率，故无虞</li>
     *     <li>两个工组根节点不一致，但能力有交叠，比如组A是全能工组，组B仅做Bra，这种情况A的任务不一定能放在B做，故不可
     *     互换，视为不兼容。这种情况下，采用第三类操作（跨工组插入）来达到遗传算法的空间搜索</li>
     * </ul>
     *
     * @param other
     * @return
     */
    public boolean compatibleWith(WorkGroup other) {
        return this.groupType.hashCode() == other.groupType.hashCode();
    }

    public String toString() {
        String toReturn = groupType.name + ":";
        for (Skill skill : skills.keySet()) {
            toReturn = toReturn + skill.name + "(" + skills.get(skill) + ") ";
        }
        toReturn += "\n";
        for (Job job : jobs) {
            toReturn += job + "\n";
        }
        return toReturn;
    }

    public double calculateCost(PlanMode mode, ZonedDateTime startDateTime) {
        autoAdjust(mode, startDateTime);
        double cost = 0;
        for(Job job: jobs) {
            cost += calendar.workDaysBetween(job.endDt, job.deliveryDate);
        }
        return cost;
    }

    public void clearJobs() {
        jobs.clear();
    }
}
