package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.RecipeType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜谱类型表 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface RecipeTypeService extends IService<RecipeType> {

    /**
     * 获取所有的菜谱集合
     *
     * @return var
     */
    Map<String, List<String>> queryTotalRecipeTypeList();
}
