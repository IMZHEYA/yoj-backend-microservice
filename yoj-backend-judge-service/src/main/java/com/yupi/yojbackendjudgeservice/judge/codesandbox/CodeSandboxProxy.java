package com.yupi.yojbackendjudgeservice.judge.codesandbox;


import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeCodeRequest;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox{


    private final  CodeSandbox codeSandbox;


    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecutecodeResponse executeCode(ExecutecodeCodeRequest excodeCodeRequest) {
        log.info("代码沙箱请求信息" + excodeCodeRequest.toString());
        ExecutecodeResponse executecodeResponse = codeSandbox.executeCode(excodeCodeRequest);
        log.info("代码沙箱响应信息" + executecodeResponse.toString());
        return executecodeResponse;
    }
}
