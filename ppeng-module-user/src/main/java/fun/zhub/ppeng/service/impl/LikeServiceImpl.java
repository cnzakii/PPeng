package fun.zhub.ppeng.service.impl;

import fun.zhub.ppeng.entity.Like;
import fun.zhub.ppeng.mapper.LikeMapper;
import fun.zhub.ppeng.service.LikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 点赞菜谱表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

}
