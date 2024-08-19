import { fetchData, postData } from "../common/utils/apiUtility.js";
import { QuestionnaireHubRenderer } from "./QuestionnaireHubRenderer.js";

export class QuestionnaireHubManager {
  constructor(containerId, noAssignmentId) {
    this.containerId = containerId;
    this.noAssignmentId = noAssignmentId;
    this.renderer = new QuestionnaireHubRenderer();
  }

  async loadAssignedQuestionnaires(patientId) {
    const endpoint = `/api/provider/${patientId}/assigned-questionnaires`;
    console.log(`Fetching assigned questionnaires from: ${endpoint}`);

    try {
      const questionnaires = await fetchData(endpoint);
      console.log(`Received questionnaires: `, questionnaires);

      if (!questionnaires || questionnaires.length === 0) {
        console.warn("No assigned questionnaires found.");
      }

      this.renderer.renderAssignedQuestionnaires(
        questionnaires,
        this.containerId,
        this.noAssignmentId
      );
    } catch (error) {
      console.error("Error loading assigned questionnaires:", error);
    }
  }

  async assignQuestionnaire(patientId, questionnaireId, dueDate) {
    const endpoint = `/api/provider/${patientId}/assign-questionnaire`;
    const data = { questionnaireId, dueDate };

    try {
      await postData(endpoint, data);
      alert("Questionnaire assigned successfully.");
      // Reload the assigned questionnaires
      await this.loadAssignedQuestionnaires(patientId);
    } catch (error) {
      console.error("Error assigning questionnaire:", error);
      alert("Failed to assign the questionnaire.");
    }
  }
}
