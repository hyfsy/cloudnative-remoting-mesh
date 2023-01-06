package com.hyf.cloudnative.remoting.mesh.proxy.http.file;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * copy from {@link org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile}
 */
public class MultipartFileAdapter implements MultipartFile {

    private final Part part;

    public MultipartFileAdapter(Part part) {
        this.part = part;
    }

    @Override
    public String getName() {
        return part.getName();
    }

    @Override
    public String getOriginalFilename() {
        return part.getSubmittedFileName();
    }

    @Override
    public String getContentType() {
        return part.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return part.getSize() == 0;
    }

    @Override
    public long getSize() {
        return part.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileCopyUtils.copyToByteArray(this.part.getInputStream());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return part.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        this.part.write(dest.getPath());
        if (dest.isAbsolute() && !dest.exists()) {
            // Servlet 3.0 Part.write is not guaranteed to support absolute file paths:
            // may translate the given path to a relative location within a temp dir
            // (e.g. on Jetty whereas Tomcat and Undertow detect absolute paths).
            // At least we offloaded the file from memory storage; it'll get deleted
            // from the temp dir eventually in any case. And for our user's purposes,
            // we can manually copy it to the requested location as a fallback.
            FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest.toPath()));
        }
    }
}
