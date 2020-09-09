package com.odianyun.search.whale.data.dao.impl;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.odianyun.search.whale.data.common.ServiceConstants;
import com.odianyun.search.whale.data.dao.HotSaleMerchantProductDao;
import com.odianyun.search.whale.data.model.MerchantProductSale;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fishcus on 16/11/24.
 */
public class HotSaleMerchantProductDaoImpl extends SqlMapClientDaoSupport implements HotSaleMerchantProductDao {

    static final int BATCH_NUM = 500;
    @Override
    public void batchSave(final List<MerchantProductSale> merchantProductSaleList, final Integer type) throws Exception {
        if(CollectionUtils.isNotEmpty(merchantProductSaleList)){
            this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
                @Override
                public Object doInSqlMapClient(SqlMapExecutor sqlMapExecutor) throws SQLException {
                    sqlMapExecutor.startBatch();
                    int count = 0;
                    for(MerchantProductSale sale : merchantProductSaleList){
                        count++;
                        sale.setType(type);
                        sqlMapExecutor.insert("insertHotSaleMerchantProduct",sale);
                        if(count == BATCH_NUM){
                            sqlMapExecutor.executeBatch();
                        }
                    }
                    sqlMapExecutor.executeBatch();
                    return null;
                }
            });
        }
    }

    @Override
    public List<MerchantProductSale> queryHotSaleMerchantProductWithPage(long maxId, int pageSize,String version) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ServiceConstants.MAX_ID, maxId);
        params.put(ServiceConstants.PAGE_SIZE, pageSize);
        params.put("version", version);

        return this.getSqlMapClientTemplate().queryForList("queryHotSaleMerchantProductWithPage",params);
    }

    @Override
    public String queryLatestVersion() throws Exception {
        return (String) this.getSqlMapClientTemplate().queryForObject("queryLatestVersion");
    }
}
