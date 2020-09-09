package com.odianyun.search.whale.index.scala.model

import scala.beans.BeanProperty
import scala.collection.mutable

case class OBusinessProduct(id: Long, productId: Long, merchantId: Long) {
  @BeanProperty var merchantProduct: OMerchantProduct = null

  @BeanProperty var pics: mutable.LinkedList[OMerchantProductPic] = mutable.LinkedList.empty[OMerchantProductPic]

  @BeanProperty var price: OMerchantProductPrice = null

  @BeanProperty var stock: OMerchantProductStock = null

  @BeanProperty var stocks: mutable.LinkedList[OMerchantProductStock] = mutable.LinkedList.empty[OMerchantProductStock]

  @BeanProperty var merchantProductAttributeIds : mutable.HashSet[Long] = mutable.HashSet.empty[Long]

  @BeanProperty var merchantProdMerchantCateTreeNodes:mutable.LinkedList[OMerchantProdMerchantCateTreeNode]
             =mutable.LinkedList.empty[OMerchantProdMerchantCateTreeNode]

  @BeanProperty var merchant: OMerchant = null

  @BeanProperty var volume4Sale: OMerchantVolume4Sale = null

  @BeanProperty var merchantBelongArea: OMerchantBelongArea = null

  @BeanProperty var productAttributes: mutable.LinkedList[OProductAttribute] = mutable.LinkedList.empty[OProductAttribute]

  @BeanProperty var product: OProduct =null

  @BeanProperty var rate : OMerchantRate = null

  @BeanProperty var saleOffset : OMerchantSaleOffset = null



  /**
    * merchantProductSearch 存储所有需要索引的最后的字段值
    */

  @BeanProperty var merchantProductSearch: OMerchantProductSearch = OMerchantProductSearch(id)
}


case class OMerchantProduct(id: Long, productId: Long, merchantId: Long, chinese_name: String,
                            english_name: String, subtitle: String, is_deleted: Int = 0,
                            create_time: String, code: String, companyId: Long, merchantSeriesId: Long,
                            tax: String, product_type: Int, sale_type: Int = 0, isNew: Int,managementState:Int = 0)
case class OMerchantProductPic(url: String, sortValue: Int)
case class OMerchantProductPrice(price: Double)
case class OMerchantProductStock(stock: Int)
case class OMerchantProdMerchantCateTreeNode(merchant_cate_tree_node_id:Long)

case class OArea(id: Long, code: Int, name: String, level: Int, parentCode: Int, postCode: String, company_id: Int)
case class OMerchantVolume4Sale(volume4Sale: Long)
case class OMerchantSaleOffset(saleOffset: Long)
case class OMerchantRate(rate : Double,positiveRate : Int,ratingCount : Int)
case class OMerchantBelongArea(merchantId: Long, nationId: Long, provinceId: Long, cityId: Long,
                               regionId: Long, areaCode: Long)
case class OMerchant(merchantId: Long, merchantType: Int, merchantStatus: Boolean, shopName: String)

case class OBrand(name: String, chinese_name: String, english_name: String)
case class OCategoryTreeNode(catNodeId: Long, parentId: Long, catId: Long, listSort: Int, name: String)
case class OCategoryRelation(catNodeId: Long, leftTreeNodeId: Long, rightTreeNodeId: Long)
case class OMerchantCategoryNode(merchantCatNodeId: Long, parentId: Long, merchantCatId: Long, catId: Long, name: String)
case class OProduct(id:Long,brand_id:Long,category_tree_node_id:Long,ean_no:String,code:String,calculation_unit:String,standard:String)
case class OProductAttribute(product_id:Long,att_name_id:Long,att_value_id:Long)
case class OAttributeValue(merchantSeriesId:Long,valueId:Long,value:String,nameId:Long,name_name:String,
                           attrType:Int,parent_id:Long,sort_value:Int)
case class OMerchantProductAttribute(merchant_product_id:Long,att_value_id:Long)

