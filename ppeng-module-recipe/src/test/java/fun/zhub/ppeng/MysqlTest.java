package fun.zhub.ppeng;

import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.service.RecipeService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试sql
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@SpringBootTest(classes = PPengModuleRecipeApplication.class)
public class MysqlTest {

    @Resource
    private RecipeService recipeService;

    @Test
    void testInsertRecipe() {
        Recipe recipe = new Recipe();
        recipe.setUserId(1L);
        recipe.setTypeId(1);
        recipe.setTitle("1");
        recipe.setMaterial("2");
        recipe.setContent("3");
        recipe.setMediaUrl("4");

        Long id = recipeService.createRecipe(recipe);
        System.out.println(id);
    }
}
