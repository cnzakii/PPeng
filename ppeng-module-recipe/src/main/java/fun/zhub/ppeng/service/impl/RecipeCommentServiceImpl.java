package fun.zhub.ppeng.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeComment;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.RecipeCommentMapper;
import fun.zhub.ppeng.mapper.RecipeMapper;
import fun.zhub.ppeng.service.RecipeCommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 菜谱评论表 服务实现类
 * </p>
 *
 * @author Zaki
 * @since 2023-03-17
 */
@Service
public class RecipeCommentServiceImpl extends ServiceImpl<RecipeCommentMapper, RecipeComment> implements RecipeCommentService {
    @Resource
    private RecipeCommentMapper recipeCommentMapper;

    @Resource
    private RecipeMapper recipeMapper;

    /**
     * 实现新增评论
     *
     * @param recipeId    菜谱id
     * @param parentId    父评论id
     * @param commenterId 评论者id
     * @param content     评论内容
     */
    @Override
    public void addComment(Long recipeId, Integer parentId, Long commenterId, String content) {

        //检查菜谱评论是否为空
        if (StrUtil.isEmpty(content)) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "评论不能为空");
        }

        //检查菜谱id是否存在
        LambdaQueryWrapper<Recipe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Recipe::getId, recipeId);
        Recipe recipe = recipeMapper.selectOne(lambdaQueryWrapper);
        if (recipe == null) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱不存在");
        }

        //当父评论id不为0时，检查父评论是否存在
        if (parentId != 0) {
            LambdaQueryWrapper<RecipeComment> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(RecipeComment::getId, parentId);
            RecipeComment num = recipeCommentMapper.selectOne(lambdaQueryWrapper2);
            if (num == null) {
                throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "父评论不存在");
            }
        }

        RecipeComment recipeComment = new RecipeComment();
        recipeComment.setRecipeId(recipeId);
        recipeComment.setParentId(parentId);
        recipeComment.setCommenterId(commenterId);
        recipeComment.setContent(content);
//        recipeComment.setCreateTime(LocalDateTime.now());
        int i = recipeCommentMapper.insert(recipeComment);
        if (i != 1) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "评价失败");
        }


    }

    /**
     * 实现通过评论id删除评论
     *
     * @param id     评论id
     * @param userId 用户id
     */
    @Override
    public void deleteCommentById(Integer id, Long userId) {
        //获取该评论所评价的菜谱的id
        LambdaQueryWrapper<RecipeComment> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(RecipeComment::getId, id);
        Long recipeId = recipeCommentMapper.selectOne(lambdaQueryWrapper1).getRecipeId();

        //获取该菜谱的发布者id
        LambdaQueryWrapper<Recipe> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Recipe::getId, recipeId);
        Long userId1 = recipeMapper.selectOne(lambdaQueryWrapper2).getUserId();
        Long commenterId = recipeCommentMapper.selectOne(lambdaQueryWrapper1).getCommenterId();
        //判断是否为菜谱发布者
        if (Objects.equals(userId1, userId)) {
            int c1 = recipeCommentMapper.delete(new LambdaQueryWrapper<RecipeComment>().eq(RecipeComment::getId, id));
            if (c1 == 0) {
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "评价不存在");
            }

        }
        //否则，只有评论者才能删除评论
        else if (Objects.equals(commenterId, userId)) {
            int c2 = recipeCommentMapper.delete(new LambdaQueryWrapper<RecipeComment>().eq(RecipeComment::getId, id));
            if (c2 == 0) {
                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "评价不存在");
            }
        } else {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "只有作者或是评论者才能删除该评论");
        }
    }


    /**
     * 通过菜谱id查看评论
     *
     * @param recipeId 菜谱id
     * @return list
     */
    @Override
    public List<RecipeComment> getCommentsByRecipeId(Long recipeId) {
        //判断菜谱是否存在
        LambdaQueryWrapper<Recipe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Recipe::getId, recipeId);
        Recipe recipe = recipeMapper.selectOne(lambdaQueryWrapper);
        if (recipe == null) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_400, "菜谱不存在");
        }

        //从数据库中获取parentId为0的评论数据,创建根评论
        List<RecipeComment> rootComments = baseMapper.findByRecipeId(recipeId, 0);
        //创建子评论
        rootComments.forEach(this::setChildren);
        return rootComments;

    }

    /**
     * 创建子评论
     *
     * @param recipeComment recipeComment
     */
    @Override
    public void setChildren(RecipeComment recipeComment) {

        //从数据库中获取parentId为该评论id的评论数据
        List<RecipeComment> children = baseMapper.findByParentId(recipeComment.getId());

        //如果children不为空，则创建子评论
        if (!children.isEmpty()) {
            recipeComment.setChildren(children);
            children.forEach(this::setChildren);
        }
    }


}




