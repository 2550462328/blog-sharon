package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.domain.Tag;
import cc.ryanc.halo.model.dto.Archive;
import cc.ryanc.halo.model.enums.PostStatusEnum;
import cc.ryanc.halo.model.enums.PostTypeEnum;
import cc.ryanc.halo.model.enums.PostWrapTypeEnum;
import cc.ryanc.halo.repository.PostRepository;
import cc.ryanc.halo.service.CategoryService;
import cc.ryanc.halo.service.PostService;
import cc.ryanc.halo.service.TagService;
import cc.ryanc.halo.utils.ElasticSearchUtils;
import cc.ryanc.halo.utils.HaloUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * <pre>
 *     文章业务逻辑实现类
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2017/11/14
 */
@Service
public class PostServiceImpl implements PostService {

    private static final String POSTS_CACHE_NAME = "posts";

    private static final String COMMENTS_CACHE_NAME = "comments";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;
    
    @Resource
    private ElasticSearchUtils elasticSearchUtils;
    
    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    /**
     * 保存文章
     *
     * @param post Post
     * @return Post
     */
    @Override
    @CacheEvict(value = {POSTS_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Post save(Post post) {
        Post postSaved = postRepository.save(post);
        try {
            elasticSearchUtils.savePost(postSaved);
        } catch (IOException e) {
            logger.error("elastasic新增索引" + post.getPostId() + "出现IO异常 ", e);
        }
        return postSaved;
    }

    /**
     * 根据编号移除文章
     *
     * @param postId postId
     * @return Post
     */
    @Override
    @CacheEvict(value = {POSTS_CACHE_NAME, COMMENTS_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Post remove(Long postId) {
        Optional<Post> post = this.findByPostId(postId);
        postRepository.delete(post.get());
        return post.get();
    }

    /**
     * 修改文章状态
     *
     * @param postId postId
     * @param status status
     * @return Post
     */
    @Override
    @CacheEvict(value = POSTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Post updatePostStatus(Long postId, Integer status) {
        Optional<Post> post = this.findByPostId(postId);
        post.get().setPostStatus(status);
        try {
            if (status.equals(PostStatusEnum.RECYCLE.getCode()) || status.equals(PostStatusEnum.DRAFT.getCode())) {
                elasticSearchUtils.deletePost(postId.toString());
            } else if (status.equals(PostStatusEnum.PUBLISHED.getCode())) {
                elasticSearchUtils.savePost(post.get());
            }
        } catch (IOException e) {
            logger.error("elastasic新增或删除索引" + postId + "出现IO异常 ", e);
        }
        return postRepository.save(post.get());
    }

    /**
     * 批量更新文章摘要
     *
     * @param postSummary postSummary
     */
    @Override
    @CacheEvict(value = POSTS_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public void updateAllSummary(Integer postSummary) {
        List<Post> posts = this.findAll(PostTypeEnum.POST_TYPE_POST.getDesc());
        for (Post post : posts) {
            String text = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
            if (text.length() > postSummary) {
                post.setPostSummary(text.substring(0, postSummary));
            } else {
                post.setPostSummary(text);
            }
            postRepository.save(post);
        }
    }

    /**
     * 获取文章列表 不分页
     *
     * @param postType post or page
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_type_'+#postType")
    public List<Post> findAll(String postType) {
        return postRepository.findPostsByPostType(postType);
    }

    /**
     * 模糊查询文章
     *
     * @param keyWord  keyword
     * @param pageable pageable
     * @return List
     */
    @Override
    public Page<Post> searchPosts(String keyWord, long[] postIds, Integer status, Pageable pageable) {
        return postRepository.findByPostTitleIsLikeOrPostIdIsInAndPostStatus(keyWord, postIds, status, pageable);
    }

    /**
     * 根据文章状态查询 分页，用于后台管理
     *
     * @param status   0，1，2
     * @param postType post or page
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<Post> findPostByStatus(Integer status, String postType, Pageable pageable) {
        return postRepository.findPostsByPostStatusAndPostType(status, postType, pageable);
    }

    /**
     * 根据文章状态查询 分页，首页分页
     *
     * @param pageable pageable
     * @return Page
     */
    @Override
    public Page<Post> findPostByCondition(Pageable pageable, String keyword, Long cateId) {
        Page<Post> result = null;
        if (cateId != null) {
            long[] postIds = categoryService.findReferPost(cateId);
            result = postRepository.findPostsByPostStatusAndPostTypeAndPostIdIsIn(PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc(), postIds, pageable);
        } else if (StringUtils.hasText(keyword)) {
            try {
                result = elasticSearchUtils.getPost(keyword, pageable.getPageNumber(), pageable.getPageSize());
            } catch (IOException e) {
                logger.error("elastasic搜索" + keyword + "出现IO异常 ", e);
            }
        } else {
            result = postRepository.findPostsByPostStatusAndPostType(PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc(), pageable);
        }
        return result;
    }

    /**
     * 帮助inPost根据它里面的categories排序
     *
     * @param inPage
     * @return org.springframework.data.domain.Page<cc.ryanc.halo.model.domain.Post>
     * @author ZhangHui
     * @date 2019/12/23
     */
    @Override
    public List<Post> sortPostByCate(Page<Post> inPage) {
        List<Post> postList = new ArrayList<>(inPage.getContent());
        if (!postList.isEmpty() && postList.size() > 1) {
            Collections.sort(postList, (p1, p2) -> {
                return p1.getPostId() > p2.getPostId() ? 1 : -1;
            });
        }
        return postList;
    }

    /**
     * 根据文章状态查询
     *
     * @param status   0，1，2
     * @param postType post or page
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_status_type_'+#status+'_'+#postType")
    public List<Post> findPostByStatus(Integer status, String postType) {
        return postRepository.findPostsByPostStatusAndPostType(status, postType);
    }

    /**
     * 根据编号查询文章
     *
     * @param postId postId
     * @return Optional
     */
    @Override
    public Optional<Post> findByPostId(Long postId) {
        return postRepository.findById(postId);
    }

    /**
     * 根据编号和类型查询文章
     *
     * @param postId postId
     * @return Post
     */
    @Override
    public Post findByPostId(Long postId, String postType) {
        return postRepository.findPostByPostIdAndPostType(postId, postType);
    }

    /**
     * 根据文章路径查询
     *
     * @param postUrl  路径
     * @param postType post or page
     * @return Post
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_posturl_'+#postUrl+'_'+#postType")
    public Post findByPostUrl(String postUrl, String postType) {
        return postRepository.findPostByPostUrlAndPostType(postUrl, postType);
    }

    /**
     * 查询最新的5篇文章
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_latest'")
    public List<Post> findPostLatest() {
        return postRepository.findTopFive();
    }

    /**
     * 查询之后的文章
     *
     * @param postDate 发布时间
     * @return List
     */
    @Override
    public List<Post> findByPostDateAfter(Date postDate) {
        return postRepository.findByPostDateAfterAndPostStatusAndPostTypeOrderByPostDateDesc(postDate, PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc());
    }

    /**
     * 查询Id之前的文章
     *
     * @param postDate 发布时间
     * @return List
     */
    @Override
    public List<Post> findByPostDateBefore(Date postDate) {
        return postRepository.findByPostDateBeforeAndPostStatusAndPostTypeOrderByPostDateAsc(postDate, PostStatusEnum.PUBLISHED.getCode(), PostTypeEnum.POST_TYPE_POST.getDesc());
    }


    /**
     * 查询归档信息 根据年份和月份
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'archives_year_month'")
    public List<Archive> findPostGroupByYearAndMonth() {
        List<Object[]> objects = postRepository.findPostGroupByYearAndMonth();
        List<Archive> archives = new ArrayList<>();
        Archive archive = null;
        for (Object[] obj : objects) {
            archive = new Archive();
            archive.setYear(obj[0].toString());
            archive.setMonth(obj[1].toString());
            archive.setCount(obj[2].toString());
            archive.setPosts(this.findPostByYearAndMonth(obj[0].toString(), obj[1].toString()));
            archives.add(archive);
        }
        return archives;
    }

    /**
     * 查询归档信息 根据年份
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'archives_year'")
    public List<Archive> findPostGroupByYear() {
        List<Object[]> objects = postRepository.findPostGroupByYear();
        List<Archive> archives = new ArrayList<>();
        Archive archive = null;
        for (Object[] obj : objects) {
            archive = new Archive();
            archive.setYear(obj[0].toString());
            archive.setCount(obj[1].toString());
            archive.setPosts(this.findPostByYear(obj[0].toString()));
            archives.add(archive);
        }
        return archives;
    }

    /**
     * 根据年份和月份查询文章
     *
     * @param year  year
     * @param month month
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_year_month_'+#year+'_'+#month")
    public List<Post> findPostByYearAndMonth(String year, String month) {
        return postRepository.findPostByYearAndMonth(year, month);
    }

    /**
     * 根据年份查询文章
     *
     * @param year year
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_year_'+#year")
    public List<Post> findPostByYear(String year) {
        return postRepository.findPostByYear(year);
    }

    /**
     * 根据年份和月份索引文章
     *
     * @param year     year year
     * @param month    month month
     * @param pageable pageable pageable
     * @return Page
     */
    @Override
    public Page<Post> findPostByYearAndMonth(String year, String month, Pageable pageable) {
        return postRepository.findPostByYearAndMonth(year, month, null);
    }

    /**
     * 根据分类目录查询文章
     *
     * @param category category
     * @param pageable pageable
     * @return Page
     */
    @Override
    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_category_'+#category.cateId+'_'+#pageable.pageNumber")
    public Page<Post> findPostByCategories(Category category, Pageable pageable) {
        return postRepository.findPostByCategoriesAndPostStatus(category, PostStatusEnum.PUBLISHED.getCode(), pageable);
    }

//    /**
//     * 根据标签查询文章，分页
//     *
//     * @param tag      tag
//     *                 //     * @param status   status
//     * @param pageable pageable
//     * @return Page
//     */
//    @Override
//    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_tag_'+#tag.tagId+'_'+#pageable.pageNumber")
//    public Page<Post> findPostsByTags(Tag tag, Pageable pageable) {
//        return postRepository.findPostsByTagsAndPostStatus(tag, PostStatusEnum.PUBLISHED.getCode(), pageable);
//    }

    /**
     * 搜索文章
     *
     * @param keyword  关键词
     * @param pageable 分页信息
     * @return Page
     */
    @Override
    public Page<Post> searchByKeywords(String keyword, Pageable pageable) {
        return postRepository.findPostByPostTitleLikeOrPostContentLikeAndPostTypeAndPostStatus(keyword, pageable);
    }

    /**
     * 热门文章
     *
     * @return List
     */
    @Override
    @Cacheable(value = POSTS_CACHE_NAME, key = "'posts_hot'")
    public List<Post> hotPosts() {
        return postRepository.findPostsByPostTypeOrderByPostViewsDesc(PostTypeEnum.POST_TYPE_POST.getDesc());
    }

    /**
     * 当前文章的相似文章
     *
     * @param post post
     * @return List
     */
//    @Override
//    @CachePut(value = POSTS_CACHE_NAME, key = "'posts_related_'+#post.getPostId()")
//    public List<Post> relatedPosts(Post post) {
//        //获取当前文章的所有标签
//        List<Tag> tags = post.getTags();
//        List<Post> tempPosts = new ArrayList<>();
//        for (Tag tag : tags) {
//            tempPosts.addAll(postRepository.findPostsByTags(tag));
//        }
//        //去掉当前的文章
//        tempPosts.remove(post);
//        //去掉重复的文章
//        List<Post> allPosts = new ArrayList<>();
//        for (int i = 0; i < tempPosts.size(); i++) {
//            if (!allPosts.contains(tempPosts.get(i))) {
//                allPosts.add(tempPosts.get(i));
//            }
//        }
//        return allPosts;
//    }

    /**
     * 获取所有文章的阅读量
     *
     * @return Long
     */
    @Override
    public Long getPostViews() {
        return postRepository.getPostViewsSum();
    }

    /**
     * 根据文章状态查询数量
     *
     * @param status 文章状态
     * @return 文章数量
     */
    @Override
    public Integer getCountByStatus(Integer status) {
        return postRepository.countAllByPostStatusAndPostType(status, PostTypeEnum.POST_TYPE_POST.getDesc());
    }

    /**
     * 生成rss
     *
     * @param posts posts
     * @return String
     */
    @Override
    public String buildRss(List<Post> posts) {
        String rss = "";
        try {
            rss = HaloUtils.getRss(posts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rss;
    }

    /**
     * 生成sitemap
     *
     * @param posts posts
     * @return String
     */
    @Override
    public String buildSiteMap(List<Post> posts) {
        return HaloUtils.getSiteMap(posts);
    }

    /**
     * 缓存阅读数
     *
     * @param postId postId
     */
    @Override
    public void cacheViews(Long postId) {
        //更新阅读数
        Post p = postRepository.getOne(postId);
        p.setPostViews(p.getPostViews() + 1);
        postRepository.saveAndFlush(p);

    }

    /**
     * 组装分类目录和标签
     *
     * @param post     post
     * @param cateList cateList
     * @param tagList  tagList
     * @return Post Post
     */
    @Override
    public Post buildCategoriesAndTags(Post post, List<String> cateList, String tagList) {
        List<Category> categories = categoryService.strListToCateList(cateList);
        if (categories.isEmpty()) {
            categories.add(categoryService.findByCateId(1L).get());
        }
        post.setCategories(categories);
        if (StrUtil.isNotEmpty(tagList)) {
            List<Tag> tags = tagService.strListToTagList(StrUtil.trim(tagList));
            post.setTags(tags);
        }
        return post;
    }

    /**
     * 包装Post中得postSummary，让它关键词变黑
     *
     * @param
     * @return java.util.List<cc.ryanc.halo.model.domain.Post>
     * @author ZhangHui
     * @date 2019/12/26
     */
    @Override
    public List<Post> wrapPostSummary(List<Post> postList, String keyword, int splitLength, PostWrapTypeEnum postWrapTypeEnum) {
        List<Post> resultPost = new ArrayList<>();
        List<Post> notReferPost = new ArrayList<>();

        for (Post post : postList) {
            String postContent = StrUtil.cleanBlank(HtmlUtil.cleanHtmlTag(post.getPostContent()));
            if (postContent.indexOf(keyword) > -1) {
                int index = postContent.indexOf(keyword);
                post.setPostSummary(subPostContent(postContent, keyword, index, splitLength, postWrapTypeEnum));
                resultPost.add(post);
            } else {
                notReferPost.add(post);
            }
        }
        if (!notReferPost.isEmpty()) {
            resultPost.addAll(notReferPost);
        }
        return resultPost;
    }

    @Override
    public Page<Map<String, Object>> findPostsByCateId(Pageable pageable, Long cateId) {
        return postRepository.findPostsByCateId(cateId, pageable);
    }

    /**
     * 切割postContent
     *
     * @return
     * @author ZhangHui
     * @date 2019/12/26
     */
    private String subPostContent(String postContent, String keyword, int index, int splitLength, PostWrapTypeEnum postWrapTypeEnum) {
        StringBuilder sb = new StringBuilder();
        int len = postContent.length();
        if (index < splitLength) {
            sb.append(postContent.substring(0, index));
            if ((len - index - 1) >= (splitLength * 2 - index)) {
                sb.append(postContent.substring(index, splitLength * 2 - index));
            } else {
                sb.append(postContent.substring(index, len - 1));
            }
        } else {
            if (len - index - 1 > splitLength) {
                sb.append(postContent.substring(index - splitLength + 1, index));
                sb.append(postContent.substring(index, index + splitLength));
            } else {
                if (index > splitLength * 2 - 1) {
                    sb.append(postContent.substring(index - splitLength * 2 + 1, index));
                } else {
                    sb.append(postContent.substring(0, index));
                }
                sb.append(postContent.substring(index, len - 1));
            }
        }
        return sb.toString().replace(keyword, postWrapTypeEnum.getBeginTag() + keyword + postWrapTypeEnum.getEndTag());
    }
}
