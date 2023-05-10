package fun.zhub.ppeng.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.entity.Recipe;
import fun.zhub.ppeng.entity.RecipeComment;
import fun.zhub.ppeng.exception.BusinessException;
import fun.zhub.ppeng.mapper.RecipeCommentMapper;
import fun.zhub.ppeng.mapper.RecipeMapper;
import fun.zhub.ppeng.service.RecipeCommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
     * @param recipeId 菜谱id
     * @param parentId 父评论id
     * @param commenterId 评论者id
     * @param content 评论内容
     */
    @Override
    public void addComment(Long recipeId, Long parentId, Long commenterId, String content) {

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
        recipeComment.setCreateTime(LocalDateTime.now());
        recipeComment.setIsDeleted(0);
        int i = recipeCommentMapper.insert(recipeComment);
        if (i != 1) {
            throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "评价失败");
        }


    }

    /**
     * 实现通过评论id删除评论
     *
     * @param id 评论id
     */
    @Override
    public void deleteCommentById(Long id) {
        int c = recipeCommentMapper.deleteById(id);
        if (c == 0) {
            throw new BusinessException(ResponseStatus.FAIL, "评价不存在");
        }

    }

    /**
     * 通过菜谱id查看评论
     *
     * @param recipeId 菜谱id
     * @return
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
        List<RecipeComment> rootComments = baseMapper.findByRecipeId(recipeId, 0L);
        //创建子评论
        rootComments.forEach(this::setChildren);
        return rootComments;

    }

    /**
     * 创建子评论
     *
     * @param recipeComment
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




