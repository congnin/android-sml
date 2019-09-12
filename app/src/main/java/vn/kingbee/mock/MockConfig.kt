package vn.kingbee.mock

data class MockConfig(val url: String,
                      val responseBodyPath: String,
                      var responseBodyFile: String? = null,
                      val responseHeader: List<Pair<String, String>>? = null,
                      val filterKey: String? = null)