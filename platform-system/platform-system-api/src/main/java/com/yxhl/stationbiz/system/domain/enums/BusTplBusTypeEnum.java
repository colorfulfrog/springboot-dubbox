package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 班次类型,枚举
 */
public enum BusTplBusTypeEnum {

	/** * 运营类别 */
	BUS_TYPE_0("0", "普通"),
	/** * 营运方式 */
	BUS_TYPE_1("1", "豪华"),
	/** * 班次类型 */
	BUS_TYPE_2("2", "卧铺"),
	/** * 班次状态 */
	BUS_TYPE_3("3", "快车");
	
	

    private String busType;
    private String desc;

    BusTplBusTypeEnum(String busType, String desc) {
        this.busType = busType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (BusTplBusTypeEnum t : BusTplBusTypeEnum.values()) {
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
    public static boolean isIn(String type, BusTplBusTypeEnum... idTypeEnums) {
        for (BusTplBusTypeEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.busType)) {
                return true;
            }
        }
        return false;
    }

    public static BusTplBusTypeEnum getEnum(String type) {
        for (BusTplBusTypeEnum f : BusTplBusTypeEnum.values()) {
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
