package utils;

import com.base.sbc.aps.entity.ExecuteObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 技能效率比较器
 */
public class ComparatorBySkillEfficiency implements Comparator<ExecuteObject>, Serializable {

    @Override
    public int compare(ExecuteObject object1, ExecuteObject object2) {
        BigDecimal o1skillEfficiency = object1.getExecuteObjectSkillMap().get(object1.getCurSkillId()) != null ?
                object1.getExecuteObjectSkillMap().get(object1.getCurSkillId()).getSkillEfficiency() : BigDecimal.ZERO;
        BigDecimal o2skillEfficiency = object2.getExecuteObjectSkillMap().get(object2.getCurSkillId()) != null ?
                object2.getExecuteObjectSkillMap().get(object2.getCurSkillId()).getSkillEfficiency() : BigDecimal.ZERO;
        if (o2skillEfficiency == null && o1skillEfficiency == null) {
            return 0;
        }
        if (o2skillEfficiency == null) {
            return 1;
        }
        if (o1skillEfficiency == null) {
            return -1;
        }
        return o2skillEfficiency.compareTo(o1skillEfficiency);
    }
}
