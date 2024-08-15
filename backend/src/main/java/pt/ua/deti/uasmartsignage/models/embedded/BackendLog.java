package pt.ua.deti.uasmartsignage.models.embedded;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.ua.deti.uasmartsignage.enums.Severity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BackendLog {

        private String module;

        private String operation;

        private String description;

        private Severity severity;

        private String timestamp;

        private String user;

        @Override
        public String toString() {
            return "BackendLog{" +
                    "user='" + user + '\'' +
                    "module='" + module + '\'' +
                    ", operation='" + operation + '\'' +
                    ", description='" + description + '\'' +
                    ", severity='" + severity + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
}
