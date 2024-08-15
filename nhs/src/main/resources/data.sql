INSERT INTO UserCredentials (UserName, UserPassword, UserRole)
VALUES (
        'admin',
        '$2y$10$J68pzi1vv7Sjez4Yt5siU.v4FrsXxD58APhQHiXNEldGNRa1um5uK',
        'ROLE_ADMIN'
    ),
    (
        'testUser',
        '$2a$10$hznjz2afk0Dlu3ye10eoPuLWDvnFTuI3ZPegsFs1KJ1mbbfH1MVzq',
        'ROLE_PATIENT'
    ),
    (
        'testProvider',
        '$2a$10$cibnUtr2BBkAQ2HibZDFWOarJcCtfRg1uj0HcfEd8z2WQyN4EePlC',
        'ROLE_PROVIDER'
    );
INSERT INTO Questionnaires (
        QuestionnaireType,
        QuestionnaireName,
        QuestionnaireDesc
    )
VALUES (
        'QOL-ARM',
        'Lymphoedema Quality of Life - ARM',
        'This questionnaire measures the quality of life score for patients with chronic oedema/lymphoedema of one or both arms.'
    );
INSERT INTO Question (QuestionText, QuestionCategory, QuestionResponseType, QuestionnaireID)
VALUES 
-- Function (Daily Activities)
('How much does your swollen arm affect your daily activities in your occupation?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities in doing your housework?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities such as combing your hair?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities of getting dressed?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities of writing?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities of eating?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities of washing?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your daily activities in cleaning teeth?', 'Function', 'Scale', 1),
('How much does your swollen arm affect your leisure activities/social life?', 'Function', 'Scale', 1),
('Please give examples of how your swollen arm affects your leisure activities/social life.', 'Function', 'Text', 1),
('How much do you have to depend on other people?', 'Function', 'Scale', 1),

-- Appearance
('How much do you feel the swelling affects your appearance?', 'Appearance', 'Scale', 1),
('How much difficulty do you have finding clothes to fit?', 'Appearance', 'Scale', 1),
('How much difficulty do you have finding clothes you would like to wear?', 'Appearance', 'Scale', 1),
('Does the swelling affect how you feel about yourself?', 'Appearance', 'Scale', 1),
('Does it affect your relationships with other people?', 'Appearance', 'Scale', 1),

-- Symptoms
('Does your lymphoedema cause you pain?', 'Symptoms', 'Scale', 1),
('Do you have any numbness in your swollen arm?', 'Symptoms', 'Scale', 1),
('Do you have any feelings of "pins & needles" or tingling in your swollen arm?', 'Symptoms', 'Scale', 1),
('Does your swollen arm feel weak?', 'Symptoms', 'Scale', 1),
('Does your swollen arm feel heavy?', 'Symptoms', 'Scale', 1),
('Do you feel tired?', 'Symptoms', 'Scale', 1),

-- Emotion
('In the past week have you had trouble sleeping?', 'Emotion', 'Scale', 1),
('In the past week have you had difficulty concentrating on things like reading?', 'Emotion', 'Scale', 1),
('In the past week have you felt tense?', 'Emotion', 'Scale', 1),
('In the past week have you felt worried?', 'Emotion', 'Scale', 1),
('In the past week have you felt irritable?', 'Emotion', 'Scale', 1),
('In the past week have you felt depressed?', 'Emotion', 'Scale', 1),

-- Overall Quality of Life
('Overall, how would you rate your quality of life at present?', 'Quality of Life', 'Scale', 1);

INSERT INTO UserQuestionnaires (QuestionnaireID, UserID, QuestionnaireStartDate)
VALUES (1, 2, NOW());

INSERT INTO UserWidgets (UserID, WidgetName, Position)
VALUES (2, 'task-completion', 1);