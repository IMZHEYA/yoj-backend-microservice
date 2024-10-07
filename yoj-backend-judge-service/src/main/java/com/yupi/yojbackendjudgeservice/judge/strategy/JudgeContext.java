package com.yupi.yojbackendjudgeservice.judge.strategy;


import com.yupi.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.yupi.yojbackendmodel.model.dto.question.JudgeCase;
import com.yupi.yojbackendmodel.model.entity.Question;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 在策略中传递的参数
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}