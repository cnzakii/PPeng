package fun.zhub.ppeng.entity.baidu;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 不合规/疑似/命中白名单项详细信息
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-06
 **/
public class Result implements Serializable {
    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 内层错误提示码，底层服务失败才返回，成功不返
     */
    private Integer error_code;

    /**
     * 内层错误提示信息，底层服务失败才返回，成功不返回
     */
    private String error_msg;

    /**
     * 审核主类型，11：百度官方违禁词库、12：文本反作弊、13:自定义文本黑名单、14:自定义文本白名单
     */
    private  Integer type;

    /**
     *审核子类型，此字段需参照type主类型字段决定其含义：
     * 当type=11时subType取值含义：
     * 0:百度官方默认违禁词库
     * 当type=12时subType取值含义：
     * 0:低质灌水、1:违禁、2:文本色情、3:敏感信息、4:恶意推广、5:低俗辱骂
     * 当type=13时subType取值含义：
     * 0:自定义文本黑名单
     * 当type=14时subType取值含义：
     * 0:自定义文本白名单
     */
    private Integer subType;

    /**
     * 不合规项描述信息
     */
    private String msg;

    /**
     *  结论
     */
    private String conclusion;


    /**
     * 命中关键词信息
     */
    private hit[] hits;
}
