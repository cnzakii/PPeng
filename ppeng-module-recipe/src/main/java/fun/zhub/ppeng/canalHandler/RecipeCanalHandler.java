package fun.zhub.ppeng.canalHandler;

import fun.zhub.ppeng.canal.AbstractCanalHandler;
import fun.zhub.ppeng.canal.CanalTable;
import fun.zhub.ppeng.entity.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * recipe表中数据发生变化 的处理类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-08
 **/
@CanalTable("t_recipe")
@Component
@Slf4j
public class RecipeCanalHandler extends AbstractCanalHandler<Recipe> {


    /**
     * 处理新增的数据
     *
     * @param data 插入的数据
     */
    @Override
    public void insert(Recipe data) {

    }

    /**
     * 处理更新的数据
     *
     * @param oldData 旧数据
     * @param newData 新数据
     */
    @Override
    public void update(Recipe oldData, Recipe newData) {

    }

    /**
     * 处理删除的数据
     *
     * @param data 删除操作
     */
    @Override
    public void delete(Recipe data) {

    }
}
