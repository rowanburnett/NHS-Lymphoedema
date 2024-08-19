package uk.ac.cf.spring.nhs.UserQuestionnaire.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import uk.ac.cf.spring.nhs.Questionnaire.Model.Questionnaire;
import uk.ac.cf.spring.nhs.Security.AuthenticationInterface;
import uk.ac.cf.spring.nhs.Security.CustomUserDetails;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Model.UserQuestionnaire;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Service.UserQuestionnaireService;

class UserQuestionnaireControllerUnitTests {

    @Mock
    private UserQuestionnaireService userQuestionnaireService;

    @Mock
    private AuthenticationInterface authenticationFacade;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails customUserDetails;

    @InjectMocks
    private UserQuestionnaireController userQuestionnaireController;

    private UserQuestionnaire userQuestionnaire;
    private Questionnaire questionnaire;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        questionnaire = new Questionnaire();
        questionnaire.setId(1L);
        questionnaire.setTitle("Test Questionnaire");

        userQuestionnaire = new UserQuestionnaire();
        userQuestionnaire.setUserID(123L);
        userQuestionnaire.setQuestionnaire(questionnaire);
        userQuestionnaire.setQuestionnaireIsCompleted(false);
        userQuestionnaire.setQuestionnaireInProgress(false);

        // Set up authentication mocks
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getUserId()).thenReturn(123L);
    }

    @Test
    void testGetUserQuestionnaires() {
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<>();
        userQuestionnaires.add(userQuestionnaire);
        when(userQuestionnaireService.getUserQuestionnaires(123L)).thenReturn(userQuestionnaires);

        ResponseEntity<List<UserQuestionnaire>> response = userQuestionnaireController.getUserQuestionnaires();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userQuestionnaireService, times(1)).getUserQuestionnaires(123L);
    }

    @SuppressWarnings("null")
    @Test
    void testGetCompletedUserQuestionnaires() {
        userQuestionnaire.setQuestionnaireIsCompleted(true);
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<>();
        userQuestionnaires.add(userQuestionnaire);
        when(userQuestionnaireService.getCompletedUserQuestionnaires(123L)).thenReturn(userQuestionnaires);

        ResponseEntity<List<UserQuestionnaire>> response = userQuestionnaireController.getCompletedUserQuestionnaires();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userQuestionnaireService, times(1)).getCompletedUserQuestionnaires(123L);
    }

    @SuppressWarnings("null")
    @Test
    void testGetIncompleteUserQuestionnaires() {
        userQuestionnaire.setQuestionnaireIsCompleted(false);
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<>();
        userQuestionnaires.add(userQuestionnaire);
        when(userQuestionnaireService.getIncompleteUserQuestionnaires(123L)).thenReturn(userQuestionnaires);

        ResponseEntity<List<UserQuestionnaire>> response = userQuestionnaireController
                .getIncompleteUserQuestionnaires();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userQuestionnaireService, times(1)).getIncompleteUserQuestionnaires(123L);
    }

    @Test
    void testGetUserQuestionnaire() {
        Long questionnaireId = 1L;
        when(userQuestionnaireService.getUserQuestionnaire(123L, questionnaireId))
                .thenReturn(Optional.of(userQuestionnaire));

        ResponseEntity<UserQuestionnaire> response = userQuestionnaireController.getUserQuestionnaire(questionnaireId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userQuestionnaire, response.getBody());
        verify(userQuestionnaireService, times(1)).getUserQuestionnaire(123L, questionnaireId);
    }

    @Test
    void testGetUserQuestionnaireNotFound() {
        Long questionnaireId = 1L;
        when(userQuestionnaireService.getUserQuestionnaire(123L, questionnaireId)).thenReturn(Optional.empty());

        ResponseEntity<UserQuestionnaire> response = userQuestionnaireController.getUserQuestionnaire(questionnaireId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userQuestionnaireService, times(1)).getUserQuestionnaire(123L, questionnaireId);
    }

    @Test
    void testCreateUserQuestionnaire() {
        when(userQuestionnaireService.saveUserQuestionnaire(userQuestionnaire)).thenReturn(userQuestionnaire);

        ResponseEntity<UserQuestionnaire> response = userQuestionnaireController
                .createUserQuestionnaire(userQuestionnaire);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userQuestionnaire, response.getBody());
        verify(userQuestionnaireService, times(1)).saveUserQuestionnaire(userQuestionnaire);
    }

    @Test
    void testUpdateUserQuestionnaire() {
        Long questionnaireId = 1L;
        when(userQuestionnaireService.getUserQuestionnaire(123L, questionnaireId))
                .thenReturn(Optional.of(userQuestionnaire));
        when(userQuestionnaireService.saveUserQuestionnaire(userQuestionnaire)).thenReturn(userQuestionnaire);

        ResponseEntity<UserQuestionnaire> response = userQuestionnaireController
                .updateUserQuestionnaire(questionnaireId, userQuestionnaire);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userQuestionnaire, response.getBody());
        verify(userQuestionnaireService, times(1)).saveUserQuestionnaire(userQuestionnaire);
    }

    @Test
    void testUpdateUserQuestionnaireNotFound() {
        Long questionnaireId = 1L;
        when(userQuestionnaireService.getUserQuestionnaire(123L, questionnaireId)).thenReturn(Optional.empty());

        ResponseEntity<UserQuestionnaire> response = userQuestionnaireController
                .updateUserQuestionnaire(questionnaireId, userQuestionnaire);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userQuestionnaireService, times(1)).getUserQuestionnaire(123L, questionnaireId);
    }

    @Test
    void testDeleteUserQuestionnaire() {
        Long questionnaireId = 1L;
        when(userQuestionnaireService.getUserQuestionnaire(123L, questionnaireId))
                .thenReturn(Optional.of(userQuestionnaire));
        doNothing().when(userQuestionnaireService).deleteUserQuestionnaire(questionnaireId);

        ResponseEntity<Void> response = userQuestionnaireController.deleteUserQuestionnaire(questionnaireId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userQuestionnaireService, times(1)).deleteUserQuestionnaire(questionnaireId);
    }

    @Test
    void testDeleteUserQuestionnaireNotFound() {
        Long questionnaireId = 1L;
        when(userQuestionnaireService.getUserQuestionnaire(123L, questionnaireId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userQuestionnaireController.deleteUserQuestionnaire(questionnaireId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userQuestionnaireService, times(1)).getUserQuestionnaire(123L, questionnaireId);
    }

    @SuppressWarnings("null")
    @Test
    void testGetIncompleteUserQuestionnairesForPatient() {
        Long patientId = 456L;
        List<UserQuestionnaire> userQuestionnaires = new ArrayList<>();
        userQuestionnaires.add(userQuestionnaire);
        when(userQuestionnaireService.getIncompleteUserQuestionnaires(patientId)).thenReturn(userQuestionnaires);

        ResponseEntity<List<UserQuestionnaire>> response = userQuestionnaireController
                .getIncompleteUserQuestionnairesForPatient(patientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userQuestionnaireService, times(1)).getIncompleteUserQuestionnaires(patientId);
    }
}
