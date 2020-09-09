package com.odianyun.search.whale.index.business.process;


import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.Merchant;
import com.odianyun.search.whale.data.model.Shop;
import com.odianyun.search.whale.data.service.MerchantService;
import com.odianyun.search.whale.index.business.process.base.BaseProductMerchantProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import org.apache.commons.lang.StringUtils;

public class ProductMerchantProcessor extends BaseProductMerchantProcessor{
	
	
	MerchantService merchantService;

	public ProductMerchantProcessor(){
		super();
		merchantService = (MerchantService) ProcessorApplication.getBean("merchantService");
	}
	
	@Override
	public void calcProductMerchant(BusinessProduct businessProduct) throws Exception {
		Long merchantId = businessProduct.getMerchantId();
		if(merchantId == null){
			businessProduct.setMerchant_status(false);
			return;
		}
		int companyId=businessProduct.getCompanyId().intValue();
		Merchant merchant = merchantService.getMerchantById(merchantId,companyId);
		if(merchant!=null){
//			businessProduct.setMerchantName_search(merchant.getCompany_name());
			businessProduct.setMerchantType(merchant.getMerchant_type());
			businessProduct.setMerchantName_search(merchant.getName());
		}else{
			businessProduct.setMerchant_status(false);
		}
		Shop shop = merchantService.getShop(merchantId,companyId);
		if(shop!=null){
			String merchantName=businessProduct.getMerchantName_search();
			if(StringUtils.isBlank(merchantName)){
				merchantName=shop.getName();
			}else{
				if(shop.getName()!=null){
					merchantName=merchantName+" "+shop.getName();
				}
			}
			businessProduct.setMerchantName_search(merchantName);
		}
	}

}
