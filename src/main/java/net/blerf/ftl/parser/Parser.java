package net.blerf.ftl.parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class Parser {

    private static final byte[] intbuf = new byte[4];
    /**
     * Toggles string encoding between windows-1252 (default) and UTF-8.
     * <p>
     * Set this before reading/writing any strings.
     * <p>
     * Unicode strings were introduced in FTL 1.6.1.
     */
    protected boolean unicodeStrings = false;


    /**
     * Reads a little-endian int from a stream, as a boolean.
     */
    protected boolean readBool(InputStream in) throws IOException {
        int i = readInt(in);

        if (!(i == 1 || i == 0)) {
            throw new IOException("Not a bool: " + i);
        }

        return i == 1;
    }

    /**
     * Writes a boolean to a stream as a little-endian int.
     */
    protected void writeBool(OutputStream out, boolean b) throws IOException {
        writeInt(out, (b ? 1 : 0));
    }

    /**
     * Reads a little-endian int from a stream.
     */
    protected static int readInt(InputStream in) throws IOException {
        int numRead = 0;
        int offset = 0;
        while (offset < intbuf.length && (numRead = in.read(intbuf, offset, intbuf.length)) >= 0) {
            offset += numRead;
        }
        if (offset < intbuf.length) {
            throw new IOException("End of stream reached before reading enough bytes for an int");
        }

        int v = 0;
        for (int i = 0; i < intbuf.length; i++) {
            v |= ((intbuf[i]) & 0xff) << (i * 8);
        }

        return v;
    }

    /**
     * Writes a little-endian int to a stream.
     */
    protected static void writeInt(OutputStream out, int value) throws IOException {
        for (int i = 0; i < intbuf.length; i++) {
            intbuf[i] = (byte) (value >> (i * 8));
        }

        out.write(intbuf);
    }

    /**
     * Reads a little-endian int bytecount + string from a stream.
     * <p>
     * Note: In unicode, bytecount != string length.
     *
     * @see #setUnicode(boolean)
     */
    protected String readString(InputStream in) throws IOException {
        int length = readInt(in);

        // Avoid allocating a rediculous array size.
        // But InputStreams don't universally track position/size.
        // And available() might only mean blocking, not the end.
        // So try some special cases...
        if (in instanceof FileInputStream) {
            FileInputStream fin = (FileInputStream) in;
            long position = fin.getChannel().position();
            if (position + length > fin.getChannel().size()) {
                throw new IOException(String.format("Expected string length (%d) would extend beyond the end of the stream, from current position (%d)", length, position));
            }
        } else {
            // Call available on streams that really end.
            int remaining = -1;
            if (in instanceof ByteArrayInputStream) {
                remaining = ((ByteArrayInputStream) in).available();
            }
            if (remaining != -1 && length > remaining) {
                throw new IOException(String.format("Expected string length (%d) would extend beyond the end of the stream", length));
            }
        }

        int numRead = 0;
        int offset = 0;
        byte[] strBytes = new byte[length];
        while (offset < strBytes.length && (numRead = in.read(strBytes, offset, strBytes.length)) >= 0)
            offset += numRead;

        if (offset < strBytes.length) {
            throw new IOException(String.format("End of stream reached before reading enough bytes for string of length %d", length));
        }

        if (unicodeStrings) {
            return new String(strBytes, StandardCharsets.UTF_8);
        } else {
            return new String(strBytes, "windows-1252");
        }
    }

    protected void writeString(OutputStream out, String str) throws IOException {
        byte[] strBytes;
        if (unicodeStrings) {
            strBytes = str.getBytes(StandardCharsets.UTF_8);
        } else {
            strBytes = str.getBytes("windows-1252");
        }

        writeInt(out, strBytes.length);
        out.write(strBytes);
    }


    public void setUnicode(boolean b) {
        unicodeStrings = b;
    }

    protected boolean isUnicode() {
        return unicodeStrings;
    }
}
