package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 班次模板  运营类别,枚举
 */
public enum BusTplOprCategoryEnum {

	/** * 0 单营 */
	OPR_CATEGORY_0("0", "单营"),

	/** *1  共营 */
	OPR_CATEGORY_1("1", "共营");
	
	

    private String busType;
    private String desc;

    BusTplOprCategoryEnum(String busType, String desc) {
        this.busType = busType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (BusTplOprCategoryEnum t : BusTplOprCategoryEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, t.busType)) {
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
    public static boolean isIn(String type, BusTplOprCategoryEnum... idTypeEnums) {
        for (BusTplOprCategoryEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.busType)) {
                return true;
            }
        }
        return false;
    }

    public static BusTplOprCategoryEnum getEnum(String type) {
        for (BusTplOprCategoryEnum f : BusTplOprCategoryEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, f.busType)) {
                return f;
            }
        }
        return null;
    }

    public String getbusType() {
        return busType;
    }
    
    

    public String getDesc() {
		return desc;
	}


	public boolean equals(String busType) {
        return this.busType.equals(busType);
    }
}
