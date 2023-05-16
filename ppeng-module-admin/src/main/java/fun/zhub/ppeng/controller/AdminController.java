package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.dto.AdminDTO;
import fun.zhub.ppeng.dto.login.LoginFormDTO;
import fun.zhub.ppeng.entity.Admin;
import fun.zhub.ppeng.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static fun.zhub.ppeng.constant.RedisConstants.USER_ROLE;

/**
 * admin接口
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/hidden/admin")
public class AdminController {

    @Resource
    private RSA rsa;

    @Resource
    private AdminService adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * GET方法获取公钥，用于密码登录
     *
     * @return RSA公钥
     */
    @GetMapping("/rsa")
    public ResponseResult<String> getPublicKey() {
        return ResponseResult.success(rsa.getPublicKeyBase64());
    }

    /**
     * 通过邮箱和密码登录
     *
     * @param loginFormDTO 用户密码登录结构体
     * @return p-token
     */
    @PostMapping("/login/by/password")
    public ResponseResult<String> loginByPassword(@RequestBody @Validated LoginFormDTO loginFormDTO) {
        String email = loginFormDTO.getEmail();
        String password;
        try {
            password = rsa.decryptStr(loginFormDTO.getPassword(), KeyType.PrivateKey);
        } catch (Exception e) {
            return ResponseResult.fail("密码未加密");
        }

        Admin admin = adminService.loginByPassword(email, password);
        Long adminId = admin.getId();


        // 登录
        StpUtil.login(adminId);

        // 将角色信息写入redis
        stringRedisTemplate.opsForSet().add(USER_ROLE + adminId, admin.getRole());

        // 获取token
        String token = StpUtil.getTokenValueByLoginId(adminId);

        return ResponseResult.success(token);
    }

    /**
     * 管理员登出
     *
     * @return success
     */
    @PostMapping("/logout")
    public ResponseResult<String> logout() {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        StpUtil.logout(id);

        // 移除角色信息
        stringRedisTemplate.delete(USER_ROLE + id);
        return ResponseResult.success();
    }

    /**
     * 获取当前用户信息
     *
     * @return adminInfo
     */
    @PostMapping("/current")
    public ResponseResult<AdminDTO> getCurrentInfo() {
        Long id = Long.valueOf((String) StpUtil.getLoginId());
        Admin admin = adminService.getAdminInfoById(id);
        AdminDTO adminDTO = BeanUtil.copyProperties(admin, AdminDTO.class);
        // email脱敏
        adminDTO.setEmail(DesensitizedUtil.email(adminDTO.getEmail()));
        return ResponseResult.success(adminDTO);
    }


}
