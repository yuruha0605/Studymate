package edu.yonsei.Studymate.Studygroup.entity;

import edu.yonsei.Studymate.login.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudygroupEntity group;

    @ManyToOne
    private User user;

    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private MemberRole role;  // LEADER, MEMBER

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}


