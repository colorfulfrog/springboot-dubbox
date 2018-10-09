package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 班次模板  营运方式,枚举
 */
public enum BusTplOprModeEnum {

	/** * 0  直达 */
	OPR_MODE_0("0", "直达"),
	/** *1 城乡公交 */
	OPR_MODE_1("1", "城乡公交"),
	/** *2  普通 */
	OPR_MODE_2("2", "普通");
	
	

    private String busType;
    private String desc;

    BusTplOprModeEnum(String busType, String desc) {
        this.busType = busType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (BusTplOprModeEnum t : BusTplOprModeEnum.values()) {
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
    public static boolean isIn(String type, BusTplOprModeEnum... idTypeEnums) {
        for (BusTplOprModeEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.busType)) {
                return true;
            }
        }
        return false;
    }

    public static BusTplOprModeEnum getEnum(String type) {
        for (BusTplOprModeEnum f : BusTplOprModeEnum.values()) {
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
