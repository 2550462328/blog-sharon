package cc.ryanc.halo.repository;

import cc.ryanc.halo.model.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <pre>
 *     图库持久层
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2018/2/26
 */
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    @Query(value = "select gallery_url from halo_gallery", nativeQuery = true)
    List<String> findAllGalleryUrl();
}
