package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.Cafe;
import com.Minjin.TagCafe.entity.CafeImage;
import com.Minjin.TagCafe.repository.CafeImageRepository;
import com.Minjin.TagCafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Minjin.TagCafe.dto.CafeDto;
import com.Minjin.TagCafe.entity.Review;
import com.Minjin.TagCafe.repository.ReviewRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final CafeImageRepository cafeImageRepository;

    // ID로 카페 조회
    public Cafe getCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카페를 찾을 수 없습니다."));
    }

    // 하나의 키워드로 카페 조회 (카페 이름, 주소 구분 X)
    public List<Cafe> searchCafeByKeyword(String keyword) {
        List<Cafe> nameMatches = cafeRepository.findByCafeNameContainingIgnoreCase(keyword);
        List<Cafe> addressMatches = cafeRepository.findByAddressContainingIgnoreCase(keyword);

        // 두 리스트 합치기
        return Stream.concat(nameMatches.stream(), addressMatches.stream())
                .distinct()
                .collect(Collectors.toList());
    }
    private boolean isOpenNow(String openingHours) {
        if (openingHours == null || openingHours.isEmpty() || openingHours.equals("정보 없음")) {
            return false;
        }

        String[] lines = openingHours.split(", ");
        LocalDateTime now = LocalDateTime.now();
        String todayKor = switch (now.getDayOfWeek()) {
            case MONDAY -> "월요일";
            case TUESDAY -> "화요일";
            case WEDNESDAY -> "수요일";
            case THURSDAY -> "목요일";
            case FRIDAY -> "금요일";
            case SATURDAY -> "토요일";
            case SUNDAY -> "일요일";
        };

        for (String line : lines) {
            if (!line.startsWith(todayKor)) continue;

            if (line.contains("휴무일")) {
                return false;
            }

            if (line.contains("24시간 영업")) {
                return true;
            }

            String[] parts = line.split(": ");
            if (parts.length < 2) return false;

            String[] times = parts[1].split(" ~ ");
            if (times.length < 2) return false;

            LocalTime start = parseKoreanTime(times[0]);
            LocalTime end = parseKoreanTime(times[1]);
            LocalTime nowTime = now.toLocalTime();

            return nowTime.isAfter(start) && nowTime.isBefore(end);
        }
        return false;
    }

    private LocalTime parseKoreanTime(String timeStr) {
        boolean isPM = timeStr.contains("오후");
        timeStr = timeStr.replace("오전", "").replace("오후", "").trim();
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());

        if (isPM && hour != 12) hour += 12;
        if (!isPM && hour == 12) hour = 0;

        return LocalTime.of(hour, minute);
    }


    // 특정 태그와 값 리스트를 가진 카페 조회 (다중 필터링 지원)
    public List<Cafe> getCafesByMultipleTagsAndValues(List<String> tagNames, List<String> values) {
        List<Cafe> filteredCafes = cafeRepository.findAll(); // 기본적으로 모든 카페 가져오기

        // 1. 평점 필터 처리
        int ratingIndex = tagNames.indexOf("평점");
        if (ratingIndex != -1) {
            String ratingValue = values.get(ratingIndex);
            double minRating = switch (ratingValue) {
                case "5.0" -> 5.0;
                case "4.0 이상" -> 4.0;
                case "3.0 이상" -> 3.0;
                default -> 0.0;
            };

            tagNames.remove(ratingIndex);
            values.remove(ratingIndex);

            // 평균 평점이 기준 이상인 카페 필터링
            filteredCafes = filteredCafes.stream()
                    .filter(cafe -> cafe.getAverageRating() >= minRating)
                    .collect(Collectors.toList());
        }

        // 2. 운영시간 필터 분리
        int timeIndex = tagNames.indexOf("운영시간");
        String timeFilterValue = null;
        if (timeIndex != -1) {
            timeFilterValue = values.get(timeIndex);
            tagNames.remove(timeIndex);
            values.remove(timeIndex);
        }

        // 3. 일반 태그 필터 처리
        for (int i = 0; i < tagNames.size(); i++) {
            String tagName = tagNames.get(i);
            String value = values.get(i);
            final String filterValue = value;

            filteredCafes = filteredCafes.stream()
                    .filter(cafe -> cafeMatchesTag(cafe, tagName, filterValue))
                    .collect(Collectors.toList());
        }

        // 4. 운영시간 필터 처리
        if (timeFilterValue != null) {
            final String timeValue = timeFilterValue;
            filteredCafes = filteredCafes.stream()
                    .filter(cafe -> {
                        if ("영업중".equals(timeValue)) {
                            return isOpenNow(cafe.getOpeningHours());
                        } else if ("24시간".equals(timeValue)) {
                            return cafe.getOpeningHours() != null &&
                                    cafe.getOpeningHours().contains("24시간 영업");
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        return filteredCafes;
    }

    private boolean cafeMatchesTag(Cafe cafe, String tagName, String value) {
        return switch (tagName) {
            case "와이파이" -> cafe.getWifi() != null && cafe.getWifi().name().equals(value);
            case "콘센트" -> cafe.getOutlets() != null && cafe.getOutlets().name().equals(value);
            case "책상" -> cafe.getDesk() != null && cafe.getDesk().name().equals(value);
            case "화장실" -> cafe.getRestroom() != null && cafe.getRestroom().name().equals(value);
            case "주차" -> cafe.getParking() != null && cafe.getParking().name().equals(value);
            default -> false;
        };
    }

    /**
     * 특정 리뷰가 사용자가 입력한 태그와 일치하는지 확인하는 메서드
     */
    private boolean reviewMatchesTag(Review review, String tagName, String value) {
        return switch (tagName) {
            case "와이파이" -> review.getWifi() != null && review.getWifi().name().equals(value);
            case "콘센트" -> review.getOutlets() != null && review.getOutlets().name().equals(value);
            case "책상" -> review.getDesk() != null && review.getDesk().name().equals(value);
            case "화장실" -> review.getRestroom() != null && review.getRestroom().name().equals(value);
            case "주차" -> review.getParking() != null && review.getParking().name().equals(value);
            default -> false;
        };
    }

    // 지도 영역 내 카페 조회
    public List<Cafe> getCafesInArea(double minLat, double maxLat, double minLng, double maxLng) {
        return cafeRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);
    }

    // admin - 카페 추가
    public Cafe addCafe(CafeDto cafeDto) {
        if (cafeRepository.findByKakaoPlaceId(cafeDto.getKakaoPlaceId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 카페입니다.");
        }

        Cafe cafe = Cafe.builder()
                .kakaoPlaceId(cafeDto.getKakaoPlaceId())
                .cafeName(cafeDto.getCafeName())
                .latitude(cafeDto.getLatitude())
                .longitude(cafeDto.getLongitude())
                .address(cafeDto.getAddress())
                .phoneNumber(cafeDto.getPhoneNumber())
                .websiteUrl(cafeDto.getWebsiteUrl())
                .openingHours(cafeDto.getOpeningHours())
                .build();

        cafe.setImages(new ArrayList<>());
        cafeRepository.save(cafe);

        List<String> imageUrls = cafeDto.getPhotoUrls();
        if (imageUrls != null) {
            imageUrls.stream().limit(5).forEach(url -> {
                System.out.println("📸 이미지 URL 확인: " + url);
                byte[] imageData = fetchImageAsBytes(url);
                System.out.println("📏 이미지 크기: " + imageData.length + " 바이트");
                CafeImage image = CafeImage.builder()
                        .imageData(imageData)
                        .cafe(cafe)
                        .build();
                cafe.getImages().add(image);
            });
        }

        return cafeRepository.save(cafe);
    }

    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    public byte[] fetchImageAsBytes(String imageUrl) {
        if (imageUrl == null || imageUrl.contains("undefined")) {
            throw new RuntimeException("잘못된 이미지 URL: " + imageUrl);
        }

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // ✅ 요게 핵심!

            try (InputStream in = conn.getInputStream();
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int n;
                while ((n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                }
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 다운로드 실패: " + imageUrl, e);
        }
    }



}
