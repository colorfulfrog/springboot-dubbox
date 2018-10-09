package com.yxhl.stationbiz.web.consumer.controller.ticket;

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
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.service.ticket.OrderService;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;


/**
 *	
 *  订单表控制器
 *  创建人: lw
 *  创建日期:2018-9-15 10:39:27
 */
@RestController
@Api(tags = "订单表")
@RequestMapping(value = "/m")
public class OrderController extends BaseController{
	
    @Autowired
    private OrderService orderService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/order/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Order order) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Order> page = new Page<Order>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Order> result = orderService.selPageList(page, order);
		resp.setData(result);
		/*List<Dictionary> loopTypeList = dictionaryService.getRediesByKey("loop_typeint"); //查询循环类型字典
		resp.extendsRes("loopType", loopTypeList);*/
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询订单表", notes = "根据条件查询订单表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/orders", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<Order> wrapper = new EntityWrapper<Order>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<Order> list = orderService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增订单表", notes = "新增订单表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/order", produces = "application/json;charset=UTF-8")
	public CommonResponse addOrder(@RequestBody Order order) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			order.setOrgId(loginUser.getOrgId());
			order.setCompId(loginUser.getCompanyId());
			order.setCreateBy(loginUser.getId());
			order.setUpdateBy(loginUser.getId());
		}
		order.setId(IdWorker.get32UUID());
		boolean isAdded = orderService.insert(order);
		if(isAdded) {
			return CommonResponse.createCommonResponse(order);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改订单表", notes = "修改订单表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/order/{orderId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateOrder(@PathVariable("orderId") String orderId,@RequestBody Order order) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			order.setUpdateBy(loginUser.getId());
		}
		order.setId(orderId);
		boolean isUpdated = orderService.updateById(order);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除订单表", notes = "删除订单表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/order/{orderIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("orderIds") List<String> orderIds) {
		boolean isDel = orderService.deleteBatchIds(orderIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看订单表详情", notes = "查看订单表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/order/{orderId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("orderId") String orderId) {
		Order order = orderService.selectById(orderId);
		return CommonResponse.createCommonResponse(order);
	}
}
