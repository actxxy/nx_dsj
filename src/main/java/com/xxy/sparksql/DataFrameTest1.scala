package com.xxy.sparksql

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}



object DataFrameTest1 {
  case class Stu(var sid:String,var name:String,var age:Int)
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("DataFrameTest1").master("local").getOrCreate()
    val sourceRdd: RDD[String] = spark.sparkContext.textFile("D:\\work\\train_data\\nx_dsj\\sparksql\\stu.txt")
    val stuRdd: RDD[Stu] = sourceRdd.map(
      row => {
        var splits = row.split(",")
        Stu(splits(0), splits(1), splits(2).toInt)
      }
    )
    import spark.implicits._
    val frame: DataFrame = stuRdd.toDF()
    frame.createOrReplaceTempView("stu")
    spark.sql("select * from stu where sid='1001'").show()
//    frame.show()

//    val frame: DataFrame = spark.read.json("D:\\work\\train_data\\nx_dsj\\sparksql\\stu.json")
//    frame.show()
//    frame.foreach(row=>{println(row.getAs[String]("name"))})
    spark.close()
  }
}
