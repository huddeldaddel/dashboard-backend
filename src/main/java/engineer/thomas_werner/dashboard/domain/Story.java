package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Story {

    @Id
    public String id;

    @JsonProperty("current_state")
    public String currentState;

    public Integer estimate;

    public String kind;

}
