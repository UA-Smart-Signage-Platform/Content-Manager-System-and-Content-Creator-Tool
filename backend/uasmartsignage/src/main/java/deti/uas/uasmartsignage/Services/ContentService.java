package deti.uas.uasmartsignage.Services;

import org.springframework.stereotype.Service;

import deti.uas.uasmartsignage.Repositories.ContentRepository;

import org.springframework.beans.factory.annotation.Autowired;

import deti.uas.uasmartsignage.Models.Content;


@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    public Content saveContent(Content content) {
        return contentRepository.save(content);
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    public Content updateContent(Content content) {
        return contentRepository.save(content);
    }

    public Iterable<Content> getAllContents() {
        return contentRepository.findAll();
    }

    

    

    
}
