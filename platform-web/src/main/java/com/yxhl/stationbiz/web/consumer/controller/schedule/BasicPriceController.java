package com.yxhl.stationbiz.web.consumer.controller.schedule;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.request.BusPriceRequest;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;
import com.yxhl.stationbiz.system.domain.response.SeatCategoryResp;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusTplService;
import com.yxhl.stationbiz.system.domain.service.schedule.BasicPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  基础票价表控制器
 *  创建人: xjh
 *  创建日期:2018-8-20 10:53:51
 */
@RestController
@Api(tags = "基础票价表")
@RequestMapping(value = "/m/basicPrice")
public class BasicPriceController extends BaseController{
	
    @Autowired
    private BasicPriceService basicPriceService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
    private HolidayPriceService holidayPriceService;
    
    @Autowired
    private ScheduleBusTplService scheduleBusTplService;
    
    @Autowired
	private OperateLogService logService;
    
    @Autowired
    private LineService lineService;
    
    @ApiOperation("班次模板分页查询") 
	@PostMapping(value = "/busPage", produces = "application/json;charset=UTF-8")
	public CommonResponse busPage(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleBusTpl scheduleBusTpl) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		scheduleBusTpl.setOrgId(loginUser.getOrgId());
		scheduleBusTpl.setCompId(loginUser.getCompanyId());
		Page<ScheduleBusTpl> page = new Page<ScheduleBusTpl>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBusTpl> result = basicPriceService.selBusPageList(page, scheduleBusTpl,"1");
		resp.setData(result);
		List<Dictionary> oprCategoryvarchar= dictionaryService.getRediesByKey("opr_categoryvarchar"); //运营类别
		List<Dictionary> runAreavarchar= dictionaryService.getRediesByKey("run_areavarchar"); //运行区域
		List<Dictionary> oprModevarchar= dictionaryService.getRediesByKey("opr_modevarchar"); //营运方式
		List<Dictionary> busTypevarchar= dictionaryService.getRediesByKey("bus_typevarchar"); //车辆类型
		resp.extendsRes("oprCategoryvarchar", oprCategoryvarchar);
		resp.extendsRes("runAreavarchar", runAreavarchar);
		resp.extendsRes("oprModevarchar", oprModevarchar);
		resp.extendsRes("busTypevarchar", busTypevarchar);
		return resp;
	}
    
    @ApiOperation("分页查询")
	@PostMapping(value = "/pricePage", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody BasicPrice basicPrice) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		ELUser loginUser = getLoginUser();
		basicPrice.setOrgId(loginUser.getOrgId());
		basicPrice.setCompId(loginUser.getCompanyId());
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<BasicPrice> page = new Page<BasicPrice>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		//查票价列表
		Page<BasicPrice> selPageList = basicPriceService.selPageList(page, basicPrice);
		Page<BasicPrice> result = selPageList;
		List<String> ids = new ArrayList<String>();
		List<BasicPrice> records = selPageList.getRecords();
		for(BasicPrice bp1 : records) {
			ids.add(bp1.getId());
		}
		if(ids!=null && ids.size()>0) {
			//查票种价格信息
			List<HolidayPriceResponse> dyList = holidayPriceService.dynamic(ids, "1", loginUser.getOrgId(), loginUser.getCompanyId());
			resp.extendsRes("dyList", dyList);
		}
		resp.setData(result);
		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.extendsRes("seatCategory", seatCategory);
		return resp;
	}
    
	@ApiOperation(value = "票价设置列表", notes = "票价设置列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/configList", produces = "application/json;charset=UTF-8")
	public CommonResponse addPrice(@RequestBody BasicPrice bp) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		ELUser loginUser = getLoginUser();
		bp.setOrgId(loginUser.getOrgId());
		bp.setCompId(loginUser.getCompanyId());
		String lineId = bp.getLineId();
		//查票价列表
		List<BasicPrice> priceList = basicPriceService.getPriceList(bp);
		//查班次模板信息
		List<BasicPrice> busList = basicPriceService.getBusPrice(bp);
		if(priceList!=null && priceList.size()>0) {
			//查班次模板信息
			if(busList.size()>0) {
				Iterator<BasicPrice> iterator = busList.iterator();
				while (iterator.hasNext()) {
					BasicPrice busResp = iterator.next();
					for (BasicPrice basicPrice : priceList) {
						boolean flag=false;
						//基础票价和班次模板比较
						if(busResp.getOnStaId().equals(basicPrice.getOnStaId()) 
								&& busResp.getOffStaId().equals(basicPrice.getOffStaId()) && busResp.getSeatCate().equals(basicPrice.getSeatCate())
								&& busResp.getSeats().equals(basicPrice.getSeats())) {
							flag=true;
						}
						if(flag) {
							iterator.remove();
						}
					}
				}
			}
			//票价id去重
			Set<String> set = new HashSet<String>();
			for (BasicPrice bpr : priceList) {
				set.add(bpr.getId());
			}
			List<String> ids= new ArrayList<String>();
			for(String id : set) {
				ids.add(id);
			}
			//根据票价id查有值票种价格信息
			List<HolidayPriceResponse> dyList= holidayPriceService.dynamic(ids, "1", loginUser.getOrgId(), loginUser.getCompanyId());
			//查票种价格信息
			List<HolidayPriceResponse> dlist = holidayPriceService.dynamicForTitle(loginUser.getOrgId(), loginUser.getCompanyId());
			//给没有值的票种添加默认值
			List<HolidayPriceResponse> priceValueList= new ArrayList<HolidayPriceResponse>();
			if(busList.size()>0 && dlist.size()>0) {
				//班次模板信息绑定票种
				priceValueList = priceValueList(busList,dlist,lineId);
			}
			//票价信息绑定票种
			for (BasicPrice basicPrice : priceList) {
				basicPrice.setRowId(basicPrice.getId());
			}
			dyList.addAll(priceValueList);
			priceList.addAll(busList);
			resp.setData(priceList);
			resp.extendsRes("dyList", dyList);
		}else {
			//查票种价格信息
			List<HolidayPriceResponse> dlist = holidayPriceService.dynamicForTitle(loginUser.getOrgId(), loginUser.getCompanyId());
			List<HolidayPriceResponse> priceValueList= new ArrayList<HolidayPriceResponse>();
			if(busList.size()>0 && dlist.size()>0) {
				//班次模板信息绑定票种
				priceValueList = priceValueList(busList,dlist,lineId);
			}
			resp.extendsRes("dyList", priceValueList);
			resp.setData(busList);
		}
		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		//班次模板座位类型
		List<SeatCategoryResp> busSeatCategory = basicPriceService.getSeatCategory(bp);
		resp.extendsRes("seatCategory", seatCategory);
		resp.extendsRes("busSeatCategory", busSeatCategory);
		return resp;
	}
	
	@ApiOperation(value = "修改基础票价表", notes = "修改基础票价表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateBasicPrice(@RequestBody List<BusPriceRequest> req,@RequestParam("lineCopyFlag")String lineCopyFlag) throws Exception {
		ELUser loginUser = getLoginUser();
		for(BusPriceRequest busReq : req) {
			if(loginUser != null) {
				busReq.setOrgId(loginUser.getOrgId());
				busReq.setCompId(loginUser.getCompanyId());
				busReq.setCreateBy(loginUser.getId());
				busReq.setUpdateBy(loginUser.getId());
			}
		}
		boolean isUpdated = basicPriceService.updatePrice(req,lineCopyFlag);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "站点票价设置", notes = "站点票价设置", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/configStaPrice", produces = "application/json;charset=UTF-8")
	public CommonResponse configStaPrice(@RequestBody BasicPrice bp) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		ELUser loginUser = getLoginUser();
		//班次模板座位类型
		List<SeatCategoryResp> busSeatCategory = basicPriceService.getSeatCategory(bp);
		//查票种价格信息
		List<HolidayPriceResponse> dlist = holidayPriceService.dynamicForTitle(loginUser.getOrgId(), loginUser.getCompanyId());
		for (HolidayPriceResponse holidayPriceResponse : dlist) {
			holidayPriceResponse.setTicketValue(0F);
		}
		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category");
		resp.extendsRes("seatCategory", seatCategory);
		resp.extendsRes("busSeatCategory", busSeatCategory);
		resp.setData(dlist);
		return resp;
	}
	
	@ApiOperation(value = "设置站点基础票价", notes = "新增站点基础票价", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/addStaPrice", produces = "application/json;charset=UTF-8")
	public CommonResponse addStaPrice(@RequestBody BusPriceRequest req) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			req.setOrgId(loginUser.getOrgId());
			req.setCompId(loginUser.getCompanyId());
			req.setCreateBy(loginUser.getId());
			req.setUpdateBy(loginUser.getId());
		}
		checkNotNull(req.getScheduleTplId(),"参数：Error:scheduleTplId 不允许为空");
		checkNotNull(req.getLineId(),"参数：lienid 不允许为空");
		checkNotNull(req.getOffStaId(),"参数：offstaid 不允许为空");
		checkNotNull(req.getOnStaId(),"参数：onstaid 不允许为空");
		boolean isAdded = basicPriceService.addStaPrice(req);
		if(isAdded) {
			return CommonResponse.createCommonResponse(req);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "设置失败!");
		}
	}
	
	@ApiOperation(value = "复制票价列表", notes = "复制票价列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/copyPriceList", produces = "application/json;charset=UTF-8")
	public CommonResponse copyPriceList(@RequestBody BasicPrice bp) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		//查该班次已有票价的座位类型和座位数
		List<BasicPrice> priceList = basicPriceService.getPriceList(bp);
		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.setData(priceList);
		resp.extendsRes("seatCategory", seatCategory);
		return resp;
	}
	
	@ApiOperation(value = "班次列表", notes = "复制票价列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/copyBusList", produces = "application/json;charset=UTF-8")
	public CommonResponse copyBusList(@RequestBody BasicPrice bp) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		//该线路所有班次模板
		Wrapper<ScheduleBusTpl> wrapper = new EntityWrapper<ScheduleBusTpl>();
		wrapper.eq("line_id", bp.getLineId());
		ELUser user= this.getLoginUser();
		wrapper.eq("org_id", user.getOrgId());
			//正班，加班班次为0否
		if(bp.getOvertimeBusFlag2() != null && bp.getOvertimeBusFlag2() == 1 && bp.getOvertimeBusFlag() != 1) {	
			wrapper.eq("overtime_bus_flag", 0);
			//正班，加班都为0 或者都为1时查全部
		}else if((bp.getOvertimeBusFlag()== 0 && bp.getOvertimeBusFlag2() == 0) || (bp.getOvertimeBusFlag()== 1 && bp.getOvertimeBusFlag2() == 1)) {
			
		}else if(bp.getOvertimeBusFlag() != null && bp.getOvertimeBusFlag2() == 0) {
			wrapper.eq("overtime_bus_flag", bp.getOvertimeBusFlag());
		}
		List<ScheduleBusTpl> list = scheduleBusTplService.selectList(wrapper);
		//把当前班次剔除
		for (ScheduleBusTpl scheduleBusTpl : list) {
			if(scheduleBusTpl.getId().equals(bp.getScheduleTplId())) {
				list.remove(scheduleBusTpl);
				break;
			}
		}
		resp.setData(list);
		return resp;
	}
	
	@ApiOperation(value = "复制票价", notes = "复制票价", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/copyPrice", produces = "application/json;charset=UTF-8")
	public CommonResponse copyPrice(@RequestBody BusPriceRequest req) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			req.setOrgId(loginUser.getOrgId());
			req.setCompId(loginUser.getCompanyId());
			req.setCreateBy(loginUser.getId());
			req.setUpdateBy(loginUser.getId());
		}
		boolean isAdded = basicPriceService.copyPrice(req);
		if(isAdded) {
			return CommonResponse.createCommonResponse(req);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "复制失败!");
		}
	}
	
	@ApiOperation(value = "删除基础票价表", notes = "删除基础票价表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{priceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("priceId") List<String> priceId) {
		List<BasicPrice> list = basicPriceService.selectBatchIds(priceId);
		boolean isDel = basicPriceService.delectPrice(priceId);
		if(isDel) {
			BasicPrice bp = null;
			String pId = null;
			for(BasicPrice bpc : list) {
				bp = bpc;
				pId += bp.getId()+",";
			}
			pId = pId.substring(0, pId.length());
			logService.insertLog(OperateLogModelEnum.BASICPRICE.getModule(),"删除票价",getRemoteAddr(),"被删除票价ID【"+pId+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}
	
	@ApiOperation(value = "根据条件查询基础票价表", notes = "根据条件查询基础票价表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<BasicPrice> wrapper = new EntityWrapper<BasicPrice>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<BasicPrice> list = basicPriceService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation(value = "票价列表", notes = "添加票价列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/priceList", produces = "application/json;charset=UTF-8")
	public CommonResponse priceList(@RequestBody BasicPrice req) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		ELUser loginUser = getLoginUser();
		BasicPrice bp = new BasicPrice();
		bp.setOrgId(loginUser.getOrgId());
		bp.setCompId(loginUser.getCompanyId());
		//查票价列表
		List<BasicPrice> priceList = basicPriceService.getPriceList(bp);
		List<String> ids = new ArrayList<String>();
		for(BasicPrice bpl : priceList) {
			ids.add(bpl.getId());
		}
		//查票种价格信息
		List<HolidayPriceResponse> dyList = holidayPriceService.dynamic(ids, "1", loginUser.getOrgId(), loginUser.getCompanyId());
		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.setData(priceList);
		resp.extendsRes("dyList", dyList);
		resp.extendsRes("seatCategory", seatCategory);
		return resp;
	}
	
	/**
	 * 班次模板信息绑定票种
	 * @param busList
	 * @param dlist
	 * @param lineId
	 * @return
	 */
	public List<HolidayPriceResponse> priceValueList(List<BasicPrice> busList,List<HolidayPriceResponse> dlist,String lineId){
		List<HolidayPriceResponse> priceValueList = new ArrayList<HolidayPriceResponse>();
		int a=0;
		for (BasicPrice basicPrice : busList) {
			a++;
			String rowId = Integer.valueOf(a).toString();
			basicPrice.setRowId(rowId);
			for (HolidayPriceResponse holidayPriceResponse : dlist) {
				holidayPriceResponse.setRowId(basicPrice.getRowId());
				holidayPriceResponse.setTicketValue(0F);
				//保险票
				if(holidayPriceResponse.getTicketCateName().equals("保险票")) {
					Line line = lineService.selectById(lineId);
					//线路里程
					int lineMileage = Integer.parseInt(line.getLineMileage());
					if(lineMileage>=100) {
						holidayPriceResponse.setTicketValue(2F);
					}else {
						holidayPriceResponse.setTicketValue(1F);
					}
				}
				HolidayPriceResponse nowholiday= new HolidayPriceResponse();
				try {
					BeanUtils.copyProperties(nowholiday, holidayPriceResponse);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				priceValueList.add(nowholiday);
			}
		}
		return priceValueList;
	}
	
//	@ApiOperation(value = "新增基础票价表", notes = "新增基础票价表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
//	public CommonResponse addBasicPrice(@RequestBody List<BusPriceRequest> req,@RequestParam("lineCopyFlag")String lineCopyFlag) throws Exception {
//		ELUser loginUser = getLoginUser();
//		for(BusPriceRequest busReq : req) {
//			if(loginUser != null) {
//				busReq.setOrgId(loginUser.getOrgId());
//				busReq.setCompId(loginUser.getCompanyId());
//				busReq.setCreateBy(loginUser.getId());
//				busReq.setUpdateBy(loginUser.getId());
//			}
//		}
//		boolean isAdded = basicPriceService.add(req,lineCopyFlag);
//		if(isAdded) {
//			return CommonResponse.createCommonResponse(req);
//		}else {
//			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
//		}
//	}
	
//	@ApiOperation(value = "票价列表详情", notes = "票价列表详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//	@PostMapping(value = "/info", produces = "application/json;charset=UTF-8")
//	public CommonResponse getInfoById(@RequestBody BasicPrice bp) {
//		CommonResponse resp = CommonResponse.createCommonResponse();
//		ELUser loginUser = getLoginUser();
//		bp.setOrgId(loginUser.getOrgId());
//		bp.setCompId(loginUser.getCompanyId());
//		//查票价列表
//		List<BasicPrice> priceList = basicPriceService.getPriceList(bp);
//		//查班次模板信息
//		List<BasicPrice> busList = basicPriceService.getBusPrice(bp);
//		if(busList.size()>0) {
//			Iterator<BasicPrice> iterator = busList.iterator();
//			while (iterator.hasNext()) {
//				BasicPrice busResp = iterator.next();
//				for (BasicPrice basicPrice : priceList) {
//					boolean flag=false;
//					//基础票价和班次模板比较
//					if(busResp.getOnStaId().equals(basicPrice.getOnStaId()) 
//							&& busResp.getOffStaId().equals(basicPrice.getOffStaId()) && busResp.getSeatCate().equals(basicPrice.getSeatCate())
//							&& busResp.getSeats().equals(basicPrice.getSeats())) {
//						flag=true;
//					}
//					if(flag) {
//						iterator.remove();
//					}
//				}
//			}
//		}
//		if(priceList.size()>0) {
//			//票价id去重
//			Set<String> set = new HashSet<String>();
//			for (BasicPrice bpr : priceList) {
//				set.add(bpr.getId());
//			}
//			List<String> ids= new ArrayList<String>();
//			for(String id : set) {
//				ids.add(id);
//			}
//			//根据票价id查有值票种价格信息
//			List<HolidayPriceResponse> dyList= holidayPriceService.dynamic(ids, "1", loginUser.getOrgId(), loginUser.getCompanyId());
//			//查票种价格信息
//			List<HolidayPriceResponse> dlist = holidayPriceService.dynamicForTitle(loginUser.getOrgId(), loginUser.getCompanyId());
//			//给没有值的票种添加默认值
//			List<HolidayPriceResponse> newList= new ArrayList<HolidayPriceResponse>();
//			if(busList.size()>0 && dlist.size()>0) {
//				int a=0;
//				for (BasicPrice basicPrice : busList) {
//					a++;
//					basicPrice.setRowId(a);
//					for (HolidayPriceResponse holidayPriceResponse : dlist) {
//						holidayPriceResponse.setRowId(Integer.valueOf(basicPrice.getRowId()).toString());
//						holidayPriceResponse.setTicketValue(0F);
//						HolidayPriceResponse nowholiday= new HolidayPriceResponse();
//						try {
//							BeanUtils.copyProperties(nowholiday, holidayPriceResponse);
//						} catch (IllegalAccessException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (InvocationTargetException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						newList.add(nowholiday);
//					}
//				}
//			}
//			dyList.addAll(newList);
//			priceList.addAll(busList);
//			resp.setData(priceList);
//			resp.extendsRes("dyList", dyList);
//		}
//		
//		//查询座位类型字典
//		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
//		//班次模板座位类型去重
//		List<SeatCategoryResp> busSeatCategory = basicPriceService.getSeatCategory(bp);
//		resp.extendsRes("seatCategory", seatCategory);
//		resp.extendsRes("busSeatCategory", busSeatCategory);
//		return resp;
//	}
	
}
