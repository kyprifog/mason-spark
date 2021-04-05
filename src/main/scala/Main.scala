package mason.spark

import mason.spark.configs.{JobConfig, MergeConfig}

object Main {
  def main(args: Array[String]) {

    val jc = JobConfig.zero
    val argMap = args.grouped(2).map { case Array(p1: String,p2: String) => (p1,p2) }.toArray.groupBy(_._1).map { case (k,v) => (k,v.map(_._2).head)}
    val jobTypeArgs = Array("--job", argMap("--job"))
    val otherArgs = argMap.-("--job").toArray.flatMap{case (k,v) => List(k,v)}

    jc.parser.parse(jobTypeArgs, jc) match {
      case Some(config) =>
        if (config.job == "merge") {
          val mc = MergeConfig.zero
          mc.parser.parse(otherArgs, mc) match {
            case Some(mergeConfig) => {
              mergeConfig.run()
            }
            case None => println(s"Bad merge config specification ${args}")
          }
        }
        else {
          println(f"Job type not supported ${config.job}")
        }
      case None => println(s"Bad config specification ${args}")
    }
  }

}

