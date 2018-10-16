package com.github.donmahallem.dota2gamefileapi;

import java.io.IOException;

import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

public abstract class ValveFormatToJson implements Source {

    private final static long BUFFER_READ_SIZE = 1024;
    private final static int START = 1, IN_TITLE = 2, IN_TAG = 3, IN_VALUE = 4;
    private final static ByteString NEW_LINE = ByteString.encodeUtf8("\n");
    private final Source mInputSource;
    private final Buffer mTempBuffer = new Buffer();

    public ValveFormatToJson(Source source) {
        this.mInputSource = source;
    }

    @Override
    public void close() throws IOException {
        this.mInputSource.close();
    }

    @Override
    public Timeout timeout() {
        return this.mInputSource.timeout();
    }

    @Override
    public long read(Buffer sink, final long byteCount) throws IOException {
        final long readBytes = this.mInputSource.read(this.mTempBuffer, 1024);
        long linebreak = -1;
        while ((linebreak = this.mTempBuffer.indexOf(NEW_LINE)) < 0) {
            final long readLength = this.mInputSource.read(this.mTempBuffer, BUFFER_READ_SIZE);
            if (readLength < BUFFER_READ_SIZE) {
                //FILE END
                break;
            }
        }
        return 0;
    }

}
