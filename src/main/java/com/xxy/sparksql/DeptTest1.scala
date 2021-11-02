package com.xxy.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * sparksql 排序
 */

object DeptTest1 {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local").appName("sortDeptOrderBySalary").getOrCreate()
    val dept: RDD[String] = spark.sparkContext.textFile("D:\\work\\train_data\\nx_dsj\\sparksql\\dept.txt")
    val schema = StructType(
      StructField("id", StringType, true) ::
        StructField("name", StringType, true) ::
        StructField("dept", StringType, true) ::
        StructField("salary", IntegerType, true) :: Nil
    )
    val rowRDD: RDD[Row] = dept.map(row => {
      val fields = row.split(",")
      Row(fields(0), fields(1), fields(2), fields(3).toInt)
    })
    val frame: DataFrame = spark.createDataFrame(rowRDD, schema)
    frame.createOrReplaceTempView("dept")
    spark.sql(
      """
        |select id,name,dept,salary
        |from (select id,name,dept,salary,row_number() over (partition by dept order by salary desc) rank from dept order by rank) tmp
        |""".stripMargin).show()

    spark.close()
  }

}
