package com.yxhl.stationbiz.system.domain.enums;

/**
 * 售票状态
 */
public enum ScheduleBusSaleStatusEnum {
    REPORTED(1, "已报班"),
    NOT_REPORT(2, "未报班"),
    CANCLE(3, "已取消");

    private int type;
    private String desc;

    ScheduleBusSaleStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusSaleStatusEnum t : ScheduleBusSaleStatusEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusSaleStatusEnum... enums) {
        for (ScheduleBusSaleStatusEnum statusEnum : enums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusSaleStatusEnum getEnum(int type) {
        for (ScheduleBusSaleStatusEnum f : ScheduleBusSaleStatusEnum.values()) {
            if (f.getType() == type) {
                return f;
            }
        }
        return null;
    }

    public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean equals(int type) {
        return this.type == type;
    }
}
