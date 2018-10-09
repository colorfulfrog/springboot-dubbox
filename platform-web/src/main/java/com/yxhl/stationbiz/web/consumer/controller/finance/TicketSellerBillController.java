package com.yxhl.stationbiz.web.consumer.controller.finance;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hdf.extractor.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.entity.finance.BillEntry;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;
import com.yxhl.stationbiz.system.domain.entity.sys.Company;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.entity.sys.Organization;
import com.yxhl.stationbiz.system.domain.entity.sys.User;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;
import com.yxhl.stationbiz.system.domain.service.finance.BillEntryService;
import com.yxhl.stationbiz.system.domain.service.finance.TicketSellerBillService;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.stationbiz.system.domain.service.sys.CompanyService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.sys.OrganizationService;
import com.yxhl.stationbiz.system.domain.service.sys.UserService;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  售票员票据表控制器
 *  创建人: ypf
 *  创建日期:2018-9-12 11:45:23
 */
@RestController
@Api(tags = "售票员票据表")
@RequestMapping(value = "/m")
public class TicketSellerBillController extends BaseController{
	
    @Autowired
    private TicketSellerBillService ticketSellerBillService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
    private OrganizationService organizationService;
    
	@Autowired
	private CompanyService companyService;
	
    @Autowired
    private StationService stationService;
    
	@Autowired
	private UserService userService;
	
    @Autowired
    private BillEntryService billEntryService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/ticketSellerBill/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody TicketSellerBill ticketSellerBill) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<TicketSellerBill> page = new Page<TicketSellerBill>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<TicketSellerBill> result = ticketSellerBillService.selPageList(page, ticketSellerBill);
		resp.setData(result);
		List<Dictionary> ticketSourceInsurance = dictionaryService.getRediesByKey("ticket_source_insurance"); //票源单位保险票数据字典
		List<Dictionary> ticketSourceComputer = dictionaryService.getRediesByKey("ticket_source_computer"); //票源单位电脑票数据字典
		List<Dictionary> billType = dictionaryService.getRediesByKey("bill_type");//票据类型
		List<Dictionary> billStatus = dictionaryService.getRediesByKey("bill_status");
		resp.extendsRes("ticketSourceInsurance", ticketSourceInsurance);
		resp.extendsRes("ticketSourceComputer", ticketSourceComputer);
		resp.extendsRes("billType", billType);
		resp.extendsRes("billStatus", billStatus);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询售票员票据表", notes = "根据条件查询售票员票据表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticketSellerBills", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("billType") Integer billType) {
		ELUser loginUser = getLoginUser();
		Wrapper<TicketSellerBill> wrapper = new EntityWrapper<TicketSellerBill>();
		wrapper.where("receive_user={0}", loginUser.getId());
		if(billType != null) {
			wrapper.and("bill_type={0}", billType);
		}
		wrapper.and("status='1'"); // 1启用
		List<TicketSellerBill> list = ticketSellerBillService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增售票员票据表", notes = "新增售票员票据表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/ticketSellerBill", produces = "application/json;charset=UTF-8")
	public CommonResponse addTicketSellerBill(@RequestBody TicketSellerBill ticketSellerBill) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketSellerBill.setOrgId(loginUser.getOrgId());
			ticketSellerBill.setCompId(loginUser.getCompanyId());
			ticketSellerBill.setCreateBy(loginUser.getId());
			ticketSellerBill.setUpdateBy(loginUser.getId());
		}
		ticketSellerBill.setId(IdWorker.get32UUID());
		boolean isAdded = ticketSellerBillService.insert(ticketSellerBill);
		if(isAdded) {
			return CommonResponse.createCommonResponse(ticketSellerBill);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改售票员票据表", notes = "修改售票员票据表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketSellerBill/{ticketSellerBillId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateTicketSellerBill(@PathVariable("ticketSellerBillId") String ticketSellerBillId,@RequestBody TicketSellerBill ticketSellerBill) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketSellerBill.setUpdateBy(loginUser.getId());
		}
		ticketSellerBill.setId(ticketSellerBillId);
		boolean isUpdated = ticketSellerBillService.updateById(ticketSellerBill);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除售票员票据表", notes = "删除售票员票据表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/ticketSellerBill/{ticketSellerBillIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("ticketSellerBillIds") List<String> ticketSellerBillIds) {
		boolean isDel = ticketSellerBillService.deleteBatchIds(ticketSellerBillIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看售票员票据表详情", notes = "查看售票员票据表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticketSellerBill/{ticketSellerBillId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("ticketSellerBillId") String ticketSellerBillId) {
		TicketSellerBill ticketSellerBill = ticketSellerBillService.selectById(ticketSellerBillId);
		return CommonResponse.createCommonResponse(ticketSellerBill);
	}
	
	@ApiOperation(value = "票据入库机构菜单", notes = "票据入库机构菜单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/ticketSellerBill/treeForOrgId", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById() {
		ELUser loginUser = getLoginUser();
		Wrapper<Organization> wrapper= new EntityWrapper<Organization>();
		if(loginUser != null) {
			if(!"".equals(loginUser.getOrgId()) && loginUser.getOrgId()!=null) {
				wrapper.eq("id", loginUser.getOrgId());
			}
		}
		List<Organization> organization= organizationService.selectList(wrapper);
		return CommonResponse.createCommonResponse(organization);
	}
	
	@ApiOperation(value = "票据入库机构菜单下面的所有单位", notes = "票据入库机构菜单下面的所有单位", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/getCompanys/{id}", produces = "application/json;charset=UTF-8")
	public CommonResponse getCompanys(@PathVariable("id") String id) {
		Wrapper<Company> wrapper= new EntityWrapper<Company>();
		wrapper.eq("org_id", id);//机构下的单位
		List<Company> companys= companyService.selectList(wrapper);
		return CommonResponse.createCommonResponse(companys);
	}
	
	@ApiOperation(value = "票据入库单位菜单下面的所有站点", notes = "票据入库单位菜单下面的所有站点", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/getStations/{id}", produces = "application/json;charset=UTF-8")
	public CommonResponse getStations(@PathVariable("id") String id) {
		Wrapper<Station> wrapper= new EntityWrapper<Station>();
		wrapper.eq("comp_id", id);//单位下的一级站点,二级站点,三级站点
		wrapper.in("station_level", "0,1,2");
		List<Station> stations= stationService.selectList(wrapper);
		if(stations.size()>0) {
			for (Station station : stations) {
				station.setFullName(station.getStationName());
			}
		}
		return CommonResponse.createCommonResponse(stations);
	}
	
	@ApiOperation(value = "票据入库站点菜单下面的所有售票员", notes = "票据入库站点菜单下面的所有售票员", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/getUsers/{id}", produces = "application/json;charset=UTF-8")
	public CommonResponse getUsers(@PathVariable("id") String id) {
		List<User> users= userService.userTree(id);
		return CommonResponse.createCommonResponse(users);
	}
	
	@ApiOperation(value = "根据公司查询公司下面的财务", notes = "根据公司查询公司下面的财务", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/getFinanceUsers/{id}", produces = "application/json;charset=UTF-8")
	public CommonResponse getFinanceUsers(@PathVariable("id") String id) {
		List<User> users= userService.financeUser(id);
		return CommonResponse.createCommonResponse(users);
	}
	
	@ApiOperation(value = "分公司领票明细分页", notes = "分公司领票明细分页", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/companybillInfo/{id}", produces = "application/json;charset=UTF-8")
	public CommonResponse companybillInfo(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@PathVariable("id") String id) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<TicketSellerBill> page = new Page<TicketSellerBill>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<TicketSellerBill> result = ticketSellerBillService.companybillInfo(page, id);
		resp.setData(result);
		List<Dictionary> ticketSourceInsurance = dictionaryService.getRediesByKey("ticket_source_insurance"); //票源单位保险票数据字典
		List<Dictionary> ticketSourceComputer = dictionaryService.getRediesByKey("ticket_source_computer"); //票源单位电脑票数据字典
		List<Dictionary> billType = dictionaryService.getRediesByKey("bill_type");//票据类型
		List<Dictionary> billStatus = dictionaryService.getRediesByKey("bill_status");//票据类型
		resp.extendsRes("ticketSourceInsurance", ticketSourceInsurance);
		resp.extendsRes("ticketSourceComputer", ticketSourceComputer);
		resp.extendsRes("billType", billType);
		resp.extendsRes("billStatus", billStatus);
		return resp;
	}
	
	@ApiOperation(value = "分发到售票员", notes = "分发到售票员", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/billEntryForconductor", produces = "application/json;charset=UTF-8")
	public CommonResponse addBillEntryForconductor(@RequestBody TicketSellerBill ticketSellerBill) {
		BigInteger startNum = new BigInteger(ticketSellerBill.getStartNum());
		BigInteger endNum = new BigInteger(ticketSellerBill.getEndNum());
		if(endNum.compareTo(startNum)==-1) {//结束号小于开始号
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "分发错误!开始号必须要大余等于结束号");
		}
		ticketSellerBill.getId();//获取选中的票据id
		Wrapper<BillEntry> wrapper=new EntityWrapper<BillEntry>();
		wrapper.eq("id", ticketSellerBill.getId());
		BillEntry bigbil=billEntryService.selectOne(wrapper);
		
		BigInteger oldstartNum = new BigInteger(bigbil.getStartNum());
		BigInteger oldendNum = new BigInteger(bigbil.getEndNum());
		//开始号要大于原有的开始号   结束号要小于原有的结束号
		if(startNum.compareTo(oldstartNum)==-1 ||endNum.compareTo(oldendNum)==1) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "分发错误!");
		}
		Wrapper<TicketSellerBill> wrapper1=new EntityWrapper<TicketSellerBill>();
		wrapper1.eq("bill_entry_id", ticketSellerBill.getId());
		List<TicketSellerBill> billList=ticketSellerBillService.selectList(wrapper1);//已分发的票
		if(billList.size()>0) {
			for (TicketSellerBill billEntry2 : billList) {
				BigInteger billEntry2startNum = new BigInteger(billEntry2.getStartNum());
				BigInteger billEntry2endNum = new BigInteger(billEntry2.getEndNum());
				if ((startNum.compareTo(billEntry2startNum)>=0
						&& startNum.compareTo(billEntry2endNum)<=0)
						|| ((endNum.compareTo(billEntry2endNum)<=0) && endNum.compareTo(billEntry2startNum)>=0)) {
					return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "分发错误!");
				}
			}
		}
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketSellerBill.setCreateBy(loginUser.getId());
			ticketSellerBill.setUpdateBy(loginUser.getId());
		}
		ticketSellerBill.setBillEntryId(ticketSellerBill.getId());
		ticketSellerBill.setId(IdWorker.get32UUID());
		BigInteger remainBI = new BigInteger(ticketSellerBill.getEndNum()).subtract(new BigInteger(ticketSellerBill.getStartNum())).add(new BigInteger("1"));
		ticketSellerBill.setSectionCount(Integer.valueOf(remainBI.toString()));
		ticketSellerBill.setRemainCount(Integer.valueOf(remainBI.toString()));
		ticketSellerBill.setStatus("0");
		ticketSellerBill.setCurrentNum(ticketSellerBill.getStartNum());
		ticketSellerBill.setRollCount(bigbil.getRollCount());
		ticketSellerBill.setBillType(bigbil.getBillType());
		ticketSellerBill.setTicketSource(bigbil.getTicketSource());
		ticketSellerBill.setGrantUser(loginUser.getId());
		boolean isAdded = ticketSellerBillService.insert(ticketSellerBill);
		if(isAdded) {
			//修改剩余数量
			bigbil.setRemainCount(bigbil.getRemainCount()-ticketSellerBill.getRemainCount());
			billEntryService.updateById(bigbil);
			return CommonResponse.createCommonResponse(ticketSellerBill);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}
	
	@ApiOperation(value = "启用", notes = "启用", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/getTrue/{id}", produces = "application/json;charset=UTF-8")
	public CommonResponse getTrue(@PathVariable("id") String id) {
		TicketSellerBill ticketSellerBill= new TicketSellerBill();
		ticketSellerBill.setId(id);
		ticketSellerBill.setStatus("1");
		boolean isUpdate=ticketSellerBillService.updateById(ticketSellerBill);
		if(isUpdate) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "启用失败!");
		}
	}
	
	@ApiOperation(value = "重置售票员票号", notes = "重置售票员票号", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/confirm/{sellerBillId}/{insuranceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse confirmTicketSellerBill(@PathVariable("sellerBillId") String sellerBillId,@PathVariable("insuranceId") String insuranceId,@RequestBody TicketSellerBill ticketSellerBill) {
		ELUser loginUser = getLoginUser();
		TicketSellerBill oldSellerBillTicket= ticketSellerBillService.selectById(sellerBillId);
		TicketSellerBill oldSellerBillInsurance= ticketSellerBillService.selectById(insuranceId);
		checkNotNull(ticketSellerBill,"必需参数不能为空");
		checkNotNull(StringUtils.isNoneBlank(sellerBillId,insuranceId,ticketSellerBill.getCurrentNum(),ticketSellerBill.getReCurrentNum()),"必需参数不能为空");
		checkNotNull(StringUtils.equals(ticketSellerBill.getCurrentNum(), ticketSellerBill.getReCurrentNum()),"当前票号与重复票号不一致");
		checkNotNull(StringUtils.equals(ticketSellerBill.getInCurrentNum(), ticketSellerBill.getReInCurrentNum()),"当前保险票号与重复保险票号不一致");
		checkNotNull(oldSellerBillTicket,"重制的领用票号不存在");
		checkNotNull(oldSellerBillInsurance,"重制的领用票号不存在");
		checkArgument(StringUtils.equals(oldSellerBillTicket.getReceiveUser(), loginUser.getId()),"票号领用人与当前用户不符");
		checkArgument(Long.valueOf(oldSellerBillTicket.getCurrentNum())<=Long.valueOf(ticketSellerBill.getCurrentNum()),"重置票号不能低于当前票号");
		checkArgument(Long.valueOf(ticketSellerBill.getCurrentNum())<=Long.valueOf(oldSellerBillTicket.getEndNum()),"重置票号不能超出领用票号段");
		checkArgument(StringUtils.equals(oldSellerBillInsurance.getReceiveUser(), loginUser.getId()),"保险票号领用人与当前用户不符");
		checkArgument(Long.valueOf(oldSellerBillInsurance.getCurrentNum())<=Long.valueOf(ticketSellerBill.getInCurrentNum()),"当前保险票号不能小于之前票号");
		checkArgument(Long.valueOf(ticketSellerBill.getInCurrentNum())<=Long.valueOf(oldSellerBillInsurance.getEndNum()),"重置当前保险票号不能超出领用票号段");
		checkArgument(StringUtils.equals(oldSellerBillTicket.getBillType(), "1") ,"修改的当前票号有误");//票据类型    1电脑票 2保险票
		checkArgument(StringUtils.equals(oldSellerBillInsurance.getBillType(), "2") ,"修改的当前保险票号有误");//票据类型    1电脑票 2保险票
		
		oldSellerBillTicket.setCurrentNum(ticketSellerBill.getCurrentNum());
		oldSellerBillTicket.setUpdateBy(loginUser.getId());
		oldSellerBillInsurance.setCurrentNum(ticketSellerBill.getInCurrentNum()); 
		oldSellerBillInsurance.setUpdateBy(loginUser.getId());
		List<TicketSellerBill> list =Lists.newArrayList();
		list.add(oldSellerBillTicket);
		list.add(oldSellerBillInsurance);
		boolean isUpdated = ticketSellerBillService.updateBatchById(list);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	
	@ApiOperation(value = "查询售票员重置票号", notes = "查询售票员重置票号", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/getTicketSellerBill", produces = "application/json;charset=UTF-8")
	public CommonResponse getTicketSellerBill() {
		ELUser loginUser = getLoginUser();
		Wrapper<TicketSellerBill> wrapper = new EntityWrapper<TicketSellerBill>();
		wrapper.eq("receive_user", loginUser.getId()).eq("status", 1);//票据状态   status 1启用;
		wrapper.and(" ( bill_type=1 or bill_type=2 ) ");//票据类型    1电脑票 2保险票
		List<TicketSellerBill> list = ticketSellerBillService.selectList(wrapper);
		checkNotNull(list,"没有可用的票据，请确认票据是否有领用或者启用的票据");
		checkArgument(list.size()==2,"票据设置不完整，请确保电脑票和保险票配置好");
		TicketSellerBill dest=new TicketSellerBill();
		for(TicketSellerBill ticketSellerBill:list) {
			if(StringUtils.equals(ticketSellerBill.getBillType(), "1") ) {
				dest.setCurrentNum(ticketSellerBill.getCurrentNum());
				dest.setId(ticketSellerBill.getId()); 
				dest.setRemainCount(ticketSellerBill.getRemainCount());
			}else if(StringUtils.equals(ticketSellerBill.getBillType(), "2") ) {
				dest.setInCurrentNum(ticketSellerBill.getCurrentNum());;
				dest.setInId(ticketSellerBill.getId());
				dest.setInRemainCount(ticketSellerBill.getRemainCount()); 
			}
		}
		list.add(0, dest);
		return CommonResponse.createCommonResponse(list);
	}
	
	
}
