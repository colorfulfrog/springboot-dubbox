package com.yxhl.stationbiz.system.domain.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 结算单-各票种售票数
 * @author lw
 */
@Data
public class TicketCategoryCountResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "票种ID")
	private String ticketCategoryId;
	
	@ApiModelProperty(value = "票种名称")
	private java.lang.String ticketCateName;
	
	@ApiModelProperty(value = "票种代码")
	private java.lang.String ticketCateCode;
	
	@ApiModelProperty(value = "售票数量")
	private int saleCount;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketCategoryCountResponse other = (TicketCategoryCountResponse) obj;
		if (ticketCateName == null) {
			if (other.ticketCateName != null)
				return false;
		} else if (!ticketCateName.equals(other.ticketCateName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ticketCateName == null) ? 0 : ticketCateName.hashCode());
		return result;
	}
}
