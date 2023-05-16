package fun.zhub.ppeng.constant;

import java.time.ZoneOffset;

/**
 * <p>
 * 系统常量
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
public interface SystemConstants {

    /**
     * 北京时间
     */
    ZoneOffset CST = ZoneOffset.ofHours(8);

    /**
     * 终端Id
     */
    Integer WORK_ID = 1;

    /**
     * 数据中心Id
     */
    Integer DATACENTER_ID = 1;


    /**
     * 文件存储根路径  /root/ppeng
     */
    String FILE_ROOT_PATH = "/Users/zaki/Desktop";

    /**
     * url
     */
    String PPENG_URL = "https://ppeng.zhub.fun/";


    /**
     * 资源url
     */
    String PPENG_RESOURCE_URL = PPENG_URL + "resource";

    /**
     * 菜谱上述url
     */
    String PPENG_RECIPE_APPEAL_URL = PPENG_URL + "recipe/censor/appeal";

}
