package com.yxhl.stationbiz.system.domain.enums;

/**
 * 班次状态1正常 2停班 3被并/停班 4并班 5配载 6被配载 
 */
public enum ScheduleBusStatusEnum {
    NORMAL(1, "正常"),
    STOP(2, "停班"),
    BE_MERGED(3, "被并/停班"),
    MERGE(4, "并班"),
    STOWAGE(5, "配载"),
    BE_STOWAGED(6, "被配载");

    private int type;
    private String desc;

    ScheduleBusStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusStatusEnum t : ScheduleBusStatusEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusStatusEnum... enums) {
        for (ScheduleBusStatusEnum statusEnum : enums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusStatusEnum getEnum(int type) {
        for (ScheduleBusStatusEnum f : ScheduleBusStatusEnum.values()) {
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
