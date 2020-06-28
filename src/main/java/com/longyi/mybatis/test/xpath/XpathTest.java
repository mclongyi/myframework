package com.longyi.mybatis.test.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

/**
 * @author ly
 * @Description  对xpath的应用
 * @date 2020/6/21 15:23
 */
public class XpathTest {

  public static void main(String[] args) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    vaild(documentBuilderFactory);
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
   documentBuilder.setErrorHandler(new ErrorHandler() {
     @Override
     public void warning(SAXParseException exception) throws SAXException {
       
     }

     @Override
     public void error(SAXParseException exception) throws SAXException {

     }

     @Override
     public void fatalError(SAXParseException exception) throws SAXException {

     }
   });

    Document doc =
        documentBuilder.parse(
            "D:\\sourcecode\\mybatis-test\\src\\main\\java\\com\\longyi\\mybatis\\test\\xpath/MapperConfig.xml");
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();
    XPathExpression compile = xPath.compile("//settings/setting[@name='cacheEnabled']");
    NodeList evaluate = (NodeList)compile.evaluate(doc, XPathConstants.NODESET);
    for(int i=0;i<evaluate.getLength();i++){
      System.out.println(evaluate.item(i));
    }
    System.out.println(evaluate.toString());
  }

  private static void vaild(DocumentBuilderFactory documentBuilderFactory) {
    documentBuilderFactory.setValidating(true);
    documentBuilderFactory.setNamespaceAware(false);
    documentBuilderFactory.setIgnoringComments(true);
    documentBuilderFactory.setIgnoringElementContentWhitespace(false);
    documentBuilderFactory.setCoalescing(false);
    documentBuilderFactory.setExpandEntityReferences(true);
  }

}
   