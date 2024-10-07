package com.yupi.yojbackendjudgeservice.judge;


import com.yupi.yojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.yupi.yojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.yupi.yojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yupi.yojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.yupi.yojbackendmodel.model.codesandbox.JudgeInfo;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Component;

/**
 * 判题管理
 */
@Component
public class JudgeManager {


    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if("java".equals(language)){
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
