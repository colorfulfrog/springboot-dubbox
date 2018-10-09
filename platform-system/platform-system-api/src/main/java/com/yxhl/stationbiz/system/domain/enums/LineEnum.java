package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 线路售票种类,枚举
 */
public enum LineEnum {

	/** * 区域类别 */
	REGION_TYPE("region_type", "区域类别"),
	/** * 线路方向 */
	LINE_DIRECTION("line_directionvarchar", "线路方向"),
	/** * 线路等级 */
	LINE_LEVE("line_levelint", "线路等级");

    private String lineType;
    private String desc;

    LineEnum(String lineType, String desc) {
        this.lineType = lineType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (LineEnum t : LineEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, t.lineType)) {
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
    public static boolean isIn(String type, LineEnum... idTypeEnums) {
        for (LineEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.lineType)) {
                return true;
            }
        }
        return false;
    }

    public static LineEnum getEnum(String type) {
        for (LineEnum f : LineEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, f.lineType)) {
                return f;
            }
        }
        return null;
    }

    public String getLineType() {
        return lineType;
    }
    
    

    public String getDesc() {
		return desc;
	}


	public boolean equals(String lineType) {
        return this.lineType.equals(lineType);
    }
}
