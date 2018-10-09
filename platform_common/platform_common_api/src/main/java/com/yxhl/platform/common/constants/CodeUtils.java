package com.yxhl.platform.common.constants;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;
import com.yxhl.platform.common.utils.JsonMapper;
import com.yxhl.platform.common.utils.StringHelper;

/**
 * CodeUtils
 *
 */
public class CodeUtils {


    private CodeUtils() {
    }

    // code
    public static final String SUCCESS_CODE = "200"; // 成功
    public static final String FAIL_CODE = "999"; // 失败
    public static final String BAD_REQUEST="400"; //Bad Request
    public static final String EXCEPTION_CODE = "11000"; // 异常
    public static final String SYSTEM_BUSY_CODE = "11011"; // 系统繁忙，接口熔断
    public static final String EMPTY_CODE = "10001"; // XXX字段数据为空
    public static final String TOO_LONG_CODE = "10002";// XXX字段太长
    public static final String API_FREQ_CODE = "10003"; // 接口调动频率超过限制
    public static final String EMPTY_LIST_CODE = "10004"; // 空白的列表
    public static final String INVALID_CODE = "10005"; // 无效的字段
    public static final String EMPTY_DAT_CODE = "10006"; // 空白的单条数据
    public static final String DATA_DUPLICATION_CODE = "10008"; // 数据重复
    public static final String PAGENO_INVALID_CODE = "10009"; // 当前页数值错误
    // 范围：0～totalpage
    public static final String PAGESIZE_INVALID_CODE = "10010"; // 每页数量错误 范围
    // 0～100
    public static final String INTEGER_CODE = "10011"; // int 类型格式的字段；
    public static final String INTEGER_RANGE = "10012"; // INT类型范围
    public static final String DATA_WRONGFULNESS_CODE = "10013"; // 数据不合法
    public static final String IP_ERROR_CODE = "10014"; // ip地址格式错误
    public static final String DATETIME_ERROR_CODE = "10015"; // 日期格式错误
    public static final String LONG_RANG = "10016";
    public static final String EN_ERROR_CODE = "10017";
    public static final String ZH_ERROR_CODE = "10018";
    public static final String ID_ERROR_CODE = "10019";// id格式错误
    public static final String MIN1_TO_127_CODE = "10020";// [-1,127]
    public static final String ZERO_ONE_ERROR_CODE = "10021";
    public static final String VERSION_ERROR_CODE = "10022";
    public static final String MIN1_TO_16_CODE = "10023";
    public static final String SIBLING_ERROR_CODE = "10024";
    public static final String ZERO_TO_127_CODE = "10025";
    public static final String ZERO_TO_9999_CODE = "10026";
    public static final String DATA_NOT_EXIST_CODE = "10027";
    public static final String STATUS_ERROR_CODE = "10028";// stutus格式错误
    public static final String TYPE_ERROR_CODE = "10029";// type格式错误
    public static final String DATE_ERROR_CODE = "10030";// 时间格式错误
    public static final String TYPE_ERROR_CODE_ZERO_ONE = "10031";// 格式错误
    public static final String TYPE_ERROR_CODE_ZERO_ONE_TWO = "10032";// TYPE格式错误
    public static final String ID_MIN1_ERROR_CODE = "10033";// id格式错误,应该是[0-999..]或-1
    public static final String IDS_ERROR_CODE = "10034";// ids格式错误
    public static final String APPOINT_REPEAT_ERROR_CODE = "10035";// 重复预约
    public static final String DATE_IS_ERROR_CODE = "10036";// 日期格式错误
    public static final String DATE_ERROR_CODE_HOUR_SECOND = "10037";// 时间格式错误
    public static final String DOUBLE_RANGE = "10042";
    public static final String PRIMARY_KEY_DUPLICATE_ERROR_CODE = "1051";// 主键重复
    public static final String PRIMARY_KEY_DUPLICATE_ERROR_MSG = "主键id重复";
    public static final String NOT_ONE_TO_ONE_CODE = "10050";
    public static final String DUPLICATE_UNIQUE_KEY_CODE = "10051";
    public static final String DUPLICATE_UNIQUE_KEY_MSG = "数据库唯一性约束冲突";
    public static final String ACCESS_DATA_LIMIT_CODE = "10052";

    // message
    public static final String SUCCESS_MSG = "success"; // 成功
    public static final String FAIL_MSG = "failure"; // 失败
    public static final String DATA_WRONGFULNESS_MSG = "数据不合法"; // 数据不合法
    public static final String EXCEPTION_MSG = "系统异常"; // 异常
    public static final String EMPTY_MSG = "字段为空 "; // XXX字段数据为空
    public static final String EMPTY_LIST_MSG = "列表数据为空"; // 空白的列表
    public static final String EMPTY_DAT_MSG = "空白的单条数据"; // 空白的单条数据
    public static final String DATA_DUPLICATION_MSG = "数据重复"; // 数据重复
    public static final String TOO_LONG_MSG = "字段过长 ";// XXX字段太长
    public static final String PAGENO_INVALID_MSG = "pageNo range 1~totalpage";
    public static final String PAGESIZE_INVALID_MSG = "pageSize range 1~100";
    public static final String INTEGER_MSG = " is int";
    public static final String INTEGER_RANGE_MSG = "  int rang is 0~2147483647";
    public static final String LONG_RANG_MSG = "  long rang is 0~9223372036854775807";

    public static final String IP_ERROR_MSG = " format error"; // ip地址格式错误
    public static final String DATETIME_ERROR_MSG = " format error"; // 日期格式错误
    public static final String EN_ERROR_MSG = "must enter English";
    public static final String ZH_ERROR_MSG = "must enter Chinese";
    public static final String ID_ERROR_MSG = "should range 0~999999999999999999";
    public static final String MIN1_TO_127_MSG = "should range -1~127";
    public static final String ZERO_ONE_ERROR_MSG = "should be 0 or 1";
    public static final String VERSION_ERROR_MSG = "should be -1 or range 0~999999999";
    public static final String MIN1_TO_16_MSG = "should range -1~16";
    public static final String SIBLING_ERROR_MSG = "should be -1 or range 0~999999999";
    public static final String ZERO_TO_127_MSG = "should range 0~127";
    public static final String POSITIVE_INTEGER_RANGE_MSG_ = "  int rang is 1~2147483647";
    public static final String ZERO_TO_9999_MSG = "should range 0~9999";
    public static final String DATA_NOT_EXIST_MSG = "查询的数据不存在";
    public static final String DATA_STATUS_ERROR_MSG = " range is 0~10";
    public static final String DATA_TYPE_ERROR_MSG = " range is 1~999999999";
    public static final String DATE_TYPE_ERROR_MSG = " type is yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TYPE_ERROR_ONE_ZERO_MSG = "  is 0 or 1";
    public static final String DATE_TYPE_ERROR_ONE_ZERO_TWO_MSG = "  is 0 or 1 or 2";
    public static final String IDS_ERROR__MSG = "  应该以逗号分割，data should range 0~999999999999999999";
    public static final String ID_MIN1_ERROR_MSG = "should range -1~999999999999999999";
    public static final String DATE_IS_ERROR_MSG = "type is yyyy-MM-dd";
    public static final String DATE_HOUR_SECOND_ERROR_MSG = "should HH:mm";
    public static final String APPOINT_REPEAT_ERROR_MSG = "appoint repeat";
    public static final String APPOINT_COST_ERROR_MSG = "cost point failure";
    public static final String APPOINT_FULL_ERROR_MSG = "course is full";
    public static final String APPOINT_CANCELED_ERROR_MSG = "course was cancened";
    public static final String APPOINT_NOROOM_ERROR_MSG = "room not exist";
    public static final String DOUBLE_RANGE_ERROR_MSG = "should range is double example:1.00";
    public static final String MIN_MAX_RANGE_ERROR_MSG = "min 应该小于 max";
    public static final String ZERO_NINETYNINE_ERROR_MSG = "should range 0~99";
    public static final String LOW_HIGH_RANGE_ERROR_MSG = "low 应该小于 high";
    public static final String TAGS_IDS_ERROR_MSG = "应该以逗号分隔，范围0到99";
    public static final String INTEGER_ONE_MSG = "  int rang is 1~2147483647";
    public static final String DOUBLE_RANGE_MSG = "  不能大于99.9";
    public static final String ONE_TEN_REEOR_MSG = " 只能为1~10";
    public static final String ONE_NINETYNINE_ERROR_MSG = "should range 1~99";
    public static final String ID_ONE_REEOR_MSG = " should range 1~999999999999999999";
    public static final String TIME_LEN_REEOR_MSG = " should range 1~999";

    public static final String ACCESS_DATA_LIMIT_MSG="没有数据操作权限";
    public static final String ACCESS_DATA_LIMIT_MSG_FORMAT=ACCESS_DATA_LIMIT_MSG + " [%s : %s]";
    /**
     * 校验ip是否合法
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIpv4(String ipAddress) {

        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();

    }

    public static void main(String[] args) {
        System.out.println(isIpv4("1.1.1.101"));
        System.out.println(toErrorMessage("10001", "123"));
    }

    public static String toErrorMessage(String errorCode, String field) {
        Map<String, String> map = Maps.newConcurrentMap();
        //将所有非蛇形的属性名称转换为蛇形的形式返回
        field = StringHelper.toUnderScoreCase(field);
        switch (errorCode) {
            case CodeUtils.EXCEPTION_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.EXCEPTION_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.EMPTY_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.EMPTY_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.TOO_LONG_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.TOO_LONG_MSG);
                return JsonMapper.toJsonString(map);


            case CodeUtils.PAGENO_INVALID_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.PAGENO_INVALID_MSG);
                return JsonMapper.toJsonString(map);

            case CodeUtils.PAGESIZE_INVALID_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.PAGESIZE_INVALID_MSG);
                return JsonMapper.toJsonString(map);

            case CodeUtils.INTEGER_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.INTEGER_MSG);
                return JsonMapper.toJsonString(map);

            case CodeUtils.INTEGER_RANGE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.INTEGER_RANGE_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.DATA_WRONGFULNESS_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATA_WRONGFULNESS_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.IP_ERROR_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.IP_ERROR_MSG);
                return JsonMapper.toJsonString(map);

            case CodeUtils.DATETIME_ERROR_CODE:

                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATETIME_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.LONG_RANG:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.LONG_RANG_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.EN_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.EN_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.ZH_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.ZH_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.ID_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.ID_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.MIN1_TO_127_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.MIN1_TO_127_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.ZERO_ONE_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.ZERO_ONE_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.VERSION_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.VERSION_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.MIN1_TO_16_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.MIN1_TO_16_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.SIBLING_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.SIBLING_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.ZERO_TO_127_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.ZERO_TO_127_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.ZERO_TO_9999_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.ZERO_TO_9999_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.DATA_NOT_EXIST_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATA_NOT_EXIST_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.STATUS_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATA_STATUS_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.TYPE_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATA_TYPE_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.DATE_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATE_TYPE_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.TYPE_ERROR_CODE_ZERO_ONE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATE_TYPE_ERROR_ONE_ZERO_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.TYPE_ERROR_CODE_ZERO_ONE_TWO:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATE_TYPE_ERROR_ONE_ZERO_TWO_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.IDS_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.IDS_ERROR__MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.DATE_ERROR_CODE_HOUR_SECOND:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATE_HOUR_SECOND_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.ID_MIN1_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.ID_MIN1_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.APPOINT_REPEAT_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.APPOINT_REPEAT_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.DATE_IS_ERROR_CODE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DATE_IS_ERROR_MSG);
                return JsonMapper.toJsonString(map);
            case CodeUtils.DOUBLE_RANGE:
                map.put("code", errorCode);
                map.put("message", field + " " + CodeUtils.DOUBLE_RANGE_ERROR_MSG);
                return JsonMapper.toJsonString(map);

            default:
                map.put("code", DATA_WRONGFULNESS_CODE);
                map.put("message", field + " " + CodeUtils.DATA_WRONGFULNESS_MSG);
                return JsonMapper.toJsonString(map);
        }
    }
}
