package com.db.service;

import com.db.POJO.Picture;
import com.db.repository.PicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureService {

    @Autowired
    PicturesRepository picturesRepository;

    public List<Picture> findAll() {
        return picturesRepository.findAll();
    }

    public Picture findById(int id) {
        return picturesRepository.findById(id).get();
    }

    public Picture insert(Picture picture) {
        return picturesRepository.save(picture);
    }

    public Picture update(Picture picture) {
        Picture picture1 = this.findById(picture.getId());
        picture1.setName(picture.getName());
        return picturesRepository.save(picture1);
    }

    public Picture delete(int id) {
        Picture picture = this.findById(id);
        picturesRepository.delete(picture);
        return picture;
    }
}
