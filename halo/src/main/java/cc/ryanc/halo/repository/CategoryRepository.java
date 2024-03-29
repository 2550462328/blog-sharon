package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Category;
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

    @Query(value = "SELECT cate_id FROM( SELECT @r AS _ids, ( SELECT @r \\:= GROUP_CONCAT( cate_id) FROM halo_category WHERE FIND_IN_SET( cate_pid, @r ) ) AS subIds FROM ( SELECT @r \\:= :pId ) vars,halo_category WHERE @r IS NOT NULL ) sub, halo_category WHERE FIND_IN_SET( halo_category.cate_id, sub._ids)", nativeQuery = true)
    List<Long> findCateIdByCatePid(@Param("pId") Long pId);

    @Query(value = "select count(*) from halo_posts_categories hpc inner join halo_post hp on hpc.post_id = hp.post_id where hpc.cate_id in (:cateIds) and hp.post_status = 0", nativeQuery = true)
    int countSubPostsByCateIds(List<Long> cateIds);

    /**
     * 根据父节点id查找子类
     *
     * @param cateId
     * @return java.util.List<cc.ryanc.halo.model.domain.Category>
     * @author ZhangHui
     * @date 2019/12/5
     */
    List<Category> findAllByCatePid(long cateId);

    /**
     * 查找节点cateId下的直接子节点的个数
     *
     * @param cateId
     * @return int
     * @author ZhangHui
     * @date 2019/12/9
     */
    @Query(value = "select count(*) from halo_category where cate_pid=:cateId", nativeQuery = true)
    int countChildCates(@Param("cateId") long cateId);

    /**
     * 修改原cateId下的直接子节点
     *
     * @param cateId
     * @return void
     * @author ZhangHui
     * @date 2019/12/9
     */
    @Modifying
    @Query(value = "update halo_category set cate_pid = 1 where cate_pid=:cateId", nativeQuery = true)
    void updateAfterRemove(@Param("cateId") long cateId);

    @Modifying
    @Query(value = "delete from halo_posts_categories where cate_id=:cateId", nativeQuery = true)
    void deleteReferPost(@Param("cateId") long cateId);

    @Query(value = "select post_id from halo_posts_categories where cate_id=:cateId", nativeQuery = true)
    long[] findReferPost(@Param("cateId") long cateId);
}
