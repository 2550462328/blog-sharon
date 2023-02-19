package cc.ryanc.halo.web.controller.api;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.ResponseStatusEnum;
import cc.ryanc.halo.service.CategoryService;
import cn.hutool.json.JSONObject;
import cc.ryanc.halo.service.PostService;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 *     文章分类API
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/6/6
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/categories")
public class ApiCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostService postService;


    /**
     * 获取所有分类
     *
     * <p>
     * result json:
     * <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": [
     *         {
     *             "cateId": "",
     *             "cateName": "",
     *             "cateUrl": "",
     *             "cateDesc": ""
     *             "count":"文章总数"
     *         }
     *     ]
     * }
     *     </pre>
     * </p>
     *
     * @return JsonResult
     */
    @GetMapping("/list/{pId}/{pageNo}")
    public JsonResult categories(@PathVariable("pId") Long pId,@PathVariable("pageNo") Integer pageNo) {
        if (pId == null) {
            pId = 0L;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "post_date");
        //默认显示10条
        int size = 10;
        //尝试加载设置选项，用于设置显示条数
        if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
            size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
        }
        //所有文章数据，分页
        Pageable pageable = PageRequest.of(pageNo -1, size, sort);

        List<JSONObject> list = new ArrayList<>();
        List<Category> categories = categoryService.findAllByCatePid(pId);
        for (Category i : categories) {
            List<Long> subCateIds = categoryService.findCateIdByCatePid(i.getCateId());
            JSONObject info = new JSONObject();
            info.put("cateId", i.getCateId());
            info.put("count", categoryService.countSubPostsByCateIds(subCateIds));
            info.put("cateName", i.getCateName());
            info.put("hasSub", i.getHasChild());
            info.put("cateUrl", i.getCateUrl());
            info.put("cateIcon", i.getCateIcon());
            info.put("posts", i.getPosts());
            info.put("categories", i.getCateDesc());
            list.add(info);
        }
        Page<Map<String,Object>> posts = postService.findPostsByCateId(pageable, pId);
        JSONObject result = new JSONObject().put("posts", posts).put("result", list);
        return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), result);
    }

    /**
     * 获取单个分类的信息
     *
     * <p>
     * result json:
     * <pre>
     * {
     *     "code": 200,
     *     "msg": "OK",
     *     "result": {
     *         "cateId": "",
     *         "cateName": "",
     *         "cateUrl": "",
     *         "cateDesc": ""
     *     }
     * }
     *     </pre>
     * </p>
     *
     * @param cateUrl 分类路径
     * @return JsonResult
     */
    @GetMapping(value = "/{cateUrl}")
    public JsonResult categories(@PathVariable("cateUrl") String cateUrl) {
        Category category = categoryService.findByCateUrl(cateUrl);
        if (null != category) {
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), category);
        } else {
            return new JsonResult(ResponseStatusEnum.EMPTY.getCode(), ResponseStatusEnum.EMPTY.getMsg());
        }
    }

    /**
     * 根据分类目录查询所有文章 分页
     *
     * @param cateUrl 分类目录路径
     * @param page    页码
     * @return String
     */
    @GetMapping("{cateUrl}/page/{page}")
    public JsonResult categories(Model model,
                                 @PathVariable("cateUrl") String cateUrl,
                                 @PathVariable("page") Integer page) {
        Category category = categoryService.findByCateUrl(cateUrl);
        if (null != category) {
            Sort sort = new Sort(Sort.Direction.DESC, "postDate");
            Integer size = 10;
            if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
                size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
            }
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Post> posts = postService.findPostByCategories(category, pageable);
            return new JsonResult(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMsg(), posts);
        } else {
            return new JsonResult(ResponseStatusEnum.NOTFOUND.getCode(), ResponseStatusEnum.NOTFOUND.getMsg());
        }
    }
}
