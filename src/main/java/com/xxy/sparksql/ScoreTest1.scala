package com.xxy.sparksql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * spark行列转换
 */
object ScoreTest1 {
  case class Score(var sid:String,var name :String,var xk:String,var score:Integer)
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local").appName("行列转换").getOrCreate()
    val rdd: RDD[String] = spark.sparkContext.textFile("D:\\work\\train_data\\nx_dsj\\sparksql\\score.txt")
    val scoreRdd: RDD[Score] = rdd.map(row => {
      val fields: Array[String] = row.split(",")
      Score(fields(0), fields(1), fields(2), fields(3).toInt)
    })
    import spark.implicits._
    val frame: DataFrame = scoreRdd.toDF()
    frame.createOrReplaceTempView("score")
//    val sql = "select * from score pivot (sum(score) for xk in ('语文','数学','英语','政治'))"
//    spark.sql(sql).show()
    val vpivot: DataFrame = spark.sql(
      """
        |select * from score
        |pivot
        |(
        | sum(score) for
        | xk in ('语文','数学','英语','政治')
        |)
        |order by sid
        |""".stripMargin)
    vpivot.show()
    vpivot.createOrReplaceTempView("vpivot")
    spark.sql(
      """
        |select sid,name,
        |stack(4,'语文',`语文`,'数学',`数学`,'英语',`英语`,'政治',`政治`) as (xk,score)
        |from vpivot
        |""".stripMargin).show()


    spark.close()


  }
}
