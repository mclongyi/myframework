package com.odianyun.search.whale.data.manager;

import com.odianyun.search.backend.api.service.DictFromDbToDiskServiceClient;
import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.post.PinyinFilter;
import com.odianyun.search.whale.analysis.user.IndexPolicy;
import com.odianyun.search.whale.common.util.LyfStringUtils;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.data.model.geo.O2OStore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * index-end word split logic
 * using ik and our tokenfilter
 */
public class SegmentManager {

    //多个词的拼接器
    public final static String WORDCONNECT = " ";

    static Logger log = Logger.getLogger(SegmentManager.class);

    private IndexPolicy index;
    private volatile ISegment segment;
    private volatile PinyinFilter pinyin;

    private static SegmentManager sm = new SegmentManager();

    private static Pattern supplement = Pattern.compile("(\\w+)\\(\\w+?\\)(\\w+)");

    public static SegmentManager getInstance() {
        return sm;
    }

    protected SegmentManager() {
        try {
            this.index = new IndexPolicy();
            this.segment = this.index.get();
            this.pinyin = new PinyinFilter();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 热更新词典
     *
     * @throws Exception
     */
    public void reload() throws Exception {
        this.index.reload();
        DictFromDbToDiskServiceClient.getInstance().exportDictFromDbToDisk();
        this.segment = this.index.get();
        this.pinyin = new PinyinFilter();
    }

    public void process(List<MerchantProductSearch> searches) throws Exception {
        for (MerchantProductSearch search : searches) {
            String tag_words = search.getTag_words();
            if (StringUtils.isNotEmpty(tag_words)) {
                String tag_words_rewrite = doProcess(tag_words);
                if (StringUtils.isNotEmpty(tag_words_rewrite)) {
                    search.setTag_words(tag_words_rewrite);
                }
            }
            String categoryName_search = search.getCategoryName_search();
            if (StringUtils.isNotEmpty(categoryName_search)) {
                String categoryName_search_rewrite = doProcess(categoryName_search);
                if (StringUtils.isNotEmpty(categoryName_search_rewrite)) {
                    search.setCategoryName_search(categoryName_search_rewrite);
                }
            }
            String attrValue_search = search.getAttrValue_search();
            if (StringUtils.isNotEmpty(attrValue_search)) {
                String attrValue_search_rewrite = doProcess(attrValue_search);
                if (StringUtils.isNotEmpty(attrValue_search_rewrite)) {
                    search.setAttrValue_search(attrValue_search_rewrite);
                }
            }
            String merchantName_search = search.getMerchantName_search();
            if (StringUtils.isNotEmpty(merchantName_search)) {
                String merchantName_search_rewrite = doProcess(merchantName_search);
                if (StringUtils.isNotEmpty(merchantName_search_rewrite)) {
                    search.setMerchantName_search(merchantName_search_rewrite);
                }
            }
            String brandName_search = search.getBrandName_search();
            if (StringUtils.isNotEmpty(brandName_search)) {
                String brandName_search_rewrite = doProcess(brandName_search);
                if (StringUtils.isNotEmpty(brandName_search_rewrite)) {
                    search.setBrandName_search(brandName_search_rewrite);
                }
            }


        }
    }

    public void process(BusinessProduct businessProduct) throws Exception {
        String tag_words = businessProduct.getTag_words();
        if (StringUtils.isNotEmpty(tag_words)) {
            String tag_words_rewrite = doProcess(tag_words);
            if (StringUtils.isNotEmpty(tag_words_rewrite)) {
                businessProduct.setTag_words(tag_words_rewrite);
            }
        }
        StringBuffer tag_wordsBuffer = new StringBuffer();
        if (businessProduct.getCode() != null) {
            tag_wordsBuffer.append(" " + businessProduct.getCode().trim().toLowerCase());
        }
        Set<String> subCodeSet = businessProduct.getSubCodeSet();
        if (CollectionUtils.isNotEmpty(subCodeSet)) {
            subCodeSet.remove(businessProduct.getCode());
            if (CollectionUtils.isNotEmpty(subCodeSet)) {
                for (String code : subCodeSet) {
                    tag_wordsBuffer.append(" " + code.trim().toLowerCase());
                }
            }
        }

        if (businessProduct.getEan_no() != null && !businessProduct.getEan_no().equals("0")) {
            tag_wordsBuffer.append(" " + businessProduct.getEan_no().toLowerCase());
        }

//			List<String> dest_barcodes = businessProduct.getDest_barcode();
//			if(CollectionUtils.isNotEmpty(dest_barcodes)){
//				for (String destbarcode:dest_barcodes) {
//					if(!destbarcode.equals("0")){
//						tag_wordsBuffer.append(" "+destbarcode.toLowerCase());
//					}
//				}
//			}
        if (businessProduct.getProductCode() != null) {
            tag_wordsBuffer.append(" " + businessProduct.getProductCode().trim().toLowerCase());
        }
        /**
         * 补充处理  CPC(D)30S 这类词
         */
        if (StringUtils.isNotEmpty(tag_words)) {
            Matcher n = supplement.matcher(tag_words);
            while (n.find()) {
                String value = n.group(0);
                String temp = value.replace("(", "").replace(")", "");
                tag_wordsBuffer.append(" " + temp.trim().toLowerCase());
                temp = value.substring(0, value.indexOf("(")) + value.substring(value.indexOf(")") + 1);
                tag_wordsBuffer.append(" " + temp.trim().toLowerCase());
            }
        }
        businessProduct.setTag_words(businessProduct.getTag_words() + " " + tag_wordsBuffer.toString());

        String categoryName_search = businessProduct.getCategoryName_search();
        if (StringUtils.isNotEmpty(categoryName_search)) {
            String categoryName_search_rewrite = doProcess(categoryName_search);
            if (StringUtils.isNotEmpty(categoryName_search_rewrite)) {
                businessProduct.setCategoryName_search(categoryName_search_rewrite);
            }
        }
        /**
         * 属性计算
         */
        StringBuffer attrValueStringBuffer = new StringBuffer();
        for (String value : businessProduct.getAttrValueSet()) {
            attrValueStringBuffer.append(value + WORDCONNECT);
        }
        String attrValue_search = attrValueStringBuffer.toString();
        if (StringUtils.isNotEmpty(attrValue_search)) {
            String attrValue_search_rewrite = doProcess(attrValue_search);
            if (StringUtils.isNotEmpty(attrValue_search_rewrite)) {
                businessProduct.setAttrValue_search(attrValue_search_rewrite);
            }
        }

        String merchantName_search = businessProduct.getMerchantName_search();
        if (StringUtils.isNotEmpty(merchantName_search)) {
            String merchantName_search_rewrite = doProcess(merchantName_search);
            if (StringUtils.isNotEmpty(merchantName_search_rewrite)) {
                businessProduct.setMerchantName_search(merchantName_search_rewrite);
            }
        }
        String brandName_search = businessProduct.getBrandName_search();
        if (StringUtils.isNotEmpty(brandName_search)) {
            String brandName_search_rewrite = doProcess(brandName_search);
            if (StringUtils.isNotEmpty(brandName_search_rewrite)) {
                businessProduct.setBrandName_search(brandName_search_rewrite);
            }
        }
        String tw = businessProduct.getTag_words();
        String keyword = LyfStringUtils.guessKeyword(businessProduct.getChinese_name());
        if (StringUtils.isNotEmpty(tw) && StringUtils.isNotEmpty(keyword) && !tw.contains(keyword)) {
            //对商品的中文名,如果没有配置成为主词,也当做主词来处理,使得拼音搜索可以直接搜索全名
            String res = getKeyword(keyword, tw);
            tw += res;
            businessProduct.setTag_words(tw);
//            System.out.println("原商品名词 = "+businessProduct.getChinese_name()+",猜词结果="+keyword+",最终搜索词="+res);
        }
    }

    public void process(O2OStore o2OStore) throws Exception {
        String tag_words = o2OStore.getTag_words();
        if (StringUtils.isNotBlank(tag_words)) {
            String tag_words_write = doProcess(tag_words);
            o2OStore.setTag_words(tag_words_write);
        }


    }

    public String doProcess(String text) throws Exception {
        List<String> unique = this.segment.segment(text);
        Set<String> uniqueSet = new HashSet(unique);
        StringBuilder ret = new StringBuilder();

        for (String t : uniqueSet) {
            ret.append(t).append(" ");
        }

        return ret.toString();
    }

    /**
     * 对某个关键字做拼音
     *
     * @param keyword 关键字:天天坚果
     * @return 天天坚果   ttjg tiantianjianguo
     * @throws Exception
     */
    public String getKeyword(String keyword) throws Exception {
        if (StringUtils.isEmpty(keyword)) {
            return "";
        }
        List<String> unique = pinyin.action(null, keyword);
        Set<String> uniqueSet = new HashSet(unique);
        StringBuilder ret = new StringBuilder(" ");

        for (String t : uniqueSet) {
            ret.append(t).append(" ");
        }

        return ret.toString();
    }

    /**
     * 对某个关键字做拼音，然后拼接结果（已经去重）
     * @return tag_word
     */
    public String getKeyword(String keyword, String tag_word) throws Exception {
        if (StringUtils.isEmpty(keyword)) {
            return tag_word;
        }
        List<String> unique = pinyin.action(null, keyword);
        for (String t : unique) {
            if (!tag_word.contains(t)) {
                tag_word += " " + t;
            }
        }
        return tag_word;
    }


    public static void main(String[] args) {
//        String text = "AAAAdsda[香]辣&%$00  藕片190g";
//        System.out.println(getChinese(text));
        String str = "你好，空指针，Welcome to 游戏大厅！ ";
    }

}
