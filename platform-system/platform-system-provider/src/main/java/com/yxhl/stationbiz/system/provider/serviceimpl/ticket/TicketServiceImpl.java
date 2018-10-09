package com.yxhl.stationbiz.system.provider.serviceimpl.ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.utils.PrintBarcodeUtil;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.response.StatisticTCResponse;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
import com.yxhl.stationbiz.system.domain.service.ticket.ScheduleBusSeatsService;
import com.yxhl.stationbiz.system.domain.service.ticket.TicketService;
import com.yxhl.stationbiz.system.provider.dao.schedule.ScheduleBusDao;
import com.yxhl.stationbiz.system.provider.dao.ticket.ScheduleBusSeatsDao;
import com.yxhl.stationbiz.system.provider.dao.ticket.TicketDao;
/**
 * @ClassName: TicketServiceImpl
 * @Description: 售票表 serviceImpl
 * @author lw
 * @date 2018-9-14 10:43:42
 */
@Transactional(readOnly = true)
@Service("ticketService")
public class TicketServiceImpl extends CrudService<TicketDao, Ticket> implements TicketService {

	@Autowired
	private TicketDao ticketDao;
	
	@Autowired
	private ScheduleBusDao scheduleBusDao;

    @Autowired
    private ConfigService configService;
    
    @Autowired
    private ScheduleBusService scheduleBusService;
    
    @Autowired
    private ScheduleBusSeatsService scheduleBusSeatsService;
    
    @Autowired
	private ScheduleBusSeatsDao scheduleBusSeatsDao;
    
	/**
	 * 财务模块/售票分页查询
	 */
	@Override
	public Page<Ticket> selPageList(Page<Ticket> page, Ticket ticket) {
		List<Ticket> list = ticketDao.selPageList(page, ticket);
		for (Ticket tk : list) {
			if(tk.getTicketCateName().equals("全票")) {
				tk.setQpNum(1);
				tk.setZkNum(0);
			}else {
				tk.setZkNum(1);
				tk.setQpNum(0);
			}
		}
		page.setRecords(list);
		return page;
	}

	@Override
	public Page<Ticket> selTicketPageList(Page<Ticket> page, Ticket ticket) {
		List<Ticket> list = ticketDao.selTicketPageList(page, ticket);
		Wrapper<Config> wrapper=new EntityWrapper<Config>();
		wrapper.eq("code", "1011");
		wrapper.eq("org_id", ticket.getOrgId());
		Config config= configService.selectOne(wrapper);//意外伤害
		
		Wrapper<Config> wrapper1=new EntityWrapper<Config>();
		wrapper1.eq("code", "1012");
		wrapper1.eq("org_id", ticket.getOrgId());
		Config config1= configService.selectOne(wrapper1);//意外医疗
		for (Ticket ticket2 : list) {
			if(config!=null) {
				ticket2.setYwsh(String.valueOf(ticket2.getInsuranceCost()*Double.valueOf(config.getValue())));
			}
			if(config1!=null) {
				ticket2.setYwyl(String.valueOf(ticket2.getInsuranceCost()*Double.valueOf(config1.getValue())));
			}
		}
		page.setRecords(list);
		return page;
	}
	/**
	 * 售票模块/废票分页查询
	 */
	@Override
	public Page<Ticket> fpPageList(Page<Ticket> page, Ticket ticket) {
		List<Ticket> list = ticketDao.fpPageList(page, ticket);
		page.setRecords(list);
		return page;
	}

	@Override
	public Page<Ticket> selRefundPageList(Page<Ticket> page, Ticket ticket) throws NumberFormatException, Exception {
		List<Ticket> list = ticketDao.selRefundPageList(page, ticket);
		if(list.size()>0) {
			Ticket t=list.get(0);
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			Integer num=Integer.parseInt(TimeDiff(format.format(System.currentTimeMillis()),DateHelper.formatDate(t.getRunDate(), "yyyy-MM-dd")+" "+DateHelper.formatDate(t.getRunTime(),"HH:mm")));
			Float sxf=0F;
			String fl="";
			Wrapper<Config> wrapper=new EntityWrapper<Config>();
			boolean flag=false;
			if(num>=-120 && num<=-1) {//班次出发两个小时内
				wrapper.eq("code", "1005");
				flag=true;
			}else if(num<-120) {//班次出发超过两个小时
				sxf=t.getPrice();
			}else if(num<=120 && num>=1) {//班次发车前两个小时以内
				wrapper.eq("code", "1004");
				flag=true;
			}else if(num>120) {//班次发车前两个小时以外
				wrapper.eq("code", "1003");
				flag=true;
			}
			if(flag) {
				wrapper.eq("org_id", ticket.getOrgId());
				Config config= configService.selectOne(wrapper);
				if (config != null) {
					fl = Float.valueOf(config.getValue()) * 100 + "%";
					sxf = t.getPrice() * Float.valueOf(config.getValue());
				}
			}
			for (Ticket ticket2 : list) {
				if(ticket2.getStatus().equals("1")) {
					ticket2.setRefundCharge(sxf);
					ticket2.setFl(fl);
				}else if(ticket2.getStatus().equals("3")) {
					ticket2.setFl(ticket2.getRefundCharge()/t.getPrice()+"%");
				}
			}
		}

		page.setRecords(list);
		return page;
	}
	 public static String TimeDiff(String pBeginTime, String pEndTime) throws Exception {
		 SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		  Long beginL = format.parse(pBeginTime).getTime();
		  Long endL = format.parse(pEndTime).getTime();
//		  Long day = (endL - beginL)/86400000;
//		  Long hour = ((endL - beginL)%86400000)/3600000;
		  Long min = ((endL - beginL)%86400000%3600000)/60000;
		  return min+"";
		 }
	 
	/**
	 * 废票
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean scrapTicket(Ticket ticket) {
		if(ticket.getStatus().equals("3") || ticket.getCheckStatus() ==1) {
			throw new YxBizException("废票不能是已检票和已退票!");
		}
		//根据班次id查班次发班状态
		ScheduleBus bus = scheduleBusDao.selectById(ticket.getScheduleBusId());
		//废票不能是已过发班时间2小时
		Date realRunTime = bus.getRealRunTime();
		Date nowDate = new Date();
		long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    // 获得两个时间的毫秒时间差异
	    long diff = nowDate.getTime() - realRunTime.getTime();
	    // 计算差多少小时
	    long hour = diff % nd / nh;
		if(hour>=2) {
			throw new YxBizException("废票不能是已过发班时间2小时!");
		}
		ticket.setStatus("2");
		Integer result = ticketDao.updateById(ticket);
		if(result>0) {
			if(bus!=null && bus.getRunStatus()==0) {
				ScheduleBusSeats seat = new ScheduleBusSeats();
				seat.setSeatStatus("2");
				Wrapper<ScheduleBusSeats> wrapper = new EntityWrapper<ScheduleBusSeats>();
				wrapper.eq("schedule_bus_id", ticket.getScheduleBusId());
				scheduleBusSeatsDao.update(seat, wrapper);
			}
		}
		return result > 0 ? true : false;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public boolean refundCheck(Ticket ticket,String checkUserId) {
		//1票的状态设置为未检票
		ticket.setCheckStatus(0);
		ticket.setCheckScheduleBusId(null);
		ticket.setCheckDate(new Date());
		ticket.setCheckUserId(checkUserId);
		Integer updateFlag = ticketDao.updateAllColumnById(ticket);
		return updateFlag>0;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public boolean checkSingle(Ticket ticket,String checkUserId) {
		ticket.setCheckStatus(1);
		ticket.setCheckScheduleBusId(ticket.getScheduleBusId());
		ticket.setCheckDate(new Date());
		ticket.setCheckUserId(checkUserId);
		Integer updateFlag = ticketDao.updateById(ticket);
		return updateFlag>0;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public boolean mixCheck(String scheduleBusId, Ticket ticket,String checkUserId) {
		ticket.setCheckStatus(1);
		ticket.setCheckScheduleBusId(scheduleBusId);
		ticket.setCheckDate(new Date());
		ticket.setCheckUserId(checkUserId);
		Integer updateFlag = ticketDao.updateById(ticket);
		return updateFlag>0;
	}

	@Override
	public List<Ticket> retreatTicketInfo(Ticket ticket) throws NumberFormatException, Exception{
		List<Ticket> list = new ArrayList<Ticket>();
		Wrapper<Ticket> wrapper= new EntityWrapper<Ticket>();
		wrapper.eq("id", ticket.getId());
		Ticket tick=this.selectOne(wrapper);
		if(tick.getStatus().equals("3") || tick.getStatus().equals("2")) {
			throw new YxBizException("此票已退或已作废!不能重复退票");
		}
		if(tick.getCheckStatus()==1) {
			throw new YxBizException("已检票!不能退票");
		}
		if(ticket.getType().equals("3")) {//退保险
			if(tick.getInsuranceStatus().equals("2") || tick.getInsuranceStatus().equals("3")) {
				throw new YxBizException("保险票已退或已作废!");
			}
		}
		Ticket t1= new Ticket();
		t1.setRefundFee(tick.getPrice());
		if(tick.getInsuranceCost()!=null && tick.getInsuranceCost()!=0F) {
			t1.setInsuranceCost(tick.getInsuranceCost());
			t1.setRefundFeeSum(t1.getRefundFee()+t1.getInsuranceCost());
		}
		t1.setRefundReason("1");
		list.add(t1);//车站原因
		//获取退票所对应的班次
		Wrapper<ScheduleBus> wrapper1= new EntityWrapper<ScheduleBus>();
		wrapper1.eq("id", tick.getScheduleBusId());
		ScheduleBus sche= scheduleBusService.selectOne(wrapper1);
		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Integer num=Integer.parseInt(TimeDiff(format.format(System.currentTimeMillis()),DateHelper.formatDate(sche.getRunDate(), "yyyy-MM-dd")+" "+DateHelper.formatDate(sche.getRunTime(),"HH:mm")));
		Wrapper<Config> wrapper2=new EntityWrapper<Config>();//配置信息
		boolean flag=false;
		if(num>=-120 && num<=-1) {//班次出发两个小时内
			wrapper2.eq("code", "1005");
			flag=true;
		}else if(num<-120) {//班次出发超过两个小时
			throw new YxBizException("已经发车两小时，不允许退票!");
		}else if(num<=120 && num>=1) {//班次发车前两个小时以内
			wrapper2.eq("code", "1004");
			flag=true;
		}else if(num>120) {//班次发车前两个小时以外
			wrapper2.eq("code", "1003");
			flag=true;
		}
		if(flag) {
			wrapper2.eq("org_id", ticket.getOrgId());
			Config config= configService.selectOne(wrapper2);
			Ticket t2= new Ticket();//乘客原因
			t2.setRefundReason("0");
			t2.setRefundFee(tick.getPrice()-(tick.getPrice()*Float.valueOf(config.getValue())));
			if(tick.getInsuranceStatus().equals("1") && tick.getInsuranceCost()!=null && tick.getInsuranceCost()!=0F) {//保险票正常情况下
				t2.setInsuranceCost(tick.getInsuranceCost());
				t2.setRefundFeeSum(t2.getRefundFee()+t1.getInsuranceCost());//应退金额
				t2.setRefundCharge(tick.getPrice()-t2.getRefundFee());//手续费
			}else {
				t2.setRefundFeeSum(t2.getRefundFee());
				t2.setRefundCharge(tick.getPrice()-t2.getRefundFee());//手续费
			}
			list.add(t2);
		}
		return list;
	}
	
	public List<StatisticTCResponse> statisticTCCount(String scheduleBusId) {
		return ticketDao.statisticTCCount(scheduleBusId);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean retreatTicket(Ticket ticket) throws NumberFormatException, Exception {
		List<Ticket> list = new ArrayList<Ticket>();
		Wrapper<Ticket> wrapper= new EntityWrapper<Ticket>();
		wrapper.eq("id", ticket.getId());
		Ticket tick=this.selectOne(wrapper);
		if(tick.getStatus().equals("3") || tick.getStatus().equals("2")) {
			throw new YxBizException("此票已退或已作废!不能重复退票");
		}
		if(tick.getCheckStatus()==1) {
			throw new YxBizException("已检票!不能退票");
		}
		// 获取退票所对应的班次
		Wrapper<ScheduleBus> wrapper1 = new EntityWrapper<ScheduleBus>();
		wrapper1.eq("id", tick.getScheduleBusId());
		ScheduleBus sche = scheduleBusService.selectOne(wrapper1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Integer num=Integer.parseInt(TimeDiff(format.format(System.currentTimeMillis()),DateHelper.formatDate(sche.getRunDate(), "yyyy-MM-dd")+" "+DateHelper.formatDate(sche.getRunTime(),"HH:mm")));
		if(num<-120) {//班次出发超过两个小时
			throw new YxBizException("已经发车两小时，不允许退票!");
		}
		int result=0;
		if(ticket.getType().equals("1") || ticket.getType().equals("2")) {//按票号退票  (需要退票价以及保险金额)
			if(!tick.getInsuranceCode().equals("") && tick.getInsuranceCode()!=null) {//有买保险退保险
				if(tick.getInsuranceStatus().equals("1")) {//正常
					tick.setRefundFee(ticket.getRefundFeeSum());//退保险 加退票
				}else{
					tick.setRefundFee((tick.getRefundFee()==null?0:tick.getRefundFee())+ticket.getRefundFee());
				}
				tick.setInsuranceStatus("3");
			}
			result=1;
		}else {//退保险
			if(tick.getInsuranceStatus().equals("2") || tick.getInsuranceStatus().equals("3")) {
				throw new YxBizException("保险票已退或已作废!");
			}else {
				tick.setRefundFee(ticket.getRefundFee());//等于保险票
			}
		}
		tick.setStatus("3");
		tick.setRefundDate(new Date());
		tick.setRefundReason(ticket.getRefundReason());
		tick.setRefundUserId(ticket.getRefundUserId());
		
		boolean isUpdate= this.updateById(tick);
		if(result==1 && isUpdate) {
			ScheduleBusSeats sch= new ScheduleBusSeats();
			sch.setId(tick.getSeatId());
			sch.setSeatStatus("2");
			scheduleBusSeatsService.updateById(sch);
		}
		return isUpdate;
	}

	/**
	 * 车票打印信息
	 */
	@Override
	public List<Ticket> getcpdyList(String orderId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderId", orderId);
		List<Ticket> getcpdyList = ticketDao.getcpdyList(map);
		for (Ticket ticket : getcpdyList) {
			ticket.setBarCode("data:image/gif;base64,"+PrintBarcodeUtil.generateBarCode128(ticket.getBarCode(), "0.4", "15"));
		}
		return getcpdyList;
	}
}
