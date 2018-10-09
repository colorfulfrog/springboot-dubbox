package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 线路等级,枚举
 */
public enum LineLevelEnum {

	/** * 0 一类客运班线 */
	LINE_LEVEL_0("0", "一类客运班线"),
	/** * 1 二类客运班线 */
	LINE_LEVEL_1("1", "二类客运班线"),
	/** * 2 三类客运班线 */
	LINE_LEVEL_2("2", "三类客运班线"),
	/** * 3  四类客运班线 */
	LINE_LEVEL_3("3", "四类客运班线");

    private String lineLevel;
    private String desc;

    LineLevelEnum(String lineLevel, String desc) {
        this.lineLevel = lineLevel;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (LineLevelEnum t : LineLevelEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, t.lineLevel)) {
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
    public static boolean isIn(String type, LineLevelEnum... idTypeEnums) {
        for (LineLevelEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.lineLevel)) {
                return true;
            }
        }
        return false;
    }

    public static LineLevelEnum getEnum(String type) {
        for (LineLevelEnum f : LineLevelEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, f.lineLevel)) {
                return f;
            }
        }
        return null;
    }

    public String getlineLevel() {
        return lineLevel;
    }

    public boolean equals(String lineLevel) {
        return this.lineLevel.equals(lineLevel);
    }


	public String getDesc() {
		return desc;
	}
    
    
}
