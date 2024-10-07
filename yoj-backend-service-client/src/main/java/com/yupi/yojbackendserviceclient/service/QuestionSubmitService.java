package com.yupi.yojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yojbackendmodel.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.yojbackendmodel.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.yupi.yojbackendmodel.model.entity.QuestionSubmit;
import com.yupi.yojbackendmodel.model.entity.User;
import com.yupi.yojbackendmodel.model.vo.QuestionSubmitVO;

/**
* @author 13425
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-05-22 18:56:03
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);


    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);



    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);


}
