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
     * 获取所有的菜谱名字集合
     *
     * @return map
     */
    Map<String, List<String>> queryTotalNameList();

    /**
     * 获取所有菜谱对象集合
     * @return list
     */
    List<RecipeType> queryTotalRecipeTypeList();


    /**
     * 根据recipeTypeId获取相应的名字
     *
     * @param recipeTypeId recipeTypeId
     * @return name
     */
    String getNameById(Integer recipeTypeId);

}
