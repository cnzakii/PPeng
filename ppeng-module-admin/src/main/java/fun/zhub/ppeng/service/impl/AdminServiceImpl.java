package fun.zhub.ppeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.entity.Admin;
import fun.zhub.ppeng.mapper.AdminMapper;
import fun.zhub.ppeng.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
