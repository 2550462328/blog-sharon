package cc.ryanc.halo.config.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangHui
 * @version 1.0
 * @className RoleTagDirective
 * @description 自定义指令，roleTag
 * @date 2019/6/6
 */
@Service
public class RoleTagDirective implements TemplateDirectiveModel {
    /**
     * 功能描述
     * @author ZhangHui
     * @date 2019/6/6
     * @param env 环境变量
     * @param map 指令参数
     * @param templateModels 循环变量
     * @param templateDirectiveBody 指令内容
     * @return void
     */
    @Override
    public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        TemplateScalarModel user = (TemplateScalarModel) map.get("user");
        TemplateScalarModel role = (TemplateScalarModel) map.get("role");

        if("zhanghui".equals(user.toString()) && "admin".equals(role.toString())){
            templateModels[0] = TemplateBooleanModel.TRUE;
        }

        List<String> roleList = new ArrayList<String>();
        roleList.add("add");
        roleList.add("delete");
        roleList.add("update");
        templateModels[1] = new SimpleSequence(roleList);
        templateDirectiveBody.render(env.getOut());
    }
}
