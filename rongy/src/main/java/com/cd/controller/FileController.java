package com.cd.controller;

import com.cd.common.ImageTypeConstants;
import com.cd.common.RestResult;
import com.cd.common.ResultUtil;
import com.cd.entity.vo.UploadImgVO;
import com.cd.enums.ErrorCodeEnum;
import com.cd.exception.BusiException;
import com.cd.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${image-file-path}")
    private String imageFilePath;

    @Value("${image-file-addr}")
    private String imageFileAddr;

    @PostMapping(value = "/upload")
    public RestResult uploadFile(HttpServletRequest request, @RequestParam("img")MultipartFile file) {
        String originName = file.getOriginalFilename();

        logger.info("upload file: {}", originName);

        String suffix = validImageType(originName);

        String newFilename = String.format("%s.%s", CommonUtil.uniqueNo(), suffix);

        logger.info("file type {} permitted, could upload", suffix);

        // store file
        File baseFolder = new File(imageFilePath);

        if (!baseFolder.exists()) {
            logger.warn("file path {} not exist, create first", baseFolder);
            // if folder not exists, mkdir -p first
            if (!baseFolder.mkdirs()) {
                logger.error("create folder {} failed", baseFolder);

                throw new BusiException(ErrorCodeEnum.FILE_SYSTEM_ERROR);
            }

            logger.info("create folder {} success", baseFolder);
        }

        // store file
        String filePath = String.format("%s%s", imageFilePath, newFilename);

        try {
            file.transferTo(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("store file {} to {} failed", originName, filePath, e );

            throw new BusiException(ErrorCodeEnum.FILE_SYSTEM_ERROR);
        }

        return ResultUtil.createSuccessResult(new UploadImgVO(imageFileAddr + newFilename));
    }

    private String validImageType(String originName) {
        String lowerName = originName.toLowerCase();

        int dotLastIdx = lowerName.lastIndexOf(".");

        if (dotLastIdx == -1 || dotLastIdx == originName.length() - 1) {
            throw new BusiException(ErrorCodeEnum.FILE_TYPE_NOT_SUPPORT);
        }

        String suffix = lowerName.substring(dotLastIdx + 1);

        if (ImageTypeConstants.TYPES_LIST.contains(suffix)) {
            return suffix;
        }

        throw new BusiException(ErrorCodeEnum.FILE_TYPE_NOT_SUPPORT);
    }



}
