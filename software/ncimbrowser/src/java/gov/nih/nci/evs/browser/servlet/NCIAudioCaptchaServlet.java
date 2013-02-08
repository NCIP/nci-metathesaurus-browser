package gov.nih.nci.evs.browser.servlet;

import nl.captcha.servlet.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.audio.AudioCaptcha;


/**
 * Generates a new {@link AudioCaptcha} and writes the audio to the response.
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 */
public class NCIAudioCaptchaServlet extends AudioCaptchaServlet {

    private static final long serialVersionUID = 4690256047223360040L;

    @Override protected void doGet(HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {

        AudioCaptcha ac = new AudioCaptcha.Builder()
            .addAnswer()
            //.addNoise()
            .build();

        req.getSession().setAttribute(AudioCaptcha.NAME, ac);
        CaptchaServletUtil.writeAudio(resp, ac.getChallenge());
    }

    @Override protected void doPost(HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
