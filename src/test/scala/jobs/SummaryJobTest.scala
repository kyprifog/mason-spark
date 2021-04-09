package test.jobs.summary

import com.holdenkarau.spark.testing.{DataFrameSuiteBase, SharedSparkContext}
import mason.spark.configs.SummaryConfig
import mason.spark.jobs.SummaryJob
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import scala.reflect.io.Directory
import java.io.File
import org.apache.log4j.{Level, Logger}
import util.Spark.assertEquals


class SummaryJobTest extends AnyFunSuite with BeforeAndAfter with DataFrameSuiteBase with SharedSparkContext {
  before {
    spark.sparkContext.setLogLevel("FATAL")
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    Logger.getLogger("spark").setLevel(Level.OFF)
  }

  after {
    val directory = new Directory(new File(".tmp"))
    directory.deleteRecursively()
  }

  test("valid csv test") {
    val config = new SummaryConfig("src/test/resources/test_csv_with_nulls/", "text-csv", ".tmp/summary/", true, "", "")

    SummaryJob.run(config)

    val summaryDF = spark.read.parquet(".tmp/summary")

    val expect = """
    +------+-----+--------------+--------------+----------+-------+------+
    |column|count|distinct_count|non_null_count|null_count|max    |min   |
    +------+-----+--------------+--------------+----------+-------+------+
    |type  |8    |8             |8             |2         |wrench5|hammer|
    |price |9    |8             |9             |1         |9.0    |10.0  |
    |*     |10   |7             |7             |0         |null   |null  |
    +------+-----+--------------+--------------+----------+-------+------+
    """.stripMargin
    assertEquals(summaryDF, expect)
  }




}
