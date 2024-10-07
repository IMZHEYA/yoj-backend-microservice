package com.yupi.yojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.yojbackendcommon.common.ErrorCode;
import com.yupi.yojbackendcommon.exception.BusinessException;
import com.yupi.yojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeCodeRequest;
import com.yupi.yojbackendmodel.model.codesandbox.ExecutecodeResponse;


/**
 * 远程代码沙箱：自己开发的沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {
    //鉴权请求头
    public static final String AUTH_REQUEST_HEADER = "auth";
    //密钥
    public static final String AUTH_REQUEST_SECRET = "secretKey";
    @Override
    public ExecutecodeResponse executeCode(ExecutecodeCodeRequest excodeCodeRequest) {
//        String url = "http://localhost:8090/executeCode";
        //http://localhost:8090/executeCode
        String url = "http://106.54.12.71:8090/executeCode";
        String jsonStr = JSONUtil.toJsonStr(excodeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)   //带上请求头，
                .body(jsonStr)
                .execute()
                .body();
        if(StrUtil.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"调用远程代码沙箱接口失败：" + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecutecodeResponse.class);
    }
}
