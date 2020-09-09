package com.odianyun.search.whale.analysis.user;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.odianyun.search.whale.analysis.Constants;
import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.SegmentPolicy;
import com.odianyun.search.whale.analysis.TextFilter;
import com.odianyun.search.whale.analysis.ik.AutoReloadableISegmentFactory;
import com.odianyun.search.whale.analysis.ik.ReloadableISegmentFactory;
import com.odianyun.search.whale.analysis.ik.TextFilterSegment;
import com.odianyun.search.whale.analysis.post.*;

/**
 * 构建索引分词链
 *
 * @author jing liu
 */
public class IndexPolicy implements SegmentPolicy {
	
	static Logger log = Logger.getLogger(IndexPolicy.class);

    private ReloadableISegmentFactory factory;

    public IndexPolicy() throws Exception {
        factory = new AutoReloadableISegmentFactory();
    }

    @Override
    public ISegment get() throws Exception {
        //创建底层分词器
        ISegment max = this.factory.create(false);
        ISegment smart = this.factory.create(true);
        List<ISegment> segs = new LinkedList<ISegment>();
        segs.add(smart);
        segs.add(max);
        ISegment combine = new CombineSegment(segs);
        //创建过滤器
        List<PostTokenFilter> filters = new LinkedList<PostTokenFilter>();
        //繁体转简体
        SimplifiedChineseFilter chinese = new SimplifiedChineseFilter();
        SymbolTextFilter symbol = new SymbolTextFilter();
        filters.add(symbol);
        //filters.add(chinese);
        //同义词列表
        PostTokenFilter synonymy = this.createSynonymyFilter();
        if (synonymy != null) {
            filters.add(synonymy);
        }
        //扩展词列表
        PostTokenFilter extend = this.createExtendFilter();
        if (extend != null) {
            filters.add(extend);
        }
        //汉字转拼音
        PinyinFilter pinyin = new PinyinFilter();
        filters.add(pinyin);

        PostTokenFilterSegment top = new PostTokenFilterSegment(combine, filters);
        List<TextFilter> pres = new LinkedList<TextFilter>();
        pres.add(chinese);
//        pres.add(symbol);

        TextFilterSegment ret = new TextFilterSegment(top, pres);

        //构建完整分词策略
        return ret;
    }

    @Override
    public ISegment get(boolean isSmart) throws Exception {
        //暂不处理
        return get();
    }

    @Override
    public void reload() throws Exception {
        this.factory.reload();
    }

    private PostTokenFilter createSynonymyFilter() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Constants.getSynonymyPath()), "UTF-8"));

            SynonymyFilter filter = new SynonymyFilter(br);
            return filter;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //do nothing else
                }
            }
        }

        return null;
    }

    private PostTokenFilter createExtendFilter() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Constants.getExtendPath()), "UTF-8"));

            ExtendFilter filter = new ExtendFilter(br);
            return filter;
        } catch (FileNotFoundException e) {
        	log.error(e.getMessage(), e);
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //do nothing else
                }
            }
        }

        return null;
    }
}
