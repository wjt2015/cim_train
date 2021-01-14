package wjt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wjt.model.ApiResult;
import wjt.service.SessionService;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Slf4j
@RestController
public class CheckController {

    private static final Random RANDOM = new Random();
    private static final int COUNT = 4, IMAGE_WIDTH = 120, IMAGE_HEIGHT = 80;
    private static final String CHECK_CODE = "CHECK_CODE";

    @Resource
    private SessionService sessionService;

    /**
     * 生成验证码图片;
     *
     * @return
     */
    @RequestMapping(value = "/checkCodePic.json")
    public ApiResult checkCodePic(@RequestParam("id") long id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        BufferedImage bufferedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.setColor(Color.RED);
        g2d.setFont(new Font(Font.DIALOG, Font.BOLD | Font.ITALIC, 60));
        StringBuilder checkCodeBuilder = new StringBuilder();
        for (int i = 0; i < COUNT; i++) {
            String s = String.valueOf(RANDOM.nextInt(10));
            checkCodeBuilder.append(s);
            g2d.drawString(s, IMAGE_WIDTH * i / COUNT, IMAGE_HEIGHT * 4 / 5);
        }
        /**
         * 将验证码数据以图片形式写入response;
         */
        ImageIO.write(bufferedImage, "jpeg", httpServletResponse.getOutputStream());
        /**
         * 将验证码加入会话;
         */
        final HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute(CHECK_CODE, checkCodeBuilder.toString());

        log.info("id={};checkCodePic finish!", id);
        return new ApiResult(0, "ok", "checkCode");
    }

    @RequestMapping(value = "/codeCheck.json")
    public ApiResult codeCheck(HttpServletRequest httpServletRequest){

        return new ApiResult(0,"ok",true);
    }

}
