package cc.ryanc.halo.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     文章分类
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2017/11/30
 */
@Entity
@Table(name = "halo_category")
@ToString(of = {"cateId","cateName"})
public class Category implements Serializable, Comparable {

    private static final long serialVersionUID = 8383678847517271505L;

    /**
     * 分类编号
     */
    @Id
    @GeneratedValue
    private Long cateId;

    /**
    上级分组Id
    */
    private Long catePid;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String cateName;

    /**
     * 分类路径
     */
    @NotBlank(message = "分类路径不能为空")
    private String cateUrl;

    /**
     * 分类图标
     */
    @NotBlank(message = "分类图标不能为空")
    private String cateIcon;

    /**
     * 分类描述
     */
    private String cateDesc;

    @Transient
    private Boolean hasChild;

    @ManyToMany(mappedBy = "categories",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    @Transient
    private List<Post> posts = new ArrayList<>();

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }

    public Long getCatePid() {
        return catePid;
    }

    public void setCatePid(Long catePid) {
        this.catePid = catePid;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateUrl() {
        return cateUrl;
    }

    public void setCateUrl(String cateUrl) {
        this.cateUrl = cateUrl;
    }

    public String getCateIcon() {
        return cateIcon;
    }

    public void setCateIcon(String cateIcon) {
        this.cateIcon = cateIcon;
    }

    public String getCateDesc() {
        return cateDesc;
    }

    public void setCateDesc(String cateDesc) {
        this.cateDesc = cateDesc;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int compareTo(Object o) {
        Category category = (Category) o;
        if (this.cateId.equals(category.cateId)) {
            return 0;
        }
        return this.cateId > category.cateId ? 1 : -1;
    }
}
