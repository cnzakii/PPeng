package fun.zhub.ppeng.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhub.ppeng.common.ResponseStatus;
import com.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserInfoMapper;
import fun.zhub.ppeng.service.UserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;



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




    @Override
    public void updateUserInfo(Long id, String address,String introduce, Byte gender, LocalDate birthday) {
        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("user_id", id));


        boolean b = false;

        if(StrUtil.isNotEmpty(address)){
            b = true;
         userInfo.setAddress(address);
        }


        if(StrUtil.isNotEmpty(introduce)){
            b = true;
            userInfo.setIntroduce(introduce);
        }

        if(gender!=null){
            b = true;
            userInfo.setGender(gender);
        }

        if(birthday!=null){
            b = true;
            userInfo.setBirthday(birthday);
        }

        if(b){
            userInfo.setUpdateTime(LocalDateTime.now());
            int i = userInfoMapper.updateById(userInfo);
            if (i == 0) {
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "更新失败");
            }
            log.info("更新用户具体信息{}成功,更新时间：{}", userInfo.getUserId(), userInfo.getUpdateTime());
        }

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
     * @return userInfo
     */
    @Override
    public UserInfo createUserInfoById(Long id) {
        UserInfo userInfo = new UserInfo();

        userInfo.setUserId(id);
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setFans(0);
        userInfo.setFollows(0);
        userInfo.setGender((byte) 0);


        boolean b = save(userInfo);
        if (!b) {
            log.error("用户{}详细信息创建错误", id);
            throw new BusinessException(ResponseStatus.FAIL, "初始化用户信息错误");
        }
        log.info("用户{}详细信息创建成功", id);

        return userInfo;
    }
}

