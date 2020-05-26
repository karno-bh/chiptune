package org.sm.tests;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.InflaterInputStream;

public class TestDeflate001 {

    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("muzfubit.fym.raw");
        FileInputStream fis = new FileInputStream("c:\\Users\\sergeymo\\Downloads\\muzfubit.fym");
        InflaterInputStream iis = new InflaterInputStream(fis);
        int aByte;
        while ((aByte = iis.read()) !=  -1) {
            fos.write(aByte);
        }
        fos.flush();
        fos.close();
        fis.close();
    }
}
