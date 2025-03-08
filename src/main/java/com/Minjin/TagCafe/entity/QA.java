package com.Minjin.TagCafe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qa")  // ✅ 테이블 이름을 "qa"로 변경
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String question;
    private String answer;
}
