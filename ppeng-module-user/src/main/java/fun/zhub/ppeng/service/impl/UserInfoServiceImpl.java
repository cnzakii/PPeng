package fun.zhub.ppeng.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import static com.zhub.ppeng.constant.RedisConstants.USER_INFO;
import static com.zhub.ppeng.constant.RedisConstants.USER_INFO_TTL;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserInfoMapper;
import fun.zhub.ppeng.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * UserInfoServiceImpl, UserInfoService interface 实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 实现根据用户id查询用户具体信息
     *
     * @param userId 用户id
     * @return UserInfo
     */
    @Override
    public UserInfo getUserInfoById(Long userId) {
        String key = USER_INFO + userId;
        // 先查询redis
        String info = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(info)) {
            stringRedisTemplate.expire(key, USER_INFO_TTL, TimeUnit.MINUTES);
            return JSONUtil.toBean(info, UserInfo.class);
        }

        // 查询数据库
        UserInfo userInfo = query().eq("user_id", userId).one();

        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userInfo), USER_INFO_TTL, TimeUnit.MINUTES);

        return userInfo;
    }

}
