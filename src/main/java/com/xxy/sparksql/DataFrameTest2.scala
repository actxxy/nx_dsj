package com.xxy.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * sparksql dataframe schema
 */
object DataFrameTest2 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("DataFrameTest1").master("local").getOrCreate()
    val sourceRdd: RDD[String] = spark.sparkContext.textFile("D:\\work\\train_data\\nx_dsj\\sparksql\\stu.txt")
    val rowRdd: RDD[Row] = sourceRdd.map(line => {
      val fields: Array[String] = line.split(",")
      Row(fields(0), fields(1), fields(2).toInt)
    })
    val stuSchema = StructType(
      StructField("sid", StringType, true)::
      StructField("name", StringType, true)::
      StructField("age", IntegerType, true)::Nil
    )

    val frame: DataFrame = spark.createDataFrame(rowRdd, stuSchema)
    frame.createOrReplaceTempView("stu")
    spark.sql("select * from stu where sid='1001'").show()

    spark.close()
  }
}
