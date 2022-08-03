package com.example.simple.bot.bo;

import lombok.Data;

/**
 * @author yj632
 */
@Data
public class RegexConfigVo {
    private String regex;

    private String target;

    private Boolean white;

    private Boolean admin;

    private Boolean whiteInner;
}
