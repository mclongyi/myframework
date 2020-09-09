package com.odianyun.search.whale.suggest.check;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import com.odianyun.search.whale.common.util.Latin;
import com.odianyun.search.whale.common.util.PinYin;
import com.odianyun.search.whale.es.api.ESClient;
import com.odianyun.search.whale.index.api.common.SuggestIndexConstants;
import com.odianyun.search.whale.suggest.common.IndexFieldConstants;
import com.odianyun.search.whale.suggest.model.QueryContext;
import com.odianyun.search.whale.suggest.model.QueryResult;

/**
 * 
 * @author yuqian
 *
 */
public class DisCheckStrategy implements CheckStrategy {
	
	static Logger log = Logger.getLogger(DisCheckStrategy.class);
    public static final int LIMIT_LENGTH = 5;
    public static final int THRESHOLD_ROWS = 5;
	@Override
	public List<QueryResult> reWrite(QueryContext context) throws Exception {
		List<QueryResult> rets = new ArrayList<QueryResult>();
		String input = context.getOriginal();
		BoolQueryBuilder boolQuery = buildQuery(input);

		SearchRequestBuilder searchRequestBuilder=ESClient.getClient().prepareSearch(
				SuggestIndexConstants.indexName_pre)
		        .setTypes("qrw")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(boolQuery)
		        .setFetchSource(false).setExplain(false)
		        .setFrom(0).setSize(10);
				
				String[] fields=new String[]{};
				searchRequestBuilder.addFields(fields);

	SortBuilder sortBuilder= new FieldSortBuilder("score").order(SortOrder.DESC);
	searchRequestBuilder.addSort(sortBuilder);
	SearchResponse queryResponse = searchRequestBuilder.execute().actionGet();
	
	SearchHits hits = queryResponse.getHits();
		if(hits!=null&&hits.getHits().length>0)
			return rets;
	
	    SearchHit[] hitArray=hits.getHits();
	    for(int i=0;i<hitArray.length;i++){
	    	SearchHit hit=hitArray[i];
	    	QueryResult qr = convertSearchHitField(hit.getFields());
	    	rets.add(qr);
	    	
	    }
		return null;
	}
	
	public QueryResult convertSearchHitField(Map<String,SearchHitField> fieldMap){
		//TODO
		return null;
	}
	
	
	public BoolQueryBuilder buildQuery(String input){
		
		BoolQueryBuilder checkQueryBuilder=new BoolQueryBuilder();
		
		
		if(!Latin.isLatinString(input, true)){
			/*如果是非英文的话，考虑拼音与首拼的情况*/
			try {
				String spell = PinYin.toPinYin(input);
				String firstSpell = PinYin.toFirstSpell(input);
				checkQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.QUANPIN,spell).boost(10.0f));
				checkQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.SHOUPIN,firstSpell));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}
		}
		
		String orderStr = PinYin.toSort(input);
		checkQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CHAR_SORT, orderStr).boost(10.0f));
		checkQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.CHAR_CUT, input));
		if(input.length()<LIMIT_LENGTH){
			List<String> cuts = PinYin.cutOneChar(input);
			for(String cut:cuts){
			    checkQueryBuilder.should(new TermQueryBuilder(IndexFieldConstants.ORI_TEXT, cut));
			}
		}
		return checkQueryBuilder;
	}
	
	

	
}
