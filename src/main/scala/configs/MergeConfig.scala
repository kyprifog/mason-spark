package mason.spark.configs

import scopt.OptionParser

object MergeConfig {
  val zero = MergeConfig("", input_format="parquet", "")

  val parser = new OptionParser[MergeConfig]("mason-spark") {
    opt[String]('i', "input_path")
      .required()
      .valueName("<input_path>")
      .action((x,c) => c.copy(input_path = x))
    opt[String]('o', "output_path")
      .required()
      .valueName("<output_path>")
      .action((x,c) => c.copy(output_path = x))
    opt[Boolean]('p', "extract_file_paths")
      .valueName("<extract_paths>")
      .action((x,c) => c.copy(extract_file_path = x))
    opt[String]('r', "repartition_keys")
      .valueName("<repartition_keys>")
      .action((x,c) => c.copy(repartition_keys = x))
    opt[Boolean]('h', "read_headers")
      .valueName("<read_headers>")
      .action((x,c) => c.copy(read_headers = x))
    opt[String]('h', "input_format")
      .valueName("<input_format>")
      .action((x,c) => c.copy(input_format = x))
    // BIG TODO:  Wrap the metastore configuration into a seperate config and pass this along so that this is not aws specific
    opt[String]('a', "access_key")
      .valueName("<access_key>")
      .action((x,c) => c.copy(access_key = x))
    opt[String]('s', "secret_key")
      .valueName("<secret_key>")
      .action((x,c) => c.copy(secret_key = x))
  }
}

case class MergeConfig(
  input_path: String,
  input_format: String = "parquet",
  output_path: String,
  extract_file_path: Boolean = false,
  repartition_keys: String = "",
  read_headers: Boolean = true,
  access_key: String = "",
  secret_key: String = ""
) extends JobConfig
