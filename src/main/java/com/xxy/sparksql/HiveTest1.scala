package com.xxy.sparksql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * sparksql 访问hive表
 */

object HiveTest1 {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    val spark = SparkSession
      .builder
      .appName("HiveTest1")
      .enableHiveSupport()
      .master("local")
      .config("hive.metastore.uris", "thrift://node02:9083")
      .config("spark.sql.warehouse.dir", "hdfs://node01:9000/user/hive/warehouse")
      .getOrCreate()
    val pivot: DataFrame = spark.sql(
      """
        |select * from ods.score
        |pivot
        |(
        | sum(score) for
        | xk in ('语文','数学','英语','政治')
        |)
        |""".stripMargin)
    pivot.show()
    pivot.createOrReplaceTempView("pivot")
    spark.sql(
      """
        |select
        | name,
        | stack(4,'语文',`语文`,'数学',`数学`,'英语',`英语`,'政治',`政治`) as (xk,score)
        | from pivot
        |""".stripMargin).show()
    spark.close()
  }
}
