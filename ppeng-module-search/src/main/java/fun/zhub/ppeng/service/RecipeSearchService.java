package fun.zhub.ppeng.service;

import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.entity.RecipeVO;

import java.util.List;

/**
 * 菜谱搜索 interface
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-19
 **/
public interface RecipeSearchService {

    /**
     * 将recipeVO保存进ElasticSearch
     *
     * @param recipeVO recipeVO
     */
    void saveRecipeVO(RecipeVO recipeVO);

    /**
     * 根据RecipeId删除recipeVO
     *
     * @param recipeId 菜谱id
     */
    void removeRecipeVoById(Long recipeId);


    /**
     * 根据关键词搜索菜谱--聚合搜索(匹配菜谱标题和内容)
     *
     * @param keyword 关键词
     * @param page    页数
     * @param size    一页所呈现的菜谱数
     */
    List<RecipeDTO> selectRecipeByTitleAndContent(String keyword, Integer page, Integer size);

    /**
     * 填充菜谱的用户信息
     *
     * @param recipeVO 菜谱对象
     * @return RecipeDTO
     */
    RecipeDTO toRecipeDTO(RecipeVO recipeVO);
}
