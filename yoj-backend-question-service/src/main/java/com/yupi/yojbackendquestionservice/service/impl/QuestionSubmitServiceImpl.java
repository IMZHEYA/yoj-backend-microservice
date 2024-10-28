package com.yupi.yojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yojbackendcommon.common.ErrorCode;
import com.yupi.yojbackendcommon.constant.CommonConstant;
import com.yupi.yojbackendcommon.exception.BusinessException;
import com.yupi.yojbackendcommon.utils.SqlUtils;
import com.yupi.yojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.yojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.yupi.yojbackendmodel.model.entity.Question;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import com.yupi.yojbackendmodel.model.entity.User;
import com.yupi.yojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.yojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.yupi.yojbackendmodel.model.vo.QuestionSubmitVO;
import com.yupi.yojbackendmodel.model.vo.QuestionVO;
import com.yupi.yojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.yupi.yojbackendquestionservice.service.QuestionSubmitService;
import com.yupi.yojbackendserviceclient.service.JudgeFeignClient;
import com.yupi.yojbackendserviceclient.service.QuestionFeignClient;
import com.yupi.yojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author 13425
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-05-22 18:56:03
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {


    @Resource
    private QuestionFeignClient questionFeignClient;


    @Resource
    private QuestionSubmitService questionSubmitService;


    @Resource
    private UserFeignClient userFeignClient;


    @Resource
    private JudgeFeignClient judgeFeignClient;

    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }

        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        boolean save = questionSubmitService.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交失败");
        }
        //todo 执行判题服务
        CompletableFuture.runAsync(() -> {
            judgeFeignClient.doJudge(questionSubmit.getId());
        });
        return questionSubmit.getId();
    }


    /**
     * 获取mybatis查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取单个信息的包装类
     *
     * @param questionSubmit
     * @param loginUser
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        QuestionVO questionVO = QuestionVO.objToVo(questionFeignClient.getQuestionById(questionSubmit.getQuestionId()));
        questionSubmitVO.setQuestionVO(questionVO);
        long userId = loginUser.getId();
        //不是提交者且不是管理员，不允许看别人的代码
        if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    /**
     * 获取多条信息的包装类
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




