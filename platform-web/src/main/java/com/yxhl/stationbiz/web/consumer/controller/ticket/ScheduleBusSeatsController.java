package com.yxhl.stationbiz.web.consumer.controller.ticket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.redis.util.RedisLockUtil;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.constants.SysConfigConstant;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.ticket.ScheduleBusSeatsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  班次座位表控制器
 *  创建人: lw
 *  创建日期:2018-9-13 9:57:59
 */
@RestController
@Api(tags = "班次座位表")
@RequestMapping(value = "/m")
public class ScheduleBusSeatsController extends BaseController{
	
    @Autowired
    private ScheduleBusSeatsService scheduleBusSeatsService;
    //@Autowired
	//private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private RedisLockUtil lock;
    @Autowired
    private ConfigService configService;
    
    @ApiOperation(value = "修改班次座位状态", notes = "修改班次座位状态", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/scheduleBusSeats/{scheduleBusSeatsId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleBusSeats(@PathVariable("scheduleBusSeatsId") String scheduleBusSeatsId,@RequestBody ScheduleBusSeats scheduleBusSeats) {
		ELUser loginUser = getLoginUser();
		Wrapper<Config> wrapper = new EntityWrapper<Config>();
		wrapper.where("org_id={0} and code={1}", loginUser.getOrgId(),SysConfigConstant.SEAT_LOCK_EXPIRE);
		Config config = configService.selectOne(wrapper);
		String lockTime = config.getValue(); //锁票时长(分钟)
		long expire = Long.parseLong(lockTime) * 60 * 1000; //转化为毫秒 
		String seatStatus = scheduleBusSeats.getSeatStatus();
		if(seatStatus.equals("1")) { //1 当前
			boolean isLocked = lock.lock(scheduleBusSeatsId, loginUser.getId(), expire);
			if(!isLocked) { //该座位已经被锁定
				return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "该座位已经被锁定");
			}else {
				return CommonResponse.createCommonResponse();
			}
		}else { //其他状态：2可选 3预留 4不售 5已售
			scheduleBusSeats.setUpdateBy(loginUser.getId());
			//修改前先判断该座位是否被锁定
			boolean isLocked = lock.lock(scheduleBusSeatsId, loginUser.getId(), expire);
			if(!isLocked) { //该座位已经被锁定
				return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "该座位已经被锁定");
			}else {
				boolean isUpdated = scheduleBusSeatsService.updateById(scheduleBusSeats);
				//释放锁
				lock.unlock(scheduleBusSeatsId, loginUser.getId());
				if(isUpdated) {
					return CommonResponse.createCommonResponse();
				}else {
					return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改座位状态失败!");
				}
			}
		}
	}
    
	@ApiOperation(value = "根据班次ID查询班次座位", notes = "根据班次ID查询班次座位", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/scheduleBusSeats", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("scheduleBusId") String scheduleBusId) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		Wrapper<ScheduleBusSeats> wrapper = new EntityWrapper<ScheduleBusSeats>();
		wrapper.where("schedule_bus_id={0}", scheduleBusId);
		wrapper.orderBy("seat_num",true);
		List<ScheduleBusSeats> list = scheduleBusSeatsService.selectList(wrapper);
		resp.setData(list);
		List<Dictionary> seatStatus = dictionaryService.getRediesByKey("seat_status"); //查询座位状态字典
		resp.extendsRes("seatStatus", seatStatus);
		return resp;
	}

	@ApiOperation(value = "查看班次座位表详情", notes = "查看班次座位表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/scheduleBusSeats/{scheduleBusSeatsId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("scheduleBusSeatsId") String scheduleBusSeatsId) {
		ScheduleBusSeats scheduleBusSeats = scheduleBusSeatsService.selectById(scheduleBusSeatsId);
		return CommonResponse.createCommonResponse(scheduleBusSeats);
	}
}
