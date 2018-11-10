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
    private String currentState;
    private Integer estimate;
    private String kind;

    @JsonGetter("current_state")
    public String getCurrentState() {
        return currentState;
    }

    @JsonSetter("current_state")
    public void setCurrentState(final String value) {
        currentState = value;
    }

}
