import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class StegHide {
    void embed(String message, String source_image) {
        byte input[] = message.getBytes();
        BufferedImage img = null;
        File f = null;
        try {
            f = new File(source_image);
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println("Error:" + e);
            return;
        }
        int message_size = input.length;
        int p = img.getRGB(0, 0);
        int a = (p >> 24) & 0xff;
        int r = (p >> 16) & 0xff;
        int g = (p >> 8) & 0xff;
        int b = p & 0xff;
        a = ((a >> 2) << 2) | ((message_size >> 6) & 3);
        r = ((r >> 2) << 2) | ((message_size >> 4) & 3);
        g = ((g >> 2) << 2) | ((message_size >> 2) & 3);
        b = ((b >> 2) << 2) | (message_size & 3);
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(0, 0, p);
        int width = img.getWidth();
        int height = img.getHeight();
        int chars_written = 0;
        for (int x = 0; x < width; ++x) {
            if (chars_written == message_size) break;
            for (int y = 0; y < height; ++y) {
                if (x == 0 && y == 0) continue;
                if (chars_written == message_size) break;
                p = img.getRGB(x, y);
                a = (p >> 24) & 0xff;
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;
                a = ((a >> 2) << 2) | ((input[chars_written] >> 6) & 3);
                r = ((r >> 2) << 2) | ((input[chars_written] >> 4) & 3);
                g = ((g >> 2) << 2) | ((input[chars_written] >> 2) & 3);
                b = ((b >> 2) << 2) | (input[chars_written] & 3);
                p = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, p);
                ++chars_written;
            }
        }
        try {
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            System.out.println("Error: " + e);
            return;
        }
        System.out.println("Message successfully embedded.");
    }

    void extract(String stegofile) {
        int message_size = 0;
        BufferedImage img = null;
        File f = null;
        try {
            f = new File(stegofile);
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println("Error :" + e);
            return;
        }
        int p = img.getRGB(0, 0);
        int a = ((p >> 24) & 0xff) & 3;
        int r = ((p >> 16) & 0xff) & 3;
        int g = ((p >> 8) & 0xff) & 3;
        int b = (p & 0xff) & 3;
        message_size = (a << 6) | (r << 4) | (g << 2) | b;
        int ch = 0;
        int chars_read = 0;
        int width = img.getWidth();
        int height = img.getHeight();
        System.out.print("Message is: ");
        for (int x = 0; x < width; ++x) {
            if (chars_read == message_size) break;
            for (int y = 0; y < height; ++y) {
                if (x == 0 && y == 0) continue;
                if (chars_read == message_size) break;
                p = img.getRGB(x, y);
                a = ((p >> 24) & 0xff)&3;
                r = ((p >> 16) & 0xff)&3;
                g = ((p >> 8) & 0xff)&3;
                b = (p & 0xff)&3;
                ch = (a << 6) | (r << 4) | (g << 2) | b;
                System.out.print((char) ch);
                ++chars_read;
            }
        }

        System.out.println("\nMessage extracted successfully.");
    }
}
