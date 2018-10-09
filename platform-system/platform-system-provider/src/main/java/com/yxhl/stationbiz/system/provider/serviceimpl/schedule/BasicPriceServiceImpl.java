package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.WebHelper;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.request.BusPriceRequest;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;
import com.yxhl.stationbiz.system.domain.response.SeatCateResp;
import com.yxhl.stationbiz.system.domain.response.SeatCategoryResp;
import com.yxhl.stationbiz.system.domain.service.schedule.BasicPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleBusTplDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.BasicPriceDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.TicketCateValueDao;

/**
 * @ClassName: BasicPriceServiceImpl
 * @Description: 基础票价表 serviceImpl
 * @author xjh
 * @date 2018-8-20 10:53:51
 */
@Transactional(readOnly = true)
@Service("basicPriceService")
public class BasicPriceServiceImpl extends CrudService<BasicPriceDao, BasicPrice> implements BasicPriceService {
	@Autowired
	private BasicPriceDao basicPriceDao;
	
	@Autowired
	private ScheduleBusTplDao scheduleBusTplDao;
	
	@Autowired
	private TicketCateValueDao ticketCateValueDao;
	
    @Autowired
    private ExecPriceService execPriceService;
    
    @Autowired
	private LineDao lineDao;
    
    @Autowired
	private OperateLogService logService;
    
    @Autowired
	protected HttpServletRequest request;

	@Override
	public Page<BasicPrice> selPageList(Page<BasicPrice> page, BasicPrice basicPrice) {
		List<BasicPrice> list = basicPriceDao.selPageList(page, basicPrice);
		page.setRecords(list);
		return page;
	}

	/**
	 * 班次模板分页
	 */
	@Override
	public Page<ScheduleBusTpl> selBusPageList(Page<ScheduleBusTpl> page, ScheduleBusTpl st,String Type) {
		//正班，加班班次为0否
		if(Type.equals("1")) {
			if(st.getOvertimeBusFlag2() != null && st.getOvertimeBusFlag2() == 1 && st.getOvertimeBusFlag() != 1) {
				st.setOvertimeBusFlag(0);
			}
			if((st.getOvertimeBusFlag()== 0 && (st.getOvertimeBusFlag2() == 0) || (st.getOvertimeBusFlag()== 1 && st.getOvertimeBusFlag2() == 1))) {
				st.setOvertimeBusFlag(null);
			}
		}
		List<ScheduleBusTpl> list = scheduleBusTplDao.selBusPageList(page, st);
		page.setRecords(list);
		return page;
	}

	/**
	 * 查班次模板信息
	 */
	@Override
	public List<BasicPrice> getBusPrice(BasicPrice busPrice) {
		List<BasicPrice> list = basicPriceDao.getBusPrice(busPrice);
		if(list.size()>0 && list!=null) {
			for (BasicPrice bas : list) {
				if((bas.getSeatCate()==null) || (bas.getSeats()==null)) {
					throw new YxBizException("该班次的车辆没有座位类型或者座位数!");
				}
			}
		}
		return list;
	}

	/**
	 * 查班次模板座位类型
	 */
	@Override
	public List<SeatCategoryResp> getSeatCategory(BasicPrice busPrice) {
		List<SeatCategoryResp> seatCategory = basicPriceDao.getSeatCategory(busPrice);
		return seatCategory;
	}

	@Override
	public List<BasicPrice> getSeatCategoryList(BasicPrice basicPrice) {
		return basicPriceDao.getSeatCategoryList(basicPrice);
	}
	/**
	 * 根据班次模板id查票价
	 */
	@Override
	public List<BasicPrice> getPriceList(BasicPrice basicPrice) {
		List<BasicPrice> priceList = basicPriceDao.getPriceList(basicPrice);
		return priceList;
	}
	
	/**
	 * 添加票价
	 */
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(List<BusPriceRequest> req,String lineCopyFlag) throws Exception {
		Integer result = 1;
		//用于同步设置  执行票价List
		List<BasicPrice> basicPriceList=Lists.newArrayList();
		//用于同步设置  执行票价的值 List
		List<TicketCateValue> ticketCateValueList=Lists.newArrayList();
		for(BusPriceRequest breq : req) {
			if(lineCopyFlag.equals("0")) {
				//添加票价和取值表
				addBasicPrice(breq);
			}else {	//复制到本线路其他班次模板
				Wrapper<ScheduleBusTpl> swrapper = new EntityWrapper<ScheduleBusTpl>();
				swrapper.eq("line_id", breq.getLineId());
				// 本线路所有的班次模板
				List<ScheduleBusTpl> scList = scheduleBusTplDao.selectList(swrapper);
				//添加票价表
				for(ScheduleBusTpl tpl : scList) {
					breq.setScheduleTplId(tpl.getId());
					//添加票价和取值表
					addBasicPrice(breq);
				}
			}
		}
		//同步设置执行票价
		execPriceService.addExecPriceByBasic(basicPriceList, ticketCateValueList);
		return result > 0 ? true : false;
	}

	/**
	 * 修改
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePrice(List<BusPriceRequest> request,String lineCopyFlag) throws Exception  {
		Integer result = 1;
		//用于同步设置  执行票价List
		List<BasicPrice> basicPriceListDest=Lists.newArrayList();
		//用于同步设置  执行票价的值 List
		List<TicketCateValue> ticketCateValueListDest=Lists.newArrayList();
		for(BusPriceRequest req : request) {
			if(lineCopyFlag.equals("0")) {//not copy
				//根据票价id查票价
				List<BasicPrice> basicPriceList1 = basicPriceList1(req);
				if(basicPriceList1.size()>0 && basicPriceList1!=null) {
					//删除取值表旧数据，添加取值表
					priceVlue(basicPriceList1,req);
				}else {
					//添加票价和取值表
					Map<String,Object> maps=addBasicPrice(req);
					
					//用于同步设置  执行票价List
					List<BasicPrice> basicPriceList=(List)maps.get("basicPriceList");
					basicPriceListDest.addAll(basicPriceList);
					
					//用于同步设置  执行票价的值 List
					List<TicketCateValue> ticketCateValueList=(List)maps.get("ticketCateValueList");
					ticketCateValueListDest.addAll(ticketCateValueList);
				}
			}else {  //复制到本线路其他班次模板
				Wrapper<ScheduleBusTpl> swrapper = new EntityWrapper<ScheduleBusTpl>();
				swrapper.eq("line_id", req.getLineId());
				// 本线路所有的班次模板
				List<ScheduleBusTpl> scList = scheduleBusTplDao.selectList(swrapper);
				for (ScheduleBusTpl scheduleBusTpl : scList) {
					//根据班次模板id查票价
					req.setScheduleTplId(scheduleBusTpl.getId());
					List<BasicPrice> basicPriceList1 = basicPriceList1(req);
					if(basicPriceList1.size()>0 && basicPriceList1!=null) {
						//删除取值表旧数据，添加取值表
						priceVlue(basicPriceList1,req);
					}else {
						//添加票价和取值表
						req.setScheduleTplId(scheduleBusTpl.getId());
						Map<String,Object> maps=addBasicPrice(req);
						
						//用于同步设置  执行票价List
						List<BasicPrice> basicPriceList=(List)maps.get("basicPriceList");
						basicPriceListDest.addAll(basicPriceList);
						//用于同步设置  执行票价的值 List
						List<TicketCateValue> ticketCateValueList=(List)maps.get("basicPriceList");
						ticketCateValueListDest.addAll(ticketCateValueList);
						
					}
				}
				
			}
			
		}
		//同步设置执行票价
		execPriceService.addExecPriceByBasic(basicPriceListDest, ticketCateValueListDest);
		
		return result > 0 ? true : false;
	}

	/**
	 * 设置站点票价
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addStaPrice(BusPriceRequest req) {
		Integer result = 1;
		Line line = lineDao.selById(req.getLineId());
		if(line.getStartStateId().equals(req.getOnStaId()) && line.getEndStateId().equals(req.getOffStaId())) {
			throw new YxBizException("不能添加线路起始站点票价!");
		}
		List<String> busIdList = req.getBusIdList();
		BasicPrice price = new BasicPrice();
		price.setCompId(req.getCompId());
		price.setOrgId(req.getOrgId());
		price.setOnStaId(req.getOnStaId());
		price.setOffStaId(req.getOffStaId());
		//座位类型
		if(!(req.getSeatCate().equals(""))) {
			price.setSeatCate(req.getSeatCate());
		}
		//座位数
		if(req.getSeats()!=null) {
			price.setSeats(req.getSeats());
		}
		//是否需要复制到其他班次
		if(busIdList.size()>0 && busIdList!=null) {
			for (String busId : busIdList) {
				price.setScheduleTplId(busId);
				//根据班次模板id、座位数、座位类型 查班次模板座位数、座位类型
				List<BasicPrice> list = basicPriceDao.getBusPrice(price);
				if(list.size()>0 && list!=null) {
					for (BasicPrice basicPrice : list) {
						basicPrice.setCompId(req.getCompId());
						basicPrice.setOrgId(req.getOrgId());
						basicPrice.setOnStaId(req.getOnStaId());
						basicPrice.setOffStaId(req.getOffStaId());
						//根据班次模板id,座位类型，座位数,始发站查票价
						List<BasicPrice> basicPriceList = basicPriceList(basicPrice);
						if(basicPriceList.size()>0 && basicPriceList!=null) {
							//删除取值表旧数据，添加取值表
							priceVlue(basicPriceList,req);
						}else {
								//添加票价和取值表
								req.setScheduleTplId(busId);
								req.setSeatCate(basicPrice.getSeatCate());
								req.setSeats(basicPrice.getSeats());
								addBasicPrice(req);
							}
					}
				}
			}
		}
		//当前班次模板
		price.setScheduleTplId(req.getScheduleTplId());
		//根据班次模板id、座位数、座位类型 查班次模板座位数、座位类型
		List<BasicPrice> list = basicPriceDao.getBusPrice(price);
		if(list.size()>0 && list!=null) {
			for (BasicPrice basicPrice : list) {
				basicPrice.setCompId(req.getCompId());
				basicPrice.setOrgId(req.getOrgId());
				basicPrice.setOnStaId(req.getOnStaId());
				basicPrice.setOffStaId(req.getOffStaId());
				//根据班次模板id,座位类型，座位数,始发站查票价
				List<BasicPrice> basicPriceList = basicPriceList(basicPrice);
				if(basicPriceList.size()>0 && basicPriceList!=null) {
					//删除取值表旧数据，添加取值表
					priceVlue(basicPriceList,req);
				}else {
						//添加票价和取值表
						req.setSeatCate(basicPrice.getSeatCate());
						req.setSeats(basicPrice.getSeats());
						addBasicPrice(req);
					}
			}
		}
		return result > 0 ? true : false;
	}
	
	/**
	 * 复制票价
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean copyPrice(BusPriceRequest req) {
		String orgId = req.getOrgId();
		String compId = req.getCompId();
		String scheduleTplId = req.getScheduleTplId();
		List<String> busIdList = req.getBusIdList();
		if(busIdList!=null && busIdList.size()>0) {
			//座位数，座位类型list
			List<SeatCateResp> seatList = req.getSeatList();
			for (SeatCateResp seatCateResp : seatList) {
				//清空req对象
				req = new BusPriceRequest();
				req.setOrgId(orgId);
				req.setCompId(compId);
				req.setSeatCate(seatCateResp.getSeatCate());
				req.setSeats(seatCateResp.getSeats());
				req.setScheduleTplId(scheduleTplId);
				//根据班次模板id,座位类型,座位数查当前班次模板所有票价
				List<BasicPrice> basicPriceList1 = basicPriceList1(req);
				if(basicPriceList1!=null && basicPriceList1.size()>0) {
					for (BasicPrice basicPrice : basicPriceList1) {
						List<HolidayPriceResponse> tvList = new ArrayList<HolidayPriceResponse>();
						//根据票价id查票价取值表
						Wrapper<TicketCateValue> wrapper = new EntityWrapper<TicketCateValue>();
						wrapper.eq("price_tbl_type", "1");
						wrapper.eq("price_id", basicPrice.getId());
						List<TicketCateValue> selectList = ticketCateValueDao.selectList(wrapper);
						if(selectList!=null && selectList.size()>0) {
							//将当前班次模板票价取值数据放到tvList
							for (TicketCateValue tv : selectList) {
								HolidayPriceResponse hps = new HolidayPriceResponse();
								hps.setId(tv.getTicketCateId());
								hps.setTicketValue(tv.getTicketValue());
								tvList.add(hps);
							}
							//当前班次模板票价取值数据
							req.setTvList(tvList);
							//查需要复制的班次模板票价
							for (String busId : busIdList) {
								req.setScheduleTplId(busId);
								req.setOnStaId(basicPrice.getOnStaId());
								req.setOffStaId(basicPrice.getOffStaId());
								//根据班次模板id,座位类型,座位数 查需要复制的班次模板票价
								List<BasicPrice> basicPriceList2 = basicPriceList1(req);
								if(basicPriceList2!=null && basicPriceList2.size()>0) {
									//删除取值表旧数据，添加取值表
									priceVlue(basicPriceList2,req);
								}else {
									//添加票价和取值表
									req.setScheduleTplId(busId);
									addBasicPrice(req);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 删除票价
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delectPrice(List<String> priceId) {
		Integer result = basicPriceDao.deleteBatchIds(priceId);
		if(result>0) {
			TicketCateValue tv = new TicketCateValue();
			for(String pd : priceId) {
				//根据票价id查取值表
				Wrapper<TicketCateValue> wrapper = new EntityWrapper<TicketCateValue>();
				wrapper.eq("price_tbl_type", "1");
				wrapper.eq("price_id", pd);
				List<TicketCateValue> selectList = ticketCateValueDao.selectList(wrapper);
				//删除取值表
				for (TicketCateValue ticketCateValue : selectList) {
					result = ticketCateValueDao.deleteById(ticketCateValue.getId());
					if(result>0) {
						logService.insertLog(OperateLogModelEnum.PRICE.getModule(),"删除票价值","1","被删除票价值ID【"+ticketCateValue.getId()+"】",ticketCateValue.getCreateBy());
					}
				}
			}
		}
		return result > 0 ? true : false;
	}
	
	/**
	 * 根据班次模板id查票价
	 * @param basicPrice
	 * @return
	 */
	public List<BasicPrice> basicPriceList(BasicPrice basicPrice){
		//根据班次模板id查票价
		Wrapper<BasicPrice> wrapper = new EntityWrapper<BasicPrice>();
		if(basicPrice.getOrgId()!=null) {
			wrapper.eq("org_id", basicPrice.getOrgId());
		}
		if(basicPrice.getCompId()!=null) {
			wrapper.eq("comp_id", basicPrice.getCompId());
		}
		wrapper.eq("schedule_tpl_id", basicPrice.getScheduleTplId());
		wrapper.eq("on_sta_id", basicPrice.getOnStaId());
		wrapper.eq("off_sta_id", basicPrice.getOffStaId());
		if(basicPrice.getSeatCate()!=null) {
			wrapper.eq("seat_cate", basicPrice.getSeatCate());
		}
		if(basicPrice.getSeats()!=null) {
			wrapper.eq("seats", basicPrice.getSeats());
		}
		List<BasicPrice> selectList = basicPriceDao.selectList(wrapper);
		return selectList;
	}
	
	/**
	 * 根据条件查票价
	 * @param req
	 * @return
	 */
	public List<BasicPrice> basicPriceList1(BusPriceRequest req){
		//根据条件查票价
		Wrapper<BasicPrice> wrapper = new EntityWrapper<BasicPrice>();
		wrapper.eq("org_id", req.getOrgId());
		wrapper.eq("comp_id", req.getCompId());
		if(req.getPriceId()!=null) {
			wrapper.eq("id", req.getPriceId());
		}
		if(req.getScheduleTplId()!=null) {
			wrapper.eq("schedule_tpl_id", req.getScheduleTplId());
		}
		if(req.getOnStaId()!=null) {
			wrapper.eq("on_sta_id", req.getOnStaId());
		}
		if(req.getOffStaId()!=null) {
			wrapper.eq("off_sta_id", req.getOffStaId());
		}
		if(req.getSeatCate()!=null) {
			wrapper.eq("seat_cate", req.getSeatCate());
		}
		if(req.getSeats()!=null) {
			wrapper.eq("seats", req.getSeats());
		}
		List<BasicPrice> selectList = basicPriceDao.selectList(wrapper);
		return selectList;
	}
	
	/**
	 * 删除取值表旧数据，添加取值表
	 * @param basicPriceList
	 * @param breq
	 */
	public void priceVlue(List<BasicPrice> basicPriceList,BusPriceRequest breq) {
		Integer result = 1;
		for (BasicPrice bPrice : basicPriceList) {
			//根据票价id查取值表
			Wrapper<TicketCateValue> wrapper1 = new EntityWrapper<TicketCateValue>();
			wrapper1.eq("price_tbl_type", "1");
			wrapper1.eq("price_id", bPrice.getId());
			//删除取值表数据
			result = ticketCateValueDao.delete(wrapper1);
			List<HolidayPriceResponse> tvList = breq.getTvList();
			//添加取值表数据
			for(HolidayPriceResponse hp : tvList) {
				if(result >0) {
					TicketCateValue tv = new TicketCateValue();
					tv.setTicketCateId(hp.getId());
					tv.setTicketValue(hp.getTicketValue());
					tv.setId(IdWorker.get32UUID());
					tv.setPriceId(bPrice.getId());
					tv.setPriceTblType("1");
					tv.setCompId(breq.getCompId());
					tv.setOrgId(breq.getOrgId());
					tv.setCreateBy(breq.getCreateBy());
					tv.setUpdateBy(breq.getUpdateBy());
					Integer insert = ticketCateValueDao.insert(tv);
					if(insert>0) {
						logService.insertLog(OperateLogModelEnum.BASICPRICE.getModule(),"修改票价","1","被修改票价ID【"+bPrice.getId()+"】",breq.getCreateBy());
						logService.insertLog(OperateLogModelEnum.PRICE.getModule(),"修改票价值","1","被修改票价值ID【"+tv.getId()+"】",breq.getCreateBy());
					}
				}
			}
		}
	}
	
	/**
	 * 添加票价和票价取值表
	 * @param breq
	 * @return
	 */
	public Map<String,Object> addBasicPrice(BusPriceRequest breq) {
		Map<String,Object> maps=new HashMap<String,Object>();
		//用于同步设置  执行票价List
		List<BasicPrice> basicPriceList=Lists.newArrayList();
		//用于同步设置  执行票价的值 List
		List<TicketCateValue> ticketCateValueList=Lists.newArrayList();
		
		BasicPrice price = new BasicPrice();
		price.setId(IdWorker.get32UUID());
		price.setCompId(breq.getCompId());
		price.setOrgId(breq.getOrgId());
		price.setCreateBy(breq.getCreateBy());
		price.setUpdateBy(breq.getUpdateBy());
		price.setOffStaId(breq.getOffStaId());
		price.setOnStaId(breq.getOnStaId());
		price.setScheduleTplId(breq.getScheduleTplId());
		price.setSeatCate(breq.getSeatCate());
		price.setSeatNo(0);
		price.setSeats(breq.getSeats());
		Integer result = basicPriceDao.insert(price);
		if(result>0) {
			logService.insertLog(OperateLogModelEnum.BASICPRICE.getModule(),"新增票价","1","票价ID【"+price.getId()+"】",breq.getCreateBy());
		}
		basicPriceList.add(price);
		//取值表
		List<HolidayPriceResponse> tvList = breq.getTvList();
		for(HolidayPriceResponse hp : tvList) {
			TicketCateValue tv = new TicketCateValue();
			tv.setTicketCateId(hp.getId());
			tv.setTicketValue(hp.getTicketValue());
			tv.setId(IdWorker.get32UUID());
			tv.setPriceId(price.getId());
			tv.setPriceTblType("1");
			tv.setCompId(breq.getCompId());
			tv.setOrgId(breq.getOrgId());
			tv.setCreateBy(breq.getCreateBy());
			tv.setUpdateBy(breq.getUpdateBy());
			Integer insert = ticketCateValueDao.insert(tv);
			if(insert > 0) {
				logService.insertLog(OperateLogModelEnum.PRICE.getModule(),"新增票价值","1","被修改票价值ID【"+tv.getId()+"】",breq.getCreateBy());
			}
			ticketCateValueList.add(tv);
		}
		maps.put("basicPriceList", basicPriceList);
		maps.put("ticketCateValueList", ticketCateValueList);
		return maps;
	}
	
//	/**
//	 * 获取请求客户端IP
//	 * @return
//	 */
//	protected String getRemoteAddr() {
//		String remoteAddr = WebHelper.getRemoteAddr(request);
//		String ipAddr = WebHelper.getIpAddr(request);
//		return remoteAddr == null ? ipAddr : remoteAddr;
//	}
	
}
