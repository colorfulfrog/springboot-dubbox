package com.yxhl.stationbiz.system.domain.enums;

/**
 * 检票状态 1开检 2未检
 */
public enum ScheduleBusCheckStatusEnum {
    START_CHECK(1, "开检"),
    NOT_CHECK(2, "未检");

    private int type;
    private String desc;

    ScheduleBusCheckStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusCheckStatusEnum t : ScheduleBusCheckStatusEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusCheckStatusEnum... enums) {
        for (ScheduleBusCheckStatusEnum statusEnum : enums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusCheckStatusEnum getEnum(int type) {
        for (ScheduleBusCheckStatusEnum f : ScheduleBusCheckStatusEnum.values()) {
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
