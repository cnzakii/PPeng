package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.Follow;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * FollowService interface
 * 包括用户关注的所有用户和用户的所有粉丝
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface FollowService extends IService<Follow> {

    /**
     * 添加关注
     *
     * @param userId   用户id
     * @param followId 关注id
     */
    void addFollow(Long userId, Long followId);


    /**
     * 根据id查询用户的所有关注
     *
     * @return 关注列表
     */
    Set<String> queryFollowById(Long id);


    /**
     * 根据id查询用户的所有粉丝
     *
     * @return 粉丝列表
     */
    Set<String> queryFansById(Long id);

    /**
     * 删除关注
     *
     * @param userId   用户id
     * @param followId 关注id
     */
    void deleteFollow(Long userId, Long followId);


    /**
     * 根据id查询用户粉丝或者关注
     *
     * @param prefixKey redis Key前缀
     * @param TTL       redis Key的过期时间
     * @param timeUnit  时间单位
     * @param name      数据库中的id字段名
     * @param id        id
     * @return 关注列表或者粉丝列表
     */
    Set<String> queryById(String prefixKey, Long TTL, TimeUnit timeUnit, String name, Long id);


    /**
     * 判断该用户是否关注过
     *
     * @param userId   用户id
     * @param followId 关注id
     * @return true or false
     */
    Boolean isFollow(Long userId, Long followId);


}
