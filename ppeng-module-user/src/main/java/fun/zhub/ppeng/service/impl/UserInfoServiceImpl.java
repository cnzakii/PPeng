package fun.zhub.ppeng.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import static com.zhub.ppeng.constant.SaTokenConstants.SESSION_USER_INFO;
import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserInfoMapper;
import fun.zhub.ppeng.service.UserInfoService;
import org.springframework.stereotype.Service;

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


    /**
     * 实现根据用户id查询用户具体信息
     *
     * @param userId 用户id
     * @return UserInfo
     */
    @Override
    public UserInfo getUserInfoById(Long userId) {
        UserInfo userInfo;
        SaSession session = StpUtil.getSessionByLoginId(userId);

        // 先从Session中查找
        userInfo = (UserInfo) session.get(SESSION_USER_INFO);

        if (!BeanUtil.isEmpty(userInfo)) {
            return userInfo;
        }

        // 如果不存在，查询数据库
        userInfo = query().eq("user_id", userId).one();

        // 写入Session
        session.set(SESSION_USER_INFO, userInfo);

        return userInfo;
    }

}
