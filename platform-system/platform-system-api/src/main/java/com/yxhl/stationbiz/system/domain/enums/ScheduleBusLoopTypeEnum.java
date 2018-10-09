package com.yxhl.stationbiz.system.domain.enums;

/**
 * 班次循环类型
 * 0每日、1农历单、2农历双、3每周、4隔周、5隔日、6月班
 */
public enum ScheduleBusLoopTypeEnum {
    DAILY(0, "每日"),
    LUNAR_ODD(1, "农历单"),
    LUNAR_EVEN(2, "农历双"),
    WEEKLY(3, "每周"),
    EVERY_OTHER_WEEK(4, "隔周"),
    EVERY_OTHER_DAY(5, "隔日"),
    MONTHLY(6, "月班");

    private int type;
    private String desc;

    ScheduleBusLoopTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static boolean isIn(int type) {
        for (ScheduleBusLoopTypeEnum t : ScheduleBusLoopTypeEnum.values()) {
            if (t.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(int type, ScheduleBusLoopTypeEnum... scheduleBusLoopTypeEnums) {
        for (ScheduleBusLoopTypeEnum statusEnum : scheduleBusLoopTypeEnums) {
            if (type == statusEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ScheduleBusLoopTypeEnum getEnum(int type) {
        for (ScheduleBusLoopTypeEnum f : ScheduleBusLoopTypeEnum.values()) {
            if (f.getType() == type) {
                return f;
            }
        }
        return null;
    }
    
    /**
     * 根据type获取desc
     * @param type
     * @return
     */
    public static String getDescByType(int type) {
    	for (ScheduleBusLoopTypeEnum f : ScheduleBusLoopTypeEnum.values()) {
    		if (f.getType() == type) {
    			return f.getDesc();
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
	
	public static void main(String[] args) {
		System.out.println(ScheduleBusLoopTypeEnum.getDescByType(0));
	}
}
