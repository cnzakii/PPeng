package fun.zhub.ppeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.constant.IndexConstant;
import fun.zhub.ppeng.dto.RecipeDTO;
import fun.zhub.ppeng.entity.RecipeVO;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.UserService;
import fun.zhub.ppeng.service.RecipeSearchService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜谱搜索实现类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-19
 **/
@Service
@Slf4j
public class RecipeSearchServiceimpl implements RecipeSearchService {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private UserService userService;


    /**
     * 实现 保存recipeVO
     *
     * @param recipeVO recipeVO
     */
    @Override
    public void saveRecipeVO(RecipeVO recipeVO) {
        String id = String.valueOf(recipeVO.getId());
        IndexQuery query = new IndexQueryBuilder().withObject(recipeVO).withId(id).build();
        elasticsearchTemplate.index(query, IndexCoordinates.of(IndexConstant.RECIPE_INDEX));

    }

    /**
     * 根据RecipeId删除recipeVO
     *
     * @param recipeId 菜谱id
     */
    @Override
    public void removeRecipeVoById(Long recipeId) {
        String id = String.valueOf(recipeId);
        elasticsearchTemplate.delete(id, IndexCoordinates.of("goods"));
    }


    /**
     * 实现 根据关键词搜索菜谱--聚合搜索(匹配菜谱标题和内容)
     *
     * @param keyword 关键词
     * @param page    页数
     * @param size    一页所呈现的菜谱数
     * @return list
     */
    @Override
    public List<RecipeDTO> selectRecipeByTitleAndContent(String keyword, Integer page, Integer size) {
        // ElasticSearch 合并字段
        String filed = "combined";

        // 构建查询条件
        NativeQuery nativeSearchQuery = new NativeQueryBuilder()
                .withQuery(new MatchQuery.Builder().field(filed).query(keyword).build()._toQuery())
                .withPageable(PageRequest.of(page, size))
                .build();

        // 查询ElasticSearch
        SearchHits<RecipeVO> searchHits = elasticsearchTemplate.search(nativeSearchQuery, RecipeVO.class,
                IndexCoordinates.of(IndexConstant.RECIPE_INDEX));


        // 获取recipeDTO对象
        List<RecipeDTO> list;
        list = searchHits.getSearchHits().stream().map(bean -> fillRecipeUserInfo(bean.getContent())).toList();

        return list;
    }

    /**
     * 填充菜谱的用户信息
     *
     * @param recipeVO 菜谱对象
     * @return RecipeDTO
     */
    @Override
    public RecipeDTO fillRecipeUserInfo(RecipeVO recipeVO) {
        RecipeDTO recipeDTO = BeanUtil.copyProperties(recipeVO, RecipeDTO.class);

        Long userId = recipeVO.getUserId();
        ResponseResult<User> result = userService.getUserInfo(userId);

        // 判断是否请求成功
        if (!StrUtil.equals(result.getStatus(), "200")) {
            log.error("填充菜谱的用户信息失败===》{}", result);
            return recipeDTO;
        }

        User userInfo = result.getData();
        recipeDTO.setNickName(userInfo.getNickName());
        recipeDTO.setIcon(userInfo.getIcon());
        return recipeDTO;
    }


}
