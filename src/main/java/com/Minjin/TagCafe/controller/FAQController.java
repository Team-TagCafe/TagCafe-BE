package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.FAQResponse;
import com.Minjin.TagCafe.dto.FeedbackRequest;
import com.Minjin.TagCafe.dto.FeedbackResponse;
import com.Minjin.TagCafe.entity.Feedback;
import com.Minjin.TagCafe.repository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faq")
@CrossOrigin(origins = "http://localhost:3000")
public class FAQController {

    private final FeedbackRepository feedbackRepository;

    public FAQController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    //자주 묻는 질문 조회
    @GetMapping("/qa")
    public @ResponseBody ResponseEntity<List<FAQResponse>> getFAQs() {
        List<FAQResponse> faqs = Arrays.asList(
                new FAQResponse("서비스 관련", "필터를 어떻게 사용하나요?", "필터는 검색창 옆의 버튼을 클릭하여 사용할 수 있습니다."),
                new FAQResponse("서비스 관련", "회원 가입 없이 사용할 수 있나요?", "회원 가입 없이도 일부 서비스를 이용할 수 있습니다."),
                new FAQResponse("기능 관련", "내가 좋아하는 카페를 추가할 수 있나요?", "내 프로필에서 좋아하는 카페를 추가할 수 있습니다.")
        );
        return ResponseEntity.ok().body(faqs);
    }

    //사용자 의견 및 오류 제보 제출
    @PostMapping("/feedback")
    public ResponseEntity<Map<String, String>> submitFeedback(@RequestBody FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setContent(request.getContent());
        feedbackRepository.save(feedback);

        Map<String, String> response = new HashMap<>();
        response.put("message", "피드백이 정상적으로 저장되었습니다!");

        return ResponseEntity.ok(response);
    }

    // ✅ 관리자용 - 사용자 의견 및 오류 목록 조회 API
    @GetMapping("/feedback")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackList() {
        List<FeedbackResponse> feedbacks = feedbackRepository.findAll()
                .stream()
                .map(feedback -> new FeedbackResponse(feedback.getId(), feedback.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(feedbacks);
    }
}

