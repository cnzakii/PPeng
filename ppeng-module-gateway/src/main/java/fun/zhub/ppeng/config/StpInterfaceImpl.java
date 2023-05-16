package fun.zhub.ppeng.config;


import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.*;
import static fun.zhub.ppeng.constant.RoleConstants.ROLE_ADMIN;
import static fun.zhub.ppeng.constant.RoleConstants.ROLE_USER;


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


        // 从redis中获取角色信息
        Set<String> role = stringRedisTemplate.opsForSet().members(USER_ROLE + loginId);
        if (CollUtil.isEmpty(role)) {
            return null;
        }
        // 更新角色信息缓存
        stringRedisTemplate.expire(USER_ROLE + loginId, ROLE_USER_TTL, TimeUnit.HOURS);

        if (role.contains(ROLE_USER)) {
            // 刷新用户缓存
            stringRedisTemplate.expire(USER_INFO + loginId, USER_INFO_TTL, TimeUnit.HOURS);
        }

        if (role.contains(ROLE_ADMIN)) {
            // 刷新管理员缓存
            stringRedisTemplate.expire(ADMIN_INFO + loginId, ADMIN_INFO_TTL, TimeUnit.HOURS);
        }


        return role.stream().toList();
    }

}
