package com.yupi.yojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.yupi.yojbackendmodel.model.entity.Question;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import com.yupi.yojbackendmodel.model.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.BitSet;

/**
* @author 13425
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-05-22 18:55:53
*/
@FeignClient(name = "yoj-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient {

    /**
     * questionService.getById(questionId)
     * questionSubmitService.getById(questionSubmitId)
     * questionSubmitService.updateById(questionSubmitUpdate)1
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);


    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    Boolean updateQuestionSubmitById(@RequestBody  QuestionSubmit questionSubmit);
}
