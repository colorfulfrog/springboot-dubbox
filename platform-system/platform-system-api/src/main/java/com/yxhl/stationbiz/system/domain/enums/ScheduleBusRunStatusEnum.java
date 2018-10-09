package com.yxhl.stationbiz.system.domain.enums;

/**
 * 发班状态 1已发班 0未发班 2班次完成
 */
public enum ScheduleBusRunStatusEnum {
    RUNNING(1, "已发班"),
    NOT_RUN(0, "未发班"),
	FINISHED(2, "班次完成");

    private int type;
    private String desc;

    ScheduleBusRunStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusRunStatusEnum t : ScheduleBusRunStatusEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusRunStatusEnum... enums) {
        for (ScheduleBusRunStatusEnum statusEnum : enums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusRunStatusEnum getEnum(int type) {
        for (ScheduleBusRunStatusEnum f : ScheduleBusRunStatusEnum.values()) {
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
