package com.yupi.yojbackendserviceclient.service;


import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {
    //返回值应该是什么？

    /**
     * 判题服务
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
