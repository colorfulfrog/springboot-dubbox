package com.yxhl.stationbiz.web.consumer.controller.finance;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.finance.Payment;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.response.PaymentResp;
import com.yxhl.stationbiz.system.domain.service.finance.PaymentService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  缴款表控制器
 *  创建人: xjh
 *  创建日期:2018-9-14 11:26:25
 */
@RestController
@Api(tags = "缴款表")
@RequestMapping(value = "/m/payment")
public class PaymentController extends BaseController{
	
    @Autowired
    private PaymentService paymentService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Payment payment) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		if(loginUser!=null) {
			payment.setOrgId(loginUser.getOrgId());
			payment.setCompId(loginUser.getCompanyId());
		}
		Page<Payment> page = new Page<Payment>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Payment> result = paymentService.selPageList(page, payment);
		resp.setData(result);
		List<Dictionary> paymentTypeList = dictionaryService.getRediesByKey("payment_type"); //查询缴款类型字典
		resp.extendsRes("paymentTypeList", paymentTypeList);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询缴款表", notes = "根据条件查询缴款表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Payment payment) {
		Wrapper<Payment> wrapper = new EntityWrapper<Payment>();
		if(StringUtils.isNotBlank(payment.getPayer())) {
			wrapper.eq("payer", payment.getPayer());
		}
		if(StringUtils.isNotBlank(getLoginUser().getOrgId())) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		if(StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			wrapper.eq("comp_id", getLoginUser().getCompanyId());
		}
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<Payment> list = paymentService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增缴款表", notes = "新增缴款表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addPayment(@RequestBody Payment payment) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			payment.setOrgId(loginUser.getOrgId());
			payment.setCompId(loginUser.getCompanyId());
			payment.setCreateBy(loginUser.getId());
			payment.setUpdateBy(loginUser.getId());
			payment.setPayee(loginUser.getId());//收款人
		}
		payment.setId(IdWorker.get32UUID());
		boolean isAdded = paymentService.add(payment);
		if(isAdded) {
			return CommonResponse.createCommonResponse(payment);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改缴款表", notes = "修改缴款表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{paymentId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updatePayment(@PathVariable("paymentId") String paymentId,@RequestBody Payment payment) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			payment.setUpdateBy(loginUser.getId());
		}
		payment.setId(paymentId);
		payment.setPaymentStatus(1);
		boolean isUpdated = paymentService.updateById(payment);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除缴款表", notes = "删除缴款表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{paymentIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("paymentIds") List<String> paymentIds) {
		boolean isDel = paymentService.deleteBatchIds(paymentIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看缴款表详情", notes = "查看缴款表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{paymentId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("paymentId") String paymentId) {
		Payment payment = paymentService.selectById(paymentId);
		return CommonResponse.createCommonResponse(payment);
	}
	
	@ApiOperation("售票模块/缴款分页查询")
	@PostMapping(value = "/jkPage", produces = "application/json;charset=UTF-8")
	public CommonResponse jkPage(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Payment payment) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		if(loginUser!=null) {
			payment.setPayer(loginUser.getId());
			payment.setOrgId(loginUser.getOrgId());
			payment.setCompId(loginUser.getCompanyId());
		}
		Page<PaymentResp> page = new Page<PaymentResp>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<PaymentResp> result = paymentService.jkPageList(page, payment);
		resp.setData(result);
		List<Dictionary> paymentTypeList = dictionaryService.getRediesByKey("payment_type"); //查询缴款类型字典
		resp.extendsRes("paymentTypeList", paymentTypeList);
		return resp;
	}
}
