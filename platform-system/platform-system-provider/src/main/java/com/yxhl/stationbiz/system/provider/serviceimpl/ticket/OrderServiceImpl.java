package com.yxhl.stationbiz.system.provider.serviceimpl.ticket;

import static com.google.common.base.Preconditions.checkArgument;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.utils.IdCardUtils;
import com.yxhl.platform.common.utils.NumberHelper;
import com.yxhl.platform.common.utils.RedeemCodeUtils;

import org.springframework.transaction.annotation.Transactional;

import com.yxhl.stationbiz.system.provider.dao.finance.TicketSellerBillDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.ScheduleBusDao;
import com.yxhl.stationbiz.system.provider.dao.sys.ConfigDao;
import com.yxhl.stationbiz.system.provider.dao.sys.DictionaryDao;
import com.yxhl.stationbiz.system.provider.dao.ticket.OrderDao;
import com.yxhl.stationbiz.system.provider.dao.ticket.ScheduleBusSeatsDao;
import com.yxhl.stationbiz.system.provider.dao.ticket.TicketDao;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.request.TicketOrderRequest;
import com.yxhl.stationbiz.system.domain.response.TicketOrderResponse;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.ticket.OrderService;

/**
 * @ClassName: OrderServiceImpl
 * @Description: 订单表 serviceImpl
 * @author lw
 * @date 2018-9-15 10:39:27
 */
@Transactional(readOnly = true)
@Service("orderService")
public class OrderServiceImpl extends CrudService<OrderDao, Order> implements OrderService {
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private ScheduleBusDao scheduleBusDao;// 班次

	@Autowired
	private ConfigDao configDao;// 参数配置

	@Autowired
	private TicketDao ticketDao;// 售票

	@Autowired
	private TicketSellerBillDao ticketSellerBillDao;// 售票员票据

	@Autowired
	private DictionaryDao dictionaryDao;// 字典

	@Autowired
	private ScheduleBusSeatsDao scheduleBusSeatsDao;// 班次座位

	@Autowired
	private ExecPriceService execPriceService;// 票价

	@Override
	public Page<Order> selPageList(Page<Order> page, Order order) {
		List<Order> list = orderDao.selPageList(page, order);
		page.setRecords(list);
		return page;
	}

	@Override
	public JSONObject getOrderCost(TicketOrderRequest ticketOrderRequest, ELUser loginUser) {
		double d = 0;
		// 根据班次ID获取班次单条记录
		List<ScheduleBus> buss = this.getScheduleBuss(ticketOrderRequest);
		checkArgument((buss != null && buss.size() > 0), "操作失败，获取班次信息出错");
		ScheduleBus getScheduleBus = buss.get(0);
		checkArgument(getScheduleBus.getBusStatus() == 1, "操作失败，班次状态不是正常状态");
		checkArgument((getScheduleBus.getTicketCateValues() != null && getScheduleBus.getTicketCateValues().size() > 0),
				"操作失败，获取执行票价出错");
		checkArgument((ticketOrderRequest.getTicketCateValues() != null
				&& ticketOrderRequest.getTicketCateValues().size() > 0), "操作失败，票种集合获取失败");
		// 不同类型单张费用(查询)
		Map<String, Object> maps = new HashMap<String, Object>();
		for (TicketCateValue cateValue : getScheduleBus.getTicketCateValues()) {
			maps.put(cateValue.getTicketCateId(), cateValue.getTicketValue() != null ? cateValue.getTicketValue() : 0);
		}
		// 不同类型单张费用(获取)
		Map<String, Object> returnMaps = new HashMap<String, Object>();
		int bxcount = 0;// 实际人数
		for (TicketCateValue cateValue : ticketOrderRequest.getTicketCateValues()) {
			// 实际票价
			d += NumberHelper.mul(cateValue.getNum(),
					Double.parseDouble(maps.get(cateValue.getTicketCateId()).toString()));//
			returnMaps.put(cateValue.getTicketCateId(), maps.get(cateValue.getTicketCateId()));
			bxcount += cateValue.getNum();
		}

		JSONObject j = new JSONObject();
		j.put("bxCost", 0);// 单张保险票费用
		// 是否购买保险票
		if (ticketOrderRequest.getIsPurchase() == 1) {
			checkArgument(!(bxcount > ticketOrderRequest.getPurchaseNum()), "操作失败，保险票数量大于乘车人数");
			Config config = null;
			if (getScheduleBus.getLineMileage() <= 100) {// 线路里程 100公里以内
				config = configDao.selByCode(loginUser.getOrgId(), "1014");
			} else
				config = configDao.selByCode(loginUser.getOrgId(), "1015");
			checkArgument(config != null, "操作失败，保险票费用没有进行配置");
			double purchaseCost = NumberHelper.mul(ticketOrderRequest.getPurchaseNum(),
					Double.parseDouble(config.getValue()));// 保险费用
			// 实际票价 + 保险费用
			d += NumberHelper.add(d, purchaseCost);
			j.put("bxCost", config.getValue());// 单张保险票费用
		}
		j.put("sumCost", d);// 总费用
		j.put("ticketCateValues", returnMaps);// 每个票种独立费用
		return j;
	}

	@Override
	public ScheduleBus checkPlaceOrder(TicketOrderRequest ticketOrderRequest, ELUser loginUser, String type) {
		checkArgument(ticketOrderRequest.getOrder() == null, "操作失败，订单信息获取失败!");
		checkArgument(!(ticketOrderRequest.getTickets() != null && ticketOrderRequest.getTickets().size() > 0),
				"操作失败，乘车人信息获取失败!");
		// 根据班次ID获取班次单条记录
		List<ScheduleBus> buss = this.getScheduleBuss(ticketOrderRequest);
		checkArgument((buss != null && buss.size() > 0), "操作失败，获取班次信息出错");
		ScheduleBus getScheduleBus = buss.get(0);
		checkArgument(!(getScheduleBus.getBusStatus() != 1), "操作失败，班次状态不是正常状态");
		checkArgument(!(getScheduleBus.getTicketCateValues() != null), "操作失败，获取执行票价失败");
		if (type.equals("1")) {// 窗口售票F3
			Config config = configDao.selByCode(loginUser.getOrgId(), "1001");// 停止售票
			checkArgument(config == null, "操作失败，停止售票时间没有进行配置");
			this.checkSetting("已停止购票", getScheduleBus, config);
		} else if (type.equals("3")) {// 改签F8
			checkArgument(ticketOrderRequest.getOldTicketId() != null, "操作失败，原售票信息获取失败");
			Config config = configDao.selByCode(loginUser.getOrgId(), "1002");// 停止改签
			checkArgument(config == null, "操作失败，停止改签时间没有进行配置");
			this.checkSetting("已停止改签", getScheduleBus, config);
		}
		return getScheduleBus;
	}

	private void checkSetting(String error, ScheduleBus getScheduleBus, Config config) {
		String rundate = DateHelper.getTime(getScheduleBus.getRunDate());
		String runTime = DateHelper.getTime(getScheduleBus.getRunTime());
		try {
			int i = DateHelper.compareDate(DateHelper.getOldOrNewTime(Integer.parseInt(config.getValue())),
					rundate + " " + runTime);
			checkArgument(i != -1, "操作失败，" + error);
		} catch (NumberFormatException e) {
			checkArgument(false, "操作失败，数字类型转换失败");
			e.printStackTrace();
		} catch (ParseException e) {
			checkArgument(false, "操作失败，日期转换失败");
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String addPlaceOrder(TicketOrderRequest ticketOrderRequest, ELUser loginUser, ScheduleBus scheduleBus) {
		// 根据班次ID获取班次单条记录
		ScheduleBus getScheduleBus = scheduleBus;
		// 入参订单对象
		Order order = ticketOrderRequest.getOrder();
		// 获取保险票和电脑票
		checkArgument(ticketOrderRequest.getIsPurchase() != null, "操作失败，是否购买保险状态获取失败");
		long insuranceCode = 0;// 保险当前票号
		Dictionary dictionary = null;// 保险公司字典
		TicketSellerBill ticketSellerBillBx = null;// 保险对象
		if (ticketOrderRequest.getIsPurchase() == 1) {// 购买保险
			Wrapper<TicketSellerBill> wrapperBx = new EntityWrapper<TicketSellerBill>();
			wrapperBx.where("receive_user={0}", loginUser.getId());//
			wrapperBx.and("bill_type={1}", 2);// 保险票
			List<TicketSellerBill> ticketSellerBillsBx = ticketSellerBillDao.selectList(wrapperBx);
			if (!(ticketSellerBillsBx != null && ticketSellerBillsBx.size() == 1))
				checkArgument(false, "操作失败，售票员票据未配置或者配置出错");
			ticketSellerBillBx = ticketSellerBillsBx.get(0);// 保险对象
			if (!(ticketSellerBillBx != null && (ticketSellerBillBx.getStatus()).equals("1")))
				checkArgument(false, "操作失败，当前保险票号为非启用状态");
			if (!(ticketSellerBillBx != null && ticketSellerBillBx.getRemainCount() != null
					&& ticketSellerBillBx.getRemainCount() == 0))
				checkArgument(false, "操作失败，当前保险票号剩余数量为0");
			if (order.getAmount() > ticketSellerBillBx.getRemainCount())
				checkArgument(false, "操作失败，当前保险票号剩余数量不够");
			// 获取保险名称
			Wrapper<Dictionary> wrapperd = new EntityWrapper<Dictionary>();
			wrapperd.where("config_key={0}", ticketSellerBillBx.getTicketSource());// 保险票key（ticket_source_insurance）
			List<Dictionary> dictionarys = dictionaryDao.selectList(wrapperd);
			if (!(dictionarys != null && dictionarys.size() == 1))
				checkArgument(false, "操作失败，保险公司字典没有配置或者配置出错");
			dictionary = dictionarys.get(0);
			insuranceCode = Long.parseLong(ticketSellerBillBx.getCurrentNum());// 保险当前票号
		}
		Wrapper<TicketSellerBill> wrapperDn = new EntityWrapper<TicketSellerBill>();
		wrapperDn.where("receive_user={0}", loginUser.getId());//
		wrapperDn.and("bill_type={1}", 1);// 电脑票
		List<TicketSellerBill> ticketSellerBillsDn = ticketSellerBillDao.selectList(wrapperDn);
		if (!(ticketSellerBillsDn != null && ticketSellerBillsDn.size() == 1))
			checkArgument(false, "操作失败，售票员票据未配置或者配置出错");
		TicketSellerBill ticketSellerBillDn = ticketSellerBillsDn.get(0);// 电脑对象
		if (!(ticketSellerBillDn != null && (ticketSellerBillDn.getStatus()).equals("1")))
			checkArgument(false, "操作失败，当前车票号为非启用状态");
		if (!(ticketSellerBillDn != null && ticketSellerBillDn.getRemainCount() != null
				&& ticketSellerBillDn.getRemainCount() == 0))
			checkArgument(false, "操作失败，当前车票号剩余数量为0");
		if (order.getAmount() > ticketSellerBillDn.getRemainCount())
			checkArgument(false, "操作失败，当前车票号剩余数量不够");

		String orderId = DateHelper.getConcatYmdhms() + System.currentTimeMillis();
		order.setOrderCode(orderId);// 订单编号
		order.setOrgId(loginUser.getOrgId());// 所属机构
		order.setCompId(loginUser.getCompanyId());// 所属单位
		// order.setScheduleBusId(scheduleBusId);//所属班次数 -(界面取值)
		// order.setUserId(userId);//用户编号
		// order.setMobile(mobile);//手机号码
		order.setBusCode(getScheduleBus.getBusCode());// 班次号
		order.setStart(getScheduleBus.getStartStation());// 出发地名称
		order.setDestination(getScheduleBus.getEndStation());// 目的地名称
		order.setStartStationId(getScheduleBus.getStartStationId());// 出发站点编码
		order.setEndStationId(getScheduleBus.getEndStationId());// 目的地站点编码
		// order.setChildren(children);//携童数-(界面取值)
		// order.setAmount(amount);//人数-(界面取值)
		// order.setSeatNo(seatNo);//座位号,多张票如（23,24）-(界面取值)
		order.setVerifyCode(RedeemCodeUtils.createBigStrOrNumberRadom(6));// 取票码
		// order.setTotalFee(totalFee);//支付总金额 -(界面取值)
		order.setDiscountFee(0F);// 拆扣费用
		order.setServiceFee(0F);// 技术服务费
		order.setRefundFee(0F);// 退款金额
		order.setRefundCharge(0F);// 退款手续费
		// order.setRefundStatus("1");//退款状态 1申请退款 2、退款中 3、退款完成
		// order.setRefundDate(null);//退款时间
		order.setBizType("1");// 业务类型 1 汽车大巴
		order.setPayStatus(1);// 支付状态（1已下单 2已支付 3取消 ）
		order.setPayDate(new Date(0));// 支付时间
		// order.setOutPayNo(outPayNo);//支付(平台)订单号
		order.setOutPayType("CASH");// CASH(现金)、WECHAT(微信)、ALIPAY(支付宝)
		// order.setPaySellerId(paySellerId);//支付平台商户id
		// order.setPayBuyerId(payBuyerId);//乘客支付id
		// order.setFailReason(failReason);//购票失败原因
		order.setVersion(1);// 版本号
		// order.setAttributes(attributes);//扩展属性：存放出发地目的地（经纬度）等
		order.setSellWay(0);// 0窗口、1网络、2自助
		order.setSellType(0);// 售票类型 0固定单售票 1固定单补票 2流水班补票 3退票
		// order.setRemark(remark);//备注
		order.setCreateBy(loginUser.getId());// 创建人
		orderDao.insert(order);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("orderId", orderId);
		maps.put("scheduleBusId", order.getScheduleBusId());
		long invoiceNo = Long.parseLong(ticketSellerBillDn.getCurrentNum());// 电脑
		List<String> ids = new ArrayList<String>();
		for (Ticket ticket : ticketOrderRequest.getTickets()) {
			Ticket getTicket = this.getTicket(ticket, loginUser, maps, getScheduleBus);
			if (ticketOrderRequest.getIsPurchase() == 1) {// 购买保险
				getTicket.setInsuranceCompany(dictionary.getKeyName());// 保险公司
				getTicket.setInsuranceCode(insuranceCode + "");// 保险票号
				insuranceCode++;
			}
			ticket.setInvoiceNo(invoiceNo + "");// 发票号码(电脑票)
			invoiceNo++;
			ticket.setBarCode(ticket.getInvoiceNo());// 条码(电脑票)
			ticketDao.insert(getTicket);
			ids.add(getTicket.getSeatId());// 座位号
		}
		// 更新当前电脑票号
		ticketSellerBillDn.setCurrentNum(invoiceNo + "");// 当前电脑票号
		ticketSellerBillDn
				.setRemainCount(ticketSellerBillDn.getRemainCount() - order.getAmount() + order.getChildren());// 剩余票号
		if (ticketSellerBillDn.getRemainCount() == 0)
			ticketSellerBillDn.setStatus("2");// 2用完
		ticketSellerBillDao.updateById(ticketSellerBillDn);
		// 更新当前保险票号
		if (ticketOrderRequest.getIsPurchase() == 1) {// 购买保险
			ticketSellerBillBx.setCurrentNum(insuranceCode + "");// 当前保险票号
			ticketSellerBillBx
					.setRemainCount(ticketSellerBillBx.getRemainCount() - order.getAmount() + order.getChildren());// 剩余票号
			if (ticketSellerBillBx.getRemainCount() == 0)
				ticketSellerBillBx.setStatus("2");// 2用完
			ticketSellerBillDao.updateById(ticketSellerBillBx);
		}
		// 更新座位为已售
		scheduleBusSeatsDao.updateScheduleBusSeatStatus(ids);
		return orderId;
	}

	// 根据班次ID获取班次单条记录
	private List<ScheduleBus> getScheduleBuss(TicketOrderRequest ticketOrderRequest) {
		ScheduleBus scheduleBus = new ScheduleBus();
		scheduleBus.setScheduleBusId(ticketOrderRequest.getScheduleBusId());
		scheduleBus.setStartStationId(ticketOrderRequest.getStartStationId());
		scheduleBus.setRunDateStr(ticketOrderRequest.getRunDateStr());
		scheduleBus.setReportStaId(ticketOrderRequest.getReportStaId());
		Page<ScheduleBus> page = new Page<ScheduleBus>(1, 1);
		List<ScheduleBus> buss = scheduleBusDao.queryScheduleBusList(page, scheduleBus);
		for (ScheduleBus bus : buss) {
			// 查询执行票价
			List<TicketCateValue> ticketCateValues = execPriceService.selExecPriceBy(bus.getId(), bus.getVehicleType(),
					bus.getSeats());
			bus.setTicketCateValues(ticketCateValues);
		}
		return buss;
	}

	// 售票信息赋值
	private Ticket getTicket(Ticket ticket, ELUser loginUser, Map<String, Object> maps, ScheduleBus getScheduleBus) {
		ticket.setOrgId(loginUser.getOrgId());
		ticket.setCompId(loginUser.getCompanyId());
		ticket.setOrderId(maps.get("orderId").toString());// 所属订单ID
		// ticket.setName(name);//乘车人姓名 -(界面取值)
		int idCardNo = ticket.getIdCardNo().length();
		try {
			if (idCardNo == 15) {
				Map<String, Object> idcardMaps = IdCardUtils.identityCard15(ticket.getIdCardNo());
				ticket.setSex(idcardMaps.get("sex").toString());// 女，男
			} else {
				Map<String, Object> idcardMaps = IdCardUtils.identityCard18(ticket.getIdCardNo());
				ticket.setSex(idcardMaps.get("sex").toString());// 女，男
			}
		} catch (Exception e) {
			e.printStackTrace();
			checkArgument(false, "操作失败，身份证号码转换失败");
		}
		// ticket.setIdCardNo(idCardNo);//身份证号 -(界面取值)
		// ticket.setMobile(mobile);//电话号码
		// ticket.setPrice(price);//票价-(界面取值)
		// ticket.setCheckScheduleBusId(checkScheduleBusId);//检票班次
		ticket.setScheduleBusId(maps.get("scheduleBusId").toString());// 所属班次
		// ticket.setSeatId(seatId);//所属座位号id -(界面取值)
		// ticket.setInsuranceCost(insuranceCost);//保险费用-- (界面取值)
		ticket.setInsuranceStatus("1");// 保险状态 1正常 2作废 3退保
		// ticket.setTicketCategoryId(ticketCategoryId);//所属票种- 界面传值
		ticket.setPayStatus(1);// 支付状态（1未支付 2已支付 ）
		ticket.setTicketCheck(getScheduleBus.getTicketGateName());// 检票口（名称）
		ticket.setBusGarage(getScheduleBus.getBusEntranceName());// 乘车库(名称)
																	// （上车口）
		ticket.setStationId(loginUser.getStationId());// 售票点
		ticket.setNetTicketFlag(0);// 0 否，1是
		ticket.setNetName("");// 互联网名称
		ticket.setTakeTicketFlag(1);// 0未取票 1已取票
		ticket.setTakeTicketDate(new Date(0));// 取票时间
		// ticket.setCheckDate(checkDate);//检票时间
		ticket.setCheckStatus(0);// 检票状态 0未检 1已检
		// ticket.setCheckUserId(checkUserId);//检票员
		ticket.setRefundFee(0F);// 退款金额
		ticket.setRefundCharge(0F);// 退款手续费
		// ticket.setRefundUserId(refundUserId);//退票员/废票员
		ticket.setStatus("1");// 票状态（0锁票 1正常 2、废票 3、退票）
		// ticket.setRefundDate(refundDate);//退款时间/废票时间
		ticket.setSellerType(0);// 0窗口、1网络、2自助
		ticket.setSellerUserId(loginUser.getId());// 售票员
		// ticket.setRemark(remark);//备注
		ticket.setFuelFee(0F);// 燃油费
		ticket.setCreateBy(loginUser.getId());// 创建人
		// ticket.setAbolishReason(abolishReason);//废票原因
		// ticket.setRefundReason(refundReason);//退票原因 0乘客原因 1车站
		return ticket;
	}

	@Override
	public TicketOrderResponse getOrderAndTicket(String invoiceNo) {
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		wrapper.where("invoice_no={0}", invoiceNo);//
		List<Ticket> tickets = ticketDao.selectList(wrapper);
		if (tickets != null && tickets.size() == 1) {
			Ticket ticket = tickets.get(0);
			Wrapper<Order> wrapperOrder = new EntityWrapper<Order>();
			wrapperOrder.where("order_code={0}", ticket.getOrderId());//
			List<Order> orders = orderDao.selectList(wrapperOrder);
			if (tickets != null && tickets.size() == 1) {
				TicketOrderResponse response = new TicketOrderResponse();
				response.setOrder(orders.get(0));
				response.setTickets(tickets);
				return response;
			}
		}
		return null;
	}

	/**
	 * 改签F9-下单
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String resetAddPlaceOrder(TicketOrderRequest ticketOrderRequest, ELUser loginUser,
			ScheduleBus getScheduleBus) {
		String orderId = this.addPlaceOrder(ticketOrderRequest, loginUser, getScheduleBus);
		Ticket ticket = ticketDao.selectById(ticketOrderRequest.getOldTicketId());
		ticket.setStatus("2");// 票状态（0锁票 1正常 2、废票 3、退票）
		ticket.setRefundUserId(loginUser.getId());// 废票员
		ticket.setRefundDate(new Date(0));// 废票时间
		ticket.setUpdateTime(new Date(0));// 更新时间
		ticket.setUpdateBy(loginUser.getId());// 更新人
		ticket.setAbolishReason("乘客改签");// 废票原因
		// 更新为废票
		ticketDao.updateById(ticket);
		// 还原座位可选
		ScheduleBusSeats scheduleBusSeats = new ScheduleBusSeats();
		scheduleBusSeats.setId(ticket.getSeatId());
		scheduleBusSeats.setSeatStatus("2");// 状态(数据字典)：2可选 3预留 4不售 5已售
		ticket.setUpdateTime(new Date(0));// 更新时间
		ticket.setUpdateBy(loginUser.getId());// 更新人
		scheduleBusSeatsDao.updateById(scheduleBusSeats);
		return orderId;
	}
}
