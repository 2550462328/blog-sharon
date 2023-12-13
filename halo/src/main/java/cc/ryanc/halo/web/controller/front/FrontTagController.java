package cc.ryanc.halo.web.controller.front;

import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.service.TagService;
import cc.ryanc.halo.web.controller.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <pre>
 *     前台标签控制器
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/4/26
 */
@Controller
@RequestMapping(value = "/tags")
public class FrontTagController extends BaseController {

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    /**
     * 标签
     *
     * @return 模板路径/themes/{theme}/tags
     */
    @GetMapping
    public String tags() {
        return this.render("tags");
    }

    /**
     * 根据标签路径查询所有文章
     *
     * @param tagUrl 标签路径
     * @param model  model
     * @return String
     */
//    @GetMapping(value = "{tagUrl}")
//    public String tags(Model model,
//                       @PathVariable("tagUrl") String tagUrl) {
//        return this.tags(model, tagUrl, 1);
//    }

//    /**
//     * 根据标签路径查询所有文章 分页
//     *
//     * @param model  model
//     * @param tagUrl 标签路径
//     * @param page   页码
//     * @return String
//     */
//    @GetMapping(value = "{tagUrl}/page/{page}")
//    public String tags(Model model,
//                       @PathVariable("tagUrl") String tagUrl,
//                       @PathVariable("page") Integer page) {
//        Tag tag = tagService.findByTagUrl(tagUrl);
//        if(null==tag){
//            return this.renderNotFound();
//        }
//        Sort sort = new Sort(Sort.Direction.DESC, "postDate");
//        Integer size = 10;
//        if (StrUtil.isNotBlank(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()))) {
//            size = Integer.parseInt(HaloConst.OPTIONS.get(BlogPropertiesEnum.INDEX_POSTS.getProp()));
//        }
//        Pageable pageable = PageRequest.of(page - 1, size, sort);
//        Page<Post> posts = postService.findPostsByTags(tag, pageable);
//        int[] rainbow = PageUtil.rainbow(page, posts.getTotalPages(), 3);
//        model.addAttribute("is_tags",true);
//        model.addAttribute("posts", posts);
//        model.addAttribute("rainbow", rainbow);
//        model.addAttribute("tag", tag);
//        return this.render("tag");
//    }
}
