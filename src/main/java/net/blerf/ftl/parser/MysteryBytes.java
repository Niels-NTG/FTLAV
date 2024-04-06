package net.blerf.ftl.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MysteryBytes {
    private static final byte[] HEX_CHARS = new byte[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };
    private long offset = -1;
    private byte[] bytes = null;

    public MysteryBytes(long offset, byte[] bytes) {
        this.offset = offset;
        this.bytes = bytes;
    }

    /**
     * Constructor using bytes from an InputStream.
     *
     * @param in     a stream to read from. Instances of
     *               FileInputStream will be interrogated for an offset.
     * @param length the number of bytes to read.
     */
    public MysteryBytes(InputStream in, int length) throws IOException {
        int numRead = 0;
        int unOffset = 0;
        byte[] unBytes = new byte[length];
        while (unOffset < unBytes.length && (numRead = in.read(unBytes, unOffset, unBytes.length)) >= 0) {
            unOffset += numRead;
        }

        if (in instanceof FileInputStream) {
            this.offset = ((FileInputStream) in).getChannel().position() - unBytes.length;
        }
        this.bytes = unBytes;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        StringBuilder ascii = new StringBuilder();

        if (offset >= 0)
            result.append(String.format("Location: %d-%d%n", offset, offset + bytes.length));
        else
            result.append(String.format("Length: %d%n", bytes.length));

        for (int i = 0; i < bytes.length; i++) {
            result.append((char) (HEX_CHARS[(bytes[i] & 0x00F0) >> 4]));
            result.append((char) (HEX_CHARS[(bytes[i] & 0x000F)]));

            char charValue = (char) bytes[i];
            if (charValue >= 32 && charValue < 127) {
                ascii.append(charValue);
            } else {
                ascii.append(".");
            }

            if (i < bytes.length - 1) {
                if (i % 16 == 15) {
                    result.append("   ").append(ascii);
                    result.append("\n");
                    ascii.setLength(0);
                } else if (i % 2 == 1) {
                    result.append(" ");
                }
            }
        }
        result.append("\n");
        return result.toString();
    }
}
