package com.yupi.yojbackendjudgeservice.judge.codesandbox;


import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeCodeRequest;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeResponse;

public interface CodeSandbox {
    /**
     * 执行代码
     * @param excodeCodeRequest
     * @return
     */
    ExecutecodeResponse executeCode(ExecutecodeCodeRequest excodeCodeRequest);
}
