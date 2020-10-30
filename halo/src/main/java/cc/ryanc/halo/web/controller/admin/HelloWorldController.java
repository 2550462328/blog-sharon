package cc.ryanc.halo.web.controller.admin;

import cc.ryanc.halo.config.freemarker.SortInt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ZhangHui
 * @version 1.0
 * @className HelloWorldController
 * @description 这是测试控制类
 * @date 2019/6/4
 */
@Slf4j
@Controller
@RequestMapping("/hello")
public class HelloWorldController {
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("userName", "zhanghui");
        model.addAttribute("sort_int", new SortInt());
        return "test/hello1";
    }
}
