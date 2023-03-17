package fun.zhub.ppeng.service.impl;

import fun.zhub.ppeng.entity.UserInfo;
import fun.zhub.ppeng.mapper.UserInfoMapper;
import fun.zhub.ppeng.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表，包含更加详细的用户信息：所在城市，自我介绍，性别，生日 等 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
