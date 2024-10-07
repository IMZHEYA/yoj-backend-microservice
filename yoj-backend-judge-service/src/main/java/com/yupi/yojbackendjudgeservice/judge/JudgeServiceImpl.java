package com.yupi.yojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.yupi.yojbackendcommon.common.ErrorCode;
import com.yupi.yojbackendcommon.exception.BusinessException;
import com.yupi.yojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yupi.yojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.yupi.yojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.yupi.yojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeCodeRequest;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeResponse;
import com.yupi.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.yupi.yojbackendmodel.model.dto.question.JudgeCase;
import com.yupi.yojbackendmodel.model.entity.Question;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import com.yupi.yojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.yupi.yojbackendserviceclient.service.QuestionService;
import com.yupi.yojbackendserviceclient.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codeSandbox.type}")
    private String type;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
//        1）传入题目的提交id,获取到对应的题目，提交信息（包含代码，编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if(questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if(question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
//        2）如果题目提交状态不为等待中，就不用重复执行了，只执行等待中的题目
        Integer status = questionSubmit.getStatus();
        if(!Objects.equals(status, QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判題中");
        }
//        3）更改题目提交状态为“判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean save = questionSubmitService.updateById(questionSubmitUpdate);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新失败");
        }
//        4）调用沙箱，获取到执行结果
        CodeSandbox codesandbox = CodeSandboxFactory.newInstance(type);
        codesandbox = new CodeSandboxProxy(codesandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        //输入用例转列表
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        //把判题用例中的输入用例过滤出来，喂给需要的inputList,得到了输入列表
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecutecodeCodeRequest executecodeCodeRequest = ExecutecodeCodeRequest.builder().code(code).language(language).inputList(inputList).build();
//        5）根据沙箱的执行结果，设置题目的判题状态和信息
        ExecutecodeResponse executecodeResponse = codesandbox.executeCode(executecodeCodeRequest);
        List<String> outputList = executecodeResponse.getOutputList();
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executecodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //6)修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        save = questionSubmitService.updateById(questionSubmitUpdate);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        //修改数据库题目全部提交数目
        Integer submitNum = question.getSubmitNum();
        question.setSubmitNum(submitNum + 1);
        if(judgeInfo.getMessage().equals("通过")){
            //更新数据库通过数目
            Integer acceptedNum = question.getAcceptedNum();
            question.setAcceptedNum(acceptedNum + 1);
        }
        boolean b = questionService.updateById(question);
        if(!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目信息更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
