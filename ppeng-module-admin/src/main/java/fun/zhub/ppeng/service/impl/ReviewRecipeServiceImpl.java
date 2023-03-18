package fun.zhub.ppeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.entity.ReviewRecipe;
import fun.zhub.ppeng.mapper.ReviewRecipeMapper;
import fun.zhub.ppeng.service.ReviewRecipeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审核菜谱表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class ReviewRecipeServiceImpl extends ServiceImpl<ReviewRecipeMapper, ReviewRecipe> implements ReviewRecipeService {

}
