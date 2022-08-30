import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

public class WorkGroup {
    public Skill groupType;
    public HashMap<Skill, Float> skills;
    public WorkCalendar calendar;
    public List<Job> jobs;

    public WorkGroup(Skill rootSkill) {
        skills = new HashMap<Skill, Float>();
        this.groupType = rootSkill;
    }

    public void setCalendar(WorkCalendar calendar) {
        this.calendar = calendar;
    }

    public void addSkill(Skill newSkill, float timePerPiece) {
        skills.put(newSkill, timePerPiece);
    }

    /**
     * 依照任务序号把一个任务加到一个工组。考虑以下场景：
     * <ul>
     *     <li>正常场景：i-1号任务的结束和i+1号任务的开始之间有充裕时间，此时用alignment参数确定左对齐还是右对齐</li>
     *     <li>失败场景：i-1号任务的结束和i+1号任务的开始之间无法容下这个任务，抛异常</li>
     * </ul>
     * 此方法不考虑交期，只考虑是否空余时间段是否够插入。
     *
     * @param job
     * @param index
     * @param alignment 1表示左对齐（对应顺排），2表示右对齐（对应倒排），3表示居中（前后留有余地，较安全）
     */
    public void addJob(Job job, int index, int alignment) {
        Job predecessor = (index == 0) ? null : jobs.get(index - 1);
        Job successor = (index == jobs.size() - 1) ? null : jobs.get(index + 1);
        boolean leftClear = false, rightClear = false;
        if (predecessor == null) {
            leftClear = true;
        }
        if (successor == null) {
            rightClear = true;
        }
        // 左侧无则往左无限延展；右侧无则往右侧无限延展；又或者中间够空间；三种情况均可插入
        if (leftClear || rightClear || calendar.workTimeBetween(predecessor.endDt, successor.startDt) >= job.duration) {
            jobs.add(index, job);
        }

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
        return toReturn;
    }
}
