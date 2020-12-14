package wjt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wjt.model.ApiResult;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping(value = {"/home.json"})
    @ResponseBody
    public ApiResult home() {
        ApiResult apiResult = new ApiResult(0, "success", "test");

        return apiResult;
    }

    @RequestMapping(value = {"/v1/publish.json"})
    @ResponseBody
    public ApiResult publish(@RequestBody PublishRequest publishRequest) {
        log.info("publishRequest={};", publishRequest);

        return new ApiResult(0, "success", "succ");
    }

}
