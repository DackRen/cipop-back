//package com.cipop.bean.abs;
//
//import com.cipop.bean.abs.base.AbstractAuditing;
//import io.swagger.annotations.ApiModelProperty;
//import org.springframework.data.annotation.Id;
//
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
///**
// * @ClassName: AbstractCommand
// * @Description: AbstractCommand
// * @Author: Yang Naihua
// * @Create: 2018-06-18 10:42
// **/
//public class AbstractCommand extends AbstractAuditing {
//    @Id
//    @ApiModelProperty(value = "订单ID", readOnly = true)
//    private String id;
//    @NotNull
//    @ApiModelProperty(value = "客户ID", required = true)
//    private String client;
//    private Transporter transporter;
//    @ApiModelProperty(value = "国家代码", readOnly = true)
//    private String country;
//
//    private String supplier;
//    private String supplierIndex;
//    private String supplierComment;
//    private String comment;
//
//    @NotNull
//    @ApiModelProperty(value = "重量", required = true)
//    private BigDecimal weight = BigDecimal.ZERO;
//
//    @ApiModelProperty(value = "申报重量", readOnly = true)
//    private BigDecimal declaredWeight = BigDecimal.ZERO;
//
//
//    @ApiModelProperty(value = "航班到达时间", hidden = true)
//    private LocalDateTime eta;
//    @ApiModelProperty(value = "确定时间", readOnly = true)
//    private LocalDateTime validatedAt;
//    @ApiModelProperty(value = "清关时间", readOnly = true)
//    private LocalDateTime clearedAt;
//    @ApiModelProperty(value = "待入库时间", readOnly = true)
//    private LocalDateTime preStockInAt;
//    @ApiModelProperty(value = "入库时间", readOnly = true)
//    private LocalDateTime stockInAt;
//    @ApiModelProperty(value = "出库时间", readOnly = true)
//    private LocalDateTime stockOutAt;
//    @Enumerated(EnumType.STRING)
//    @ApiModelProperty(value = "订单状态", readOnly = true)
//    private Status status = Status.CREATED;
//
//    @ApiModelProperty(value = "干线费用", readOnly = true)
//    private BigDecimal deliveryFee = BigDecimal.ZERO;
//
//    @ApiModelProperty(value = "产品类型")
//    private String product;
//
//    public boolean isClearance() {
//        return !Boolean.TRUE.equals(noBatch);
//    }
//}