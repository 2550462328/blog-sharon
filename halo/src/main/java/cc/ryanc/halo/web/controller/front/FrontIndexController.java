package cc.ryanc.halo.web.controller.front;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.model.enums.BlogPropertiesEnum;
import cc.ryanc.halo.model.enums.PostWrapTypeEnum;
import cc.ryanc.halo.service.CategoryService;
import cc.ryanc.halo.service.GalleryService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.web.controller.core.BaseController;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     前台首页控制器
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/4/26
 */
@Slf4j
@Controller
@RequestMapping(value = {"/", "index"})
public class FrontIndexController extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GalleryService galleryService;

    /**
     * 请求首页
     *
     * @param model model
     * @return 模板路径
     */
    @GetMapping
    public String index(Model model) {
        List<Category> categoryList = categoryService.findChildCatesById(0);
        List<Map<String, Object>> cateList = new ArrayList<>();

        model.addAttribute("is_post", false);
        model.addAttribute("categoryList", categoryService.convertToTreeMap(categoryList));
        String galleries = Arrays.toString(galleryService.findAllGalleryUrl().toArray(new String[0]));
        model.addAttribute("galleries", galleries);
        return this.render("index");
    }

    /**
     * 搜索文章
     *
     * @param keyword keyword
     * @param model   model
     * @return 模板路径/themes/{theme}/index
     */
    @GetMapping(value = "search/{page}")
    public String search(@PathVariable(name = "page") Integer page, @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "cateId", required = false) Long cateId, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "postDate");
        //默认显示10条
        int size = 10;
        //尝试加载设置选项，用于设置显示条数
        if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
            size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
        }
        //所有文章数据，分页
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> posts = postService.findPostByCondition(pageable, keyword, cateId);
        if (null == posts) {
            return this.renderNotFound();
        }

        int[] rainbow = PageUtil.rainbow(page, posts.getTotalPages(), 3);
        model.addAttribute("is_index", true);
        model.addAttribute("posts", posts);
        model.addAttribute("rainbow", rainbow);
        model.addAttribute("keyword", keyword);
        model.addAttribute("cateId", cateId);

        //分类查询
        if (cateId != null) {
            model.addAttribute("postlist", postService.sortPostByCate(posts));
            //关键字查询
        } else if (keyword != null) {
            if (!posts.getContent().isEmpty()) {
                model.addAttribute("postlist", postService.wrapPostSummary(posts.getContent(), keyword, 60, PostWrapTypeEnum.H5));
            } else {
                model.addAttribute("postlist", posts.getContent());
            }
            //默认
        } else {
            model.addAttribute("postlist", posts.getContent());
        }
        return this.render("module/post_entry");
    }
}
