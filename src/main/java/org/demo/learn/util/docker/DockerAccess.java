package org.demo.learn.util.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author luwt
 * @date 2020/12/11.
 */
public class DockerAccess {

    private static DockerClient dockerClient;

    public static DockerClient getDockerClient(){
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://centos122:2376")
                .withDockerTlsVerify(true)
                .withDockerCertPath("D:\\cert")
                .build();
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        dockerClient = DockerClientImpl.getInstance(config, httpClient);
        return dockerClient;
    }


    public static void main(String[] args) throws Exception {
        dockerClient = getDockerClient();
//        getInfo();
        String repo = "harbor.cvhub.com/library/test", tag = "v1";
//        changeTag(repo, tag);
//        pullImage("harbor.cvhub.com/library/cvhub:base");
        getImages();
//        saveImage();
//        loadImage();
//        dockerClient.close();
        //pushImage(repo + ":" + tag);
        buildImage();
    }

    public static void buildImage()throws Exception {
        System.out.println("------------------??????????????????---------------------");
        File baseDir = fileFromBuildTestResource("D:/test/");
        Collection<File> files = FileUtils.listFiles(baseDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        File tarFile = CompressArchiveUtil.archiveTARFiles(baseDir, files, UUID.randomUUID().toString());
        String imageId = dockerClient.buildImageCmd(new FileInputStream(tarFile))
//                .withDockerfile(new File("D:/test/file"))
                .withTags(Sets.newHashSet("java-docker-test:v1"))
                .start()
                .awaitImageId();
        System.out.println(imageId);
        System.out.println("------------------??????????????????---------------------");
    }

    private static File fileFromBuildTestResource(String resource) {
        return new File(resource);
    }

    public static void getInfo() {
        Info info = dockerClient.infoCmd().exec();
        System.out.println("------------------??????????????????---------------------");
        System.out.println(info);
        System.out.println("------------------??????????????????---------------------");
    }

    public static void getImages() {
        List<Image> images = dockerClient.listImagesCmd().exec();
        System.out.println("------------------????????????????????????---------------------");
        images.forEach(System.out::println);
        System.out.println("------------------????????????????????????---------------------");
    }

    public static void changeTag(String repo, String tag) {
        // id????????????sha256???+id???????????????id???????????????id
        dockerClient.tagImageCmd("76575dbe8b3e", repo, tag).exec();
    }

    public static void loadImage() {
        String path = "D:\\workspace\\learn\\python.tar";
        try (InputStream uploadStream = Files.newInputStream(Paths.get(path))) {
            System.out.println("----????????????----");
            Void exec = dockerClient.loadImageCmd(uploadStream).exec();
            System.out.println(exec);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("----????????????----");
        }
    }

    public static void saveImage() {
        InputStream input = dockerClient.saveImageCmd("harbor.cvhub.com/library/test:v1").exec();
        try {
            OutputStream out = new FileOutputStream("D:\\workspace\\learn\\testv1.tar");
            byte[] temp = new byte[1024];
            int length = 0;
            // ??????????????????????????????
            while ((length = input.read(temp)) != -1) {
                // ?????????????????????????????????
                out.write(temp, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pullImage(String repo) {
        AuthConfig authConfig = new AuthConfig()
                // harbor????????????
                .withUsername("admin")
                .withPassword("Harbor12345");
        //    .withRegistryAddress("centos122:" + 2376);

        System.out.println("repo:" + repo);


        try {
            boolean b = dockerClient.pullImageCmd(repo).withAuthConfig(authConfig).start().awaitCompletion(20L, TimeUnit.SECONDS);
            System.out.println(b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pushImage(String name) {
        AuthConfig authConfig = new AuthConfig()
                // harbor????????????
                .withUsername("admin")
                .withPassword("Harbor12345");

        System.out.println("name:" + name);
        PushImageCmd pushImageCmd = dockerClient.pushImageCmd(name);
        System.out.println("pushImageCmd" + pushImageCmd);
        ResultCallback.Adapter<PushResponseItem> start = pushImageCmd.withAuthConfig(authConfig).start();
        boolean b = false;
        try {
            b = start.awaitCompletion(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(b);


    }
}
