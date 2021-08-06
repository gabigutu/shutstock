package com.db.controller;

import com.db.POJO.Picture;
import com.db.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PictureController {

    @Autowired
    PictureService pictureService;

    @GetMapping("/pictures")
    public List<Picture> getAllPictures() {
        return pictureService.findAll();
    }

    @GetMapping("/pictures/{id}")
    public Picture getPictureById(@PathVariable(name = "id") Integer id){
        return pictureService.findById(id);
    }

    @PutMapping("/pictures")
    public Picture insertPicture(@RequestBody Picture picture) {
        return pictureService.insert(picture);
    }

    @PutMapping("/pictures/{id}")
    public Picture updatePicture(@RequestBody Picture picture,@PathVariable(name = "id") Integer id) {
        picture.setId(id);
        return pictureService.update(picture);
    }

    @DeleteMapping("/pictures/{id}")
    public Picture deletePicture(@PathVariable(name = "id") Integer id) {
        return pictureService.delete(id);
    }
}
