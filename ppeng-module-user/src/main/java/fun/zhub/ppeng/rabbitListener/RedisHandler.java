package fun.zhub.ppeng.rabbitListener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.dto.UserInfoDTO;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RedisConstants.*;

/**
 * <p>
 * 更新redis缓存
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-13
 **/

@Component
public class RedisHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;


    /**
     * 更新用户基本信息
     *
     * @param newUser newUser
     */
    public void updateUser(User newUser) {
        Long id = newUser.getId();
        // 根据UserId 查询用户数据
        UserInfoDTO userInfoDTO = userService.getUserInfoById(id);

        BeanUtil.copyProperties(newUser, userInfoDTO);

        stringRedisTemplate.opsForValue().set(USER_INFO + id, JSONUtil.toJsonStr(userInfoDTO), USER_INFO_TTL, TimeUnit.HOURS);

    }


    /**
     * 更新用户详细信息
     *
     * @param newUserInfo newUserInfo
     */
    public void updateUserInfo(UserInfo newUserInfo) {
        Long id = newUserInfo.getUserId();
        // 根据UserId 查询用户数据
        UserInfoDTO userInfoDTO = userService.getUserInfoById(id);

        BeanUtil.copyProperties(newUserInfo, userInfoDTO);

        stringRedisTemplate.opsForValue().set(USER_INFO + id, JSONUtil.toJsonStr(userInfoDTO), USER_INFO_TTL, TimeUnit.HOURS);

    }


    /**
     * 更新用户关注和粉丝
     *
     * @param follow follow
     */
    public void updateFan(Follow follow) {
        String key = USER_FANS_KEY + follow.getFollowId();
        // 先检查是否存在
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (members != null && !members.isEmpty()) {
            stringRedisTemplate.opsForSet().add(key, String.valueOf(follow.getUserId()));
            stringRedisTemplate.expire(key, USER_FANS_TTL, TimeUnit.MINUTES);
        }


    }


}
