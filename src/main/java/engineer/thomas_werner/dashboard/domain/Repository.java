package engineer.thomas_werner.dashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Repository {

    private String name;
    private List<PullRequest> pullRequests;

}
