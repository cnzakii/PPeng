package fun.zhub.ppeng.entity.baidu;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 命中关键词信息
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-06
 **/
public class hit implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;


    /**
     * 不合规项置信度
     */
    private Float probability;

    /**
     * 违规项目所属数据集名称
     */
    private String datasetName;

    /**
     * 违规关键词
     */
    private String[] words;


}
