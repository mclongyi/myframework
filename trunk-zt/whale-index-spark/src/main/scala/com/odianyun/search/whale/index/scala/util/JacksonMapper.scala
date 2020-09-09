package com.odianyun.search.whale.index.scala.util

import java.lang.reflect.ParameterizedType

import com.fasterxml.jackson.core._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.core.`type`.{TypeReference}
/**
  * Created by zengfenghua on 16/5/10.
  */
object JacksonMapper {

  def jsonSerializer = {
    val m = new ObjectMapper
    m.registerModule(DefaultScalaModule)
    m
  }

  def main(args: Array[String]) {
       val testObject=Test("lucy",25)
       println(testObject)
       val testObjectJson=jsonSerializer.writeValueAsString(testObject)
       println(testObjectJson)
       val testObject2=jsonSerializer.readValue(testObjectJson,classOf[Test])
       println(testObject2)
  }


  case class Test(name: String, age: Int)

/**

  def deserializeJson[T: Manifest](value: String): T = jsonSerializer.readValue(value, typeReference[T])
  def serializeJson(value: Any) = jsonSerializer.writerWithDefaultPrettyPrinter().writeValueAsString(value)

  private[this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) { m.erasure }
    else new ParameterizedType {
      def getRawType = m.erasure
       def getActualTypeArguments=m.typeArguments.map(typeFromManifest).toArray
     // def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }
  */

}
