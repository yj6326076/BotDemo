package com.example.simple.bot.controller;

import com.example.simple.bot.service.GetLiveRomeInfo;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author yj632
 */
@RestController
@RequestMapping("live-rome")
public class LiveRomeController {
    @Inject
    GetLiveRomeInfo getLiveRomeInfo;

    @PostMapping("find")
    public String getRomeInfo(@RequestBody Long romeId) throws IOException, ParseException {
        return getLiveRomeInfo.getLiveRomeInfo(romeId).getData().formatRomeInfo();
    }
}
