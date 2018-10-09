package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 线路区域类别,枚举
 */
public enum LineDirectionEnum {

	/** *    东 */
	LINE_DIRECTIONVARCHAR_0("东", "东"),
	/** *    南 */
	LINE_DIRECTIONVARCHAR_1("南", "南"),
	/** *  西 */
	LINE_DIRECTIONVARCHAR_2("西", "西"),
	/** * 北 */
	LINE_DIRECTIONVARCHAR_3("北", "北"),
	/** *  东北 */
	LINE_DIRECTIONVARCHAR_4("东北", "东北"),
	/** *  东南 */
	LINE_DIRECTIONVARCHAR_5("东南", "东南"),
	/** *  西南 */
	LINE_DIRECTIONVARCHAR_6("西南", "西南"),
	/** *   西北 */
	LINE_DIRECTIONVARCHAR_7("西北", "西北");

    private String direct;
    private String desc;

    LineDirectionEnum(String direct, String desc) {
        this.direct = direct;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (LineDirectionEnum t : LineDirectionEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, t.direct)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断type是否在指定的typeEnum范围中
     *
     * @param type
     * @param idTypeEnums
     * @return
     */
    public static boolean isIn(String type, LineDirectionEnum... idTypeEnums) {
        for (LineDirectionEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.direct)) {
                return true;
            }
        }
        return false;
    }

    public static LineDirectionEnum getEnum(String type) {
        for (LineDirectionEnum f : LineDirectionEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, f.direct)) {
                return f;
            }
        }
        return null;
    }

    public String getdirect() {
        return direct;
    }

    public boolean equals(String direct) {
        return this.direct.equals(direct);
    }


	public String getDesc() {
		return desc;
	}
    
    
}
