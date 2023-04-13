package fun.zhub.ppeng.rabbitListener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.dto.UserDTO;
import fun.zhub.ppeng.entity.Follow;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.entity.UserInfo;
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


    /**
     * 更新用户基本信息
     *
     * @param newUser newUser
     */
    public void updateUser(User newUser) {
        Long id = newUser.getId();


        UserDTO userDTO = BeanUtil.copyProperties(newUser, UserDTO.class);

        // 邮箱脱敏
        String mobileEmail = DesensitizedUtil.email(userDTO.getEmail());
        userDTO.setEmail(mobileEmail);

        stringRedisTemplate.opsForValue().set(USER_BASE_INFO + id, JSONUtil.toJsonStr(userDTO), USER_BASE_INFO_TTL, TimeUnit.MINUTES);

    }


    /**
     * 更新用户详细信息
     *
     * @param newUserInfo newUserInfo
     */
    public void updateUserInfo(UserInfo newUserInfo) {
        Long userId = newUserInfo.getUserId();
        String key = USER_DETAIL_INFO + userId;

        // 检查是否存在
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotEmpty(s)) {
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(newUserInfo), USER_DETAIL_INFO_TTL, TimeUnit.MINUTES);
        }

    }


    /**
     * 更新用户粉丝
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
