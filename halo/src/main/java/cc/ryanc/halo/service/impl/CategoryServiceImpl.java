package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.repository.CategoryRepository;
import cc.ryanc.halo.service.CategoryService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *     分类业务逻辑实现类
 * </pre>
 *
 * @author : RYAN0UP
 * @date : 2017/11/30
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String POSTS_CACHE_NAME = "posts";

    private static final String SUBCATES_CACHE_NAME = "subCategories";

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 保存/修改分类目录
     * @param category 分类目录
     * @return Category
     */
    @Override
    @CacheEvict(value = {POSTS_CACHE_NAME, SUBCATES_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Category save(Category category) {
        if(category.getCatePid() == null){
            category.setCatePid(0L);
        }
        return categoryRepository.save(category);
    }

    /**
     * 根据编号移除分类目录
     * @param cateId 分类目录编号
     * @return Category
     */
    @Override
    @CacheEvict(value = {POSTS_CACHE_NAME, SUBCATES_CACHE_NAME}, allEntries = true, beforeInvocation = true)
    public Category remove(Long cateId) {
        Optional<Category> category = this.findByCateId(cateId);
        categoryRepository.delete(category.get());
        return category.get();
    }

    /**
     * 查询所有分类目录
     *
     * @return List
     */
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * 根据编号查询分类目录
     *
     * @param cateId 分类编号
     * @return Category
     */
    @Override
    public Optional<Category> findByCateId(Long cateId) {
        return categoryRepository.findById(cateId);
    }

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param cateUrl cateUrl
     * @return Category
     */
    @Override
    public Category findByCateUrl(String cateUrl) {
        return categoryRepository.findCategoryByCateUrl(cateUrl);
    }

    /**
     * 根据分类名称查询
     *
     * @param cateName 分类名称
     * @return Category
     */
    @Override
    public Category findByCateName(String cateName) {
        return categoryRepository.findCategoryByCateName(cateName);
    }

    /**
     * 将分类字符串集合转化为Category泛型集合
     *
     * @param strings strings
     * @return List
     */
    @Override
    public List<Category> strListToCateList(List<String> strings) {
        if (null == strings) {
            return null;
        }
        List<Category> categories = new ArrayList<>();
        Optional<Category> category = null;
        for (String str : strings) {
            category = findByCateId(Long.parseLong(str));
            categories.add(category.get());
        }
        return categories;
    }

    @Override
    @Cacheable(value = SUBCATES_CACHE_NAME)
    public List<Category> findAllByCatePid(int catePid) {
        List<Category> categoryList = categoryRepository.findAllByCatePid(catePid);
        categoryList = categoryList.stream().map(cate -> {
            Category cateTo = (Category) cate;
            if (categoryRepository.countChildCates(cate.getCateId()) > 0) {
                cateTo.setHasChild(true);
            } else {
                cateTo.setHasChild(false);
            }
            return cateTo;
        }).collect(Collectors.toList());

        return categoryList;
    }

    @Override
    public void updateAfterRemove(long cateId) {
        categoryRepository.updateAfterRemove(cateId);
    }

    @Override
    public void deleteReferPost(long cateId) {
        categoryRepository.deleteReferPost(cateId);
    }

    /**
     * 查找cateId在关联表中关联的postId的集合（包括当前cateId的子类）
     * @author ZhangHui
     * @date 2019/12/23
     * @param cateId
     * @return long[]
     */
    @Override
    public long[] findReferPost(long cateId) {
        List<Long> resultList = new LinkedList<>();

        Set<Long> referSet = new TreeSet<>();
        referSet.add(cateId);

        List<Category> childCates = this.findChildCatesById(cateId);
        childCates.stream().forEach(category -> {
            referSet.add(category.getCateId());
        });

        Iterator<Long> iterator = referSet.iterator();
        while(iterator.hasNext()){
            Long referCateId = iterator.next();
            long[] referPostIds = categoryRepository.findReferPost(referCateId);
            if(referPostIds.length > 0){
                for(int i = 0; i < referPostIds.length; i++){
                    resultList.add(referPostIds[i]);
                }
            }
        }

        if(!resultList.isEmpty()) {
            Long[] result = resultList.toArray(new Long[resultList.size()]);
            return toSwapLongArray(result);
        }
        return new long[]{};
    }


    /**
     * 根据cateId查找所有子分类
     * @author ZhangHui
     * @date 2019/12/23
     * @param cateId
     * @return java.util.List<cc.ryanc.halo.model.domain.Category>
     */
    @Override
    public List<Category> findChildCatesById(long cateId) {
        Set<Category> referSet = new TreeSet<>();

        List<Category> currentRefer = categoryRepository.findAllByCatePid(cateId);
        getChildCates(referSet, currentRefer);

        return new ArrayList<>(referSet);
    }

    @Override
    public String convertToTreeMap(List<Category> categoryList) {
        if(categoryList.isEmpty()){
            return null;
        }
        List<Map<String, Object>> cateList = new ArrayList<>();
        //文章分类TreeView
        for (Category category : categoryList) {
            Map<String, Object> cateTreeMap = new HashMap<>();
            cateTreeMap.put("id", category.getCateId());
            cateTreeMap.put("name", category.getCateName());
            cateTreeMap.put("pId", category.getCatePid());
            cateTreeMap.put("icon",category.getCateIcon());
            cateTreeMap.put("open", false);
            cateList.add(cateTreeMap);
        }
        return JSONObject.toJSONString(cateList);
    }


    /**
     * 递归获取集合cateids中的所有子类节点，放到resutSet中
     * @author ZhangHui
     * @date 2019/12/10
     * @param referSet
     * @param currentRefer
     * @return void
     */
    private void getChildCates(Set<Category> referSet, List<Category> currentRefer){
        if(currentRefer.size() > 0) {
            for(int i =0, length = currentRefer.size(); i < length; i++){
                referSet.add(currentRefer.get(i));
                List<Category>  nextRefer = categoryRepository.findAllByCatePid(currentRefer.get(i).getCateId());
                getChildCates(referSet, nextRefer);
            }
        }
    }

    /**
     * 包装类数组转换成基本类型数组
     * @author ZhangHui
     * @date 2019/12/10
     * @param array
     * @return long[]
     */
    private long[] toSwapLongArray(Long[] array){
        long[] swapArray = new long[array.length];
        for(int i = 0; i < array.length; i++){
            swapArray[i] = array[i];
        }
        return swapArray;
    }
}
