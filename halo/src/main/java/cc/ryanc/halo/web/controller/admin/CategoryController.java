package cc.ryanc.halo.web.controller.admin;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.dto.JsonResult;
import cc.ryanc.halo.model.enums.ResultCodeEnum;
import cc.ryanc.halo.service.CategoryService;
import cc.ryanc.halo.utils.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     后台分类管理控制器
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2017/12/10
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    private final static int DEFAULT_PARENT = 0;

    private final static String DEFAULT_PARENTName = "请选择上级分类名称";

    /**
     * 查询所有分类并渲染category页面
     *
     * @return 模板路径admin/admin_category
     */
    @GetMapping
    public String categories(Model model) {
        List<Category> categoryList = categoryService.findAllByCatePid(DEFAULT_PARENT);
        model.addAttribute("categoryList", categoryList);
        return "admin/admin_category";
    }

    /**
     * 获取子目录节点
     *
     * @param catePid
     * @return java.lang.String
     * @author ZhangHui
     * @date 2019/12/5
     */
    @GetMapping("/getSub")
    @ResponseBody
    public JsonResult getSubCate(@RequestParam(value = "catepId", defaultValue = "0") int catePid) {
        List<Category> categoryList = categoryService.findAllByCatePid(catePid);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), categoryList);
    }

    /**
     * 新增/修改分类目录
     *
     * @param category category对象
     * @return 重定向到/admin/category
     */
    @PostMapping(value = "/save")
    public String saveCategory(@ModelAttribute Category category) {
        try {
            categoryService.save(category);
        } catch (Exception e) {
            log.error("Modify category failed: {}", e.getMessage());
        }
        return "redirect:/admin/category";
    }

    /**
     * 验证分类目录路径是否已经存在
     *
     * @param cateUrl 分类路径
     * @return JsonResult
     */
    @GetMapping(value = "/checkUrl")
    @ResponseBody
    public JsonResult checkCateUrlExists(@RequestParam("cateUrl") String cateUrl) {
        Category category = categoryService.findByCateUrl(cateUrl);
        if (null != category) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.url-is-exists"));
        }
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), "");
    }

    /**
     * 处理删除分类目录的请求
     *
     * @param cateId cateId
     * @return 重定向到/admin/category
     */
    @Transactional
    @GetMapping(value = "/remove")
    public String removeCategory(@RequestParam("cateId") Long cateId) {
        try {
            categoryService.remove(cateId);
            categoryService.updateAfterRemove(cateId);
            categoryService.deleteReferPost(cateId);
        } catch (Exception e) {
            log.error("Delete category failed: {}", e.getMessage());
        }
        return "redirect:/admin/category";
    }

    /**
     * 跳转到修改页面
     *
     * @param cateId cateId
     * @param model  model
     * @return 模板路径admin/admin_category
     */
    @GetMapping(value = "/edit")
    public String toEditCategory(Model model, @RequestParam("cateId") Long cateId) {
        Optional<Category> category = categoryService.findByCateId(cateId);
        model.addAttribute("updateCategory", category.get());

        long catePid = category.get().getCatePid();
        if (catePid == 0) {
            model.addAttribute("pCateName", DEFAULT_PARENTName);
        } else {
            model.addAttribute("pCateName", categoryService.findByCateId(catePid).get().getCateName());
        }
        Optional<Category> pCategory = categoryService.findByCateId(cateId);
        model.addAttribute("updateCategory", category.get());

        List<Category> categoryList = categoryService.findAllByCatePid(DEFAULT_PARENT);
        model.addAttribute("categoryList", categoryList);
        return "admin/admin_category";
    }


}
