package com.yupi.yojbackendserviceclient.service;


import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yoj-backend-judge-service",path = "/api/judge/inner")
public interface JudgeFeignClient {
    //返回值应该是什么？

    /**
     * 判题服务
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
