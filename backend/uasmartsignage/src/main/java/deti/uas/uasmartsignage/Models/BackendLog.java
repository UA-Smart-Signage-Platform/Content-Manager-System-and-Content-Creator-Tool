package deti.uas.uasmartsignage.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BackendLog {

        private String module;

        private String operation;

        private String description;

        private String severity;

        private String timestamp;

        @Override
        public String toString() {
            return "BackendLog{" +
                    "module='" + module + '\'' +
                    ", operation='" + operation + '\'' +
                    ", description='" + description + '\'' +
                    ", severity='" + severity + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
}
