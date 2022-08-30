import java.util.List;

/**
 * @author Amos Zhou
 * <p>排产方案的染色体类。编码方案是一维数组，长度为款式数*2，每个款式占两位，下标2i的元素记录工组号，下标2i+1的元素记录工组中序号。通过
 * crossover和mutation可达到以下效果：</p>
 * <ul>
 *     <li>crossover：两个排产方案直接互换基因。出现序号重复、序号断码的状况，需要重新平滑化序号</li>
 *     <li>mutation操作1：相兼容的工组互换整段基因。加工组掩码，交换的开始和结束点都只在2i位置，切断后整段和别的工组互换，并更新序号</li>
 *     <li>mutation操作2：同工组互换加工顺序。对工组掩码下的2i+1位置进行数值对调</li>
 *     <li>mutation操作3：跨工组插入（不对调）。对工组掩码下的来源和目标工组通过序号调整进行操作</li>
 * </ul>
 * 在所有操作中，均需考量品类能力、交期等约束，减少计算量。
 */
public class Arrangement implements Chromosome {
    public int chromosome[];
    public Environment env;

    public Arrangement(int orderCount, Environment env) {
        chromosome = new int[2 * orderCount];
        this.env = env;
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
        int i = (int) Math.random() * chromosome.length;
        int j = (int) Math.random() * chromosome.length;
        // 确保不会截了个寂寞
        while (i == j) {
            j = (int) Math.random() * chromosome.length;
        }

        // 2 互换
        int[] staging = new int[Math.abs(i - j) + 1];

        // 3 平滑序号

        return null;
    }

    @Override
    public List<Arrangement> mutate() {
        return null;
    }

    @Override
    public double cost() {
        return 0;
    }
}
