package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.ExecPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;
import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;
import com.yxhl.stationbiz.system.domain.request.HolidayPriceRequest;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusTplService;
import com.yxhl.stationbiz.system.domain.service.schedule.BasicPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCateValueService;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCategoryService;
import com.yxhl.stationbiz.system.provider.dao.schedule.HolidayPriceDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.TicketCateValueDao;
/**
 * @ClassName: HolidayPriceServiceImpl
 * @Description: 节假日票价表 serviceImpl
 * @author ypf
 * @date 2018-8-15 14:20:34
 */
@Transactional(readOnly = true)
@Service("holidayPriceService")
public class HolidayPriceServiceImpl extends CrudService<HolidayPriceDao, HolidayPrice> implements HolidayPriceService {
	@Autowired
	private HolidayPriceDao holidayPriceDao;
	
	@Autowired
	private TicketCategoryService ticketCategoryService;
	
	@Autowired
	private BasicPriceService basicPriceService;
	
	@Autowired
	private TicketCateValueDao ticketCateValueDao;
	
    @Autowired
    private ExecPriceService execPriceService;
    
    @Autowired
    private ScheduleBusService scheduleBusService;
    
    @Autowired
    private HolidayService holidayService;
    
    @Autowired
    private TicketCateValueService ticketCateValueService;
    @Autowired
    private ScheduleBusTplService scheduleBusTplService;
	@Override
	public Page<HolidayPrice> selPageList(Page<HolidayPrice> page, HolidayPrice holidayPrice) {
		List<HolidayPrice> list = holidayPriceDao.selPageList(page, holidayPrice);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<HolidayPrice> selholidayPriceList(HolidayPrice holidayPrice) {
		List<HolidayPrice> list=holidayPriceDao.selholidayPriceList(holidayPrice);
		/*if(list.size()>0) {
			for (HolidayPrice holidayPrice2 : list) {
				TicketCateValue t=new TicketCateValue();
				t.setPriceId(holidayPrice2.getId());
				t.setPriceTblType("2");
				List<TicketCateValue> list1=ticketCateValueService.selPriceValue(t);
				List<HolidayPriceResponse> holist= new ArrayList<HolidayPriceResponse>();
				for (TicketCateValue ticketCateValue : list1) {
					HolidayPriceResponse res= new HolidayPriceResponse();
					res.setColumnId(holidayPrice2.getId());
					res.setScheduleTplId(holidayPrice2.getScheduleTplId());
					res.setId(ticketCateValue.getPriceId());
					res.setTicketCateName(ticketCateValue.getTicketCateName());
					res.setTicketValue(ticketCateValue.getTicketValue());
					res.setFieldName(ticketCateValue.getFieldName());
					holist.add(res);
				}
				holidayPrice2.setHolidayPriceResponse(holist);
//				holidayPrice2.get
			}
		}*/
		return list;
	}
	
	/***
	 * 
	 * @param ids 票价表关联的id集合
	 * @param type 票价类型
	 * @param org_id 所属机构
	 * @param comp_id 所属单位
	 * @return
	 */
	public List<HolidayPriceResponse> dynamic(List<String> ids,String type,String org_id,String comp_id){
		TicketCateValue t=new TicketCateValue();
		t.setIds(ids);
		t.setPriceTblType(type);
		t.setOrgId(org_id);
		t.setCompId(comp_id);
		List<TicketCateValue> list1=ticketCateValueDao.selPriceValue(t);//机构单位下的已有的票价种类以及value
		List<HolidayPriceResponse>  HolidayPriceResponse= new ArrayList<HolidayPriceResponse>();
		if(list1.size()>0) {
			Wrapper<TicketCategory> wrapper = new EntityWrapper<TicketCategory>();
			wrapper.eq("org_id", org_id);
			wrapper.eq("comp_id", comp_id);
			List<TicketCategory> ticketList=ticketCategoryService.selectList(wrapper);//所有的票种
			for (String i : ids) {
				for (TicketCategory ticketCategory : ticketList) {// 所有的票种
					HolidayPriceResponse h = new HolidayPriceResponse();
					boolean flag=false;
					for (TicketCateValue ticketCateValue : list1) {// 已有的票种
						if (ticketCateValue.getPriceId().equals(i)) {// 一个一个比较缺失的票种
							if(ticketCateValue.getTicketCateId().equals(ticketCategory.getId())) {
								flag=true;
								h.setId(ticketCategory.getId());
								h.setFieldName(ticketCateValue.getFieldName());
								h.setTicketCateName(ticketCateValue.getTicketCateName());
								h.setTicketValue(ticketCateValue.getTicketValue());
								h.setRowId(i);//数据行id
								break;
							}
						}
					}
					if(flag) {
						HolidayPriceResponse.add(h);
					}else {
						h.setId(ticketCategory.getId());
						h.setFieldName(ticketCategory.getFieldName());
						h.setTicketCateName(ticketCategory.getTicketCateName());
						h.setRowId(i);// 数据行id
						if(type.equals("1")) {//基础票价
							h.setTicketValue(0F);
							HolidayPriceResponse.add(h);
						}else {//非基础票价
							float ticket_value = forType(i, type, ticketCategory.getId());//获取其他票价里面的价格
							if (ticket_value == 0) {
								h.setTicketValue(0F);
								HolidayPriceResponse.add(h);
							}else {
								h.setTicketValue(ticket_value);
								HolidayPriceResponse.add(h);
							}
							
						}
						
					}
				}
			}
		}
		return HolidayPriceResponse;
	}
	
	public float forType(String i,String type,String id) {
		float ticket_value=0F;
		if(type.equals("3")) {//执行票价类型
			Wrapper<ExecPrice> wrapper = new EntityWrapper<ExecPrice>();
			wrapper.eq("id", i);
			ExecPrice ep=execPriceService.selectOne(wrapper);//根据执行票价在id获取执行票价在数据
			
			Wrapper<ScheduleBus> wrapper1 = new EntityWrapper<ScheduleBus>();
			wrapper1.eq("id", ep.getScheduleBusId());
			ScheduleBus sb=scheduleBusService.selectOne(wrapper1);//根据执行票价在班次id查陈班次摸版
			
			Wrapper<HolidayPrice> wrapper2 = new EntityWrapper<HolidayPrice>();
			wrapper2.eq("schedule_tpl_id", sb.getScheduleTplId());
			wrapper2.eq("on_sta_id", ep.getOnStaId());
			wrapper2.eq("off_sta_id", ep.getOffStaId());
			wrapper2.eq("seat_cate", ep.getSeatCate());
			wrapper2.eq("seats", ep.getSeats());
			wrapper2.eq("org_id", ep.getOrgId());
			wrapper2.eq("comp_id", ep.getCompId());
			HolidayPrice hp=this.selectOne(wrapper2);//根据班次摸版信息查询节日票价
			Wrapper<Holiday> wrapper3 = new EntityWrapper<Holiday>();
			wrapper3.eq("id", hp.getHolidayId());
			Holiday hd = holidayService.selectOne(wrapper3);
			if(hd==null) {//没有节日票价对应数据 则去获取基础票价是否有票价
				Wrapper<BasicPrice> wrapper4 = new EntityWrapper<BasicPrice>();
				wrapper4.eq("schedule_tpl_id", sb.getScheduleTplId());
				wrapper4.eq("on_sta_id", ep.getOnStaId());
				wrapper4.eq("off_sta_id", ep.getOffStaId());
				wrapper4.eq("seat_cate", ep.getSeatCate());
				wrapper4.eq("seats", ep.getSeats());
				wrapper4.eq("org_id", ep.getOrgId());
				wrapper4.eq("comp_id", ep.getCompId());
				BasicPrice bp=basicPriceService.selectOne(wrapper4);//获取基础票价在数据
				if(bp==null) {
					ticket_value= 0F;
				}else {
					TicketCateValue t = new TicketCateValue();
					t.setTicketCateId(id);
					t.setPriceTblType("1");
					t.setOrgId(bp.getOrgId());
					t.setCompId(bp.getCompId());
					t.setPriceId(bp.getId());
					TicketCateValue newT = ticketCateValueDao.selPriceValueForType(t);
					ticket_value=newT.getTicketValue();//基础价格在数据
				}
			}else {//有节假日票价判断是否当前时间是否在节假日时间内
				boolean result=DateHelper.containDate(new Date(), hd.getBeginDate(), hd.getEndDate());
				if (result) {//在(判断节假日础票价是否有票价)
						TicketCateValue t = new TicketCateValue();
						t.setTicketCateId(id);
						t.setPriceTblType("2");
						t.setOrgId(hd.getOrgId());
						t.setCompId(hd.getCompId());
						t.setPriceId(hd.getId());
						TicketCateValue newT = ticketCateValueDao.selPriceValueForType(t);
						if(newT==null) {
							Wrapper<BasicPrice> wrapper4 = new EntityWrapper<BasicPrice>();
							wrapper4.eq("schedule_tpl_id", sb.getScheduleTplId());
							wrapper4.eq("on_sta_id", ep.getOnStaId());
							wrapper4.eq("off_sta_id", ep.getOffStaId());
							wrapper4.eq("seat_cate", ep.getSeatCate());
							wrapper4.eq("seats", ep.getSeats());
							wrapper4.eq("org_id", ep.getOrgId());
							wrapper4.eq("comp_id", ep.getCompId());
							BasicPrice bp=basicPriceService.selectOne(wrapper4);//获取基础票价在数据
							if(bp==null) {
								ticket_value=0F;
							}else {
								TicketCateValue t1 = new TicketCateValue();
								t1.setTicketCateId(id);
								t1.setPriceTblType("1");
								t1.setOrgId(bp.getOrgId());
								t1.setCompId(bp.getCompId());
								t1.setPriceId(bp.getId());
								TicketCateValue newT1 = ticketCateValueDao.selPriceValueForType(t1);
								if(newT1==null) {
									ticket_value=0F;
								}else {
									ticket_value=newT1.getTicketValue();//基础价格在数据
								}
							}
						}else {
							ticket_value = newT.getTicketValue();// 节假日票价票价
						}
				}else {
					Wrapper<BasicPrice> wrapper4 = new EntityWrapper<BasicPrice>();
					wrapper4.eq("schedule_tpl_id", sb.getScheduleTplId());
					wrapper4.eq("on_sta_id", ep.getOnStaId());
					wrapper4.eq("off_sta_id", ep.getOffStaId());
					wrapper4.eq("seat_cate", ep.getSeatCate());
					wrapper4.eq("seats", ep.getSeats());
					wrapper4.eq("org_id", ep.getOrgId());
					wrapper4.eq("comp_id", ep.getCompId());
					BasicPrice bp=basicPriceService.selectOne(wrapper4);//获取基础票价在数据
					if(bp==null) {
						ticket_value=0F;
					}else {
						TicketCateValue t = new TicketCateValue();
						t.setTicketCateId(id);
						t.setPriceTblType("1");
						t.setOrgId(bp.getOrgId());
						t.setCompId(bp.getCompId());
						t.setPriceId(bp.getId());
						TicketCateValue newT = ticketCateValueDao.selPriceValueForType(t);
						if(newT==null) {
							ticket_value=0F;//基础价格在数据
						}else {
							ticket_value=newT.getTicketValue();//基础价格在数据
						}
						
						
					}
				}
			}
		}
		if(type.equals("2")) {
			Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
			wrapper.eq("id", i);
			HolidayPrice ht=this.selectOne(wrapper);//获取当前票价的数据
			Wrapper<BasicPrice> wrapper1 = new EntityWrapper<BasicPrice>();
			wrapper.eq("schedule_tpl_id", ht.getScheduleTplId());
			wrapper.eq("on_sta_id", ht.getOnStaId());
			wrapper.eq("off_sta_id", ht.getOffStaId());
			wrapper.eq("seat_cate", ht.getSeatCate());
			wrapper.eq("seats", ht.getSeats());
			wrapper.eq("org_id", ht.getOrgId());
			wrapper.eq("comp_id", ht.getCompId());
			BasicPrice bp=basicPriceService.selectOne(wrapper1);//获取基础数据的票价信息
			if(bp!=null) {
				TicketCateValue t=new TicketCateValue();
				t.setTicketCateId(id);
				t.setPriceTblType("1");
				t.setOrgId(ht.getOrgId());
				t.setCompId(ht.getCompId());
				t.setPriceId(bp.getId());
				TicketCateValue newT= ticketCateValueDao.selPriceValueForType(t);
				if(newT!=null) {
					ticket_value=newT.getTicketValue();//基础票价
				}
			}
			
		}
		return ticket_value;
	}	

	@Override
	public List<HolidayPriceResponse> dynamicForTitle(String org_id, String comp_id) {
		Wrapper<TicketCategory> wrapper = new EntityWrapper<TicketCategory>();
		wrapper.eq("org_id", org_id);
		wrapper.eq("comp_id", comp_id);
		List<TicketCategory> ticket=ticketCategoryService.selectList(wrapper);
		List<HolidayPriceResponse> list = new ArrayList<HolidayPriceResponse>();
		for (TicketCategory ticketCategory : ticket) {
			HolidayPriceResponse h = new HolidayPriceResponse();
			h.setId(ticketCategory.getId());
			h.setTicketCateName(ticketCategory.getTicketCateName());
			list.add(h);
		}
		return list;
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addHolidayPrice(HolidayPriceRequest req,String userid,String id) throws Exception {
		boolean flag=false;
		List<HolidayPrice> list = req.getHolidayprice();// 节日票价列表
		if (req.getLineNameCopyFlag().equals("0")) {//  0非复制节假日票价设置
			for (HolidayPrice holidayPrice : list) {
				Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
				wrapper.eq("holiday_id", req.getHolidayId());
				wrapper.eq("on_sta_id", holidayPrice.getOnStaId());
				wrapper.eq("off_sta_id", holidayPrice.getOffStaId());
				wrapper.eq("seat_cate", holidayPrice.getSeatCate());
				wrapper.eq("seats", holidayPrice.getSeats());
				List<HolidayPriceResponse> horesponse = holidayPrice.getNewRuleForm();
				for (HolidayPriceResponse holidayPriceResponse : horesponse) {
					Wrapper<TicketCateValue> wrapper1 = new EntityWrapper<TicketCateValue>();
					wrapper1.eq("price_tbl_type", "2");
					wrapper1.eq("price_id", holidayPrice.getId());
					wrapper1.eq("ticket_cate_id", holidayPriceResponse.getId());
					ticketCateValueService.delete(wrapper1);
				}
				wrapper.eq("schedule_tpl_id", id);
				this.delete(wrapper);
			}
			//用于同步设置  执行票价List
			List<HolidayPrice> holidayPriceList=Lists.newArrayList();
			//用于同步设置  执行票价的值 List
			List<TicketCateValue> ticketCateValueList=Lists.newArrayList();
			
			for (HolidayPrice holidayPrice : list) {
				holidayPrice.setId(IdWorker.get32UUID());
				holidayPrice.setHolidayId(req.getHolidayId());
				this.insert(holidayPrice);
				holidayPriceList.add(holidayPrice);
				List<HolidayPriceResponse> holidayList = holidayPrice.getNewRuleForm();
				List<TicketCateValue> listValue = new ArrayList<TicketCateValue>();
				for (HolidayPriceResponse holidayPriceResponse : holidayList) {
					TicketCateValue v = new TicketCateValue();
					v.setOrgId(holidayPrice.getOrgId());
					v.setCompId(holidayPrice.getCompId());
					v.setTicketValue(holidayPriceResponse.getTicketValue());
					v.setPriceTblType("2");
					v.setPriceId(holidayPrice.getId());
					v.setTicketCateId(holidayPriceResponse.getId());
					listValue.add(v);
					
				}
				ticketCateValueList.addAll(listValue);
				flag = ticketCateValueService.insertBatch(listValue);
			}
			//同步设置执行票价
			execPriceService.addExecPriceByHoliday(holidayPriceList, ticketCateValueList);
		} else {
			Wrapper<ScheduleBusTpl> swrapper = new EntityWrapper<ScheduleBusTpl>();
			swrapper.eq("line_id", req.getLineId());
			List<ScheduleBusTpl> sclist = scheduleBusTplService.selectList(swrapper);// 本线路所有的班次
			if (list.size() > 0) {
				for (ScheduleBusTpl scheduletop : sclist) {
					for (HolidayPrice holidayPrice : list) {
						Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
						wrapper.eq("holiday_id", req.getHolidayId());
						wrapper.eq("on_sta_id", holidayPrice.getOnStaId());
						wrapper.eq("off_sta_id", holidayPrice.getOffStaId());
						wrapper.eq("seat_cate", holidayPrice.getSeatCate());
						wrapper.eq("seats", holidayPrice.getSeats());
						List<HolidayPriceResponse> horesponse = holidayPrice.getNewRuleForm();
						for (HolidayPriceResponse holidayPriceResponse : horesponse) {
							Wrapper<TicketCateValue> wrapper1 = new EntityWrapper<TicketCateValue>();
							wrapper1.eq("price_tbl_type", "2");
							wrapper1.eq("price_id", holidayPrice.getId());
							wrapper1.eq("ticket_cate_id", holidayPriceResponse.getId());
							ticketCateValueService.delete(wrapper1);
						}
						wrapper.eq("schedule_tpl_id", scheduletop.getId());
						this.delete(wrapper);
					}
				}
					for (ScheduleBusTpl scheduleBusTpl : sclist) {
						
						//用于同步设置  执行票价List
						List<HolidayPrice> holidayPriceList=Lists.newArrayList();
						//用于同步设置  执行票价的值 List
						List<TicketCateValue> ticketCateValueList=Lists.newArrayList();
						
						for (HolidayPrice holidayPrice : list) {
							holidayPrice.setId(IdWorker.get32UUID());
							holidayPrice.setHolidayId(req.getHolidayId());
							holidayPrice.setScheduleTplId(scheduleBusTpl.getId());
							this.insert(holidayPrice);
							holidayPriceList.add(holidayPrice);
							List<HolidayPriceResponse> holidayList = holidayPrice.getNewRuleForm();
							List<TicketCateValue> listValue = new ArrayList<TicketCateValue>();
							for (HolidayPriceResponse holidayPriceResponse : holidayList) {
								TicketCateValue v = new TicketCateValue();
								v.setOrgId(holidayPrice.getOrgId());
								v.setCompId(holidayPrice.getCompId());
								v.setTicketValue(holidayPriceResponse.getTicketValue());
								v.setPriceTblType("2");
								v.setPriceId(holidayPrice.getId());
								v.setTicketCateId(holidayPriceResponse.getId());
								listValue.add(v);
							}
							ticketCateValueList.addAll(listValue);
							flag = ticketCateValueService.insertBatch(listValue);
						}
						
						//同步设置执行票价
						execPriceService.addExecPriceByHoliday(holidayPriceList, ticketCateValueList);

					}
			}
		}
		return flag;
	}
	@Override
	public String isHolidayPriceSet(ScheduleBus bus) {
		String holidayID = "";
		ScheduleBusTpl tpl = scheduleBusTplService.selectById(bus.getScheduleTplId());
		//查询班次所属公司节假日设置
		EntityWrapper<Holiday> holidayWrapper = new EntityWrapper<Holiday>();
		if(StringUtils.isNotBlank(bus.getOrgId())) {
			holidayWrapper.where("org_id={0}", bus.getOrgId());
		}
		if(StringUtils.isNotBlank(bus.getCompId())) {
			holidayWrapper.and("comp_id={0}", bus.getCompId());
		}
		holidayWrapper.orderBy("update_time", false);
		List<Holiday> holidays = holidayService.selectList(holidayWrapper);
		for (Holiday holiday : holidays) {
			boolean isHoliday = DateHelper.containDate(bus.getRunDate(), holiday.getBeginDate(), holiday.getEndDate());
			if(isHoliday) {
				Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
				wrapper.where("schedule_tpl_id={0} and holiday_id={1}", tpl.getId(),holiday.getId());
				List<HolidayPrice> hpList = holidayPriceDao.selectList(wrapper);
				holidayID = CollectionUtils.isEmpty(hpList) ? "" : holiday.getId();
				break;
			}
		}
		return holidayID;
	}
}
