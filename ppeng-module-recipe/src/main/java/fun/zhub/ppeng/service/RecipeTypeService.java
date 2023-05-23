package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.dto.RecipeTypeDTO;
import fun.zhub.ppeng.entity.RecipeType;

import java.util.List;

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
     * 实现获取所有菜谱对象集合
     *
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

    /**
     * 获取所有的父类集合
     *
     * @return list集合
     */
    List<RecipeTypeDTO> getParentRecipeTypeList();

    /**
     * 根据父类id获取 子类集合
     *
     * @return list集合
     */
    List<RecipeTypeDTO> getSubRecipeTypeListByParentId(Integer parentId);
}
