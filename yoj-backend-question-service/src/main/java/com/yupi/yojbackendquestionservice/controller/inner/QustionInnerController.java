package com.yupi.yojbackendquestionservice.controller.inner;

import com.yupi.yojbackendmodel.model.entity.Question;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import com.yupi.yojbackendmodel.model.entity.User;
import com.yupi.yojbackendquestionservice.service.QuestionService;
import com.yupi.yojbackendquestionservice.service.QuestionSubmitService;
import com.yupi.yojbackendserviceclient.service.QuestionFeignClient;
import com.yupi.yojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class QustionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * questionService.getById(questionId)
     * questionSubmitService.getById(questionSubmitId)
     * questionSubmitService.updateById(questionSubmitUpdate)1
     */
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId){

       return questionService.getById(questionId);

    }


    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }


    @PostMapping("/question_submit/update")
    public Boolean updateQuestionSubmitById(@RequestBody  QuestionSubmit questionSubmit){
         return questionSubmitService.updateById(questionSubmit);

    }
}

