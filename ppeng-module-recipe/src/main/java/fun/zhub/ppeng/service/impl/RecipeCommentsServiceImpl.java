package fun.zhub.ppeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.entity.RecipeComments;
import fun.zhub.ppeng.mapper.RecipeCommentsMapper;
import fun.zhub.ppeng.service.RecipeCommentsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜谱评论表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class RecipeCommentsServiceImpl extends ServiceImpl<RecipeCommentsMapper, RecipeComments> implements RecipeCommentsService {

}
