package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 线路区域类别,枚举
 */
public enum LineRegionTypeEnum {

	/** * 0 县内 */
	REGION_TYPE_0("0", "县内"),
	/** * 1 县际 */
	REGION_TYPE_1("1", "县际"),
	/** * 2 市际 */
	REGION_TYPE_2("2", "市际"),
	/** * 3  省际 */
	REGION_TYPE_3("3", "省际");

    private String lineType;
    private String desc;

    LineRegionTypeEnum(String lineType, String desc) {
        this.lineType = lineType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (LineRegionTypeEnum t : LineRegionTypeEnum.values()) {
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
    public static boolean isIn(String type, LineRegionTypeEnum... idTypeEnums) {
        for (LineRegionTypeEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.lineType)) {
                return true;
            }
        }
        return false;
    }

    public static LineRegionTypeEnum getEnum(String type) {
        for (LineRegionTypeEnum f : LineRegionTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, f.lineType)) {
                return f;
            }
        }
        return null;
    }

    public String getLineType() {
        return lineType;
    }

    public boolean equals(String lineType) {
        return this.lineType.equals(lineType);
    }


	public String getDesc() {
		return desc;
	}
    
    
}
