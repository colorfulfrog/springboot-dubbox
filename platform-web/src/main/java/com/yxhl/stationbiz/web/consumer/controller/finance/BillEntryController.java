package com.yxhl.stationbiz.web.consumer.controller.finance;

import java.math.BigInteger;
import java.util.List;
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
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.finance.BillEntry;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.finance.BillEntryService;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;


/**
 *	
 *  票据录入表控制器
 *  创建人: ypf
 *  创建日期:2018-9-12 11:16:57
 */
@RestController
@Api(tags = "票据录入表")
@RequestMapping(value = "/m")
public class BillEntryController extends BaseController{
	
    @Autowired
    private BillEntryService billEntryService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/billEntry/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody BillEntry billEntry) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<BillEntry> page = new Page<BillEntry>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<BillEntry> result = billEntryService.selPageList(page, billEntry);
		resp.setData(result);
		List<Dictionary> ticketSourceInsurance = dictionaryService.getRediesByKey("ticket_source_insurance"); //票源单位保险票数据字典
		List<Dictionary> ticketSourceComputer = dictionaryService.getRediesByKey("ticket_source_computer"); //票源单位电脑票数据字典
		List<Dictionary> billType = dictionaryService.getRediesByKey("bill_type");//票据类型
		resp.extendsRes("ticketSourceInsurance", ticketSourceInsurance);
		resp.extendsRes("ticketSourceComputer", ticketSourceComputer);
		resp.extendsRes("billType", billType);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询票据录入表", notes = "根据条件查询票据录入表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/billEntrys", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<BillEntry> wrapper = new EntityWrapper<BillEntry>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<BillEntry> list = billEntryService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增票据录入表", notes = "新增票据录入表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/billEntry", produces = "application/json;charset=UTF-8")
	public CommonResponse addBillEntry(@RequestBody BillEntry billEntry) {
		BigInteger startNum = new BigInteger(billEntry.getStartNum());
		BigInteger endNum = new BigInteger(billEntry.getEndNum());
		if(endNum.compareTo(startNum)==-1) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "票据录入错误!开始号必须要大余等于结束号");
		}
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			billEntry.setOrgId(loginUser.getOrgId());
/*			
			billEntry.setCompId(loginUser.getCompanyId());*/
			billEntry.setCreateBy(loginUser.getId());
			billEntry.setUpdateBy(loginUser.getId());
		}
		billEntry.setId(IdWorker.get32UUID());
		BigInteger remainBI = new BigInteger(billEntry.getEndNum()).subtract(new BigInteger(billEntry.getStartNum())).add(new BigInteger("1"));
		billEntry.setSectionCount(Integer.valueOf(remainBI.toString()));
		billEntry.setRemainCount(Integer.valueOf(remainBI.toString()));
		billEntry.setReceiveUser(loginUser.getId());
		billEntry.setStatus("1");
		boolean isAdded = billEntryService.insert(billEntry);
		if(isAdded) {
			return CommonResponse.createCommonResponse(billEntry);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改票据录入表", notes = "修改票据录入表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/billEntry/{billEntryId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateBillEntry(@PathVariable("billEntryId") String billEntryId,@RequestBody BillEntry billEntry) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			billEntry.setUpdateBy(loginUser.getId());
		}
		billEntry.setId(billEntryId);
		boolean isUpdated = billEntryService.updateById(billEntry);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除票据录入表", notes = "删除票据录入表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/billEntry/{billEntryIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("billEntryIds") List<String> billEntryIds) {
		boolean isDel = billEntryService.deleteBatchIds(billEntryIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看票据录入表详情", notes = "查看票据录入表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/billEntry/{billEntryId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("billEntryId") String billEntryId) {
		BillEntry billEntry = billEntryService.selectById(billEntryId);
		return CommonResponse.createCommonResponse(billEntry);
	}
	
	@ApiOperation(value = "分发到站", notes = "分发到站", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/billEntryStation", produces = "application/json;charset=UTF-8")
	public CommonResponse addBillEntryStation(@RequestBody BillEntry billEntry) {
		BigInteger startNum = new BigInteger(billEntry.getStartNum());
		BigInteger endNum = new BigInteger(billEntry.getEndNum());
		if(endNum.compareTo(startNum)==-1) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "分发错误!开始号必须要大余等于结束号");
		}
		billEntry.getId();//获取选中的票据id
		Wrapper<BillEntry> wrapper=new EntityWrapper<BillEntry>();
		wrapper.eq("id", billEntry.getId());
		BillEntry bigbil=billEntryService.selectOne(wrapper);
		BigInteger oldstartNum = new BigInteger(bigbil.getStartNum());
		BigInteger oldendNum = new BigInteger(bigbil.getEndNum());
		//开始号要大于原有的开始号   结束号要小于原有的结束号
		if(startNum.compareTo(oldstartNum)==-1 ||endNum.compareTo(oldendNum)==1) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "分发错误!");
		}
		Wrapper<BillEntry> wrapper1=new EntityWrapper<BillEntry>();
		wrapper1.eq("parent_id", billEntry.getId());
		List<BillEntry> billList=billEntryService.selectList(wrapper1);//已分发的票
		if(billList.size()>0) {
			for (BillEntry billEntry2 : billList) {
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
			billEntry.setCreateBy(loginUser.getId());
			billEntry.setUpdateBy(loginUser.getId());
		}
		billEntry.setParentId(billEntry.getId());
		billEntry.setId(IdWorker.get32UUID());
		BigInteger remainBI = new BigInteger(billEntry.getEndNum()).subtract(new BigInteger(billEntry.getStartNum())).add(new BigInteger("1"));
		billEntry.setSectionCount(Integer.valueOf(remainBI.toString()));
		billEntry.setRemainCount(Integer.valueOf(remainBI.toString()));
		billEntry.setStatus("1");
		billEntry.setCost(bigbil.getCost());
		billEntry.setRollCount(bigbil.getRollCount());
		billEntry.setBillType(bigbil.getBillType());
		billEntry.setTicketSource(bigbil.getTicketSource());
		boolean isAdded = billEntryService.insert(billEntry);
		if(isAdded) {
			//修改剩余数量
			bigbil.setRemainCount(bigbil.getRemainCount()-billEntry.getRemainCount());
			billEntryService.updateById(bigbil);
			return CommonResponse.createCommonResponse(billEntry);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}
}
