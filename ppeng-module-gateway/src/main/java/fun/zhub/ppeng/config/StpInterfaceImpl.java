package fun.zhub.ppeng.config;


import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import static com.zhub.ppeng.constant.RedisConstants.ROLE_KEY;
import static com.zhub.ppeng.constant.RedisConstants.ROLE_TTL;
import static com.zhub.ppeng.constant.SaTokenConstants.SESSION_ROLE;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 自定义权限验证接口扩展
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 返回此 loginId 拥有的权限列表
     *
     * @param loginId   用户id
     * @param loginType loginType
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        return null;
    }

    /**
     * 返回此 loginId 拥有的角色列表
     *
     * @param loginId   用户id
     * @param loginType loginType
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String key = ROLE_KEY + loginId;

        String s = stringRedisTemplate.opsForValue().get(key);

        if (s != null) {
            stringRedisTemplate.expire(key, ROLE_TTL, TimeUnit.MINUTES);
        }

        List<String> list = new ArrayList<>();
        list.add(s);

        return list;
    }

}
