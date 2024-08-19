package uk.ac.cf.spring.nhs.UserQuestionnaire.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import uk.ac.cf.spring.nhs.Questionnaire.Model.Questionnaire;
import uk.ac.cf.spring.nhs.Security.AuthenticationInterface;
import uk.ac.cf.spring.nhs.Security.CustomUserDetails;
import uk.ac.cf.spring.nhs.UserQuestion.Service.UserQuestionService;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Model.UserQuestionnaire;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Service.UserQuestionnaireService;

@WebMvcTest(controllers = UserQuestionnaireController.class)
@ActiveProfiles("test")
class UserQuestionnaireControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationInterface authenticationFacade;

    @MockBean
    private UserQuestionnaireService userQuestionnaireService;

    @MockBean
    private UserQuestionService userQuestionService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testGetUserQuestionnaires() throws Exception {
        // Arrange
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(
                userId,
                "testuser",
                "password",
                List.of());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        // Mock the behavior of the authenticationFacade to return the mocked
        // Authentication object
        when(authenticationFacade.getAuthentication()).thenReturn(auth);

        // Mock the service to return some user questionnaires
        List<UserQuestionnaire> userQuestionnaires = List.of(new UserQuestionnaire());
        when(userQuestionnaireService.getUserQuestionnaires(userId)).thenReturn(userQuestionnaires);

        mockMvc.perform(get("/api/userQuestionnaires/user"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    assertThat(jsonResponse).contains("\"questionnaireIsCompleted\":false");
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testGetCompletedUserQuestionnaires() throws Exception {
        // Arrange
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(
                userId,
                "testuser",
                "password",
                List.of());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        // Mock the behavior of the authenticationFacade to return the mocked
        // Authentication object
        when(authenticationFacade.getAuthentication()).thenReturn(auth);

        // Mock the service to return some completed user questionnaires
        UserQuestionnaire completedQuestionnaire = new UserQuestionnaire();
        completedQuestionnaire.setQuestionnaireIsCompleted(true); // Ensure this is set to true
        completedQuestionnaire.setUserID(userId);
        completedQuestionnaire.setQuestionnaire(new Questionnaire()); // Assuming a non-null Questionnaire object is
                                                                      // needed

        List<UserQuestionnaire> userQuestionnaires = List.of(completedQuestionnaire);
        when(userQuestionnaireService.getCompletedUserQuestionnaires(userId)).thenReturn(userQuestionnaires);

        mockMvc.perform(get("/api/userQuestionnaires/user/completed"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    assertThat(jsonResponse).contains("\"questionnaireIsCompleted\":true");
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testGetIncompleteUserQuestionnaires() throws Exception {

        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(
                userId,
                "testuser",
                "password",
                List.of());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        // Mock the behavior of the authenticationFacade to return the mocked
        // Authentication object
        when(authenticationFacade.getAuthentication()).thenReturn(auth);

        // Mock the service to return some incomplete user questionnaires
        List<UserQuestionnaire> userQuestionnaires = List.of(new UserQuestionnaire());
        when(userQuestionnaireService.getIncompleteUserQuestionnaires(userId)).thenReturn(userQuestionnaires);

        mockMvc.perform(get("/api/userQuestionnaires/user/incomplete"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    assertThat(jsonResponse).contains("\"questionnaireIsCompleted\":false");
                });
    }

    @Test
    @WithMockUser(username = "testuser", roles = { "USER" })
    void testGetUserQuestionnaireById() throws Exception {

        Long userId = 1L;
        Long questionnaireId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(
                userId,
                "testuser",
                "password",
                List.of());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        // Mock the behavior of the authenticationFacade to return the mocked
        // Authentication object
        when(authenticationFacade.getAuthentication()).thenReturn(auth);

        // Create and set up the UserQuestionnaire object
        UserQuestionnaire userQuestionnaire = new UserQuestionnaire();
        userQuestionnaire.setUserID(userId);
        userQuestionnaire.setQuestionnaire(new Questionnaire());

        // Mock the service to return the user questionnaire
        when(userQuestionnaireService.getUserQuestionnaire(userId, questionnaireId))
                .thenReturn(Optional.of(userQuestionnaire));

        mockMvc.perform(get("/api/userQuestionnaires/user/questionnaire/{questionnaireId}", questionnaireId))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    assertThat(jsonResponse).contains("\"userID\":1");
                });
    }

}
