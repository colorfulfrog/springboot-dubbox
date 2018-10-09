package com.yxhl.stationbiz.web.consumer.controller.schedule;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.schedule.ExecPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.enums.TicketCateValueTypeEnum;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCateValueService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *	
 *  执行票价表控制器
 *  创建人: lw
 *  创建日期:2018-8-21 10:23:17
 */
@RestController
@Api(tags = "执行票价表")
@RequestMapping(value = "/m")
public class ExecPriceController extends BaseController{
	
	
    private static final Logger logger = LoggerFactory.getLogger(ExecPriceController.class);
    @Autowired
    private ExecPriceService execPriceService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
	@Autowired
	private HolidayPriceService holidayPriceService;
	
	@Autowired
	private TicketCateValueService ticketCateValueService;
    

	@ApiOperation(value = "修改执行票价表", notes = "修改执行票价表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/execPrice/{priceId}/{rowid}/{ticketCateId}/{ticketValue}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateExecPrice(@PathVariable("priceId") String priceId,@PathVariable("rowid") String rowid,@PathVariable("ticketCateId") String ticketCateId,@PathVariable("ticketValue") Float ticketValue) throws Exception {
		
		checkNotNull(priceId,"参数错误");
		checkNotNull(ticketCateId,"参数错误");
		checkNotNull(ticketValue,"参数错误");
		checkNotNull(rowid,"参数错误");
		logger.info("修改执行票价表:priceId:"+priceId+",ticketCateId:"+ticketCateId+",ticketValue:"+ticketValue+",rowid:"+rowid);
		boolean isUpdated=false;
		if(StringUtils.equals(priceId, rowid)) {
			Wrapper<TicketCateValue> wrapper = new EntityWrapper<TicketCateValue>();
			wrapper.like("ticket_cate_id", ticketCateId);
			wrapper.like("price_id", priceId);
			wrapper.like("price_tbl_type", TicketCateValueTypeEnum.EXEC_TYPE.getValue());
			TicketCateValue ticketCateValue = ticketCateValueService.selectOne(wrapper);
			if(Util.isNull(ticketCateValue)) {
				ELUser user= getLoginUser();
				ticketCateValue=new TicketCateValue();
				ticketCateValue.setId(IdWorker.get32UUID());
				ticketCateValue.setTicketCateId(ticketCateId);
				ticketCateValue.setPriceId(rowid);
				ticketCateValue.setTicketValue(ticketValue);
				ticketCateValue.setPriceTblType(TicketCateValueTypeEnum.EXEC_TYPE.getValue());
				ticketCateValue.setCompId(user.getCompanyId());
				ticketCateValue.setOrgId(user.getOrgId());
				isUpdated =ticketCateValueService.insert(ticketCateValue);
			}else {
				ticketCateValue.setTicketValue(ticketValue);
				isUpdated = ticketCateValueService.updateById(ticketCateValue);
			}
		}
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.EXEC_PRICE.getModule(),"修改执行票价",getRemoteAddr(),getLoginUser().getUserName()+"修改了执行票价",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	
	
	@ApiOperation(value = "查询班次的执行票价", notes = "查询班次的执行票价信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/execPrice/selExecPrice/{scheduleBusId}", produces = "application/json;charset=UTF-8")
	public CommonResponse selExecPrice(@PathVariable("scheduleBusId") String scheduleBusId) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		ELUser loginUser = getLoginUser();
		List<ExecPrice> execPriceList = execPriceService.selExecPrice(scheduleBusId);
		List<String> ids=Lists.newArrayList();
		List<HolidayPriceResponse> list =Lists.newArrayList();
		for(ExecPrice execPrice:execPriceList) {
			ids.add(execPrice.getId());
		}
		if(Util.isNotNull(ids)) {
			list=holidayPriceService.dynamic(ids, TicketCateValueTypeEnum.EXEC_TYPE.getValue(), loginUser.getOrgId(), loginUser.getCompanyId());
		}
		List<Dictionary> seatCategoryList = dictionaryService.getRediesByKey("seat_category"); //座位类型
		resp.setData(execPriceList);
		resp.extendsRes("dynamic", list);
		resp.extendsRes("seat_category", seatCategoryList);
		return resp;
	}
	
}
