package me.ift8.basic.webdriver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by IFT8 on 2019-01-29.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElkDump {

    private List<ResponsesResponse> responses;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponsesResponse {
        /**
         * took : 55
         * timed_out : false
         * _shards : {"total":528,"successful":528,"skipped":480,"failed":0}
         * hits : {"total":2934,"max_score":null,"hits":[{"_index":"znjksyb-logs-2019.01.29","_type":"logs","_id":"y2lxl2gBCVUngXkeIaQW","_version":1,"_score":null,"_source":{"thread":"nioEventLoopGroup-5-6","@version":"1","@timestamp":"2019-01-29T02:29:59.178Z","fields":{"log_type":"znjksyb-great-device","project_name":"znjksyb-great-device","team_name":"ZNJKSYB","ip":"172.16.26.3"},"source":"/data/app_log/great-device/access.log","app_name":"great-device","offset":954367430,"msg":"-->:012510730526:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E  ","level":"INFO","time":"2019-01-29 10:29:59.107","tags":["znjksyb-great-device"],"type":"log","stack_trace":"","log_name":"common.utils.LogUtil"},"fields":{"@timestamp":["2019-01-29T02:29:59.178Z"]},"highlight":{"msg":["-->:@kibana-highlighted-field@012510730526@/kibana-highlighted-field@:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E"],"app_name":["@kibana-highlighted-field@great@/kibana-highlighted-field@-@kibana-highlighted-field@device@/kibana-highlighted-field@"],"log_name":["@kibana-highlighted-field@common.utils.LogUtil@/kibana-highlighted-field@"]},"sort":[1548728999178]}]}
         * aggregations : {"2":{"buckets":[{"key_as_string":"2019-01-29T09:00:00.000+08:00","key":1548723600000,"doc_count":18},{"key_as_string":"2019-01-29T09:01:00.000+08:00","key":1548723660000,"doc_count":18},{"key_as_string":"2019-01-29T09:02:00.000+08:00","key":1548723720000,"doc_count":6},{"key_as_string":"2019-01-29T09:03:00.000+08:00","key":1548723780000,"doc_count":6},{"key_as_string":"2019-01-29T09:04:00.000+08:00","key":1548723840000,"doc_count":65},{"key_as_string":"2019-01-29T09:05:00.000+08:00","key":1548723900000,"doc_count":228},{"key_as_string":"2019-01-29T09:06:00.000+08:00","key":1548723960000,"doc_count":228},{"key_as_string":"2019-01-29T09:07:00.000+08:00","key":1548724020000,"doc_count":6},{"key_as_string":"2019-01-29T09:08:00.000+08:00","key":1548724080000,"doc_count":16},{"key_as_string":"2019-01-29T09:09:00.000+08:00","key":1548724140000,"doc_count":18},{"key_as_string":"2019-01-29T09:10:00.000+08:00","key":1548724200000,"doc_count":18},{"key_as_string":"2019-01-29T09:11:00.000+08:00","key":1548724260000,"doc_count":33},{"key_as_string":"2019-01-29T09:12:00.000+08:00","key":1548724320000,"doc_count":25},{"key_as_string":"2019-01-29T09:13:00.000+08:00","key":1548724380000,"doc_count":18},{"key_as_string":"2019-01-29T09:14:00.000+08:00","key":1548724440000,"doc_count":18},{"key_as_string":"2019-01-29T09:15:00.000+08:00","key":1548724500000,"doc_count":24},{"key_as_string":"2019-01-29T09:16:00.000+08:00","key":1548724560000,"doc_count":18},{"key_as_string":"2019-01-29T09:17:00.000+08:00","key":1548724620000,"doc_count":18},{"key_as_string":"2019-01-29T09:18:00.000+08:00","key":1548724680000,"doc_count":18},{"key_as_string":"2019-01-29T09:19:00.000+08:00","key":1548724740000,"doc_count":49},{"key_as_string":"2019-01-29T09:20:00.000+08:00","key":1548724800000,"doc_count":80},{"key_as_string":"2019-01-29T09:21:00.000+08:00","key":1548724860000,"doc_count":84},{"key_as_string":"2019-01-29T09:22:00.000+08:00","key":1548724920000,"doc_count":107},{"key_as_string":"2019-01-29T09:23:00.000+08:00","key":1548724980000,"doc_count":34},{"key_as_string":"2019-01-29T09:24:00.000+08:00","key":1548725040000,"doc_count":33},{"key_as_string":"2019-01-29T09:25:00.000+08:00","key":1548725100000,"doc_count":16},{"key_as_string":"2019-01-29T09:26:00.000+08:00","key":1548725160000,"doc_count":51},{"key_as_string":"2019-01-29T09:27:00.000+08:00","key":1548725220000,"doc_count":18},{"key_as_string":"2019-01-29T09:28:00.000+08:00","key":1548725280000,"doc_count":18},{"key_as_string":"2019-01-29T09:29:00.000+08:00","key":1548725340000,"doc_count":18},{"key_as_string":"2019-01-29T09:30:00.000+08:00","key":1548725400000,"doc_count":18},{"key_as_string":"2019-01-29T09:31:00.000+08:00","key":1548725460000,"doc_count":18},{"key_as_string":"2019-01-29T09:32:00.000+08:00","key":1548725520000,"doc_count":394},{"key_as_string":"2019-01-29T09:33:00.000+08:00","key":1548725580000,"doc_count":18},{"key_as_string":"2019-01-29T09:34:00.000+08:00","key":1548725640000,"doc_count":18},{"key_as_string":"2019-01-29T09:35:00.000+08:00","key":1548725700000,"doc_count":18},{"key_as_string":"2019-01-29T09:36:00.000+08:00","key":1548725760000,"doc_count":18},{"key_as_string":"2019-01-29T09:37:00.000+08:00","key":1548725820000,"doc_count":18},{"key_as_string":"2019-01-29T09:38:00.000+08:00","key":1548725880000,"doc_count":18},{"key_as_string":"2019-01-29T09:39:00.000+08:00","key":1548725940000,"doc_count":18},{"key_as_string":"2019-01-29T09:40:00.000+08:00","key":1548726000000,"doc_count":18},{"key_as_string":"2019-01-29T09:41:00.000+08:00","key":1548726060000,"doc_count":31},{"key_as_string":"2019-01-29T09:42:00.000+08:00","key":1548726120000,"doc_count":36},{"key_as_string":"2019-01-29T09:43:00.000+08:00","key":1548726180000,"doc_count":51},{"key_as_string":"2019-01-29T09:44:00.000+08:00","key":1548726240000,"doc_count":24},{"key_as_string":"2019-01-29T09:45:00.000+08:00","key":1548726300000,"doc_count":18},{"key_as_string":"2019-01-29T09:46:00.000+08:00","key":1548726360000,"doc_count":20},{"key_as_string":"2019-01-29T09:47:00.000+08:00","key":1548726420000,"doc_count":18},{"key_as_string":"2019-01-29T09:48:00.000+08:00","key":1548726480000,"doc_count":18},{"key_as_string":"2019-01-29T09:49:00.000+08:00","key":1548726540000,"doc_count":18},{"key_as_string":"2019-01-29T09:50:00.000+08:00","key":1548726600000,"doc_count":26},{"key_as_string":"2019-01-29T09:51:00.000+08:00","key":1548726660000,"doc_count":18},{"key_as_string":"2019-01-29T09:52:00.000+08:00","key":1548726720000,"doc_count":18},{"key_as_string":"2019-01-29T09:53:00.000+08:00","key":1548726780000,"doc_count":24},{"key_as_string":"2019-01-29T09:54:00.000+08:00","key":1548726840000,"doc_count":20},{"key_as_string":"2019-01-29T09:55:00.000+08:00","key":1548726900000,"doc_count":22},{"key_as_string":"2019-01-29T09:56:00.000+08:00","key":1548726960000,"doc_count":20},{"key_as_string":"2019-01-29T09:57:00.000+08:00","key":1548727020000,"doc_count":18},{"key_as_string":"2019-01-29T09:58:00.000+08:00","key":1548727080000,"doc_count":18},{"key_as_string":"2019-01-29T09:59:00.000+08:00","key":1548727140000,"doc_count":18},{"key_as_string":"2019-01-29T10:00:00.000+08:00","key":1548727200000,"doc_count":20},{"key_as_string":"2019-01-29T10:01:00.000+08:00","key":1548727260000,"doc_count":12},{"key_as_string":"2019-01-29T10:02:00.000+08:00","key":1548727320000,"doc_count":427},{"key_as_string":"2019-01-29T10:03:00.000+08:00","key":1548727380000,"doc_count":12},{"key_as_string":"2019-01-29T10:04:00.000+08:00","key":1548727440000,"doc_count":12},{"key_as_string":"2019-01-29T10:05:00.000+08:00","key":1548727500000,"doc_count":12},{"key_as_string":"2019-01-29T10:23:00.000+08:00","key":1548728580000,"doc_count":14},{"key_as_string":"2019-01-29T10:24:00.000+08:00","key":1548728640000,"doc_count":18},{"key_as_string":"2019-01-29T10:25:00.000+08:00","key":1548728700000,"doc_count":18},{"key_as_string":"2019-01-29T10:26:00.000+08:00","key":1548728760000,"doc_count":18},{"key_as_string":"2019-01-29T10:27:00.000+08:00","key":1548728820000,"doc_count":18},{"key_as_string":"2019-01-29T10:28:00.000+08:00","key":1548728880000,"doc_count":18},{"key_as_string":"2019-01-29T10:29:00.000+08:00","key":1548728940000,"doc_count":18}]}}
         * status : 200
         */

        private Integer took;
        @JsonProperty("timed_out")
        private Boolean timedOut;
        @JsonProperty("_shards")
        private ShardsResponse shards;
        private HitsResponseX hits;
        private AggregationsResponse aggregations;
        private Integer status;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ShardsResponse {
            /**
             * total : 528
             * successful : 528
             * skipped : 480
             * failed : 0
             */

            private Integer total;
            private Integer successful;
            private Integer skipped;
            private Integer failed;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class HitsResponseX {
            /**
             * total : 2934
             * max_score : null
             * hits : [{"_index":"znjksyb-logs-2019.01.29","_type":"logs","_id":"y2lxl2gBCVUngXkeIaQW","_version":1,"_score":null,"_source":{"thread":"nioEventLoopGroup-5-6","@version":"1","@timestamp":"2019-01-29T02:29:59.178Z","fields":{"log_type":"znjksyb-great-device","project_name":"znjksyb-great-device","team_name":"ZNJKSYB","ip":"172.16.26.3"},"source":"/data/app_log/great-device/access.log","app_name":"great-device","offset":954367430,"msg":"-->:012510730526:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E  ","level":"INFO","time":"2019-01-29 10:29:59.107","tags":["znjksyb-great-device"],"type":"log","stack_trace":"","log_name":"common.utils.LogUtil"},"fields":{"@timestamp":["2019-01-29T02:29:59.178Z"]},"highlight":{"msg":["-->:@kibana-highlighted-field@012510730526@/kibana-highlighted-field@:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E"],"app_name":["@kibana-highlighted-field@great@/kibana-highlighted-field@-@kibana-highlighted-field@device@/kibana-highlighted-field@"],"log_name":["@kibana-highlighted-field@common.utils.LogUtil@/kibana-highlighted-field@"]},"sort":[1548728999178]}]
             */

            private Integer total;
            @JsonProperty("max_score")
            private Object maxScore;
            private List<HitsResponse> hits;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class HitsResponse {
                /**
                 * _index : znjksyb-logs-2019.01.29
                 * _type : logs
                 * _id : y2lxl2gBCVUngXkeIaQW
                 * _version : 1
                 * _score : null
                 * _source : {"thread":"nioEventLoopGroup-5-6","@version":"1","@timestamp":"2019-01-29T02:29:59.178Z","fields":{"log_type":"znjksyb-great-device","project_name":"znjksyb-great-device","team_name":"ZNJKSYB","ip":"172.16.26.3"},"source":"/data/app_log/great-device/access.log","app_name":"great-device","offset":954367430,"msg":"-->:012510730526:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E  ","level":"INFO","time":"2019-01-29 10:29:59.107","tags":["znjksyb-great-device"],"type":"log","stack_trace":"","log_name":"common.utils.LogUtil"}
                 * fields : {"@timestamp":["2019-01-29T02:29:59.178Z"]}
                 * highlight : {"msg":["-->:@kibana-highlighted-field@012510730526@/kibana-highlighted-field@:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E"],"app_name":["@kibana-highlighted-field@great@/kibana-highlighted-field@-@kibana-highlighted-field@device@/kibana-highlighted-field@"],"log_name":["@kibana-highlighted-field@common.utils.LogUtil@/kibana-highlighted-field@"]}
                 * sort : [1548728999178]
                 */

                @JsonProperty("_index")
                private String index;
                @JsonProperty("_type")
                private String type;
                @JsonProperty("_id")
                private String id;
                @JsonProperty("_version")
                private Integer version;
                @JsonProperty("_score")
                private Object score;
                @JsonProperty("_source")
                private SourceResponse source;
                private FieldsResponseX fields;
                private HighlightResponse highlight;
                private List<Long> sort;

                // FIXME generate failure  field @timestamp
// FIXME generate failure  field @version
                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class SourceResponse {
                    /**
                     * thread : nioEventLoopGroup-5-6
                     * @version : 1
                     * @timestamp : 2019-01-29T02:29:59.178Z
                     * fields : {"log_type":"znjksyb-great-device","project_name":"znjksyb-great-device","team_name":"ZNJKSYB","ip":"172.16.26.3"}
                     * source : /data/app_log/great-device/access.log
                     * app_name : great-device
                     * offset : 954367430
                     * msg : -->:012510730526:7E 80 01 00 05 01 25 10 73 05 26 00 3E 00 3C 00 02 00 E0 7E
                     * level : INFO
                     * time : 2019-01-29 10:29:59.107
                     * tags : ["znjksyb-great-device"]
                     * type : log
                     * stack_trace :
                     * log_name : common.utils.LogUtil
                     */

                    private String thread;
                    private FieldsResponse fields;
                    private String source;
                    @JsonProperty("app_name")
                    private String appName;
                    private Integer offset;
                    private String msg;
                    private String level;
                    private String time;
                    private String type;
                    @JsonProperty("stack_trace")
                    private String stackTrace;
                    @JsonProperty("log_name")
                    private String logName;
                    private List<String> tags;

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class FieldsResponse {
                        /**
                         * log_type : znjksyb-great-device
                         * project_name : znjksyb-great-device
                         * team_name : ZNJKSYB
                         * ip : 172.16.26.3
                         */

                        @JsonProperty("log_type")
                        private String logType;
                        @JsonProperty("project_name")
                        private String projectName;
                        @JsonProperty("team_name")
                        private String teamName;
                        private String ip;
                    }
                }

                // FIXME generate failure  field @timestamp
                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class FieldsResponseX {
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class HighlightResponse {
                    private List<String> msg;
                    @JsonProperty("app_name")
                    private List<String> appName;
                    @JsonProperty("log_name")
                    private List<String> logName;
                }
            }
        }

        // FIXME generate failure  field $2
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AggregationsResponse {
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class _$2Response {
                private List<BucketsResponse> buckets;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class BucketsResponse {
                    /**
                     * key_as_string : 2019-01-29T09:00:00.000+08:00
                     * key : 1548723600000
                     * doc_count : 18
                     */

                    @JsonProperty("key_as_string")
                    private String keyAsString;
                    private Long key;
                    @JsonProperty("doc_count")
                    private Integer docCount;
                }
            }
        }
    }
}
