package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.FAQResponse;
import com.Minjin.TagCafe.dto.FeedbackRequest;
import com.Minjin.TagCafe.dto.FeedbackResponse;
import com.Minjin.TagCafe.entity.Feedback;
import com.Minjin.TagCafe.entity.QA;
import com.Minjin.TagCafe.repository.FeedbackRepository;
import com.Minjin.TagCafe.repository.QARepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "FAQ", description = "자주 묻는 질문 및 사용자 피드백 관련 API")
@RestController
@RequestMapping("/faq")
@CrossOrigin(origins = "http://tagcafe.site")
public class FAQController {

    private final FeedbackRepository feedbackRepository;
    private final QARepository qaRepository;
    public FAQController(FeedbackRepository feedbackRepository, QARepository qaRepository) {
        this.feedbackRepository = feedbackRepository;
        this.qaRepository = qaRepository;
    }


    //자주 묻는 질문 조회
    @Operation(summary = "자주 묻는 질문 조회", description = "모든 QA 데이터를 조회합니다.")
    @GetMapping("/qa")
    public ResponseEntity<List<QA>> getQAs() {
        List<QA> qaList = qaRepository.findAll();
        return ResponseEntity.ok(qaList);
    }

    //사용자 의견 및 오류 제보 제출
    @Operation(summary = "사용자 피드백 제출", description = "사용자의 의견이나 오류 제보를 저장합니다.")
    @PostMapping("/feedback")
    public ResponseEntity<Map<String, String>> submitFeedback(@RequestBody FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setContent(request.getContent());
        feedback.setEmail(request.getEmail());
        feedbackRepository.save(feedback);

        Map<String, String> response = new HashMap<>();
        response.put("message", "피드백이 정상적으로 저장되었습니다!");

        return ResponseEntity.ok(response);
    }

    // ✅ 관리자용 - 사용자 의견 및 오류 목록 조회 API
    @Operation(summary = "admin - 사용자 피드백 전체 조회", description = "관리자가 보는 사용자 피드백 목록입니다.")
    @GetMapping("/feedback")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackList() {
        List<FeedbackResponse> feedbacks = feedbackRepository.findAll()
                .stream()
                .map(feedback -> new FeedbackResponse(feedback.getId(), feedback.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(feedbacks);
    }
}

