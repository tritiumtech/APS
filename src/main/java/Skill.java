/**
 * 技能类。注意重载后的hashcode()和equals()方法。为方便比较和取技能分数，Skill类的hashcode()区分技能名称，而
 * equals()方法允许名称不同但存在字母关系的技能被视作匹配技能。
 */
public class Skill {
    public String name;
    public int level;

    public Skill parent;

    public Skill(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public void setParent(Skill parent) {
        this.parent = parent;
    }

    /**
     * 两种情况下视为相等：
     * <ul>
     *     <li>技能名完全一致</li>
     *     <li>技能名不一致但存在子母技能关系</li>
     * </ul>
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        Skill ref = (Skill) o;
        boolean exactMatch = this.name.equals(ref.name);
        boolean crossLevelMatch = false;
        if(this.parent != null && this.parent.name.equals(ref.name)) {
            crossLevelMatch = true;
        } else if(ref.parent != null && this.name.equals(ref.parent.name)) {
            crossLevelMatch = true;
        }
        return exactMatch || crossLevelMatch;
    }

    /**
     * 两个技能当且仅当技能名一致时有一样的hashcode（为避免使用map之类数据结构时性能出问题），但equals方法中允许跨层级字母关系视为相等
     * @return
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
