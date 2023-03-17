package fun.zhub.ppeng.service.impl;

import fun.zhub.ppeng.entity.RecipeType;
import fun.zhub.ppeng.mapper.RecipeTypeMapper;
import fun.zhub.ppeng.service.RecipeTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜谱类型表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class RecipeTypeServiceImpl extends ServiceImpl<RecipeTypeMapper, RecipeType> implements RecipeTypeService {

}
