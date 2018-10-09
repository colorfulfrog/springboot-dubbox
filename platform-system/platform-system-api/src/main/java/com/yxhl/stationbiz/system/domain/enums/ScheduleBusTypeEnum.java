package com.yxhl.stationbiz.system.domain.enums;

/**
 * 班次类型 1固定班 2流水班
 */
public enum ScheduleBusTypeEnum {
    STABLE(1, "固定班"),
    FLOW(2, "流水班");

    private int type;
    private String desc;

    ScheduleBusTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusTypeEnum t : ScheduleBusTypeEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusTypeEnum... enums) {
        for (ScheduleBusTypeEnum statusEnum : enums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusTypeEnum getEnum(int type) {
        for (ScheduleBusTypeEnum f : ScheduleBusTypeEnum.values()) {
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
