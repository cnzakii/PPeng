package fun.zhub.ppeng.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.FollowService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 用户关注表 前端控制器
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private FollowService followService;


    /**
     * 添加用户关注
     *
     * @param followId 关注id
     * @return success
     */
    @PostMapping("/add/{followId}")
    public ResponseResult<String> addMyFollow(@PathVariable("followId") Long followId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        followService.addFollow(userId, followId);

        return ResponseResult.success();
    }


    /**
     * 列出关注列表
     *
     * @return set
     */
    @GetMapping("/list/follow")
    public ResponseResult<Set<String>> listMyFollow() {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        Set<String> set = followService.queryFollowById(userId);

        return ResponseResult.success(set);
    }

    /**
     * 列出粉丝列表
     *
     * @return set
     */
    @GetMapping("/list/fans")
    public ResponseResult<Set<String>> listMyFans() {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        Set<String> set = followService.queryFansById(userId);

        return ResponseResult.success(set);
    }


    /**
     * 查询是否关注过
     *
     * @param followId 关注id
     * @return true or false
     */
    @PostMapping("/{followId}")
    public ResponseResult<Boolean> isMyFollow(@PathVariable("followId") Long followId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        Boolean isFollow = followService.isFollow(userId, followId);

        return ResponseResult.success(isFollow);
    }


    /**
     * 取消关注
     *
     * @param followId 关注id
     * @return true or false
     */
    @DeleteMapping("/delete/{followId}")
    public ResponseResult<String> deleteMyFollow(@PathVariable("followId") Long followId) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());

        followService.deleteFollow(userId, followId);

        return ResponseResult.success();
    }


}
