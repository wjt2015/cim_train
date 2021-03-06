package wjt.controller;

import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import wjt.model.ApiResult;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 常用功能测试;
 */
@Slf4j
@RestController
public class HomeController {

    private static final String FILE_SAVE_DIR = "docs/file_dir/";

    @RequestMapping(value = {"/home.json"})
    public ApiResult home() {
        ApiResult apiResult = new ApiResult(0, "success", "test");

        return apiResult;
    }

    @RequestMapping(value = {"/v1/publish.json"})
    public ApiResult publish(@RequestBody PublishRequest publishRequest) {
        log.info("publishRequest={};", publishRequest);

        return new ApiResult(0, "success", "succ");
    }

    @RequestMapping(value = "/detail.json")
    public ApiResult detail(@RequestParam("selected") String selected) {
        log.info("selected={};", selected);
        return new ApiResult(0, "ok", selected);
    }


    /**
     * 文件上传;
     * [
     * curl -F file=@abc.png http://localhost:8085/cim_train/upload.json
     * <p>
     * ]
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/upload.json"})
    public ApiResult upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getName();
        String originalFilename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        long size = multipartFile.getSize();
        log.info("fileName={};originalFilename={};contentType={};size={};", fileName, originalFilename, contentType, size);

        multipartFile.transferTo(new File(FILE_SAVE_DIR + originalFilename));
        return new ApiResult(0, "success", originalFilename);
    }

    /**
     * [
     * curl -F file2=@从零开始搭建IM.pdf http://localhost:8085/cim_train/upload2.json
     * <p>
     * ]
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = {"/upload2.json"})
    public ApiResult upload2(@RequestPart("file2") MultipartFile multipartFile) {
        String name = multipartFile.getName();
        String originalFilename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        long size = multipartFile.getSize();
        log.info("name={};originalFilename={};contentType={};size={};", name, originalFilename, contentType, size);
        return new ApiResult(0, "success", originalFilename);
    }

    /**
     * 下载;
     * [
     *
     * http://localhost:8085/cim_train/download.json?fileName=123
     * ]
     *
     * @param fileName
     * @param httpServletResponse
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/download.json"})
    public ApiResult download(@RequestParam("fileName") String fileName, HttpServletResponse httpServletResponse) throws IOException {

        //docs/file_dir/从零开始搭建瓜子IM系_滴滴技术沙龙第7期.pdf
        //String downLoadFileName = "docs/db.txt";
        String downLoadFileName = "docs/file_dir/从零开始搭建瓜子IM系_滴滴技术沙龙第7期.pdf";
//
        String contentDisposition = new StringBuilder().append("form-data; name=\"").append(downLoadFileName).append("\"")
                .append("; filename=").append("\"").append(downLoadFileName).append("\"")
                .toString();
        //下载,浏览器不打开;
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION,contentDisposition);
        FileUtils.copyFile(new File(downLoadFileName), httpServletResponse.getOutputStream());
        log.info("fileName={};downLoadFileName={};copy finish!", fileName, downLoadFileName);
        return new ApiResult(0, "ok", downLoadFileName);
    }


}
