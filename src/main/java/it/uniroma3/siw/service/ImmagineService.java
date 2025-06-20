package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Immagine;
import it.uniroma3.siw.repository.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImmagineService {

    @Autowired
    private ImmagineRepository immagineRepository;

    public void saveImmagine(MultipartFile file) throws Exception {
        Immagine img = new Immagine();
        img.setNomeFile(file.getOriginalFilename());
        img.setTipoMime(file.getContentType());
        img.setDati(file.getBytes());
        immagineRepository.save(img);
    }

    public Immagine getImmagineById(Long id){
        return immagineRepository.findById(id).get();
    }
}
