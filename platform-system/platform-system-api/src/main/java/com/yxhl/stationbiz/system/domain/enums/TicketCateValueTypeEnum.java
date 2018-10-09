package com.yxhl.stationbiz.system.domain.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 线路售票种类,枚举
 */
public enum TicketCateValueTypeEnum {

	/** * 基础票价类型 */
	BASIC_TYPE("1", "基础票价类型"),
	/** * 节假日票价类型  */
	HOLIDAY_TYPE("2", "节假日票价类型"),
	/** * 执行票价类型  */
	EXEC_TYPE("3", "执行票价类型");

    private String value;
    private String desc;

    TicketCateValueTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    public static boolean isIn(String type) {
        for (TicketCateValueTypeEnum t : TicketCateValueTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, t.value)) {
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
    public static boolean isIn(String type, TicketCateValueTypeEnum... idTypeEnums) {
        for (TicketCateValueTypeEnum typeEnum : idTypeEnums) {
            if (StringUtils.equalsIgnoreCase(type, typeEnum.value)) {
                return true;
            }
        }
        return false;
    }

    public static TicketCateValueTypeEnum getEnum(String type) {
        for (TicketCateValueTypeEnum f : TicketCateValueTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type, f.value)) {
                return f;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
    
    

    public String getDesc() {
		return desc;
	}


	public boolean equals(String value) {
        return this.value.equals(value);
    }
}
