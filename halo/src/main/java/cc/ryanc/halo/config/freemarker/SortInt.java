package cc.ryanc.halo.config.freemarker;

import freemarker.template.SimpleSequence;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SortInt
 * @description freemarker的自定义排序函数
 * @date 2019/6/6
 */
public class SortInt implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        //list中是参数列表
        SimpleSequence arg0 = (SimpleSequence) list.get(0);
        List<BigDecimal> sortList = arg0.toList();
        Collections.sort(sortList, new Comparator<BigDecimal>() {
            @Override
            public int compare(BigDecimal o1, BigDecimal o2) {
                return o1.intValue() - o2.intValue();
            }
        });
        return sortList;
    }
}
