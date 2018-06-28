package com.cipop.bean;

import com.cipop.bean.abs.base.AbstractAuditing;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: Command
 * @Description: Bean for Command
 * @Author: Yang Naihua
 * @Create: 2018-06-18 10:28
 **/
public class Command extends AbstractAuditing {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @ApiModelProperty(value = "ID", readOnly = true)
    private String id;
    @NotNull
    @ApiModelProperty(value = "客户ID", required = true)
    private String clientId;
    @ApiModelProperty(value = "开始入住时间")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "结束入住时间")
    private LocalDateTime endTime;
    @ApiModelProperty(value = "商品ID", readOnly = true)
    private String productId;
    @ApiModelProperty(value = "地址", readOnly = true)
    private String address;
    @ApiModelProperty(value = "城市", readOnly = true)
    private String city;
    @ApiModelProperty(value = "国家", readOnly = true)
    private String country;
    @ApiModelProperty(value = "邮编", readOnly = true)
    private String zipCode;
    @ApiModelProperty(value = "费用", readOnly = true)
    private BigDecimal amount = BigDecimal.ZERO;
}