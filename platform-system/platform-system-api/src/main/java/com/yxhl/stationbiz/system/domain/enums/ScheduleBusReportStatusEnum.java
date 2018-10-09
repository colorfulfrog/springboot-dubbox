package com.yxhl.stationbiz.system.domain.enums;

/**
 * 报班状态 1已报班 2未报班 3已取消
 */
public enum ScheduleBusReportStatusEnum {
    REPORTED(1, "已报班"),
    NOT_REPORT(2, "未报班"),
    CANCLE(3, "已取消");

    private int type;
    private String desc;

    ScheduleBusReportStatusEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusReportStatusEnum t : ScheduleBusReportStatusEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusReportStatusEnum... enums) {
        for (ScheduleBusReportStatusEnum statusEnum : enums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusReportStatusEnum getEnum(int type) {
        for (ScheduleBusReportStatusEnum f : ScheduleBusReportStatusEnum.values()) {
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
