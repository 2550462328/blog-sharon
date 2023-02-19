package cc.ryanc.halo.model.enums;

/**
 * <pre>
 *     文件标签标记类型
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/7/1
 */
public enum PostWrapTypeEnum {

    /**
     * h5
     */
    H5("<span style='font-size:18px;font-weight: bolder'>", "</span>"),

    /**
     * 微信小程序
     */
    WX("<span style='font-size: 16px;font-weight: 700'>", "</span>");


    private String beginTag;
    private String endTag;

    PostWrapTypeEnum(String beginTag, String endTag) {
        this.beginTag = beginTag;
        this.endTag = endTag;
    }

    public String getBeginTag() {
        return beginTag;
    }

    public String getEndTag() {
        return endTag;
    }
}
