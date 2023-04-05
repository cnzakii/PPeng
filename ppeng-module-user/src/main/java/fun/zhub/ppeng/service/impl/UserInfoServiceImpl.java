package fun.zhub.ppeng.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import static com.zhub.ppeng.constant.RedisConstants.USER_DETAIL_INFO;
import static com.zhub.ppeng.constant.RedisConstants.USER_DETAIL_INFO_TTL;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.dto.DeleteUserInfoDTO;
import fun.zhub.ppeng.dto.UpdateUserInfoDTO;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserInfoMapper;
import fun.zhub.ppeng.service.UserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
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
     * @return UserInfo
     */
    @Override
    public Boolean updateUserInfo(UpdateUserInfoDTO userInfoDTO) {
        UserInfo userInfo = BeanUtil.toBean(userInfoDTO, UserInfo.class);
        userInfo.setUpdateTime(DateUtil.toLocalDateTime(new Date(System.currentTimeMillis())));
        int i = userInfoMapper.updateById(userInfo);
        if (i == 0) {
            log.error("更新用户具体信息{}失败", userInfo.getUserId());
            throw new BusinessException(ResponseStatus.FAIL, "该用户不存在");
        }

        log.info("更新用户具体信息{}成功,更新时间：{}", userInfo.getUserId(), userInfo.getUpdateTime());
        return true;
    }
    /**
     * 实现根据用户id删除用户具体信息
     *
     * @param deleteUserInfoDTO 用户具体信息
     * @return UserInfo
     */

    @Override
    public Boolean deleteUserInfo(DeleteUserInfoDTO deleteUserInfoDTO) {
        UserInfo userInfo = BeanUtil.toBean(deleteUserInfoDTO, UserInfo.class);
        userInfo.setUpdateTime(DateUtil.toLocalDateTime(new Date(System.currentTimeMillis())));
        int i = userInfoMapper.deleteById(userInfo);
        if (i == 0) {
            log.error("删除用户具体信息{}失败", userInfo.getUserId());
            throw new BusinessException(ResponseStatus.FAIL, "用户不存在");
        }

        log.info("删除用户具体信息{}成功,更新时间：{}", userInfo.getUserId(), userInfo.getUpdateTime());
        return true;
    }
}

