package com.yxhl.stationbiz.system.domain.enums;

/**
 * 座位状态 2可选 3预留 4不售 5已售
 */
public enum ScheduleBusSeatStatusEnum {
	OPTIONAL("2", "可选"),
	RESERVED("3", "预留"),
    NOT_SALE("4", "不售"),
    SALED("5", "已售");

    private String type;
    private String desc;

    ScheduleBusSeatStatusEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(String type) {
        for (ScheduleBusSeatStatusEnum t : ScheduleBusSeatStatusEnum.values()) {
            if (t.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(String type, ScheduleBusSeatStatusEnum... enums) {
        for (ScheduleBusSeatStatusEnum statusEnum : enums) {
            if (type.equals(statusEnum.getType())) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusSeatStatusEnum getEnum(String type) {
        for (ScheduleBusSeatStatusEnum f : ScheduleBusSeatStatusEnum.values()) {
            if (f.getType().equals(type)) {
                return f;
            }
        }
        return null;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean equals(String type) {
        return this.type.equals(type);
    }
}
