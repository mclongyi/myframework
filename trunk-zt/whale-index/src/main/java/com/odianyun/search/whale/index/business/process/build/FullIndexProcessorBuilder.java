package com.odianyun.search.whale.index.business.process.build;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.index.business.process.*;
import com.odianyun.search.whale.processor.Processor;
import com.odianyun.search.whale.processor.ProcessorsBuilder;

public class FullIndexProcessorBuilder implements ProcessorsBuilder {


	@Override
	public List<Processor> build() throws Exception {
		// TODO Auto-generated method stub
		List<Processor> processors = new ArrayList<Processor>();
		processors.add(new BusinessProductProcessor());
		processors.add(new ProductPicUrlProcessor());
		processors.add(new SearchWordProcessor());
		processors.add(new ProductPriceProcessor());
		processors.add(new ProductMerchantProcessor());
		processors.add(new ProductBrandProcessor());
		processors.add(new ProductCategoryProcessor());
		processors.add(new ProductAttributeProcessor());
		processors.add(new ProductStockProcessor());
		processors.add(new MerchantVolume4SaleProcessor());
		processors.add(new MerchantProductWeightProcessor());
		processors.add(new MerchantRateProcessor());
		processors.add(new ProductSeriesProcessor());
		processors.add(new DistributionProcessor());
		processors.add(new SegmentProcessor());
		processors.add(new ProductScriptProcessor());
		processors.add(new ProductSaleAreasProcessor());
		processors.add(new MerchantAreaCodeProcessor());
		processors.add(new MerchantProductRefIdProcess());
		processors.add(new PointsBusinessProductProcessor());
		processors.add(new CurrentPointsBusinessProductProcessor());

		processors.add(new MerchantProductSearchProcessor());
		processors.add(new IncIndexProcessor());

		return processors;
	}

}
