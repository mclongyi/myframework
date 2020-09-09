package com.odianyun.search.whale.index.scala.process

import com.odianyun.search.whale.index.scala.common.{BroadcastVals, NumConstants, ProcessContext}
import com.odianyun.search.whale.index.scala.model._

import scala.collection.{Map, mutable}

/**
  * Created by zengfenghua on 16/5/17.
  */
class ProductCategoryProcessor extends Processor {

  var productCategoryInfoCache = mutable.HashMap.empty[Long, ProductCategoryInfo]

  override def process(processContext: ProcessContext): Unit = {

    val merchantCatNodeMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.merchantCategoryNode,
      Map.empty[Long, OMerchantCategoryNode]).asInstanceOf[Map[Long, OMerchantCategoryNode]]
    val catNodeMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.categoryTreeNode,
      Map.empty[Long, OCategoryTreeNode]).asInstanceOf[Map[Long, OCategoryTreeNode]]
    val catRelationMap = processContext.broadcastMap.value.getOrElse(BroadcastVals.categoryRelation,
      Map.empty[Long, mutable.LinkedList[OCategoryRelation]]).asInstanceOf[Map[Long, mutable.LinkedList[OCategoryRelation]]]

    processContext.data = processContext.data.map(a => (a._1, calcCategory(a._2, merchantCatNodeMap, catNodeMap, catRelationMap)))

  }

  private def calcCategory(businessProduct: OBusinessProduct, merchantCatNodeMap: Map[Long, OMerchantCategoryNode],
                           catNodeMap: Map[Long, OCategoryTreeNode],
                           catRelationMap: Map[Long, mutable.LinkedList[OCategoryRelation]]): OBusinessProduct = {
    calcProductCategory(businessProduct, catNodeMap, catRelationMap)
    calcMerchantCategory(businessProduct, merchantCatNodeMap)
  }


  private def calcMerchantCategory(businessProduct: OBusinessProduct,
                                   merchantCatNodeMap: Map[Long, OMerchantCategoryNode]): OBusinessProduct = {
    val merchantProductSearch = businessProduct.merchantProductSearch
    val merchantCategoryIds = new StringBuffer()
    val merchantCategoryId_search = new StringBuffer()
    for (mpmCategory <- businessProduct.merchantProdMerchantCateTreeNodes) {
      var merchantCategoryNode = merchantCatNodeMap.getOrElse(mpmCategory.merchant_cate_tree_node_id, null)
      if (merchantCategoryNode != null) {
        merchantCategoryIds.append(merchantCategoryNode.catId + " ")
      }
      while (merchantCategoryNode != null) {
        merchantCategoryId_search.append(merchantCategoryNode.catId + " ")
        merchantProductSearch.categoryName_search = merchantProductSearch.categoryName_search + " " + merchantCategoryNode.name
        val parentId = merchantCategoryNode.parentId
        if (parentId != 0) {
          merchantCategoryNode = merchantCatNodeMap.getOrElse(parentId, null)
        } else {
          merchantCategoryNode = null
        }

      }
    }
    merchantProductSearch.setMerchant_categoryId(merchantCategoryIds.toString)
    merchantProductSearch.setMerchantCategoryId_search(merchantCategoryId_search.toString)
    businessProduct
  }

  private def calcProductCategory(businessProduct: OBusinessProduct, catNodeMap: Map[Long, OCategoryTreeNode],
                                  catRelationMap: Map[Long, mutable.LinkedList[OCategoryRelation]]): OBusinessProduct = {
    val merchantProductSearch = businessProduct.merchantProductSearch
    if (businessProduct.product != null) {
      val productCategoryInfoOption = productCategoryInfoCache.get(businessProduct.product.category_tree_node_id)
      if (productCategoryInfoOption != None) {
        setProductCategoryInfo(merchantProductSearch, productCategoryInfoOption.get)
      } else {
        val categroyTreeNode = catNodeMap.getOrElse(businessProduct.product.category_tree_node_id, null)
        if(categroyTreeNode != null) {
          val categories = getFullPathCategory(categroyTreeNode, catNodeMap)
          var naviCategories = mutable.HashSet.empty[OCategoryTreeNode]
          var categoryId_search: String = ""
          var categoryName_search: String = ""
          for (treeNode <- categories) {
            categoryId_search = categoryId_search + " " + treeNode.catId
            categoryName_search = categoryName_search + " " + treeNode.name
            val categoryRelations = catRelationMap.getOrElse(treeNode.catNodeId, null)
            if (categoryRelations != null) {
              for (categoryRelation <- categoryRelations) {
                val categroyTreeNode = catNodeMap.getOrElse(categoryRelation.leftTreeNodeId, null)
                if (categroyTreeNode != null) {
                  naviCategories ++= getFullPathCategory(categroyTreeNode, catNodeMap)
                }
              }
            }
          }
          var naviCategoryId_search: String = ""
          for (category <- naviCategories) {
            naviCategoryId_search = naviCategoryId_search + " " + category.catId
            categoryName_search = categoryName_search + " " + category.name
          }
          val productCategoryInfo = ProductCategoryInfo(categroyTreeNode.catId, categoryId_search,
            naviCategoryId_search, categoryName_search)
          setProductCategoryInfo(merchantProductSearch, productCategoryInfo)
          productCategoryInfoCache.put(businessProduct.product.category_tree_node_id, productCategoryInfo)
        }
      }
    }

    businessProduct

  }

  private def setProductCategoryInfo(merchantProductSearch: OMerchantProductSearch, productCategoryInfo: ProductCategoryInfo): Unit = {
    merchantProductSearch.categoryId = productCategoryInfo.categoryId
    merchantProductSearch.categoryId_search = productCategoryInfo.categoryId_search
    merchantProductSearch.navCategoryId_search = productCategoryInfo.navCategoryId_search
    merchantProductSearch.categoryName_search = productCategoryInfo.categoryName_search
  }

  private def getFullPathCategory(catNode: OCategoryTreeNode, catNodeMap: Map[Long, OCategoryTreeNode]): Seq[OCategoryTreeNode] = {
    var categories = scala.collection.mutable.LinkedList.empty[OCategoryTreeNode]
    if(catNode != null) {
      categories = categories :+ catNode
      var parentId = catNode.parentId
      while (parentId != NumConstants.ZERO) {
        val parentCategroyTreeNode = catNodeMap.getOrElse(parentId, null)
        if (parentCategroyTreeNode != null) {
          categories = categories :+ parentCategroyTreeNode
          parentId = parentCategroyTreeNode.parentId
        } else {
          parentId = NumConstants.ZERO
        }
      }
    }
    categories

  }

  case class ProductCategoryInfo(categoryId: Long, categoryId_search: String, navCategoryId_search: String, categoryName_search: String)


}
