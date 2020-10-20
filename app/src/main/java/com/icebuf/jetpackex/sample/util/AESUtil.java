package com.icebuf.jetpackex.sample.util;


import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/8/24
 * E-mail：bflyff@hotmail.com
 *
 * AES加密工具
 */
public class AESUtil{

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";//默认的加密算法

    /**
     * 可根据需要替换加密算法
     */
    private static String sCipherAlgorithm = DEFAULT_CIPHER_ALGORITHM;

    /**
     * 密钥长度
     */
    private static final int KEY_LENGTH = 16;
    /**
     * 默认填充位数
     */
    private static final String DEFAULT_VALUE = "0";


    public static void setCipherAlgorithm(String cipherAlgorithm) {
        sCipherAlgorithm = cipherAlgorithm;
    }

    /**
     * 加密
     * @param key 密钥
     * @param src 加密文本
     * @return 加密后的文本
     * @throws Exception
     */
    public static String encrypt(String key, String src) throws Exception {
        // 对源数据进行Base64编码
        src = Base64.encodeToString(src.getBytes(), Base64.DEFAULT);
        // 补全KEY为16位
        byte[] rawKey = toMakeKey(key, KEY_LENGTH, DEFAULT_VALUE).getBytes();
        // 获取加密后的字节数组
        byte[] result = getBytes(rawKey, src.getBytes(StandardCharsets.UTF_8), Cipher.ENCRYPT_MODE);
        // 对加密后的字节数组进行Base64编码
        result = Base64.encode(result, Base64.DEFAULT);
        // 返回字符串
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     * @param key 密钥
     * @param encrypted 待解密文本
     * @return 返回解密后的数据
     * @throws Exception
     */
    public static String decrypt(String key, String encrypted) throws Exception {
        // 补全KEY为16位
        byte[] rawKey = toMakeKey(key, KEY_LENGTH, DEFAULT_VALUE).getBytes();
        encrypted = encrypted.replace("\r\n", "");
        // 获取加密后的二进制字节数组
        byte[] enc = encrypted.getBytes(StandardCharsets.UTF_8);
        // 对二进制数组进行Base64解码
        enc = Base64.decode(enc, Base64.DEFAULT);
        // 获取解密后的二进制字节数组
        byte[] result = getBytes(rawKey, enc, Cipher.DECRYPT_MODE);
        // 对解密后的二进制数组进行Base64解码
        result = Base64.decode(result, Base64.DEFAULT);
        // 返回字符串
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 密钥key ,默认补的数字，补全16位数，以保证安全补全至少16位长度,android和ios对接通过
     * @param key 密钥key
     * @param length 密钥应有的长度
     * @param text 默认补的文本
     * @return 密钥
     */
    private static String toMakeKey(String key, int length, String text) {
        // 获取密钥长度
        int strLen = key.length();
        // 判断长度是否小于应有的长度
        if (strLen < length) {
            // 补全位数
            StringBuilder builder = new StringBuilder();
            // 将key添加至builder中
            builder.append(key);
            // 遍历添加默认文本
            for (int i = 0; i < length - strLen; i++) {
                builder.append(text);
            }
            // 赋值
            key = builder.toString();
        }
        return key;
    }

    /**
     * 加解密过程
     * 1. 通过密钥得到一个密钥专用的对象SecretKeySpec
     * 2. Cipher 加密算法，加密模式和填充方式三部分或指定加密算 (可以只用写算法然后用默认的其他方式)
     * @param key 二进制密钥数组
     * @param src 加解密的源二进制数据
     * @param mode 模式，加密为：Cipher.ENCRYPT_MODE;解密为：Cipher.DECRYPT_MODE
     * @return 加解密后的二进制数组
     * @throws NoSuchAlgorithmException 无效算法
     * @throws NoSuchPaddingException 无效填充
     * @throws InvalidKeyException 无效KEY
     * @throws InvalidAlgorithmParameterException 无效密钥
     * @throws IllegalBlockSizeException 非法块字节
     * @throws BadPaddingException 坏数据
     */
    private static byte[] getBytes(byte[] key, byte[] src, int mode)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // 密钥规格
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
        // 密钥实例
        Cipher cipher = Cipher.getInstance(sCipherAlgorithm);
        // 初始化密钥模式
        cipher.init(mode, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        // 加密数据
        return cipher.doFinal(src);
    }

}