package fun.zhub.ppeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.mapper.RecipeMapper;
import fun.zhub.ppeng.service.RecipeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

}
