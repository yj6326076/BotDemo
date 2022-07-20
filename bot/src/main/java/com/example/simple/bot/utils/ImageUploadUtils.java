package com.example.simple.bot.utils;

import com.example.simple.bot.bo.RomeInfoVo;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yj632
 */
public class ImageUploadUtils {
    public static Image upload(RomeInfoVo romeInfoVo, Contact contact) throws IOException {
        String imageUrl = romeInfoVo.getImageUrl();
        if (!imageUrl.startsWith("http")) {
            imageUrl = "https://rpic.douyucdn.cn/" + imageUrl;
        }

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(imageUrl);
        CloseableHttpResponse response = client.execute(httpGet);
        InputStream content = response.getEntity().getContent();
        Image image = Contact.uploadImage(contact, content);
        content.close();
        response.close();
        content.close();
        return image;
    }
}
