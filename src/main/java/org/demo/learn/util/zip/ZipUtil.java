package org.demo.learn.util.zip;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author luwt-a
 * @date 2023/9/5
 */
public class ZipUtil {

    public static void main(String[] args) {
        String zipFileName = "example.zip";
        String folderToCompress = "G:\\1693884534971";

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFileName)))) {
            // 压缩文件夹
            compressFolder(folderToCompress, "", zipOutputStream);

            System.out.println("Folder compressed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFolder(String sourceFolder, String folderName, ZipOutputStream zipOutputStream) throws IOException {
        File folder = new File(sourceFolder);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 压缩子文件夹
                    compressFolder(file.getAbsolutePath(), getFolderName(folderName) + file.getName(), zipOutputStream);
                } else {
                    // 压缩文件
                    addToZipFile(getFolderName(folderName) + file.getName(), file.getAbsolutePath(), zipOutputStream);
                }
            }
        }
    }

    private static String getFolderName(String folderName) {
        return StringUtils.isBlank(folderName) ? StringUtils.EMPTY : folderName + "/";
    }

    private static void addToZipFile(String fileName, String fileAbsolutePath, ZipOutputStream zipOutputStream) throws IOException {
        // 创建ZipEntry对象并设置文件名
        ZipEntry entry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(entry);

        // 读取文件内容并写入Zip文件
        try (FileInputStream fileInputStream = new FileInputStream(fileAbsolutePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }
        }
        // 完成当前文件的压缩
        zipOutputStream.closeEntry();
    }

}
