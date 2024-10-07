package com.yupi.yojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 判题配置
 */
@Data
public class JudgeConfig {


    private Long timeLimit;


    private Long memoryLimit;

    private Long stackLimit;
}
