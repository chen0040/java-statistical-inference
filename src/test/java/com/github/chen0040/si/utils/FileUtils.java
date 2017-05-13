package com.github.chen0040.si.utils;


import java.io.IOException;
import java.io.InputStream;


/**
 * Created by xschen on 13/5/2017.
 */
public class FileUtils {
   public static InputStream getResource(String filename) throws IOException {
      return FileUtils.class.getClassLoader().getResource(filename).openStream();
   }

}
