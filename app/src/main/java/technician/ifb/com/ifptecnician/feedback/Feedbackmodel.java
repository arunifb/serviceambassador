package technician.ifb.com.ifptecnician.feedback;

public class Feedbackmodel {

    String SurveyId,Title,Description,CreatedBy,QuestionId;

    public String getSurveyId() {
        return SurveyId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }
}
