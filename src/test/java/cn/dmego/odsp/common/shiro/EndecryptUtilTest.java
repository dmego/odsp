package cn.dmego.odsp.common.shiro;

import org.junit.Test;

import static org.junit.Assert.*;

public class EndecryptUtilTest {

    @Test
    public void encrytMd5() {
        String encrytMd5 = EndecryptUtil.encrytMd5("admin", "admin", 3);
        System.out.println(encrytMd5);

    }
}