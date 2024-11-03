package com.yupi.yojbackendjudgeservice.judge.controller.inner;
import com.yupi.yojbackendjudgeservice.judge.JudgeService;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import com.yupi.yojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {


    @Resource
    private JudgeService judgeService;
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }


}

