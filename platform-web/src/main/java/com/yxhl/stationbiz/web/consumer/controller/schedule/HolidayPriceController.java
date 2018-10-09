package com.yxhl.stationbiz.web.consumer.controller.schedule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;
import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.request.HolidayPriceRequest;
import com.yxhl.stationbiz.system.domain.response.BusPriceResp;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;
import com.yxhl.stationbiz.system.domain.service.schedule.BasicPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;


/**
 *	
 *  节假日票价表控制器
 *  创建人: ypf
 *  创建日期:2018-8-15 14:20:34
 */
@RestController
@Api(tags = "节假日票价表")
@RequestMapping(value = "/m")
public class HolidayPriceController extends BaseController{
	
    @Autowired
    private HolidayPriceService holidayPriceService;
    
    @Autowired
    private BasicPriceService basicPriceService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private OperateLogService logService;
    
    @Autowired
    private HolidayService holidayService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/holidayPrice/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleBusTpl scheduleBusTpl) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ScheduleBusTpl> page = new Page<ScheduleBusTpl>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBusTpl> result = basicPriceService.selBusPageList(page, scheduleBusTpl,"2");
		resp.setData(result);
		List<Dictionary> oprCategoryvarchar= dictionaryService.getRediesByKey("opr_categoryvarchar"); //运营类别
		List<Dictionary> runAreavarchar= dictionaryService.getRediesByKey("run_areavarchar"); //运行区域
		List<Dictionary> oprModevarchar= dictionaryService.getRediesByKey("opr_modevarchar"); //营运方式
		List<Dictionary> busTypevarchar= dictionaryService.getRediesByKey("bus_typevarchar"); //班次类型
		resp.extendsRes("oprCategoryvarchar", oprCategoryvarchar);
		resp.extendsRes("runAreavarchar", runAreavarchar);
		resp.extendsRes("oprModevarchar", oprModevarchar);
		resp.extendsRes("busTypevarchar", busTypevarchar);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询节假日票价表", notes = "根据条件查询节假日票价表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/holidayPrices", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<HolidayPrice> list = holidayPriceService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增节假日票价表", notes = "新增节假日票价表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/holidayPrice", produces = "application/json;charset=UTF-8")
	public CommonResponse addHolidayPrice(@RequestBody HolidayPrice holidayPrice) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			holidayPrice.setOrgId(loginUser.getOrgId());
			holidayPrice.setCompId(loginUser.getCompanyId());
			holidayPrice.setCreateBy(loginUser.getId());
			holidayPrice.setUpdateBy(loginUser.getId());
		}
		holidayPrice.setId(IdWorker.get32UUID());
		boolean isAdded = holidayPriceService.insert(holidayPrice);
		if(isAdded) {
			return CommonResponse.createCommonResponse(holidayPrice);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改节假日票价表", notes = "修改节假日票价表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/holidayPrice/{holidayPriceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateHolidayPrice(@PathVariable("holidayPriceId") String holidayPriceId,@RequestBody HolidayPrice holidayPrice) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			holidayPrice.setUpdateBy(loginUser.getId());
		}
		holidayPrice.setId(holidayPriceId);
		boolean isUpdated = holidayPriceService.updateById(holidayPrice);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除节假日票价表", notes = "删除节假日票价表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/holidayPrice/{holidayPriceIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("holidayPriceIds") List<String> holidayPriceIds) {
		boolean isDel = holidayPriceService.deleteBatchIds(holidayPriceIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看节假日票价表详情", notes = "查看节假日票价表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/holidayPrice/{holidayPriceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("holidayPriceId") String holidayPriceId) {
		HolidayPrice holidayPrice = holidayPriceService.selectById(holidayPriceId);
		return CommonResponse.createCommonResponse(holidayPrice);
	}
	
	@ApiOperation("票价列表")
	@PostMapping(value = "/holidayPriceTicket", produces = "application/json;charset=UTF-8")
	public CommonResponse holidayPriceTicket(@RequestParam("scheduleTplId")String scheduleTplId) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		HolidayPrice holidayPrice= new HolidayPrice();
		holidayPrice.setScheduleTplId(scheduleTplId);
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			holidayPrice.setOrgId(loginUser.getOrgId());
			holidayPrice.setCompId(loginUser.getCompanyId());
		}
		List<HolidayPrice> list= holidayPriceService.selholidayPriceList(holidayPrice);
		List<HolidayPriceResponse> list1= new ArrayList<HolidayPriceResponse>();
		if(list.size()>0) {
			List<String> ids= new ArrayList<String>();
			for (HolidayPrice holidayPrice2 : list) {
				ids.add(holidayPrice2.getId());
			}
			list1= holidayPriceService.dynamic(ids, "2", loginUser.getOrgId(), loginUser.getCompanyId());
			
		}
		resp.extendsRes("dynamic", list1);
		resp.setData(list);
		return resp;
	}
	
	@ApiOperation("新增/修改节日票价列表数据")
	@PostMapping(value = "/holidayPriceListInfo", produces = "application/json;charset=UTF-8")
	public CommonResponse holidayPriceListInfo(@RequestParam("scheduleTplId")String scheduleTplId,@RequestParam("holidayId")String holidayId) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		BusPriceResp bp= new BusPriceResp();
		ELUser loginUser = getLoginUser();
		bp.setOrgId(loginUser.getOrgId());
		bp.setCompId(loginUser.getCompanyId());
		//查班次模板信息
		HolidayPrice holp= new HolidayPrice();
		holp.setScheduleTplId(scheduleTplId);
		if(loginUser != null) {
			holp.setOrgId(loginUser.getOrgId());
			holp.setCompId(loginUser.getCompanyId());
		}
		holp.setHolidayId(holidayId);
		List<HolidayPrice> list= holidayPriceService.selholidayPriceList(holp);//查询节日票价信息
		List<HolidayPriceResponse> list1= new ArrayList<HolidayPriceResponse>();
		
		BasicPrice basicP= new BasicPrice() ;//查询基础票价的信息
		basicP.setScheduleTplId(scheduleTplId);
		basicP.setCompId(loginUser.getCompanyId());
		basicP.setOrgId(loginUser.getOrgId());
		List<BasicPrice> blist= basicPriceService.getSeatCategoryList(basicP);
		
		if(list.size()>0) {
			Iterator<BasicPrice> iterator = blist.iterator();
			while (iterator.hasNext()) {
				BasicPrice bpr = iterator.next();
				for (HolidayPrice basicPrice : list) {//基础票价和节假日票价比较
					boolean flag=false;
					if(bpr.getScheduleTplId().equals(basicPrice.getScheduleTplId()) && bpr.getOnStaId().equals(basicPrice.getOnStaId()) 
							&& bpr.getOffStaId().equals(basicPrice.getOffStaId()) && bpr.getSeatCate().equals(basicPrice.getSeatCate())
							&& bpr.getSeats().equals(basicPrice.getSeats())) {
						flag=true;
					}
					if(flag) {
						iterator.remove();
					}
				}
			} 
		}
		 
		if(list.size()>0) {
			List<String> ids= new ArrayList<String>();
			for (HolidayPrice holidayPrice : list) {
				ids.add(holidayPrice.getId());
			}
			list1= holidayPriceService.dynamic(ids, "2", loginUser.getOrgId(), loginUser.getCompanyId());
		}
		if(blist.size()>0) {
			List<String> ids= new ArrayList<String>();
			for (BasicPrice basicPrice : blist) {
				ids.add(basicPrice.getId());
				HolidayPrice pr=new HolidayPrice();
				try {
					BeanUtils.copyProperties(pr, basicPrice);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.add(pr);
			}
			List<HolidayPriceResponse> list2= holidayPriceService.dynamic(ids, "1", loginUser.getOrgId(), loginUser.getCompanyId());
			for (HolidayPriceResponse holidayPriceResponse : list2) {
				list1.add(holidayPriceResponse);
			}
		}
		resp.setData(list);
		resp.extendsRes("dynamic", list1);
		List<Dictionary> driverTypeList = dictionaryService.getRediesByKey("seat_category"); //查询循环类型字典
		resp.extendsRes("driverType", driverTypeList);
//			CommonResponse resp = CommonResponse.createCommonResponse();
//			List<HolidayPriceResponse> list1 = new ArrayList<HolidayPriceResponse>();
//			ELUser loginUser = getLoginUser();
//			BasicPrice br= new BasicPrice();
//			br.setScheduleTplId(scheduleTplId);
//			br.setOrgId(loginUser.getOrgId());
//			br.setCompId(loginUser.getCompanyId());
//			List<BasicPrice> brlist= basicPriceService.getSeatCategoryList(br);
//			if(brlist.size()>0) {
//				List<String> ids= new ArrayList<String>();
//				for (BasicPrice basicPrice : brlist) {
//					ids.add(basicPrice.getId());
//				}
//				list1= holidayPriceService.dynamic(ids, "1", loginUser.getOrgId(), loginUser.getCompanyId());
//			}
//		resp.setData(brlist);
//		resp.extendsRes("dynamic", list1);
		return resp;
	}
	
	@ApiOperation(value = "新增节假日票价表", notes = "新增节假日票价表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/addholidayPriceValue", produces = "application/json;charset=UTF-8")
	public CommonResponse addholidayPriceValue(@RequestBody HolidayPriceRequest holidayPriceRequest,@RequestParam("id")String id) throws Exception {
		boolean isAdded= holidayPriceService.addHolidayPrice(holidayPriceRequest,getLoginUser().getId(),id);
		if(isAdded) {
			Wrapper<Holiday> wrapper= new EntityWrapper<Holiday>();
			wrapper.eq("id", holidayPriceRequest.getHolidayId());
			Holiday ho=holidayService.selectOne(wrapper);
			logService.insertLog(OperateLogModelEnum.PRICE.getModule(),"设置节日票价",getRemoteAddr(),"节日名称【"+ho.getHolidayName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(holidayPriceRequest);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}
}
