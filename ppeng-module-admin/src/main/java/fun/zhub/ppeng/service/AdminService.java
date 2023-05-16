package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.Admin;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface AdminService extends IService<Admin> {

    /**
     * 通过邮箱和密码登录
     *
     * @param email    邮件
     * @param password 密码
     * @return admin
     */
    Admin loginByPassword(String email, String password);


    /**
     * 实现根据id获取管理员信息
     *
     * @param id id
     * @return admin
     */
    Admin getAdminInfoById(Long id);


    /**
     * 更新用户任务处理数
     *
     * @param id      用户id
     * @param isPasss 处理的任务是否通过 0:通过，1未通过
     */
    void updateAdminTaskNum(Long id, Integer isPasss);
}
