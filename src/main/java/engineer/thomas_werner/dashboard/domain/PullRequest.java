package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequest {

    private String id;
    private String createdAt;
    private String htmlUrl;
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

    @JsonGetter("html_url")
    public String getHtmlUrl() {
        return htmlUrl;
    }

    @JsonSetter("html_url")
    public void setHtmlUrl(String value) {
        htmlUrl = value;
    }

}
