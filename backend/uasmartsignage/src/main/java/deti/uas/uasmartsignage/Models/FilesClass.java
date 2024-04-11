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

    private CustomFile parent;

    private MultipartFile file;

    @Override
    public String toString() {
        return "FilesClass{" +
                ", parent=" + parent +
                ", file=" + file +
                '}';
    }
}
