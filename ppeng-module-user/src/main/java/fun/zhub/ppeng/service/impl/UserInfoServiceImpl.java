package fun.zhub.ppeng.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.dto.update.UpdateUserInfoDTO;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserInfoMapper;
import fun.zhub.ppeng.service.UserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.zhub.ppeng.constant.RedisConstants.USER_DETAIL_INFO;
import static com.zhub.ppeng.constant.RedisConstants.USER_DETAIL_INFO_TTL;

/**
 * <p>
 * UserInfoServiceImpl, UserInfoService interface 实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
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
        String key = USER_DETAIL_INFO + userId;
        // 先查询redis
        String info = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotEmpty(info)) {
            stringRedisTemplate.expire(key, USER_DETAIL_INFO_TTL, TimeUnit.MINUTES);
            return JSONUtil.toBean(info, UserInfo.class);
        }

        // 查询数据库
        UserInfo userInfo = query().eq("user_id", userId).one();

        // 写入redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userInfo), USER_DETAIL_INFO_TTL, TimeUnit.MINUTES);

        return userInfo;
    }


    /**
     * 实现根据用户id更新用户具体信息
     *
     * @param userInfoDTO 用户具体信息
     */
    @Override
    public void updateUserInfo(UpdateUserInfoDTO userInfoDTO) {
        UserInfo userInfo = BeanUtil.copyProperties(userInfoDTO, UserInfo.class);
        userInfo.setUpdateTime(LocalDateTime.now());

        int i = userInfoMapper.updateById(userInfo);
        if (i == 0) {
            log.error("更新用户具体信息{}失败", userInfo.getUserId());
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }

        log.info("更新用户具体信息{}成功,更新时间：{}", userInfo.getUserId(), userInfo.getUpdateTime());
    }

    /**
     * 实现更新粉丝或者关注
     *
     * @param name   字段名
     * @param userId id
     * @param type   添加或删除
     */
    @Override
    public void updateFollowOrFans(String name, Long userId, String type) {
        String sql;
        if (StrUtil.equals(type, "insert")) {
            sql = name + "=" + name + "+1";
        } else if (StrUtil.equals(type, "delete")) {
            sql = name + "=" + name + "-1";
        } else {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "参数失败");
        }


        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("user_id", userId).setSql(sql);

        boolean b = update(updateWrapper);
        if (!b) {
            log.error("更新用户具体信息{}失败", userId);
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新失败");
        }

    }

    /**
     * 实现根据用户id删除用户具体信息
     *
     * @param id id
     */

    @Override
    public void deleteUserInfoById(Long id) {
        boolean b = remove(new QueryWrapper<UserInfo>().eq("user_id", id));

        if (!b) {
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        log.info("删除用户具体信息{}成功,更新时间：{}", id, LocalDateTime.now());
    }

    /**
     * 实现根据id创建用户详细信息
     *
     * @param id id
     */
    @Override
    public void createUserInfoById(Long id) {
        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(id);
        userInfo.setCreateTime(LocalDateTime.now());

        boolean b = save(userInfo);
        if (!b) {
            log.error("用户{}详细信息创建错误", id);
            throw new BusinessException(ResponseStatus.FAIL, "初始化用户信息错误");
        }
    }
}

