package com.xiaoxu.xBlog.Utils;

import com.xiaoxu.xBlog.Entities.CommentInfo;

import java.util.ArrayList;
import java.util.List;

public class CommentUtil {
    /**
     * 递归方法转换成树形结构
     * @param treeList
     * @return
     */
    public static List<CommentInfo> recursionMethod(List<CommentInfo> treeList) {
        List<CommentInfo> trees = new ArrayList<>();
        for (CommentInfo tree : treeList) {
            // 找出父节点
            if (0 == tree.getParentId()) {
                // 调用递归方法填充子节点列表
                trees.add(findChildren(tree, treeList));
            }
        }
        return trees;
    }

    /**
     * 递归方法
     * @param tree 父节点对象
     * @param treeList 所有的List
     * @return
     */
    public static CommentInfo findChildren(CommentInfo tree, List<CommentInfo> treeList) {
        for (CommentInfo node : treeList) {
            if (tree.getCommentId().equals(node.getParentId())) {
                if (tree.getChild() == null) {
                    tree.getChild();
                }
                // 递归 调用自身
                tree.getChild().add(findChildren(node, treeList));
            }
        }
        return tree;
    }
}
