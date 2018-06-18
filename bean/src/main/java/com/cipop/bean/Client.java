package com.cipop.bean;

import com.cipop.bean.abs.base.AbstractAuditing;
import com.cipop.bean.enums.ClientType;
import com.cipop.bean.enums.PricingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @ClassName: Client
 * @Description: Bean for Client
 * @Author: Yang Naihua
 * @Create: 2018-06-18 10:27
 **/
@Entity
@Table(name = "clients")
@Data
@ApiModel(value = "Client", description = "客户")
public class Client extends AbstractAuditing {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @ApiModelProperty(value = "ID", readOnly = true)
    private String id;
    @NotNull
    @Column(unique = true)
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @JsonIgnore
    @NotNull
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @NotNull
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    @NotNull
    @ApiModelProperty(value = "电话号码", required = true)
    private String phoneNumber;
    @ApiModelProperty(value = "语言")
    private String language;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "货币类型", required = true)
    private PricingType pricingType = PricingType.EUR;
    @ApiModelProperty(value = "生日")
    private LocalDateTime birthday;
    @ApiModelProperty(value = "接收广告", readOnly = true)
    private Boolean advertisement = true;
    @ApiModelProperty(value = "是否已经激活", readOnly = true)
    private Boolean activity = false;
    @ApiModelProperty(value = "头像", readOnly = true)
    private String image;
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "客户类型", readOnly = true)
    private ClientType clientType = ClientType.TENANT;
}