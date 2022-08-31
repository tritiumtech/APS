/**
 * @author Amos Zhou
 * Error Codes:
 * <ul>
 *     <li>APS001: 车间未设置工组</li>
 *     <li>APS002: 技能集均无两个以上工组供选择</li>
 *     <li>APS003: 不存在工组满足任务所需技能</li>
 * </ul>
 *
 */
public class ApsException extends Exception {
    public ApsException(String errMsg){
        super(errMsg);
    }
}
