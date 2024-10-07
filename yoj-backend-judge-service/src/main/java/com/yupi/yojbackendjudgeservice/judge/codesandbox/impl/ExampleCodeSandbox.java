package com.yupi.yojbackendjudgeservice.judge.codesandbox.impl;



import com.yupi.yojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeCodeRequest;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeResponse;
import com.yupi.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.yupi.yojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.yupi.yojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecutecodeResponse executeCode(ExecutecodeCodeRequest excodeCodeRequest) {

        List<String> inputList = excodeCodeRequest.getInputList();
        String code = excodeCodeRequest.getCode();
        String language = excodeCodeRequest.getLanguage();
        ExecutecodeResponse executecodeResponse = new ExecutecodeResponse();
        executecodeResponse.setOutputList(inputList);
        executecodeResponse.setMessage("测试执行成功");
        executecodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executecodeResponse.setJudgeInfo(judgeInfo);
        return executecodeResponse;
    }
}
