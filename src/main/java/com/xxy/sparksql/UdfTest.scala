package com.xxy.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

/**
 * spark udf函数
 */

object UdfTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("UDF").getOrCreate()
    spark.udf.register("strLen",(str:String)=> {
      if (str != null) {
        str.length
      } else {
        0
      }
    })
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
    spark.sql("select strLen(name) from stu").show()
    spark.close()

  }
}
