package com.yxhl.stationbiz.web.consumer.excel;

import com.yxhl.easy.excel.config.FieldValue;
import com.yxhl.easy.excel.parsing.CellValueConverter;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusLoopTypeEnum;

/**
 * 循环类型转换
 * @author lw
 *
 */
public class ScheduleLoopTypeConverter  implements CellValueConverter {

	@Override
	public Object convert(Object bean, Object value, FieldValue fieldValue, Type type, int rowNum) throws Exception {
		String convertedValue = "未知"; //转换后的值
		if (String.valueOf(ScheduleBusLoopTypeEnum.DAILY.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.DAILY.getType());
		} else if (String.valueOf(ScheduleBusLoopTypeEnum.LUNAR_ODD.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.LUNAR_ODD.getType());
		} else if (String.valueOf(ScheduleBusLoopTypeEnum.LUNAR_EVEN.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.LUNAR_EVEN.getType());
		} else if (String.valueOf(ScheduleBusLoopTypeEnum.WEEKLY.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.WEEKLY.getType());
		} else if (String.valueOf(ScheduleBusLoopTypeEnum.EVERY_OTHER_WEEK.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.EVERY_OTHER_WEEK.getType());
		} else if (String.valueOf(ScheduleBusLoopTypeEnum.EVERY_OTHER_DAY.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.EVERY_OTHER_DAY.getType());
		} else if (String.valueOf(ScheduleBusLoopTypeEnum.MONTHLY.getType()).equals(value.toString())) {
			convertedValue = ScheduleBusLoopTypeEnum.getDescByType(ScheduleBusLoopTypeEnum.MONTHLY.getType());
		}
		return convertedValue;
	}

}
