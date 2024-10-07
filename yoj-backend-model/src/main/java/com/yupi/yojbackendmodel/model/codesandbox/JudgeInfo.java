package com.yupi.yojbackendmodel.model.codesandbox;

import lombok.Data;

@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 程序执行消耗时间
     */
    private Long time;

    /**
     * 程序执行消耗内存
     */
    private Long memory;

    /**
     * 题目通过测试用例数
     */
    private Integer  passNum;

    /**
     * 总测试用例数
     */

    private Integer  totalNum;

}
