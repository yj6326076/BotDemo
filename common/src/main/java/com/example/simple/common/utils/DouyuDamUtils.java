package com.example.simple.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 斗鱼弹幕工具类
 * @author yj632
 */
public class DouyuDamUtils {
    private static final String LOGIN = "type@=loginreq/roomid@=%d";
    private static final String JOIN = "type@=joingroup/rid@=%d/gid@=-9999/";
    private static final String HART_BEAT = "type@=mrkl/";
    private static final byte ZERO = 0;
    private static final byte B1 = (byte) 177;
    private static final byte TOW = 2;

    public static byte[] loginRome(Long romeId) {
        String login = String.format(LOGIN, romeId);
        byte[] bytes = login.getBytes(StandardCharsets.UTF_8);
        int total = bytes.length + 9;
        byte count = (byte) (total);
        return format(bytes,count);
    }

    public static byte[] join(Long romeId) {
        String login = String.format(JOIN, romeId);
        byte[] bytes = login.getBytes(StandardCharsets.UTF_8);
        int total = bytes.length + 9;
        byte count = (byte) (total);
        return format(bytes,count);
    }

    public static byte[] hartBeat() {
        byte[] bytes = HART_BEAT.getBytes(StandardCharsets.UTF_8);
        int total = bytes.length + 9;
        byte count = (byte) (total);
        return format(bytes,count);
    }

    private static byte[] format(byte[] bytes, byte total) {
        byte[] bytes1 = {total, ZERO, ZERO, ZERO, total, ZERO, ZERO, ZERO, B1, TOW, ZERO, ZERO};
        byte[] insert = ArrayUtils.addAll(bytes1,bytes);
        return ArrayUtils.add(insert, ZERO);
    }

    public static CharBuffer tarnsForm(byte[] bytes) {
        Charset charset = StandardCharsets.UTF_8;
        ByteBuffer allocate = ByteBuffer.wrap(bytes);
        return charset.decode(allocate);
    }
}
