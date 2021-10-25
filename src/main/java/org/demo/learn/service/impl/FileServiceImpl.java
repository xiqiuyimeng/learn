package org.demo.learn.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.demo.learn.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author luwt
 * @date 2021/5/25.
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public void uploadFile(MultipartFile file) {
        log.info("上传文件个数：1");
        log.info("上传文件名称：{}，文件大小：{}", file.getOriginalFilename(), file.getSize());
    }

    @Override
    public void uploadFileArr(MultipartFile[] files) {
        log.info("上传文件个数：{}", files.length);
        for (MultipartFile file : files) {
            log.info("上传文件名称：{}，文件大小：{}", file.getOriginalFilename(), file.getSize());
        }
    }

    @Override
    public void uploadFiles(List<MultipartFile> files) {
        log.info("上传文件个数：{}", files.size());
        for (MultipartFile file : files) {
            log.info("上传文件名称：{}，文件大小：{}", file.getOriginalFilename(), file.getSize());
        }
    }
}
