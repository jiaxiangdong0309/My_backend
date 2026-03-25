package com.peanut.trace.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 花生产品溯源系统 SHA-256 哈希校验工具类
 * 实现原始数据、文件数据的哈希计算及哈希值比对
 */
@Component
public class SHA256HashUtil {

    private static final String ALGORITHM = "SHA-256";

    /**
     * 计算字符串类型原始数据的 SHA-256 哈希值
     * 适配种植、加工等环节的结构化表单数据
     *
     * @param rawData 原始字符串数据（如 JSON 格式的种植记录）
     * @return 64 位十六进制哈希字符串
     */
    public String calculateStrHash(String rawData) throws NoSuchAlgorithmException {
        if (rawData == null || rawData.trim().isEmpty()) {
            throw new IllegalArgumentException("原始数据不能为空");
        }
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        byte[] hashBytes = md.digest(rawData.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hashBytes);
    }

    /**
     * 计算文件类型数据的 SHA-256 哈希值
     * 适配质检报告、检测证书等文件类溯源数据
     *
     * @param file 待计算的文件
     * @return 64 位十六进制哈希字符串
     */
    public String calculateFileHash(File file) throws NoSuchAlgorithmException, IOException {
        if (file == null || !file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("文件不存在或为目录");
        }
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
        }
        return Hex.encodeHexString(md.digest());
    }

    /**
     * 哈希值比对验证，判断链下数据是否被篡改
     *
     * @param rawDataHash 链下计算的原始数据哈希值
     * @param chainHash   区块链智能合约中存储的哈希值
     * @return true-哈希一致（数据未篡改），false-哈希不一致（数据已篡改）
     */
    public boolean compareHash(String rawDataHash, String chainHash) {
        if (rawDataHash == null || chainHash == null) {
            return false;
        }
        return rawDataHash.trim().equalsIgnoreCase(chainHash.trim());
    }

    /**
     * 重载：直接对原始数据进行哈希计算并与链上哈希比对
     *
     * @param rawData   链下原始字符串数据
     * @param chainHash 区块链智能合约中存储的哈希值
     * @return true-数据未篡改，false-数据已篡改
     */
    public boolean verifyHash(String rawData, String chainHash) throws NoSuchAlgorithmException {
        String calculatedHash = this.calculateStrHash(rawData);
        return this.compareHash(calculatedHash, chainHash);
    }
}
