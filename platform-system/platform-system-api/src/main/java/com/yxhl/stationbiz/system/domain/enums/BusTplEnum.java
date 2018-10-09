package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 班次模板,枚举
 */
public enum BusTplEnum {

	/** * 运营类别 */
	OPR_CATEGORYVARCHAR("opr_categoryvarchar", "运营类别"),
	/** * 营运方式 */
	LINE_DIRECTIONVARCHAR("opr_modevarchar", "营运方式"),
	/** * 班次类型 */
	BUS_TYPEVARCHAR("bus_typevarchar", "班次类型"),
	/** * 运行区域 */
	RUN_AREAVARCHAR("run_areavarchar", "运行区域"),
	/** * 班次状态 */
	BUS_STATUS("bus_status", "班次状态");
	
	

    private String busType;
    private String desc;

    BusTplEnum(String busType, String desc) {
        this.busType = busType;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (BusTplEnum t : BusTplEnum.values()) {
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
    public static boolean isIn(String type, BusTplEnum... idTypeEnums) {
        for (BusTplEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.busType)) {
                return true;
            }
        }
        return false;
    }

    public static BusTplEnum getEnum(String type) {
        for (BusTplEnum f : BusTplEnum.values()) {
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
