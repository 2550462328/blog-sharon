package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Category;
import org.hibernate.annotations.Cache;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <pre>
 *     分类持久层
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2017/11/30
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param cateUrl cateUrl 文章url
     * @return Category
     */
    Category findCategoryByCateUrl(String cateUrl);

    /**
     * 根据分类名称查询
     *
     * @param cateName 分类名称
     * @return Category
     */
    Category findCategoryByCateName(String cateName);

    /**
     * 根据分类查询文章总数
     *
     * @return Long
     */
    @Query(value = "select count(1) from halo_posts_categories where cate_id=:cateId", nativeQuery = true)
    Long getCategoryPostCount(@Param("cateId") Long cateId);

    /**
     * 根据父节点id查找子类
     * @author ZhangHui
     * @date 2019/12/5
     * @param cateId
     * @return java.util.List<cc.ryanc.halo.model.domain.Category>
     */
    List<Category> findAllByCatePid(long cateId);

    /**
     * 查找节点cateId下的直接子节点的个数
     * @author ZhangHui
     * @date 2019/12/9
     * @param cateId
     * @return int
     */
    @Query(value="select count(*) from halo_category where cate_pid=:cateId", nativeQuery = true)
    int countChildCates(@Param("cateId") long cateId);

    /**
     * 修改原cateId下的直接子节点
     * @author ZhangHui
     * @date 2019/12/9
     * @param cateId
     * @return void
     */
    @Modifying
    @Query(value="update halo_category set cate_pid = 1 where cate_pid=:cateId", nativeQuery = true)
    void updateAfterRemove(@Param("cateId") long cateId);

    @Modifying
    @Query(value = "delete from halo_posts_categories where cate_id=:cateId", nativeQuery = true)
    void deleteReferPost(@Param("cateId")long cateId);

    @Query(value = "select post_id from halo_posts_categories where cate_id=:cateId", nativeQuery = true)
    long[] findReferPost(@Param("cateId")long cateId);
}
