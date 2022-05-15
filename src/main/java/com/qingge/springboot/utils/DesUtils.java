package com.qingge.springboot.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @description:
 * @author: waxsq
 * @date:
 */
public class DesUtils {




    public static String getInfo()
    {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
        String secretKey = new BASE64Encoder().encodeBuffer(key);
        return secretKey;
    }

    //info:需要加密的明文
    public static String encrypt(String info,String secretKey) {
        byte[] key = new byte[0];
        try {
            key = new BASE64Decoder().decodeBuffer(secretKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DES des = SecureUtil.des(key);
        String encrypt = des.encryptHex(info);
        return encrypt;
    }


    //encrypt:需要解密的密文
    public static String decode(String encrypt,String secretKey) {
        byte[] key = new byte[0];
        try {
            key = new BASE64Decoder().decodeBuffer(secretKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DES des = SecureUtil.des(key);
        return des.decryptStr(encrypt);
    }
}
