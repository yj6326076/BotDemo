package com.example.simple.danmu.utils;

import com.example.simple.danmu.entity.DamnEntity;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern TYPE_PATTERN = Pattern.compile("type@=(\\w*)");
    private static final Pattern NAME_PATTERN = Pattern.compile("nn@=([^/]*)");
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("txt@=([^/]*)");
    private static final Pattern TITLE_PATTERN = Pattern.compile("bnn@=([^/]*)");
    private static final Pattern ID_PATTERN = Pattern.compile("uid@=([^/]*)");

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

    public static DamnEntity transform(ByteBuffer byteBuffer) {
        byte[] array = byteBuffer.array();
        byte[] subarray = ArrayUtils.subarray(array, 12, array.length - 1);
        CharBuffer charBuffer = tarnsForm(subarray);
        DamnEntity damnEntity = new DamnEntity();
        String s = charBuffer.toString();
        Matcher matcher = TYPE_PATTERN.matcher(s);
        if (!matcher.find()) {
            return null;
        }
        String type = matcher.group(1);
        if (!type.equals("chatmsg")) {
            return null;
        }
        Matcher matcher1 = NAME_PATTERN.matcher(s);
        if (matcher1.find()) {
            damnEntity.setName(matcher1.group(1));
        }
        matcher1 = MESSAGE_PATTERN.matcher(s);
        if (matcher1.find()) {
            damnEntity.setMessage(matcher1.group(1));
        }
        matcher1 = TITLE_PATTERN.matcher(s);
        if (matcher1.find()) {
            damnEntity.setTitle(matcher1.group(1));
        }
        matcher1 = ID_PATTERN.matcher(s);
        if (matcher1.find()) {
            damnEntity.setSenderId(matcher1.group(1));
        }
        return damnEntity;
    }
}
