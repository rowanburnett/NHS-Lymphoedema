package uk.ac.cf.spring.nhs.UserQuestion.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import uk.ac.cf.spring.nhs.Question.Model.Question;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Model.UserQuestionnaire;

@Entity
@Table(name = "UserQuestions")
public class UserQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_question_id")
    private Long userQuestionID;

    @ManyToOne
    @JoinColumn(name = "user_questionnaire_id", nullable = false)
    private UserQuestionnaire userQuestionnaire;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "user_response_text", columnDefinition = "TEXT")
    private String userResponseText;

    @Column(name = "user_response_score")
    private Integer userResponseScore;

    @Column(name = "response_date_time", nullable = false)
    private LocalDateTime responseDateTime;

    @PrePersist
    protected void onCreate() {
        this.responseDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.responseDateTime = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getUserQuestionID() {
        return userQuestionID;
    }

    public void setUserQuestionID(Long userQuestionID) {
        this.userQuestionID = userQuestionID;
    }

    public UserQuestionnaire getUserQuestionnaire() {
        return userQuestionnaire;
    }

    public void setUserQuestionnaire(UserQuestionnaire userQuestionnaire) {
        if (userQuestionnaire == null) {
            throw new NullPointerException("UserQuestionnaire cannot be null");
        }
        this.userQuestionnaire = userQuestionnaire;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        if (question == null) {
            throw new NullPointerException("Question cannot be null");
        }
        this.question = question;
    }

    public String getUserResponseText() {
        return userResponseText;
    }

    public void setUserResponseText(String userResponseText) {
        this.userResponseText = userResponseText;
    }

    public Integer getUserResponseScore() {
        return userResponseScore;
    }

    public void setUserResponseScore(Integer userResponseScore) {
        this.userResponseScore = userResponseScore;
    }

    public LocalDateTime getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(LocalDateTime responseDateTime) {
        this.responseDateTime = responseDateTime;
    }
}
