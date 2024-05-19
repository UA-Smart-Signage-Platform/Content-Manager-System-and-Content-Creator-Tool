package deti.uas.uasmartsignage.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilesClass {

    private Long parentId;

    private MultipartFile file;

    @Override
    public String toString() {
        return "FilesClass{" +
                "parent=" + parentId +
                ", file=" + file +
                '}';
    }
}
