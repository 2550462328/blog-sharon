package cc.ryanc.halo.utils;

import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : RYAN0UP
 * @date : 2018/11/14
 */
public class MarkdownUtils {

    /**
     * Front-matter插件
     */
    private static final Set<Extension> EXTENSIONS_YAML = Collections.singleton(YamlFrontMatterExtension.create());

    /**
     * Table插件
     */
    private static final Set<Extension> EXTENSIONS_TABLE = Collections.singleton(TablesExtension.create());

    /**
     * 解析Markdown文档
     */
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS_YAML).extensions(EXTENSIONS_TABLE).build();

    /**
     * 渲染HTML文档
     */
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS_YAML).extensions(EXTENSIONS_TABLE).build();

    /**
     * 渲染Markdown
     *
     * @param content content
     * @return String
     */
    public static String renderMarkdown(String content) {
        return renderMarkdown(content, true);
    }

    /**
     * 解析content成html
     *
     * @param content
     * @param isParseB
     * @return java.lang.String
     * @author ZhangHui
     * @date 2020/1/10
     */
    public static String renderMarkdown(String content, boolean isParseB) {
        Node document = PARSER.parse(content);
        String postContent = RENDERER.render(document);
        if (isParseB) {
            postContent = postContent.replaceAll("\\*\\[\\*", "<b style='font-size:12.5px'>").replaceAll("\\*\\]\\*", "</b>");
        }
        return postContent;
    }

    /**
     * 获取元数据
     *
     * @param content content
     * @return Map
     */
    public static Map<String, List<String>> getFrontMatter(String content) {
        YamlFrontMatterVisitor visitor = new YamlFrontMatterVisitor();
        Node document = PARSER.parse(content);
        document.accept(visitor);
        return visitor.getData();
    }
}
