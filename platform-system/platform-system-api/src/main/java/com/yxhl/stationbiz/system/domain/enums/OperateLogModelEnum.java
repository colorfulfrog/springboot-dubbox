package com.yxhl.stationbiz.system.domain.enums;

/**
 * 操作日志-模块枚举
 */
public enum OperateLogModelEnum {
	/*基础资料 begin*/
    REGION("区域管理"),
    STATION("站点管理"),
	LINE("线路管理"),
	DRIVER("驾驶员管理"),
	VEHICLE("车辆管理"),
	DRIVER_VEHICLE_BIND("驾驶员绑定车辆"),
	TICKET_GATE("检票口"),
	BUS_ENTRANCE("乘车库"),
	BUS_TPL("班次管理"),
	BUS_LOOP("班次循环"),
	EXEC_PRICE("班次执行票价"),
	/*基础资料 end*/
	
	/*系统配置 begin*/
    CONFIG("参数设置"),
    OPERATELOG("操作日志"),
	RESOURCE("菜单管理"),
	ROLE("角色管理"),
	PERMISSION("权限管理"),
	USER("用户管理"),
	ORGANIZATION("机构管理"),
	COMPANY("单位管理"),
	/*系统配置 end*/
	
	/*安检 begin*/
	VEHICLEINSTATION("车辆进站"),
	VEHICLESECURUTYCHECK("车辆安检"),
	/*安检 end*/
	
	/*调度begin*/
	TICKETCATEGROY("票种设置"),
	BASICPRICE("基础票价"),
	HOLIDAY("节日信息设置"),
	HOLIDAYPRICE("节日票价信息设置"),
	PRICE("票价值管理"),
	SCHEDULEBUS("排版管理"),
	DISPATCH("综合调度"),
	VEHICLEBUS("车辆报班"),
	/*调度 end*/
	
	/*售票begin*/
	WINDOW_TICKETING("窗口售票"),
	INTERNET_TICKETING("互联网售票"),
	REFUND_TICKET("退票"),
	BAD_TICKET("废票"),
	CHANGE_TICKET("改签"),
	REPLACEMENT_TICKET("补票"),
	PAYMENT("缴款"),
	/*售票 end*/
	
	/*检票begin*/
	CHECK_TICKET("班次检票"),
	VEHICLE_OUTBOUND("车辆出站"),
	INSPECTION_MANAGEMENT("稽查管理"),
	SETTLEMENT_MANAGEMENT("结算单管理");
	/*检票 end*/
	
    private String module;

    OperateLogModelEnum(String module) {
        this.module = module;
    }

    public static boolean isIn(String module) {
        for (OperateLogModelEnum t : OperateLogModelEnum.values()) {
            if (t.getModule() == module) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(String module, OperateLogModelEnum... enums) {
        for (OperateLogModelEnum statusEnum : enums) {
            if (statusEnum.getModule().equals(module)) {
                return true;
            }
        }
        return false;
    }

    public static OperateLogModelEnum getEnum(String module) {
        for (OperateLogModelEnum f : OperateLogModelEnum.values()) {
            if (f.getModule().equalsIgnoreCase(module)) {
                return f;
            }
        }
        return null;
    }

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public boolean equals(String module) {
        return this.module == module;
    }
}
