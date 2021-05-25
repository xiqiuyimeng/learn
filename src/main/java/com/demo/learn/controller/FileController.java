package com.demo.learn.controller;

import com.demo.annotation.MyFile;
import com.demo.learn.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author luwt
 * @date 2021/5/25.
 */
@RestController
@RequestMapping(value = "file")
@ResponseStatus(HttpStatus.OK)
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("file")
    public String testFile(@MyFile(value = "file") MultipartFile file) throws Exception{
        fileService.uploadFile(file);
        return "ok";
    }

    @PostMapping("fileArr")
    public String testFileArr(@MyFile("files") MultipartFile[] files) throws Exception{
        fileService.uploadFileArr(files);
        return "ok";
    }

    @PostMapping("files")
    public String testFiles(@MyFile("files") List<MultipartFile> files) throws Exception{
        fileService.uploadFiles(files);
        return "ok";
    }
}
