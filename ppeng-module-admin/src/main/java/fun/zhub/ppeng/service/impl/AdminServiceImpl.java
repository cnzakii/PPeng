package fun.zhub.ppeng.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.Admin;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.AdminMapper;
import fun.zhub.ppeng.service.AdminService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static fun.zhub.ppeng.constant.RedisConstants.ADMIN_INFO;
import static fun.zhub.ppeng.constant.RedisConstants.ADMIN_INFO_TTL;

/**
 * AdminService接口实现类
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 通过邮件和密码登录
     *
     * @param email    邮件
     * @param password 密码
     * @return admin
     */
    @Override
    public Admin loginByPassword(String email, String password) {
        // 根据邮箱查询用户
        Admin admin = Optional
                .ofNullable(adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getEmail, email)))
                .orElseThrow(() -> new BusinessException(ResponseStatus.FAIL, "用户不存在"));

        // 验证密码
        password = SmUtil.sm3(admin.getId() + password);
        if (!StrUtil.equals(password, admin.getPassword())) {
            throw new BusinessException(ResponseStatus.FAIL, "密码错误");
        }
        return admin;
    }

    /**
     * 实现根据id获取管理员信息
     *
     * @param id id
     * @return admin
     */
    @Override
    public Admin getAdminInfoById(Long id) {
        String key = ADMIN_INFO + id;
        Admin one;

        // 先查询redis，
        one = Optional.ofNullable(stringRedisTemplate.opsForValue().get(key))
                .map(json -> JSONUtil.toBean(json, Admin.class))
                .orElseGet(() -> {
                    // redis不存在则查询数据库，数据库仍不存在则抛出异常
                    Admin admin = Optional.ofNullable(adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId, id)))
                            .orElseThrow(() -> new BusinessException(ResponseStatus.FAIL, "用户不存在"));

                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(admin), ADMIN_INFO_TTL, TimeUnit.HOURS);
                    return admin;
                });

        return one;
    }

    /**
     * 实现 更新管理员处理任务数
     *
     * @param id      用户id
     * @param isPasss 处理的任务是否通过
     */
    @Override
    public void updateAdminTaskNum(Long id, Integer isPasss) {
        Admin admin = getAdminInfoById(id);

        admin.setProcessed(admin.getProcessed() + 1);

        // 如果处理的任务通过审核
        if (isPasss == 0) {
            admin.setPassed(admin.getPassed() + 1);
        } else {
            // 如果处理的任务不通过审核
            admin.setRejected(admin.getRejected() + 1);

        }

        boolean b = updateById(admin);
        if (BooleanUtil.isFalse(b)) {
            log.error("更新管理员({})处理任务数失败", id);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新管理员处理任务数失败");
        }

        // 更新缓存
        stringRedisTemplate.opsForValue().set(ADMIN_INFO + id, JSONUtil.toJsonStr(admin), ADMIN_INFO_TTL, TimeUnit.HOURS);

    }

}
