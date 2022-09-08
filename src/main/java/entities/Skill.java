package entities;

/**
 * 技能类。注意重载后的hashcode()和equals()方法。为方便比较和取技能分数，Skill类的hashcode()区分技能名称，而
 * equals()方法允许名称不同但存在字母关系的技能被视作匹配技能。
 */
public class Skill implements Comparable {
    public String name;
    public int level;

    public Skill parent;

    public Skill(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Skill getAncestor() {
        Skill ancestor = this;
        while (ancestor.parent != null) {
            ancestor = ancestor.parent;
        }
        return ancestor;
    }

    public void setParent(Skill parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    /**
     * 当other技能和本技能相同，或者是本技能的母系技能时，视作compatible，因为此时可以得到可靠的效率值
     */
    public boolean compatibleWith(Skill other) {
        if (this.name.equals(other.name)) {
            return true;
        } else {
            Skill thisSkill = this;
            while (thisSkill.parent != null) {
                thisSkill = thisSkill.parent;
                if (thisSkill.name.equals(other.name)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 两个技能当且仅当技能名一致时有一样的hashcode（为避免使用map之类数据结构时性能出问题），但equals方法中允许跨层级子母关系视为相等
     *
     * @return
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.level, ((Skill) o).level);
    }
}
