package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

    @Id
    private String id;
    private String acceptedAt;
    private String createdAt;
    private String currentState;
    private Integer estimate;
    private String kind;
    private String storyType;

    @JsonGetter("accepted_at")
    public String getAcceptedAt() {
        return acceptedAt;
    }

    @JsonSetter("accepted_at")
    public void setAcceptedAt(final String value) {
        acceptedAt = value;
    }

    @JsonGetter("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonSetter("created_at")
    public void setCreatedAt(final String value) {
        createdAt = value;
    }

    @JsonGetter("current_state")
    public String getCurrentState() {
        return currentState;
    }

    @JsonSetter("current_state")
    public void setCurrentState(final String value) {
        currentState = value;
    }

    @JsonGetter("story_type")
    public String getStoryType() {
        return storyType;
    }

    @JsonSetter("story_type")
    public void setStoryType(final String value) {
        storyType = value;
    }

}
