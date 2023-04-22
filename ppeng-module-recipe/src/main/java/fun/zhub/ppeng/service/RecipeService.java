package fun.zhub.ppeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.zhub.ppeng.entity.Recipe;

/**
 * <p>
 * 菜谱表,包含了菜谱的标题，配料表，图片路径，以及其他属性 服务类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
public interface RecipeService extends IService<Recipe> {

    /**
     * 保存图文菜谱
     *
     * @param userId         用户id
     * @param typeId         类型id
     * @param title          标题
     * @param material       配料
     * @param content        内容
     * @param images         图片
     * @param isProfessional 是否专业
     * @return recipeId
     */
    Long saveImageRecipe(Long userId, Integer typeId, String title, String material, String content, String images, Integer isProfessional);

    /**
     * 保存视频菜谱
     *
     * @param userId         用户id
     * @param typeId         类型
     * @param title          标题
     * @param material       配料表
     * @param video          视频
     * @param isProfessional 是否专业
     * @return recipeId
     */
    Long saveVideoRecipe(Long userId, Integer typeId, String title, String material, String video, Integer isProfessional);
}
