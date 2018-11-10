package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestHead {

    private String label;
    private PullRequestHeadRepo repo;

}
