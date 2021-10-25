package org.demo.learn.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author luwt
 * @date 2021/5/25.
 */
public interface FileService {

    void uploadFile(MultipartFile file);

    void uploadFileArr(MultipartFile[] files);

    void uploadFiles(List<MultipartFile> files);
}
