package de.agilecoders.wicket.less;

import com.github.sommeri.less4j.LessSource;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.resource.AbstractStringResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Time;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A IResourceStream that loads the generated CSS content for Less resources
 */
public class LessResourceStream extends AbstractStringResourceStream {

    /**
     * The LessSource for the root Less resource.
     * Any LessSource can have children resources - imported resources
     */
    private final LessSource.URLSource lessSource;

    private final List<LessSource.URLSource> imports;

    /**
     * Constructor.
     *
     * @param lessStream The resource stream that loads the Less content. Only UrlResourceStream is supported at the moment!
     */
    public LessResourceStream(IResourceStream lessStream, List<IResourceStream> imports) {
        Args.notNull(lessStream, "lessStream");
        Args.notNull(imports, "imports");

        if (!(lessStream instanceof UrlResourceStream)) {
            throw new IllegalArgumentException(String.format("%s can work only with %s",
                LessResourceStream.class.getSimpleName(), UrlResourceStream.class.getName()));
        }

        URL lessUrl = ((UrlResourceStream) lessStream).getURL();

        LessCacheManager cacheManager = LessCacheManager.get();

        this.lessSource = cacheManager.getLessSource(lessUrl);
        this.imports = new ArrayList<LessSource.URLSource>();
        for (IResourceStream importStream : imports) {
            if (importStream instanceof UrlResourceStream) {
                UrlResourceStream urlResourceStream = (UrlResourceStream) importStream;
                URL importUrl = urlResourceStream.getURL();
                this.imports.add(cacheManager.getLessSource(importUrl));
            } else {
                throw new IllegalArgumentException(String.format("%s can work only with %s",
                    LessResourceStream.class.getSimpleName(), UrlResourceStream.class.getName()));
            }
        }
    }

    @Override
    protected String getString() {
        LessCacheManager cacheManager = LessCacheManager.get();
        return cacheManager.getCss(lessSource);
    }

    @Override
    public Time lastModifiedTime() {
        LessCacheManager cacheManager = LessCacheManager.get();
        return cacheManager.getLastModifiedTime(lessSource);
    }

    @Override
    public String getContentType() {
        return "text/css";
    }
}
