package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequest {

    private String createdAt;
    private PullRequestHead head;
    private String title;
    private PullRequestUser user;
    private List<Label> labels;

    @JsonGetter("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonSetter("created_at")
    public void setCreatedAt(String value) {
        createdAt = value;
    }

}