package cc.ryanc.halo.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangHui
 * @version 1.0
 * @className FreemarkerUtil
 * @description freemarker工具类
 * @date 2019/6/4
 */
public class FreemarkerUtil {

    private FreemarkerUtil(){};
   /**
    * 通过指定的名字获取相应的模板
    * @author ZhangHui
    * @date 2019/6/4
    * @param fileName
    * @return freemarker.template.Template
    */
    public  static Template getTemplate( String filePath, String fileName) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        String templatePath = FreemarkerUtil.class.getResource("/").getPath()+"/templates" + filePath;
        try {
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            Template template = cfg.getTemplate(fileName);
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过指定的文件目录和文件名生成相应的文件
     * @param template
     * @param paramMap
     * @return java.lang.Boolean
     * @author ZhangHui
     * @date 2019/6/4
     */
    public static Boolean printToFile(Template template, File htmFile, Map<String, Object> paramMap) {
        boolean done = false;
        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmFile), "UTF-8"));) {
            //输出模板和数据模型都对应的文件
            template.process(paramMap, writer);
            done = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return done;
    }

    public static void main(String[] args) {
        File file = new File("E:\\htmFile\\test.htm");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userName", "张辉");
        Template template = FreemarkerUtil.getTemplate("/test","hello.ftl");
        FreemarkerUtil.printToFile(template, file, paramMap);
        System.out.println("operate done！");
    }
}
