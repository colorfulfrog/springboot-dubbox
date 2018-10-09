package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 班次模板   运行区域,枚举
 */
public enum BusTplRunAreaEnum {

	/** *0 县内 */
	RUN_AREA_0("0", "县内"),
	/** *1 县际 */
	RUN_AREA_1("1", "县际"),
	/** *2 市际*/
	RUN_AREA_2("2", "市际"),
	/** *3 省际 */
	RUN_AREA_3("3", "省际");
	
	

    private String busType;
    private String desc;

    BusTplRunAreaEnum(String busType, String desc) {
        this.busType = busType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (BusTplRunAreaEnum t : BusTplRunAreaEnum.values()) {
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
    public static boolean isIn(String type, BusTplRunAreaEnum... idTypeEnums) {
        for (BusTplRunAreaEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.busType)) {
                return true;
            }
        }
        return false;
    }

    public static BusTplRunAreaEnum getEnum(String type) {
        for (BusTplRunAreaEnum f : BusTplRunAreaEnum.values()) {
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
