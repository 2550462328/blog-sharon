package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.dto.PageSearch;
import cc.ryanc.halo.model.enums.*;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.utils.ElasticSearchUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <pre>
 *     文章API
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/6/6
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/posts")
@Slf4j
public class ApiPostController {

    @Autowired
    private PostService postService;

    /**
     * 获取文章列表 分页
     *
     * <p>
     *     result api
     *     <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": {
     *         "content": [
     *             {
     *                 "postId": ,
     *                 "user": {},
     *                 "postTitle": "",
     *                 "postType": "",
     *                 "postContentMd": "",
     *                 "postContent": "",
     *                 "postUrl": "",
     *                 "postSummary": "",
     *                 "categories": [],
     *                 "tags": [],
     *                 "comments": [],
     *                 "postThumbnail": "",
     *                 "postDate": "",
     *                 "postUpdate": "",
     *                 "postStatus": 0,
     *                 "postViews": 0,
     *                 "allowComment": 1,
     *                 "customTpl": ""
     *             }
     *         ],
     *         "pageable": {
     *             "sort": {
     *                 "sorted": true,
     *                 "unsorted": false,
     *                 "empty": false
     *             },
     *             "offset": 0,
     *             "pageSize": 10,
     *             "pageNumber": 0,
     *             "unpaged": false,
     *             "paged": true
     *         },
     *         "last": true,
     *         "totalElements": 1,
     *         "totalPages": 1,
     *         "size": 10,
     *         "number": 0,
     *         "first": true,
     *         "numberOfElements": 1,
     *         "sort": {
     *             "sorted": true,
     *             "unsorted": false,
     *             "empty": false
     *         },
     *         "empty": false
     *     }
     * }
     *     </pre>
     * </p>
     *
     * @param page 页码
     * @return JsonResult
     */
        @GetMapping(value = "/page/{page}/{tab}")
        public JsonResult posts(@PathVariable(value = "page") Integer page,@PathVariable(value = "tab") Integer tab) {
            Sort sort;
            switch (tab){
                case 0:
                    sort = new Sort(Sort.Direction.ASC, "postId");
                    break;
                case 1:
                    sort = new Sort(Sort.Direction.DESC, "postDate");
                    break;
                case 2:
                    sort = new Sort(Sort.Direction.DESC, "postViews");
                    break;
                default:
                    sort = new Sort(Sort.Direction.DESC, "postDate");
            }
            int size = 10;
            if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
                size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
            }
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Post> posts = postService.findPostByStatus(PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc(), pageable);
            if (null == posts) {
                return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
            }
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), posts);
        }

    /**
     * 搜索文章
     * @param page 页码
     * @param keyword keyword
     */
    @GetMapping(value = "search/{page}")
    public JsonResult search(@RequestParam("keyword") String keyword, @PathVariable(value = "page") Integer page) {
        int size = 10;
        if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
            size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts;
        try {
            posts = ElasticSearchUtils.getPost(keyword, pageable.getPageNumber(),pageable.getPageSize());
        } catch (Exception e) {
            log.error("ElasticSearch 搜索文章列表 {} 失败：{}",keyword,e.getMessage(),e);
            posts = postService.searchByKeywords(keyword, pageable);
        }

        if (null == posts) {
            return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
        }
        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), new PageImpl(postService.wrapPostSummary(posts.getContent(), keyword,40, PostWrapTypeEnum.WX),pageable, posts.getTotalElements()));
    }
    /**
     * 获取单个文章信息
     *
     * <p>
     *     result json:
     *     <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": {
     *         "postId": ,
     *         "user": {},
     *         "postTitle": "",
     *         "postType": "",
     *         "postContentMd": "",
     *         "postContent": "",
     *         "postUrl": "",
     *         "postSummary": "",
     *         "categories": [],
     *         "tags": [],
     *         "comments": [],
     *         "postThumbnail": "",
     *         "postDate": "",
     *         "postUpdate": "",
     *         "postStatus": 0,
     *         "postViews": 0,
     *         "allowComment": 1,
     *         "customTpl": ""
     *     }
     * }
     *     </pre>
     * </p>
     *
     * @param postId 文章编号
     * @return JsonResult
     */
    @GetMapping(value = "/{postId}")
    public JsonResult posts(@PathVariable(value = "postId") Long postId) {
        Post post = postService.findByPostId(postId, PostTypeEnum.POST_TYPE_POST.getDesc());
        if (null != post) {

            postService.cacheViews(post.getPostId());
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), post);
        } else {
            return new JsonResult(ResponseStatusEnum.NOTFOUND.getCode(), ResponseStatusEnum.NOTFOUND.getMsg());
        }
    }
}
