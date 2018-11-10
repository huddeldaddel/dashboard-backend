package engineer.thomas_werner.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Label {

    private Long id;
    private String name;
    private String description;
    private String color;

}
