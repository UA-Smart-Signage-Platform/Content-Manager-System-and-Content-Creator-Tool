package deti.uas.uasmartsignage.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilesClass {

    private CustomFile parent;

    private List<CustomFile> subDirectories;

    private MultipartFile file;

    @Override
    public String toString() {
        return "FilesClass{" +
                ", parent=" + parent +
                ", subDirectories=" + subDirectories +
                ", file=" + file +
                '}';
    }
}
