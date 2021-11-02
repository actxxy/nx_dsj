package com.xxy.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

/**
 * sparksql 读取mysql表
 */

object JdbcTest1 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("DataFrameTest1").master("local").getOrCreate()
    val properties = new Properties()
    val url = "jdbc://mysql://node03:3306/xxy"
    properties.put("url",url)
    properties.put("user","root")
    properties.put("password","123456")
    properties.put("driver","com.mysql.jdbc.Driver")
    val frame: DataFrame = spark.read.jdbc(url, "user_shaoxing", properties)
    frame.show()
    spark.close()


  }

}
